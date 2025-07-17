package com.example.si.service.impl;

import com.example.si.dto.order.OrderResponse;
import com.example.si.dto.product.ProductResponse;
import com.example.si.entity.Order;
import com.example.si.entity.User;
import com.example.si.mapper.order.OrderMapper;
import com.example.si.mapper.product.ProductMapper;
import com.example.si.mapper.user.UserMapper;
import com.example.si.repository.OrderRepository;
import com.example.si.service.BasketService;
import com.example.si.service.OrderService;
import com.example.si.service.ProductService;
import com.example.si.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ProductService productService;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final BasketService basketService;


    @Override
    @Transactional
    public OrderResponse saveOrder(int id, int quantity, String email) {
        ProductResponse productById = productService.findProductById(id);
        User byEmail = userService.findByEmail(email);
        Order save = orderRepository.save(Order.builder()
                .product(productMapper.toGetEntity(productById))
                .user(byEmail)
                .quantity(quantity)
                .data(LocalDateTime.now())
                .build());
        basketService.deleteBasketByProductID(id);
        return orderMapper.toDto(save);
    }
}
