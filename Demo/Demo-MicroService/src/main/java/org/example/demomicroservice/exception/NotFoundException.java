package org.example.demomicroservice.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super("Produit a l'id : "+ message);
    }
}
