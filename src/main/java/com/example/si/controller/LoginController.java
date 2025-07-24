package com.example.si.controller;

import com.example.si.entity.Role;
import com.example.si.entity.User;
import com.example.si.security.SpringUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/login")
public class LoginController {

    //перенаправляет вас на страницу входа
    @GetMapping("/login-page")
    public String loginPage(@AuthenticationPrincipal SpringUser springUser) {
        if (springUser != null) {
            return "redirect:/v1/";
        }
        return "login";
    }

    //данный метод предназначен для цели куда будет направлен пользователь после входа в систему исходя из его роли.
    @GetMapping("/success")
    public String loginSuccess(@AuthenticationPrincipal SpringUser springUser) {
        User username = springUser.getUser();
        if (username.getRole().equals(Role.ADMIN)) {
            return "adminHom";
        }
        return "redirect:/v1/";
    }
}
