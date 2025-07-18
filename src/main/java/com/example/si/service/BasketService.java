package com.example.si.service;

import com.example.si.dto.basket.BaskedResponse;

import java.util.List;

public interface BasketService {

    BaskedResponse save(int id, String email, int quantity);

    List<BaskedResponse> getAllByUserEmail(String email);

    int total(String email);

    void deleteBasketByProductID(int id);

}
