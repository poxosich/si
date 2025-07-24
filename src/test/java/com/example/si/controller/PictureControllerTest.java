package com.example.si.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"si.pic.package.url=src/test/resources/test-images"})
class PictureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setupImage() throws IOException {
        Path imageDir = Paths.get("src/test/resources/test-images");
        Files.createDirectories(imageDir);
        Path imagePath = imageDir.resolve("test-image.jpg");

        byte[] jpegBytes = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xD9};
        Files.write(imagePath, jpegBytes);
    }

    @Test
    void testGetImage() throws Exception {
        mockMvc.perform(get("/grtImage")
                        .param("picture", "test-image.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(result -> {
                    byte[] content = result.getResponse().getContentAsByteArray();
                    assertTrue(content.length > 0, "Image content should not be empty");
                });
    }

    @Test
    void testGetImageFileNotFound() throws Exception {
        mockMvc.perform(get("/grtImage")
                        .param("picture", "non-existent.jpg"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    byte[] content = result.getResponse().getContentAsByteArray();
                    assertEquals(0, content.length, "Content length should be zero for non-existent image");
                });
    }
}
