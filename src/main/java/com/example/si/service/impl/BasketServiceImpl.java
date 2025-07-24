package com.example.si.service.impl;

import com.example.si.dto.basket.BaskedResponse;
import com.example.si.dto.product.ProductResponse;
import com.example.si.entity.Basket;
import com.example.si.entity.User;
import com.example.si.mapper.basket.BasketMapper;
import com.example.si.mapper.product.ProductMapper;
import com.example.si.repository.BasketRepository;
import com.example.si.service.BasketService;
import com.example.si.service.ProductService;
import com.example.si.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {
    private final BasketRepository basketRepository;
    private final BasketMapper basketMapper;
    private final UserService userService;
    private final ProductMapper productMapper;
    private final ProductService productService;

    // когда кладешь продукт в корзину
    @Override
    public BaskedResponse save(int id, String email, int quantity) {
        Optional<Basket> basketByProductId = basketRepository.findBasketByProductId(id);
        if (basketByProductId.isEmpty()) {
            ProductResponse productById = productService.findProductById(id);
            User byEmail = userService.findByEmail(email);
            return basketMapper.toDto(basketRepository.save(Basket.builder()
                    .product(productMapper.toResponseEntity(productById))
                    .user(byEmail)
                    .quantity(quantity)
                    .build()));
        }
        return basketMapper.toDto(basketByProductId.get());
    }

    // этот метод вазврашает все прадукти из Basked с помшю UserEmail
    @Override
    public List<BaskedResponse> getAllByUserEmail(String email) {
        List<Basket> basketByUserEmail = basketRepository.findBasketByUserEmail(email);
        return basketMapper.toDtoList(basketByUserEmail);
    }


    //этот метод вазврашаеть обши цену продуктв каторие находица в карзине
    @Override
    public double total(String email) {
        double totalPrice = 0;
        List<Basket> basketByUserEmail = basketRepository.findBasketByUserEmail(email);
        for (Basket basket : basketByUserEmail) {
            totalPrice += basket.getProduct().getPrice() * basket.getQuantity();
        }
        return totalPrice;
    }

    // удаление Basket с помшю ProductID
    @Override
    @Transactional
    public void deleteBasketByProductID(int id) {
        basketRepository.deleteByProductId(id);
    }


    //биру каличства продуктв из карзине с помшю emil ползвтеля
    @Override
    public Long countByUserEmail(String userEmail) {
        return basketRepository.countByUserEmail(userEmail);
    }
}
