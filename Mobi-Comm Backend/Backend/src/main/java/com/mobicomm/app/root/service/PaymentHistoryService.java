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

            // ✅ Keep manually provided `paymentDate` or set current time
            if (paymentHistory.getPaymentDate() == null) {
                paymentHistory.setPaymentDate(LocalDateTime.now());
            }

            // ✅ Set expiryDate only if not manually provided
            if (paymentHistory.getExpiryDate() == null) {
                LocalDateTime expiryDate = paymentHistory.getPaymentDate().plusDays(plan.get().getValidity());
                paymentHistory.setExpiryDate(expiryDate);
            }

            // ✅ Save to DB
            PaymentHistory savedPayment = paymentHistoryRepository.save(paymentHistory);

            // ✅ Send Payment Confirmation Email
            emailService.sendPaymentConfirmation(
                user.get().getEmailId(),
                user.get().getName(),
                plan.get().getTag(),
                String.valueOf(plan.get().getPrice()),
                savedPayment.getExpiryDate().toLocalDate().toString(),
                savedPayment.getTransactionId()
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

