package com.example.si.mapper.product;

import com.example.si.dto.product.ProductRequest;
import com.example.si.dto.product.ProductResponse;
import com.example.si.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toDto(Product product);

    Product toEntity(ProductRequest productRequest);

    Product toGetEntity(ProductResponse productResponse);

}
