package org.com.kakaobank.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum TransferStatus {
    PENDING,
    COMPLETED,
    FAILED
}