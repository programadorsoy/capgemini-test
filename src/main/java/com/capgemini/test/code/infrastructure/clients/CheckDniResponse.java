package com.capgemini.test.code.infrastructure.clients;

/**
 * DTO de respuesta de PATCH /check-dni del MockServer.
 * El MockServer devuelve { "message": "Valid DNI" } o { "message": "Invalid DNI" }.
 * La validez real se decide por el status HTTP (200 vs 409), no por este body.
 * Versión SIN Lombok: getters, setters y constructores escritos a mano.
 */
public class CheckDniResponse {

    private String message;

    public CheckDniResponse() {
    }

    public CheckDniResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CheckDniResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}