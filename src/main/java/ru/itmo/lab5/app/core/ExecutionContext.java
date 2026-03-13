package ru.itmo.lab5.app.core;

import ru.itmo.lab5.app.command.CommandRegistry;
import ru.itmo.lab5.app.io.InputManager;
import ru.itmo.lab5.io.XmlCollectionIO;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Контекст выполнения: коллекция, ввод, IO, реестр команд, стек скриптов.
 */
public final class ExecutionContext {
    public final CollectionManager collections;
    public final InputManager input;
    public final XmlCollectionIO io;
    public final CommandRegistry registry;
    public final String filePath;
    public final Deque<String> scriptStack = new LinkedList<>();
    public boolean shouldExit = false;

    public ExecutionContext(CollectionManager collections, InputManager input, XmlCollectionIO io,
                            CommandRegistry registry, String filePath) {
        this.collections = collections;
        this.input = input;
        this.io = io;
        this.registry = registry;
        this.filePath = filePath;
    }
}
