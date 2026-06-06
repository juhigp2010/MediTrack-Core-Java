package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.util.DataStore;
import java.util.List;
import java.util.stream.Collectors;

public class PatientService {
    private final DataStore<Patient> patientStore = new DataStore<>();

    public void addPatient(Patient patient) {
        patientStore.add(patient);
    }

    public List<Patient> getAllPatients() {
        return patientStore.getAll();
    }

    public boolean deletePatient(String id) {
        List<Patient> list = patientStore.getAll();
        Patient target = null;
        for (Patient p : list) {
            if (p.getId().equalsIgnoreCase(id)) {
                target = p;
                break;
            }
        }
        if (target != null) {
            patientStore.clear();
            for (Patient p : list) {
                if (!p.getId().equalsIgnoreCase(id)) {
                    patientStore.add(p);
                }
            }
            return true;
        }
        return false;
    }

    public boolean updatePatient(String id, String name, int age, String ailment) {
        Patient p = searchPatient(id);
        if (p != null) {
            p.setName(name);
            p.setAge(age);
            p.setAilment(ailment);
            return true;
        }
        return false;
    }

    // Overloaded search method 1: Search by ID (returns single Patient or null)
    public Patient searchPatient(String id) {
        return patientStore.getAll().stream()
                .filter(p -> p.matchesId(id))
                .findFirst()
                .orElse(null);
    }

    // Overloaded search method 2: Search by Age (returns List of Patients)
    public List<Patient> searchPatient(int age) {
        return patientStore.getAll().stream()
                .filter(p -> p.getAge() == age)
                .collect(Collectors.toList());
    }

    // Overloaded search method 3: Search by Name (using boolean flag to disambiguate the String signature)
    public List<Patient> searchPatient(String name, boolean searchByName) {
        if (searchByName) {
            return patientStore.getAll().stream()
                    .filter(p -> p.matchesName(name))
                    .collect(Collectors.toList());
        } else {
            Patient p = searchPatient(name);
            return p != null ? List.of(p) : List.of();
        }
    }
}
