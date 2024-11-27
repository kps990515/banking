package org.com.kakaobank.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.kakaobank.api.dto.*;
import org.com.kakaobank.common.util.ObjectConvertUtil;
import org.com.kakaobank.service.dto.BalanceServiceRequest;
import org.com.kakaobank.service.dto.TransferServiceRequest;
import org.com.kakaobank.service.search.SearchService;
import org.com.kakaobank.service.transfer.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.naming.ServiceUnavailableException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class BankingController {

    private final TransferService transferService;
    private final SearchService searchService;

    @PostMapping("/transfer")
    public Mono<ResponseEntity<TransferResponse>> transfer(@Valid @RequestBody TransferRequest request) {
        TransferServiceRequest serviceRequest = ObjectConvertUtil.copyVO(request, TransferServiceRequest.class);

        return Mono.fromCallable(() -> transferService.processTransfer(serviceRequest))
                .subscribeOn(Schedulers.boundedElastic())
                .map(serviceResponse -> ObjectConvertUtil.copyVO(serviceResponse, TransferResponse.class))
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class, e -> {
                    log.error("잘못된 요청", e);
                    return Mono.just(ResponseEntity.badRequest().body(null));
                })
                .onErrorResume(ServiceUnavailableException.class, e -> {
                    log.error("서비스 불가", e);
                    return Mono.just(ResponseEntity.status(503).body(null));
                })
                .onErrorResume(e -> {
                    log.error("예상치 못한 에러", e);
                    return Mono.just(ResponseEntity.status(500).body(null));
                });
    }

    @PostMapping("/balance")
    public Mono<ResponseEntity<BalanceResponse>> getBalance(@Valid @RequestBody BalanceRequest request) {
        BalanceServiceRequest serviceRequest = ObjectConvertUtil.copyVO(request, BalanceServiceRequest.class);

        return Mono.fromCallable(() -> searchService.getBalance(serviceRequest))
                .subscribeOn(Schedulers.boundedElastic())
                .map(serviceResponse -> ObjectConvertUtil.copyVO(serviceResponse, BalanceResponse.class))
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class, e -> {
                    log.error("잘못된 요청", e);
                    return Mono.just(ResponseEntity.badRequest().body(null));
                })
                .onErrorResume(ServiceUnavailableException.class, e -> {
                    log.error("서비스 불가", e);
                    return Mono.just(ResponseEntity.status(503).body(null));
                })
                .onErrorResume(e -> {
                    log.error("예상치 못한 에러", e);
                    return Mono.just(ResponseEntity.status(500).body(null));
                });
    }

    @GetMapping("/transactions/{txID}")
    public Mono<ResponseEntity<TransactionResponse>> getTransactionResult(@PathVariable String txID) {
        return Mono.fromCallable(() -> searchService.getTransactionResult(txID))
                .subscribeOn(Schedulers.boundedElastic())
                .map(serviceResponse -> ObjectConvertUtil.copyVO(serviceResponse, TransactionResponse.class))
                .map(ResponseEntity::ok)
                .onErrorResume(IllegalArgumentException.class, e -> {
                    log.error("잘못된 요청", e);
                    return Mono.just(ResponseEntity.badRequest().body(null));
                })
                .onErrorResume(ServiceUnavailableException.class, e -> {
                    log.error("서비스 불가", e);
                    return Mono.just(ResponseEntity.status(503).body(null));
                })
                .onErrorResume(e -> {
                    log.error("예상치 못한 에러", e);
                    return Mono.just(ResponseEntity.status(500).body(null));
                });
    }
}