package com.manager.social_network.user.validate;

import com.manager.social_network.user.entity.User;
import com.manager.social_network.user.respository.UserRepository;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EmailUniqueValidatorTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private EmailUniqueValidator emailUniqueValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testInitialize() {
        EmailUniqueValid annotation = Mockito.mock(EmailUniqueValid.class);
        EmailUniqueValidator emailUniqueValidator = new EmailUniqueValidator(userRepository);

        emailUniqueValidator.initialize(annotation);

         assertTrue(true);
    }


    @Test
    void testIsValid_EmailNotExists_ReturnsTrue() {
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertTrue(emailUniqueValidator.isValid(email, mock(ConstraintValidatorContext.class)));
    }

    @Test
    void testIsValid_EmailExists_ReturnsFalse() {
        String email = "existing@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        assertFalse(emailUniqueValidator.isValid(email, mock(ConstraintValidatorContext.class)));
    }
}
