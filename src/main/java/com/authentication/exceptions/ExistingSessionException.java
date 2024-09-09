package com.authentication.exceptions;

public class ExistingSessionException extends Exception {

    private static final long serialVersionUID = 1L;

    public ExistingSessionException() {
    }

    public ExistingSessionException(String message) {
        super(message);
    }

}
