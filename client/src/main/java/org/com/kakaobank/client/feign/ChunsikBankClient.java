package org.com.kakaobank.client.feign;

import org.com.kakaobank.client.config.FeignClientConfig;
import org.com.kakaobank.client.config.FeignConfig;
import org.com.kakaobank.client.config.FeignErrorDecoder;
import org.com.kakaobank.client.fallback.BankingApiFallback;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "chunsik-bank-client",
        url = "${client.bank.chunsik}",
        configuration = {FeignConfig.class, FeignClientConfig.class, FeignErrorDecoder.class},
        fallback = BankingApiFallback.class)

public interface ChunsikBankClient extends BankingApiClient{
}


