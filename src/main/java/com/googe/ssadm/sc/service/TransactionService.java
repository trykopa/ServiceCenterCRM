package com.googe.ssadm.sc.service;

import com.googe.ssadm.sc.entity.Transaction;
import com.googe.ssadm.sc.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepo transactionRepo;

    @Autowired
    public TransactionService(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TransactionService.class);

    public Optional<Transaction> findById(Long id){
        log.info("TransactionService: findById");
        return transactionRepo.findById(id);
    }

    public Page<Transaction> findAllPageable(Pageable pageable){
        log.info("TransactionService: findAll pageable");
        return transactionRepo.findAll(pageable);
    }

    public Transaction save(Transaction transaction){
        log.info("TransactionService: save {}", transaction.getDescription());
        return transactionRepo.save(transaction);
    }

    public void delete(Long id){
        log.info("TransactionService: delete {}", id);
        transactionRepo.deleteById(id);
    }

    //for statistic
    public List<Transaction> findAllIncome(){
        log.info("TransactionService: findAllIncome");
        return transactionRepo.findAllByDebitTrue();
    }

    public List<Transaction> findAllExpense(){
        log.info("TransactionService: findAllExpense");
        return transactionRepo.findAllByDebitFalse();
    }

}
