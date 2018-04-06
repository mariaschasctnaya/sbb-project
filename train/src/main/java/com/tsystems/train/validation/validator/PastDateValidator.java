package com.tsystems.train.validation.validator;


import com.tsystems.train.exception.FutureTimeException;
import com.tsystems.train.validation.constraint.Past;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
//Defines the logic to validate constraint  @Past
public class PastDateValidator implements ConstraintValidator<Past, LocalDate> {
    //Initializes the validator in preparation for isValid calls.
    @Override
    public void initialize(Past constraintAnnotation) {

    }
    //Implements the validation logic
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        //Method checks if this value date is after current date.
        if(value.isAfter(LocalDate.now()))
            throw new FutureTimeException();
        return true;
    }
}
