package org.com.kakaobank.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BalanceResponse {
    @NotNull(message = "amount는 필수")
    private BigDecimal amount;
    private boolean isSuccess;
    private String message; // 실패 메시지

    public BalanceResponse(boolean isSuccess, String message){
        this.isSuccess = isSuccess;
        this.message = message;
    }
}
