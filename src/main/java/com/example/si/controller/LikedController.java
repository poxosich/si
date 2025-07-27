package com.example.si.controller;

import com.example.si.security.SpringUser;
import com.example.si.service.LikedService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/liked")
public class LikedController {
    private final LikedService likedService;

    //дабавление продукта в liked
    @GetMapping("/{id}")
    public String addLikedProduct(@PathVariable int id, @AuthenticationPrincipal SpringUser springUser) {
        if (springUser != null) {
            likedService.save(id, springUser.getUsername());
        }
        return "redirect:/";
    }

    // удалю liked
    @GetMapping("/delete{id}")
    public String deleteLikedById(@PathVariable int id) {
        likedService.deleteById(id);
        return "redirect:/";
    }
}
