package com.example.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration programmatique des routes du Gateway.
 * Alternative au YAML pour les cas complexes.
 *
 * Note : Dans cette demo, les routes sont definies en YAML (application.yml).
 * Cette classe montre l'approche Java comme reference.
 */
@Configuration
public class GatewayConfig {

    /**
     * Exemple de configuration programmatique (desactive par defaut,
     * car les routes YAML sont utilisees).
     *
     * Decommenter pour utiliser cette approche a la place du YAML.
     */
    // @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route vers User Service
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .filters(f -> f
                                .stripPrefix(0)
                                .addRequestHeader("X-Gateway", "true")
                                .retry(config -> config.setRetries(3)))
                        .uri("lb://user-service"))

                // Route vers Order Service
                .route("order-service", r -> r
                        .path("/api/orders/**")
                        .filters(f -> f
                                .stripPrefix(0)
                                .addRequestHeader("X-Gateway", "true"))
                        .uri("lb://order-service"))

                .build();
    }
}
