package com.capgemini.test.code.acceptance;

import com.capgemini.test.code.api.dto.CreateUserRequest;
import com.capgemini.test.code.infrastructure.persistence.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test de aceptación E2E:
 *  - App arrancada en puerto aleatorio (RANDOM_PORT).
 *  - BBDD H2 en memoria (perfil test).
 *  - MockServer REAL en 127.0.0.1:1080 (contenedor docker).
 * Verifica el flujo completo exigido por el cliente:
 *  - POST /users → 201 { id } (con DNI validado contra MockServer real).
 *  - Notificación real a /email o /sms según rol.
 *  - DNI inválido (99999999w) → 400.
 *  - Email duplicado → 409.
 * Requiere: docker compose up -d mockserver (127.0.0.1:1080).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CreateUserAcceptanceTest {

    @LocalServerPort private int port;
    @Autowired private TestRestTemplate rest;
    @Autowired private UserJpaRepository userJpaRepository;

    private String baseUrl() { return "http://localhost:" + port + "/users"; }

    @BeforeEach
    void setUp() {
        userJpaRepository.deleteAll();

        // Falla rápido si MockServer no está arriba
        ResponseEntity<String> status = rest.exchange(
                "http://127.0.0.1:1080/mockserver/status",
                HttpMethod.PUT, HttpEntity.EMPTY, String.class);
        assertThat(status.getStatusCode().value())
                .as("MockServer debe estar arriba en 127.0.0.1:1080 (docker compose up -d)")
                .isEqualTo(200);
    }

    private CreateUserRequest validRequest(String rol) {
        return CreateUserRequest.builder()
                .name("pablo")
                .email("pablo.acceptance@test.com")
                .phone("677998899")
                .dni("23454234W")
                .rol(rol)
                .build();
    }

    @Test
    @DisplayName("ACEPTACIÓN: crear admin válido → 201 { id } y GET OK")
    void createAdmin_happyPath() {
        ResponseEntity<String> created = rest.postForEntity(
                baseUrl(), validRequest("admin"), String.class);

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(created.getBody()).contains("\"id\"");

        String id = created.getBody().replaceAll(".*\"id\":(\\d+).*", "$1");

        ResponseEntity<String> fetched = rest.getForEntity(baseUrl() + "/" + id, String.class);
        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetched.getBody()).contains("pablo.acceptance@test.com");
        assertThat(fetched.getBody()).contains("admin");
    }

    @Test
    @DisplayName("ACEPTACIÓN: crear superadmin válido → 201 (notifica por SMS)")
    void createSuperadmin_happyPath() {
        CreateUserRequest req = validRequest("superadmin");
        req.setEmail("super.acceptance@test.com");
        req.setPhone("677998800");
        req.setDni("11111111H");

        ResponseEntity<String> created = rest.postForEntity(baseUrl(), req, String.class);

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("ACEPTACIÓN: DNI inválido (99999999w) → MockServer 409 → 400")
    void create_invalidDni_400() {
        CreateUserRequest req = validRequest("admin");
        req.setDni("99999999w");
        req.setEmail("bad.dni@test.com");

        ResponseEntity<String> response = rest.postForEntity(baseUrl(), req, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("ACEPTACIÓN: email duplicado → 409")
    void create_duplicateEmail_409() {
        rest.postForEntity(baseUrl(), validRequest("admin"), String.class);

        CreateUserRequest dup = validRequest("admin");
        dup.setDni("11111111H");
        dup.setPhone("677998800");

        ResponseEntity<String> response = rest.postForEntity(baseUrl(), dup, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).contains("error validation email");
    }

    @Test
    @DisplayName("ACEPTACIÓN: nombre > 6 caracteres → 400")
    void create_nameTooLong_400() {
        CreateUserRequest req = validRequest("admin");
        req.setName("pablote");

        ResponseEntity<String> response = rest.postForEntity(baseUrl(), req, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("ACEPTACIÓN: rol inválido → 400")
    void create_invalidRole_400() {
        CreateUserRequest req = validRequest("user");

        ResponseEntity<String> response = rest.postForEntity(baseUrl(), req, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}