package org.com.kakaobank.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "banking.api")
public class BankingApiConfig {
    private String ryanBank;
    private String chunsikBank;
}
