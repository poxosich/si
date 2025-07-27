package com.example.si.service.impl;

import com.example.si.dto.category.CategoryResponse;
import com.example.si.entity.Category;
import com.example.si.exeption.CategoryFoundException;
import com.example.si.exeption.CategoryNotFoundException;
import com.example.si.mapper.category.CategoryMapper;
import com.example.si.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @Test
    void findCategoryById() {
        Integer id = 3;
        Category category = new Category();
        CategoryResponse categoryResponse = new CategoryResponse();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryService.findCategoryById(id)).thenReturn(categoryResponse);

        CategoryResponse categoryById = categoryService.findCategoryById(id);

        assertEquals(categoryResponse, categoryById);
        assertEquals(categoryResponse.getName(), categoryById.getName());
        assertEquals(categoryResponse.getDescription(), categoryById.getDescription());
    }

    @Test
    void findCategoryByIdIsEmpty() {
        Integer id = 3;

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.findCategoryById(id));
        verify(categoryRepository).findById(id);
        verifyNoInteractions(categoryMapper);

    }

    @Test
    void addCategory() {
        Category category = Category.builder()
                .id(1)
                .name("koshik")
                .description("kgjhfgnhkhfkg")
                .build();
        CategoryResponse categoryResponse = new CategoryResponse();

        when(categoryRepository.findCategoryByName(category.getName())).thenReturn(Optional.empty());
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryResponse);

        CategoryResponse categoryDto = categoryService.addCategory(category);

        assertEquals(categoryResponse.getId(), categoryDto.getId());
        assertEquals(categoryResponse.getName(), categoryDto.getName());
        assertEquals(categoryResponse.getDescription(), categoryDto.getDescription());

        verify(categoryRepository, times(1)).findCategoryByName(category.getName());
        verify(categoryMapper,times(1)).toDto(category);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void addCategoryIsPresent() {
        Category category = Category.builder()
                .id(1)
                .name("koshik")
                .description("kgjhfgnhkhfkg")
                .build();

        when(categoryRepository.findCategoryByName(category.getName())).thenReturn(Optional.of(category));
        assertThrows(CategoryFoundException.class, () -> categoryService.addCategory(category));

        verify(categoryRepository,never()).save(category);
        verify(categoryMapper,never()).toDto(category);
    }

    @Test
    void findAllCategory() {

        Category category1 = Category.builder().id(1).name("fdgdfg").description("bb").build();
        Category category2 = Category.builder().id(5).name("ars").description("llb").build();
        List<Category> categories = List.of(category1, category2);

        List<CategoryResponse> responses = List.of(
                CategoryResponse.builder().id(1).name("fdgdfg").description("bb").build(),
                CategoryResponse.builder().id(5).name("ars").description("llb").build());

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toDtoList(categories)).thenReturn(responses);

        List<CategoryResponse> result = categoryService.findAllCategory();

        assertEquals(2, result.size());
        assertEquals(Set.of(1, 5),
                result.stream().map(CategoryResponse::getId).collect(Collectors.toSet()));

    }
}