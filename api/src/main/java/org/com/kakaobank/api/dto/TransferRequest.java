package org.com.kakaobank.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.com.kakaobank.common.validation.BankCode;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    @BankCode
    @NotBlank(message = "toAccountBank는 필수")
    private String toAccountBank;

    @NotBlank(message = "toAccountNumber는 필수")
    private String toAccountNumber;

    @BankCode
    @NotBlank(message = "fromAccountBank는 필수")
    private String fromAccountBank;

    @NotBlank(message = "fromAccountNumber는 필수")
    private String fromAccountNumber;

    @NotNull(message = "amount는 필수")
    @Positive(message = "amount는 0보다 커야함")
    private BigDecimal amount;
}
