package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.interfaces.AppointmentObserver;

public class ConsoleNotificationService implements AppointmentObserver {
    @Override
    public void onAppointmentScheduled(Appointment appointment) {
        System.out.println("\n>>> [NOTIFICATION] Appointment Scheduled: ID " + appointment.getAppointmentId() +
                           " for Patient '" + appointment.getPatient().getName() +
                           "' with Dr. " + appointment.getDoctor().getName() +
                           " on " + appointment.getDate() + ".");
    }

    @Override
    public void onAppointmentCancelled(Appointment appointment) {
        System.out.println("\n>>> [NOTIFICATION] Appointment Cancelled: ID " + appointment.getAppointmentId() +
                           " for Patient '" + appointment.getPatient().getName() + "'.");
    }
}
