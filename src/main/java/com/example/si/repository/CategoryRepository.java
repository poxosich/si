package com.example.si.repository;

import com.example.si.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

  Optional<Category> findCategoryById(Integer integer);

    Optional<Category> findCategoryByName(String name);
}
