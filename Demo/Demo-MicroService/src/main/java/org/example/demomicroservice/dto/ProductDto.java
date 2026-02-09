package org.example.demomicroservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductDto (
        Long id,
        String name,
        Double price
){
}
