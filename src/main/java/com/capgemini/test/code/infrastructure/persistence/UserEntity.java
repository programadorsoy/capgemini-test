package com.capgemini.test.code.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA mapeada a la tabla "users" de V1_init.sql.
 * Columnas: id, name, email, dni, phone, role, room_id.
 * id es SERIAL en Postgres → Integer en Java.
 * Versión SIN Lombok: getters, setters, constructores y builder escritos a mano.
 */
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;
    private String dni;
    private String phone;
    private String role;

    @Column(name = "room_id")
    private Integer roomId;

    public UserEntity() {
    }

    public UserEntity(Integer id, String name, String email, String dni,
                      String phone, String role, Integer roomId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dni = dni;
        this.phone = phone;
        this.role = role;
        this.roomId = roomId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public static UserEntityBuilder builder() {
        return new UserEntityBuilder();
    }

    public static class UserEntityBuilder {
        private Integer id;
        private String name;
        private String email;
        private String dni;
        private String phone;
        private String role;
        private Integer roomId;

        UserEntityBuilder() {
        }

        public UserEntityBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public UserEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserEntityBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserEntityBuilder dni(String dni) {
            this.dni = dni;
            return this;
        }

        public UserEntityBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserEntityBuilder role(String role) {
            this.role = role;
            return this;
        }

        public UserEntityBuilder roomId(Integer roomId) {
            this.roomId = roomId;
            return this;
        }

        public UserEntity build() {
            return new UserEntity(id, name, email, dni, phone, role, roomId);
        }
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dni='" + dni + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", roomId=" + roomId +
                '}';
    }
}