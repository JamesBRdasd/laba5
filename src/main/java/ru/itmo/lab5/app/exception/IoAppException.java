package ru.itmo.lab5.app.exception;

/**
 * Ошибка ввода/вывода приложения.
 */
public class IoAppException extends AppException {
    public IoAppException(String message) { super(message); }
    public IoAppException(String message, Throwable cause) { super(message, cause); }
}
