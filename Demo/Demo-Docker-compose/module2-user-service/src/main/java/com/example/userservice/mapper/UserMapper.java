package com.example.userservice.mapper;

import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.dto.UpdateUserRequest;
import com.example.userservice.dto.UserDto;
import com.example.userservice.model.User;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre entités et DTOs.
 *
 * Centralise la logique de conversion et évite la duplication de code.
 */
@Component
public class UserMapper {

    /**
     * Convertit une entité User en UserDto.
     */
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }

    /**
     * Crée une entité User à partir d'une requête de création.
     */
    public User toEntity(CreateUserRequest request) {
        if (request == null) {
            return null;
        }

        User user = new User();
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhoneNumber(request.phoneNumber());
        return user;
    }

    /**
     * Met à jour une entité User avec les données d'une requête.
     * Ne modifie que les champs non-null (mise à jour partielle).
     */
    public void updateEntity(User user, UpdateUserRequest request) {
        if (request == null) {
            return;
        }

        if (request.email() != null) {
            user.setEmail(request.email());
        }
        if (request.firstName() != null) {
            user.setFirstName(request.firstName());
        }
        if (request.lastName() != null) {
            user.setLastName(request.lastName());
        }
        if (request.phoneNumber() != null) {
            user.setPhoneNumber(request.phoneNumber());
        }
    }
}
