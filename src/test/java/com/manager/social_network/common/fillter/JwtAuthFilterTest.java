package com.manager.social_network.common.fillter;

import com.manager.social_network.common.jwt.JwtService;
import com.manager.social_network.user.entity.User;
import com.manager.social_network.user.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private UserDetails createUserDetails() {
        User user = new User();
        user.setUsername("mockUsername");
        user.setPassword("mockPassword");
        return org.springframework.security.core.userdetails.User
                .withUsername("mockUsername")
                .password("mockPassword")
                .authorities("user")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
    
    @Test
    void testDoFilterInternal() throws ServletException, IOException {
        // Mocking the request header
        when(request.getHeader("Authorization")).thenReturn("Bearer mockToken");

        // Mocking token extraction
        when(jwtService.extractUsername("mockToken")).thenReturn("mockUsername");

        // Mocking user details retrieval
        UserDetails userDetails = createUserDetails();
        when(userDetailsService.loadUserByUsername("mockUsername")).thenReturn(userDetails);

        // Mocking token validation
        when(jwtService.validateToken("mockToken", userDetails)).thenReturn(true);

        // Calling the method to test
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verifying that the authentication token is set in the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        assert authentication.getName().equals("mockUsername");

        // Verifying that the filter chain is called
        verify(filterChain, times(1)).doFilter(request, response);
    }

}