package org.com.kakaobank.service.transfer;

import lombok.extern.slf4j.Slf4j;
import org.com.kakaobank.service.dto.TransferServiceRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransferLogService {

    /**
     * 트랜잭션 성공 로그 기록
     *
     * @param txID   트랜잭션 ID
     * @param request 송금 요청 데이터
     */
    public void logSuccess(String txID, TransferServiceRequest request) {
        try {
            MDC.put("txID", txID); // MDC에 txID 추가
            log.info("트랜잭션 성공: {}", request);
        } finally {
            MDC.clear(); // MDC 정리
        }
    }

    /**
     * 트랜잭션 실패 로그 기록
     *
     * @param txID   트랜잭션 ID
     * @param request 송금 요청 데이터
     * @param message 실패 메시지
     */
    public void logFailure(String txID, TransferServiceRequest request, String message) {
        try {
            MDC.put("txID", txID); // MDC에 txID 추가
            log.error("트랜잭션 실패: {} Request: {}", message, request);
        } finally {
            MDC.clear(); // MDC 정리
        }
    }

    /**
     * 롤백 성공 로그 기록
     *
     * @param txID   트랜잭션 ID
     * @param request 송금 요청 데이터
     */
    public void logRollback(String txID, TransferServiceRequest request) {
        try {
            MDC.put("txID", txID); // MDC에 txID 추가
            log.warn("보상 트랜잭션 성공: {}", request);
        } finally {
            MDC.clear(); // MDC 정리
        }
    }
}
