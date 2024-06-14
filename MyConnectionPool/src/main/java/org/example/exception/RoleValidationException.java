package org.example.exception;

public class RoleValidationException extends BaseException {

    public RoleValidationException() {
    }

    public RoleValidationException(String message) {
        super(message);
    }

    public RoleValidationException(String message, Object... args) {
        super(message, args);
    }
}