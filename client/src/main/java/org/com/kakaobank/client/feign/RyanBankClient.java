package org.com.kakaobank.client.feign;

import org.com.kakaobank.client.config.FeignClientConfig;
import org.com.kakaobank.client.config.FeignConfig;
import org.com.kakaobank.client.config.FeignErrorDecoder;
import org.com.kakaobank.client.fallback.BankingApiFallback;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "ryan-bank-client",
        url = "${client.bank.ryan}",
        configuration = {FeignConfig.class, FeignClientConfig.class, FeignErrorDecoder.class},
        fallback = BankingApiFallback.class)

public interface RyanBankClient extends BankingApiClient{
}


