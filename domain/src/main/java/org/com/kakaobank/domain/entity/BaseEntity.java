package org.com.kakaobank.domain.entity;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity implements Persistable<String> {

    @Id
    @Column(nullable = false, updatable = false, length = 45)
    private String id; // 고유 식별자

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @PrePersist
    private void generateUUID() {
        if (this.id == null || this.id.isEmpty()) {
            this.id = UuidCreator.getTimeOrderedEpoch().toString();
        }
    }

    @Override
    @Transient
    public boolean isNew() {
        return this.createdAt == null;
    }

    @Override
    public String getId() {
        return this.id;
    }
}