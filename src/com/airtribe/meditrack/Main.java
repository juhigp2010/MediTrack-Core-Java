package com.airtribe.meditrack;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.enums.AppointmentStatus;
import com.airtribe.meditrack.enums.Specialization;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.interfaces.BillingStrategy;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.ConsoleNotificationService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final PatientService patientService = new PatientService();
    private static final DoctorService doctorService = new DoctorService();
    private static final AppointmentService appointmentService = new AppointmentService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Register console notifier observer
        appointmentService.registerObserver(new ConsoleNotificationService());

        System.out.println("=============================================================");
        System.out.println("        Welcome to MediTrack Healthcare Management           ");
        System.out.println("=============================================================");

        // Handle loadData CLI arg
        for (String arg : args) {
            if (arg.equalsIgnoreCase("--loadData")) {
                System.out.println("\n[BOOT] '--loadData' argument detected. Preloading database...");
                loadDatabase();
            }
        }

        boolean exit = false;
        while (!exit) {
            printMainMenu();
            int choice = readIntInput("Select option: ");
            switch (choice) {
                case 1:
                    manageDoctors();
                    break;
                case 2:
                    managePatients();
                    break;
                case 3:
                    manageAppointments();
                    break;
                case 4:
                    manageBilling();
                    break;
                case 5:
                    runAnalytics();
                    break;
                case 6:
                    demoDeepCopy();
                    break;
                case 7:
                    saveDatabase();
                    break;
                case 8:
                    exit = true;
                    System.out.println("\nThank you for using MediTrack. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please choose between 1 and 8.");
            }
        }
        appointmentService.stopBackgroundMonitor();
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n----------------- MAIN MENU -----------------");
        System.out.println("1. Manage Doctors (CRUD & Search)");
        System.out.println("2. Manage Patients (CRUD, Search, & History)");
        System.out.println("3. Schedule & Manage Appointments (AI Slots)");
        System.out.println("4. Create & Pay Bills (Factory & Strategy)");
        System.out.println("5. Analytics & Dashboard Reports (Streams)");
        System.out.println("6. Demonstrate Deep vs Shallow Copying");
        System.out.println("7. Save Database to CSV Files");
        System.out.println("8. Exit Application");
        System.out.println("---------------------------------------------");
    }

    private static int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private static double readDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid monetary amount.");
            }
        }
    }

    private static void manageDoctors() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- DOCTOR MANAGEMENT ---");
            System.out.println("1. Register Doctor");
            System.out.println("2. View All Doctors");
            System.out.println("3. Search Doctor");
            System.out.println("4. Delete Doctor");
            System.out.println("5. Back to Main Menu");
            int choice = readIntInput("Select option: ");
            switch (choice) {
                case 1:
                    registerDoctor();
                    break;
                case 2:
                    viewAllDoctors();
                    break;
                case 3:
                    searchDoctor();
                    break;
                case 4:
                    deleteDoctor();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void registerDoctor() {
        System.out.println("\n--- Register Doctor ---");
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        int age = readIntInput("Enter Age: ");
        
        System.out.println("Select Specialization:");
        Specialization[] specs = Specialization.values();
        for (int i = 0; i < specs.length; i++) {
            System.out.println((i + 1) + ". " + specs[i]);
        }
        int specChoice = readIntInput("Choose spec index: ");
        if (specChoice < 1 || specChoice > specs.length) {
            System.out.println("Invalid choice. Aborting registration.");
            return;
        }
        Specialization specialization = specs[specChoice - 1];
        double fee = readDoubleInput("Enter Consultation Fee: ");

        try {
            String id = IdGenerator.getInstance().nextDoctorId();
            Doctor doctor = new Doctor(id, name, age, specialization, fee);
            doctorService.addDoctor(doctor);
            System.out.println("Doctor registered successfully! ID: " + id);
        } catch (InvalidDataException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
    }

    private static void viewAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors registered yet.");
        } else {
            System.out.println("\nRegistered Doctors:");
            for (Doctor d : doctors) {
                d.displayDetails();
            }
        }
    }

    private static void searchDoctor() {
        System.out.print("\nEnter ID or Name: ");
        String query = scanner.nextLine().trim();
        Doctor d = doctorService.searchDoctor(query);
        if (d != null) {
            System.out.println("Found Doctor:");
            d.displayDetails();
            return;
        }
        List<Doctor> list = doctorService.searchDoctorByName(query);
        if (!list.isEmpty()) {
            System.out.println("Found Doctors matching name:");
            for (Doctor doc : list) {
                doc.displayDetails();
            }
        } else {
            System.out.println("No doctor found matching query: " + query);
        }
    }

    private static void deleteDoctor() {
        System.out.print("\nEnter Doctor ID to delete: ");
        String id = scanner.nextLine().trim();
        if (doctorService.deleteDoctor(id)) {
            System.out.println("Doctor ID " + id + " deleted successfully.");
        } else {
            System.out.println("Doctor not found.");
        }
    }

    private static void managePatients() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- PATIENT MANAGEMENT ---");
            System.out.println("1. Register Patient");
            System.out.println("2. View All Patients");
            System.out.println("3. Search Patient");
            System.out.println("4. Update Patient Medical History");
            System.out.println("5. Delete Patient");
            System.out.println("6. Back to Main Menu");
            int choice = readIntInput("Select option: ");
            switch (choice) {
                case 1:
                    registerPatient();
                    break;
                case 2:
                    viewAllPatients();
                    break;
                case 3:
                    searchPatient();
                    break;
                case 4:
                    updateMedicalHistory();
                    break;
                case 5:
                    deletePatient();
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void registerPatient() {
        System.out.println("\n--- Register Patient ---");
        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();
        int age = readIntInput("Enter Age: ");
        System.out.print("Enter Primary Ailment: ");
        String ailment = scanner.nextLine().trim();

        try {
            String id = IdGenerator.getInstance().nextPatientId();
            Patient patient = new Patient(id, name, age, ailment);
            patientService.addPatient(patient);
            System.out.println("Patient registered successfully! ID: " + id);
        } catch (InvalidDataException e) {
            System.out.println("Validation Error: " + e.getMessage());
        }
    }

    private static void viewAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients registered yet.");
        } else {
            System.out.println("\nRegistered Patients:");
            for (Patient p : patients) {
                p.displayDetails();
            }
        }
    }

    private static void searchPatient() {
        System.out.println("\nSearch by: 1. ID | 2. Name | 3. Age");
        int subChoice = readIntInput("Choose search method: ");
        if (subChoice == 1) {
            System.out.print("Enter Patient ID: ");
            String id = scanner.nextLine().trim();
            Patient p = patientService.searchPatient(id);
            if (p != null) {
                p.displayDetails();
            } else {
                System.out.println("No patient found with ID: " + id);
            }
        } else if (subChoice == 2) {
            System.out.print("Enter Patient Name: ");
            String name = scanner.nextLine().trim();
            List<Patient> results = patientService.searchPatient(name, true);
            if (results.isEmpty()) {
                System.out.println("No patient found with Name: " + name);
            } else {
                results.forEach(Patient::displayDetails);
            }
        } else if (subChoice == 3) {
            int age = readIntInput("Enter Age: ");
            List<Patient> results = patientService.searchPatient(age);
            if (results.isEmpty()) {
                System.out.println("No patient found with Age: " + age);
            } else {
                results.forEach(Patient::displayDetails);
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void updateMedicalHistory() {
        System.out.print("\nEnter Patient ID to update history: ");
        String id = scanner.nextLine().trim();
        Patient patient = patientService.searchPatient(id);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.println("\n1. Add Condition | 2. Add Medication");
        int action = readIntInput("Choose action: ");
        if (action == 1) {
            System.out.print("Enter condition (e.g. Asthma, Diabetes): ");
            String condition = scanner.nextLine().trim();
            patient.getMedicalHistory().addCondition(condition);
            System.out.println("Condition added!");
        } else if (action == 2) {
            System.out.print("Enter medication (e.g. Inhaler, Insulin): ");
            String medication = scanner.nextLine().trim();
            patient.getMedicalHistory().addMedication(medication);
            System.out.println("Medication added!");
        } else {
            System.out.println("Invalid selection.");
        }
    }

    private static void deletePatient() {
        System.out.print("\nEnter Patient ID to delete: ");
        String id = scanner.nextLine().trim();
        if (patientService.deletePatient(id)) {
            System.out.println("Patient ID " + id + " deleted successfully.");
        } else {
            System.out.println("Patient not found.");
        }
    }

    private static void manageAppointments() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- APPOINTMENT MANAGEMENT ---");
            System.out.println("1. Schedule Appointment (Manual)");
            System.out.println("2. AI Slot Suggester (Symptom Recommendation)");
            System.out.println("3. View All Appointments");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. Back to Main Menu");
            int choice = readIntInput("Select option: ");
            switch (choice) {
                case 1:
                    scheduleManualAppointment();
                    break;
                case 2:
                    scheduleAIAppointment();
                    break;
                case 3:
                    viewAllAppointments();
                    break;
                case 4:
                    cancelAppointment();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void scheduleManualAppointment() {
        System.out.print("Enter Patient ID: ");
        String patId = scanner.nextLine().trim();
        Patient patient = patientService.searchPatient(patId);
        if (patient == null) {
            System.out.println("Patient not found. Register the patient first.");
            return;
        }

        System.out.print("Enter Doctor ID: ");
        String docId = scanner.nextLine().trim();
        Doctor doctor = doctorService.searchDoctor(docId);
        if (doctor == null) {
            System.out.println("Doctor not found.");
            return;
        }

        System.out.print("Enter Appointment Date & Time (yyyy-MM-dd HH:mm): ");
        String datetime = scanner.nextLine().trim();

        try {
            Validator.validateDate(datetime);
            Appointment app = appointmentService.scheduleAppointment(datetime, doctor, patient);
            System.out.println("Appointment Scheduled successfully! ID: " + app.getAppointmentId());
        } catch (Exception e) {
            System.out.println("Scheduling Error: " + e.getMessage());
        }
    }

    private static void scheduleAIAppointment() {
        System.out.print("Enter Patient ID: ");
        String patId = scanner.nextLine().trim();
        Patient patient = patientService.searchPatient(patId);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.print("Describe Symptoms (e.g. chest pain, bone fracture, rash, fever): ");
        String symptoms = scanner.nextLine().trim();
        Specialization recommended = AIHelper.recommendSpecialization(symptoms);
        System.out.println("\n>>> [AI RECOMMENDATION] Recommended Specialty: " + recommended);

        List<Doctor> matches = doctorService.filterDoctorsBySpecialization(recommended);
        if (matches.isEmpty()) {
            System.out.println("No doctors currently available in the " + recommended + " specialty.");
            return;
        }

        System.out.println("Available Doctors:");
        for (int i = 0; i < matches.size(); i++) {
            System.out.println((i + 1) + ". Dr. " + matches.get(i).getName() + " | Fee: $" + matches.get(i).getConsultationFee());
        }
        int docIndex = readIntInput("Choose doctor index: ") - 1;
        if (docIndex < 0 || docIndex >= matches.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        Doctor doctor = matches.get(docIndex);

        System.out.print("Enter Date (yyyy-MM-dd): ");
        String dateOnly = scanner.nextLine().trim();
        
        List<String> suggestedSlots = AIHelper.suggestAvailableSlots(doctor, dateOnly, appointmentService.getAllAppointments());
        if (suggestedSlots.isEmpty()) {
            System.out.println("No available slots found for Dr. " + doctor.getName() + " on " + dateOnly);
            return;
        }

        System.out.println("Available Slots:");
        for (int i = 0; i < suggestedSlots.size(); i++) {
            System.out.println((i + 1) + ". " + suggestedSlots.get(i));
        }
        int slotIndex = readIntInput("Choose slot index: ") - 1;
        if (slotIndex < 0 || slotIndex >= suggestedSlots.size()) {
            System.out.println("Invalid slot index.");
            return;
        }
        String slotTime = suggestedSlots.get(slotIndex);
        String fullDatetime = dateOnly + " " + slotTime;

        try {
            Appointment app = appointmentService.scheduleAppointment(fullDatetime, doctor, patient);
            System.out.println("AI Recommended Appointment Scheduled! ID: " + app.getAppointmentId());
        } catch (Exception e) {
            System.out.println("Error scheduling: " + e.getMessage());
        }
    }

    private static void viewAllAppointments() {
        List<Appointment> list = appointmentService.getAllAppointments();
        if (list.isEmpty()) {
            System.out.println("No appointments scheduled.");
        } else {
            System.out.println("\nScheduled Appointments:");
            list.forEach(Appointment::displayDetails);
        }
    }

    private static void cancelAppointment() {
        System.out.print("\nEnter Appointment ID to cancel: ");
        String appId = scanner.nextLine().trim();
        try {
            appointmentService.cancelAppointment(appId);
            System.out.println("Appointment cancelled successfully.");
        } catch (AppointmentNotFoundException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void manageBilling() {
        System.out.println("\n--- CREATE INVOICE & BILLING ---");
        System.out.print("Enter Appointment ID for billing: ");
        String appId = scanner.nextLine().trim();
        
        // Find appointment
        Appointment target = appointmentService.getAllAppointments().stream()
                .filter(app -> app.getAppointmentId().equalsIgnoreCase(appId))
                .findFirst()
                .orElse(null);
        
        if (target == null) {
            System.out.println("Appointment ID not found.");
            return;
        }

        System.out.println("Select Bill Type: 1. Consultation | 2. Procedure | 3. Pharmacy");
        int typeChoice = readIntInput("Choice: ");
        String type = "DEFAULT";
        if (typeChoice == 1) type = "CONSULTATION";
        else if (typeChoice == 2) type = "PROCEDURE";
        else if (typeChoice == 3) type = "PHARMACY";

        double baseAmount = readDoubleInput("Enter base charge amount: ");

        System.out.println("Select Billing Strategy: 1. Standard (No discount) | 2. Discounted (10% off) | 3. Premium (20% Emergency surcharge)");
        int strategyChoice = readIntInput("Choice: ");
        BillingStrategy strategy;
        if (strategyChoice == 2) {
            strategy = new DiscountBilling(0.10);
        } else if (strategyChoice == 3) {
            strategy = new PremiumBilling(0.20);
        } else {
            strategy = new StandardBilling();
        }

        String billId = IdGenerator.getInstance().nextBillId();
        // Factory pattern: Refined factory instantiation
        Bill bill = BillFactory.createBill(type, target, baseAmount, billId, strategy);

        System.out.println("\nCalculated Invoice Details:");
        System.out.println("Bill ID: " + bill.getBillId());
        System.out.println("Bill Subclass: " + bill.getClass().getSimpleName());
        System.out.println("Patient Name: " + target.getPatient().getName());
        System.out.println("Base Amount: $" + String.format("%.2f", bill.getBaseAmount()));
        System.out.println("Tax Rate Applied: " + (Constants.TAX_RATE * 100) + "%");
        System.out.println("Final Due: $" + String.format("%.2f", bill.calculateFinalAmount()));

        System.out.print("\nProcess payment now? (y/n): ");
        String payNow = scanner.nextLine().trim();
        if (payNow.equalsIgnoreCase("y")) {
            bill.processPayment();
            // Generate immutable bill summary
            BillSummary summary = new BillSummary(bill.getBillId(), target.getPatient().getName(), bill.calculateFinalAmount());
            System.out.println("Generated Immutable Bill Summary: [ID: " + summary.getBillId() + " | Patient: " + summary.getPatientName() + " | Paid: $" + String.format("%.2f", summary.getTotalPaid()) + "]");
        } else {
            System.out.println("Invoice saved. Pending payment.");
        }
    }

    private static void runAnalytics() {
        System.out.println("\n================= STREAM ANALYTICS REPORT =================");
        
        // 1. Average fee
        double avgFee = doctorService.calculateAverageFee();
        System.out.printf("Average Consultation Fee across all Doctors: $%.2f\n", avgFee);

        // 2. Filter by spec
        System.out.println("\nFilter Doctors by Specialization:");
        Specialization[] specs = Specialization.values();
        for (int i = 0; i < specs.length; i++) {
            System.out.println((i + 1) + ". " + specs[i]);
        }
        int specChoice = readIntInput("Choose spec index: ");
        if (specChoice >= 1 && specChoice <= specs.length) {
            Specialization targetSpec = specs[specChoice - 1];
            List<Doctor> filtered = doctorService.filterDoctorsBySpecialization(targetSpec);
            System.out.println("Doctors specializing in " + targetSpec + ":");
            if (filtered.isEmpty()) {
                System.out.println("  None found.");
            } else {
                filtered.forEach(d -> System.out.println("  - Dr. " + d.getName() + " | Fee: $" + d.getConsultationFee()));
            }
        }

        // 3. Appointments per Doctor
        System.out.println("\nAppointments Count per Doctor (Stream Analytics):");
        Map<String, Long> appMap = appointmentService.getAppointmentsCountPerDoctor();
        if (appMap.isEmpty()) {
            System.out.println("  No appointment logs found.");
        } else {
            appMap.forEach((docName, count) -> System.out.println("  - Dr. " + docName + ": " + count + " appointments"));
        }
        System.out.println("===========================================================");
    }

    private static void demoDeepCopy() {
        System.out.println("\n=== DEMONSTRATING DEEP COPY VS SHALLOW COPY ===");
        
        Patient original = new Patient("PAT-DEMO", "Original Patient", 30, "Allergies");
        original.getMedicalHistory().addCondition("Penicillin Allergy");
        original.getMedicalHistory().addMedication("Antihistamine");

        System.out.println("1. Original Patient Details:");
        original.displayDetails();

        System.out.println("\nCloning patient object using deep copy clone()...");
        Patient cloned = original.clone();
        
        System.out.println("2. Cloned Patient Details (Initially identical):");
        cloned.displayDetails();

        System.out.println("\nModifying cloned Patient's allergies (Adding 'Peanut Allergy' and 'EpiPen')...");
        cloned.getMedicalHistory().addCondition("Peanut Allergy");
        cloned.getMedicalHistory().addMedication("EpiPen");
        cloned.setName("Cloned Patient (Modified)");

        System.out.println("\nChecking both instances post-modification:");
        System.out.println("--> Original Patient History: " + original.getMedicalHistory());
        System.out.println("--> Cloned Patient History:   " + cloned.getMedicalHistory());

        if (original.getMedicalHistory().getConditions().size() != cloned.getMedicalHistory().getConditions().size()) {
            System.out.println("\n[SUCCESS] Original history is unaffected! This proves that a deep copy occurred, duplicating internal collections instead of referencing the same list.");
        } else {
            System.out.println("\n[FAILURE] Original history was modified! This indicates a shallow copy leakage occurred.");
        }
    }

    private static void loadDatabase() {
        try {
            List<Doctor> loadedDocs = CSVUtil.loadDoctors(Constants.DOCTOR_FILE);
            for (Doctor doc : loadedDocs) {
                doctorService.addDoctor(doc);
            }
            
            List<Patient> loadedPats = CSVUtil.loadPatients(Constants.PATIENT_FILE);
            for (Patient pat : loadedPats) {
                patientService.addPatient(pat);
            }

            Map<String, Doctor> docMap = doctorService.getAllDoctors().stream()
                    .collect(Collectors.toMap(Doctor::getId, doc -> doc));
            Map<String, Patient> patMap = patientService.getAllPatients().stream()
                    .collect(Collectors.toMap(Patient::getId, pat -> pat));

            List<Appointment> loadedApps = CSVUtil.loadAppointments(Constants.APPOINTMENT_FILE, docMap, patMap);
            for (Appointment app : loadedApps) {
                appointmentService.addRawAppointment(app);
            }

            System.out.println("\n>>> CSV database loaded successfully!");
            System.out.println("    Doctors: " + loadedDocs.size());
            System.out.println("    Patients: " + loadedPats.size());
            System.out.println("    Appointments: " + loadedApps.size());
        } catch (IOException e) {
            System.err.println("Failed to load CSV database: " + e.getMessage());
        }
    }

    private static void saveDatabase() {
        try {
            CSVUtil.saveDoctors(Constants.DOCTOR_FILE, doctorService.getAllDoctors());
            CSVUtil.savePatients(Constants.PATIENT_FILE, patientService.getAllPatients());
            CSVUtil.saveAppointments(Constants.APPOINTMENT_FILE, appointmentService.getAllAppointments());
            System.out.println("\n>>> Database saved to CSV files successfully!");
        } catch (IOException e) {
            System.err.println("Failed to save CSV database: " + e.getMessage());
        }
    }
}
