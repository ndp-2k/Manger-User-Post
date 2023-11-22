package com.manager.social_network.common.controller;


import com.manager.social_network.common.constan.Message;
import com.manager.social_network.common.dto.TokenRequest;
import com.manager.social_network.common.jwt.JwtService;
import com.manager.social_network.otp.service.OtpService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
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
