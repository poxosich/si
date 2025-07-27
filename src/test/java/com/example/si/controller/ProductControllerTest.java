package com.example.si.controller;

import com.example.si.dto.product.ProductResponse;
import com.example.si.entity.Role;
import com.example.si.entity.User;
import com.example.si.security.SpringUser;

import com.example.si.service.LikedService;
import com.example.si.service.ProductService;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @MockBean
    LikedService likedService;
    

Authentication setSpringUser() {
    User user = new User();
    user.setEmail("arsencholakyan@gmail.com");
    user.setPassword("password");
    user.setRole(Role.USER);
    user.setActive(true);
    SpringUser springUser = new SpringUser(user);
    Authentication auth = new UsernamePasswordAuthenticationToken(springUser, springUser.getPassword(), springUser.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);
    return auth;
}


    @Test
    void getProductsByCategoryId() throws Exception {
        int categoryId = 1;
        mockMvc.perform(get("/product/{id}", categoryId)
                        .with(user("arsencholakyan@example.com").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("allCategory"))
                .andExpect(model().attributeExists("newProducts"));
    }

    @Test
    void productPage() throws Exception {
        int productId = 1;
        ProductResponse mockProduct = new ProductResponse();
        mockProduct.setId(productId);
        mockProduct.setName("product");

        when(productService.findProductById(productId)).thenReturn(mockProduct);

        mockMvc.perform(get("/product/page/{id}", productId)
                        .with(user("user@example.com").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void addLikedProductPage() throws Exception {
        int id = 1;
        ProductResponse mockProduct = new ProductResponse();
        mockProduct.setId(id);
        mockProduct.setName("pr");

        when(productService.findProductById(id)).thenReturn(mockProduct);

        mockMvc.perform(get("/product/liked/{id}", id)
                        .with(csrf()))
                .andExpect(model().attributeExists("product"))
                .andExpect(view().name("productPage"));
    }

    @Test
    void addLikedProductPage_withAuthenticatedUser() throws Exception {

        int id = 1;
        ProductResponse product = new ProductResponse();
        product.setId(id);
        product.setName("ProductName");

        when(productService.findProductById(id)).thenReturn(product);

        mockMvc.perform(get("/product/liked/{id}", id)
                        .with(authentication(setSpringUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("productPage"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void deleteLikedById() throws Exception {
        int id = 2;
        mockMvc.perform(get("/product/delete{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void productPageDeleteLikedById() throws Exception {
        int id = 3;
        ProductResponse product = new ProductResponse();
        product.setId(id);
        product.setName("ProductName");

        when(productService.findProductById(id)).thenReturn(product);

        mockMvc.perform(get("/product/product-page/delete")
                        .param("id", String.valueOf(id))
                        .param("productId", String.valueOf(id))
                        .with(authentication(setSpringUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("productPage"))
                .andExpect(model().attribute("product", product));
    }
}