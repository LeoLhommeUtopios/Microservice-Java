package org.example.demofeignclient.dto;

public record ProductDto(
        Long id,
        String name,
        Double price
){
}
