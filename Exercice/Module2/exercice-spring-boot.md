# Exercice Module 2 : Création d'un Microservice Spring Boot

## Objectifs

- Créer un projet Spring Boot from scratch
- Implémenter une API REST CRUD complète
- Utiliser les bonnes pratiques (DTO, validation, exceptions)
- Écrire des tests unitaires

## Contexte

Vous créez un **User Service** pour le catalogue produits de ShopEasy.

---

## Partie 1 : Création du projet 

### 1.1 Générer le projet

Allez sur [start.spring.io](https://start.spring.io) et configurez :

- **Project** : Maven
- **Language** : Java
- **Spring Boot** : 3.2.x
- **Group** : com.example
- **Artifact** : product-service
- **Java** : 21

**Dépendances à sélectionner** :
- Spring Web
- Spring Data JPA
- Validation
- H2 Database
- Spring Boot Actuator

---

## Partie 2 : Entité User 

### 2.1 Créer l'entité

Créez `User.java` avec les attributs suivants :

| Attribut | Type | Contraintes |
|----------|------|-------------|
| id | Long | Auto-généré |
| email | String | NotBlank, max 100 |
| firstName | String |NotBlank, max 50 |
| lastName | BigDecimal | NotBlank, max 50 |
| phoneNumber | String |  |
| status | Enum | |
| active | Boolean | default true |
| createdAt | LocalDateTime | Auto |
| updatedAt | LocalDateTime | Auto |

### 2.2 Lifecycle callbacks

Implémentez :
- `@PrePersist` pour `createdAt`
- `@PreUpdate` pour `updatedAt`

---

## Partie 3 : DTOs et Mapper 

### 3.1 UserDto (lecture)

```java
public record UserDto(
    Long id,
    String email,
    String firstName,
    String lastName,
    String fullName,
    String phoneNumber,
    UserStatus status,
    LocalDateTime createdAt
) {}
```

### 3.2 CreateUserRequest (création)

Créez un DTO avec les validations appropriées.

### 3.3 UpdateuserRequest (mise à jour)

Créez un DTO pour les mises à jour partielles.

### 3.4 UserMapper

Implémentez les méthodes :
- `toDto(User)`
- `toEntity(CreateUserRequest)`
- `updateEntity(Product, UpdateUserRequest)`

---

## Partie 4 : Repository (5 min)

### 4.1 UserRepository

Créez l'interface avec les méthodes :

```java
public interface UserRepository extends JpaRepository<user, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByStatus(UserStatus status);
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<User> search(@Param("search") String search);
    List<User> findByCreatedAtAfter(LocalDateTime date);
    long countByStatus(UserStatus status);
    @Query(value = "SELECT * FROM users WHERE status = 'ACTIVE' " +
                   "ORDER BY created_at DESC LIMIT :limit",
           nativeQuery = true)
    List<User> findRecentActiveUsers(@Param("limit") int limit);
}
```

---

## Partie 5 : Service (

### 5.1 userService

Implémentez les méthodes suivantes :

```java
@Service
@Transactional(readOnly = true)
public class UserService {

    // Injection du repository et mapper

    public List<UserDto> findAll() { ... }

    public UserDto findById(Long id) { ... }

    public UserDto findByEmail(String email) { ... }

   public List<UserDto> search(String query){ ... }

    @Transactional
    public UserDto create(CreateUserRequest request) { ... }

    @Transactional
    public UserDto update(Long id, UpdateUserRequest request) { ... }

    @Transactional
    public void delete(Long id) { ... }
    @Transactional
    public UserDto changeStatus(Long id, UserStatus status) {}

}
```

### 5.2 Exceptions

Créez les exceptions :
- `userNotFoundException`
- `EmailAlreadyExistsException`
- `UserNotFoundException`

---

## Partie 6 : Contrôleur REST 

### 6.1 Endpoints à implémenter

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | /api/users | Liste tous les users |
| GET | /api/users/{id} | Récupère un users |
| GET | /api/users/email/{email} | Récupère users par email |
| GET | /api/users/search | Récupère un user selon search |
| POST | /api/users | Crée un users |
| PUT | /api/users/{id} | Met à jour un user |
| PATCH | /api/users/{id}/status | Change le statut d'un utilisateur. |
| PATCH | /api/users/{id}| Met à jour partiellement un utilisateur. |
| DELETE | /api/users/{id} | Supprime un produit |

### 6.2 GlobalExceptionHandler

Gérez les exceptions avec ProblemDetail (RFC 7807).

---

## Partie 7 : Configuration 

### 7.1 application.yml

```yaml
server:
  port: 8083

spring:
  application:
    name: users-service
  datasource:
    url: jdbc:h2:mem:products
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  h2:
    console:
      enabled: true

management:
  endpoints:
    web:
      exposure:
        include: health,info
```

---

## Partie 8 : Tests 

### 8.1 Tests unitaires du Service

Testez avec Mockito :
- `findById` avec User existant
- `findById` avec Users inexistant
- `create` avec succès
- `create` avec email dupliqué

### 8.2 Tests du Contrôleur

Testez avec MockMvc :
- GET /api/products → 200 OK
- GET /api/products/1 → 200 OK
- GET /api/products/999 → 404 NOT FOUND
- POST /api/products (valide) → 201 CREATED
- POST /api/products (invalide) → 400 BAD REQUEST

---

