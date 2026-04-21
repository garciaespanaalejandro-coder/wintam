package com.wintam.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String email){
        super("No existe ninguna cuenta con el correo: "+email);
    }
}
