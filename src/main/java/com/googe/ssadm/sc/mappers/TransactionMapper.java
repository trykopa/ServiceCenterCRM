package com.googe.ssadm.sc.mappers;

import com.googe.ssadm.sc.dto.TransactionDTO;
import com.googe.ssadm.sc.entity.Transaction;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface TransactionMapper {
    Transaction toTransaction(TransactionDTO transactionDTO);
    TransactionDTO toTransactionDTO(Transaction transaction);
}
