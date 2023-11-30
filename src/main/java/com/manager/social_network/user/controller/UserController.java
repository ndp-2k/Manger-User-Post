package com.manager.social_network.user.controller;

import com.manager.social_network.common.constan.Message;
import com.manager.social_network.common.function.Common;
import com.manager.social_network.friend.service.FriendService;
import com.manager.social_network.user.dto.UserRequest;
import com.manager.social_network.user.entity.User;
import com.manager.social_network.user.service.ImgService;
import com.manager.social_network.user.service.UserService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequiredArgsConstructor
@Tag(name = "User Controller Manager")
@RequestMapping("api/v1/users/")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    ImgService imgService;
    @Autowired
    FriendService friendService;
    @Autowired
    private Common common;
    @Value("${server.url}")
    private String serverUrl;
    @Value("${upload.dir}")
    private String uploadDir;

    @PutMapping("/edit")
    @Operation(summary = "Cập nhật thông tin người dùng")
    @PermitAll
    public String edit(
            @Valid @RequestBody @ApiParam(value = "User Request", required = true) UserRequest userRequest,
            HttpServletRequest request
    ) throws Throwable {
        userService.saveUser(common.getUsernameByToken(request), userRequest);
        return "Success";
    }

    @GetMapping("/info/me")
    @Operation(summary = "Thông tin người dùng")
    @PermitAll
    public User info(HttpServletRequest request) {

        return userService.findById(common.getUserIdByToken(request));
    }

    @Operation(summary = "Xem avatar")
    @GetMapping(value = "/avt/show-avatar")
    public ResponseEntity<Object> getAvatar(
            HttpServletRequest request
    ) {
        if (imgService.isEmpty(common.getUserIdByToken(request))) {
            return new ResponseEntity<>("Chua chap nhat anh", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(serverUrl + "/images/" +
                    imgService.getAvatar(common.getUserIdByToken(request)).getImgName(), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/avt/upload-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(
            @RequestPart("img") @Size(max = 5 * 1024 * 1024, message = "Tối đa 5MB") MultipartFile file,
            HttpServletRequest request
    ) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(file.getInputStream());
        if (!"image/png".equals(mimeType)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Chi nhan PNG");
        }
        imgService.saveAvt(common.getUserIdByToken(request), file);

        return ResponseEntity.ok(Message.SUCCESS);
    }
}
