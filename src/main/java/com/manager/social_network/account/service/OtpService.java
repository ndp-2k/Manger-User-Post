package com.manager.social_network.account.service;

import com.manager.social_network.account.entity.OTP;
import com.manager.social_network.account.repository.OtpRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OtpService {
    OtpRepository otpRepository;

    public String getOTP(String username) {
        Optional<OTP> otp = otpRepository.findByUsername(username);
        OTP otpEntity;
        if (otp.isPresent()) {
            otpEntity = otp.get();
        } else {
            otpEntity = new OTP();
            otpEntity.setUsername(username);
        }
        otpEntity.setOtp(generateOTP());
        otpEntity.setCreateAt(LocalDateTime.now());
        otpRepository.save(otpEntity);
        return otpEntity.getOtp();
    }

    @Transactional
    public void deleteOtp(String username) {
        otpRepository.deleteByUsername(username);
    }

    private String generateOTP() {
        int length = 6;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }
        return sb.toString();
    }

    public boolean validateOTP(String otp, String username) {
        OTP otpEntity = otpRepository.findByOtp(otp);
        if (otpEntity != null && Objects.equals(otpEntity.getUsername(), username)) {
            LocalDateTime now = LocalDateTime.now();
            return now.isBefore(otpEntity.getCreateAt().plusSeconds(60));
        }
        return false;
    }

}
