package com.capgemini.test.code.exception;

public class InvalidDniException extends RuntimeException {
    public InvalidDniException(String dni) {
        super("Invalid DNI: " + dni);
    }
}