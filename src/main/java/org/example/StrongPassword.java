package org.example;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StrongPasswordValidator.class)
public @interface StrongPassword {

    String message() default
            "Lösenord måste innehålla åtminstone 1 stor bokstav, en liten och en siffra.";

    Class<?>[] groups()default  {};

    Class<?extends Payload>[] payload()default {};
}
