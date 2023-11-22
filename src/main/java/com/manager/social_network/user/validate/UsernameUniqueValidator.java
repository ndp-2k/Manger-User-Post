package com.manager.social_network.user.validate;

import com.manager.social_network.user.respository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class UsernameUniqueValidator implements ConstraintValidator<UsernameUniqueValid, String> {

    UserRepository userRepository;

    @Override
    public void initialize(UsernameUniqueValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return !userRepository.findByUsername(username).isPresent();
    }
}
