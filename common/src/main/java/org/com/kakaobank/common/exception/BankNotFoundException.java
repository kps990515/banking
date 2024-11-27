package org.com.kakaobank.common.exception;

/**
 * 맞는 은행이 아닐시 나오는 에러
 */
public class BankNotFoundException extends RuntimeException {
    public BankNotFoundException(String message) {
        super(message);
    }
}
