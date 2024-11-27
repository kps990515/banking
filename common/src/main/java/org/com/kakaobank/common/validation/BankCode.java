package org.com.kakaobank.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 커스텀 애노테이션: 허용된 은행 코드만 유효하도록 제한.
 */
@Constraint(validatedBy = BankCodeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BankCode {

    String message() default "'RYAN_BANK' / 'CHUNSIK_BANK'만 가능.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


