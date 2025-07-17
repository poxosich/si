package com.example.si.service.impl;

import com.example.si.dto.category.CategoryResponse;
import com.example.si.entity.Category;
import com.example.si.exeption.CategoryFoundException;
import com.example.si.exeption.CategoryNotFoundException;
import com.example.si.mapper.category.CategoryMapper;
import com.example.si.repository.CategoryRepository;
import com.example.si.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse findCategoryById(Integer id) {
        Optional<Category> categoryById = categoryRepository.findCategoryById(id);
        if (categoryById.isPresent()) {
            return categoryMapper.toDto(categoryById.get());
        }
        throw new CategoryNotFoundException("not found");
    }


    @Override
    public Category findCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + name));
    }

    @Override
    public CategoryResponse addCategory(Category category) {
        if (categoryRepository.findCategoryByName(category.getName()).isPresent()) {
            throw new CategoryFoundException("Category already exists: " + category.getName());
        }
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryResponse> findAllCategory() {
        List<Category> all = categoryRepository.findAll();
        List<CategoryResponse> allResponse = new ArrayList<>();
        for (Category category : all) {
            allResponse.add(categoryMapper.toDto(category));
        }
        return allResponse;
    }
}
