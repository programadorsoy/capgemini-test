package com.capgemini.test.code.exception;

import com.capgemini.test.code.api.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Traduce excepciones de negocio/validación a respuestas HTTP con el formato
 * exigido por el cliente: { "code": ..., "message": "..." }.
 * Mapeos:
 *  - UserNotFoundException       → 404
 *  - DuplicateEmailException     → 409 con "error validation email"
 *  - InvalidDniException         → 400 con "error validation dni"
 *  - MethodArgumentNotValid      → 400 con lista de errores campo a campo
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .code(404)
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.builder()
                        .code(409)
                        .message("error validation email")
                        .build());
    }

    @ExceptionHandler(InvalidDniException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDni(InvalidDniException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .code(400)
                        .message("error validation dni")
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .code(400)
                        .message("error validation")
                        .errors(errors)
                        .build());
    }
}