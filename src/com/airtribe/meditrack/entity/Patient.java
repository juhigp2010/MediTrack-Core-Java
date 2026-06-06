package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interfaces.Searchable;
import com.airtribe.meditrack.util.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Patient extends Person implements Searchable, Cloneable {
    private String ailment;
    private MedicalHistory medicalHistory;

    public Patient(String id, String name, int age, String ailment) {
        super(id, name, age);
        Validator.validateAilment(ailment);
        this.ailment = ailment;
        this.medicalHistory = new MedicalHistory();
    }

    public Patient(String id, String name, int age, String ailment, MedicalHistory medicalHistory) {
        super(id, name, age);
        Validator.validateAilment(ailment);
        this.ailment = ailment;
        this.medicalHistory = medicalHistory != null ? medicalHistory : new MedicalHistory();
    }

    public String getAilment() {
        return ailment;
    }

    public void setAilment(String ailment) {
        Validator.validateAilment(ailment);
        this.ailment = ailment;
    }

    public MedicalHistory getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(MedicalHistory medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    @Override
    public void displayDetails() {
        System.out.println("Patient [ID: " + getId() + ", Name: " + getName() +
                ", Age: " + getAge() + ", Ailment: " + ailment + 
                ", Medical History: " + medicalHistory + "]");
    }

    @Override
    public boolean matchesId(String id) {
        return getId().equalsIgnoreCase(id);
    }

    @Override
    public boolean matchesName(String name) {
        return getName().equalsIgnoreCase(name);
    }

    @Override
    public Patient clone() {
        try {
            Patient cloned = (Patient) super.clone();
            // Deep copy the medical history object
            if (this.medicalHistory != null) {
                cloned.medicalHistory = this.medicalHistory.clone();
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public String toCSV() {
        String conditionsJoined = String.join(";", medicalHistory.getConditions());
        String medicationsJoined = String.join(";", medicalHistory.getMedications());
        if (conditionsJoined.isEmpty()) conditionsJoined = "None";
        if (medicationsJoined.isEmpty()) medicationsJoined = "None";
        return getId() + "," + getName() + "," + getAge() + "," + ailment + "," + conditionsJoined + "," + medicationsJoined;
    }

    public static Patient fromCSV(String csv) {
        String[] parts = csv.split(",");
        String id = parts[0];
        String name = parts[1];
        int age = Integer.parseInt(parts[2]);
        String ailment = parts[3];
        
        Patient patient = new Patient(id, name, age, ailment);
        if (parts.length > 4) {
            String conds = parts[4];
            if (!conds.equalsIgnoreCase("None")) {
                for (String c : conds.split(";")) {
                    patient.getMedicalHistory().addCondition(c);
                }
            }
        }
        if (parts.length > 5) {
            String meds = parts[5];
            if (!meds.equalsIgnoreCase("None")) {
                for (String m : meds.split(";")) {
                    patient.getMedicalHistory().addMedication(m);
                }
            }
        }
        return patient;
    }
}
