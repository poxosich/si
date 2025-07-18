package com.example.si.repository;

import com.example.si.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Integer> {

    Optional<Basket> findBasketByProductId(Integer id);

    List<Basket> findBasketByUserEmail(String email);

    void deleteByProductId(int id);
}
