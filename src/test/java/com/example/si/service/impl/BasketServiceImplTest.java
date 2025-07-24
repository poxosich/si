package com.example.si.service.impl;

import com.example.si.dto.basket.BaskedResponse;
import com.example.si.dto.product.ProductResponse;
import com.example.si.entity.Basket;
import com.example.si.entity.Product;
import com.example.si.entity.User;
import com.example.si.mapper.basket.BasketMapper;
import com.example.si.mapper.product.ProductMapper;
import com.example.si.repository.BasketRepository;
import com.example.si.service.ProductService;
import com.example.si.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BasketServiceImplTest {

    @InjectMocks
    private BasketServiceImpl basketService;

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private ProductService productService;

    @Mock
    UserService userService;

    @Mock
    BasketMapper basketMapper;

    @Mock
    ProductMapper productMapper;

    @Test
    void saveFindBasketProductIsEmpty() {
        int id = 24;
        String email = "cholakyanars4@gmail.com";
        int quantity = 5;
        ProductResponse productResponse = new ProductResponse();
        User user = new User();
        Product productEntity = new Product();
        BaskedResponse basketResponse = new BaskedResponse();

        when(basketRepository.findBasketByProductId(id)).thenReturn(Optional.empty());
        when(productService.findProductById(id)).thenReturn(productResponse);
        when(userService.findByEmail(email)).thenReturn(user);
        when(productMapper.toResponseEntity(productResponse)).thenReturn(productEntity);
        Basket basket = Basket.builder()
                .product(productEntity)
                .user(user)
                .quantity(quantity)
                .build();
        when(basketRepository.save(any(Basket.class))).thenReturn(basket);
        when(basketMapper.toDto(basket)).thenReturn(basketResponse);

        BaskedResponse result = basketService.save(id, email, quantity);

        assertThat(result).isSameAs(basketResponse);
    }


    @Test
    void saveFindBasketProductIsPresent() {
        int productId = 42;
        String email = "u@example.com";
        int quantity = 3;
        BaskedResponse expectedDto = new BaskedResponse();
        Basket existing = Basket.builder().id(100).quantity(5).build();

        when(basketRepository.findBasketByProductId(productId))
                .thenReturn(Optional.of(existing));
        when(basketMapper.toDto(existing))
                .thenReturn(expectedDto);

        BaskedResponse actual = basketService.save(productId, email, quantity);

        assertThat(actual).isEqualTo(expectedDto);

        verify(basketRepository, never()).save(any(Basket.class));
        verify(productService, never()).findProductById(anyInt());
        verify(userService, never()).findByEmail(anyString());
    }


    @Test
    void getAllByUserEmail() {
        String email = "u@example.com";
        Basket basket1 = Basket.builder().id(3).quantity(5).build();
        Basket basket2 = Basket.builder().id(4).quantity(6).build();
        List<Basket> baskets = List.of(basket1, basket2);
        BaskedResponse baskedResponse1 = new BaskedResponse();
        BaskedResponse baskedResponse2 = new BaskedResponse();
        List<BaskedResponse> baskedResponses = List.of(baskedResponse1, baskedResponse2);

        when(basketRepository.findBasketByUserEmail(email)).thenReturn(baskets);

        when(basketMapper.toDtoList(baskets)).thenReturn(baskedResponses);

        List<BaskedResponse> bask = basketService.getAllByUserEmail(email);

        assertThat(bask).isEqualTo(baskedResponses);
    }

    @Test
    void total() {
        String email = "u@example.com";
        Product product1 = new Product();
        Product product2 = new Product();
        product1.setPrice(34);
        product2.setPrice(356);
        Basket basket1 = Basket.builder().product(product1).quantity(4).build();
        Basket basket2 = Basket.builder().product(product2).quantity(4).build();
        List<Basket> baskets = List.of(basket1, basket2);

        when(basketRepository.findBasketByUserEmail(email)).thenReturn(baskets);

        double total = basketService.total(email);

        assertEquals(1560.0, total);
    }

    @Test
    void deleteBasketByProductID() {
        int id = 2;
        basketService.deleteBasketByProductID(id);

    }

    @Test
    void countByUserEmail() {
        long count = 4;
        String email = "u@example.com";
        when(basketRepository.countByUserEmail(email)).thenReturn(count);
        long l = basketService.countByUserEmail(email);

        assertThat(count).isEqualTo(l);

        verify(basketRepository).countByUserEmail(email);
        verify(basketRepository, times(1)).countByUserEmail(email);
    }

}