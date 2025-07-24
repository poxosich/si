package com.example.si.mapper.product;

import com.example.si.dto.product.ProductRequest;
import com.example.si.dto.product.ProductResponse;
import com.example.si.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toDto(Product product);

    Product toEntity(ProductRequest productRequest);

    Product toResponseEntity(ProductResponse productResponse);

    List<ProductResponse> toDtoList(List<Product> productList);

}
