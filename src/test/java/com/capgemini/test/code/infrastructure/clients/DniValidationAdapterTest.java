package com.capgemini.test.code.infrastructure.clients;

import com.capgemini.test.code.domain.service.DniValidationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DniValidationAdapterTest {

    @Mock
    private DniClient dniClient;

    private DniValidationPort adapter;

    @BeforeEach
    void setUp() {
        adapter = new DniValidationAdapter(dniClient);
    }

    @Test
    @DisplayName("isValidDni: true cuando MockServer responde 200")
    void isValidDni_shouldReturnTrue_when200() {
        CheckDniResponse body = new CheckDniResponse("Valid DNI");
        when(dniClient.check(any(CheckDniRequest.class)))
                .thenReturn(ResponseEntity.ok(body));

        boolean result = adapter.isValidDni("12345678Z");

        assertThat(result).isTrue();
        verify(dniClient).check(argThat(req -> "12345678Z".equals(req.getDni())));
    }

    @Test
    @DisplayName("isValidDni: false cuando MockServer responde 409")
    void isValidDni_shouldReturnFalse_when409() {
        CheckDniResponse body = new CheckDniResponse("Invalid DNI");
        when(dniClient.check(any(CheckDniRequest.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CONFLICT).body(body));

        boolean result = adapter.isValidDni("99999999w");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("isValidDni: false cuando MockServer responde 5xx")
    void isValidDni_shouldReturnFalse_when5xx() {
        when(dniClient.check(any(CheckDniRequest.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new CheckDniResponse("boom")));

        assertThat(adapter.isValidDni("12345678Z")).isFalse();
    }

    @Test
    @DisplayName("isValidDni: false cuando el DNI es null o blank (no llama al cliente)")
    void isValidDni_shouldReturnFalse_whenBlank() {
        assertThat(adapter.isValidDni(null)).isFalse();
        assertThat(adapter.isValidDni("")).isFalse();
        assertThat(adapter.isValidDni("   ")).isFalse();

        verifyNoInteractions(dniClient);
    }
}