package me.uwu.deta4j.base.exception;

public class SQLConflictException extends SQLException {
    public SQLConflictException(String message) {
        super(message);
    }
    public static SQLConflictException of(String message) {
        return new SQLConflictException(message);
    }
}
