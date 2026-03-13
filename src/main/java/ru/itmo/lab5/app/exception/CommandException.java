package ru.itmo.lab5.app.exception;

/**
 * Ошибка выполнения команды.
 */
public class CommandException extends AppException {
    public CommandException(String message) { super(message); }
    public CommandException(String message, Throwable cause) { super(message, cause); }
}
