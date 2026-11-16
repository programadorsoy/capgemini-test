package com.capgemini.test.code.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Modelo de dominio de la sala.
 * Coincide con la tabla "rooms" de V1_init.sql: (id, name).
 * No se expone por API (el cliente solo pide /users), pero existe
 * para que la FK users.room_id tenga su equivalente de dominio.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Room {
    private Long id;
    private String name;
}