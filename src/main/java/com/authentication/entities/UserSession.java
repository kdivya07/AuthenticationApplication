package com.authentication.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserSession {

    private String email;
    @Id
    private int id;
    private String token;

    public UserSession(String token, int id, String email) {
        this.token = token;
        this.id = id;
        this.email = email;
    }
}