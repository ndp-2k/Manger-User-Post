package com.manager.social_network.otp.controller;

import com.manager.social_network.common.constan.Message;
import com.manager.social_network.otp.dto.OtpRequest;
import com.manager.social_network.otp.service.OtpService;
import com.manager.social_network.user.respository.UserRepository;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@Tag(name = "OTP Controller Manager")
@RequestMapping("api/v1/otp/")
public class OtpController {
    OtpService otpService;
    UserRepository userRepository;
    PasswordEncoder encoder;
    AuthenticationManager authenticationManager;

    @Operation(summary = "Tạo OTP để đăng nhập")
    @PostMapping("send-otp")
    public ResponseEntity<Map<String, Object>> getOtp(@RequestBody @ApiParam(value = "otpRequest",required = true) OtpRequest otpRequest) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(otpRequest.getUsername(), otpRequest.getPassword())
            );
            response.put("otp", otpService.getOTP(otpRequest.getUsername()));
        } catch (BadCredentialsException e) {
            status = HttpStatus.BAD_REQUEST;

            response.put(Message.ERROR, "Sai tài khỏan hoặc mật khẩu");

        }
        return new ResponseEntity<>(response, status);
    }
}
