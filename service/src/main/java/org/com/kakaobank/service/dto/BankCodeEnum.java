package org.com.kakaobank.service.dto;

import lombok.Getter;

@Getter
public enum BankCodeEnum {
    RYAN_BANK("org.com.kakaobank.client.feign.RyanBankClient"),
    CHUNSIK_BANK("org.com.kakaobank.client.feign.ChunsikBankClient");

    private final String clientKey;

    BankCodeEnum(String clientKey) {
        this.clientKey = clientKey;
    }

}
