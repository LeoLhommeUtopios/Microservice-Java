package org.example.demomicroservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProductRequest(
        @NotBlank(message = "Le nom est obligatoire")
        @Size(min = 2 ,max = 50, message = "Le nom dois faire entre 2 et 50 caracteres")
        String name,

        @Min(value = 0,message = "Le prix dois etre positif")
        Double price
) {
}
