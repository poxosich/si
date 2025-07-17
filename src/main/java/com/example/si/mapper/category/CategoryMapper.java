package com.example.si.mapper.category;

import com.example.si.dto.category.CategoryRequest;
import com.example.si.dto.category.CategoryResponse;
import com.example.si.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryRequest categoryRequest);

    CategoryResponse toDto(Category category);

    Category toGetEntity(CategoryResponse categoryResponse);

}
