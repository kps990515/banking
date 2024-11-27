package org.com.kakaobank.common.exception;

/**
 * Feign 에러
 */
public class FeignException extends RuntimeException {
    public FeignException(String message) {
        super(message);
    }
}
