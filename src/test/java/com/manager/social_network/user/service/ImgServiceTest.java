package com.manager.social_network.user.service;

import com.manager.social_network.user.entity.Img;
import com.manager.social_network.user.respository.ImgRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImgServiceTest {

    private ImgRepository imgRepository;
    private ImgService imgService;

    @BeforeEach
    void setUp() {
        imgRepository = mock(ImgRepository.class);
        imgService = new ImgService(imgRepository, "test/upload/directory");
    }

    @Test
    void testSaveAvt() throws IOException {
        Long userId = 1L;
        byte[] pngContent = Files.readAllBytes(Paths.get("src/main/resources/static/images/1701080725055_unnamed.png"));
        MockMultipartFile file = new MockMultipartFile("img", "1701080725055_unnamed.png", "image/png", pngContent);

        imgService.saveAvt(userId, file);

        verify(imgRepository, times(1)).save(any(Img.class));
    }

    @Test
    void testIsEmpty_WhenNotEmpty_ReturnsFalse() {
        Long userId = 1L;
        List<Img> imgList = new ArrayList<>();
        Img img = new Img();
        img.setId(1L);
        img.setUserId(1L);
        img.setType("avt");
        img.setCreateAt(Instant.now());
        imgList.add(img);
        when(imgRepository.findAllByUserId(userId)).thenReturn(imgList);

        boolean isEmpty = imgService.isEmpty(userId);

        assertFalse(isEmpty);
    }

    @Test
    void testIsEmpty_WhenEmpty_ReturnsTrue() {
        Long userId = 1L;
        when(imgRepository.findAllByUserId(userId)).thenReturn(new ArrayList<>());

        boolean isEmpty = imgService.isEmpty(userId);

        assertTrue(isEmpty);
    }

    @Test
    void testGetAvatar_WhenExists_ReturnsImg() {
        Long userId = 1L;
        Img img = new Img();
        img.setUserId(userId);
        img.setCreateAt(Instant.now());
        img.setType("avt");
        when(imgRepository.findLatestImgByUserId(userId, "avt")).thenReturn(Optional.of(img));

        Img result = imgService.getAvatar(userId);

        assertNotNull(result);
        assertEquals(result.getId(),img.getId());
        assertEquals(result.getUserId(),img.getUserId());
        assertEquals(result.getCreateAt(),img.getCreateAt());
        assertEquals(result.getType(),img.getType());

    }

    @Test
    void testSaveAvt_IOException_ThrowsRuntimeException() throws IOException {
        Long userId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
        mockStatic(Files.class);
        doThrow(IOException.class).when(Files.class);
        Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class));

        assertThrows(IOException.class, () -> imgService.saveAvt(userId, file));


    }
}
