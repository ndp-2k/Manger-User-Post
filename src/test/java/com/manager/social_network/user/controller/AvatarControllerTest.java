package com.manager.social_network.user.controller;


import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AvatarControllerTest {

    private static final String AVATAR_DIRECTORY = "src/main/resources/static/images/";

    @Test
    void getAvatarImage_ExistingImage_ReturnsImage() throws Exception {
        String fileName = "1701080725055_unnamed.png";
        Path imagePath = Paths.get(AVATAR_DIRECTORY, fileName);
        byte[] imageData = Files.readAllBytes(imagePath);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new AvatarController()).build();

        mockMvc.perform(get("/images/{fileName}", fileName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(imageData))
                .andExpect(header().string(HttpHeaders.CACHE_CONTROL, "max-age=0, must-revalidate, private"));
    }

    @Test
    void getAvatarImage_NonExistingImage_ReturnsNotFound() throws Exception {
        String nonExistingFileName = "non_existing.jpg";

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new AvatarController()).build();

        mockMvc.perform(get("/images/{fileName}", nonExistingFileName))
                .andExpect(status().isNotFound());
    }
}
