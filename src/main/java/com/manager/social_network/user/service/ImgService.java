package com.manager.social_network.user.service;

import com.manager.social_network.user.entity.Img;
import com.manager.social_network.user.respository.ImgRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class ImgService {
    @Autowired
    ImgRepository imgRepository;
    @Value("${upload.dir}")
    private String uploadDir;

    public void saveAvt(Long userId, MultipartFile file) throws IOException {
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Img img = new Img();
            img.setUserId(userId);
            img.setImgName(fileName);
            img.setCreateAt(Instant.now());
            img.setType("avt");
            imgRepository.save(img);
        } catch (IOException ex) {
            throw new IOException("Could not store the file. Please try again!", ex);
        }
    }

    public boolean isEmpty(Long userId) {
        return imgRepository.findAllByUserId(userId).isEmpty();
    }

    public Img getAvatar(Long userId) {
        return imgRepository.findLatestImgByUserId(userId, "avt").get();
    }
}
