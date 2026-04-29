package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {


    @Override
    public void initialize(StrongPassword constraintAnnotation){
        ConstraintValidator.super.initialize(constraintAnnotation);

    }

    @Override
    public boolean isValid (String s, ConstraintValidatorContext constraintValidatorContext){

        if(s==null){
            return false;
        }
        return s.matches("(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).*");
    }
}
