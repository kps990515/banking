package org.com.kakaobank.client.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BankClientResponse {
    @NotBlank(message = "accountNumber는 필수")
    private String txID;
    private boolean isSuccess;
    private String message; // 적요 및 실패

    public BankClientResponse() {
    }
}
