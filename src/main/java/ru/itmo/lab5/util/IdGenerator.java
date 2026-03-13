package ru.itmo.lab5.util;

import java.util.HashSet;
import java.util.Set;

/**
 * Генератор уникальных id (Long).
 */
public final class IdGenerator {
    private long maxId = 0;
    private final Set<Long> used = new HashSet<>();

    /**
     * Регистрирует уже существующий id (например, при загрузке).
     */
    public void register(long id) {
        used.add(id);
        if (id > maxId) maxId = id;
    }

    /**
     * Создаёт новый уникальный id (>0).
     */
    public long next() {
        long id = maxId + 1;
        while (used.contains(id) || id <= 0) {
            id++;
        }
        maxId = id;
        used.add(id);
        return id;
    }

    public boolean isUsed(long id) {
        return used.contains(id);
    }
}
