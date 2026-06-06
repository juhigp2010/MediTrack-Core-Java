package com.airtribe.meditrack.test;

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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestRunner {
    private static int totalTests = 0;
    private static int passedTests = 0;

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("       RUNNING MEDITRACK AUTOMATED TESTS         ");
        System.out.println("=================================================");

        runTest("Eager/Lazy Singleton Pattern", TestRunner::testSingletonPattern);
        runTest("Input Validation & Exceptions", TestRunner::testValidationAndExceptions);
        runTest("Inheritance and Getters/Setters", TestRunner::testInheritanceAndEncapsulation);
        runTest("Patient Deep Copy Cloning", TestRunner::testPatientCloning);
        runTest("Appointment Deep Copy Cloning", TestRunner::testAppointmentCloning);
        runTest("Immutability of BillSummary", TestRunner::testBillSummaryImmutability);
        runTest("Strategy Design Pattern for Billing", TestRunner::testStrategyPattern);
        runTest("Factory Design Pattern for Bills", TestRunner::testFactoryPattern);
        runTest("Observer Design Pattern for Appointments", TestRunner::testObserverPattern);
        runTest("Java 8 Streams & Lambdas Analytics", TestRunner::testStreamsAndLambdas);
        runTest("CSV Serialization & Persistence File I/O", TestRunner::testCSVFileOperations);

        System.out.println("=================================================");
        System.out.println("TEST SUITE SUMMARY: " + passedTests + "/" + totalTests + " Passed");
        System.out.println("=================================================");
        if (passedTests < totalTests) {
            System.exit(1);
        } else {
            System.out.println("ALL TESTS PASSED SUCCESSFULLY!");
            System.exit(0);
        }
    }

    private static void runTest(String testName, TestRunnable runnable) {
        totalTests++;
        System.out.print("Running " + testName + "... ");
        try {
            runnable.run();
            passedTests++;
            System.out.println("PASS");
        } catch (Throwable e) {
            System.out.println("FAIL");
            e.printStackTrace();
        }
    }

    interface TestRunnable {
        void run() throws Exception;
    }

    private static void testSingletonPattern() {
        IdGenerator eager1 = IdGenerator.getInstance();
        IdGenerator eager2 = IdGenerator.getInstance();
        assertSame(eager1, eager2, "Eager singletons must be the same instance");

        IdGenerator lazy1 = IdGenerator.getLazyInstance();
        IdGenerator lazy2 = IdGenerator.getLazyInstance();
        assertSame(lazy1, lazy2, "Lazy singletons must be the same instance");
    }

    private static void testValidationAndExceptions() {
        assertThrows(InvalidDataException.class, () -> new Patient("PAT999", "Invalid Patient", -5, "Flu"),
                "Should throw InvalidDataException for negative age");

        assertThrows(InvalidDataException.class, () -> new Doctor("DOC999", " ", 35, Specialization.CARDIOLOGY, 150),
                "Should throw InvalidDataException for empty name");

        assertThrows(InvalidDataException.class, () -> new Doctor("DOC999", "Dr. Bob", 35, Specialization.CARDIOLOGY, -50),
                "Should throw InvalidDataException for negative fee");
    }

    private static void testInheritanceAndEncapsulation() {
        Doctor doc = new Doctor("DOC101", "Dr. Alice", 40, Specialization.CARDIOLOGY, 200.0);
        assertEquals("Dr. Alice", doc.getName(), "Doctor name getter mismatch");
        assertEquals(40, doc.getAge(), "Doctor age getter mismatch");
        assertEquals("DOC101", doc.getId(), "Doctor ID getter mismatch");
        assertEquals(Specialization.CARDIOLOGY, doc.getSpecialization(), "Doctor specialization mismatch");
        assertEquals(200.0, doc.getConsultationFee(), "Doctor fee mismatch");
    }

    private static void testPatientCloning() {
        Patient original = new Patient("PAT501", "Alice", 28, "Migraine");
        original.getMedicalHistory().addCondition("Asthma");
        original.getMedicalHistory().addMedication("Inhaler");

        Patient cloned = original.clone();
        assertNotSame(original, cloned, "Cloned Patient must be a different instance");
        assertEquals(original.getName(), cloned.getName(), "Names must match in clone");
        assertNotSame(original.getMedicalHistory(), cloned.getMedicalHistory(), "Medical history in cloned Patient must be a new instance");
        assertEquals(original.getMedicalHistory().getConditions().size(), cloned.getMedicalHistory().getConditions().size(), "Medical conditions size mismatch");

        cloned.getMedicalHistory().addCondition("Diabetes");
        assertFalse(original.getMedicalHistory().getConditions().contains("Diabetes"), "Modifying clone history should not alter original");
    }

    private static void testAppointmentCloning() {
        Patient patient = new Patient("PAT501", "Alice", 28, "Migraine");
        Doctor doctor = new Doctor("DOC101", "Dr. Bob", 45, Specialization.PEDIATRICS, 120.0);
        Appointment appointment = new Appointment("APP1001", "2026-06-10 10:00", doctor, patient, AppointmentStatus.CONFIRMED);

        Appointment clonedApp = appointment.clone();
        assertNotSame(appointment, clonedApp, "Cloned Appointment must be a different instance");
        assertNotSame(appointment.getPatient(), clonedApp.getPatient(), "Nested patient object must be cloned deeply");
        assertSame(appointment.getDoctor(), clonedApp.getDoctor(), "Doctor is reference shared data, keeping shallow reference");
    }

    private static void testBillSummaryImmutability() {
        BillSummary summary = new BillSummary("BIL2001", "Charlie", 250.0);
        assertEquals("BIL2001", summary.getBillId(), "Bill summary ID mismatch");
        assertEquals("Charlie", summary.getPatientName(), "Bill summary Patient Name mismatch");
        assertEquals(250.0, summary.getTotalPaid(), "Bill summary Total Paid mismatch");
    }

    private static void testStrategyPattern() {
        Patient patient = new Patient("PAT501", "Alice", 28, "Migraine");
        Doctor doctor = new Doctor("DOC101", "Dr. Bob", 45, Specialization.PEDIATRICS, 100.0);
        Appointment app = new Appointment("APP1001", "2026-06-10 10:00", doctor, patient, AppointmentStatus.CONFIRMED);

        Bill stdBill = new Bill(app, 100.0, "BIL2001", new StandardBilling());
        assertEquals(118.0, stdBill.calculateFinalAmount(), "Standard billing mismatch (100 + 18% tax)");

        Bill discBill = new Bill(app, 100.0, "BIL2002", new DiscountBilling(0.1));
        assertEquals(106.2, discBill.calculateFinalAmount(), "Discounted billing mismatch (90 + 18% tax)");

        Bill premBill = new Bill(app, 100.0, "BIL2003", new PremiumBilling(0.2));
        assertEquals(141.6, premBill.calculateFinalAmount(), "Premium billing mismatch (120 + 18% tax)");
    }

    private static void testFactoryPattern() {
        Patient patient = new Patient("PAT501", "Alice", 28, "Migraine");
        Doctor doctor = new Doctor("DOC101", "Dr. Bob", 45, Specialization.PEDIATRICS, 100.0);
        Appointment app = new Appointment("APP1001", "2026-06-10 10:00", doctor, patient, AppointmentStatus.CONFIRMED);

        Bill bill1 = BillFactory.createBill("consultation", app, 100.0, "BIL1");
        assertTrue(bill1 instanceof ConsultationBill, "Factory must return ConsultationBill");

        Bill bill2 = BillFactory.createBill("procedure", app, 500.0, "BIL2");
        assertTrue(bill2 instanceof ProcedureBill, "Factory must return ProcedureBill");

        Bill bill3 = BillFactory.createBill("pharmacy", app, 50.0, "BIL3");
        assertTrue(bill3 instanceof PharmacyBill, "Factory must return PharmacyBill");
    }

    private static void testObserverPattern() {
        AppointmentService service = new AppointmentService();
        final int[] notificationsCount = {0};
        service.registerObserver(new com.airtribe.meditrack.interfaces.AppointmentObserver() {
            @Override
            public void onAppointmentScheduled(Appointment app) {
                notificationsCount[0]++;
            }

            @Override
            public void onAppointmentCancelled(Appointment app) {
                notificationsCount[0]++;
            }
        });

        Patient patient = new Patient("PAT501", "Alice", 28, "Migraine");
        Doctor doctor = new Doctor("DOC101", "Dr. Bob", 45, Specialization.PEDIATRICS, 100.0);

        Appointment app = service.scheduleAppointment("2026-06-12 14:00", doctor, patient);
        assertEquals(1, notificationsCount[0], "Observer should get notified of scheduling");

        try {
            service.cancelAppointment(app.getAppointmentId());
        } catch (AppointmentNotFoundException e) {
            fail("Appointment should exist");
        }
        assertEquals(2, notificationsCount[0], "Observer should get notified of cancellation");
        service.stopBackgroundMonitor();
    }

    private static void testStreamsAndLambdas() {
        DoctorService service = new DoctorService();
        Doctor d1 = new Doctor("DOC101", "Alice", 35, Specialization.CARDIOLOGY, 300.0);
        Doctor d2 = new Doctor("DOC102", "Bob", 40, Specialization.CARDIOLOGY, 200.0);
        Doctor d3 = new Doctor("DOC103", "Charlie", 50, Specialization.DERMATOLOGY, 150.0);
        service.addDoctor(d1);
        service.addDoctor(d2);
        service.addDoctor(d3);

        List<Doctor> cardio = service.filterDoctorsBySpecialization(Specialization.CARDIOLOGY);
        assertEquals(2, cardio.size(), "Cardiologists count mismatch");

        double avg = service.calculateAverageFee();
        assertEquals(216.67, Math.round(avg * 100.0) / 100.0, "Average fee calculation mismatch");

        AppointmentService appService = new AppointmentService();
        Patient p = new Patient("PAT501", "Patient A", 20, "None");
        appService.scheduleAppointment("2026-06-15 09:00", d1, p);
        appService.scheduleAppointment("2026-06-15 10:00", d1, p);
        appService.scheduleAppointment("2026-06-15 11:00", d2, p);

        Map<String, Long> countMap = appService.getAppointmentsCountPerDoctor();
        assertEquals(Long.valueOf(2), countMap.get("Alice"), "Alice should have 2 appointments");
        assertEquals(Long.valueOf(1), countMap.get("Bob"), "Bob should have 1 appointment");
        appService.stopBackgroundMonitor();
    }

    private static void testCSVFileOperations() throws IOException {
        String testDocFile = "test_doctors.csv";
        String testPatFile = "test_patients.csv";

        Doctor doc = new Doctor("DOC999", "Test Doctor", 50, Specialization.ORTHOPEDICS, 250.0);
        Patient pat = new Patient("PAT999", "Test Patient", 25, "Fracture");
        pat.getMedicalHistory().addCondition("High BP");

        CSVUtil.saveDoctors(testDocFile, List.of(doc));
        CSVUtil.savePatients(testPatFile, List.of(pat));

        List<Doctor> loadedDocs = CSVUtil.loadDoctors(testDocFile);
        List<Patient> loadedPats = CSVUtil.loadPatients(testPatFile);

        new File(testDocFile).delete();
        new File(testPatFile).delete();

        assertEquals(1, loadedDocs.size(), "Loaded doctors size mismatch");
        assertEquals("Test Doctor", loadedDocs.get(0).getName(), "Loaded doctor name mismatch");

        assertEquals(1, loadedPats.size(), "Loaded patients size mismatch");
        assertEquals("Test Patient", loadedPats.get(0).getName(), "Loaded patient name mismatch");
        assertTrue(loadedPats.get(0).getMedicalHistory().getConditions().contains("High BP"), "Loaded patient medical history condition mismatch");
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " - Expected: " + expected + ", Actual: " + actual);
        }
    }

    private static void assertEquals(double expected, double actual, String message) {
        if (Math.abs(expected - actual) > 0.001) {
            throw new AssertionError(message + " - Expected: " + expected + ", Actual: " + actual);
        }
    }

    private static void assertSame(Object expected, Object actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + " - Expected same instance.");
        }
    }

    private static void assertNotSame(Object expected, Object actual, String message) {
        if (expected == actual) {
            throw new AssertionError(message + " - Expected different instances.");
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertThrows(Class<? extends Throwable> exceptionClass, Runnable runnable, String message) {
        try {
            runnable.run();
            throw new AssertionError(message + " - Expected " + exceptionClass.getSimpleName() + " to be thrown");
        } catch (Throwable t) {
            if (!exceptionClass.isInstance(t)) {
                throw new AssertionError(message + " - Expected " + exceptionClass.getSimpleName() + " but got " + t.getClass().getSimpleName());
            }
        }
    }

    private static void fail(String message) {
        throw new AssertionError(message);
    }
}
