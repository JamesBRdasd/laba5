package ru.itmo.lab5.app.command.impl;

import ru.itmo.lab5.app.command.Command;
import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;
import ru.itmo.lab5.app.exception.ValidationException;
import ru.itmo.lab5.model.Flat;

import java.io.IOException;

/**
 * update id {element} : обновить значение элемента коллекции, id которого равен заданному
 */
public final class UpdateCommand implements Command {

    @Override
    public String name() { return "update"; }

    @Override
    public String description() {
        return "update <id> {element} : обновить значение элемента коллекции, id которого равен заданному";
    }

    @Override
    public void execute(String args, ExecutionContext ctx) throws CommandException {
        long id = parseId(args);
        try {
            Flat newData = ctx.input.readFlat(null, null);
            boolean ok = ctx.collections.updateById(id, newData);
            System.out.println(ok ? "Обновлено." : "Элемент с таким id не найден.");
        } catch (ValidationException | IOException ex) {
            throw new CommandException("update: " + ex.getMessage(), ex);
        }
    }

    private static long parseId(String args) throws CommandException {
        if (args == null || args.isBlank()) throw new CommandException("update: требуется id (Long)");
        try {
            return Long.parseLong(args.trim());
        } catch (NumberFormatException ex) {
            throw new CommandException("update: id должен быть числом (Long)");
        }
    }
}
