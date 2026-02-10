package org.example.module3.client;


import org.example.module3.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Fallback pour le UserClient en cas d'indisponibilité du User Service.
 *
 * Cette classe est utilisée quand le circuit breaker est ouvert
 * ou quand le service ne répond pas.
 */
@Component
public class UserClientFallback implements UserClient {

    private static final Logger log = LoggerFactory.getLogger(UserClientFallback.class);

    @Override
    public UserDto findById(Long id) {
        log.warn("Fallback: User Service indisponible pour l'ID {}", id);
        // Retourner un utilisateur "inconnu" par défaut
        return new UserDto(
                id,
                "unknown@example.com",
                "Unknown",
                "User",
                "Unknown User",
                null
        );
    }

    @Override
    public List<UserDto> findAll() {
        log.warn("Fallback: User Service indisponible pour findAll");
        return Collections.emptyList();
    }

    @Override
    public boolean exists(Long id) {
        log.warn("Fallback: User Service indisponible pour exists {}", id);
        // Par sécurité, on considère que l'utilisateur existe
        // pour ne pas bloquer les commandes
        return true;
    }
}
