package org.com.kakaobank.domain.repository;

import org.com.kakaobank.domain.entity.AccountEntity;
import org.com.kakaobank.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, String> {

    /**
     * 특정 사용자의 모든 계좌 조회
     * @param user 사용자 엔티티
     * @return 계좌 리스트
     */
    List<AccountEntity> findByUser(UserEntity user);

    /**
     * 계좌번호를 기준으로 계좌 조회
     * @param accountNumber 계좌 번호
     * @return 계좌 엔티티
     */
    Optional<AccountEntity> findByAccountNumber(String accountNumber);
}
