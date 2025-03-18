package com.mobicomm.app.root.controller;


import com.mobicomm.app.root.model.Recharge;
import com.mobicomm.app.root.service.RechargeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recharges")
@CrossOrigin(origins = "http://127.0.0.1:7000")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;
    
    @PostMapping("/add")
    public ResponseEntity<?> addRecharge(@RequestBody Recharge recharge) {
        System.out.println("Received Recharge Data: " + recharge); // ✅ Debugging
        String savedRecharge = rechargeService.saveRecharge(recharge);
        return ResponseEntity.ok(savedRecharge);
    }



    @GetMapping("/")
    public ResponseEntity<List<Recharge>> getAllRecharges() {
        return ResponseEntity.ok(rechargeService.getAllRecharges());
    }

//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Recharge>> getRechargesByUser(@PathVariable String userId) {
//        return ResponseEntity.ok(rechargeService.getRechargesByUserId(userId));
//    }
    
    @GetMapping("/expiring-plans")
    public ResponseEntity<List<Recharge>> getExpiringPlans() {
        return ResponseEntity.ok(rechargeService.getExpiringRecharges());
    }

 // Modify the existing endpoint to fetch recharges along with plan details
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recharge>> getRechargesByUser(@PathVariable String userId) {
        List<Recharge> recharges = rechargeService.getRechargesWithPlansAndCategories(userId);
        return ResponseEntity.ok(recharges);
    }


    
}
