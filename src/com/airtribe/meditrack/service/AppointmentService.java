package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.enums.AppointmentStatus;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.interfaces.AppointmentObserver;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class AppointmentService {
    private final DataStore<Appointment> appointmentStore = new DataStore<>();
    private final List<AppointmentObserver> observers = new ArrayList<>();
    private Timer daemonTimer;

    public AppointmentService() {
        startBackgroundMonitor();
    }

    private void startBackgroundMonitor() {
        // Concurrency Demonstration: Run periodic scheduler checking active appointments
        daemonTimer = new Timer(true); // Daemon thread
        daemonTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                long activeCount = appointmentStore.getAll().stream()
                        .filter(app -> app.getStatus() == AppointmentStatus.CONFIRMED)
                        .count();
                System.out.println("\n[SYSTEM MONITOR] Running periodic check... Current Active Appointments: " + activeCount);
            }
        }, 10000, 30000); // Trigger after 10s, repeating every 30s
    }

    public void stopBackgroundMonitor() {
        if (daemonTimer != null) {
            daemonTimer.cancel();
        }
    }

    public void registerObserver(AppointmentObserver observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void removeObserver(AppointmentObserver observer) {
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    private void notifyScheduled(Appointment app) {
        List<AppointmentObserver> snapshot;
        synchronized (observers) {
            snapshot = new ArrayList<>(observers);
        }
        for (AppointmentObserver obs : snapshot) {
            obs.onAppointmentScheduled(app);
        }
    }

    private void notifyCancelled(Appointment app) {
        List<AppointmentObserver> snapshot;
        synchronized (observers) {
            snapshot = new ArrayList<>(observers);
        }
        for (AppointmentObserver obs : snapshot) {
            obs.onAppointmentCancelled(app);
        }
    }

    // Thread-safe synchronized slot booking
    public synchronized Appointment scheduleAppointment(String date, Doctor doctor, Patient patient) {
        // Check for double booking
        boolean conflict = appointmentStore.getAll().stream()
                .anyMatch(app -> app.getDoctor().getId().equalsIgnoreCase(doctor.getId())
                        && app.getDate().equalsIgnoreCase(date)
                        && app.getStatus() != AppointmentStatus.CANCELLED);
        if (conflict) {
            throw new IllegalStateException("Time slot " + date + " is already booked for Dr. " + doctor.getName());
        }

        String appId = IdGenerator.getInstance().nextAppointmentId();
        Appointment app = new Appointment(appId, date, doctor, patient, AppointmentStatus.CONFIRMED);
        appointmentStore.add(app);
        notifyScheduled(app);
        return app;
    }

    public synchronized void addRawAppointment(Appointment app) {
        appointmentStore.add(app);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentStore.getAll();
    }

    public synchronized void cancelAppointment(String appointmentId) throws AppointmentNotFoundException {
        Appointment target = appointmentStore.getAll().stream()
                .filter(app -> app.getAppointmentId().equalsIgnoreCase(appointmentId))
                .findFirst()
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment with ID " + appointmentId + " not found."));

        if (target.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Appointment is already cancelled.");
        }

        target.setStatus(AppointmentStatus.CANCELLED);
        notifyCancelled(target);
    }

    // Java 8 Streams Analytics: Count appointments per doctor (groupingBy)
    public Map<String, Long> getAppointmentsCountPerDoctor() {
        return appointmentStore.getAll().stream()
                .collect(Collectors.groupingBy(
                        app -> app.getDoctor().getName(),
                        Collectors.counting()
                ));
    }
}
