package com.authentication.services;

import com.authentication.entities.UserSession;
import com.authentication.entities.LoginRequest;
import com.authentication.exceptions.ExistingSessionException;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    public UserSession login(LoginRequest loginRequest) throws ExistingSessionException;

    public String logout(String token) throws ExistingSessionException;

    public ResponseEntity<String> validate(String token);
}
