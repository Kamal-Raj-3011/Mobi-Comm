package com.mobicomm.app.root.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobicomm.app.root.exception.DuplicateResourceException;
import com.mobicomm.app.root.exception.InvalidRequestException;
import com.mobicomm.app.root.exception.ResourceNotFoundException;
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
        Optional<Category> existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (existingCategory.isPresent()) {
            throw new DuplicateResourceException("Category with name '" + category.getCategoryName() + "' already exists.");
        }

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
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
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
        Category category = getCategoryById(id);  // Throws exception if not found
        if (category.getStatus() == Status.ACTIVE) {
            throw new InvalidRequestException("Category " + id + " is already active.");
        }

        category.setStatus(Status.ACTIVE);
        category.getPlans().forEach(plan -> plan.setStatus(Status.ACTIVE));
        categoryRepository.save(category);
    }

    
    public void deactivateCategory(String id) {
        Category category = getCategoryById(id); // Throws exception if not found
        if (category.getStatus() == Status.INACTIVE) {
            throw new InvalidRequestException("Category " + id + " is already inactive.");
        }

        category.setStatus(Status.INACTIVE);
        category.getPlans().forEach(plan -> plan.setStatus(Status.INACTIVE));
        categoryRepository.save(category);
    }
    
    
} 