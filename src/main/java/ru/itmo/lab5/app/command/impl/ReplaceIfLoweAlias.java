package ru.itmo.lab5.app.command.impl;

import ru.itmo.lab5.app.command.Command;
import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;

/**
 * Алиас для команды replace_if_lowe (опечатка в задании).
 */
public final class ReplaceIfLoweAlias implements Command {

    private final ReplaceIfLowerCommand delegate = new ReplaceIfLowerCommand();

    @Override
    public String name() { return "replace_if_lowe"; }

    @Override
    public String description() { return "replace_if_lowe <key> {element} : алиас команды replace_if_lower"; }

    @Override
    public void execute(String args, ExecutionContext ctx) throws CommandException {
        delegate.execute(args, ctx);
    }
}
