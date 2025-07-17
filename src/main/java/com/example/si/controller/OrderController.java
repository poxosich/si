package com.example.si.controller;

import com.example.si.security.SpringUser;
import com.example.si.service.OrderService;
import com.example.si.service.impl.MailMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MailMessageService mailMessageService;
    private static final String MASSAGE = "Thank you for your purchase. ;)))";

    @PostMapping
    public String saveOneOrder(@RequestParam int productId, @RequestParam int quantity, @AuthenticationPrincipal SpringUser springUser) {
        orderService.saveOrder(productId, quantity, springUser.getUsername());
        mailMessageService.sent(springUser.getUsername(), "Shopping", MASSAGE);
        return "redirect:/v1/";
    }

    @PostMapping("/all")
    public String saveAll(@RequestParam("productIdList") List<Integer> productIds,
                          @RequestParam("quantityList") List<Integer> quantities,
                          @AuthenticationPrincipal SpringUser springUser) {
        for (int i = 0; i < productIds.size(); i++) {
            orderService.saveOrder(productIds.get(i), quantities.get(i), springUser.getUsername());
        }
        mailMessageService.sent(springUser.getUsername(), "Shopping", MASSAGE);
        return "redirect:/v1/";
    }

}
