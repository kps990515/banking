package org.com.kakaobank.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BalanceServiceRequest {
    @NotBlank(message = "accountNumber는 필수")
    private String accountNumber;
}
