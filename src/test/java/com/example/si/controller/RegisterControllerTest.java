package com.example.si.controller;

import com.example.si.dto.user.UserDtoRequest;
import com.example.si.dto.user.UserDtoResponse;
import com.example.si.entity.User;
import com.example.si.exeption.UserNotFoundException;
import com.example.si.mapper.user.UserMapper;
import com.example.si.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Test
    void registerPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("userDtoSave"))
                .andExpect(model().attributeDoesNotExist("activationMail"));
    }

    @Test
    void registerPageTrue() throws Exception {
        mockMvc.perform(get("/register")
                        .param("success", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("userDtoSave"))
                .andExpect(model().attributeExists("activationMail"));
    }

    @Test
    void registerPageFalse() throws Exception {
        mockMvc.perform(get("/register")
                        .param("success", "false"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("userDtoSave"))
                .andExpect(model().attributeDoesNotExist("activationMail"));
    }

    @Test
    void userRegister() throws Exception {
        mockMvc.perform(post("/register")
                        .param("email", "test@example.com")
                        .param("password", "password123")
                        .param("password_cracking", "differentPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("passwordCheck"))
                .andExpect(model().attributeExists("userDtoSave"));
    }


    @Test
    void registerError() throws Exception {
        Mockito.when(userService.findByEmail("existing@example.com"))
                .thenReturn(new User());

        mockMvc.perform(post("/register")
                        .param("email", "existing@example.com")
                        .param("password", "password123")
                        .param("password_cracking", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("emailMassage"))
                .andExpect(model().attributeExists("userDtoSave"));
    }

    @Test
    void registerSuccess() throws Exception {
        Mockito.when(userService.findByEmail("newuser@example.com"))
                .thenThrow(new UserNotFoundException("not found"));

        mockMvc.perform(post("/register")
                        .param("email", "newuser@example.com")
                        .param("password", "password123")
                        .param("password_cracking", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register?success=true"));

        Mockito.verify(userService).save(Mockito.any(UserDtoRequest.class));
    }

    @Test
    void confirmationEmail() throws Exception {
        Mockito.when(userService.findUserByToken("null-token")).thenReturn(null);

        mockMvc.perform(get("/register/confirmation")
                        .param("token", "null-token"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }


    @Test
    void confirmationActive() throws Exception {
        UserDtoResponse activeUser = new UserDtoResponse();
        activeUser.setActive(true);
        Mockito.when(userService.findUserByToken("active-token")).thenReturn(activeUser);

        mockMvc.perform(get("/register/confirmation")
                        .param("token", "active-token"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }


    @Test
    void confirmationOk() throws Exception {
        UserDtoResponse inactiveUser = new UserDtoResponse();
        inactiveUser.setActive(false);
        inactiveUser.setToken("valid-token");

        Mockito.when(userService.findUserByToken("valid-token")).thenReturn(inactiveUser);

        UserDtoRequest mappedRequest = new UserDtoRequest();
        Mockito.when(userMapper.toRequestDto(Mockito.any(UserDtoResponse.class))).thenReturn(mappedRequest);

        mockMvc.perform(get("/register/confirmation")
                        .param("token", "valid-token"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

        Mockito.verify(userService).onlySave(mappedRequest);
    }
}