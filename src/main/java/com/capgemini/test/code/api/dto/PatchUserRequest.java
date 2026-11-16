package com.capgemini.test.code.api.dto;

import com.capgemini.test.code.validation.ValidDni;
import com.capgemini.test.code.validation.ValidRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de entrada para PATCH /users/{id}.
 * PATCH = actualización parcial: todos los campos son opcionales (sin @NotBlank).
 * Las validaciones de formato se mantienen: si un campo llega, debe ser válido.
 * El servicio ignora los campos null (no enviados) y aplica solo los presentes.
 * Versión SIN Lombok: getters, setters, constructores y builder escritos a mano.
 */
public class PatchUserRequest {

    @Size(max = 6)
    private String name;

    @Email
    private String email;

    @Pattern(regexp = "^[0-9]{9}$")
    private String phone;

    @ValidDni
    private String dni;

    @ValidRole
    private String rol;

    public PatchUserRequest() {
    }

    public PatchUserRequest(String name, String email, String phone, String dni, String rol) {
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

    public static PatchUserRequestBuilder builder() {
        return new PatchUserRequestBuilder();
    }

    public static class PatchUserRequestBuilder {
        private String name;
        private String email;
        private String phone;
        private String dni;
        private String rol;

        PatchUserRequestBuilder() {
        }

        public PatchUserRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PatchUserRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        public PatchUserRequestBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public PatchUserRequestBuilder dni(String dni) {
            this.dni = dni;
            return this;
        }

        public PatchUserRequestBuilder rol(String rol) {
            this.rol = rol;
            return this;
        }

        public PatchUserRequest build() {
            return new PatchUserRequest(name, email, phone, dni, rol);
        }
    }

    @Override
    public String toString() {
        return "PatchUserRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dni='" + dni + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}