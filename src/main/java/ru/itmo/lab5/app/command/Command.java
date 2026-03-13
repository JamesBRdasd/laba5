package ru.itmo.lab5.app.command;

import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;

/**
 * Команда интерактивного консольного приложения.
 */
public interface Command {

    /**
     * имя команды (то, что вводит пользователь).
     */
    String name();

    /**
     * Краткое описание (для help).
     */
    String description();

    /**
     * Выполнить команду.
     *
     * @param args строка аргументов (может быть пустой)
     * @param ctx контекст приложения
     * @throws CommandException ошибка выполнения команды
     */
    void execute(String args, ExecutionContext ctx) throws CommandException;
}
