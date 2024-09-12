package com.authentication.entities;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Id
    private String email;
    private String password;


//    public LoginRequest(String email, String password) {
//        this.email = email;
//        this.password = password;
//    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
