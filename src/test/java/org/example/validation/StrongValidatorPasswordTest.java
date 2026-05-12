package org.example.validation;

import org.example.StrongPasswordValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StrongValidatorPasswordTest {

    @Test
    void testStrongPasswordValidationHappy() {
        StrongPasswordValidator strongPasswordValidator = new StrongPasswordValidator();
        boolean result = strongPasswordValidator.isValid("Hejsan1", null);

        assertTrue(result);

    }

    @Test
    void testStrongPasswordValidationSad() {
        StrongPasswordValidator strongPasswordValidator = new StrongPasswordValidator();
        boolean result = strongPasswordValidator.isValid("hejsan1", null);

        assertFalse(result);

    }

    @Test
    void testStrongPasswordValidationSadTwo() {
        StrongPasswordValidator strongPasswordValidator = new StrongPasswordValidator();
        boolean result = strongPasswordValidator.isValid("Hejsan", null);

        assertFalse(result);

    }
    @Test
    void testStrongPasswordValidationSadThree() {
        StrongPasswordValidator strongPasswordValidator = new StrongPasswordValidator();
        boolean result = strongPasswordValidator.isValid(null, null);

        assertFalse(result);

    }

    @Test
    void testStrongPasswordValidationSadFour() {
        StrongPasswordValidator strongPasswordValidator = new StrongPasswordValidator();
        boolean result = strongPasswordValidator.isValid("HEJSAN1", null);

        assertFalse(result);

    }


}