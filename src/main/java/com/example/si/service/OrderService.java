package com.example.si.service;

import com.example.si.dto.order.OrderResponse;

public interface OrderService {

    OrderResponse saveOrder(int id, int quantity, String email);
}
