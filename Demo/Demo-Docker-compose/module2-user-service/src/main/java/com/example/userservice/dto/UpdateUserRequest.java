package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la mise à jour d'un utilisateur.
 *
 * Tous les champs sont optionnels pour permettre
 * des mises à jour partielles (PATCH).
 */
public record UpdateUserRequest(
        @Email(message = "Format email invalide")
        String email,

        @Size(min = 2, max = 50, message = "Le prénom doit faire entre 2 et 50 caractères")
        String firstName,

        @Size(min = 2, max = 50, message = "Le nom doit faire entre 2 et 50 caractères")
        String lastName,

        String phoneNumber
) {
}
