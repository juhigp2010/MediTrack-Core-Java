package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.enums.Specialization;
import com.airtribe.meditrack.util.DataStore;
import java.util.List;
import java.util.stream.Collectors;

public class DoctorService {
    private final DataStore<Doctor> doctorStore = new DataStore<>();

    public void addDoctor(Doctor doctor) {
        doctorStore.add(doctor);
    }

    public List<Doctor> getAllDoctors() {
        return doctorStore.getAll();
    }

    public boolean deleteDoctor(String id) {
        List<Doctor> list = doctorStore.getAll();
        Doctor target = null;
        for (Doctor d : list) {
            if (d.getId().equalsIgnoreCase(id)) {
                target = d;
                break;
            }
        }
        if (target != null) {
            doctorStore.clear();
            for (Doctor d : list) {
                if (!d.getId().equalsIgnoreCase(id)) {
                    doctorStore.add(d);
                }
            }
            return true;
        }
        return false;
    }

    public boolean updateDoctor(String id, String name, int age, Specialization specialization, double fee) {
        Doctor doc = searchDoctor(id);
        if (doc != null) {
            doc.setName(name);
            doc.setAge(age);
            doc.setSpecialization(specialization);
            doc.setConsultationFee(fee);
            return true;
        }
        return false;
    }

    public Doctor searchDoctor(String id) {
        return doctorStore.getAll().stream()
                .filter(d -> d.matchesId(id))
                .findFirst()
                .orElse(null);
    }

    public List<Doctor> searchDoctorByName(String name) {
        return doctorStore.getAll().stream()
                .filter(d -> d.matchesName(name))
                .collect(Collectors.toList());
    }

    // Java 8 Streams: Filter doctors by specialization
    public List<Doctor> filterDoctorsBySpecialization(Specialization specialization) {
        return doctorStore.getAll().stream()
                .filter(d -> d.getSpecialization() == specialization)
                .collect(Collectors.toList());
    }

    // Java 8 Streams: Compute average consultation fee
    public double calculateAverageFee() {
        return doctorStore.getAll().stream()
                .mapToDouble(Doctor::getConsultationFee)
                .average()
                .orElse(0.0);
    }
}
