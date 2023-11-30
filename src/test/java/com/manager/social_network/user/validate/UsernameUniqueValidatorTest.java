package com.manager.social_network.user.validate;

import com.manager.social_network.user.entity.User;
import com.manager.social_network.user.respository.UserRepository;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsernameUniqueValidatorTest {

    private UserRepository userRepository;
    private UsernameUniqueValidator usernameUniqueValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        usernameUniqueValidator = new UsernameUniqueValidator();
        usernameUniqueValidator = new UsernameUniqueValidator(userRepository);
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void testInitialize() {
        UsernameUniqueValid annotation = mock(UsernameUniqueValid.class);

        usernameUniqueValidator.initialize(annotation);
        assertTrue(true);
    }

    @Test
    void testIsValid_UniqueUsername_ReturnsTrue() {
        String uniqueUsername = "newUsername";
        when(userRepository.findByUsername(uniqueUsername)).thenReturn(Optional.empty());

        boolean isValid = usernameUniqueValidator.isValid(uniqueUsername, context);

        assertTrue(isValid);
    }

    @Test
    void testIsValid_DuplicateUsername_ReturnsFalse() {
        String duplicateUsername = "existingUsername";
        when(userRepository.findByUsername(duplicateUsername)).thenReturn(Optional.of(new User()));

        boolean isValid = usernameUniqueValidator.isValid(duplicateUsername, context);

        assertFalse(isValid);
    }
}
