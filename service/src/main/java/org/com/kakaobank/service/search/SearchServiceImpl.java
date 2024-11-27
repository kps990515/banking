package org.com.kakaobank.service.search;

import com.github.f4b6a3.uuid.UuidCreator;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.com.kakaobank.client.dto.BalanceClientRequest;
import org.com.kakaobank.client.dto.BalanceClientResponse;
import org.com.kakaobank.client.feign.BankingApiClient;
import org.com.kakaobank.common.exception.AccountNotFoundException;
import org.com.kakaobank.common.exception.BankNotFoundException;
import org.com.kakaobank.common.exception.TransactionNotFoundException;
import org.com.kakaobank.common.retry.RetryHandler;
import org.com.kakaobank.common.util.ObjectConvertUtil;
import org.com.kakaobank.domain.entity.AccountEntity;
import org.com.kakaobank.domain.entity.TransferEntity;
import org.com.kakaobank.domain.entity.TransferStatus;
import org.com.kakaobank.domain.repository.AccountRepository;
import org.com.kakaobank.domain.repository.TransferRepository;
import org.com.kakaobank.service.dto.BalanceServiceRequest;
import org.com.kakaobank.service.dto.BalanceServiceResponse;
import org.com.kakaobank.service.dto.BankCodeEnum;
import org.com.kakaobank.service.dto.TransactionServiceResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

    private final Map<String, BankingApiClient> bankingApiClients; // 은행별 클라이언트 매핑
    private final SearchLogService searchLogService; // 조회 트랜잭션 로그 관리
    private final RetryHandler retryHandler; // 재시도 로직
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public SearchServiceImpl(Map<String, BankingApiClient> bankingApiClients,
                             RetryHandler retryHandler,
                             SearchLogService searchLogService,
                             AccountRepository accountRepository,
                             TransferRepository transferRepository) {
        this.bankingApiClients = bankingApiClients;
        this.retryHandler = retryHandler;
        this.searchLogService = searchLogService;
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    @Override
    public BalanceServiceResponse getBalance(BalanceServiceRequest request) {
        String txID = UuidCreator.getTimeOrderedEpoch().toString();

        String bankCode = accountRepository.findByAccountNumber(request.getAccountNumber())
                .map(AccountEntity::getAccountBank)
                .orElseThrow(() -> new AccountNotFoundException("해당 계좌 미존재 : " + request.getAccountNumber()));

        try {
            BalanceClientRequest clientRequest = ObjectConvertUtil.copyVO(request, BalanceClientRequest.class);
            BankingApiClient client = getBankingApiClient(bankCode);

            // 외부 API 호출
            BalanceClientResponse balanceClientResponse = retryHandler.execute(() -> client.getBalance(clientRequest));
            balanceClientResponse.setSuccess(true);

            saveSearchEntity(txID, request, balanceClientResponse.getAmount(), TransferStatus.COMPLETED, "잔액 조회 성공");
            searchLogService.logBalanceSuccess(request);

            return ObjectConvertUtil.copyVO(balanceClientResponse, BalanceServiceResponse.class);
        } catch (FeignException.GatewayTimeout e) {
            logBalanceError(txID, request, e, "잔액 조회 실패 (네트워크 타임아웃)");
            throw new RuntimeException("잔액 조회 실패 (네트워크 타임아웃)", e);
        } catch (FeignException e) {
            logBalanceError(txID, request, e, "잔액 조회 실패 (외부 서버 문제)");
            throw new RuntimeException("잔액 조회 실패 (외부 서버 문제)", e);
        } catch (Exception e) {
            logBalanceError(txID, request, e, "잔액 조회 실패 (예상치 못한 오류)");
            throw new RuntimeException("잔액 조회 실패 (예상치 못한 오류)", e);
        }
    }

    @Transactional
    @Override
    public TransactionServiceResponse getTransactionResult(String txID) {
        TransferEntity transferEntity = transferRepository.findByTxID(txID)
                .orElseThrow(() -> new TransactionNotFoundException("해당 거래 미존재: " + txID));

        String bankCode = transferEntity.getFromAccount().getAccountBank();
        TransferStatus status = transferEntity.getStatus();

        if (status == TransferStatus.PENDING) {
            return new TransactionServiceResponse(false, "거래 진행 중");
        } else if (status == TransferStatus.FAILED) {
            return new TransactionServiceResponse(false, "거래 실패");
        } else if (status == TransferStatus.COMPLETED) {
            try {
                BankingApiClient client = getBankingApiClient(bankCode);
                return retryHandler.execute(() -> {
                    TransactionServiceResponse response = ObjectConvertUtil.copyVO(client.getTransactionResult(txID), TransactionServiceResponse.class);
                    searchLogService.logTransactionSuccess(txID, response);
                    return response;
                });
            } catch (FeignException.GatewayTimeout e) {
                logTransactionError(txID, e, "거래 결과 조회 실패 (네트워크 타임아웃)");
                throw new RuntimeException("거래 결과 조회 실패 (네트워크 타임아웃)", e);
            } catch (FeignException e) {
                logTransactionError(txID, e, "거래 결과 조회 실패 (외부 서버 문제)");
                throw new RuntimeException("거래 결과 조회 실패 (외부 서버 문제)", e);
            } catch (Exception e) {
                logTransactionError(txID, e, "거래 결과 조회 실패 (예상치 못한 오류)");
                throw new RuntimeException("거래 결과 조회 실패 (예상치 못한 오류)", e);
            }
        } else {
            return new TransactionServiceResponse(false, "알 수 없는 거래 상태");
        }
    }

    private void saveSearchEntity(String txID, BalanceServiceRequest request, BigDecimal amount, TransferStatus status, String message) {
        try {
            TransferEntity entity = new TransferEntity();
            entity.setFromAccount(findAccount(request.getAccountNumber()));
            entity.setAmount(amount);
            entity.setStatus(status);
            entity.setMessage(message);
            transferRepository.save(entity);
        } catch (Exception e) {
            log.error("SearchEntity 저장 실패: {}", e.getMessage());
        }
    }

    private void logBalanceError(String txID, BalanceServiceRequest request, Exception e, String message) {
        searchLogService.logBalanceFailure(request, message + ": " + e.getMessage());
        saveSearchEntity(txID, request, BigDecimal.ZERO, TransferStatus.FAILED, message);
    }

    private void logTransactionError(String txID, Exception e, String message) {
        searchLogService.logTransactionFailure(txID, message + ": " + e.getMessage());
        saveTransactionEntity(txID, message);
    }

    private void saveTransactionEntity(String txID, String message) {
        try {
            TransferEntity entity = new TransferEntity();
            entity.setTxID(txID);
            entity.setStatus(TransferStatus.FAILED);
            entity.setMessage(message);
            transferRepository.save(entity);
        } catch (Exception e) {
            log.error("TransactionEntity 저장 실패: {}", e.getMessage());
        }
    }

    private AccountEntity findAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("계좌 번호 " + accountNumber + "를 찾을 수 없습니다."));
    }

    private BankingApiClient getBankingApiClient(String bankCode) {
        String clientKey = BankCodeEnum.valueOf(bankCode).getClientKey();
        BankingApiClient client = bankingApiClients.get(clientKey);
        if (client == null) {
            throw new BankNotFoundException(bankCode + " 잘못된 은행 코드");
        }
        return client;
    }
}