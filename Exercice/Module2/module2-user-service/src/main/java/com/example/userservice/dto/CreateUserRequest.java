package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la création d'un utilisateur.
 *
 * Contient les validations nécessaires pour s'assurer
 * que les données reçues sont correctes.
 */
public record CreateUserRequest(
        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format email invalide")
        String email,

        @NotBlank(message = "Le prénom est obligatoire")
        @Size(min = 2, max = 50, message = "Le prénom doit faire entre 2 et 50 caractères")
        String firstName,

        @NotBlank(message = "Le nom est obligatoire")
        @Size(min = 2, max = 50, message = "Le nom doit faire entre 2 et 50 caractères")
        String lastName,

        String phoneNumber
) {
}
