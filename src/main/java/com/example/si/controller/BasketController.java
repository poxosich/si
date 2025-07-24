package com.example.si.controller;

import com.example.si.security.SpringUser;
import com.example.si.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/v1/basket")
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;

    // когда кладешь продукт в корзину
    @PostMapping
    public String saveBasket(@RequestParam int productId, int quantity, @AuthenticationPrincipal SpringUser springUser) {
        if (springUser != null) {
            basketService.save(productId, springUser.getUsername(), quantity);
        }
        return "redirect:/v1/product/page/" + productId;
    }

    //удаление продукта с помошью ID продукта
    @PostMapping("/delete")
    public String deleteBasketById(@RequestParam int productId) {
        basketService.deleteBasketByProductID(productId);
        return "redirect:/v1/";
    }

}
