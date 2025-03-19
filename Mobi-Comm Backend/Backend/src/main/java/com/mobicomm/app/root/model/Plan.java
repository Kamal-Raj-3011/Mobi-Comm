package com.mobicomm.app.root.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "plan")
public class Plan {
    @Id
    private String planId;
    
    @ManyToOne
    @JoinColumn(name = "cat_id", nullable = false)
    @JsonIgnoreProperties({"plans"})
    private Category category;
    
    private int price;
    private int validity;
    private String calls;
    private String data;
    private String message;
    
    @Column(name = "additional_feature")
    private String additionalFeature;
    
    private String tag;
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentHistory> paymentHistories;

}

