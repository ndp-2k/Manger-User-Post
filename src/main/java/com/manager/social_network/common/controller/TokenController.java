package com.manager.social_network.common.controller;


import com.manager.social_network.common.constan.Message;
import com.manager.social_network.common.dto.TokenRequest;
import com.manager.social_network.common.jwt.JwtService;
import com.manager.social_network.account.service.OtpService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@Tag(name = "Token Controller Manager")
@RequestMapping("/auth")
public class TokenController {
    @Autowired
    OtpService otpService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public TokenController(AuthenticationManager authenticationManager, JwtService jwtService, OtpService otpService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.otpService = otpService;
    }


    @Operation(summary = "Tạo token")
    @PostMapping("/generateToken")
    public ResponseEntity<Map<String, Object>> authenticateAndGetToken(@RequestBody  @ApiParam(value = "Token Request",required = true)  TokenRequest tokenRequest) {
        HttpStatus status = HttpStatus.OK;
        Map<String, Object> response = new HashMap<>();
        if (!otpService.validateOTP(tokenRequest.getOtp(), tokenRequest.getUsername())) {
            status = HttpStatus.BAD_REQUEST;
            response.put(Message.ERROR, "Sai mã otp");
        } else {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(tokenRequest.getUsername(), tokenRequest.getPassword())
                );
                otpService.deleteOtp(tokenRequest.getUsername());
                response.put("token", jwtService.generateToken(tokenRequest.getUsername()));
            } catch (BadCredentialsException e) {
                status = HttpStatus.BAD_REQUEST;
                response.put(Message.ERROR, "Sai tài khỏan hoặc mật khẩu");
            }
        }
        return new ResponseEntity<>(response, status);
    }

}
