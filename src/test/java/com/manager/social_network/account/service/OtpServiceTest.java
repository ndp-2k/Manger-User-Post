package com.manager.social_network.account.service;

import com.manager.social_network.account.entity.OTP;
import com.manager.social_network.account.repository.OtpRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OtpServiceTest {

    @Mock
    private OtpRepository otpRepository;

    @InjectMocks
    private OtpService otpService;

    @Test
    void whenGetOtpCase() {
        String username = "username";
        when(otpRepository.findByUsername(username)).thenReturn(Optional.empty());

        String otp = otpService.getOTP(username);

        assertNotNull(otp);
        verify(otpRepository, times(1)).save(any(OTP.class));
    }

    @Test
    void testGetOTP_ExistingUser() {
        String username = "username";
        OTP existingOtpEntity = new OTP();
        existingOtpEntity.setId(27L);
        existingOtpEntity.setUsername(username);
        existingOtpEntity.setOtp("123456");
        existingOtpEntity.setCreateAt(LocalDateTime.now().minusSeconds(30));

        when(otpRepository.findByUsername(username)).thenReturn(Optional.of(existingOtpEntity));

        String otp = otpService.getOTP(username);

        assertNotNull(otp);
        assertNotEquals("123456", otp);
        verify(otpRepository, times(1)).save(existingOtpEntity);
    }

    @Test
    void testDeleteOtp() {
        String username = "userToDelete";

        otpService.deleteOtp(username);

        verify(otpRepository, times(1)).deleteByUsername(username);
    }

    @Test
    void testValidateOTP_ValidOTP() {
        String otp = "123456";
        String username = "userWithValidOTP";
        OTP validOtpEntity = new OTP();
        validOtpEntity.setOtp(otp);
        validOtpEntity.setUsername(username);
        validOtpEntity.setCreateAt(LocalDateTime.now().minusSeconds(30));

        when(otpRepository.findByOtp(otp)).thenReturn(validOtpEntity);

        assertTrue(otpService.validateOTP(otp, username));
    }

    @Test
    void testValidateOTP_InvalidOTP() {
        String otp = "654321";
        String username = "userWithValidOTP";

        when(otpRepository.findByOtp(otp)).thenReturn(null);

        assertFalse(otpService.validateOTP(otp, username));
    }
}