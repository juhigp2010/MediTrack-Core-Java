package com.airtribe.meditrack.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    // 1. Eager Initialization Singleton Example
    private static final IdGenerator eagerInstance = new IdGenerator();

    // 2. Lazy Initialization Singleton Example (Double-Checked Locking)
    private static volatile IdGenerator lazyInstance;

    private final AtomicInteger doctorCounter = new AtomicInteger(100);
    private final AtomicInteger patientCounter = new AtomicInteger(500);
    private final AtomicInteger appointmentCounter = new AtomicInteger(1000);
    private final AtomicInteger billCounter = new AtomicInteger(2000);

    // Private constructor to prevent instantiation
    private IdGenerator() {}

    // Eager getter
    public static IdGenerator getInstance() {
        return eagerInstance;
    }

    // Lazy getter (Double-Checked Locking)
    public static IdGenerator getLazyInstance() {
        if (lazyInstance == null) {
            synchronized (IdGenerator.class) {
                if (lazyInstance == null) {
                    lazyInstance = new IdGenerator();
                }
            }
        }
        return lazyInstance;
    }

    public String nextDoctorId() {
        return "DOC" + doctorCounter.incrementAndGet();
    }

    public String nextPatientId() {
        return "PAT" + patientCounter.incrementAndGet();
    }

    public String nextAppointmentId() {
        return "APP" + appointmentCounter.incrementAndGet();
    }

    public String nextBillId() {
        return "BIL" + billCounter.incrementAndGet();
    }
}
