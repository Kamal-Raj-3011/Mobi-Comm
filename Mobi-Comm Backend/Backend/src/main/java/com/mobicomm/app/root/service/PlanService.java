package com.mobicomm.app.root.service;


import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mobicomm.app.root.exception.InvalidRequestException;
import com.mobicomm.app.root.exception.ResourceNotFoundException;
import com.mobicomm.app.root.model.Plan;
import com.mobicomm.app.root.model.Status;
import com.mobicomm.app.root.repository.PlanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;
    
    private String generatePlanId() {
        Optional<Plan> lastPlan = planRepository.findTopByOrderByPlanIdDesc();

        if (lastPlan.isPresent()) {
            String lastId = lastPlan.get().getPlanId();

            // Extract the numeric part by removing all non-numeric characters
            String numericPart = lastId.replaceAll("[^0-9]", ""); 

            // Ensure the extracted part is not empty before parsing
            int lastNumber = numericPart.isEmpty() ? 0 : Integer.parseInt(numericPart); 
            int newNumber = lastNumber + 1;

            return String.format("MC_PLAN-%03d", newNumber); // Format as MC_PLAN-001, MC_PLAN-002, etc.
        } else {
            return "MC_PLAN-001"; // Initial ID
        }
    }


    public String savePlan(Plan plan) {
    	plan.setPlanId(generatePlanId());
    	if (plan.getStatus() == null) {
    		plan.setStatus(Status.ACTIVE);
    	}
    	planRepository.save(plan);
    	return "Plan Added Successfully!";
    }
    

    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    public Plan getPlanById(String id) {
        return planRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Plan not found with ID: " + id));
    }

    public Plan updatePlan(String id, Plan plan) {
        Plan existingPlan = getPlanById(id);
        existingPlan.setPrice(plan.getPrice());
        existingPlan.setValidity(plan.getValidity());
        existingPlan.setCalls(plan.getCalls());
        existingPlan.setData(plan.getData());
        existingPlan.setMessage(plan.getMessage());
        existingPlan.setAdditionalFeature(plan.getAdditionalFeature());
        existingPlan.setTag(plan.getTag());
        return planRepository.save(existingPlan);
    }

    public void deletePlan(String id) {
        planRepository.deleteById(id);
    }
    
    public void activatePlan(String id) {
        Plan plan = getPlanById(id); // Throws exception if not found
        if (plan.getStatus() == Status.ACTIVE) {
            throw new InvalidRequestException("Plan " + id + " is already active.");
        }
        plan.setStatus(Status.ACTIVE);
        planRepository.save(plan);
    }
    
    public void deactivatePlan(String id) {
        Plan plan = getPlanById(id); // Throws exception if not found
        if (plan.getStatus() == Status.INACTIVE) {
            throw new InvalidRequestException("Plan " + id + " is already inactive.");
        }
        plan.setStatus(Status.INACTIVE);
        planRepository.save(plan);
    }
    
    // Method to fetch plans by category ID
    public List<Plan> getPlansByCategory(String categoryId) {
        return planRepository.findByCategoryCategoryId(categoryId);  // Query by category ID
    }
    
   
    
}

