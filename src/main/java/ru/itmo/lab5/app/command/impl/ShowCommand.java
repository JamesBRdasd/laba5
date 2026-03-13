package ru.itmo.lab5.app.command.impl;

import ru.itmo.lab5.app.command.Command;
import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;
import ru.itmo.lab5.model.Flat;

/**
 * show : вывести все элементы коллекции
 */
public final class ShowCommand implements Command {

    @Override
    public String name() { return "show"; }

    @Override
    public String description() {
        return "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }

    @Override
    public void execute(String args, ExecutionContext ctx) throws CommandException {
        if (ctx.collections.size() == 0) {
            System.out.println("(коллекция пуста)");
            return;
        }
        for (Flat f : ctx.collections.sortedValues()) {
            System.out.println(f);
        }
    }
}
