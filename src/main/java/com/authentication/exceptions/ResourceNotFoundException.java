package com.authentication.exceptions;

import lombok.Data;

@Data
public class ResourceNotFoundException extends Throwable {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
