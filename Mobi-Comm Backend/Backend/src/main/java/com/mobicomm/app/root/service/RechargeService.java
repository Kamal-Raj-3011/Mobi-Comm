package com.mobicomm.app.root.service;

import com.mobicomm.app.root.model.*;
import com.mobicomm.app.root.repository.RechargeRepository;
import com.mobicomm.app.root.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class RechargeService {

    @Autowired
    private RechargeRepository rechargeRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private String generateRechargeId() {
        long count = rechargeRepository.count() + 1;
        return String.format("MC_RECHARGE-%03d", count);
    }

    private String generateTransactionId() {
        return "TXN" + new Random().nextInt(100000);
    }

    public String saveRecharge(Recharge recharge) {
        recharge.setRechargeId(generateRechargeId());
        recharge.setRechargeDate(new Timestamp(System.currentTimeMillis()));

        // Calculate Expiry Date based on plan validity
        Plan plan = recharge.getPlan();
        int validityDays = extractValidityDays(plan.getValidity()); 
        Timestamp expiryDate = new Timestamp(System.currentTimeMillis() + (validityDays * 24L * 60 * 60 * 1000));
        recharge.setExpiryDate(expiryDate);

        // Create Transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));
        transaction.setStatus(PaymentStatus.SUCCESS);  // Dummy Payment = Always Success
        transaction.setUser(recharge.getUser());
        transaction.setRecharge(recharge);

        // Set Recharge Status & Save Both Recharge & Transaction
        recharge.setTransactionId(transaction.getTransactionId());
        recharge.setPaymentStatus(PaymentStatus.SUCCESS);
        rechargeRepository.save(recharge);
        transactionRepository.save(transaction);

        return "Recharge Successful! Transaction ID: " + transaction.getTransactionId();
    }

    private int extractValidityDays(int validity) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Recharge> getAllRecharges() {
        return rechargeRepository.findAll();
    }

    public List<Recharge> getRechargesByUserId(String userId) {
        return rechargeRepository.findByUserUserId(userId);
    }

    // Utility function to extract validity days from string like "28 days"
    private int extractValidityDays(String validity) {
        try {
            return Integer.parseInt(validity.split(" ")[0]);
        } catch (Exception e) {
            return 30; // Default to 30 days if parsing fails
        }
    }
    
    public List<Recharge> getExpiringRecharges() {
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime expiryLimit = currentDate.plusDays(3);
        return rechargeRepository.findExpiringRecharges(currentDate, expiryLimit);
    }
    
    public List<Recharge> getRechargesByUserIdWithPlans(String userId) {
        return rechargeRepository.findRechargesWithPlansByUserId(userId);  // Modify repository to join and fetch plan details
    }
    
    public List<Recharge> getRechargesWithPlansAndCategories(String userId) {
        return rechargeRepository.findRechargesWithPlansAndCategoriesByUserId(userId);
    }


}
