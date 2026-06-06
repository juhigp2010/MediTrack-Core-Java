# MediTrack Setup & Execution Instructions

This guide provides step-by-step instructions for installing and configuring Java, compiling the MediTrack application, and running the system.

---

## 1. Java Installation & Environment Setup

MediTrack is compatible with **Java Development Kit (JDK) 17** or higher.

### macOS Installation
1. Install using **Homebrew**:
   ```bash
   brew install openjdk@17
   ```
2. Or download the macOS DMG installer package from the official Oracle/Adoptium website and run the installer.
3. Configure the environment variables by editing your `~/.zshrc` or `~/.bash_profile`:
   ```bash
   export JAVA_HOME=$(/usr/libexec/java_home -v 17)
   export PATH=$JAVA_HOME/bin:$PATH
   ```
4. Reload your terminal settings:
   ```bash
   source ~/.zshrc
   ```

### Windows Installation
1. Download the Windows x64 MSI or EXE installer from Oracle/Adoptium.
2. Run the installer and check the box to automatically set `JAVA_HOME`.
3. If setting manually, open **System Environment Variables**, add a new system variable named `JAVA_HOME` pointing to your JDK installation directory (e.g., `C:\Program Files\Java\jdk-17`), and edit the system `Path` variable to append `%JAVA_HOME%\bin`.

---

## 2. Verify Installation

To verify that Java is installed and configured correctly, run the following commands in your shell:
```bash
java -version
javac -version
```
Expected output should reference Java version `17.x.x` (or higher).

---

## 3. Configuration & Steps Workflow Diagram

The diagram below outlines the installation steps, environment settings, and basic command-line compilation workflow:

![JDK Installation and CLI Compilation](/Users/juhi/Airtribe/projects/mediTrack/docs/medi_track_setup_screenshot.png)

---

## 4. Compiling and Running MediTrack

### A. Compiling the Project
To compile all classes and output the bytecodes to the `out` directory, run this command from the root directory of the workspace:
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

### B. Running the Automated Test Suite
To execute the automated checks and verify the system's compliance (assertions on deep copy, strategy billing, singleton, streams, etc.):
```bash
java -cp out com.airtribe.meditrack.test.TestRunner
```

### C. Running the Main Interactive Console Menu
To run the interactive CLI program:
```bash
java -cp out com.airtribe.meditrack.Main
```

### D. Running with Persistent CSV Data Loaded
To start the application and automatically import the saved patients, doctors, and appointment logs:
```bash
java -cp out com.airtribe.meditrack.Main --loadData
```
*(Make sure to use the 'Save' option in the Main menu to dump your in-memory database back to CSV files on exit).*
