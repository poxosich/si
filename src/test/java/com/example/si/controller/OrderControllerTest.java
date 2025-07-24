package com.example.si.controller;

import com.example.si.dto.order.OrderResponse;
import com.example.si.entity.Role;
import com.example.si.entity.User;
import com.example.si.security.SpringUser;
import com.example.si.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;
///////////////////////////////////////

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
    void saveOneOrder() throws Exception {
        OrderResponse mockOrderResponse = new OrderResponse();

        when(orderService.saveOrder(anyInt(), anyInt(), anyString()))
                .thenReturn(mockOrderResponse);

        mockMvc.perform(post("/v1/order")
                        .param("productId", "5")
                        .param("quantity", "2")
                        .with(csrf())
                        .with(authentication(setSpringUser())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/v1/"));
    }


    @Test
    void saveAll() throws Exception {
        OrderResponse mockOrderResponse = new OrderResponse();
        when(orderService.saveOrder(anyInt(), anyInt(), anyString()))
                .thenReturn(mockOrderResponse);

        mockMvc.perform(post("/v1/order/all")
                        .param("productIdList", "1", "2", "3")
                        .param("quantityList", "5", "10", "15")
                        .with(authentication(setSpringUser()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/v1/"));
    }
}