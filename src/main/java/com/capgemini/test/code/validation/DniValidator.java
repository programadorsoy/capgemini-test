package com.capgemini.test.code.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador de @ValidDni: comprueba SOLO el formato (8 dígitos + letra).
 * La validez real del DNI la decide MockServer (PATCH /check-dni).
 * Así el flujo es: formato OK → llamada a MockServer → 409 → 400.
 * null se deja pasar: @NotBlank ya lo cubre.
 */
public class DniValidator implements ConstraintValidator<ValidDni, String> {

    private static final String DNI_REGEX = "^[0-9]{8}[A-Za-z]$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches(DNI_REGEX);
    }
}