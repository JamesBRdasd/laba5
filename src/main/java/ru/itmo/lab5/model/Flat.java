package ru.itmo.lab5.model;

import java.util.Date;
import java.util.Objects;

/**
 * Квартира (элемент коллекции).
 * <p>
 * Реализует сортировку по умолчанию (natural ordering) через {@link Comparable}.
 */
public class Flat implements Comparable<Flat> {

    private Long id;                 // не null, >0, уникально, генерируется автоматически
    private String name;             // не null, не пустая
    private Coordinates coordinates; // не null
    private Date creationDate;       // не null, генерируется автоматически
    private Float area;              // >0 (если задано)
    private Long numberOfRooms;      // не null, >0
    private Furnish furnish;         // может быть null
    private View view;               // не null
    private Transport transport;     // не null
    private House house;             // не null

    /**
     * Полный конструктор. Обычно используется при загрузке из файла.
     */
    public Flat(Long id, String name, Coordinates coordinates, Date creationDate, Float area,
                Long numberOfRooms, Furnish furnish, View view, Transport transport, House house) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.furnish = furnish;
        this.view = view;
        this.transport = transport;
        this.house = house;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Coordinates getCoordinates() { return coordinates; }
    public Date getCreationDate() { return creationDate; }
    public Float getArea() { return area; }
    public Long getNumberOfRooms() { return numberOfRooms; }
    public Furnish getFurnish() { return furnish; }
    public View getView() { return view; }
    public Transport getTransport() { return transport; }
    public House getHouse() { return house; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
    public void setArea(Float area) { this.area = area; }
    public void setNumberOfRooms(Long numberOfRooms) { this.numberOfRooms = numberOfRooms; }
    public void setFurnish(Furnish furnish) { this.furnish = furnish; }
    public void setView(View view) { this.view = view; }
    public void setTransport(Transport transport) { this.transport = transport; }
    public void setHouse(House house) { this.house = house; }

    /**
     * Сравнение по умолчанию:
     * area (null считается меньше) → numberOfRooms → name → id.
     */
    @Override
    public int compareTo(Flat o) {
        if (o == null) return 1;

        int c = compareNullable(area, o.area);
        if (c != 0) return c;

        c = numberOfRooms.compareTo(o.numberOfRooms);
        if (c != 0) return c;

        c = name.compareToIgnoreCase(o.name);
        if (c != 0) return c;

        return id.compareTo(o.id);
    }

    private static <T extends Comparable<T>> int compareNullable(T a, T b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
    }

    @Override
    public String toString() {
        return "Flat{" +
                "id=" + id +
                ", name=\"" + name + "\"" +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", area=" + area +
                ", numberOfRooms=" + numberOfRooms +
                ", furnish=" + furnish +
                ", view=" + view +
                ", transport=" + transport +
                ", house=" + house +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Flat other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
