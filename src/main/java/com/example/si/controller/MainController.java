package com.example.si.controller;

import com.example.si.dto.basket.BaskedResponse;
import com.example.si.dto.category.CategoryResponse;
import com.example.si.dto.liked.LikedResponse;
import com.example.si.dto.product.ProductResponse;
import com.example.si.security.SpringUser;
import com.example.si.service.BasketService;
import com.example.si.service.CategoryService;
import com.example.si.service.LikedService;
import com.example.si.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final LikedService likedService;
    private final BasketService basketService;

    //главни страница
    @GetMapping("/")
    public String home(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        List<ProductResponse> newProducts = productService.findTop12ByOrderBayDataTimeDesc();
        List<CategoryResponse> allCategory = categoryService.findAllCategory();
        modelMap.put("newProducts", newProducts);
        modelMap.put("allCategory", allCategory);
        if (springUser != null) {
            List<LikedResponse> likedByUserEmail = likedService.findLikedByUserEmail(springUser.getUsername());
            List<BaskedResponse> allByUserEmail = basketService.getAllByUserEmail(springUser.getUsername());
            double total = basketService.total(springUser.getUsername());
            modelMap.put("liked", likedByUserEmail);
            modelMap.put("likedSze", likedService.countByUserEmail(springUser.getUsername()));
            modelMap.put("basketSize", basketService.countByUserEmail(springUser.getUsername()));
            modelMap.put("basket", allByUserEmail);
            modelMap.put("total", total);
            modelMap.put("check", true);
        }
        return "home";
    }
}
