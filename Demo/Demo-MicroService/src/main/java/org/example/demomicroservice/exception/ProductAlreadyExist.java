package org.example.demomicroservice.exception;

public class ProductAlreadyExist extends RuntimeException {
    public ProductAlreadyExist(String message) {
        super("Un produit existe deja ave cle nom : "+message);
    }
}
