package com.manager.social_network.user.validate;

import com.manager.social_network.user.respository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class EmailUniqueValidator implements ConstraintValidator<EmailUniqueValid, String> {

    UserRepository userRepository;

    @Override
    public void initialize(EmailUniqueValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return userRepository.findByEmail(email).isEmpty();
    }
}
