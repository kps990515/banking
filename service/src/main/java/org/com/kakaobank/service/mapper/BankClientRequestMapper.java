package org.com.kakaobank.service.mapper;

import org.com.kakaobank.client.dto.BankClientRequest;
import org.com.kakaobank.service.dto.TransferServiceRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BankClientRequestMapper {
    BankClientRequestMapper INSTANCE = Mappers.getMapper(BankClientRequestMapper.class);

    @Mapping(source = "fromAccountNumber", target = "accountNumber")
    @Mapping(expression = "java(request.getFromAccountBank() + request.getFromAccountNumber())", target = "comment")
    BankClientRequest toWithdrawRequest(TransferServiceRequest request);

    @Mapping(source = "toAccountNumber", target = "accountNumber")
    @Mapping(expression = "java(request.getToAccountBank() + request.getToAccountNumber())", target = "comment")
    BankClientRequest toDepositRequest(TransferServiceRequest request);

    @Mapping(source = "fromAccountNumber", target = "accountNumber")
    @Mapping(expression = "java(request.getFromAccountBank() + request.getFromAccountNumber())", target = "comment")
    BankClientRequest toUndoWithdrawalRequest(TransferServiceRequest request);
}
