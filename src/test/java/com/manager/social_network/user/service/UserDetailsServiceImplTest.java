package com.manager.social_network.user.service;

import com.manager.social_network.user.entity.User;
import com.manager.social_network.user.respository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    User user;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setFullName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole("user");
        user.setBirthday(Date.valueOf("2000-12-27"));
        user.setJob("Software Engineer");
        user.setLiving("City");
        user.setDeleteFlag(0);
        user.setCreateAt(Instant.now());
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserExists_ReturnsUserDetails() {
        String username = "testUser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound_ThrowsException() {
        String username = "testUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });
    }
}
