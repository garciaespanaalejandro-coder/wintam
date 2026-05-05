package com.wintam.exception;

public class CodeDontMatchException extends RuntimeException {
    public CodeDontMatchException() {
        super("El código introducido no coincide con el de la cata.");
    }
}
