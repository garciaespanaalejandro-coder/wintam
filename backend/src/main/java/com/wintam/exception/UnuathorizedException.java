package com.wintam.exception;

public class UnuathorizedException extends RuntimeException {
    public UnuathorizedException() {
        super("Usuario no autorizado para iniciar cata.");
    }
}
