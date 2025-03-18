package com.mobicomm.app.root.repository;

import com.mobicomm.app.root.model.Recharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RechargeRepository extends JpaRepository<Recharge, String> {
    List<Recharge> findByUserUserId(String userId); // Get all recharges for a user\

    @Query("SELECT r FROM Recharge r WHERE r.expiryDate BETWEEN :startDate AND :endDate")
    List<Recharge> findExpiringRecharges(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT r FROM Recharge r JOIN FETCH r.plan WHERE r.user.userId = :userId")
    List<Recharge> findRechargesWithPlansByUserId(@Param("userId") String userId);
    
    @Query("SELECT r FROM Recharge r JOIN FETCH r.plan p JOIN FETCH p.category WHERE r.user.userId = :userId")
    List<Recharge> findRechargesWithPlansAndCategoriesByUserId(@Param("userId") String userId);



}

