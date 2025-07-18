package com.example.si.service.impl;

import com.example.si.dto.basket.BaskedResponse;
import com.example.si.dto.product.ProductResponse;
import com.example.si.entity.Basket;
import com.example.si.entity.User;
import com.example.si.mapper.basket.BasketMapper;
import com.example.si.mapper.product.ProductMapper;
import com.example.si.repository.BasketRepository;
import com.example.si.service.BasketService;
import com.example.si.service.ProductService;
import com.example.si.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {
    private final BasketRepository basketRepository;
    private final BasketMapper basketMapper;
    private final UserService userService;
    private final ProductMapper productMapper;
    private final ProductService productService;


    @Override
    public BaskedResponse save(int id, String email, int quantity) {
        Optional<Basket> basketByProductId = basketRepository.findBasketByProductId(id);
        if (basketByProductId.isEmpty()) {
            ProductResponse productById = productService.findProductById(id);
            User byEmail = userService.findByEmail(email);
            return basketMapper.toDto(basketRepository.save(Basket.builder()
                    .product(productMapper.toGetEntity(productById))
                    .user(byEmail)
                    .quantity(quantity)
                    .build()));
        }
        return basketMapper.toDto(basketByProductId.get());
    }

    @Override
    public List<BaskedResponse> getAllByUserEmail(String email) {
        List<Basket> basketByUserEmail = basketRepository.findBasketByUserEmail(email);
        List<BaskedResponse> findAll = new ArrayList<>();
        for (Basket basket : basketByUserEmail) {
            findAll.add(basketMapper.toDto(basket));
        }
        return findAll;
    }

    @Override
    public int total(String email) {
        int totalPrice = 0;
        List<Basket> basketByUserEmail = basketRepository.findBasketByUserEmail(email);
        for (Basket basket : basketByUserEmail) {
            totalPrice += (int) (basket.getProduct().getPrice() * basket.getQuantity());
        }
        return totalPrice;
    }

    @Override
    @Transactional
    public void deleteBasketByProductID(int id) {
        basketRepository.deleteByProductId(id);
    }
}
