package com.manager.social_network.user.controller;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Controller
public class AvatarController {

    private static final String AVATAR_DIRECTORY = "src/main/resources/static/images/";

    @GetMapping("/images/{fileName}")
    public ResponseEntity<byte[]> getAvatarImage(@PathVariable String fileName) {
        try {
            Path imagePath = Paths.get(AVATAR_DIRECTORY, fileName);
            byte[] imageData = Files.readAllBytes(imagePath);

            HttpHeaders headers = new HttpHeaders();
            headers.setCacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS).cachePrivate().mustRevalidate());
            headers.setExpires(0L);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageData);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}