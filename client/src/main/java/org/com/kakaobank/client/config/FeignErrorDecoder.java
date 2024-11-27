package org.com.kakaobank.client.config;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.com.kakaobank.common.exception.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 500) {
            return new InternalServerErrorException("Internal Server Error: " + methodKey);
        }
        return new RuntimeException("Feign Client Error: " + response.status());
    }
}