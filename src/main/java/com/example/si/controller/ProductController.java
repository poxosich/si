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
@RequestMapping("/v1/product")
public class ProductController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final LikedService likedService;


    @GetMapping("/{id}")
    public String getProductsByCategoryId(@PathVariable int id, ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser) {
        List<CategoryResponse> allCategory = categoryService.findAllCategory();
        modelMap.put("allCategory", allCategory);
        List<ProductResponse> productsByCategoryId = productService.findProductsByCategoryId(id);
        modelMap.put("newProducts", productsByCategoryId);
        likedService.checkUser(springUser,modelMap);
        return "home";
    }

    @GetMapping("/page/{id}")
    public String productPage (@PathVariable int id, ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser){
        ProductResponse productById = productService.findProductById(id);
        modelMap.put("product", productById);
       likedService.checkUser(springUser, modelMap);
        return "productPage";
    }

    @GetMapping("/liked/{id}")
    public String addLikedProductPage(@PathVariable int id, @AuthenticationPrincipal SpringUser springUser, ModelMap modelMap){
        ProductResponse productById = productService.findProductById(id);
        modelMap.put("product", productById);
        if (springUser != null){
            likedService.save(id,springUser.getUsername());
            likedService.checkUser(springUser, modelMap);
        }
        return "productPage";
    }


    @GetMapping("/delete{id}")
    public String deleteLikedById(@PathVariable int id){
        likedService.deleteById(id);
        return "redirect:/v1/";
    }

    @GetMapping("/product-page/delete")
    public String productPageDeleteLikedById(@RequestParam("id") int id, @RequestParam("productId") int productId, ModelMap modelMap, @AuthenticationPrincipal SpringUser springUser){
        likedService.delete(id);
        ProductResponse productById = productService.findProductById(productId);
        modelMap.put("product", productById);
        likedService.checkUser(springUser, modelMap);
        return "productPage";
    }
}
