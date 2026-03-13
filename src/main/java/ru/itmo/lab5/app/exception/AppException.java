package ru.itmo.lab5.app.exception;

/**
 * Базовое исключение приложения.
 */
public class AppException extends Exception {
    public AppException(String message) { super(message); }
    public AppException(String message, Throwable cause) { super(message, cause); }
}
