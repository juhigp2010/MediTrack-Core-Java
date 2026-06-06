package com.airtribe.meditrack.interfaces;

import com.airtribe.meditrack.entity.Appointment;

public interface AppointmentObserver {
    void onAppointmentScheduled(Appointment appointment);
    void onAppointmentCancelled(Appointment appointment);
}
