package com.example.si.service.impl;

import com.example.si.dto.product.ProductRequest;
import com.example.si.dto.product.ProductResponse;
import com.example.si.entity.Product;
import com.example.si.exeption.ProductNodFountException;
import com.example.si.mapper.category.CategoryMapper;
import com.example.si.mapper.product.ProductMapper;
import com.example.si.repository.ProductRepository;
import com.example.si.service.CategoryService;
import com.example.si.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private static final String MASSAGE = "is Empty";

    @Value("${si.pic.package.url}")
    private String path;

    @Override
    public ProductResponse save(String name, String price, String category, String description, int quantity, MultipartFile multipartFile) {
        String picture;
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new RuntimeException(MASSAGE);
        }
        picture = System.currentTimeMillis() + " " + multipartFile.getOriginalFilename();
        File file = new File(path, picture);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Product entity = productMapper.toEntity(ProductRequest.builder()
                .name(name)
                .price(Double.parseDouble(price))
                .category(categoryMapper.toGetEntity(categoryService.findCategoryById(Integer.parseInt(category))))
                .description(description)
                .picture(picture)
                .active(true)
                .quantity(quantity)
                .build());
        Product save = productRepository.save(entity);
        return productMapper.toDto(save);
    }

    @Override
    public List<ProductResponse> findTop10ByOrderBayDataTimeDesc() {
        List<Product> top10ByOrderByTimestampDesc = productRepository.findTop12ByOrderByDataTimeDesc();
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : top10ByOrderByTimestampDesc) {
            productResponses.add(productMapper.toDto(product));
        }
        return productResponses;
    }

    @Override
    public void deleteById(int id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductResponse> findProductsByCategoryId(int id) {
        List<Product> productsByCategoryId = productRepository.findProductsByCategory_Id(id);
        List<ProductResponse> productByCategory = new ArrayList<>();
        for (Product product : productsByCategoryId) {
            productByCategory.add(productMapper.toDto(product));
        }
        return productByCategory;
    }

    @Override
    public ProductResponse findProductById(int id) {
        Optional<Product> productById = productRepository.findProductById(id);
        if (productById.isPresent()) {
            return productMapper.toDto(productById.get());
        }
        throw new ProductNodFountException("not found");
    }

    @Override
    public List<ProductResponse> getAllProduct() {
        List<Product> all = productRepository.findAll();
        List<ProductResponse> allProduct = new ArrayList<>();
        for (Product product : all) {
            allProduct.add(productMapper.toDto(product));
        }
        return allProduct;
    }

    @Override
    public List<ProductResponse> findProductByName(String name) {
        List<Product> productByName = productRepository.findTop10ByNameContainingOrderByIdDesc(name);
        List<ProductResponse> searchProducts = new ArrayList<>();
        for (Product product : productByName) {
            searchProducts.add(productMapper.toDto(product));
        }
        return searchProducts;
    }


}
