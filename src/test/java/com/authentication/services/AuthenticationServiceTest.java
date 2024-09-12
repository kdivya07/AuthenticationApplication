package com.authentication.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.authentication.entities.User;
import com.authentication.entities.UserSession;
import com.authentication.exceptions.ExistingSessionException;
import com.authentication.repositories.UserRepository;
import com.authentication.constants.ErrorConstants;
import com.authentication.entities.LoginRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InMemoryStorageServiceImpl inMemoryStorageService; // Ensure this matches the actual interface

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password"); // Ensure this matches the password used in LoginRequest
    }

    @Test
    void login_SuccessfulLogin_ReturnsUserSession() throws ExistingSessionException {

        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(inMemoryStorageService.get(loginRequest.getEmail())).thenReturn(null);

        UserSession session = authenticationService.login(loginRequest);

        assertNotNull(session);
        assertEquals(loginRequest.getEmail(), session.getEmail());
        assertEquals(user.getId(), session.getId());
        assertNotNull(session.getToken());

        verify(inMemoryStorageService).put(loginRequest.getEmail(), session.getToken());
    }

    @Test
    void login_ExistingSession_ThrowsExistingSessionException() throws ExistingSessionException {

        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(inMemoryStorageService.get(loginRequest.getEmail())).thenReturn("existingToken");

        ExistingSessionException thrown = assertThrows(ExistingSessionException.class, () -> {
            authenticationService.login(loginRequest);
        });
        assertEquals(ErrorConstants.USER_ALREADY_LOGGED_IN, thrown.getMessage());
    }

    @Test
    void login_IncorrectPassword_ThrowsExistingSessionException(){
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrongPassword");

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        ExistingSessionException thrown = assertThrows(ExistingSessionException.class, () -> {
            authenticationService.login(loginRequest);
        });
        assertEquals(ErrorConstants.INCORRECT_PASSWORD, thrown.getMessage());
    }

    @Test
    void login_EmailNotFound_ThrowsExistingSessionException() throws ExistingSessionException {
        LoginRequest loginRequest = new LoginRequest("nonexistent@example.com", "password");
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        ExistingSessionException thrown = assertThrows(ExistingSessionException.class, () -> {
            authenticationService.login(loginRequest);
        });
        assertEquals(ErrorConstants.EMAIL_NOT_FOUND, thrown.getMessage());
    }

    @Test
    void logout_SuccessfulLogout_ReturnsSuccessMessage() throws ExistingSessionException {
        String token = UUID.randomUUID().toString();
        String email = "test@example.com";
        when(inMemoryStorageService.getEmailByToken(token)).thenReturn(email);

        String response = authenticationService.logout(token);

        assertEquals("Logged out: " + email, response);
        verify(inMemoryStorageService).remove(email);
    }

    @Test
    void logout_InvalidToken_ThrowsExistingSessionException() throws ExistingSessionException {
        String token = UUID.randomUUID().toString();
        when(inMemoryStorageService.getEmailByToken(token)).thenReturn(null);

        ExistingSessionException thrown = assertThrows(ExistingSessionException.class, () -> {
            authenticationService.logout(token);
        });
        assertEquals(ErrorConstants.INVALID_TOKEN_OR_SESSION_NOT_FOUND, thrown.getMessage());
    }

    @Test
    void validate_ValidToken_ReturnsValidSession() {
        String token = UUID.randomUUID().toString();
        String email = "test@example.com";
        when(inMemoryStorageService.getEmailByToken(token)).thenReturn(email);

        ResponseEntity<String> response = authenticationService.validate(token);

        assertEquals(new ResponseEntity<>(ErrorConstants.VALID_SESSION + email, HttpStatus.OK), response);
    }

    @Test
    void validate_InvalidToken_ReturnsUnauthorized() {
        String token = UUID.randomUUID().toString();
        when(inMemoryStorageService.getEmailByToken(token)).thenReturn(null);

        ResponseEntity<String> response = authenticationService.validate(token);

        assertEquals(new ResponseEntity<>(ErrorConstants.INVALID_OR_EXPIRED_SESSION, HttpStatus.UNAUTHORIZED), response);
    }
}
