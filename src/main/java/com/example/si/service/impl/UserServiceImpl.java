package com.example.si.service.impl;

import com.example.si.dto.user.UserDtoRequest;
import com.example.si.dto.user.UserDtoResponse;
import com.example.si.entity.Role;
import com.example.si.entity.User;
import com.example.si.exeption.UserNotFoundException;
import com.example.si.mapper.user.UserMapper;
import com.example.si.repository.UserRepository;
import com.example.si.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${si.massage.active.url}")
    private String url;
    private static final String MAIL_MASSAGE = "Welcome to our online store, please click on this URL %s";


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailMessageService mailMessageService;
    private final UserMapper userMapper;

     //этот метод придназначен для реристраци ползвтеля(если все нармално)
    @Override
    public UserDtoResponse save(UserDtoRequest userDtoRequest) {
        String token = UUID.randomUUID().toString();
        userDtoRequest.setPassword(passwordEncoder.encode(userDtoRequest.getPassword()));
        userDtoRequest.setToken(token);
        userDtoRequest.setRole(Role.USER);
        User saveUser = userRepository.save(userMapper.toEntity(userDtoRequest));
        mailMessageService.sent(saveUser.getEmail(), "confirmation",
                String.format(MAIL_MASSAGE, url + saveUser.getToken()));
        return userMapper.toDto(saveUser);
    }

    //Я ишу пользователя по его email.
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException("not find"));
    }

    //ишем User с помшю токен
    @Override
    public UserDtoResponse findUserByToken(String token) {
        Optional<User> userByToken = userRepository.findUserByToken(token);
        if (userByToken.isPresent()) {
            return userMapper.toDto(userByToken.get());
        }
        throw new UserNotFoundException("not found");
    }

    //для обнавлени user
    @Override
    public void onlySave(UserDtoRequest userDtoRequest) {
        User byEmail = findByEmail(userDtoRequest.getEmail());
        userDtoRequest.setPassword(byEmail.getPassword());
        userRepository.save(userMapper.toEntity(userDtoRequest));
    }
}
