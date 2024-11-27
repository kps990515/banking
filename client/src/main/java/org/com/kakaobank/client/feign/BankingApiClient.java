package org.com.kakaobank.client.feign;

import org.com.kakaobank.client.dto.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface BankingApiClient {

    @PostMapping(value = "/api/deposit", consumes = "application/json", produces = "application/json")
    BankClientResponse deposit(@RequestBody BankClientRequest request);

    @PostMapping(value = "/api/withdrawal", consumes = "application/json", produces = "application/json")
    BankClientResponse withdraw(@RequestBody BankClientRequest request);

    @PostMapping(value = "/api/balance", consumes = "application/json", produces = "application/json")
    BalanceClientResponse getBalance(@RequestBody BalanceClientRequest request);

    @GetMapping(value = "/api/transactions/{txID}", produces = "application/json")
    TransactionClientResponse getTransactionResult(@PathVariable("txID") String txID);

    @PostMapping(value = "/api/undoWithdrawal", consumes = "application/json", produces = "application/json")
    BankClientResponse undoWithdrawal(@RequestBody BankClientRequest request);
}
