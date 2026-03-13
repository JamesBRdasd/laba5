package ru.itmo.lab5.app.core;

import ru.itmo.lab5.app.exception.CommandException;
import ru.itmo.lab5.app.exception.ValidationException;
import ru.itmo.lab5.model.*;
import ru.itmo.lab5.util.IdGenerator;
import ru.itmo.lab5.util.Validators;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Обёртка над коллекцией {@link Hashtable} и операции над ней.
 */
public final class CollectionManager {

    private final Hashtable<Long, Flat> table;
    private final Date initDate = new Date();
    private final IdGenerator idGenerator = new IdGenerator();

    public CollectionManager(Hashtable<Long, Flat> loaded) {
        this.table = loaded == null ? new Hashtable<>() : loaded;
        // зарегистрируем id из загруженных элементов
        for (Flat f : table.values()) {
            if (f != null && f.getId() != null) {
                idGenerator.register(f.getId());
            }
        }
    }

    public Date getInitDate() { return initDate; }
    public Hashtable<Long, Flat> getTable() { return table; }
    public int size() { return table.size(); }

    public List<Flat> sortedValues() {
        ArrayList<Flat> list = new ArrayList<>(table.values());
        list.sort(Comparator.naturalOrder());
        return list;
    }

    public void insert(Long key, Flat flat) throws ValidationException {
        if (key == null) throw new ValidationException("Ключ не может быть null");
        if (flat == null) throw new ValidationException("Flat не может быть null");

        long id = idGenerator.next();
        flat.setId(id);
        flat.setCreationDate(new Date());
        Validators.validateFlat(flat);

        table.put(key, flat);
    }

    public boolean removeKey(Long key) {
        return table.remove(key) != null;
    }

    public void clear() { table.clear(); }

    public boolean updateById(long id, Flat newData) throws ValidationException {
        Map.Entry<Long, Flat> entry = findById(id);
        if (entry == null) return false;

        Flat old = entry.getValue();
        newData.setId(old.getId());
        newData.setCreationDate(old.getCreationDate());

        Validators.validateFlat(newData);
        table.put(entry.getKey(), newData);
        return true;
    }

    public int removeGreater(Flat pivot) throws ValidationException {
        Validators.validateFlat(pivot);
        int before = table.size();
        table.entrySet().removeIf(e -> e.getValue() != null && e.getValue().compareTo(pivot) > 0);
        return before - table.size();
    }

    public boolean replaceIfGreater(Long key, Flat candidate) throws ValidationException {
        if (!table.containsKey(key)) return false;
        Flat current = table.get(key);
        if (current == null) return false;

        candidate.setId(current.getId());
        candidate.setCreationDate(current.getCreationDate());
        Validators.validateFlat(candidate);

        if (candidate.compareTo(current) > 0) {
            table.put(key, candidate);
            return true;
        }
        return false;
    }

    public boolean replaceIfLower(Long key, Flat candidate) throws ValidationException {
        if (!table.containsKey(key)) return false;
        Flat current = table.get(key);
        if (current == null) return false;

        candidate.setId(current.getId());
        candidate.setCreationDate(current.getCreationDate());
        Validators.validateFlat(candidate);

        if (candidate.compareTo(current) < 0) {
            table.put(key, candidate);
            return true;
        }
        return false;
    }

    public long countGreaterThanView(View view) {
        return table.values().stream()
                .filter(Objects::nonNull)
                .filter(f -> f.getView().compareTo(view) > 0)
                .count();
    }

    public List<Flat> filterLessThanView(View view) {
        return table.values().stream()
                .filter(Objects::nonNull)
                .filter(f -> f.getView().compareTo(view) < 0)
                .sorted()
                .collect(Collectors.toList());
    }

    public Set<Furnish> uniqueFurnish() {
        return table.values().stream()
                .filter(Objects::nonNull)
                .map(Flat::getFurnish)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private Map.Entry<Long, Flat> findById(long id) {
        for (Map.Entry<Long, Flat> e : table.entrySet()) {
            Flat f = e.getValue();
            if (f != null && f.getId() != null && f.getId() == id) {
                return e;
            }
        }
        return null;
    }
}
