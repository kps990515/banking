package org.com.kakaobank.client.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BalanceClientRequest {
    @NotBlank(message = "accountNumber는 필수")
    private String accountNumber;
}
