package ru.itmo.lab5.app.command.impl;

import ru.itmo.lab5.app.command.Command;
import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;

/**
 * remove_key key : удалить элемент по ключу
 */
public final class RemoveKeyCommand implements Command {

    @Override
    public String name() { return "remove_key"; }

    @Override
    public String description() {
        return "remove_key <key> : удалить элемент из коллекции по его ключу";
    }

    @Override
    public void execute(String args, ExecutionContext ctx) throws CommandException {
        Long key = parseKey(args);
        boolean ok = ctx.collections.removeKey(key);
        System.out.println(ok ? "Удалено." : "Элемент с таким ключом не найден.");
    }

    private static Long parseKey(String args) throws CommandException {
        if (args == null || args.isBlank()) throw new CommandException("remove_key: требуется ключ (Long)");
        try { return Long.parseLong(args.trim()); }
        catch (NumberFormatException ex) { throw new CommandException("remove_key: ключ должен быть числом (Long)"); }
    }
}
