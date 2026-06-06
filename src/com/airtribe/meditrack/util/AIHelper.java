package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.enums.Specialization;

import java.util.*;

public class AIHelper {
    private static final Map<String, Specialization> SYMPTOM_MAP = new HashMap<>();

    static {
        SYMPTOM_MAP.put("chest pain", Specialization.CARDIOLOGY);
        SYMPTOM_MAP.put("heart", Specialization.CARDIOLOGY);
        SYMPTOM_MAP.put("palpitation", Specialization.CARDIOLOGY);
        
        SYMPTOM_MAP.put("skin", Specialization.DERMATOLOGY);
        SYMPTOM_MAP.put("rash", Specialization.DERMATOLOGY);
        SYMPTOM_MAP.put("acne", Specialization.DERMATOLOGY);
        SYMPTOM_MAP.put("itching", Specialization.DERMATOLOGY);
        
        SYMPTOM_MAP.put("child", Specialization.PEDIATRICS);
        SYMPTOM_MAP.put("kid", Specialization.PEDIATRICS);
        SYMPTOM_MAP.put("pediatric", Specialization.PEDIATRICS);
        SYMPTOM_MAP.put("vaccine", Specialization.PEDIATRICS);
        
        SYMPTOM_MAP.put("bone", Specialization.ORTHOPEDICS);
        SYMPTOM_MAP.put("fracture", Specialization.ORTHOPEDICS);
        SYMPTOM_MAP.put("joint pain", Specialization.ORTHOPEDICS);
        SYMPTOM_MAP.put("back pain", Specialization.ORTHOPEDICS);
        
        SYMPTOM_MAP.put("fever", Specialization.GENERAL_MEDICINE);
        SYMPTOM_MAP.put("cough", Specialization.GENERAL_MEDICINE);
        SYMPTOM_MAP.put("cold", Specialization.GENERAL_MEDICINE);
        SYMPTOM_MAP.put("headache", Specialization.GENERAL_MEDICINE);
    }

    public static Specialization recommendSpecialization(String symptom) {
        if (symptom == null) {
            return Specialization.GENERAL_MEDICINE;
        }
        String lowerSymptom = symptom.toLowerCase().trim();
        for (Map.Entry<String, Specialization> entry : SYMPTOM_MAP.entrySet()) {
            if (lowerSymptom.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return Specialization.GENERAL_MEDICINE;
    }

    public static List<String> suggestAvailableSlots(Doctor doctor, String dateOnly, List<Appointment> appointments) {
        String[] standardSlots = {"09:00", "10:00", "11:00", "13:00", "14:00", "15:00", "16:00"};
        List<String> availableSlots = new ArrayList<>();
        
        Set<String> bookedSlots = new HashSet<>();
        for (Appointment app : appointments) {
            if (app.getDoctor().getId().equalsIgnoreCase(doctor.getId()) && app.getDate().startsWith(dateOnly)) {
                String appDate = app.getDate();
                if (appDate.length() >= 16) {
                    String timePart = appDate.substring(11, 16);
                    bookedSlots.add(timePart);
                }
            }
        }

        for (String slot : standardSlots) {
            if (!bookedSlots.contains(slot)) {
                availableSlots.add(slot);
            }
        }
        return availableSlots;
    }
}
