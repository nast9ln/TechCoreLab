package org.example.constanst;


public enum WebConstant {
    APPLICATION_JSON("application/json"),
    TEXT_HTML("text/html"),
    IDENTIFICATION_FIELD("id"),
    ERROR_ATTRIBUTE("error");

    private final String value;

    WebConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
