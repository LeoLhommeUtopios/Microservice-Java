package com.example.userservice.exception;

/**
 * Exception levée quand un email existe déjà.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("Un utilisateur existe déjà avec l'email: " + email);
    }
}
