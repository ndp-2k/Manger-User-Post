package com.manager.social_network.account.controller;

import com.manager.social_network.account.dto.EmailRequest;
import com.manager.social_network.account.dto.PasswordRequest;
import com.manager.social_network.account.dto.SignUpRequest;
import com.manager.social_network.account.service.OtpService;
import com.manager.social_network.user.entity.User;
import com.manager.social_network.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class AccountControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private OtpService otpService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setup() {

        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testNewPassword_UserNotFound() {
        PasswordRequest request = new PasswordRequest();
        request.setEmail("nonexistent@email.com");
        request.setOtp("123456");
        request.setPassword("newPassword");

        when(userService.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        ResponseEntity<Map<String, Object>> responseEntity = accountController.newPassword(request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Email không tồn tại", responseEntity.getBody().get("error"));
        verifyNoInteractions(otpService);

    }

    @Test
    void testSignup() {
        SignUpRequest signUpRequest = new SignUpRequest();
        doNothing().when(userService).createUser(signUpRequest);
        String result = accountController.signup(signUpRequest);

        verify(userService, times(1)).createUser(signUpRequest);

        assertEquals("Success", result);
    }

    @Test
    void testForgetPassword() {
        EmailRequest emailRequest = new EmailRequest();
        User user = new User();

        // Mocking the userService
        when(userService.findByEmail(emailRequest.getEmail())).thenReturn(Optional.of(user));

        when(otpService.getOTP(user.getUsername())).thenReturn("123456");

        ResponseEntity<Map<String, Object>> responseEntity = accountController.forgetPassword(emailRequest);

        verify(userService, times(1)).findByEmail(emailRequest.getEmail());

        verify(otpService, times(1)).getOTP(user.getUsername());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("123456", responseEntity.getBody().get("otp"));
    }

    @Test
    void testNewPassword() {
        PasswordRequest passwordRequest = new PasswordRequest(/* provide necessary details */);
        User user = new User(/* provide necessary details */);

        when(userService.findByEmail(passwordRequest.getEmail())).thenReturn(Optional.of(user));

        when(otpService.validateOTP(passwordRequest.getOtp(), user.getUsername())).thenReturn(true);

        doNothing().when(userService).updatePassword(passwordRequest.getEmail(), passwordRequest.getPassword());

        ResponseEntity<Map<String, Object>> responseEntity = accountController.newPassword(passwordRequest);

        verify(userService, times(1)).findByEmail(passwordRequest.getEmail());

        verify(otpService, times(1)).validateOTP(passwordRequest.getOtp(), user.getUsername());

        verify(userService, times(1)).updatePassword(passwordRequest.getEmail(), passwordRequest.getPassword());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Success", responseEntity.getBody().get("status"));
    }


    @Test
    void testNewPassword_InvalidOTP() {
        PasswordRequest request = new PasswordRequest();
        request.setEmail("existing@email.com");
        request.setOtp("invalidOTP");
        request.setPassword("newPassword");

        User existingUser = new User();
        when(userService.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));
        when(otpService.validateOTP(request.getOtp(), existingUser.getUsername())).thenReturn(false);

        ResponseEntity<Map<String, Object>> responseEntity = accountController.newPassword(request);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Sai OTP", responseEntity.getBody().get("error"));
        verify(otpService, times(1)).validateOTP(request.getOtp(), existingUser.getUsername());
        verify(userService, never()).updatePassword(anyString(), anyString());
    }

    @Test
    void testForgetPassword_EmailNotFound() {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail("nonexistent@email.com");

        when(userService.findByEmail(emailRequest.getEmail())).thenReturn(Optional.empty());

        ResponseEntity<Map<String, Object>> responseEntity = accountController.forgetPassword(emailRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Email không tồn tại", responseEntity.getBody().get("error"));
        verifyNoInteractions(otpService);
    }
}