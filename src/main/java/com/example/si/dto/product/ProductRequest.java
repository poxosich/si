package com.example.si.dto.product;

import com.example.si.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {

    private String name;
    private Double price;
    private String picture;
    private Category category;
    private LocalDateTime dataTime;
    private Boolean active;
    private String description;
    private Integer quantity;
}
