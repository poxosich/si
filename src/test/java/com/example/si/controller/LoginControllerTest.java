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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    Authentication setSpringUserAdmin() {
        User user = new User();
        user.setEmail("arsencholakyan@gmail.com");
        user.setPassword("password");
        user.setRole(Role.ADMIN);
        user.setActive(true);
        SpringUser springUser = new SpringUser(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(springUser, springUser.getPassword(), springUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return auth;
    }


    @Test
    void loginPageUserNotNull() throws Exception {
        Authentication authentication = setSpringUser();

        mockMvc.perform(get("/v1/login/login-page")
                        .with(authentication(authentication)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/v1/"));

        SecurityContextHolder.clearContext();
    }

    @Test
    void loginPageUserIsNull() throws Exception {
        mockMvc.perform(get("/v1/login/login-page"))
                .andExpect(status().isOk()).andExpect(view()
                        .name("login"));
    }

    @Test
    void loginSuccessUser() throws Exception {
        Authentication authentication = setSpringUser();
        mockMvc.perform(get("/v1/login/success")
                        .with(authentication(authentication)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/v1/"));
    }


    @Test
    void loginSuccessAdmin() throws Exception {
        Authentication authentication = setSpringUserAdmin();

        mockMvc.perform(get("/v1/login/success")
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(view().name("adminHom"));
    }
}
