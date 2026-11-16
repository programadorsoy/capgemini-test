package com.capgemini.test.code.infrastructure.clients;

/**
 * DTO de entrada para POST /sms del MockServer.
 * Versión SIN Lombok: getters, setters y constructores escritos a mano.
 */
public class SmsNotificationRequest {

    private String phone;
    private String message;

    public SmsNotificationRequest() {
    }

    public SmsNotificationRequest(String phone, String message) {
        this.phone = phone;
        this.message = message;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SmsNotificationRequest{" +
                "phone='" + phone + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}