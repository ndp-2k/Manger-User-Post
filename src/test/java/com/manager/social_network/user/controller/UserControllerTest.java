package com.manager.social_network.user.controller;
// -*- coding: utf-8 -*-

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.social_network.common.constan.Message;
import com.manager.social_network.common.function.Common;
import com.manager.social_network.user.dto.UserRequest;
import com.manager.social_network.user.entity.Img;
import com.manager.social_network.user.entity.User;
import com.manager.social_network.user.service.ImgService;
import com.manager.social_network.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserControllerTest {

    MockMvc mockMvc;
    @Mock
    Common common;
    @Mock
    private UserService userService;
    @Mock
    private ImgService imgService;
    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    @Test
    void edit_ShouldReturnSuccess() throws Throwable {
        UserRequest userRequest = new UserRequest();
        userRequest.setFullName("Phòng Tôm");

        doNothing().when(userService).saveUser("username", userRequest);
        when(common.getUsernameByToken(any())).thenReturn("username");

        mockMvc.perform(put("/api/v1/users/edit")
                        .content(asJsonString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));
    }

    @Test
    void info_ShouldReturnUserInfo() throws Exception {
        User user = new User();
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(userService.findById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/info/me"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(user)));

        verify(userService).findById(anyLong());
    }

    @Test
    void getAvatar_ShouldReturnBadRequestWhenNoAvatar() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(imgService.isEmpty(anyLong(),Message.AVT)).thenReturn(true);

        mockMvc.perform(get("/api/v1/users/avt/show-avatar"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Chua chap nhat anh"));

        verify(imgService).isEmpty(anyLong(),Message.AVT);
    }

    @Test
    void getAvatar_ShouldReturnAvatarUrl() throws Exception {
        Img img = new Img();
        img.setImgName("1701078685908_unnamed.png");

        when(common.getUserIdByToken(any())).thenReturn(1L);
        when(imgService.isEmpty(1L,Message.AVT)).thenReturn(false);
        when(imgService.getAvatar(1L)).thenReturn(img);

        mockMvc.perform(get("/api/v1/users/avt/show-avatar"))
                .andExpect(status().isOk());
    }

    @Test
    void uploadAvatar_ShouldReturnBadRequestForNonPNGFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("img", "filename.txt", "text/plain", "some text".getBytes());

        mockMvc.perform(multipart("/api/v1/users/avt/upload-avatar")
                        .file(file))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Chi nhan PNG"));

        verify(imgService, never()).saveImg(anyLong(), any(), Message.AVT);
    }

    @Test
    void uploadAvatar_ShouldReturnSuccessForValidPNGFile() throws Exception {
        byte[] pngContent = Files.readAllBytes(Paths.get("src/main/resources/static/images/1701080725055_unnamed.png"));
        MockMultipartFile file = new MockMultipartFile("img", "1701080725055_unnamed.png", "image/png", pngContent);


        mockMvc.perform(multipart("/api/v1/users/avt/upload-avatar")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));

        verify(imgService).saveImg(anyLong(), any(), Message.AVT);
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
