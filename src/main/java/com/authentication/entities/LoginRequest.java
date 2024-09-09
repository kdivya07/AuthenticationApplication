package com.authentication.entities;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class LoginRequest {

    @Id
    private String email;
    private String password;

}
