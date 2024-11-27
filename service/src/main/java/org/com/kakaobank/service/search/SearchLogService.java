package org.com.kakaobank.service.search;

import lombok.extern.slf4j.Slf4j;
import org.com.kakaobank.service.dto.BalanceServiceRequest;
import org.com.kakaobank.service.dto.TransactionServiceResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SearchLogService {

    public void logBalanceSuccess(BalanceServiceRequest request) {
        try {
            MDC.put("account", request.getAccountNumber());
            log.info("잔액 조회 성공: {}", request);
        } finally {
            MDC.clear();
        }
    }

    public void logBalanceFailure(BalanceServiceRequest request, String message) {
        try {
            MDC.put("account", request.getAccountNumber());
            log.error("잔액 조회 실패 - {} Request: {}", message, request);
        } finally {
            MDC.clear();
        }
    }

    public void logTransactionSuccess(String txID, TransactionServiceResponse response) {
        try {
            MDC.put("txID", txID);
            log.info("거래 결과 조회 성공: {}", response);
        } finally {
            MDC.clear();
        }
    }

    public void logTransactionFailure(String txID, String message) {
        try {
            MDC.put("txID", txID);
            log.error("거래 결과 조회 실패 - {}", message);
        } finally {
            MDC.clear();
        }
    }
}
