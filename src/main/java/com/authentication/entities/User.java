package com.authentication.entities;

import com.authentication.constants.EntityConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull(message = EntityConstants.EMPTY_PASSWORD)
    @Size(min = 6, max = 12, message = EntityConstants.PASSWORD_SIZE)
    @Column(name = "password")
    private String password;

    @NotNull(message = EntityConstants.EMPTY_EMAIL)
    @Email(message = EntityConstants.VALID_EMAIL)
    @Column(name = "email", unique = true)
    private String email;

    public int getId() {
        return id;
    }

    public @NotNull(message = EntityConstants.EMPTY_PASSWORD) @Size(min = 6, max = 12, message = EntityConstants.PASSWORD_SIZE) String getPassword() {
        return password;
    }

    public void setPassword(@NotNull(message = EntityConstants.EMPTY_PASSWORD) @Size(min = 6, max = 12, message = EntityConstants.PASSWORD_SIZE) String password) {
        this.password = password;
    }

    public void setEmail(@NotNull(message = EntityConstants.EMPTY_EMAIL) @Email(message = EntityConstants.VALID_EMAIL) String email) {
        this.email = email;
    }

    public void setId(long l) {
        this.id = id;
    }
}
