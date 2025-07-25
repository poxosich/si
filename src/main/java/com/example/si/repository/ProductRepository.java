package com.example.si.repository;

import com.example.si.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findProductsByCategoryId(int id);

    Optional<Product> findProductById(int id);

    List<Product> findTop10ByNameContainingOrderByIdDesc(String namePart);

}
