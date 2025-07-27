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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    //биру категори с помшю Id
    @Override
    public CategoryResponse findCategoryById(Integer id) {
        Optional<Category> categoryById = categoryRepository.findById(id);
        if (categoryById.isPresent()) {
            return categoryMapper.toDto(categoryById.get());
        }
        throw new CategoryNotFoundException("not found");
    }


    @Override
    public CategoryResponse addCategory(Category category) {
        if (categoryRepository.findCategoryByName(category.getName()).isPresent()) {
            throw new CategoryFoundException("Category already exists: " + category.getName());
        }
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    //с помшю этого метода вазврашаем все категори
    @Override
    public List<CategoryResponse> findAllCategory() {
        List<Category> all = categoryRepository.findAll();
        return categoryMapper.toDtoList(all);
    }
}
