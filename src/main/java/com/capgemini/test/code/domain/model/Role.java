package com.capgemini.test.code.domain.model;

/**
 * Roles permitidos por el cliente: "admin" o "superadmin".
 * En el dominio se representan en mayúsculas (convención Java para enums);
 * la conversión desde/hacia el JSON se hace en el controlador (toLowerCase/toUpperCase).
 */
public enum Role {
    ADMIN,
    SUPERADMIN
}