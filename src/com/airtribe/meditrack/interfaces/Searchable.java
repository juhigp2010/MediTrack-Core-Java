package com.airtribe.meditrack.interfaces;

public interface Searchable {
    boolean matchesId(String id);
    boolean matchesName(String name);
}
