package ru.itmo.lab5.util;

import ru.itmo.lab5.app.exception.ValidationException;
import ru.itmo.lab5.model.*;

import java.util.Date;
import java.util.Objects;

/**
 * Утилиты для проверки ограничений полей.
 */
public final class Validators {

    private Validators() { }

    public static void validateFlat(Flat f) throws ValidationException {
        if (f == null) throw new ValidationException("Flat не может быть null");

        validateId(f.getId());
        validateName(f.getName());
        validateCoordinates(f.getCoordinates());
        validateCreationDate(f.getCreationDate());

        if (f.getArea() != null && f.getArea() <= 0) {
            throw new ValidationException("area должно быть > 0");
        }
        if (f.getNumberOfRooms() == null) throw new ValidationException("numberOfRooms не может быть null");
        if (f.getNumberOfRooms() <= 0) throw new ValidationException("numberOfRooms должно быть > 0");

        if (f.getView() == null) throw new ValidationException("view не может быть null");
        if (f.getTransport() == null) throw new ValidationException("transport не может быть null");

        validateHouse(f.getHouse());
    }

    public static void validateId(Long id) throws ValidationException {
        if (id == null) throw new ValidationException("id не может быть null");
        if (id <= 0) throw new ValidationException("id должно быть > 0");
    }

    public static void validateName(String name) throws ValidationException {
        if (name == null) throw new ValidationException("name не может быть null");
        if (name.isBlank()) throw new ValidationException("name не может быть пустым");
    }

    public static void validateCoordinates(Coordinates c) throws ValidationException {
        if (c == null) throw new ValidationException("coordinates не может быть null");
        if (c.getX() == null) throw new ValidationException("coordinates.x не может быть null");
        if (c.getY() == null) throw new ValidationException("coordinates.y не может быть null");
    }

    public static void validateCreationDate(Date d) throws ValidationException {
        if (d == null) throw new ValidationException("creationDate не может быть null");
    }

    public static void validateHouse(House h) throws ValidationException {
        if (h == null) throw new ValidationException("house не может быть null");
        if (h.getYear() <= 0) throw new ValidationException("house.year должно быть > 0");
        if (h.getNumberOfLifts() <= 0) throw new ValidationException("house.numberOfLifts должно быть > 0");
    }

    public static <T> T requireNonNull(T v, String field) throws ValidationException {
        if (v == null) throw new ValidationException(field + " не может быть null");
        return v;
    }

    public static String normalizeNullableString(String s) {
        if (s == null) return null;
        if (s.isEmpty()) return null;
        return s;
    }
}
