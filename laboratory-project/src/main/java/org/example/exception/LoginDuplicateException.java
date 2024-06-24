package org.example.exception;

public class LoginDuplicateException extends BaseException {
    public LoginDuplicateException() {
    }

    public LoginDuplicateException(String message) {
        super(message);
    }

    public LoginDuplicateException(String message, Object... args) {
        super(message, args);
    }
}