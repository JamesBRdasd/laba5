package ru.itmo.lab5.app.command.impl;

import ru.itmo.lab5.app.command.Command;
import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;

/**
 * print_unique_furnish : вывести уникальные значения поля furnish
 */
public final class PrintUniqueFurnishCommand implements Command {

    @Override
    public String name() { return "print_unique_furnish"; }

    @Override
    public String description() {
        return "print_unique_furnish : вывести уникальные значения поля furnish всех элементов в коллекции";
    }

    @Override
    public void execute(String args, ExecutionContext ctx) throws CommandException {
        var set = ctx.collections.uniqueFurnish();
        if (set.isEmpty()) {
            System.out.println("(уникальных furnish нет — все null или коллекция пуста)");
            return;
        }
        for (var f : set) {
            System.out.println(f);
        }
    }
}
