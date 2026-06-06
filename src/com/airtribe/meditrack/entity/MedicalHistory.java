package com.airtribe.meditrack.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicalHistory implements Cloneable, Serializable {
    private List<String> conditions;
    private List<String> medications;

    public MedicalHistory() {
        this.conditions = new ArrayList<>();
        this.medications = new ArrayList<>();
    }

    public MedicalHistory(List<String> conditions, List<String> medications) {
        this.conditions = new ArrayList<>(conditions);
        this.medications = new ArrayList<>(medications);
    }

    public List<String> getConditions() {
        return conditions;
    }

    public void addCondition(String condition) {
        this.conditions.add(condition);
    }

    public List<String> getMedications() {
        return medications;
    }

    public void addMedication(String medication) {
        this.medications.add(medication);
    }

    @Override
    public MedicalHistory clone() {
        try {
            MedicalHistory cloned = (MedicalHistory) super.clone();
            cloned.conditions = new ArrayList<>(this.conditions);
            cloned.medications = new ArrayList<>(this.medications);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "Conditions: " + conditions + " | Medications: " + medications;
    }
}
