package com.personal.exception;

import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {
    private final String errorCode;

    public AuthenticationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public static AuthenticationException invalidCredentials() {
        return new AuthenticationException("Invalid password", "INVALID_CREDENTIALS");
    }
}
