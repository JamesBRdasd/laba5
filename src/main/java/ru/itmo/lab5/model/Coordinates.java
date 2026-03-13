package ru.itmo.lab5.model;

import java.util.Objects;

/**
 * Координаты объекта.
 */
public class Coordinates {
    private Double x; // Поле не может быть null
    private Long y;   // Поле не может быть null

    public Coordinates(Double x, Long y) {
        this.x = Objects.requireNonNull(x, "x не может быть null");
        this.y = Objects.requireNonNull(y, "y не может быть null");
    }

    public Double getX() { return x; }
    public Long getY() { return y; }

    public void setX(Double x) { this.x = Objects.requireNonNull(x, "x не может быть null"); }
    public void setY(Long y) { this.y = Objects.requireNonNull(y, "y не может быть null"); }

    @Override
    public String toString() {
        return "Coordinates{x=" + x + ", y=" + y + '}';
    }
}
