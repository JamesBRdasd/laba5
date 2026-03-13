package ru.itmo.lab5.app.command.impl;

import ru.itmo.lab5.app.command.Command;
import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;
import ru.itmo.lab5.app.exception.ValidationException;
import ru.itmo.lab5.model.Flat;

import java.io.IOException;

/**
 * remove_greater {element} : удалить из коллекции все элементы, превышающие заданный
 */
public final class RemoveGreaterCommand implements Command {

    @Override
    public String name() { return "remove_greater"; }

    @Override
    public String description() {
        return "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный";
    }

    @Override
    public void execute(String args, ExecutionContext ctx) throws CommandException {
        try {
            Flat pivot = ctx.input.readFlat(1L, new java.util.Date()); // временно, валидатор требует не-null
            int removed = ctx.collections.removeGreater(pivot);
            System.out.println("Удалено элементов: " + removed);
        } catch (ValidationException | IOException ex) {
            throw new CommandException("remove_greater: " + ex.getMessage(), ex);
        }
    }
}
