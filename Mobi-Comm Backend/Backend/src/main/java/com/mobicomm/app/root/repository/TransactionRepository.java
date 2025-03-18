package com.mobicomm.app.root.repository;


import com.mobicomm.app.root.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByUserUserId(String userId); // Get all transactions for a user
}

