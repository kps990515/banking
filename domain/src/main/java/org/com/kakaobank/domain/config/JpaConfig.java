package org.com.kakaobank.domain.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.com.kakaobank.domain.repository") // 리포지토리 패키지 경로
@EntityScan(basePackages = "org.com.kakaobank.domain.entity") // 엔티티 패키지 경로
public class JpaConfig {

}
