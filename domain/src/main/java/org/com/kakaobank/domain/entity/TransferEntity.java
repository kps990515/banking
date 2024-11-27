package org.com.kakaobank.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "transfer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferEntity extends BaseEntity {

    @Column(name = "tx_id", nullable = false, unique = true, length = 45)
    private String txID; // 거래 ID (UUID)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_number", nullable = false)
    private AccountEntity fromAccount; // 출금 계좌

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_number", nullable = false)
    private AccountEntity toAccount; // 입금 계좌

    @Column(nullable = false)
    private BigDecimal amount = BigDecimal.ZERO; // 거래 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status = TransferStatus.PENDING; // 트랜잭션 상태

    @Column(length = 255)
    private String message; // 거래 결과 메시지
}
