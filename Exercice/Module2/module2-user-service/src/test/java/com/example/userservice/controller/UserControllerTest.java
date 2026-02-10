package com.example.userservice.controller;

import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.dto.UserDto;
import com.example.userservice.exception.EmailAlreadyExistsException;
import com.example.userservice.exception.GlobalExceptionHandler;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.model.UserStatus;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration pour le contrôleur REST.
 *
 * Utilise MockMvc pour tester les endpoints HTTP
 * sans démarrer le serveur complet.
 */
@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("GET /api/users - Devrait retourner la liste des utilisateurs")
    void shouldReturnAllUsers() throws Exception {
        // Given
        UserDto user1 = new UserDto(1L, "john@example.com", "John", "Doe",
                "John Doe", null, UserStatus.ACTIVE, null);
        UserDto user2 = new UserDto(2L, "jane@example.com", "Jane", "Smith",
                "Jane Smith", null, UserStatus.ACTIVE, null);

        when(userService.findAll()).thenReturn(List.of(user1, user2));

        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email").value("john@example.com"))
                .andExpect(jsonPath("$[1].email").value("jane@example.com"));

        verify(userService).findAll();
    }

    @Test
    @DisplayName("GET /api/users/{id} - Devrait retourner un utilisateur")
    void shouldReturnUserById() throws Exception {
        // Given
        UserDto user = new UserDto(1L, "john@example.com", "John", "Doe",
                "John Doe", "+33612345678", UserStatus.ACTIVE, null);

        when(userService.findById(1L)).thenReturn(user);

        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    @DisplayName("GET /api/users/{id} - Devrait retourner 404 si utilisateur non trouvé")
    void shouldReturn404WhenUserNotFound() throws Exception {
        // Given
        when(userService.findById(99L)).thenThrow(new UserNotFoundException(99L));

        // When & Then
        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Utilisateur non trouvé"))
                .andExpect(jsonPath("$.detail").value(containsString("99")));
    }

    @Test
    @DisplayName("POST /api/users - Devrait créer un utilisateur")
    void shouldCreateUser() throws Exception {
        // Given
        CreateUserRequest request = new CreateUserRequest(
                "new@example.com", "New", "User", null
        );

        UserDto createdUser = new UserDto(3L, "new@example.com", "New", "User",
                "New User", null, UserStatus.ACTIVE, null);

        when(userService.create(any(CreateUserRequest.class))).thenReturn(createdUser);

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/users/3"))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.email").value("new@example.com"));

        verify(userService).create(any(CreateUserRequest.class));
    }

    @Test
    @DisplayName("POST /api/users - Devrait retourner 400 si données invalides")
    void shouldReturn400WhenInvalidData() throws Exception {
        // Given - Email vide
        CreateUserRequest request = new CreateUserRequest(
                "", "New", "User", null
        );

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Données invalides"))
                .andExpect(jsonPath("$.errors.email").exists());

        verifyNoInteractions(userService);
    }

    @Test
    @DisplayName("POST /api/users - Devrait retourner 409 si email existe")
    void shouldReturn409WhenEmailExists() throws Exception {
        // Given
        CreateUserRequest request = new CreateUserRequest(
                "existing@example.com", "New", "User", null
        );

        when(userService.create(any(CreateUserRequest.class)))
                .thenThrow(new EmailAlreadyExistsException("existing@example.com"));

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflit - Email existant"));
    }

    @Test
    @DisplayName("DELETE /api/users/{id} - Devrait supprimer un utilisateur")
    void shouldDeleteUser() throws Exception {
        // Given
        doNothing().when(userService).delete(1L);

        // When & Then
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).delete(1L);
    }

    @Test
    @DisplayName("GET /api/users/search - Devrait rechercher des utilisateurs")
    void shouldSearchUsers() throws Exception {
        // Given
        UserDto user = new UserDto(1L, "john@example.com", "John", "Doe",
                "John Doe", null, UserStatus.ACTIVE, null);

        when(userService.search("john")).thenReturn(List.of(user));

        // When & Then
        mockMvc.perform(get("/api/users/search").param("q", "john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }
}
