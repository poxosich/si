package com.example.si.service.impl;

import com.example.si.dto.category.CategoryResponse;
import com.example.si.dto.product.ProductRequest;
import com.example.si.dto.product.ProductResponse;
import com.example.si.entity.Category;
import com.example.si.entity.Product;
import com.example.si.exeption.ProductNotFoundException;
import com.example.si.mapper.category.CategoryMapper;
import com.example.si.mapper.product.ProductMapper;
import com.example.si.repository.CategoryRepository;
import com.example.si.repository.ProductRepository;
import com.example.si.service.CategoryService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl productService;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private MultipartFile multipartFile;
    @Mock
    CategoryRepository categoryRepository;

    @SneakyThrows
    @Test
    void save() {
        String name = "pen";
        String price = "1145";
        String description = "hfghfghfgh  gfh";
        String category = "5";
        int quantity = 3;
        String originalFileName = "image.jpg";

        ProductResponse productResponse = new ProductResponse();
        Product product = new Product();
        CategoryResponse categoryResponse = new CategoryResponse();
        Category cate = new Category();

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn(originalFileName);
        doNothing().when(multipartFile).transferTo(any(File.class));

        when(categoryRepository.findCategoryById(Integer.parseInt(category))).thenReturn(Optional.of(cate));
        when(categoryMapper.toDto(cate)).thenReturn(categoryResponse);
        when(productMapper.toEntity(any(ProductRequest.class))).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productResponse);

        ProductResponse save = productService.save(name, price, category, description, quantity, multipartFile);

        assertEquals(productResponse, save);
        verify(multipartFile).transferTo(any(File.class));
    }

    @SneakyThrows
    @Test
    void saveTransferTo() {
        String originalFileName = "fail.jpg";

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn(originalFileName);
        doThrow(new IOException("disk error")).when(multipartFile).transferTo(any(File.class));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productService.save("pen", "100", "1", "desc", 1, multipartFile));
        assertTrue(exception.getCause() instanceof IOException);
    }


    @Test
    void saveISEmpty() {
        when(multipartFile.isEmpty()).thenReturn(true);
        assertThrows(RuntimeException.class, () -> productService.save("pen", "100", "1", "desc", 1, multipartFile));
    }

    @Test
    void saveIsNull() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productService.save("name", "123", "1", "desc", 1, null));
        assertEquals("Is Empty", exception.getMessage());
    }

    @Test
    void findTop12ByOrderBayDataTimeDesc() {
        List<Product> products = List.of(new Product(), new Product());
        Page<Product> page = new PageImpl<>(products);
        List<ProductResponse> responses = List.of(new ProductResponse(), new ProductResponse());

        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(productMapper.toDtoList(products)).thenReturn(responses);

        List<ProductResponse> result = productService.findTop12ByOrderBayDataTimeDesc();

        assertEquals(2, result.size());
        assertEquals(responses, result);
        verify(productRepository).findAll(any(Pageable.class));
        verify(productMapper).toDtoList(products);
    }

    @Test
    void deleteById() {
        productService.deleteById(anyInt());
        verify(productRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void findProductsByCategoryId() {
        Product product = new Product();
        Product product2 = new Product();
        List<Product> products = List.of(product, product2);
        ProductResponse productResponse = new ProductResponse();
        ProductResponse productResponse2 = new ProductResponse();

        when(productMapper.toDto(product)).thenReturn(productResponse);
        when(productMapper.toDto(product2)).thenReturn(productResponse2);
        List<ProductResponse> productResponses = List.of(productResponse, productResponse2);

        when(productRepository.findProductsByCategoryId(anyInt())).thenReturn(products);
        when(productMapper.toDtoList(products)).thenReturn(productResponses);

        List<ProductResponse> productsByCategoryId = productService.findProductsByCategoryId(anyInt());
        assertEquals(productsByCategoryId, productResponses);

    }

    @Test
    void findProductById() {
        when(productRepository.findProductById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.findProductById(anyInt()));
    }

    @Test
    void findProductByIdIsPresent() {
        Product product = new Product();
        ProductResponse productResponse = new ProductResponse();
        when(productRepository.findProductById(anyInt())).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productResponse);
        ProductResponse productById = productService.findProductById(anyInt());

        assertEquals(productResponse, productById);
    }

    @Test
    void getAllProduct() {
        Product product = new Product();
        Product product2 = new Product();
        ProductResponse productResponse = new ProductResponse();
        ProductResponse productResponse2 = new ProductResponse();
        List<Product> products = List.of(product, product2);
        when(productRepository.findAll()).thenReturn(products);
        List<ProductResponse> productResponses = List.of(productResponse, productResponse2);
        when(productMapper.toDtoList(products)).thenReturn(productResponses);

        List<ProductResponse> allProduct = productService.getAllProduct();

        assertEquals(productResponses, allProduct);
        verify(productRepository).findAll();
        verify(productMapper).toDtoList(products);
    }

    @Test
    void findProductByName() {
        when(productRepository.findTop10ByNameContainingOrderByIdDesc("pen")).thenReturn(List.of(new Product()));
        when(productMapper.toDtoList(anyList())).thenReturn(List.of(new ProductResponse()));

        List<ProductResponse> result = productService.findProductByName("pen");

        assertEquals(1, result.size());
    }
}