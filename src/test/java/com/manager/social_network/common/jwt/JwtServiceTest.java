package com.manager.social_network.common.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Test
    void testGenerateToken() {
        assertNotNull(jwtService.generateToken("testUser"));
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testExtractUsername() {
        String username = "test";
        String token = jwtService.generateToken(username);

        assertEquals(username, jwtService.extractUsername(token));
    }

    @Test
    void testExtractExpiration() {
        String username = "test";
        String token = jwtService.generateToken(username);

        Date expiration = jwtService.extractExpiration(token);

        assertNotNull(expiration);
    }

    @Test
    void testIsTokenExpired() {
        assertFalse(jwtService.isTokenExpired( jwtService.generateToken("username")));
    }

    @Test
    void testValidateToken() {
        String username = "phong";
        String token = jwtService.generateToken(username);
        UserDetails userDetails = new User(username, "Phongtom#1", new ArrayList<>());

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }
}
