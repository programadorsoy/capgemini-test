package com.capgemini.test.code.api.dto;

import com.capgemini.test.code.validation.ValidDni;
import com.capgemini.test.code.validation.ValidRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para POST /users.
 * Validaciones exigidas por el cliente:
 *  - name: no más de 6 caracteres (@Size max=6).
 *  - email: con @ y . (@Email).
 *  - rol: solo "admin" o "superadmin" (@ValidRole).
 *  - dni: formato 8 dígitos + letra (@ValidDni); la validez real la decide MockServer.
 * El campo se llama "rol" (no "role") para alinearse con el JSON del cliente.
 * Versión SIN Lombok: getters, setters, constructores y builder escritos a mano.
 */
public class CreateUserRequest {

    @NotBlank
    @Size(max = 6)
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^[0-9]{9}$")
    private String phone;

    @NotBlank
    @ValidDni
    private String dni;

    @NotBlank
    @ValidRole
    private String rol;

    public CreateUserRequest() {
    }

    public CreateUserRequest(String name, String email, String phone, String dni, String rol) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dni = dni;
        this.rol = rol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public static CreateUserRequestBuilder builder() {
        return new CreateUserRequestBuilder();
    }

    public static class CreateUserRequestBuilder {
        private String name;
        private String email;
        private String phone;
        private String dni;
        private String rol;

        CreateUserRequestBuilder() {
        }

        public CreateUserRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CreateUserRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        public CreateUserRequestBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public CreateUserRequestBuilder dni(String dni) {
            this.dni = dni;
            return this;
        }

        public CreateUserRequestBuilder rol(String rol) {
            this.rol = rol;
            return this;
        }

        public CreateUserRequest build() {
            return new CreateUserRequest(name, email, phone, dni, rol);
        }
    }

    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dni='" + dni + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}