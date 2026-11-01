import models.*;

/**
 * Main application test runner for Book Bridge OOP Project.
 * This class demonstrates all core Object-Oriented Programming (OOP) concepts:
 * 1. Classes & Objects
 * 2. Encapsulation (Getters, setters, private fields, business rules)
 * 3. Inheritance (Student & Administrator extend User; Digital & Physical extend Material)
 * 4. Polymorphism (Calling overridden displayMaterial() via parent Material references)
 * 5. Abstraction (Material abstract base class defining common contract)
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("       📚 WELCOME TO BOOK BRIDGE (OOP DEMONSTRATION)       ");
        System.out.println("==========================================================\n");

        // -------------------------------------------------------------------------
        // 1. INHERITANCE & ENCAPSULATION DEMONSTRATION
        // -------------------------------------------------------------------------
        System.out.println(">>> 1. Creating Users (Inheritance: Student & Admin extend User)");

        // Creating a Student (inherits User attributes like name, email, phone)
        Student student1 = new Student(
            1, "Shamla Shirin", "shamla@college.edu", "9876543210", "pass123",
            "STU2024CS02", "Computer Science", 4
        );

        Student student2 = new Student(
            2, "Maryam Salwa", "maryam@college.edu", "9123456780", "mypass456",
            "STU2024CS01", "Computer Science", 4
        );

        // Creating an Administrator
        Administrator admin = new Administrator(
            100, "Kadheejath Naziya", "admin@bookbridge.org", "9988776655", "adminSecure!",
            "ADM001", "SUPER_ADMIN"
        );

        // Test Login method (inherited from User)
        System.out.println("\nTesting Login for " + student1.getName() + ":");
        student1.login("shamla@college.edu", "pass123");

        // -------------------------------------------------------------------------
        // 2. POLYMORPHISM & ABSTRACTION DEMONSTRATION
        // -------------------------------------------------------------------------
        System.out.println("\n>>> 2. Creating Academic Materials (Polymorphism & Abstraction)");

        // Creating a Digital Material (PDF notes)
        DigitalMaterial notes = new DigitalMaterial(
            101, "Data Structures & Algorithms Handwritten Notes", "Computer Science",
            "Complete Unit 1-5 Trees, Graphs, Sorting algorithms with diagram notes.",
            0.0, "AVAILABLE", student1.getUserId(),
            "dsa_handwritten_notes_sem4.pdf", "PDF", 14.5
        );

        // Creating a Physical Material (Textbook for campus sale/handover)
        PhysicalMaterial textbook = new PhysicalMaterial(
            202, "Operating System Concepts (Silberschatz) 10th Edition", "Computer Science",
            "Gently used reference textbook. No torn pages, few pencil highlights.",
            350.0, "AVAILABLE", student2.getUserId(),
            "LIKE_NEW", true
        );

        // POLYMORPHISM IN ACTION:
        // Storing different child objects inside an array of the parent 'Material' type!
        Material[] catalog = { notes, textbook };

        System.out.println("\n--- Displaying Catalog Using Polymorphic Calls ---");
        for (Material item : catalog) {
            // Java automatically calls the correct child version of displayMaterial() at runtime!
            item.displayMaterial();
        }

        // -------------------------------------------------------------------------
        // 3. STUDENT ACTIONS & SIMULATED WORKFLOW
        // -------------------------------------------------------------------------
        System.out.println("\n>>> 3. Simulating Student Workflow (Search, List, Request, Handover)");

        // Student 1 searches for material
        student1.searchMaterial("Operating System");

        // Student 1 requests the physical textbook listed by Student 2
        Request bookRequest = student1.requestMaterial(textbook);
        bookRequest.createRequest();

        // Student 2 accepts the request and initiates the handover transaction
        System.out.println("\nSeller (" + student2.getName() + ") accepts the request.");
        bookRequest.updateStatus("ACCEPTED");

        // Transaction is created to track direct campus cash handover
        Transaction handoverTransaction = new Transaction(
            5001, textbook.getMaterialId(), student1.getUserId(), student2.getUserId(),
            "CASH_ON_HANDOVER", "PENDING_HANDOVER"
        );
        handoverTransaction.createTransaction();

        // Handover completed on campus
        System.out.println("\nMeeting on campus at College Canteen...");
        handoverTransaction.completeTransaction();
        textbook.updateAvailability(false); // Book is now sold
        bookRequest.updateStatus("COMPLETED");

        // Display final transaction summary
        handoverTransaction.displayTransaction();

        // -------------------------------------------------------------------------
        // 4. ADMINISTRATOR AUDIT
        // -------------------------------------------------------------------------
        System.out.println("\n>>> 4. Administrator Platform Oversight");
        admin.manageMaterials(textbook, "ARCHIVE_COMPLETED_LISTING");
        System.out.println("\n==========================================================");
        System.out.println("  ✅ ALL OOP TESTS EXECUTED SUCCESSFULLY WITHOUT ERRORS!   ");
        System.out.println("==========================================================");
    }
}
