package com.capgemini.test.code.api.dto;

/**
 * DTO de salida para GET /users/{id} (y PUT/PATCH).
 * Refleja el User de dominio: con "rol" en minúsculas y "roomId".
 * El rol se serializa como String ("admin"/"superadmin") para alinear con el cliente.
 * Versión SIN Lombok: getters, setters, constructores y builder escritos a mano.
 */
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String dni;
    private String rol;
    private Long roomId;

    public UserResponse() {
    }

    public UserResponse(Long id, String name, String email, String phone, String dni, String rol, Long roomId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dni = dni;
        this.rol = rol;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public static UserResponseBuilder builder() {
        return new UserResponseBuilder();
    }

    public static class UserResponseBuilder {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private String dni;
        private String rol;
        private Long roomId;

        UserResponseBuilder() {
        }

        public UserResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserResponseBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserResponseBuilder dni(String dni) {
            this.dni = dni;
            return this;
        }

        public UserResponseBuilder rol(String rol) {
            this.rol = rol;
            return this;
        }

        public UserResponseBuilder roomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        public UserResponse build() {
            return new UserResponse(id, name, email, phone, dni, rol, roomId);
        }
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dni='" + dni + '\'' +
                ", rol='" + rol + '\'' +
                ", roomId=" + roomId +
                '}';
    }
}