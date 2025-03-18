package com.mobicomm.app.root.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mobicomm.app.root.model.Plan;
import com.mobicomm.app.root.service.PlanService;
 
import java.util.List;

@RestController 
@RequestMapping("/api/plans")
@CrossOrigin(origins = "http://127.0.0.1:7000")
public class PlanController {
    
    @Autowired
    private PlanService planService;

    @PostMapping("/add")
    public ResponseEntity<String> addPlan(@RequestBody Plan plan) {
        return ResponseEntity.ok(planService.savePlan(plan));
    }

    @GetMapping("/")
    public ResponseEntity<List<Plan>> getAllPlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plan> getPlanById(@PathVariable String id) {
        return ResponseEntity.ok(planService.getPlanById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Plan> updatePlan(@PathVariable String id, @RequestBody Plan plan) {
        return ResponseEntity.ok(planService.updatePlan(id, plan));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePlan(@PathVariable String id) {
        planService.deletePlan(id);
        return ResponseEntity.ok("Plan deleted successfully");
    }
    
    @PutMapping("/{id}/active")
	public String activatePlan(@PathVariable String id) {
		planService.activatePlan(id);
		return "Plan "+id+" Activated Successfully";
	}
	
	@PutMapping("/{id}/inactive")
	public String deactivatePlan(@PathVariable String id) {
		planService.deactivatePlan(id);
		return "Plan "+id+" De-Activated Successfully";
	}
	
	@GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Plan>> getPlansByCategory(@PathVariable String categoryId) {
        List<Plan> plans = planService.getPlansByCategory(categoryId);
        return ResponseEntity.ok(plans);
    }
	
}
