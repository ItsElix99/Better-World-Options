package com.itselix99.betterworldoptions.config;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LinkedProperties extends Properties {
    private final List<Object> keys = new ArrayList<>();

    @Override
    public synchronized Object put(Object key, Object value) {
        if (!keys.contains(key)) {
            keys.add(key);
        }
        return super.put(key, value);
    }

    @Override
    public @NotNull Set<Object> keySet() {
        return new LinkedHashSet<>(keys);
    }

    @Override
    public synchronized Enumeration<Object> keys() {
        return Collections.enumeration(keys);
    }
}