package com.example.si.controller;

import com.example.si.entity.Role;
import com.example.si.entity.User;
import com.example.si.security.SpringUser;
import com.example.si.service.CategoryService;
import com.example.si.service.LikedService;
import com.example.si.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ShopControllerTest {

    @Autowired
    private MockMvc mockMvc;
     @MockBean
    private  ProductService productService;
     @MockBean
    private  CategoryService categoryService;
     @MockBean
    private  LikedService likedService;



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
    void shopPage() throws Exception {
        mockMvc.perform(get("/shop")
                .with(authentication(setSpringUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("shopPage"))
                .andExpect(model().attributeExists("allCategory"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    void productByCategoryId() throws Exception {
        int id = 4;

        mockMvc.perform(get("/shop/{id}", id)
                        .with(authentication(setSpringUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("shopPage"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("allCategory"));
    }

    @Test
    void addLikedSopPage() throws Exception {
        int id = 4;
        mockMvc.perform(get("/shop/liked/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("shopPage"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("allCategory"));

    }

    @Test
    void addLikedSopPageUserNotNull() throws Exception {
        int id = 4;

        mockMvc.perform(get("/shop/liked/{id}", id)
                        .with(authentication(setSpringUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("shopPage"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("allCategory"));
    }

    @Test
    void deleteLikedById() throws Exception {
        int id = 8;
        mockMvc.perform(get("/shop/delete{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/shop"));
    }

    @Test
    void searchProductByName() throws Exception {
        mockMvc.perform(get("/shop/search")
                        .param("name", "Arsen"))
                .andExpect(status().isOk())
                .andExpect(view().name("shopPage"))
                .andExpect(model().attributeExists("allCategory"))
                .andExpect(model().attributeExists("products"));
    }
}