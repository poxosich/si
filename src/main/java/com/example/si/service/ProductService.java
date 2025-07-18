package com.example.si.service;


import com.example.si.dto.product.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ProductService {

    ProductResponse save(String name, String price, String category, String description, int quantity, MultipartFile multipartFile);

    List<ProductResponse> findTop12ByOrderBayDataTimeDesc();

    void deleteById(int id);

    List<ProductResponse> findProductsByCategoryId(int id);

    ProductResponse findProductById(int id);

    List<ProductResponse> getAllProduct();

    List<ProductResponse> findProductByName(String name);


}
