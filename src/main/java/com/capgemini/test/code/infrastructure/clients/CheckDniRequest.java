package com.capgemini.test.code.infrastructure.clients;

/**
 * DTO de entrada para PATCH /check-dni del MockServer.
 * Versión SIN Lombok: getters, setters y constructores escritos a mano.
 */
public class CheckDniRequest {

    private String dni;

    public CheckDniRequest() {
    }

    public CheckDniRequest(String dni) {
        this.dni = dni;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    @Override
    public String toString() {
        return "CheckDniRequest{" +
                "dni='" + dni + '\'' +
                '}';
    }
}