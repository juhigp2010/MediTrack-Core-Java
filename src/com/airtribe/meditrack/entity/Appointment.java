package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.enums.AppointmentStatus;
import java.io.Serializable;

public class Appointment implements Serializable, Cloneable {
    private final String appointmentId;
    private Patient patient;
    private Doctor doctor;
    private String date; // Format: yyyy-MM-dd HH:mm
    private AppointmentStatus status;

    public Appointment(String appointmentId, String date, Doctor doctor, Patient patient, AppointmentStatus status) {
        this.appointmentId = appointmentId;
        this.date = date;
        this.doctor = doctor;
        this.patient = patient;
        this.status = status;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    @Override
    public Appointment clone() {
        try {
            Appointment cloned = (Appointment) super.clone();
            // Deep copy the nested Patient object
            if (this.patient != null) {
                cloned.patient = this.patient.clone();
            }
            // Doctor is a shared entity, so a shallow copy of its reference is correct
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public void displayDetails() {
        System.out.println("Appointment ID: " + appointmentId + " | Patient: " + patient.getName() +
                " (ID: " + patient.getId() + ") | Doctor: Dr. " + doctor.getName() + 
                " | Date: " + date + " | Status: " + status);
    }

    public String toCSV() {
        return appointmentId + "," + date + "," + doctor.getId() + "," + patient.getId() + "," + status.name();
    }
}
