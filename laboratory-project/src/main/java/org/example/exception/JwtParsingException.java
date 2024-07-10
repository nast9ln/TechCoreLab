package org.example.exception;

public class JwtParsingException extends BaseException {

    public JwtParsingException() {
        super("Exception while processing claims from the token");
    }

    public JwtParsingException(String message) {
        super(message);
    }

    public JwtParsingException(String message, Object... args) {
        super(message, args);
    }

}