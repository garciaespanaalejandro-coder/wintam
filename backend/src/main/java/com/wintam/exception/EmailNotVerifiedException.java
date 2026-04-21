package com.wintam.exception;

public class EmailNotVerifiedException extends RuntimeException {
    public EmailNotVerifiedException(String email) {
        super("El usuario con el mail "+email+" no está verificado.");
    }
}
