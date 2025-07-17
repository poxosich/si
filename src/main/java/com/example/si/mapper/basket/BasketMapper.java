package com.example.si.mapper.basket;

import com.example.si.dto.basket.BaskedResponse;
import com.example.si.entity.Basket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BasketMapper {

    BaskedResponse toDto(Basket basket);


}
