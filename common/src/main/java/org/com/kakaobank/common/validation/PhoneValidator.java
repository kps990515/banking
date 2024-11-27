package org.com.kakaobank.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private static final String PHONE_REGEX = "^\\d{10,15}$";

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return phone.matches(PHONE_REGEX);
    }
}
