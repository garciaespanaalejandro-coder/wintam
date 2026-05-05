package com.wintam.exception;

import com.wintam.model.CataStatus;

public class InvalidCataStatusException extends RuntimeException {
    public InvalidCataStatusException(CataStatus status) {

        super("La cata no puede realizar esta acción en su estado actual: " + status.name());
    }
}
