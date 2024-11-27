package org.com.kakaobank.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.com.kakaobank.common.validation.ValidPhone;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String username; // 사용자 이름

    @Column(nullable = false, unique = true, length = 100)
    @Email
    private String email; // 이메일 주소

    @Column(nullable = false, unique = true, length = 15)
    @ValidPhone
    private String phone; // 전화번호
}
