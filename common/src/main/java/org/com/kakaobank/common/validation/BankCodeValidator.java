package org.com.kakaobank.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.EnumSet;
import java.util.Set;

/**
 * `@BankCode` 애노테이션의 실제 검증 로직.
 */
public class BankCodeValidator implements ConstraintValidator<BankCode, String> {

    private final Set<String> validCodes = EnumSet.allOf(BankCodeEnum.class)
            .stream()
            .map(Enum::name)
            .collect(java.util.stream.Collectors.toSet());

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && validCodes.contains(value);
    }
}
