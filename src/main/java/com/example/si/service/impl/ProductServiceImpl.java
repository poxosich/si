package com.example.si.service.impl;

import com.example.si.dto.product.ProductRequest;
import com.example.si.dto.product.ProductResponse;
import com.example.si.entity.Product;
import com.example.si.exeption.ProductNotFoundException;
import com.example.si.mapper.category.CategoryMapper;
import com.example.si.mapper.product.ProductMapper;
import com.example.si.repository.ProductRepository;
import com.example.si.service.CategoryService;
import com.example.si.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private static final int PAGE_SIZE = 12;

    @Value("${si.pic.package.url}")
    private String path;

    //этот метов придназначет для сахранени прадукта
    @Override
    public ProductResponse save(String name, String price, String category, String description, int quantity, MultipartFile multipartFile) {
        String picture;
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new RuntimeException("Is Empty");
        }
        picture = System.currentTimeMillis() + " " + multipartFile.getOriginalFilename();
        File file = new File(path, picture);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return productMapper.toDto(productRepository.save(productMapper.toEntity(ProductRequest.builder().name(name).price(Double.parseDouble(price)).category(categoryMapper.toResponseEntity(categoryService.findCategoryById(Integer.parseInt(category)))).description(description).picture(picture).active(true).quantity(quantity).build())));
    }

    //с помощью этого метода findTop12ByOrderBayDataTimeDesc() мы получим последние 12 новых прадуктв
    @Override
    public List<ProductResponse> findTop12ByOrderBayDataTimeDesc() {
        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE).withSort(Sort.by(Sort.Direction.DESC, "dataTime"));
        Page<Product> top12ByOrderByTimestampDesc = productRepository.findAll(pageRequest);
        return productMapper.toDtoList(top12ByOrderByTimestampDesc.getContent());
    }

    //этот метод придназначен для удаление прадукта с помшю Id
    @Override
    public void deleteById(int id) {
        productRepository.deleteById(id);
    }

    //этот метод вазврашает все прадукти с помшю category_Id
    @Override
    public List<ProductResponse> findProductsByCategoryId(int id) {
        List<Product> productsByCategoryId = productRepository.findProductsByCategoryId(id);
        return productMapper.toDtoList(productsByCategoryId);
    }

    //биру продукт спомшю ID.
    @Override
    public ProductResponse findProductById(int id) {
        Optional<Product> productById = productRepository.findProductById(id);
        if (productById.isPresent()) {
            return productMapper.toDto(productById.get());
        }
        throw new ProductNotFoundException("not found");
    }


    //этот метод вазврашает все прадукти
    @Override
    public List<ProductResponse> getAllProduct() {
        List<Product> all = productRepository.findAll();
        return productMapper.toDtoList(all);
    }

    //этот метод возвращает все продукты по названию.
    @Override
    public List<ProductResponse> findProductByName(String name) {
        List<Product> productByName = productRepository.findTop10ByNameContainingOrderByIdDesc(name);
        return productMapper.toDtoList(productByName);
    }
}
