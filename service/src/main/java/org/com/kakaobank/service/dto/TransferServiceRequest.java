package org.com.kakaobank.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TransferServiceRequest {
    private String toAccountBank;
    private String toAccountNumber;
    private String fromAccountBank;
    private String fromAccountNumber;
    private BigDecimal amount;

    public TransferServiceRequest(String toAccountBank, String toAccountNumber,String fromAccountBank, String fromAccountNumber, BigDecimal amount) {
        this.toAccountBank = toAccountBank;
        this.toAccountNumber = toAccountNumber;
        this.fromAccountBank = fromAccountBank;
        this.fromAccountNumber = fromAccountNumber;
        this.amount = amount;
    }
}
