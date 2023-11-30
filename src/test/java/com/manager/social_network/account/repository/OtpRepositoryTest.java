package com.manager.social_network.account.repository;

import com.manager.social_network.account.entity.OTP;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OtpRepositoryTest {

    @Autowired
    private OtpRepository otpRepository;

    @Test
    void testFindByOtp() {
        // Arrange
        OTP otp = new OTP();
        otp.setOtp("123456");
        otpRepository.save(otp);

        // Act
        OTP foundOtp = otpRepository.findByOtp("123456");

        // Assert
        assertNotNull(foundOtp);
        assertEquals("123456", foundOtp.getOtp());
    }

    @Test
    void testFindByUsername() {
        // Arrange
        OTP otp = new OTP();
        otp.setUsername("testUser");
        otpRepository.save(otp);

        // Act
        Optional<OTP> foundOtp = otpRepository.findByUsername("testUser");

        // Assert
        assertTrue(foundOtp.isPresent());
        assertEquals("testUser", foundOtp.get().getUsername());
    }

    @Test
    void testDeleteByUsername() {
        // Arrange
        OTP otp = new OTP();
        otp.setId(1L);
        otp.setUsername("testUser");
        otpRepository.save(otp);

        // Act
        otpRepository.deleteByUsername("testUser");

        // Assert
        Optional<OTP> deletedOtp = otpRepository.findByUsername("testUser");
        assertTrue(deletedOtp.isEmpty());
    }
}