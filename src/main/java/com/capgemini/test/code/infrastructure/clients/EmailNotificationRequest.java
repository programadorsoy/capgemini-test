package com.capgemini.test.code.infrastructure.clients;

/**
 * DTO de entrada para POST /email del MockServer.
 * Versión SIN Lombok: getters, setters y constructores escritos a mano.
 */
public class EmailNotificationRequest {

    private String email;
    private String message;

    public EmailNotificationRequest() {
    }

    public EmailNotificationRequest(String email, String message) {
        this.email = email;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EmailNotificationRequest{" +
                "email='" + email + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}