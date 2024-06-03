package org.example.exception;

import java.text.MessageFormat;

public class BaseException extends RuntimeException{
    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }
}
