package com.authentication.rest;

import com.authentication.entities.UserSession;
import com.authentication.entities.LoginRequest;
import com.authentication.exceptions.ExistingSessionException;
import com.authentication.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class AuthenticationController {

    public static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<UserSession> login(@RequestBody LoginRequest loginRequest) throws ExistingSessionException {
        logger.debug("Received login request for email: {}", loginRequest.getEmail());
        UserSession session = authenticationService.login(loginRequest);
        logger.info("Login successful for email: {}", loginRequest.getEmail());
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String token) throws ExistingSessionException {
        logger.debug("Received logout request for token: {}", token);
        String message = authenticationService.logout(token);
        logger.info("Logout successful for token: {}", token);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestParam String token) {
        logger.debug("Received validate request for token: {}", token);
        ResponseEntity<String> response = authenticationService.validate(token);
        logger.info("Token validation successful for token: {}", token);
        return response;
    }
}
