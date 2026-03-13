package ru.itmo.lab5.app.command.impl;

import ru.itmo.lab5.app.command.Command;
import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;
import ru.itmo.lab5.model.Flat;
import ru.itmo.lab5.model.View;

/**
 * filter_less_than_view view : вывести элементы, значение поля view которых меньше заданного
 */
public final class FilterLessThanViewCommand implements Command {

    @Override
    public String name() { return "filter_less_than_view"; }

    @Override
    public String description() {
        return "filter_less_than_view <view> : вывести элементы, значение поля view которых меньше заданного";
    }

    @Override
    public void execute(String args, ExecutionContext ctx) throws CommandException {
        View view = parseView(args);
        var list = ctx.collections.filterLessThanView(view);
        if (list.isEmpty()) {
            System.out.println("(нет элементов)");
            return;
        }
        for (Flat f : list) {
            System.out.println(f);
        }
    }

    private static View parseView(String args) throws CommandException {
        if (args == null || args.isBlank()) throw new CommandException("filter_less_than_view: требуется view");
        try { return View.valueOf(args.trim()); }
        catch (IllegalArgumentException ex) { throw new CommandException("filter_less_than_view: view должен быть одной из констант View"); }
    }
}
