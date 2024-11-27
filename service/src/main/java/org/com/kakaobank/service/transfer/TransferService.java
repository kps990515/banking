package org.com.kakaobank.service.transfer;

import org.com.kakaobank.service.dto.TransferServiceRequest;
import org.com.kakaobank.service.dto.TransferServiceResponse;
import reactor.core.publisher.Mono;

public interface TransferService {
    /**
     * 계좌 이체를 처리합니다.
     * 따로 Interface로 분리한 이유는 단일책임원칙과 Mock테스트 용이성을 위해
     *
     * @param request TransferRequest - 송금 요청 데이터
     * @return TransferResponse - 송금 응답 데이터
     */
    TransferServiceResponse processTransfer(TransferServiceRequest request);
}
