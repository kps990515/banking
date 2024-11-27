package org.com.kakaobank.mock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionClientResponse {
    private boolean isSuccess; // 거래 성공 여부
    private String message; // 실패 메시지
}
