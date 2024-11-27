package org.com.kakaobank.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // 계좌 소유 회원

    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber; // 계좌 번호

    @Column(nullable = false, length = 45)
    private String accountBank; // 은행

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO; // 계좌 잔액
}
