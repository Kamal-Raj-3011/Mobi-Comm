package com.mobicomm.app.root.controller;


import com.mobicomm.app.root.model.PaymentHistory;
import com.mobicomm.app.root.service.PaymentHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-history")
@CrossOrigin(origins = "*")
public class PaymentHistoryController {

    @Autowired
    private PaymentHistoryService paymentHistoryService;

    // Add a new payment history
    @PostMapping("/{userId}/{planId}")
    public ResponseEntity<PaymentHistory> createPayment(
            @PathVariable String userId,
            @PathVariable String planId,
            @RequestBody PaymentHistory paymentHistory) {
        
        PaymentHistory savedPayment = paymentHistoryService.savePayment(userId, planId, paymentHistory);
        return ResponseEntity.ok(savedPayment);
    }

    // Get payment history by User ID
    @GetMapping("/{userId}")
    public ResponseEntity<List<PaymentHistory>> getUserPaymentHistory(@PathVariable String userId) {
        List<PaymentHistory> paymentHistory = paymentHistoryService.getPaymentHistoryByUserId(userId);
        return ResponseEntity.ok(paymentHistory);
    }

    // Get all payment histories
    @GetMapping
    public ResponseEntity<List<PaymentHistory>> getAllPayments() {
        List<PaymentHistory> allPayments = paymentHistoryService.getAllPayments();
        return ResponseEntity.ok(allPayments);
    }
    
    
}

