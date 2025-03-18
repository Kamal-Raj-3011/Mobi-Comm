package com.mobicomm.app.root.repository;

import com.mobicomm.app.root.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findTopByUserIdNotNullOrderByUserIdDesc(); // Get the most recent user by userId

    Optional<User> findByEmailId(String emailId); // Find user by email

    Optional<User> findByMobileNo(String mobileNo); // Find user by mobile number

    Optional<User> findByName(String name); // Find user by name

    boolean existsByMobileNo(String mobileNo);
    

}
