package com.manager.social_network.account.controller;

import com.manager.social_network.account.dto.EmailRequest;
import com.manager.social_network.account.dto.PasswordRequest;
import com.manager.social_network.account.dto.SignUpRequest;
import com.manager.social_network.account.service.OtpService;
import com.manager.social_network.common.constan.Message;
import com.manager.social_network.user.entity.User;
import com.manager.social_network.user.respository.UserRepository;
import com.manager.social_network.user.service.UserService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Tag(name = "Account Manager Controller ")
@RequestMapping("api/v1/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    @Operation(summary = "Tạo Account")
    @PostMapping("/signup")
    public String signup(@Valid @RequestBody @ApiParam(value = "User Request", required = true) SignUpRequest request) {
        userService.createUser(request);
        return "Success";
    }

    @Operation(summary = "Lấy otp để tạo mới password")
    @PostMapping("/forget-password")
    public ResponseEntity<Map<String, Object>> forgetPassword(@Valid @RequestBody @ApiParam(value = "Email Request", required = true) EmailRequest email) {
        Optional<User> user = userService.findByEmail(email.getEmail());
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        if (user.isEmpty()) {
            status = HttpStatus.BAD_REQUEST;
            response.put(Message.ERROR, "Email không tồn tại");
            return new ResponseEntity<>(response, status);
        }
        response.put("otp", otpService.getOTP(user.get().getUsername()));
        return new ResponseEntity<>(response, status);
    }


    @Operation(summary = "Tạo mật khẩu mới")
    @PatchMapping("/new-password")
    public ResponseEntity<Map<String, Object>> newPassword(@Valid @RequestBody PasswordRequest request) {
        Optional<User> user = userService.findByEmail(request.getEmail());
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        if (user.isEmpty()) {
            status = HttpStatus.BAD_REQUEST;
            response.put(Message.ERROR, "Email không tồn tại");
            return new ResponseEntity<>(response, status);
        }

        if (otpService.validateOTP(request.getOtp(), user.get().getUsername())) {
            userService.updatePassword(request.getEmail(), request.getPassword());
            response.put("status", "Success");
        } else {
            status = HttpStatus.BAD_REQUEST;
            response.put(Message.ERROR, "Sai OTP");
        }
        return new ResponseEntity<>(response, status);
    }
}