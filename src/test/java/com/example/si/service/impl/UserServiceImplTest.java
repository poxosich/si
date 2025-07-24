package com.example.si.service.impl;

import com.example.si.dto.user.UserDtoRequest;
import com.example.si.dto.user.UserDtoResponse;
import com.example.si.entity.Role;
import com.example.si.entity.User;
import com.example.si.exeption.UserNotFoundException;
import com.example.si.mapper.user.UserMapper;
import com.example.si.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @Spy
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private MailMessageService mailMessageService;

    @Test
    void save_shouldSendMail() {
        UserDtoRequest request = new UserDtoRequest();
        request.setEmail("arsenchlakayan@gmail.com");
        request.setPassword("password");

        User user = new User();
        user.setEmail(request.getEmail());

        UserDtoResponse response = new UserDtoResponse();
        response.setEmail(request.getEmail());

        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(response);

        userService.save(request);
    }


    @Test
    void findByEmail() {
        User user = new User();
        when(userRepository.findByEmail("arsencholakyan@gmail.com")).thenReturn(Optional.of(user));
        User byEmail = userService.findByEmail("arsencholakyan@gmail.com");
        assertEquals(user.getEmail(), byEmail.getEmail());
    }

    @Test
    void findByEmailISEmpty() {
        when(userRepository.findByEmail("arsencholakyan@gmail.com")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findByEmail("arsencholakyan@gmail.com"));
    }

    @Test
    void findUserByToken() {
        String token = "arsen";
        User user = new User();
        UserDtoResponse userDtoResponse = new UserDtoResponse();
        when(userRepository.findUserByToken(token)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDtoResponse);

        UserDtoResponse userByToken = userService.findUserByToken(token);

        assertEquals(userDtoResponse, userByToken);
    }

    @Test
    void findUserByTokenISEmpty() {
        String token = "arsen";
        when(userRepository.findUserByToken(token)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findUserByToken("arsencholakyan@gmail.com"));
    }

    @Test
    void onlySave() {
        String email = "arsencholakyan@gmail.com";
        UserDtoRequest request = UserDtoRequest.builder().email(email).build();
        User existingUser = new User();
        existingUser.setPassword("encodedPass");
        User toSave = new User();

        doReturn(existingUser).when(userService).findByEmail(request.getEmail());
        when(userMapper.toEntity(request)).thenReturn(toSave);

        userService.onlySave(request);

        assertEquals("encodedPass", request.getPassword());
        verify(userRepository).save(toSave);
    }
}
