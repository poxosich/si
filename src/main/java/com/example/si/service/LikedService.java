package com.example.si.service;

import com.example.si.dto.liked.LikedResponse;
import com.example.si.security.SpringUser;
import org.springframework.ui.ModelMap;

import java.util.List;

public interface LikedService {
    LikedResponse save(int id, String email);

    List<LikedResponse> findLikedByUserEmail(String email);

    void deleteById(int id);

    void checkUser(SpringUser springUser, ModelMap modelMap);

    void delete(int id);


}
