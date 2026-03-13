package ru.itmo.lab5.app.command.impl;

import ru.itmo.lab5.app.command.Command;
import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;

/**
 * help : вывести справку по доступным командам
 */
public final class HelpCommand implements Command {

    @Override
    public String name() { return "help"; }

    @Override
    public String description() {
        return "help : вывести справку по доступным командам";
    }

    @Override
    public void execute(String args, ExecutionContext ctx) throws CommandException {
        for (var c : ctx.registry.all()) {
            System.out.println(c.description());
        }
        System.out.println();
        System.out.println("Примечание: для null вводите пустую строку при вводе полей объекта.");
        System.out.println("Команды insert/remove_key/replace_* ожидают ключ типа Long (пример: insert 5).");
    }
}
