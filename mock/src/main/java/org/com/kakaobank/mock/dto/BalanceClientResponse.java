package org.com.kakaobank.mock.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class BalanceClientResponse {
    @NotNull(message = "amount는 필수")
    private BigDecimal amount;
    private boolean isSuccess;
    private String message; // 실패 메시지

    public BalanceClientResponse(boolean isSuccess, String message){
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public BalanceClientResponse(BigDecimal amount){
        this.amount = amount;
    }
}
