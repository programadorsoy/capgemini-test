package com.capgemini.test.code.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * DTO de error devuelto por GlobalExceptionHandler.
 * Formato exigido por el cliente: { "code": 409, "message": "error validation email" }.
 * - "code": código HTTP (400/404/409).
 * - "message": mensaje legible.
 * - "errors": lista opcional de errores campo a campo (solo en validaciones de @Valid).
 * @JsonInclude(NON_NULL) evita que "errors" salga como null cuando no aplica.
 * Versión SIN Lombok: getters, setters, constructores y builder escritos a mano.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private int code;
    private String message;
    private List<String> errors;

    public ErrorResponse() {
    }

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ErrorResponse(int code, String message, List<String> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder();
    }

    public static class ErrorResponseBuilder {
        private int code;
        private String message;
        private List<String> errors;

        ErrorResponseBuilder() {
        }

        public ErrorResponseBuilder code(int code) {
            this.code = code;
            return this;
        }

        public ErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseBuilder errors(List<String> errors) {
            this.errors = errors;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(code, message, errors);
        }
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                '}';
    }
}