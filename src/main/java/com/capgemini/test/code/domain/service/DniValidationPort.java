package com.capgemini.test.code.domain.service;

public interface DniValidationPort {

    /**
     * Valida un DNI contra el servicio externo.
     *
     * @param dni DNI a validar
     * @return true si es válido, false en caso contrario
     */
    boolean isValidDni(String dni);
}