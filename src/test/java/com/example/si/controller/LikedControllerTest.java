package com.example.si.controller;

import com.example.si.entity.Role;
import com.example.si.entity.User;
import com.example.si.repository.UserRepository;
import com.example.si.security.SpringUser;
import com.example.si.service.impl.LikedServiceImpl;
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

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LikedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikedServiceImpl likedService;

    @Mock
    UserRepository userRepository;

    @Test
    void addLikedProduct() throws Exception {
        User user = User.builder().email("arsencholakyan@gami.com").password("bmncb").surname("Cholakyan").active(true).name("Al").role(Role.USER).build();
        userRepository.save(user);
        SpringUser springUser = new SpringUser(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                springUser,
                springUser.getPassword(),
                springUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        mockMvc.perform(get("/v1/liked/46"))

                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/v1/"));
        verify(likedService).save(46, springUser.getUsername());
        SecurityContextHolder.clearContext();

    }

    @Test
    void loginPage_whenAnonymous_returnsLoginView() throws Exception {
        mockMvc.perform(get("/v1/liked/4"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/v1/"));
    }

    @Test
    void deleteLikedById() throws Exception {
        int id = 3;
        mockMvc.perform(get("/v1/liked/delete" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/v1/"));
    }
}