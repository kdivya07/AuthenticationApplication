package com.authentication.services;

import com.authentication.entities.UserSession;
import com.authentication.entities.LoginRequest;
import com.authentication.entities.User;
import com.authentication.constants.ErrorConstants;
import com.authentication.exceptions.ExistingSessionException;
import com.authentication.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InMemoryStorageService inMemoryStorageService;


    public UserSession login(LoginRequest loginRequest) throws ExistingSessionException {

        logger.debug("Attempting to log in user with email: {}", loginRequest.getEmail());
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getPassword().equals(loginRequest.getPassword())) {
                String existingToken = inMemoryStorageService.get(loginRequest.getEmail());

                if (existingToken == null) {
                    String token = UUID.randomUUID().toString();
                    UserSession session = new UserSession(loginRequest.getEmail(), user.getId(), token);

                    inMemoryStorageService.put(loginRequest.getEmail(), token);
                    logger.info("User with email {} logged in successfully. Session token: {}", loginRequest.getEmail(), token);
                    return session;
                } else {
                    logger.warn("User with email {} is already logged in.", loginRequest.getEmail());
                    throw new ExistingSessionException(ErrorConstants.USER_ALREADY_LOGGED_IN);
                }
            } else {
                logger.warn("Incorrect password for user with email {}", loginRequest.getEmail());
                throw new ExistingSessionException(ErrorConstants.INCORRECT_PASSWORD);
            }
        } else {
            logger.warn("Email not found: {}", loginRequest.getEmail());
            throw new ExistingSessionException(ErrorConstants.EMAIL_NOT_FOUND);
        }
    }


    @Override
    public String logout(String token) throws ExistingSessionException {
        logger.info("Attempting to logout user with token:{}", token);
        String email = inMemoryStorageService.getEmailByToken(token);

        if (email != null) {
            inMemoryStorageService.remove(email);
            logger.info("User with email {} logged out successfully.", email);
            return "Logged out: " + email;
        } else {
            logger.warn("Invalid token or session not found for token: {}", token);
            throw new ExistingSessionException(ErrorConstants.INVALID_TOKEN_OR_SESSION_NOT_FOUND);
        }
    }


    @Override
    public ResponseEntity<String> validate(String token) {
        logger.debug("Validating session with token: {}", token);
        String email = inMemoryStorageService.getEmailByToken(token);

        if (email != null) {
            logger.info("Session with token {} is valid for email: {}", token, email);
            return new ResponseEntity<>(ErrorConstants.VALID_SESSION + email, HttpStatus.OK);
        } else {
            logger.warn("Invalid or expired session for token: {}", token);
            return new ResponseEntity<>(ErrorConstants.INVALID_OR_EXPIRED_SESSION, HttpStatus.UNAUTHORIZED);
        }
    }

}
