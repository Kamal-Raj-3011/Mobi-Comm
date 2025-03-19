package com.mobicomm.app.root.service;


import com.mobicomm.app.root.model.PaymentHistory;
import com.mobicomm.app.root.model.User;
import com.mobicomm.app.root.model.Plan;
import com.mobicomm.app.root.repository.UserRepository;
import com.mobicomm.app.root.repository.PaymentHistoryRepository;
import com.mobicomm.app.root.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentHistoryService {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanRepository planRepository;
    
    @Autowired
    private EmailService emailService;

    // Save Payment History
    public PaymentHistory savePayment(String userId, String planId, PaymentHistory paymentHistory) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Plan> plan = planRepository.findById(planId);

        if (user.isPresent() && plan.isPresent()) {
            paymentHistory.setUser(user.get());
            paymentHistory.setPlan(plan.get());

            // ✅ Set Payment & Expiry Date (yyyy-MM-dd format)
            paymentHistory.setPaymentDate(LocalDateTime.now());
            LocalDateTime expiryDate = paymentHistory.getPaymentDate().plusDays(plan.get().getValidity());
            paymentHistory.setExpiryDate(expiryDate);

            // ✅ Save to DB
            PaymentHistory savedPayment = paymentHistoryRepository.save(paymentHistory);

            // ✅ Send Payment Confirmation Email
            emailService.sendPaymentConfirmation(
                user.get().getEmailId(),  // User email
                user.get().getName(),     // User name
                plan.get().getTag(),      // Plan name
                String.valueOf(plan.get().getPrice()), // Plan amount
                expiryDate.toLocalDate().toString(), // Expiry Date (yyyy-MM-dd format)
                savedPayment.getTransactionId() // Payment ID
            );

            return savedPayment;
        } else {
            throw new RuntimeException("User or Plan not found!");
        }
    }

    
    // Get Payment History by User ID
    public List<PaymentHistory> getPaymentHistoryByUserId(String userId) {
        return paymentHistoryRepository.findByUserUserId(userId);
    }

    // Get All Payment History
    public List<PaymentHistory> getAllPayments() {
        return paymentHistoryRepository.findAll();
    }
}

