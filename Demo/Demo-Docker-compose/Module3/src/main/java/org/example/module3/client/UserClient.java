package org.example.module3.client;

import org.example.module3.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Client Feign pour communiquer avec le User Service.
 *
 * Spring Cloud OpenFeign génère automatiquement l'implémentation
 * basée sur les annotations. Cela permet une communication
 * déclarative entre microservices.
 */
@FeignClient(
        name = "user-service",
        url = "${user-service.url:http://localhost:8081}",
        fallback = UserClientFallback.class
)
public interface UserClient {

    /**
     * Récupère un utilisateur par son ID.
     *
     * @param id L'ID de l'utilisateur
     * @return Les informations de l'utilisateur
     */
    @GetMapping("/api/users/{id}")
    UserDto findById(@PathVariable("id") Long id);

    /**
     * Récupère tous les utilisateurs.
     *
     * @return La liste des utilisateurs
     */
    @GetMapping("/api/users")
    List<UserDto> findAll();

    /**
     * Vérifie si un utilisateur existe.
     *
     * @param id L'ID de l'utilisateur
     * @return true si l'utilisateur existe
     */
    @GetMapping("/api/users/{id}/exists")
    boolean exists(@PathVariable("id") Long id);
}
