package org.com.kakaobank.common.retry;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.Callable;

@Component
public class RetryHandlerImpl implements RetryHandler {

    private final Retry retry;

    public RetryHandlerImpl() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3) // 최대 3번 시도
                .waitDuration(Duration.ofMillis(500)) // 재시도 간격
                .build();
        this.retry = Retry.of("defaultRetry", config);
    }

    @Override
    public <T> T execute(Callable<T> callable) throws Exception {
        Callable<T> retryableCallable = Retry.decorateCallable(retry, callable);
        try {
            return retryableCallable.call();
        } catch (Exception e) {
            throw new RuntimeException("재시도 실패: " + e.getMessage(), e);
        }
    }
}
