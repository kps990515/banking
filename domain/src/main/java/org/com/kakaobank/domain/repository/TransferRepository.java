package org.com.kakaobank.domain.repository;

import org.com.kakaobank.domain.entity.AccountEntity;
import org.com.kakaobank.domain.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<TransferEntity, String> {

    /**
     * 출금 계좌로 거래 조회
     * @param fromAccount 출금 계좌 엔티티
     * @return 거래 리스트
     */
    List<TransferEntity> findByFromAccount(AccountEntity fromAccount);

    /**
     * 입금 계좌로 거래 조회
     * @param toAccount 입금 계좌 엔티티
     * @return 거래 리스트
     */
    List<TransferEntity> findByToAccount(AccountEntity toAccount);

    /**
     * txID를 기준으로 거래 조회
     * @param txID 거래 ID
     * @return 거래 엔티티
     */
    Optional<TransferEntity> findByTxID(String txID);
}