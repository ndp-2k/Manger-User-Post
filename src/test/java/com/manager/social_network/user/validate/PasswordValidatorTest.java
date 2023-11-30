package com.manager.social_network.user.validate;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class PasswordValidatorTest {

    private PasswordValidator passwordValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        passwordValidator = new PasswordValidator();
        context = mock(ConstraintValidatorContext.class);
    }



    @Test
    void testInitialize() {
        PasswordValid annotation = Mockito.mock(PasswordValid.class);
        PasswordValidator passwordValidator = new PasswordValidator();

        passwordValidator.initialize(annotation);

        assertTrue(true);
    }

    @Test
    void testIsValid_ValidPassword_ReturnsTrue() {
        String validPassword = "Password123!";
        boolean isValid = passwordValidator.isValid(validPassword, context);

        assertTrue(isValid);
    }

    @Test
    void testIsValid_InvalidPassword_ReturnsFalse() {
        String invalidPassword = "weakpassword";

        boolean isValid = passwordValidator.isValid(invalidPassword, context);

        assertFalse(isValid);
    }

    @Test
    void testIsValid_EmptyPassword_ReturnsFalse() {
        String emptyPassword = "";

        boolean isValid = passwordValidator.isValid(emptyPassword, context);

        assertFalse(isValid);
    }
}
