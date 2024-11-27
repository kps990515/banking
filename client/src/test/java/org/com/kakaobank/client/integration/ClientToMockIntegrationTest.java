package org.com.kakaobank.client.integration;

import org.com.kakaobank.client.config.FeignClientConfig;
import org.com.kakaobank.client.config.FeignConfig;
import org.com.kakaobank.client.config.FeignErrorDecoder;
import org.com.kakaobank.client.dto.*;
import org.com.kakaobank.client.feign.BankingApiClient;
import org.com.kakaobank.common.web.CircuitBreakerConfig;
import org.com.kakaobank.mock.MockApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {MockApplication.class, FeignConfig.class, FeignErrorDecoder.class, FeignClientConfig.class, CircuitBreakerConfig.class})
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yaml")
class ClientToMockIntegrationTest {

    @Autowired
    private FeignClientConfig feignClientConfig;

    @Test
    void testRyanBankDeposit() {
        BankingApiClient client = feignClientConfig.getBankingApiClient("RYAN_BANK");

        BankClientRequest request = new BankClientRequest("tx123", "account123", new BigDecimal("500.00"), "RYAN_BANK100");
        BankClientResponse response = client.deposit(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).contains("입금 완료");
    }

    @Test
    void testChunsikBankWithdrawal() {
        BankingApiClient client = feignClientConfig.getBankingApiClient("CHUNSIK_BANK");

        BankClientRequest request = new BankClientRequest("tx456", "account123", new BigDecimal("300.00"), "CHUNSIK_BANK100");
        BankClientResponse response = client.withdraw(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).contains("출금 완료");
    }

    @Test
    void testBalanceRetrieval() {
        BankingApiClient client = feignClientConfig.getBankingApiClient("RYAN_BANK");

        client.deposit(new BankClientRequest("tx789", "account123", new BigDecimal("200.00"), "RYAN_BANK100"));
        BalanceClientResponse response = client.getBalance(new BalanceClientRequest("account123"));

        assertThat(response.getAmount()).isEqualTo(new BigDecimal("1000.00"));
    }

    @Test
    void testTransactionResult() {
        BankingApiClient client = feignClientConfig.getBankingApiClient("CHUNSIK_BANK");

        client.deposit(new BankClientRequest("tx999", "account123", new BigDecimal("700.00"), "CHUNSIK_BANK100"));
        TransactionClientResponse response = client.getTransactionResult("tx999");

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).contains("거래 조회 완료");
    }
}