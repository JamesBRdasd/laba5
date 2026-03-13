package ru.itmo.lab5.app.command.impl;

import ru.itmo.lab5.app.command.Command;
import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;

/**
 * info : вывести информацию о коллекции
 */
public final class InfoCommand implements Command {

    @Override
    public String name() { return "info"; }

    @Override
    public String description() {
        return "info : вывести информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }

    @Override
    public void execute(String args, ExecutionContext ctx) throws CommandException {
        System.out.println("Тип коллекции: " + ctx.collections.getTable().getClass().getName());
        System.out.println("Дата инициализации: " + ctx.collections.getInitDate());
        System.out.println("Количество элементов: " + ctx.collections.size());
        System.out.println("Файл: " + ctx.filePath);
    }
}
