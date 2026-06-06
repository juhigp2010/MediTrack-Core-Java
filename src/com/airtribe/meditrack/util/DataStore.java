package com.airtribe.meditrack.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataStore<T> {
    private final List<T> records = Collections.synchronizedList(new ArrayList<>());

    public void add(T record) {
        records.add(record);
    }

    public List<T> getAll() {
        synchronized (records) {
            return new ArrayList<>(records);
        }
    }

    public void clear() {
        records.clear();
    }
}
