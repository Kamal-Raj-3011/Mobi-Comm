package com.mobicomm.app.root.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobicomm.app.root.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
	Optional<Category> findTopByCategoryIdNotNullOrderByCategoryIdDesc();

}  