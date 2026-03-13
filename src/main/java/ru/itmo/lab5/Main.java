package ru.itmo.lab5;

import ru.itmo.lab5.app.ConsoleApp;

/**
 * Точка входа в приложение.
 */
public final class Main {

    /**
     * имя переменной окружения с путём к XML-файлу.
     */
    public static final String ENV_FILE = "FLATS_FILE";

    private Main() { }

    /**
     * Запуск приложения.
     */
    public static void main(String[] args) {
        new ConsoleApp(ENV_FILE).run();
    }
}
