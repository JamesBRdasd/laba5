package ru.itmo.lab5.app.command.impl;

import ru.itmo.lab5.app.command.Command;
import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;

/**
 * exit : завершить программу (без сохранения)
 */
public final class ExitCommand implements Command {

    @Override
    public String name() { return "exit"; }

    @Override
    public String description() {
        return "exit : завершить программу (без сохранения в файл)";
    }

    @Override
    public void execute(String args, ExecutionContext ctx) throws CommandException {
        ctx.shouldExit = true;
        System.out.println("Выход без сохранения.");
    }
}
