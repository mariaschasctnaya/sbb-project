package com.tsystems.train.validation.constraint;


import com.tsystems.train.validation.validator.PastDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
// Defined the class that is going to validate our field
@Constraint(validatedBy = PastDateValidator.class)
public @interface Past {
    // Error message that is showed in the user interface
    String message() default "{com.tsystems.train.ticketservice.validation.constraint.Past.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
