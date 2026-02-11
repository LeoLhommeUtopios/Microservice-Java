package com.example.gateway.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlleur de fallback appele quand un service est indisponible.
 * Le Circuit Breaker du Gateway redirige ici en cas de panne.
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/users")
    public ResponseEntity<Map<String, String>> userServiceFallback() {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "status", "SERVICE_UNAVAILABLE",
                        "message", "Le service utilisateur est temporairement indisponible. Veuillez reessayer.",
                        "service", "user-service"
                ));
    }

    @GetMapping("/orders")
    public ResponseEntity<Map<String, String>> orderServiceFallback() {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "status", "SERVICE_UNAVAILABLE",
                        "message", "Le service commande est temporairement indisponible. Veuillez reessayer.",
                        "service", "order-service"
                ));
    }
}
