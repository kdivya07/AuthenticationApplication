package com.authentication.services;

public interface InMemoryStorageService {

    public void put(String email, String token);

    public String get(String email);

    public String getEmailByToken(String token);

    public void remove(String email);
}
