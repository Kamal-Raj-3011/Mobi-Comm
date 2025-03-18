package com.mobicomm.app.root.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mobicomm.app.root.model.Category;
import com.mobicomm.app.root.model.Status;
import com.mobicomm.app.root.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    
    private String generateCategoryId() {
        Optional<Category> lastCategory = categoryRepository.findTopByCategoryIdNotNullOrderByCategoryIdDesc();

        if (lastCategory.isPresent()) {
            String lastId = lastCategory.get().getCategoryId();
            
            try {
                int lastNumber = Integer.parseInt(lastId.replace("MC_CAT-", ""));
                return String.format("MC_CAT-%03d", lastNumber + 1);
            } catch (NumberFormatException e) {
                System.out.println("Error parsing categoryId: " + lastId);
                return "MC_CAT-001"; // Fallback to initial ID
            }
        } else {
            return "MC_CAT-001"; // First ID when DB is empty
        }
    }


    public String saveCategory(Category category) {
    	category.setCategoryId(generateCategoryId());
    	if (category.getStatus() == null) {
    		category.setStatus(Status.ACTIVE);
    	}
    	categoryRepository.save(category);
    	return "Category Added Successfully!";
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category updateCategory(String id, Category category) {
        Category existingCategory = getCategoryById(id);
        existingCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }
    
    public void activateCategory(String id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setStatus(Status.ACTIVE);
            category.getPlans().forEach(plan -> plan.setStatus(Status.ACTIVE));
            categoryRepository.save(category);
            
         // Activate all associated plans
        } else {
            throw new RuntimeException("Category not found with ID: " + id);
        }
    }
    
    public void deactivateCategory(String id) {
        Optional<Category> optionalCategory= categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category= optionalCategory.get();
            category.setStatus(Status.INACTIVE);
            categoryRepository.save(category);
            
         // Activate all associated plans
            category.getPlans().forEach(plan -> plan.setStatus(Status.INACTIVE));
        } else {
            throw new RuntimeException("Category not found with ID: " + id);
        }
    }
    
    
} 