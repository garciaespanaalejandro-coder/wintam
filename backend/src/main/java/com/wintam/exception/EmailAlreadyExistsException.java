package com.wintam.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Usuario con "+ email+" introducido ya está en uso");
    }
}
