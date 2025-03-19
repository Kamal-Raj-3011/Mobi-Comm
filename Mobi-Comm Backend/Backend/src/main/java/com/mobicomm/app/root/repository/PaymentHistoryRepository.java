package com.mobicomm.app.root.repository;

import com.mobicomm.app.root.model.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    
//    // Find all payments made by a specific user
//    List<PaymentHistory> findByUserUserId(String userId);
    
    // Find all payments related to a specific plan
    List<PaymentHistory> findByPlanPlanId(String planId);
    
    
    @Query("SELECT p FROM PaymentHistory p JOIN FETCH p.plan WHERE p.user.userId = :userId")
    List<PaymentHistory> findByUserUserId(@Param("userId") String userId);


}

