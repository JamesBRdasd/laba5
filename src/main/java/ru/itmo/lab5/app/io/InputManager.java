package ru.itmo.lab5.app.io;

import ru.itmo.lab5.app.exception.ValidationException;
import ru.itmo.lab5.model.*;
import ru.itmo.lab5.util.Validators;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Менеджер ввода: поддерживает интерактивный ввод (stdin) и ввод из скриптов.
 */
public final class InputManager {

    private static final class Source {
        final BufferedReader reader;
        final String name;
        final boolean interactive;

        Source(BufferedReader reader, String name, boolean interactive) {
            this.reader = reader;
            this.name = name;
            this.interactive = interactive;
        }
    }

    private final Deque<Source> sources = new LinkedList<>();

    public InputManager() {
        // базовый источник: консоль
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        sources.push(new Source(br, "<stdin>", true));
    }

    /**
     * Подключает новый источник ввода (скрипт).
     *
     * @param file файл скрипта
     * @throws IOException если скрипт нельзя открыть
     */
    public void pushScript(File file) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        sources.push(new Source(br, file.getAbsolutePath(), false));
    }

    /**
     * Отключает текущий источник (обычно, когда скрипт закончился).
     */
    public void popSource() {
        if (sources.size() > 1) {
            try { sources.peek().reader.close(); } catch (IOException ignored) {}
            sources.pop();
        }
    }

    public String currentSourceName() {
        return sources.peek().name;
    }

    public boolean isInteractive() {
        return sources.peek().interactive;
    }

    /**
     * Считывает одну строку (команду или поле).
     *
     * @return строка или null, если поток закончился
     * @throws IOException ошибка чтения
     */
    public String readLine() throws IOException {
        return sources.peek().reader.readLine();
    }

    private void prompt(String msg) {
        if (isInteractive()) {
            System.out.print(msg);
        }
    }

    public String readString(String field, boolean allowNull, boolean allowEmpty) throws IOException, ValidationException {
        while (true) {
            prompt("Введите " + field + ": ");
            String s = readLine();
            if (s == null) throw new ValidationException("Неожиданный конец ввода (" + currentSourceName() + ")");
            if (s.isEmpty() && allowNull) return null;
            if (!allowEmpty && s.isBlank()) {
                System.out.println("Ошибка: значение не должно быть пустым. Повторите ввод.");
                continue;
            }
            if (!allowNull && s.isEmpty()) {
                System.out.println("Ошибка: значение не может быть null (пустая строка). Повторите ввод.");
                continue;
            }
            return s;
        }
    }

    public Long readLong(String field, boolean allowNull, Long minExclusive) throws IOException, ValidationException {
        while (true) {
            prompt("Введите " + field + ": ");
            String s = readLine();
            if (s == null) throw new ValidationException("Неожиданный конец ввода (" + currentSourceName() + ")");
            if (s.isEmpty() && allowNull) return null;
            try {
                long v = Long.parseLong(s.trim());
                if (minExclusive != null && v <= minExclusive) {
                    System.out.println("Ошибка: " + field + " должно быть > " + minExclusive);
                    continue;
                }
                return v;
            } catch (NumberFormatException ex) {
                System.out.println("Ошибка: ожидается целое число (Long). Повторите ввод.");
            }
        }
    }

    public Integer readInt(String field, boolean allowNull, Integer minExclusive) throws IOException, ValidationException {
        while (true) {
            prompt("Введите " + field + ": ");
            String s = readLine();
            if (s == null) throw new ValidationException("Неожиданный конец ввода (" + currentSourceName() + ")");
            if (s.isEmpty() && allowNull) return null;
            try {
                int v = Integer.parseInt(s.trim());
                if (minExclusive != null && v <= minExclusive) {
                    System.out.println("Ошибка: " + field + " должно быть > " + minExclusive);
                    continue;
                }
                return v;
            } catch (NumberFormatException ex) {
                System.out.println("Ошибка: ожидается целое число (int). Повторите ввод.");
            }
        }
    }

    public Double readDouble(String field, boolean allowNull) throws IOException, ValidationException {
        while (true) {
            prompt("Введите " + field + ": ");
            String s = readLine();
            if (s == null) throw new ValidationException("Неожиданный конец ввода (" + currentSourceName() + ")");
            if (s.isEmpty() && allowNull) return null;
            try {
                return Double.parseDouble(s.trim());
            } catch (NumberFormatException ex) {
                System.out.println("Ошибка: ожидается число (Double). Повторите ввод.");
            }
        }
    }

    public Float readFloat(String field, boolean allowNull, Float minExclusive) throws IOException, ValidationException {
        while (true) {
            prompt("Введите " + field + ": ");
            String s = readLine();
            if (s == null) throw new ValidationException("Неожиданный конец ввода (" + currentSourceName() + ")");
            if (s.isEmpty() && allowNull) return null;
            try {
                float v = Float.parseFloat(s.trim());
                if (minExclusive != null && v <= minExclusive) {
                    System.out.println("Ошибка: " + field + " должно быть > " + minExclusive);
                    continue;
                }
                return v;
            } catch (NumberFormatException ex) {
                System.out.println("Ошибка: ожидается число (Float). Повторите ввод.");
            }
        }
    }

    public <E extends Enum<E>> E readEnum(String field, Class<E> enumClass, boolean allowNull) throws IOException, ValidationException {
        E[] values = enumClass.getEnumConstants();
        while (true) {
            prompt("Введите " + field + " " + Arrays.toString(values) + ": ");
            String s = readLine();
            if (s == null) throw new ValidationException("Неожиданный конец ввода (" + currentSourceName() + ")");
            if (s.isEmpty() && allowNull) return null;
            try {
                return Enum.valueOf(enumClass, s.trim());
            } catch (IllegalArgumentException ex) {
                System.out.println("Ошибка: нужно ввести одну из констант " + Arrays.toString(values));
            }
        }
    }

    /**
     * Считывает объект Flat (без auto-полей id/creationDate).
     *
     * @param id           id (если null — будет создан позже)
     * @param creationDate creationDate (если null — будет создан позже)
     */
    public Flat readFlat(Long id, Date creationDate) throws IOException, ValidationException {
        String name = readString("name", false, false);

        Double x = readDouble("coordinates.x", false);
        Long y = readLong("coordinates.y", false, null);
        Coordinates coordinates = new Coordinates(Validators.requireNonNull(x, "coordinates.x"),
                Validators.requireNonNull(y, "coordinates.y"));

        Float area = readFloat("area (пустая строка = null)", true, null);
        if (area != null && area <= 0) {
            System.out.println("Ошибка: area должно быть > 0");
            return readFlat(id, creationDate);
        }

        Long rooms = readLong("numberOfRooms", false, 0L);

        Furnish furnish = readEnum("furnish (пустая строка = null)", Furnish.class, true);
        View view = readEnum("view", View.class, false);
        Transport transport = readEnum("transport", Transport.class, false);

        String houseName = readString("house.name (пустая строка = null)", true, true);
        Integer year = readInt("house.year", false, 0);
        Long lifts = readLong("house.numberOfLifts", false, 0L);
        House house = new House(houseName, year, lifts);

        Flat flat = new Flat(id, name, coordinates, creationDate, area, rooms, furnish, view, transport, house);
        Validators.validateFlat(new Flat(
                id == null ? 1L : id, // временно для проверки, настоящий id поставит менеджер
                name, coordinates,
                creationDate == null ? new Date() : creationDate,
                area, rooms, furnish, view, transport, house
        ));
        return flat;
    }
}
