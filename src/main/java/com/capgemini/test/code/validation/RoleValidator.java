package com.capgemini.test.code.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador de @ValidRole.
 * El cliente solo admite "admin" o "superadmin". Usamos equalsIgnoreCase
 * para aceptar también "ADMIN"/"Admin" y no romper si el JSON varía en mayúsculas.
 * null se deja pasar: @NotBlank ya lo cubre en los DTOs donde es obligatorio.
 */
public class RoleValidator implements ConstraintValidator<ValidRole, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return "admin".equalsIgnoreCase(value) || "superadmin".equalsIgnoreCase(value);
    }
}