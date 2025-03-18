package com.mobicomm.app.root.service;


import com.mobicomm.app.root.model.Transaction;
import com.mobicomm.app.root.model.PaymentStatus;
import com.mobicomm.app.root.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByUserId(String userId) {
        return transactionRepository.findByUserUserId(userId);
    }

    public String saveTransaction(Transaction transaction) {
        transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
        transaction.setStatus(PaymentStatus.PENDING);
        transactionRepository.save(transaction);
        return "Transaction Recorded Successfully!";
    }
}

