package com.example.userservice.service;

import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.dto.UserDto;
import com.example.userservice.exception.EmailAlreadyExistsException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.User;
import com.example.userservice.model.UserStatus;
import com.example.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour UserService.
 *
 * Utilise Mockito pour isoler le service de ses dépendances.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        testUser = new User("john@example.com", "John", "Doe");
        testUser.setId(1L);
        testUser.setStatus(UserStatus.ACTIVE);

        testUserDto = new UserDto(
                1L, "john@example.com", "John", "Doe",
                "John Doe", null, UserStatus.ACTIVE, null
        );
    }

    @Nested
    @DisplayName("Tests findById")
    class FindByIdTests {

        @Test
        @DisplayName("Devrait retourner un utilisateur quand il existe")
        void shouldReturnUserWhenExists() {
            // Given
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(userMapper.toDto(testUser)).thenReturn(testUserDto);

            // When
            UserDto result = userService.findById(1L);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.email()).isEqualTo("john@example.com");
            assertThat(result.fullName()).isEqualTo("John Doe");

            verify(userRepository).findById(1L);
            verify(userMapper).toDto(testUser);
        }

        @Test
        @DisplayName("Devrait lever une exception quand l'utilisateur n'existe pas")
        void shouldThrowExceptionWhenNotFound() {
            // Given
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> userService.findById(99L))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("99");

            verify(userRepository).findById(99L);
            verifyNoInteractions(userMapper);
        }
    }

    @Nested
    @DisplayName("Tests findAll")
    class FindAllTests {

        @Test
        @DisplayName("Devrait retourner tous les utilisateurs")
        void shouldReturnAllUsers() {
            // Given
            User user2 = new User("jane@example.com", "Jane", "Smith");
            user2.setId(2L);

            when(userRepository.findAll()).thenReturn(List.of(testUser, user2));
            when(userMapper.toDto(any(User.class))).thenReturn(testUserDto);

            // When
            List<UserDto> result = userService.findAll();

            // Then
            assertThat(result).hasSize(2);
            verify(userRepository).findAll();
        }

        @Test
        @DisplayName("Devrait retourner une liste vide quand aucun utilisateur")
        void shouldReturnEmptyListWhenNoUsers() {
            // Given
            when(userRepository.findAll()).thenReturn(List.of());

            // When
            List<UserDto> result = userService.findAll();

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Tests create")
    class CreateTests {

        @Test
        @DisplayName("Devrait créer un utilisateur avec succès")
        void shouldCreateUserSuccessfully() {
            // Given
            CreateUserRequest request = new CreateUserRequest(
                    "new@example.com", "New", "User", null
            );

            User newUser = new User("new@example.com", "New", "User");
            newUser.setId(2L);

            UserDto newUserDto = new UserDto(
                    2L, "new@example.com", "New", "User",
                    "New User", null, UserStatus.ACTIVE, null
            );

            when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
            when(userMapper.toEntity(request)).thenReturn(newUser);
            when(userRepository.save(newUser)).thenReturn(newUser);
            when(userMapper.toDto(newUser)).thenReturn(newUserDto);

            // When
            UserDto result = userService.create(request);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.email()).isEqualTo("new@example.com");

            verify(userRepository).existsByEmail("new@example.com");
            verify(userRepository).save(newUser);
        }

        @Test
        @DisplayName("Devrait lever une exception si l'email existe déjà")
        void shouldThrowExceptionWhenEmailExists() {
            // Given
            CreateUserRequest request = new CreateUserRequest(
                    "john@example.com", "John", "Doe", null
            );

            when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> userService.create(request))
                    .isInstanceOf(EmailAlreadyExistsException.class)
                    .hasMessageContaining("john@example.com");

            verify(userRepository).existsByEmail("john@example.com");
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests delete")
    class DeleteTests {

        @Test
        @DisplayName("Devrait supprimer un utilisateur existant")
        void shouldDeleteExistingUser() {
            // Given
            when(userRepository.existsById(1L)).thenReturn(true);

            // When
            userService.delete(1L);

            // Then
            verify(userRepository).existsById(1L);
            verify(userRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Devrait lever une exception si l'utilisateur n'existe pas")
        void shouldThrowExceptionWhenUserNotFound() {
            // Given
            when(userRepository.existsById(99L)).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> userService.delete(99L))
                    .isInstanceOf(UserNotFoundException.class);

            verify(userRepository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("Tests changeStatus")
    class ChangeStatusTests {

        @Test
        @DisplayName("Devrait changer le statut d'un utilisateur")
        void shouldChangeUserStatus() {
            // Given
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(userRepository.save(testUser)).thenReturn(testUser);
            when(userMapper.toDto(testUser)).thenReturn(testUserDto);

            // When
            UserDto result = userService.changeStatus(1L, UserStatus.INACTIVE);

            // Then
            assertThat(testUser.getStatus()).isEqualTo(UserStatus.INACTIVE);
            verify(userRepository).save(testUser);
        }
    }
}
