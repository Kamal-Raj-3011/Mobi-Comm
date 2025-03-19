package com.mobicomm.app.root.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_history")
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String payMethod;
    private Double amount;
    private LocalDateTime paymentDate;
    private LocalDateTime expiryDate;
    private String transactionId;

    @JsonIgnore // ✅ Prevent infinite loops
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnoreProperties("paymentHistories")
    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;
}
