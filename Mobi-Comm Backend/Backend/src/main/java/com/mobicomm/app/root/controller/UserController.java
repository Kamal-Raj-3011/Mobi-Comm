package com.mobicomm.app.root.controller;

import com.mobicomm.app.root.model.User;
import com.mobicomm.app.root.repository.UserRepository;
import com.mobicomm.app.root.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://127.0.0.1:7000")
public class UserController {

    @Autowired
    private UserService userService;
    private UserRepository userRepository;

 // Get All Users
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    
    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            User savedUser = userService.registerUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();  // Logs the error to the console
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        boolean isAuthenticated = userService.authenticateUser(user.getEmailId(), user.getPassword());

        if (isAuthenticated) {
            return ResponseEntity.ok("Login Successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
    }




    // Get User by Name
    @GetMapping("/name/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable String name) {
        Optional<User> user = userService.findByName(name);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get User by Email
    @GetMapping("/email/{emailId}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String emailId) {
        return userService.getUserByEmail(emailId)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    // Get User by Mobile Number
    @GetMapping("/mobile/{mobileNo}")
    public ResponseEntity<User> getUserByMobile(@PathVariable String mobileNo) {
        return userService.getUserByMobileNumber(mobileNo)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    // Update User Details
    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(userId, userDetails);
        return updatedUser != null
                ? new ResponseEntity<>(updatedUser, HttpStatus.OK)
                : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    // Delete User
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable String userId) {
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/validateMobile/{mobileNo}")
    public ResponseEntity<Map<String, Boolean>> validateMobile(@PathVariable String mobileNo) {
        boolean exists = userService.doesMobileExist(mobileNo);

        // Return a response with a 'exists' flag indicating whether the mobile number exists
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);

        if (exists) {
            return ResponseEntity.ok(response);  // Mobile number found
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);  // Mobile number not found
        }
    }
    
    @GetMapping("/details/{mobileNumber}")
    public ResponseEntity<User> getUserDetailsByMobile(@PathVariable String mobileNumber) {
        User user = userService.getUserDetailsByMobile(mobileNumber);
        return ResponseEntity.ok(user);
    }

    
    
}
