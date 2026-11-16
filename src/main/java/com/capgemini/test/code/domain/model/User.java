package com.capgemini.test.code.domain.model;

import java.util.Objects;

/**
 * Modelo de dominio del usuario.
 * - Incluye "roomId" porque cada usuario pertenece a una sala (se asigna la sala 1 al crear).
 * - Versión SIN Lombok: getters, setters, constructores y builder escritos a mano.
 */
public class User {

    private Long id;
    private String name;      // máx. 6 caracteres (validado en el DTO)
    private String email;
    private String phone;
    private String dni;
    private Role role;        // ADMIN o SUPERADMIN
    private Long roomId;      // FK a rooms; al crear se fija a 1L

    public User() {
    }

    public User(Long id, String name, String email, String phone, String dni, Role role, Long roomId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dni = dni;
        this.role = role;
        this.roomId = roomId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private String dni;
        private Role role;
        private Long roomId;

        UserBuilder() {
        }

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder dni(String dni) {
            this.dni = dni;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder roomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        public User build() {
            return new User(id, name, email, phone, dni, role, roomId);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dni='" + dni + '\'' +
                ", role=" + role +
                ", roomId=" + roomId +
                '}';
    }
}