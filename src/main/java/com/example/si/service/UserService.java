package com.example.si.service;

import com.example.si.dto.user.UserDtoRequest;
import com.example.si.dto.user.UserDtoResponse;
import com.example.si.entity.User;


public interface UserService {
    UserDtoResponse save(UserDtoRequest userDtoSave);

    User findByEmail(String email);

    UserDtoResponse findUserByToken(String token);

    void onlySave(UserDtoRequest userDtoRequest);
}
