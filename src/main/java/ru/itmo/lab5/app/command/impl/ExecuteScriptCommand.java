package ru.itmo.lab5.app.command.impl;

import ru.itmo.lab5.app.command.Command;
import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;

import java.io.File;

/**
 * execute_script file_name : считать и исполнить скрипт из файла
 */
public final class ExecuteScriptCommand implements Command {

    @Override
    public String name() { return "execute_script"; }

    @Override
    public String description() {
        return "execute_script <file_name> : считать и исполнить скрипт из указанного файла";
    }

    @Override
    public void execute(String args, ExecutionContext ctx) throws CommandException {
        if (args == null || args.isBlank()) {
            throw new CommandException("execute_script: требуется имя файла");
        }
        File file = new File(args.trim());
        if (!file.exists() || !file.isFile()) {
            throw new CommandException("execute_script: файл не найден: " + file.getPath());
        }
        String abs = file.getAbsolutePath();
        if (ctx.scriptStack.contains(abs)) {
            throw new CommandException("execute_script: обнаружена рекурсия скриптов: " + abs);
        }
        try {
            ctx.scriptStack.push(abs);
            ctx.input.pushScript(file);
            System.out.println("Скрипт подключён: " + abs);
        } catch (Exception ex) {
            ctx.scriptStack.remove(abs);
            throw new CommandException("execute_script: " + ex.getMessage(), ex);
        }
    }
}
