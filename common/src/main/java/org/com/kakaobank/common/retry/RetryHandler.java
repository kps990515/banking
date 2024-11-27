package org.com.kakaobank.common.retry;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface RetryHandler {
    <T> T execute(Callable<T> callable) throws Exception;
}
