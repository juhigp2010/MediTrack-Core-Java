package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.enums.AppointmentStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CSVUtil {

    public static void saveDoctors(String filePath, List<Doctor> doctors) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Doctor doc : doctors) {
                writer.write(doc.toCSV());
                writer.newLine();
            }
        }
    }

    public static List<Doctor> loadDoctors(String filePath) throws IOException {
        List<Doctor> doctors = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return doctors;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        doctors.add(Doctor.fromCSV(line));
                    } catch (Exception e) {
                        System.err.println("Skipping malformed doctor CSV line: " + line);
                    }
                }
            }
        }
        return doctors;
    }

    public static void savePatients(String filePath, List<Patient> patients) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Patient pat : patients) {
                writer.write(pat.toCSV());
                writer.newLine();
            }
        }
    }

    public static List<Patient> loadPatients(String filePath) throws IOException {
        List<Patient> patients = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return patients;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        patients.add(Patient.fromCSV(line));
                    } catch (Exception e) {
                        System.err.println("Skipping malformed patient CSV line: " + line);
                    }
                }
            }
        }
        return patients;
    }

    public static void saveAppointments(String filePath, List<Appointment> appointments) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Appointment app : appointments) {
                writer.write(app.toCSV());
                writer.newLine();
            }
        }
    }

    public static List<Appointment> loadAppointments(String filePath, Map<String, Doctor> doctorMap, Map<String, Patient> patientMap) throws IOException {
        List<Appointment> appointments = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return appointments;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        String[] parts = line.split(",");
                        String appValId = parts[0];
                        String date = parts[1];
                        String docId = parts[2];
                        String patId = parts[3];
                        AppointmentStatus status = AppointmentStatus.valueOf(parts[4]);

                        Doctor doc = doctorMap.get(docId);
                        Patient pat = patientMap.get(patId);
                        if (doc != null && pat != null) {
                            appointments.add(new Appointment(appValId, date, doc, pat, status));
                        } else {
                            System.err.println("Could not resolve reference doctor/patient for appointment line: " + line);
                        }
                    } catch (Exception e) {
                        System.err.println("Skipping malformed appointment CSV line: " + line);
                    }
                }
            }
        }
        return appointments;
    }
}
