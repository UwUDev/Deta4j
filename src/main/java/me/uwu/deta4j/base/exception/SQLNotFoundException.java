package me.uwu.deta4j.base.exception;

public class SQLNotFoundException extends SQLException {
    public SQLNotFoundException(String message) {
        super(message);
    }
    public static SQLNotFoundException of(String message) {
        return new SQLNotFoundException(message);
    }
}
