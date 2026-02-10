package com.example.userservice.controller;

import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.dto.UpdateUserRequest;
import com.example.userservice.dto.UserDto;
import com.example.userservice.model.UserStatus;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des utilisateurs.
 *
 * Expose les endpoints de l'API et délègue la logique au service.
 * Gère la transformation HTTP request/response.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /api/users
     * Récupère tous les utilisateurs.
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        log.info("GET /api/users - Récupération de tous les utilisateurs");
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * GET /api/users/{id}
     * Récupère un utilisateur par son ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        log.info("GET /api/users/{} - Récupération d'un utilisateur", id);
        UserDto user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * GET /api/users/email/{email}
     * Récupère un utilisateur par son email.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> findByEmail(@PathVariable String email) {
        log.info("GET /api/users/email/{} - Recherche par email", email);
        UserDto user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    /**
     * GET /api/users/search?q=...
     * Recherche des utilisateurs.
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> search(@RequestParam("q") String query) {
        log.info("GET /api/users/search?q={} - Recherche", query);
        List<UserDto> users = userService.search(query);
        return ResponseEntity.ok(users);
    }

    /**
     * POST /api/users
     * Crée un nouvel utilisateur.
     */
    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody CreateUserRequest request) {
        log.info("POST /api/users - Création d'un utilisateur");
        UserDto createdUser = userService.create(request);

        // Retourne 201 Created avec l'URI du nouvel utilisateur
        URI location = URI.create("/api/users/" + createdUser.id());
        return ResponseEntity.created(location).body(createdUser);
    }

    /**
     * PUT /api/users/{id}
     * Met à jour un utilisateur (remplacement complet).
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("PUT /api/users/{} - Mise à jour complète", id);
        UserDto updatedUser = userService.update(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * PATCH /api/users/{id}
     * Met à jour partiellement un utilisateur.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> partialUpdate(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        log.info("PATCH /api/users/{} - Mise à jour partielle", id);
        UserDto updatedUser = userService.update(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * DELETE /api/users/{id}
     * Supprime un utilisateur.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/users/{} - Suppression", id);
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH /api/users/{id}/status
     * Change le statut d'un utilisateur.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<UserDto> changeStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        log.info("PATCH /api/users/{}/status - Changement de statut", id);
        UserStatus status = UserStatus.valueOf(body.get("status").toUpperCase());
        UserDto updatedUser = userService.changeStatus(id, status);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * GET /api/users/stats
     * Retourne des statistiques sur les utilisateurs.
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        log.info("GET /api/users/stats - Statistiques");
        Map<String, Object> stats = Map.of(
                "totalActive", userService.countActiveUsers()
        );
        return ResponseEntity.ok(stats);
    }
}
