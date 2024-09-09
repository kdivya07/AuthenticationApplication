package com.authentication.exceptions;

public class ResourceNotFoundException extends Throwable {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
