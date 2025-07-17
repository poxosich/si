package com.example.si.controller;

import com.example.si.dto.user.UserDtoRequest;
import com.example.si.dto.user.UserDtoResponse;
import com.example.si.entity.User;
import com.example.si.exeption.UserNotFoundException;
import com.example.si.mapper.user.UserMapper;
import com.example.si.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/register")
public class RegisterController {
    private final UserService userService;
    private final UserMapper userMapper;
    private static final String PASSWORD_MESSAGE = "The passwords do not match.";
    private static final String EMAIL_MASSAGE = "A person with this email address already exists.";
    private static final String ACTIVATION_MAIL = "A verification URL has been sent to your email, please activate it.";


    @GetMapping
    public String registerPage(ModelMap modelMap, @RequestParam(value = "success", required = false) Boolean success) {
        modelMap.put("userDtoSave", new UserDtoRequest());
        if (Boolean.TRUE.equals(success)) {
            modelMap.put("activationMail", ACTIVATION_MAIL);
        }
        return "register";
    }



    @PostMapping
    public String userRegister(@ModelAttribute UserDtoRequest userDtoRequest,
                               @RequestParam("password_cracking") String passwordCracking,
                               ModelMap modelMap) {
        if (!userDtoRequest.getPassword().equals(passwordCracking)) {
            modelMap.addAttribute("passwordCheck", PASSWORD_MESSAGE);
            modelMap.addAttribute("userDtoSave", userDtoRequest);
            return "register";
        }
        try {
            userService.findByEmail(userDtoRequest.getEmail());
            modelMap.addAttribute("emailMassage", EMAIL_MASSAGE);
            modelMap.addAttribute("userDtoSave", userDtoRequest);
            return "register";
        } catch (UserNotFoundException ignored) {
        }
        userService.save(userDtoRequest);
        return "redirect:/v1/register?success=true";
    }

    @GetMapping("/confirmation")
    public String confirmationEmail(@RequestParam String token) {
        UserDtoResponse userByToken = userService.findUserByToken(token);
        if (userByToken == null) {
            return "redirect:/v1/";
        }
        if (userByToken.getActive()) {
            return "redirect:/v1/";
        }
        userByToken.setToken(null);
        userByToken.setActive(true);
        User getEntity = userMapper.toGetEntity(userByToken);
        userService.onlySave(userMapper.toDtoRequest(getEntity));
        return "login";
    }
}
