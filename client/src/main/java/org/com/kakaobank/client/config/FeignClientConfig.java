package org.com.kakaobank.client.config;

import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.com.kakaobank.client.feign.BankingApiClient;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class FeignClientConfig {

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final Map<String, BankingApiClient> clientCache = new ConcurrentHashMap<>();

    @Value("${client.bank.ryan}")
    private String ryanBankUrl;

    @Value("${client.bank.chunsik}")
    private String chunsikBankUrl;

    public FeignClientConfig(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    /**
     * 동적 URL 기반으로 BankingApiClient를 생성하거나 캐시에서 반환합니다.
     *
     * @param bankCode 은행 코드 (e.g., "'RYAN_BANK", "CHUNSIK_BANK")
     * @return BankingApiClient
     */
    public BankingApiClient getBankingApiClient(String bankCode) {
        return clientCache.computeIfAbsent(bankCode, this::createBankingApiClient);
    }

    /**
     * 주어진 은행 코드에 따라 Feign 클라이언트를 생성합니다.
     *
     * @param bankCode 은행 코드 (e.g., "'RYAN_BANK", "CHUNSIK_BANK")
     * @return BankingApiClient
     */
    private BankingApiClient createBankingApiClient(String bankCode) {
        String url = resolveBankUrl(bankCode);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(bankCode.toLowerCase() + "-banking-api");

        FeignDecorators decorators = FeignDecorators.builder()
                .withCircuitBreaker(circuitBreaker)
                .build();

        return Resilience4jFeign.builder(decorators)
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .contract(new SpringMvcContract())
                .target(BankingApiClient.class, url);
    }

    /**
     * 은행 코드에 따라 URL을 반환합니다.
     *
     * @param bankCode 은행 코드
     * @return 해당 은행의 URL
     */
    private String resolveBankUrl(String bankCode) {
        return switch (bankCode) {
            case "RYAN_BANK" -> ryanBankUrl;
            case "CHUNSIK_BANK" -> chunsikBankUrl;
            default -> throw new IllegalArgumentException("잘못된 은행코드: " + bankCode);
        };
    }
}
