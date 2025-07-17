package com.example.si.controller;

import com.example.si.dto.basket.BaskedResponse;
import com.example.si.security.SpringUser;
import com.example.si.service.BasketService;
import com.example.si.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/v1/basket")
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;

    @PostMapping
    public String saveBasket(@RequestParam int productId, int quantity, @AuthenticationPrincipal SpringUser springUser) {
        if (springUser != null) {
            basketService.save(productId, springUser.getUsername(), quantity);
        }
        return "redirect:/v1/product/page/" + productId;
    }

    @PostMapping("/delete")
    @Transactional
    public String deleteBasketById(@RequestParam int productId) {
        basketService.deleteBasketByProductID(productId);
        return "redirect:/v1/";
    }

}
