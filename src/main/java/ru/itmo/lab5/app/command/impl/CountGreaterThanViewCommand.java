package ru.itmo.lab5.app.command.impl;

import ru.itmo.lab5.app.command.Command;
import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;
import ru.itmo.lab5.model.View;

/**
 * count_greater_than_view view : вывести количество элементов, значение поля view которых больше заданного
 */
public final class CountGreaterThanViewCommand implements Command {

    @Override
    public String name() { return "count_greater_than_view"; }

    @Override
    public String description() {
        return "count_greater_than_view <view> : вывести количество элементов, значение поля view которых больше заданного";
    }

    @Override
    public void execute(String args, ExecutionContext ctx) throws CommandException {
        View view = parseView(args);
        long cnt = ctx.collections.countGreaterThanView(view);
        System.out.println(cnt);
    }

    private static View parseView(String args) throws CommandException {
        if (args == null || args.isBlank()) throw new CommandException("count_greater_than_view: требуется view");
        try { return View.valueOf(args.trim()); }
        catch (IllegalArgumentException ex) { throw new CommandException("count_greater_than_view: view должен быть одной из констант View"); }
    }
}
