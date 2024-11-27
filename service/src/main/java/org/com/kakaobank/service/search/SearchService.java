package org.com.kakaobank.service.search;

import org.com.kakaobank.service.dto.BalanceServiceRequest;
import org.com.kakaobank.service.dto.BalanceServiceResponse;
import org.com.kakaobank.service.dto.TransactionServiceResponse;
import reactor.core.publisher.Mono;

public interface SearchService {
    /**
     * 잔액 조회
     * @param request 잔액조회 요청 데이터
     * @return 잔액 응답 데이터
     */
    BalanceServiceResponse getBalance(BalanceServiceRequest request);

    /**
     * 거래 결과 조회
     * @param txID 거래 고유 ID
     * @return 거래 결과 응답 데이터
     */
    TransactionServiceResponse getTransactionResult(String txID);
}
