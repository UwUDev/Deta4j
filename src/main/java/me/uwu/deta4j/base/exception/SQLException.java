package me.uwu.deta4j.base.exception;

public class SQLException extends RuntimeException {
    public SQLException(String message) {
        super(message);
    }

    public static SQLException of(String message) {
        return new SQLException(message);
    }
}
