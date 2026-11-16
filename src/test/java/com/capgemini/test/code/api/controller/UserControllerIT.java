package com.capgemini.test.code.api.controller;

import com.capgemini.test.code.domain.service.DniValidationPort;
import com.capgemini.test.code.domain.service.NotificationPort;
import com.capgemini.test.code.infrastructure.persistence.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test de integración de la capa web completa (controlador + validaciones +
 * GlobalExceptionHandler + persistencia H2). Mockea los puertos externos
 * (DNI y notificaciones) para aislar MockServer.
 * Contrato:
 *  - POST /users       → 201 { "id": ... } | 400 | 409
 *  - GET  /users/{id}  → 200 | 404
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserJpaRepository userJpaRepository;

    @MockBean private DniValidationPort dniValidationPort;
    @MockBean private NotificationPort notificationPort;

    private static final String BASE = "/users";

    @BeforeEach
    void setUp() {
        userJpaRepository.deleteAll();
        when(dniValidationPort.isValidDni(anyString())).thenReturn(true);
    }

    private String validJson() {
        return """
            {
              "name": "pablo",
              "email": "pablo@test.com",
              "phone": "677998899",
              "dni": "23454234W",
              "rol": "admin"
            }
            """;
    }

    // ---------- POST ----------

    @Test
    @DisplayName("POST: 201 con { id } y notifica")
    void create_201() throws Exception {
        mockMvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(validJson()))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists());

        verify(notificationPort).notifyUserCreated(any());
    }

    @Test
    @DisplayName("POST: 409 si el email ya existe")
    void create_409_emailDuplicado() throws Exception {
        // Primer POST
        mockMvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(validJson()))
                .andExpect(status().isCreated());
        // Segundo POST con el mismo email
        mockMvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(validJson()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.message").value("error validation email"));
    }

    @Test
    @DisplayName("POST: 400 si el DNI es rechazado por MockServer")
    void create_400_dniInvalido() throws Exception {
        when(dniValidationPort.isValidDni(anyString())).thenReturn(false);

        mockMvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(validJson()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    @DisplayName("POST: 400 si el nombre supera 6 caracteres")
    void create_400_nameTooLong() throws Exception {
        String json = validJson().replace("\"pablo\"", "\"pablote\"");

        mockMvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST: 400 si el rol no es admin/superadmin")
    void create_400_roleInvalido() throws Exception {
        String json = validJson().replace("\"admin\"", "\"user\"");

        mockMvc.perform(post(BASE).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    // ---------- GET ----------

    @Test
    @DisplayName("GET: 200 con el usuario")
    void get_200() throws Exception {
        String response = mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON).content(validJson()))
                .andReturn().getResponse().getContentAsString();

        Long id = Long.parseLong(response.replaceAll(".*\"id\":(\\d+).*", "$1"));

        mockMvc.perform(get(BASE + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email").value("pablo@test.com"))
                .andExpect(jsonPath("$.rol").value("admin"))
                .andExpect(jsonPath("$.roomId").value(1));
    }

    @Test
    @DisplayName("GET: 404 si no existe")
    void get_404() throws Exception {
        mockMvc.perform(get(BASE + "/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }
}