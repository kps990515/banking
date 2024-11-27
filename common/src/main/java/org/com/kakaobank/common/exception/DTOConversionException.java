package org.com.kakaobank.common.exception;

/**
 * DTO 변환 실패 시 발생하는 예외
 */
public class DTOConversionException extends RuntimeException {
    public DTOConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
