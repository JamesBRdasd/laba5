package ru.itmo.lab5.model;

/**
 * Дом, в котором находится квартира.
 */
public class House {
    private String name;       // Поле может быть null
    private int year;          // Значение поля должно быть больше 0
    private long numberOfLifts;// Значение поля должно быть больше 0

    public House(String name, int year, long numberOfLifts) {
        this.name = name;
        this.year = year;
        this.numberOfLifts = numberOfLifts;
    }

    public String getName() { return name; }
    public int getYear() { return year; }
    public long getNumberOfLifts() { return numberOfLifts; }

    public void setName(String name) { this.name = name; }
    public void setYear(int year) { this.year = year; }
    public void setNumberOfLifts(long numberOfLifts) { this.numberOfLifts = numberOfLifts; }

    @Override
    public String toString() {
        return "House{name=" + name + ", year=" + year + ", numberOfLifts=" + numberOfLifts + '}';
    }
}
