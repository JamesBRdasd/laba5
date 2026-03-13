package ru.itmo.lab5.app.command.impl;

import ru.itmo.lab5.app.command.Command;
import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;
import ru.itmo.lab5.app.exception.ValidationException;
import ru.itmo.lab5.model.Flat;

import java.io.IOException;

/**
 * replace_if_greater key {element} : заменить значение по ключу, если новое значение больше старого
 */
public final class ReplaceIfGreaterCommand implements Command {

    @Override
    public String name() { return "replace_if_greater"; }

    @Override
    public String description() {
        return "replace_if_greater <key> {element} : заменить значение по ключу, если новое значение больше старого";
    }

    @Override
    public void execute(String args, ExecutionContext ctx) throws CommandException {
        Long key = parseKey(args);
        try {
            Flat candidate = ctx.input.readFlat(null, null);
            boolean ok = ctx.collections.replaceIfGreater(key, candidate);
            System.out.println(ok ? "Заменено." : "Не заменено (ключ не найден или новое значение не больше).");
        } catch (ValidationException | IOException ex) {
            throw new CommandException("replace_if_greater: " + ex.getMessage(), ex);
        }
    }

    private static Long parseKey(String args) throws CommandException {
        if (args == null || args.isBlank()) throw new CommandException("replace_if_greater: требуется ключ (Long)");
        try { return Long.parseLong(args.trim()); }
        catch (NumberFormatException ex) { throw new CommandException("replace_if_greater: ключ должен быть Long"); }
    }
}
