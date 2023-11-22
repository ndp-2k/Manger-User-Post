package com.manager.social_network.user.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailUniqueValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailUniqueValid {
    String message() default "Email đã tồn tại";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
