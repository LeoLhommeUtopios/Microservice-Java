package com.example.userservice.dto;

import com.example.userservice.model.UserStatus;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) pour exposer les données utilisateur via l'API.
 *
 * Sépare la représentation externe de l'entité interne,
 * permettant de contrôler exactement ce qui est exposé.
 */
public record UserDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String fullName,
        String phoneNumber,
        UserStatus status,
        LocalDateTime createdAt
) {
    // Les records Java génèrent automatiquement :
    // - Constructeur
    // - Getters (id(), email(), etc.)
    // - equals(), hashCode(), toString()
}
