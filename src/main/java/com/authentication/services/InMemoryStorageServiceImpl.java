package com.authentication.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryStorageServiceImpl implements InMemoryStorageService {

    private final Map<String, String> emailToTokenMap = new HashMap<>();

    public void put(String email, String token) {
        emailToTokenMap.put(email, token);

    }

    public String get(String email) {
        return emailToTokenMap.get(email);
    }

    public String getEmailByToken(String token) {
        for (Map.Entry<String, String> entry : emailToTokenMap.entrySet()) {
            if (entry.getValue().equals(token)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void remove(String email) {
        emailToTokenMap.remove(email);
    }
}
