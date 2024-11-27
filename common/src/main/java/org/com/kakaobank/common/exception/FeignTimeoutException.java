package org.com.kakaobank.common.exception;

/**
 * Feign 타임아웃 에러
 */
public class FeignTimeoutException extends RuntimeException {
    public FeignTimeoutException(String message) {
        super(message);
    }
}
