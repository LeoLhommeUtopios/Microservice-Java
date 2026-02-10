package org.example.demofeignclient.dto;

public record CreateStockRequest(
        long idProduct,
        int quantity
) {
}
