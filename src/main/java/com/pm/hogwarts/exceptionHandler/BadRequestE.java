package com.pm.hogwarts.exceptionHandler;

public class BadRequestE extends RuntimeException {
    public BadRequestE(String message) {
        super(message);
    }
}
