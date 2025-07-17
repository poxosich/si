package com.example.si.dto.order;

import com.example.si.entity.Product;
import com.example.si.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    private Product product;
    private User user;
    private Integer quantity;
    private LocalDateTime data;

}
