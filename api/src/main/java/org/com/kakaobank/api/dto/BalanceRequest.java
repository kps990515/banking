package org.com.kakaobank.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BalanceRequest {
    @NotBlank(message = "accountNumber는 필수")
    private String accountNumber;
}
