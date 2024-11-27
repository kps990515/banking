package org.com.kakaobank.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class BankClientRequest {
    @NotBlank(message = "accountNumber는 필수")
    private String txID;

    @NotBlank(message = "accountNumber는 필수")
    private String accountNumber;

    @NotNull(message = "amount는 필수")
    @Positive(message = "amount는 0보다 커야함")
    private BigDecimal amount;

    @NotBlank(message = "입금은행코드/입금은행계좌번호가 반드시 있어야함")
    private String comment;
}
