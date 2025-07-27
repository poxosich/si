package com.example.si.controller;

import com.example.si.entity.Role;
import com.example.si.entity.User;
import com.example.si.security.SpringUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHomeWithoutUser() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("newProducts"))
                .andExpect(model().attributeExists("allCategory"))
                .andExpect(model().attributeDoesNotExist("liked"))
                .andExpect(model().attributeDoesNotExist("basket"))
                .andExpect(model().attributeDoesNotExist("check"));
    }

    @Test
    public void testHomeWithout() throws Exception {
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

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("newProducts"))
                .andExpect(model().attributeExists("allCategory"))
                .andExpect(model().attributeExists("liked"))
                .andExpect(model().attributeExists("likedSze"))
                .andExpect(model().attributeExists("basketSize"))
                .andExpect(model().attributeExists("basket"))
                .andExpect(model().attributeExists("total"))
                .andExpect(model().attribute("check", true));
    }
}