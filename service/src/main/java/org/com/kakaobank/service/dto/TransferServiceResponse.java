package org.com.kakaobank.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferServiceResponse {
    @NotBlank(message = "txID 필수")
    private String txID;

    private boolean isSuccess;

    private String message;

    public TransferServiceResponse(String txID, boolean isSuccess) {
        this.txID = txID;
        this.isSuccess = isSuccess;
    }
}
