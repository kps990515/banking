package org.com.kakaobank.service.transfer;

import com.github.f4b6a3.uuid.UuidCreator;
import feign.FeignException;
import org.com.kakaobank.client.dto.BankClientRequest;
import org.com.kakaobank.client.dto.BankClientResponse;
import org.com.kakaobank.client.feign.BankingApiClient;
import org.com.kakaobank.common.exception.AccountNotFoundException;
import org.com.kakaobank.common.exception.BankNotFoundException;
import org.com.kakaobank.common.retry.RetryHandler;
import org.com.kakaobank.domain.entity.AccountEntity;
import org.com.kakaobank.domain.entity.TransferEntity;
import org.com.kakaobank.domain.entity.TransferStatus;
import org.com.kakaobank.domain.repository.AccountRepository;
import org.com.kakaobank.domain.repository.RollbackFailureStateRepository;
import org.com.kakaobank.domain.repository.TransferRepository;
import org.com.kakaobank.service.dto.BankCodeEnum;
import org.com.kakaobank.service.dto.TransferServiceRequest;
import org.com.kakaobank.service.dto.TransferServiceResponse;
import org.com.kakaobank.service.mapper.BankClientRequestMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class TransferServiceImpl implements TransferService {

    private final Map<String, BankingApiClient> bankingApiClients; // 은행별 클라이언트 매핑
    private final TransferLogService transferLogService; // 송금 트랜잭션 로그 관리
    private final RetryHandler retryHandler; // 재시도 로직
    private final RollbackFailureStateRepository rollbackFailureStateRepository;
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    public TransferServiceImpl(Map<String, BankingApiClient> bankingApiClients,
                               TransferLogService transferLogService,
                               RetryHandler retryHandler,
                               RollbackFailureStateRepository rollbackFailureStateRepository,
                               TransferRepository transferRepository,
                               AccountRepository accountRepository) {
        this.bankingApiClients = bankingApiClients;
        this.transferLogService = transferLogService;
        this.retryHandler = retryHandler;
        this.rollbackFailureStateRepository = rollbackFailureStateRepository;
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * 송금 트랜잭션 처리
     * - 출금 → 입금 순으로 처리
     * - 실패 시 롤백 처리
     * @param request 송금 요청 데이터
     * @return TransferServiceResponse
     */
    @Transactional
    @Override
    public TransferServiceResponse processTransfer(TransferServiceRequest request) {
        String txID = UuidCreator.getTimeOrderedEpoch().toString();
        TransferEntity transferEntity = initializeTransferEntity(txID, request);

        try {
            String fromBank = request.getFromAccountBank();
            String toBank = request.getToAccountBank();

            // 출금 처리
            boolean withdrawalSuccess = performWithdrawal(txID, request, fromBank);
            if (!withdrawalSuccess) {
                transferEntity.setStatus(TransferStatus.FAILED);
                transferEntity.setMessage(fromBank + " 출금 실패");
                saveTransferEntity(transferEntity);
                return createFailureResponse(txID, fromBank + " 출금 실패");
            }

            // 입금 처리
            boolean depositSuccess = performDeposit(txID, request, toBank);
            if (!depositSuccess) {
                transferEntity.setStatus(TransferStatus.FAILED);
                transferEntity.setMessage(toBank + " 입금 실패");
                saveTransferEntity(transferEntity);

                // 롤백 처리
                boolean rollbackSuccess = rollbackWithdrawal(request, fromBank, txID);
                if (!rollbackSuccess) {
                    transferLogService.logFailure(txID, request, "롤백 실패: 복구 작업 필요");
                }
                return createFailureResponse(txID, toBank + " 입금 실패");
            }

            // 성공 처리
            transferEntity.setStatus(TransferStatus.COMPLETED);
            transferEntity.setMessage("트랜잭션 성공");
            saveTransferEntity(transferEntity);
            transferLogService.logSuccess(txID, request);
            return createSuccessResponse(txID);

        } catch (Exception e) {
            transferEntity.setStatus(TransferStatus.FAILED);
            transferEntity.setMessage("예상치 못한 에러: " + e.getMessage());
            saveTransferEntity(transferEntity);
            transferLogService.logFailure(txID, request, "예상치 못한 에러: " + e.getMessage());
            return createFailureResponse(txID, "송금 실패");
        }
    }

    /**
     * 출금 처리
     * @param txID 트랜잭션 ID
     * @param request 송금 요청 데이터
     * @param bankCode 출금 은행 코드
     * @return 출금 성공 여부
     */
    private boolean performWithdrawal(String txID, TransferServiceRequest request, String bankCode) {
        try {
            BankClientRequest clientRequest = BankClientRequestMapper.INSTANCE.toWithdrawRequest(request);
            clientRequest.setTxID(txID);
            return retryHandler.execute(() -> {
                BankingApiClient client = getBankingApiClient(bankCode);
                BankClientResponse response = client.withdraw(clientRequest);
                return response.isSuccess();
            });
        } catch (Exception e) {
            transferLogService.logFailure(txID, request, "출금 실패: " + e.getMessage());
            return false;
        }
    }

    /**
     * 입금 처리
     * @param txID 트랜잭션 ID
     * @param request 송금 요청 데이터
     * @param bankCode 입금 은행 코드
     * @return 입금 성공 여부
     */
    private boolean performDeposit(String txID, TransferServiceRequest request, String bankCode) {
        try {
            BankClientRequest clientRequest = BankClientRequestMapper.INSTANCE.toDepositRequest(request);
            clientRequest.setTxID(txID);
            return retryHandler.execute(() -> {
                BankingApiClient client = getBankingApiClient(bankCode);
                BankClientResponse response = client.deposit(clientRequest);
                return response.isSuccess();
            });
        } catch (Exception e) {
            transferLogService.logFailure(txID, request, "입금 실패: " + e.getMessage());
            return false;
        }
    }

    /**
     * 출금 롤백 처리
     * @param request 송금 요청 데이터
     * @param bankCode 출금 은행 코드
     * @param txID 트랜잭션 ID
     * @return 롤백 성공 여부
     */
    private boolean rollbackWithdrawal(TransferServiceRequest request, String bankCode, String txID) {
        try {
            BankClientRequest clientRequest = BankClientRequestMapper.INSTANCE.toUndoWithdrawalRequest(request);
            clientRequest.setTxID(txID);
            BankingApiClient client = getBankingApiClient(bankCode);
            client.undoWithdrawal(clientRequest);
            transferLogService.logRollback(txID, request);
            return true;
        } catch (Exception e) {
            transferLogService.logFailure(txID, request, "롤백 실패: " + e.getMessage());
            recordRollbackFailure(request, txID, e);
            return false;
        }
    }

    /**
     * 롤백 실패 상태를 외부 시스템(DB, Redis)에 기록
     * @param request 송금 요청 데이터
     * @param txID 트랜잭션 ID
     * @param e 롤백 실패 예외
     */
    private void recordRollbackFailure(TransferServiceRequest request, String txID, Throwable e) {
        String rollbackFailureMessage = String.format(
                "롤백 실패 - Request: %s, Error: %s",
                request.toString(),
                e.getMessage()
        );
        rollbackFailureStateRepository.saveRollbackFailure(txID, rollbackFailureMessage);
    }

    /**
     * 공통 에러 처리
     * @param operation 비즈니스 로직
     * @param request 송금 요청 데이터
     * @param errorMessage 에러 메시지
     * @return 처리 결과
     */
    private <T> Mono<T> handleErrors(Mono<T> operation, String txID, TransferServiceRequest request, String errorMessage) {
        return operation
                .onErrorResume(FeignException.GatewayTimeout.class, e -> {
                    transferLogService.logFailure(txID, request, errorMessage + "(네트워크 타임아웃): " + e.getMessage());
                    return Mono.error(new RuntimeException(errorMessage + " (네트워크 타임아웃)", e));
                })
                .onErrorResume(FeignException.class, e -> {
                    transferLogService.logFailure(txID, request, errorMessage + "(외부 서버 문제): " + e.getMessage());
                    return Mono.error(new RuntimeException(errorMessage + " (외부 서버 문제)", e));
                })
                .onErrorResume(e -> {
                    transferLogService.logFailure(txID, request, errorMessage + "(예상치 못한 오류): " + e.getMessage());
                    return Mono.error(new RuntimeException(errorMessage + " (예상치 못한 오류)", e));
                });
    }

    private BankingApiClient getBankingApiClient(String bankCode) {
        String clientKey = BankCodeEnum.valueOf(bankCode).getClientKey();
        BankingApiClient client = bankingApiClients.get(clientKey);
        if (client == null) {
            throw new BankNotFoundException(bankCode + " 잘못된 은행 코드");
        }
        return client;
    }

    private TransferServiceResponse createSuccessResponse(String txID) {
        return new TransferServiceResponse(txID, true);
    }

    private TransferServiceResponse createFailureResponse(String txID, String message) {
        return new TransferServiceResponse(txID, false, message);
    }

    /**
     * TransferEntity 초기화
     */
    private TransferEntity initializeTransferEntity(String txID, TransferServiceRequest request) {
        TransferEntity transferEntity = new TransferEntity();
        transferEntity.setTxID(txID);
        transferEntity.setFromAccount(findAccount(request.getFromAccountNumber()));
        transferEntity.setToAccount(findAccount(request.getToAccountNumber()));
        transferEntity.setAmount(request.getAmount());
        transferEntity.setStatus(TransferStatus.PENDING);
        return transferEntity;
    }

    /**
     * TransferEntity 저장
     */
    private void saveTransferEntity(TransferEntity transferEntity) {
        try {
            transferRepository.save(transferEntity);
        } catch (Exception e) {
            transferLogService.logFailure(transferEntity.getTxID(), null, "TransferEntity 저장 실패: " + e.getMessage());
        }
    }

    /**
     * 계좌 조회
     */
    private AccountEntity findAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("계좌 번호 " + accountNumber + "를 찾을 수 없습니다."));
    }
}