package ru.itmo.lab5.app.command.impl;

import ru.itmo.lab5.app.command.Command;
import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;

/**
 * clear : очистить коллекцию
 */
public final class ClearCommand implements Command {

    @Override
    public String name() { return "clear"; }

    @Override
    public String description() {
        return "clear : очистить коллекцию";
    }

    @Override
    public void execute(String args, ExecutionContext ctx) throws CommandException {
        ctx.collections.clear();
        System.out.println("Коллекция очищена.");
    }
}
