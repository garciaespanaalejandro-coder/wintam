package com.wintam.exception;

public class UserNotJoinedException extends RuntimeException {
    public UserNotJoinedException() {
        super("Usuario no inscrito en la cata.");
    }
}
