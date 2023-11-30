package com.manager.social_network.user.service;

import com.manager.social_network.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserInfoDetailsTest {
    User user;
    UserInfoDetails userInfoDetails;

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
        userInfoDetails = new UserInfoDetails(user);
    }

    @Test
    void testUserInfoDetails() {
        assertEquals(user.getUsername(), userInfoDetails.getUsername());
        assertEquals(user.getPassword(), userInfoDetails.getPassword());
        assertTrue(userInfoDetails.isAccountNonExpired());
        assertTrue(userInfoDetails.isAccountNonLocked());
        assertTrue(userInfoDetails.isCredentialsNonExpired());
        assertTrue(userInfoDetails.isEnabled());
    }


    @Test
    void testGetAuthorities() {
        User mockUser = mock(User.class);
        when(mockUser.getRole()).thenReturn("ROLE_USER,ROLE_ADMIN");

        UserInfoDetails userInfoDetails = new UserInfoDetails(mockUser);

        Collection<? extends GrantedAuthority> authorities = userInfoDetails.getAuthorities();
        assertNotNull(authorities);
        assertEquals(2, authorities.size());
        assertTrue(authorities.containsAll(Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        )));
    }

    @Test
    void testGetPassword() {
        assertEquals(user.getPassword(), userInfoDetails.getPassword());
    }

    @Test
    void testGetUsername() {
        assertEquals("password", userInfoDetails.getPassword());
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(userInfoDetails.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(userInfoDetails.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(userInfoDetails.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(userInfoDetails.isEnabled());
    }
}
