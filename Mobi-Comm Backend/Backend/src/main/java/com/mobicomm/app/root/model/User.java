package com.mobicomm.app.root.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String userId;

    @Column(nullable = false, length = 225)
    private String name;

    @Column(nullable = false, length = 255)
    private String dob;

    @Column(nullable = false, length = 225)
    private String password;

    @Column(nullable = false, length = 225, unique = true)
    private String emailId;

    @Column(nullable = false)
    private String aadhar;

    @Column(nullable = false, length = 225)
    private String address;

    @Column(nullable = false)
    private int pincode;

    @Column(nullable = false, length = 225)
    private String city;

    @Column(nullable = false, length = 225)
    private String state;

    @Column(nullable = false)
    private String mobileNo;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentHistory> paymentHistories;


}
