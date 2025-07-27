package com.example.si.mapper.category;

import com.example.si.dto.category.CategoryResponse;
import com.example.si.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toDto(Category category);

    Category toResponseEntity(CategoryResponse categoryResponse);

    List<CategoryResponse> toDtoList(List<Category> categories);

}
