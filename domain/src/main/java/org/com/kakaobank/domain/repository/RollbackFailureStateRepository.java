package org.com.kakaobank.domain.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RollbackFailureStateRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public RollbackFailureStateRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRollbackFailure(String txID, String message) {
        // Key-Value 형식으로 저장
        redisTemplate.opsForValue().set("rollback_failure:" + txID, message);
    }

    public String getRollbackFailure(String txID) {
        // 저장된 실패 메시지 조회
        return (String) redisTemplate.opsForValue().get("rollback_failure:" + txID);
    }

    public void deleteRollbackFailure(String txID) {
        // 저장된 메시지 삭제
        redisTemplate.delete("rollback_failure:" + txID);
    }
}

