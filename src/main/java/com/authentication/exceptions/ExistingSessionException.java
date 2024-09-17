package com.authentication.exceptions;

import lombok.Data;

@Data
public class ExistingSessionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExistingSessionException() {
    }

    public ExistingSessionException(String message) {
        super(message);
    }

}
