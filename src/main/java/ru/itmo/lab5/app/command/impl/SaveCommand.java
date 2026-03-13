package ru.itmo.lab5.app.command.impl;

import ru.itmo.lab5.app.command.Command;
import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;
import ru.itmo.lab5.app.exception.IoAppException;

/**
 * save : сохранить коллекцию в файл
 */
public final class SaveCommand implements Command {

    @Override
    public String name() { return "save"; }

    @Override
    public String description() {
        return "save : сохранить коллекцию в файл";
    }

    @Override
    public void execute(String args, ExecutionContext ctx) throws CommandException {
        try {
            ctx.io.save(ctx.filePath, ctx.collections.getTable());
            System.out.println("Сохранено в файл: " + ctx.filePath);
        } catch (IoAppException ex) {
            throw new CommandException("save: " + ex.getMessage(), ex);
        }
    }
}
