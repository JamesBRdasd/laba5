package ru.itmo.lab5.app.command.impl;

import ru.itmo.lab5.app.command.Command;
import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;
import ru.itmo.lab5.app.exception.ValidationException;
import ru.itmo.lab5.model.Flat;

import java.io.IOException;

/**
 * insert key {element} : добавить новый элемент с заданным ключом
 */
public final class InsertCommand implements Command {

    @Override
    public String name() { return "insert"; }

    @Override
    public String description() {
        return "insert <key> {element} : добавить новый элемент с заданным ключом";
    }

    @Override
    public void execute(String args, ExecutionContext ctx) throws CommandException {
        Long key = parseKey(args);
        try {
            Flat flat = ctx.input.readFlat(null, null);
            ctx.collections.insert(key, flat);
            System.out.println("Элемент добавлен. id=" + flat.getId());
        } catch (ValidationException | IOException ex) {
            throw new CommandException("insert: " + ex.getMessage(), ex);
        }
    }

    private static Long parseKey(String args) throws CommandException {
        if (args == null || args.isBlank()) throw new CommandException("insert: требуется ключ (Long)");
        try {
            return Long.parseLong(args.trim());
        } catch (NumberFormatException ex) {
            throw new CommandException("insert: ключ должен быть числом (Long)");
        }
    }
}
