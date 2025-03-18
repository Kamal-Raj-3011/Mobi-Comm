package com.mobicomm.app.root.service;

import com.mobicomm.app.root.model.User;
import com.mobicomm.app.root.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Generate a new User ID based on the last user
    private String generateUserId() {
        Optional<User> lastUser = userRepository.findTopByUserIdNotNullOrderByUserIdDesc();

        if (lastUser.isPresent()) {
            String lastId = lastUser.get().getUserId();
            try {
                int lastNumber = Integer.parseInt(lastId.substring(lastId.lastIndexOf("-") + 1)); // Handle userId format
                return String.format("MC_USER-%03d", lastNumber + 1); // Generate next ID with 3 digits
            } catch (NumberFormatException e) {
                System.out.println("Error parsing userId: " + lastId);
                return "MC_USER-001"; // Fallback to initial ID
            }
        } else {
            return "MC_USER-001"; // First ID when DB is empty
        }
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll(); // Fetches all users from the database
    }


    // Register a new user with password hashing (optional)
    public User registerUser(User user) {
        user.setUserId(generateUserId()); // Generate userId
        return userRepository.save(user); // Save the user to the database
    }
    

    public boolean authenticateUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmailId(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return password.equals(user.getPassword()); // Plain text comparison
        }
        return false;
    }




    // Find a user by email
    public Optional<User> getUserByEmail(String emailId) {
        return userRepository.findByEmailId(emailId);
    }

    // Find a user by mobile number
    public Optional<User> getUserByMobileNumber(String mobileNo) {
        return userRepository.findByMobileNo(mobileNo);
    }


    // Update user details
    public User updateUser(String userId, User userDetails) {
        if (userRepository.existsById(userId)) {
            userDetails.setUserId(userId); // Ensure the user ID is set to the existing one
            return userRepository.save(userDetails);
        }
        return null; // Return null if user doesn't exist
    }

    // Delete a user by ID
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
    
    public boolean doesMobileExist(String mobileNo) {
        return userRepository.existsByMobileNo(mobileNo);  // Check mobile number in the database
    }
    
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }
}

