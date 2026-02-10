package com.example.userservice.repository;

import com.example.userservice.model.User;
import com.example.userservice.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'accès aux données des utilisateurs.
 *
 * Spring Data JPA génère automatiquement l'implémentation
 * basée sur les noms des méthodes ou les requêtes JPQL.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Trouve un utilisateur par son email.
     * Le nom de méthode est converti en requête SQL.
     */
    Optional<User> findByEmail(String email);

    /**
     * Vérifie si un email existe déjà.
     */
    boolean existsByEmail(String email);

    /**
     * Trouve tous les utilisateurs actifs.
     */
    List<User> findByStatus(UserStatus status);

    /**
     * Trouve les utilisateurs dont le nom ou prénom contient une chaîne.
     * Requête dérivée avec plusieurs conditions.
     */
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);

    /**
     * Requête JPQL personnalisée pour chercher par nom complet.
     */
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<User> search(@Param("search") String search);

    /**
     * Trouve les utilisateurs créés après une date donnée.
     */
    List<User> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Compte les utilisateurs par statut.
     */
    long countByStatus(UserStatus status);

    /**
     * Requête native SQL pour des cas complexes.
     */
    @Query(value = "SELECT * FROM users WHERE status = 'ACTIVE' " +
                   "ORDER BY created_at DESC LIMIT :limit",
           nativeQuery = true)
    List<User> findRecentActiveUsers(@Param("limit") int limit);
}
