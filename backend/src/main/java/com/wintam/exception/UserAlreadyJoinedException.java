package com.wintam.exception;

public class UserAlreadyJoinedException extends RuntimeException {
    public UserAlreadyJoinedException() {
        super("Usuario ya inscrito en esta cata.");
    }
}
