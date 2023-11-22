package com.manager.social_network.user.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameUniqueValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameUniqueValid {
    String message() default "Username đã tồn tại";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
