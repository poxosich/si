package com.example.si.mapper.order;

import com.example.si.dto.order.OrderResponse;
import com.example.si.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

OrderResponse toDto(Order order);
}
