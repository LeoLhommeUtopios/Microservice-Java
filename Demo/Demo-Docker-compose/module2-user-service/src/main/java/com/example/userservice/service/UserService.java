package com.example.userservice.service;

import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.dto.UpdateUserRequest;
import com.example.userservice.dto.UserDto;
import com.example.userservice.exception.EmailAlreadyExistsException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.User;
import com.example.userservice.model.UserStatus;
import com.example.userservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service contenant la logique métier pour les utilisateurs.
 *
 * Ce service orchestre les opérations entre le contrôleur et le repository,
 * en appliquant les règles métier et les validations.
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // Injection par constructeur (recommandée)
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Récupère tous les utilisateurs.
     */
    public List<UserDto> findAll() {
        log.debug("Récupération de tous les utilisateurs");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Récupère un utilisateur par son ID.
     *
     * @throws UserNotFoundException si l'utilisateur n'existe pas
     */
    public UserDto findById(Long id) {
        log.debug("Recherche utilisateur avec ID: {}", id);
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Récupère un utilisateur par son email.
     *
     * @throws UserNotFoundException si l'utilisateur n'existe pas
     */
    public UserDto findByEmail(String email) {
        log.debug("Recherche utilisateur avec email: {}", email);
        return userRepository.findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("Email non trouvé: " + email));
    }

    /**
     * Recherche des utilisateurs par terme de recherche.
     */
    public List<UserDto> search(String query) {
        log.debug("Recherche utilisateurs avec terme: {}", query);
        return userRepository.search(query)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Crée un nouvel utilisateur.
     *
     * @throws EmailAlreadyExistsException si l'email existe déjà
     */
    @Transactional
    public UserDto create(CreateUserRequest request) {
        log.info("Création d'un nouvel utilisateur: {}", request.email());

        // Vérifier que l'email n'existe pas déjà
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);

        log.info("Utilisateur créé avec ID: {}", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    /**
     * Met à jour un utilisateur existant.
     *
     * @throws UserNotFoundException si l'utilisateur n'existe pas
     * @throws EmailAlreadyExistsException si le nouvel email existe déjà
     */
    @Transactional
    public UserDto update(Long id, UpdateUserRequest request) {
        log.info("Mise à jour utilisateur ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Vérifier l'unicité de l'email si changé
        if (request.email() != null && !request.email().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.email())) {
                throw new EmailAlreadyExistsException(request.email());
            }
        }

        userMapper.updateEntity(user, request);
        User savedUser = userRepository.save(user);

        log.info("Utilisateur mis à jour: {}", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    /**
     * Supprime un utilisateur.
     *
     * @throws UserNotFoundException si l'utilisateur n'existe pas
     */
    public void delete(Long id) {
        log.info("Suppression utilisateur ID: {}", id);

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
        log.info("Utilisateur supprimé: {}", id);
    }

    /**
     * Change le statut d'un utilisateur.
     */
    @Transactional
    public UserDto changeStatus(Long id, UserStatus status) {
        log.info("Changement statut utilisateur ID: {} vers {}", id, status);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setStatus(status);
        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    /**
     * Compte les utilisateurs actifs.
     */
    public long countActiveUsers() {
        return userRepository.countByStatus(UserStatus.ACTIVE);
    }
}
