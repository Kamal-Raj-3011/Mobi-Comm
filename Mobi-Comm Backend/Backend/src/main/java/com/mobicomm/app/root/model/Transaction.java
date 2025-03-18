package com.mobicomm.app.root.model;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor 
@Table(name = "transaction")
@JsonIgnoreProperties({"user", "recharge"})
public class Transaction {
    
    @Id
    @Column(nullable = false, length = 255)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false)
    private Timestamp transactionDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"transactions"})
    private User user;

    @OneToOne
    @JoinColumn(name = "recharge_id", nullable = false)
    private Recharge recharge;
}

