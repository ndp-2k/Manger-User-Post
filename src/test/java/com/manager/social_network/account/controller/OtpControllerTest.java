package com.manager.social_network.account.controller;

import com.manager.social_network.account.dto.OtpRequest;
import com.manager.social_network.account.service.OtpService;
import com.manager.social_network.common.constan.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OtpControllerTest {

    @Mock
    private OtpService otpService;


    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private OtpController otpController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetOtpOk() {
        OtpRequest otpRequest = new OtpRequest("username", "password");
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(otpService.getOTP(anyString())).thenReturn("123456");

        ResponseEntity<Map<String, Object>> result = otpController.getOtp(otpRequest);


        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("123456", Objects.requireNonNull(result.getBody()).get("otp"));
        verify(authenticationManager, times(1)).authenticate(any());
        verify(otpService, times(1)).getOTP(anyString());
    }

    @Test
    void whenGetOtpBadRequest() {

        OtpRequest otpRequest = new OtpRequest("invalidUsername", "invalidPassword");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        ResponseEntity<Map<String, Object>> result = otpController.getOtp(otpRequest);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Sai tài khỏan hoặc mật khẩu", Objects.requireNonNull(result.getBody()).get(Message.ERROR));
        verify(authenticationManager, times(1)).authenticate(any());
        verify(otpService, never()).getOTP(anyString());
    }
}