package com.authentication.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryStorageServiceTest {

    private InMemoryStorageServiceImpl storageService;

    @BeforeEach
    void setUp() {
        storageService = new InMemoryStorageServiceImpl();
    }

    @Test
    void put_ValidData_StoresToken() {
        String email = "test@example.com";
        String token = "token123";
        storageService.put(email, token);
        assertEquals(token, storageService.get(email));
    }

    @Test
    void get_ExistingEmail_ReturnsToken() {
        String email = "test@example.com";
        String token = "token123";
        storageService.put(email, token);
        String retrievedToken = storageService.get(email);
        assertEquals(token, retrievedToken);
    }

    @Test
    void get_UnknownEmail_ReturnsNull() {
        String token = storageService.get("unknown@example.com");
        assertNull(token);
    }

    @Test
    void getEmailByToken_ExistingToken_ReturnsEmail() {
        String email = "test@example.com";
        String token = "token123";
        storageService.put(email, token);
        String retrievedEmail = storageService.getEmailByToken(token);
        assertEquals(email, retrievedEmail);
    }

    @Test
    void getEmailByToken_UnknownToken_ReturnsNull() {
        String email = storageService.getEmailByToken("unknownToken");
        assertNull(email);
    }

    @Test
    void remove_ExistingEmail_RemovesToken() {
        String email = "test@example.com";
        String token = "token123";
        storageService.put(email, token);
        storageService.remove(email);
        assertNull(storageService.get(email));
    }

    @Test
    void remove_NonExistingEmail_DoesNothing() {
        storageService.remove("nonexistent@example.com");

        assertNull(storageService.get("nonexistent@example.com"));
    }
}
