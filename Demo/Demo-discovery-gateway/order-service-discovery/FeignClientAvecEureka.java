package com.example.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * FeignClient AVEC Eureka Service Discovery.
 *
 * Difference cle : plus besoin de specifier l'URL !
 * Le nom "user-service" est resolu automatiquement
 * par Eureka + Spring Cloud LoadBalancer.
 *
 * AVANT (sans Eureka) :
 *   @FeignClient(name = "user-service", url = "${user-service.url}")
 *
 * APRES (avec Eureka) :
 *   @FeignClient(name = "user-service")
 */
@FeignClient(
        name = "user-service",
        // Plus de 'url' ! Eureka resout le nom automatiquement
        fallback = UserClientFallback.class
)
public interface UserClient {

    @GetMapping("/api/users/{id}")
    UserDto findById(@PathVariable Long id);

    @GetMapping("/api/users")
    List<UserDto> findAll();
}
