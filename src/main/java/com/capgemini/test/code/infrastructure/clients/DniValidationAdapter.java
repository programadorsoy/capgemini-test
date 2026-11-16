package com.capgemini.test.code.infrastructure.clients;

import com.capgemini.test.code.domain.service.DniValidationPort;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Adaptador que implementa DniValidationPort usando DniClient (Feign).
 * MockServer responde 200 si el DNI es válido, 409 si es inválido.
 * Feign lanza FeignException.Conflict en 409 → lo capturamos y devolvemos false.
 */
@Component
public class DniValidationAdapter implements DniValidationPort {

    private final DniClient dniClient;

    public DniValidationAdapter(DniClient dniClient) {
        this.dniClient = dniClient;
    }

    @Override
    public boolean isValidDni(String dni) {
        if (dni == null || dni.isBlank()) {
            return false;
        }
        try {
            ResponseEntity<CheckDniResponse> response =
                    dniClient.check(new CheckDniRequest(dni));
            return response.getStatusCode() == HttpStatus.OK;
        } catch (FeignException.Conflict e) {
            // 409 = DNI inválido según MockServer
            return false;
        }
    }
}