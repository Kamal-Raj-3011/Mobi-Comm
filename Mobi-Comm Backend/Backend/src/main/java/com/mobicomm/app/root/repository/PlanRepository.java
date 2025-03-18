package com.mobicomm.app.root.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobicomm.app.root.model.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, String> {
	 Optional<Plan> findTopByOrderByPlanIdDesc();
	 
	 // Custom query method to find plans by category ID
	    List<Plan> findByCategoryCategoryId(String categoryId);
}
