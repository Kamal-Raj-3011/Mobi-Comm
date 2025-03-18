package com.mobicomm.app.root.model;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recharge")
public class Recharge {
    
    @Id
    private String rechargeId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"recharges"})
    private User user;
 
    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    @JsonIgnoreProperties({"user", "transaction"})
    private Plan plan;

    @Column(nullable = false)
    private Timestamp rechargeDate;

    @Column(nullable = false)
    private Timestamp expiryDate;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @Column(nullable = false, length = 255)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @OneToOne(mappedBy = "recharge", cascade = CascadeType.ALL)
    private Transaction transaction;


}
