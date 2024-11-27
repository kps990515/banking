package org.com.kakaobank.client.config;

import feign.Contract;
import feign.Request;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig {

    @Value("${feign.connect-timeout:10000}")
    private int connectTimeout;

    @Value("${feign.read-timeout:10000}")
    private int readTimeout;

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(connectTimeout, TimeUnit.MILLISECONDS, readTimeout, TimeUnit.MILLISECONDS, false); // Timeout 설정
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    @Bean
    public Contract feignContract() {
        return new SpringMvcContract();
    }
}
