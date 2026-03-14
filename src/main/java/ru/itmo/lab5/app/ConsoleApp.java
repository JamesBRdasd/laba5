package ru.itmo.lab5.app;

import ru.itmo.lab5.app.command.Command;
import ru.itmo.lab5.app.command.CommandRegistry;
import ru.itmo.lab5.app.command.impl.*;
import ru.itmo.lab5.app.core.CollectionManager;
import ru.itmo.lab5.app.core.ExecutionContext;
import ru.itmo.lab5.app.exception.CommandException;
import ru.itmo.lab5.app.exception.IoAppException;
import ru.itmo.lab5.app.io.InputManager;
import ru.itmo.lab5.io.XmlCollectionIO;

import java.io.File;
import java.util.Hashtable;

/**
 * Консольное приложение управления коллекцией Flat.
 */
public final class ConsoleApp {

    private final String envName;

    public ConsoleApp(String envName) {
        this.envName = envName;
    }

    /**
     * Запускает приложение в интерактивном режиме.
     */
    public void run() {
        String filePath = System.getenv(envName);
        if (filePath == null || filePath.isBlank()) {
            System.out.println("Ошибка: переменная окружения " + envName + " не задана.");
            System.out.println("Пример: export " + envName + "=/path/to/flats.xml");
            return;
        }

        XmlCollectionIO io = new XmlCollectionIO();
        Hashtable<Long, ru.itmo.lab5.model.Flat> loaded;
        try {
            loaded = io.load(filePath);
            System.out.println("Коллекция загружена из файла: " + filePath + " (элементов: " + loaded.size() + ")");
        } catch (IoAppException ex) {
            System.out.println("Не удалось загрузить коллекцию: " + ex.getMessage());
            loaded = new Hashtable<>();
        }

        CollectionManager cm = new CollectionManager(loaded);
        InputManager input = new InputManager();
        CommandRegistry registry = new CommandRegistry();
        registerCommands(registry);

        ExecutionContext ctx = new ExecutionContext(cm, input, io, registry, filePath);

        System.out.println("Введите команду. Для справки: help");
        while (!ctx.shouldExit) {
            boolean wasInteractive = input.isInteractive();
            try {
                if (wasInteractive) System.out.print("> ");
                String line = input.readLine();
                if (line == null) {
                    if (wasInteractive) {
                        // EOF в интерактивном режиме
                        ctx.shouldExit = true;
                        System.out.println("Ввод завершён. Выход.");
                        continue;
                    } else {
                        // скрипт закончился
                        input.popSource();
                        if (!ctx.scriptStack.isEmpty()) ctx.scriptStack.pop();
                        System.out.println("Скрипт завершён. Возврат в предыдущий режим ввода.");
                        continue;
                    }
                }
                line = line.trim();
                if (line.isEmpty()) continue;

                String cmdName;
                String args;
                int sp = line.indexOf(' ');
                if (sp < 0) { cmdName = line; args = ""; }
                else { cmdName = line.substring(0, sp); args = line.substring(sp + 1).trim(); }

                Command cmd = registry.get(cmdName);
                if (cmd == null) {
                    System.out.println("Неизвестная команда: " + cmdName + ". help — список команд.");
                    continue;
                }
                cmd.execute(args, ctx);
            } catch (CommandException ex) {
                System.out.println("Ошибка: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Непредвиденная ошибка: " + ex.getMessage());
            }
        }
    }

    private static void registerCommands(CommandRegistry r) {
        r.register(new HelpCommand());
        r.register(new InfoCommand());
        r.register(new ShowCommand());
        r.register(new InsertCommand());
        r.register(new UpdateCommand());
        r.register(new RemoveKeyCommand());
        r.register(new ClearCommand());
        r.register(new SaveCommand());
        r.register(new ExecuteScriptCommand());
        r.register(new ExitCommand());
        r.register(new RemoveGreaterCommand());
        r.register(new ReplaceIfGreaterCommand());
        r.register(new ReplaceIfLowerCommand());
        r.register(new CountGreaterThanViewCommand());
        r.register(new FilterLessThanViewCommand());
        r.register(new PrintUniqueFurnishCommand());
    }
}
