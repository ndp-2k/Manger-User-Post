package com.manager.social_network.otp.repository;

import com.manager.social_network.otp.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OTP, Long> {

    OTP findByOtp(String otp);

    Optional<OTP> findByUsername(String username);

    void deleteByUsername(String username);
}