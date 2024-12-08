package com.personal.exception;

import lombok.Getter;

@Getter
public class UsernameException extends RuntimeException {
    private final String errorCode;

    public UsernameException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public static UsernameException userNotFound() {
        return new UsernameException("Username does not exist", "USER_NOT_FOUND");
    }

    public static UsernameException userAlreadyExists() {
        return new UsernameException("Username already exists", "USER_EXISTS");
    }
}
