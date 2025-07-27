package com.example.si.dto.liked;

import com.example.si.entity.Product;
import com.example.si.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikedResponse {

    private Integer id;
    private Product product;
    private User user;
}
