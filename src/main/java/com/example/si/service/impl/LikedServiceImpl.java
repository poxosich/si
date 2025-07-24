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

    //этот метод придназначе чтоби сахранить избрнние в базе
    @Override
    @Transactional
    public LikedResponse save(int id, String email) {
        //Здесь я возьму все liked с ProductId, так как не знаю, сколько их.
        Optional<Liked> likedByProductId = likedRepository.findLikedByProductId(id);
        if (likedByProductId.isEmpty()) {
            ProductResponse productById = productService.findProductById(id);
            User byEmail = userService.findByEmail(email);
            return likedMapper.toDto(likedRepository.save(Liked.builder().user(byEmail).product(productMapper.toResponseEntity(productById)).build()));
        }
        deleteById(likedByProductId.get().getProduct().getId());
        return likedMapper.toDto(likedByProductId.get());
    }

    //этот метод возвращает Liked-и с помшю user.email
    @Override
    public List<LikedResponse> findLikedByUserEmail(String email) {
        List<Liked> likedByUserEmail = likedRepository.findLikedByUserEmail(email);
        return likedMapper.toDtoList(likedByUserEmail);
    }

    //удалю liked с помшю ProductId.
    @Override
    @Transactional
    public void deleteById(int id) {
        likedRepository.deleteByProductId(id);
    }

    //этот метод будет отправлять только те данные, которые будут видны толко авторизовнму пользователю.
    @Override
    @Transactional
    public void checkUser(SpringUser springUser, ModelMap modelMap) {
        if (springUser != null) {
            List<LikedResponse> likedByUserEmail = findLikedByUserEmail(springUser.getUsername());
            List<BaskedResponse> allByUserEmail = basketService.getAllByUserEmail(springUser.getUsername());
            double total = basketService.total(springUser.getUsername());
            modelMap.put("liked", likedByUserEmail);
            modelMap.put("likedSize", likedByUserEmail.size());
            modelMap.put("basketSize", allByUserEmail.size());
            modelMap.put("basket", allByUserEmail);
            modelMap.put("total", total);
            modelMap.put("check", true);
        }
    }

    //этот метод предназначен чтоби удалить избрнние из бази с помшю ID
    @Override
    @Transactional
    public void delete(int id) {
        likedRepository.deleteById(id);
    }


    //палучаю кличства избрнних продуктв (спомшю email ползвтела)
    @Override
    public long countByUserEmail(String email) {
        return likedRepository.countByUserEmail(email);
    }


}
