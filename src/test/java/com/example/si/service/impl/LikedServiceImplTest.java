package com.example.si.service.impl;

import com.example.si.dto.basket.BaskedResponse;
import com.example.si.dto.liked.LikedResponse;
import com.example.si.dto.product.ProductResponse;
import com.example.si.entity.Category;
import com.example.si.entity.Liked;
import com.example.si.entity.Product;
import com.example.si.entity.User;
import com.example.si.mapper.liked.LikedMapper;
import com.example.si.mapper.product.ProductMapper;
import com.example.si.repository.LikedRepository;
import com.example.si.security.SpringUser;
import com.example.si.service.BasketService;
import com.example.si.service.ProductService;
import com.example.si.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class LikedServiceImplTest {

    @InjectMocks
    private LikedServiceImpl likedService;
    @Mock
    private LikedRepository likedRepository;
    @Mock
    private LikedMapper likedMapper;
    @Mock
    private ProductService productService;
    @Mock
    private UserService userService;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private BasketService basketService;


    @Test
    void save() {
        int id = 3;
        String email = "arsenCholakyan@gmail.com";
        User user = new User();
        Product product = new Product();
        LikedResponse likedResponse = new LikedResponse();

        when(likedRepository.findLikedByProductId(id)).thenReturn(Optional.empty());
        ProductResponse productResponse = new ProductResponse();

        when(productService.findProductById(id)).thenReturn(productResponse);
        when(userService.findByEmail(email)).thenReturn(user);
        when(productMapper.toResponseEntity(productResponse)).thenReturn(product);

        Liked liked = Liked.builder()
                .product(product)
                .user(user)
                .build();

        when(likedRepository.save(any(Liked.class))).thenReturn(liked);
        when(likedMapper.toDto(liked)).thenReturn(likedResponse);

        LikedResponse save = likedService.save(id, email);
        assertEquals(save, likedResponse);
    }

    @Test
    void saveIsPresent() {
        int id = 3;
        String email = "arsenCholakyan@gmail.com";
        LikedResponse likedResponse = new LikedResponse();
        Liked liked = new Liked();
        liked.setProduct(new Product());

        when(likedRepository.findLikedByProductId(id)).thenReturn(Optional.of(liked));
        doNothing().when(likedRepository).deleteById(id);
        when(likedMapper.toDto(liked)).thenReturn(likedResponse);
        LikedResponse save = likedService.save(id, email);
        assertEquals(likedResponse, save);
    }

    @Test
    void findLikedByUserEmail() {
        String email = "arsenCholakyan@gmail.com";
        LikedResponse likedResponse = new LikedResponse();
        LikedResponse likedResponse2 = new LikedResponse();

        Category category = Category.builder().id(2).name("gdfb").description("gfhgfhf").build();

        Product product = Product.builder().id(2).name("dsgfdg").price(34.5).quantity(4).category(category).build();
        Product product2 = Product.builder().id(4).name("dsgfdg").price(3.5).quantity(4).category(category).build();

        User user = User.builder().id(4).email(email).active(true).name("fgfd").build();

        Liked liked = Liked.builder().user(user).product(product).build();
        Liked liked2 = Liked.builder().user(user).product(product2).build();
        when(likedMapper.toDto(liked)).thenReturn(likedResponse);
        when(likedMapper.toDto(liked2)).thenReturn(likedResponse2);

        List<LikedResponse> likedResponses = List.of(likedResponse, likedResponse2);

        List<Liked> likedList = List.of(liked, liked2);
        when(likedRepository.findLikedByUserEmail(email)).thenReturn(likedList);

        when(likedMapper.toDtoList(likedList)).thenReturn(likedResponses);

        List<LikedResponse> likedByUserEmail = likedService.findLikedByUserEmail(email);

        assertEquals(likedResponse, likedByUserEmail.get(0));
        assertEquals(likedResponse2, likedByUserEmail.get(1));

    }

    @Test
    void deleteById() {
        int id = 4;
        likedService.deleteById(id);
        verify(likedRepository, times(1)).deleteByProductId(id);
    }


    @Test
    void checkUser() {
        String email = "arsenCholakyan@gmail.com";
        SpringUser springUser = mock(SpringUser.class);
        double total = 54.6;
        when(springUser.getUsername()).thenReturn(email);
        ModelMap modelMap = new ModelMap();
        List<LikedResponse> likedResponses = List.of(new LikedResponse(), new LikedResponse());
        List<BaskedResponse> baskedResponses = List.of(new BaskedResponse(), new BaskedResponse());

        when(likedService.findLikedByUserEmail(springUser.getUsername())).thenReturn(likedResponses);
        when(basketService.getAllByUserEmail(springUser.getUsername())).thenReturn(baskedResponses);
        when(basketService.total(springUser.getUsername())).thenReturn(total);

        likedService.checkUser(springUser, modelMap);

        assertEquals(likedResponses, modelMap.get("liked"));
        assertEquals(2, modelMap.get("likedSize"));
        assertEquals(2, modelMap.get("basketSize"));
        assertEquals(baskedResponses, modelMap.get("basket"));
        assertEquals(total, modelMap.get("total"));
        assertEquals(true, modelMap.get("check"));
    }


    @Test
    void checkUserIsNull() {
        SpringUser springUser = mock(SpringUser.class);
        when(springUser.getUsername()).thenReturn(null);
        ModelMap modelMap = new ModelMap();
        likedService.checkUser(null, modelMap);

        assertTrue(modelMap.isEmpty());

    }


    @Test
    void delete() {
        int id = 7;
        likedService.delete(7);
        verify(likedRepository, times(1)).deleteById(id);
    }


    @Test
    void countByUserEmail() {
        String email = "arsenCholakyan@gmail.com";
        long number = 2;
        when(likedRepository.countByUserEmail(email)).thenReturn(number);

        long l = likedService.countByUserEmail(email);
        assertEquals(number, l);
    }
}