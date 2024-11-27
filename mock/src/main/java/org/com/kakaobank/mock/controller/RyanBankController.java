package org.com.kakaobank.mock.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.kakaobank.common.exception.InternalServerErrorException;
import org.com.kakaobank.domain.entity.AccountEntity;
import org.com.kakaobank.domain.entity.TransferEntity;
import org.com.kakaobank.domain.repository.AccountRepository;
import org.com.kakaobank.domain.repository.TransferRepository;
import org.com.kakaobank.mock.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ryan-bank/api")
public class RyanBankController {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    /**
     * 입금 처리
     * @param request 입금 요청 데이터
     * @return 입금 결과
     */
    @PostMapping(value = "/deposit", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BankClientResponse> deposit(@RequestBody BankClientRequest request) {
        String txId = request.getTxID();
        String accountNumber = request.getAccountNumber();

        // 계좌 조회
        AccountEntity account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));

        // 입금 처리
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        return ResponseEntity.ok(new BankClientResponse(
                request.getTxID(),
                true,
                "입금 완료: " + BankCodeEnum.RYAN_BANK + request.getAccountNumber()
        ));
    }

    /**
     * 출금 처리
     * @param request 출금 요청 데이터
     * @return 출금 결과
     */
    @PostMapping(value = "/withdrawal", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BankClientResponse> withdraw(@RequestBody BankClientRequest request) {
        String txId = request.getTxID();
        String accountNumber = request.getAccountNumber();

        // 계좌 조회
        AccountEntity account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));

        // 잔액 확인
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            return ResponseEntity.status(500).body(new BankClientResponse(
                    request.getTxID(),
                    false,
                    "잔액 부족: " + BankCodeEnum.RYAN_BANK + request.getAccountNumber()
            ));
        }

        // 출금 처리
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        return ResponseEntity.ok(new BankClientResponse(
                request.getTxID(),
                true,
                "출금 완료: " + BankCodeEnum.RYAN_BANK + request.getAccountNumber()
        ));
    }

    /**
     * 잔액 조회
     * @param request 잔액 요청 데이터
     * @return 계좌 잔액
     */
    @PostMapping(value = "/balance", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BalanceClientResponse> getBalance(@RequestBody BalanceClientRequest request) {
        String accountNumber = request.getAccountNumber();

        // 계좌 조회
        AccountEntity account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));

        return ResponseEntity.ok(new BalanceClientResponse(account.getBalance()));
    }

    /**
     * 거래 결과 조회
     * @param txID 거래 ID
     * @return 거래 결과
     */
    @GetMapping(value = "/transactions/{txID}", produces = "application/json")
    public ResponseEntity<TransactionClientResponse> getTransactionResult(@PathVariable String txID) {
        TransferEntity transfer = transferRepository.findByTxID(txID)
                .orElseThrow(() -> new InternalServerErrorException("거래 기록 없음"));
        return ResponseEntity.ok(new TransactionClientResponse(
                true,
                "거래내역 조회완료"
        ));
    }
}