package org.com.kakaobank.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionServiceResponse {
    private boolean isSuccess; // 거래 성공 여부
    private String message; // 실패 메시지

    public TransactionServiceResponse(boolean isSuccess){
        this.isSuccess = isSuccess;
    }
}
