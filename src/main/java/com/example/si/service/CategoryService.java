package com.example.si.service;

import com.example.si.dto.category.CategoryResponse;
import com.example.si.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    CategoryResponse findCategoryById(Integer integer);

    CategoryResponse addCategory(Category category);

    List<CategoryResponse> findAllCategory();

}
