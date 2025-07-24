package com.example.si.controller.admin;

import com.example.si.service.CategoryService;
import com.example.si.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerAdminTest {

    @Autowired
    private MockMvc mockMvc;



    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;

    @Test
    void saveProduct() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "picName",
                "image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image".getBytes());

        mockMvc.perform(multipart("/v1/admin/product")
                        .file(file)
                        .param("name", "Test Product")
                        .param("price", "1000")
                        .param("category", "1")
                        .param("description", "Test description")
                        .param("quantity", "5")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }


    @Test
    void deleteProduct() throws Exception {
        int id = 4;

        mockMvc.perform(delete("/v1/admin/product/" + id))
                .andExpect(status().isOk());
    }
}
