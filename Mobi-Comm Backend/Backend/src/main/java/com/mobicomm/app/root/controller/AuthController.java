package com.mobicomm.app.root.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.mobicomm.app.root.model.Admin;
import com.mobicomm.app.root.security.JwtUtil;
import com.mobicomm.app.root.service.AdminService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://127.0.0.1:7000")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Admin admin) {
        try {
            // ✅ Authenticate the admin
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(admin.getEmail(), admin.getPassword())
            );

            // ✅ Generate Access Token (valid for 15 minutes)
            String accessToken = jwtUtil.generateToken(admin.getEmail(), 15);

            // ✅ Generate Refresh Token (valid for 7 days)
            String refreshToken = jwtUtil.generateToken(admin.getEmail(), 10080);

            // ✅ Return tokens in JSON format
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            return ResponseEntity.ok(tokens);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found");
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody Admin admin) {
        if (adminService.existsByEmail(admin.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists!");
        }
        adminService.saveAdmin(admin.getEmail(), admin.getPassword());
        return ResponseEntity.ok("Admin registered successfully!");
    }


}
