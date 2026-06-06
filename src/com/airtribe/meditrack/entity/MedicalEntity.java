package com.airtribe.meditrack.entity;

public abstract class   MedicalEntity {
    private final String id;

    protected MedicalEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract void displayDetails();
}
