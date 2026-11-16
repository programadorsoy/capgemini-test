package com.capgemini.test.code.exception;

public class DuplicateDniException extends RuntimeException {
    public DuplicateDniException(String dni) {
        super("DNI already exists: " + dni);
    }
}