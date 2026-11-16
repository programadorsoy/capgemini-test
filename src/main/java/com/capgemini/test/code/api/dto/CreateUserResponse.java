package com.capgemini.test.code.api.dto;

/**
 * DTO de salida para POST /users.
 * El cliente exige que la respuesta sea SOLO { "id": "..." } con 201 Created.
 * Por eso no reutilizamos UserResponse (que devuelve el user completo en GET).
 * Versión SIN Lombok: getters, setters, constructores y builder escritos a mano.
 */
public class CreateUserResponse {

    private Long id;

    public CreateUserResponse() {
    }

    public CreateUserResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static CreateUserResponseBuilder builder() {
        return new CreateUserResponseBuilder();
    }

    public static class CreateUserResponseBuilder {
        private Long id;

        CreateUserResponseBuilder() {
        }

        public CreateUserResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CreateUserResponse build() {
            return new CreateUserResponse(id);
        }
    }

    @Override
    public String toString() {
        return "CreateUserResponse{" +
                "id=" + id +
                '}';
    }
}