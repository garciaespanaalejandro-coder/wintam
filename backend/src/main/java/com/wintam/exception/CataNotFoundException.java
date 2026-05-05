package com.wintam.exception;

public class CataNotFoundException extends RuntimeException {
    public CataNotFoundException(Long id) {
        super("Sesión con el id "+id+" no encontrada.");
    }
}
