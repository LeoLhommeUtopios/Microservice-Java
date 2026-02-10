package com.example.userservice.exception;

/**
 * Exception levée quand un utilisateur n'est pas trouvé.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("Utilisateur non trouvé avec l'ID: " + id);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
