package com.example.si.controller.admin;


import com.example.si.dto.product.ProductResponse;
import com.example.si.service.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/v1/admin/product")
@RequiredArgsConstructor
public class ProductControllerAdmin {
    private final ProductService productService;

    //Добавление продукта
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> saveProduct(@RequestParam(name = "name") String name,
                                                       @RequestParam(name = "price") String price,
                                                       @RequestParam(name = "category") String category,
                                                       @RequestParam(name = "description") String description,
                                                       @RequestParam(name = "quantity") int quantity,
                                                       @RequestParam(name = "picName") MultipartFile multipartFile) {
        return ResponseEntity.ok(productService.save(name, price, category, description, quantity, multipartFile));
    }

     //Удаление продукта
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int id) {
        productService.deleteById(id);
    }

}
