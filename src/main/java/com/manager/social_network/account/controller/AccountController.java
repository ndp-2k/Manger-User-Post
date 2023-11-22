package com.manager.social_network.account.controller;

import com.manager.social_network.account.dto.EmailRequest;
import com.manager.social_network.account.dto.PasswordRequest;
import com.manager.social_network.account.dto.SignUpRequest;
import com.manager.social_network.common.constan.Message;
import com.manager.social_network.common.jwt.JwtService;
import com.manager.social_network.otp.service.OtpService;
import com.manager.social_network.user.entity.User;
import com.manager.social_network.user.service.UserService;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@AllArgsConstructor
@RequestMapping("api/v1/account")
public class AccountController {
    private RestTemplate restTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/signup")
    public String signup(@Valid @RequestBody  @ApiParam(value = "User Request",required = true) SignUpRequest request) throws Throwable {
        userService.createUser(request);
        return "Success";
    }

    @PostMapping("/forget-password")
    public ResponseEntity<Map<String, Object>> forgetPassword(@Valid @RequestBody @ApiParam(value = "Email Request",required = true)  EmailRequest email) {
        Optional<User> user = userService.findByEmail(email.getEmail());
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        if (user.isEmpty()) {
            status = HttpStatus.BAD_REQUEST;
            response.put(Message.ERROR, "Email không tồn tại  ");
            return new ResponseEntity<>(response, status);
        }
        response.put("otp", otpService.getOTP(user.get().getUsername()));
        return new ResponseEntity<>(response, status);
    }

    @PostMapping("/new-password")
    public ResponseEntity<Map<String, Object>> newPassword(@Valid @RequestBody PasswordRequest request) throws Throwable {
        Optional<User> user = userService.findByEmail(request.getEmail());
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        if (user.isEmpty()) {
            status = HttpStatus.BAD_REQUEST;
            response.put(Message.ERROR, "Email không tồn tại  ");
            return new ResponseEntity<>(response, status);
        }

        if (otpService.validateOTP(request.getOtp(), user.get().getUsername())) {
            userService.updatePassword(request.getEmail(), request.getPassword());
            response.put("status", "Success");
//            response.put("token", jwtService.generateToken(user.get().getUsername()));
        } else {
            response.put(Message.ERROR, "Sai OTP");
        }
        return new ResponseEntity<>(response, status);
    }
}