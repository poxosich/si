package com.example.si.controller;

import com.example.si.entity.Role;
import com.example.si.entity.User;
import com.example.si.security.SpringUser;
import com.example.si.service.BasketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BasketControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private  BasketService basketService;


    @Test
    void saveBasket() throws Exception {
        mockMvc.perform(post("/basket")
                        .param("productId", "5")
                        .param("quantity", "2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/page/5"));
    }


    @Test
    void test_saveBasket_authenticatedUser_redirectsToProductPage() throws Exception {
        User user = new User();
        user.setEmail("arsencholakyan@gami.com");
        user.setPassword("password");
        user.setRole(Role.USER);
        user.setActive(true);
        SpringUser springUser = new SpringUser(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                springUser,
                springUser.getPassword(),
                springUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(post("/basket")
                        .param("productId", "5")
                        .param("quantity", "2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/page/5"));

        SecurityContextHolder.clearContext();
    }


    @Test
    void deleteBasketById() throws Exception {
      mockMvc.perform(post("/basket/delete")
              .param("productId", "4")
              .with(csrf()))
              .andExpect(status().is3xxRedirection())
              .andExpect(redirectedUrl("/"));
    }
}