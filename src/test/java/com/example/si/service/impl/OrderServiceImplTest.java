package com.example.si.service.impl;

import com.example.si.dto.order.OrderResponse;
import com.example.si.dto.product.ProductResponse;
import com.example.si.entity.Order;
import com.example.si.entity.Product;
import com.example.si.entity.User;
import com.example.si.mapper.order.OrderMapper;
import com.example.si.mapper.product.ProductMapper;
import com.example.si.repository.OrderRepository;
import com.example.si.service.BasketService;
import com.example.si.service.ProductService;
import com.example.si.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    OrderServiceImpl orderService;

    @Mock
    private  ProductService productService;
    @Mock
    private  UserService userService;
    @Mock
    private  OrderRepository orderRepository;
    @Mock
    private  ProductMapper productMapper;
    @Mock
    private  OrderMapper orderMapper;
    @Mock
    private  BasketService basketService;

    @Test
    void saveOrder() {
        int id = 3;
        int quantity = 3;
        String email = "arsenchiolkyan@gmail.com";

        ProductResponse productResponse = new ProductResponse();
        OrderResponse orderResponse = new OrderResponse();
        User user = new User();
        Product product = new Product();
        Order order = new Order();


        when(productService.findProductById(anyInt())).thenReturn(productResponse);
        when(userService.findByEmail(anyString())).thenReturn(user);
        when(productMapper.toResponseEntity(productResponse)).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponse);
        doNothing().when(basketService).deleteBasketByProductID(anyInt());

        OrderResponse orderResponseReturn = orderService.saveOrder(id, quantity, email);

        assertEquals(orderResponse, orderResponseReturn);
    }
}