package org.com.kakaobank.client.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionClientResponse {
    private boolean isSuccess; // 거래 성공 여부
    private String message; // 실패 메시지
}
