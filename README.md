# MediTrack - Core Java Healthcare Management System

MediTrack is a comprehensive, production-grade Healthcare Management System built using Core Java. It demonstrates proficiency in advanced Object-Oriented Programming (OOP) concepts, Design Patterns, Java Streams, Concurrency, and JVM Architecture.

---

## 🛠️ Tech Stack & Key Concepts Demonstrated

1.  **Core & Advanced OOP**: Encapsulation with centralized parameter validation, inheritance, dynamic method dispatch (polymorphism), abstraction, deep vs. shallow cloning, and immutable thread-safe records.
2.  **Design Patterns**:
    *   **Singleton**: Both eager-initialized and lazy (double-checked locking) thread-safe singletons inside `IdGenerator`.
    *   **Factory**: `BillFactory` dynamically creating concrete `Bill` subclasses.
    *   **Strategy**: Interposable `BillingStrategy` for standard, discounted, or premium invoice calculations.
    *   **Observer**: Decoupled events triggering SMS/Console notifications on scheduled and cancelled appointments.
3.  **Concurrency**:
    *   Counter increments using thread-safe `AtomicInteger`.
    *   Synchronized schedule/cancel methods to prevent doctor double-booking.
    *   Background monitoring scheduler using JVM daemon threads and `TimerTask`.
4.  **Java 8+ Streams**: Declarative stream queries, specialization filtering, average fees calculation, and group-by aggregations.
5.  **File I/O**: Custom CSV database serialization/deserialization implementing standard `try-with-resources`.

---

## 📂 Project Package Structure

```
src/com/airtribe/meditrack/
├── Main.java                      # Interactive console loop and CLI bootstrapper
├── constants/
│   └── Constants.java             # Shared configurations, tax rates, and file paths
├── enums/
│   ├── AppointmentStatus.java     # Enums for Appointment lifecycle (PENDING, CONFIRMED, CANCELLED)
│   └── Specialization.java        # Medical Specializations (CARDIOLOGY, PEDIATRICS, etc.)
├── exception/
│   ├── AppointmentNotFoundException.java
│   └── InvalidDataException.java
├── interfaces/
│   ├── Searchable.java            # ID/Name matching interface
│   ├── Payable.java               # Billing interface
│   ├── BillingStrategy.java       # Strategy pattern definition
│   └── AppointmentObserver.java   # Observer pattern listener interface
├── entity/
│   ├── MedicalEntity.java         # Base abstract representation
│   ├── Person.java                # Abstract class extending MedicalEntity
│   ├── Doctor.java                # Doctor class implementing Searchable
│   ├── Patient.java               # Patient class implementing Searchable & Cloneable (Deep Copy)
│   ├── MedicalHistory.java        # Nested mutable data for deep copying
│   ├── Appointment.java           # Appointment details and deep clone implementation
│   ├── Bill.java                  # Invoice model with Strategy interposition
│   ├── ConsultationBill.java      # Subclass of Bill
│   ├── ProcedureBill.java         # Subclass of Bill
│   ├── PharmacyBill.java          # Subclass of Bill
│   └── BillSummary.java           # Final thread-safe immutable record representation
├── util/
│   ├── DataStore.java             # Generic, thread-safe in-memory database store
│   ├── IdGenerator.java           # Multi-pattern thread-safe unique ID generator
│   ├── Validator.java             # Centralized validation rules engine
│   ├── DateUtil.java              # Helper for parsing & validating format strings
│   ├── CSVUtil.java               # CSV File reader/writer utilizing try-with-resources
│   ├── AIHelper.java              # Symptoms-to-specialty recommender & slot suggester
│   ├── StandardBilling.java       # BillingStrategy implementation
│   ├── DiscountBilling.java       # BillingStrategy implementation
│   └── PremiumBilling.java        # BillingStrategy implementation
├── service/
│   ├── PatientService.java        # CRUD services and overloaded search methods
│   ├── DoctorService.java         # CRUD services and Streams specialty query methods
│   ├── AppointmentService.java    # Synced scheduler, observers broadcaster, and daemon monitors
│   └── ConsoleNotificationService.java # Concrete observer printing console reminders
└── test/
    └── TestRunner.java            # Automated manual verification suite
```

---

## 🚦 Getting Started

### 1. Compile the Project
From the root directory of the workspace, run:
```bash
mkdir -p out
javac -d out src/com/airtribe/meditrack/constants/*.java \
             src/com/airtribe/meditrack/enums/*.java \
             src/com/airtribe/meditrack/interfaces/*.java \
             src/com/airtribe/meditrack/exception/*.java \
             src/com/airtribe/meditrack/entity/*.java \
             src/com/airtribe/meditrack/util/*.java \
             src/com/airtribe/meditrack/service/*.java \
             src/com/airtribe/meditrack/*.java \
             src/com/airtribe/meditrack/test/*.java
```

### 2. Run the Automated Test Suite
To verify compliance of OOP mechanics, design patterns, deep cloning, and Streams, execute:
```bash
java -cp out com.airtribe.meditrack.test.TestRunner
```

### 3. Run the Interactive Main Application
To launch the interactive CLI program:
```bash
java -cp out com.airtribe.meditrack.Main
```

### 4. Run with Preloaded CSV Databases
To start the program and automatically load existing saved records:
```bash
java -cp out com.airtribe.meditrack.Main --loadData
```

---

## 📝 Documentation
Additional details are available under the `docs/` directory:
*   [Setup_Instructions.md](file:///Users/juhi/Airtribe/projects/mediTrack/docs/Setup_Instructions.md) - JDK setup instructions, environmental settings, and workflow visual diagram.
*   [JVM_Report.md](file:///Users/juhi/Airtribe/projects/mediTrack/docs/JVM_Report.md) - Deep dive into JVM Classloaders, execution engine, compiler mechanics, and cross-platform architecture.
