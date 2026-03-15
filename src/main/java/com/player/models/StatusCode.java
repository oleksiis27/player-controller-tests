package com.player.models;

public enum StatusCode {

    OK(200),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    FORBIDDEN(403);

    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}