package com.example.si.controller;

import com.example.si.dto.category.CategoryResponse;
import com.example.si.dto.product.ProductResponse;
import com.example.si.security.SpringUser;
import com.example.si.service.CategoryService;
import com.example.si.service.LikedService;
import com.example.si.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/shop")
public class ShopController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final LikedService likedService;


    //предназначен для перехода на страницу магазина
    @GetMapping
    public String shopPage(ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        List<ProductResponse> allProduct = productService.getAllProduct();
        List<CategoryResponse> allCategory = categoryService.findAllCategory();
        modelMap.put("allCategory", allCategory);
        modelMap.put("products", allProduct);
        likedService.checkUser(springUser, modelMap);
        return "shopPage";
    }


    //Чтобы получить товар по категории на странице магазина
    @GetMapping("/{id}")
    public String productByCategoryId(@PathVariable int id, ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        List<CategoryResponse> allCategory = categoryService.findAllCategory();
        List<ProductResponse> productsByCategoryId = productService.findProductsByCategoryId(id);
        modelMap.put("products", productsByCategoryId);
        modelMap.put("allCategory", allCategory);
        likedService.checkUser(springUser, modelMap);
        return "shopPage";
    }

    //для добавления избрнних из страници магазин
    @GetMapping("/liked/{id}")
    public String addLikedSopPage(@PathVariable int id, @AuthenticationPrincipal SpringUser springUser, ModelMap modelMap) {
        List<ProductResponse> allProduct = productService.getAllProduct();
        List<CategoryResponse> allCategory = categoryService.findAllCategory();
        modelMap.put("allCategory", allCategory);
        modelMap.put("products", allProduct);
        if (springUser != null) {
            likedService.save(id, springUser.getUsername());
            likedService.checkUser(springUser, modelMap);
        }
        return "shopPage";
    }

    //для удалени избрнних для страгиц shop
    @GetMapping("/delete{id}")
    public String deleteLikedById(@PathVariable int id) {
        likedService.deleteById(id);
        return "redirect:/v1/shop";
    }

    //для поиска прадукта (կենկրետ պոիսկն է)
    @GetMapping("/search")
    public String searchProductByName(@RequestParam String name, ModelMap modelMap) {
        List<ProductResponse> productByName = productService.findProductByName(name);
        List<CategoryResponse> allCategory = categoryService.findAllCategory();
        modelMap.put("allCategory", allCategory);
        modelMap.put("products", productByName.stream());
        return "shopPage";
    }
}
