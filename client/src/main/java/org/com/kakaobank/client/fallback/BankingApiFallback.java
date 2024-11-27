package org.com.kakaobank.client.fallback;

import org.com.kakaobank.client.feign.BankingApiClient;
import org.com.kakaobank.client.dto.*;
import org.springframework.stereotype.Component;

@Component
public class BankingApiFallback implements BankingApiClient {

    @Override
    public BankClientResponse withdraw(BankClientRequest request) {
        return new BankClientResponse(request.getTxID(), false, "출금실패");
    }

    @Override
    public BankClientResponse deposit(BankClientRequest request) {
        return new BankClientResponse(request.getTxID(), false, "입금실패");
    }

    @Override
    public BalanceClientResponse getBalance(BalanceClientRequest request) {
        return new BalanceClientResponse(false, "잔액조회실패"); // 기본 실패 반환
    }

    @Override
    public TransactionClientResponse getTransactionResult(String txID) {
        return new TransactionClientResponse(false, "거래조회실패"); // 기본 실패 반환
    }

    @Override
    public BankClientResponse undoWithdrawal(BankClientRequest request) {
        return new BankClientResponse(request.getTxID(), false, "출금 보상처리 실패");
    }
}
