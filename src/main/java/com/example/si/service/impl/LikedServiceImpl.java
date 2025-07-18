package com.example.si.service.impl;

import com.example.si.dto.basket.BaskedResponse;
import com.example.si.dto.liked.LikedResponse;

import com.example.si.dto.product.ProductResponse;
import com.example.si.entity.Liked;
import com.example.si.entity.User;
import com.example.si.mapper.liked.LikedMapper;
import com.example.si.mapper.product.ProductMapper;
import com.example.si.repository.LikedRepository;
import com.example.si.security.SpringUser;
import com.example.si.service.BasketService;
import com.example.si.service.LikedService;
import com.example.si.service.ProductService;
import com.example.si.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikedServiceImpl implements LikedService {

    private final LikedRepository likedRepository;
    private final LikedMapper likedMapper;
    private final ProductService productService;
    private final UserService userService;
    private final ProductMapper productMapper;
    private final BasketService basketService;

    @Override
    @Transactional
    public LikedResponse save(int id, String email) {
        Optional<Liked> likedByProductId = likedRepository.findLikedByProductId(id);
        if (likedByProductId.isEmpty()) {
            ProductResponse productById = productService.findProductById(id);
            User byEmail = userService.findByEmail(email);
            Liked save = likedRepository.save(Liked.builder()
                    .user(byEmail)
                    .product(productMapper.toGetEntity(productById))
                    .build());
            return likedMapper.toDto(save);
        }
        deleteById(likedByProductId.get().getProduct().getId());
        return likedMapper.toDto(likedByProductId.get());
    }

    @Override
    public List<LikedResponse> findLikedByUserEmail(String email) {
        List<Liked> likedByUserEmail = likedRepository.findLikedByUserEmail(email);
        List<LikedResponse> likedByUserEmailResp = new ArrayList<>();
        for (Liked liked : likedByUserEmail) {
            likedByUserEmailResp.add(likedMapper.toDto(liked));
        }
        return likedByUserEmailResp;
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        likedRepository.deleteByProductId(id);
    }

    @Override
    @Transactional
    public void checkUser(SpringUser springUser, ModelMap modelMap) {
        if (springUser != null) {
            List<LikedResponse> likedByUserEmail = findLikedByUserEmail(springUser.getUsername());
            List<BaskedResponse> allByUserEmail = basketService.getAllByUserEmail(springUser.getUsername());
            int total = basketService.total(springUser.getUsername());
            modelMap.put("liked", likedByUserEmail);
            modelMap.put("likedSize", likedByUserEmail.size());
            modelMap.put("basketSize", allByUserEmail.size());
            modelMap.put("basket", allByUserEmail);
            modelMap.put("total", total);
            modelMap.put("check", true);
        }
    }

    @Override
    @Transactional
    public void delete(int id) {
        likedRepository.deleteById(id);
    }
}
