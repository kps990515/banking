package org.com.kakaobank.domain.repository;

import org.com.kakaobank.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    /**
     * 이메일을 기준으로 사용자 조회
     * @param email 사용자 이메일
     * @return 사용자 엔티티
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * 전화번호를 기준으로 사용자 조회
     * @param phone 사용자 전화번호
     * @return 사용자 엔티티
     */
    Optional<UserEntity> findByPhone(String phone);
}
