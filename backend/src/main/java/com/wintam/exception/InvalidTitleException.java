package com.wintam.exception;

public class InvalidTitleException extends RuntimeException {
    public InvalidTitleException(String message) {
        super("El título "+message+" no existe.");
    }
}
