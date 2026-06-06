package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.enums.Specialization;
import com.airtribe.meditrack.interfaces.Searchable;
import com.airtribe.meditrack.util.Validator;

public class Doctor extends Person implements Searchable {
    private Specialization specialization;
    private double consultationFee;

    public Doctor(String id, String name, int age, Specialization specialization, double consultationFee) {
        super(id, name, age);
        Validator.validateFee(consultationFee);
        this.specialization = specialization;
        this.consultationFee = consultationFee;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization spec) {
        this.specialization = spec;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double fee) {
        Validator.validateFee(fee);
        this.consultationFee = fee;
    }

    @Override
    public void displayDetails() {
        System.out.println("Doctor [ID: " + getId() + ", Name: " + getName() +
                ", Age: " + getAge() + ", Specialization: " + specialization +
                ", Fee: $" + consultationFee + "]");
    }

    @Override
    public boolean matchesId(String id) {
        return getId().equalsIgnoreCase(id);
    }

    @Override
    public boolean matchesName(String name) {
        return getName().equalsIgnoreCase(name);
    }

    public String toCSV() {
        return getId() + "," + getName() + "," + getAge() + "," + specialization.name() + "," + consultationFee;
    }

    public static Doctor fromCSV(String csv) {
        String[] parts = csv.split(",");
        return new Doctor(parts[0], parts[1], Integer.parseInt(parts[2]), Specialization.valueOf(parts[3]), Double.parseDouble(parts[4]));
    }
}
