package ru.itmo.lab5.app.exception;

/**
 * Ошибка валидации пользовательского ввода или данных из файла.
 */
public class ValidationException extends AppException {
    public ValidationException(String message) { super(message); }
    public ValidationException(String message, Throwable cause) { super(message, cause); }
}
