package ru.itmo.lab5.app.command;

import java.util.*;

/**
 * Реестр команд.
 */
public final class CommandRegistry {
    private final Map<String, Command> commands = new LinkedHashMap<>();

    public void register(Command cmd) {
        commands.put(cmd.name(), cmd);
    }

    public Command get(String name) {
        return commands.get(name);
    }

    public Collection<Command> all() {
        return Collections.unmodifiableCollection(commands.values());
    }
}
