package com.googe.ssadm.sc.repository;

import com.googe.ssadm.sc.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {

    //for statistic
    List<Transaction> findAllByDebitTrue();
    List<Transaction> findAllByDebitFalse();
}
