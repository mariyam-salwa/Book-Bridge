package data;

import models.*;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DataStore - holds all BookBridge data in memory using sample records.
 *
 * Member 1 (UI) only needs data to SHOW on screen, so this class keeps
 * everything in ArrayLists. The screens never touch the lists directly;
 * they only call the methods below (Encapsulation).
 *
 * HANDOVER NOTE FOR MEMBER 3:
 * To use MySQL, keep the same method names and change the code INSIDE them
 * to run SQL through JDBC. The Swing screens will not need any changes.
 */
public class DataStore {

    // ---------- The "tables" (private: only this class can change them) ----------
    private static final List<User> users = new ArrayList<>();
    private static final List<Material> materials = new ArrayList<>();
    private static final List<Request> requests = new ArrayList<>();
    private static final List<Transaction> transactions = new ArrayList<>();
    private static final Map<Integer, List<Integer>> savedMaterials = new HashMap<>(); // userId -> materialIds
    private static final Map<Integer, File> uploadedFiles = new HashMap<>();           // materialId -> real file

    // Auto-increment counters, like AUTO_INCREMENT in MySQL
    private static int nextUserId = 4;
    private static int nextMaterialId = 109;
    private static int nextRequestId = 503;
    private static int nextTransactionId = 702;

    // The user who is logged in right now (null = nobody)
    private static User currentUser;

    public static final String[] SUBJECTS = {
            "Computer Science", "Mathematics", "Physics", "Electronics",
            "Mechanical", "Civil", "English", "Other"
    };
    public static final String[] DEPARTMENTS = {
            "Computer Science", "Electronics", "Mechanical", "Civil", "Electrical", "Other"
    };
    public static final String[] CONDITIONS = {"NEW", "LIKE_NEW", "GOOD", "FAIR"};

    // A static block runs once, when the class is first used: fills the sample data.
    static {
        loadSampleData();
    }

    private static void loadSampleData() {
        // Users (same people as Member 2's Main.java demo)
        users.add(new Student(1, "Shamla Shirin", "shamla@college.edu", "9876543210", "pass123",
                "STU2024CS02", "Computer Science", 4));
        users.add(new Student(2, "Mariyam Salwa", "mariyam@college.edu", "9123456780", "mypass456",
                "STU2024CS01", "Computer Science", 4));
        users.add(new Student(3, "Arjun Menon", "arjun@college.edu", "9012345678", "arjun123",
                "STU2023EC11", "Electronics", 6));
        users.add(new Administrator(100, "Khadeejath Naziya", "admin@bookbridge.org", "9988776655",
                "adminSecure!", "ADM001", "SUPER_ADMIN"));

        // Digital materials (notes, papers)
        materials.add(new DigitalMaterial(101, "Data Structures Handwritten Notes", "Computer Science",
                "Units 1 to 5: trees, graphs and sorting, with diagrams.", 0, "AVAILABLE", 1,
                "dsa_notes_sem4.pdf", "PDF", 14.5));
        materials.add(new DigitalMaterial(102, "OOP in Java - Previous Question Papers (2021-2025)",
                "Computer Science", "Five years of university papers with answer hints.", 0, "AVAILABLE", 2,
                "oop_question_papers.pdf", "PDF", 3.2));
        materials.add(new DigitalMaterial(103, "Engineering Mathematics III Solved Problems", "Mathematics",
                "Laplace transforms and Fourier series, fully worked out.", 30, "AVAILABLE", 3,
                "maths3_solved.pdf", "PDF", 8.0));
        materials.add(new DigitalMaterial(104, "Digital Electronics Lab Manual", "Electronics",
                "All 12 experiments with circuit diagrams and viva questions.", 0, "AVAILABLE", 3,
                "de_lab_manual.docx", "DOCX", 2.1));

        // Physical books (cash on handover)
        materials.add(new PhysicalMaterial(105, "Operating System Concepts, 10th Ed. (Silberschatz)",
                "Computer Science", "Gently used, a few pencil highlights, no torn pages.", 350, "AVAILABLE", 1,
                "GOOD", true));
        materials.add(new PhysicalMaterial(106, "Higher Engineering Mathematics (B.S. Grewal)", "Mathematics",
                "Latest edition, bought this year, almost unused.", 500, "AVAILABLE", 3,
                "LIKE_NEW", true));
        materials.add(new PhysicalMaterial(107, "Concepts of Physics Vol. 1 (H.C. Verma)", "Physics",
                "Older edition, cover slightly worn.", 150, "RESERVED", 2,
                "FAIR", false));
        materials.add(new PhysicalMaterial(108, "Java: The Complete Reference (Schildt)", "Computer Science",
                "Free to a junior who needs it.", 0, "AVAILABLE", 2,
                "GOOD", true));

        // A request + transaction already in progress, so the dashboards are not empty
        requests.add(new Request(501, 1, 107, "2026-10-28", "ACCEPTED"));
        transactions.add(new Transaction(701, 107, 1, 2, "CASH_ON_HANDOVER", "PENDING_HANDOVER"));
        requests.add(new Request(502, 3, 105, "2026-11-01", "PENDING"));

        savedMaterials.put(1, new ArrayList<>(List.of(102, 106)));
    }

    // ======================= Login / Register =======================

    /** Returns the user if email + password match, otherwise null. */
    public static User login(String email, String password) {
        for (User u : users) {
            // Uses Member 2's User.login() method to check the password
            if (u.getEmail().equalsIgnoreCase(email) && u.login(email, password)) {
                currentUser = u;
                return u;
            }
        }
        return null;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isEmailTaken(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) return true;
        }
        return false;
    }

    public static Student registerStudent(String name, String email, String phone, String password,
                                          String studentId, String department, int semester) {
        Student s = new Student(nextUserId++, name, email, phone, password, studentId, department, semester);
        s.register(); // Member 2's method (prints a log line)
        users.add(s);
        return s;
    }

    // ======================= Users =======================

    public static User getUserById(int id) {
        for (User u : users) {
            if (u.getUserId() == id) return u;
        }
        return null;
    }

    public static String getUserName(int id) {
        User u = getUserById(id);
        return u == null ? "(deleted user)" : u.getName();
    }

    public static List<Student> getStudents() {
        List<Student> result = new ArrayList<>();
        for (User u : users) {
            if (u instanceof Student) result.add((Student) u);
        }
        return result;
    }

    public static void removeUser(User user) {
        users.remove(user);
        // Hide everything that user had listed
        for (Material m : materials) {
            if (m.getUploadedByUserId() == user.getUserId()) m.changeStatus("REMOVED");
        }
    }

    // ======================= Materials =======================

    public static List<Material> getAllMaterials() {
        return new ArrayList<>(materials); // a copy, so screens cannot change our list
    }

    public static Material getMaterialById(int id) {
        for (Material m : materials) {
            if (m.getMaterialId() == id) return m;
        }
        return null;
    }

    /**
     * Search used by the Explore screen.
     * @param keyword text to look for in title / subject / description ("" = anything)
     * @param type    "All", "Digital" or "Physical"
     * @param price   "All", "Free" or "Paid"
     * @param subject "All" or one of SUBJECTS
     */
    public static List<Material> search(String keyword, String type, String price, String subject) {
        List<Material> result = new ArrayList<>();
        String k = keyword.trim().toLowerCase();
        for (Material m : materials) {
            if (m.getStatus().equals("REMOVED")) continue;

            boolean keywordOk = k.isEmpty()
                    || m.getTitle().toLowerCase().contains(k)
                    || m.getSubject().toLowerCase().contains(k)
                    || m.getDescription().toLowerCase().contains(k);
            boolean typeOk = type.equals("All")
                    || (type.equals("Digital") && m instanceof DigitalMaterial)
                    || (type.equals("Physical") && m instanceof PhysicalMaterial);
            boolean priceOk = price.equals("All")
                    || (price.equals("Free") && m.getPrice() == 0)
                    || (price.equals("Paid") && m.getPrice() > 0);
            boolean subjectOk = subject.equals("All") || m.getSubject().equals(subject);

            if (keywordOk && typeOk && priceOk && subjectOk) result.add(m);
        }
        return result;
    }

    public static List<Material> getMaterialsUploadedBy(int userId) {
        List<Material> result = new ArrayList<>();
        for (Material m : materials) {
            if (m.getUploadedByUserId() == userId && !m.getStatus().equals("REMOVED")) result.add(m);
        }
        return result;
    }

    public static DigitalMaterial addDigitalMaterial(String title, String subject, String description,
                                                     double price, File file) {
        String name = file.getName();
        String ext = name.contains(".") ? name.substring(name.lastIndexOf('.') + 1).toUpperCase() : "FILE";
        double sizeMb = Math.round(file.length() / 1024.0 / 1024.0 * 10) / 10.0;
        DigitalMaterial dm = new DigitalMaterial(nextMaterialId++, title, subject, description, price,
                "AVAILABLE", currentUser.getUserId(), name, ext, sizeMb);
        materials.add(dm);
        uploadedFiles.put(dm.getMaterialId(), file);
        dm.addMaterial();
        return dm;
    }

    public static PhysicalMaterial addPhysicalMaterial(String title, String subject, String description,
                                                       double price, String condition) {
        PhysicalMaterial pm = new PhysicalMaterial(nextMaterialId++, title, subject, description, price,
                "AVAILABLE", currentUser.getUserId(), condition, true);
        materials.add(pm);
        pm.addMaterial();
        return pm;
    }

    /** The real file a student uploaded, or null for sample records. */
    public static File getUploadedFile(int materialId) {
        return uploadedFiles.get(materialId);
    }

    public static void removeMaterial(Material m) {
        m.changeStatus("REMOVED");
    }

    public static void restoreMaterial(Material m) {
        if (m instanceof PhysicalMaterial) {
            ((PhysicalMaterial) m).updateAvailability(true); // also sets status AVAILABLE
        } else {
            m.changeStatus("AVAILABLE");
        }
    }

    // ======================= Saved materials =======================

    public static boolean isSaved(int userId, int materialId) {
        List<Integer> list = savedMaterials.get(userId);
        return list != null && list.contains(materialId);
    }

    public static void toggleSaved(int userId, int materialId) {
        savedMaterials.putIfAbsent(userId, new ArrayList<>());
        List<Integer> list = savedMaterials.get(userId);
        if (list.contains(materialId)) {
            list.remove(Integer.valueOf(materialId));
        } else {
            list.add(materialId);
        }
    }

    public static List<Material> getSavedMaterials(int userId) {
        List<Material> result = new ArrayList<>();
        List<Integer> ids = savedMaterials.getOrDefault(userId, new ArrayList<>());
        for (int id : ids) {
            Material m = getMaterialById(id);
            if (m != null && !m.getStatus().equals("REMOVED")) result.add(m);
        }
        return result;
    }

    // ======================= Requests =======================

    /**
     * Creates a purchase / handover request.
     * @return null if it worked, otherwise an error message to show the user.
     */
    public static String createRequest(User buyer, Material m) {
        if (m.getUploadedByUserId() == buyer.getUserId()) {
            return "You cannot request your own listing.";
        }
        if (!m.getStatus().equals("AVAILABLE")) {
            return "This item is not available right now.";
        }
        for (Request r : requests) {
            if (r.getStudentId() == buyer.getUserId() && r.getMaterialId() == m.getMaterialId()
                    && (r.getStatus().equals("PENDING") || r.getStatus().equals("ACCEPTED"))) {
                return "You have already requested this item.";
            }
        }
        Request r = new Request(nextRequestId++, buyer.getUserId(), m.getMaterialId(),
                LocalDate.now().toString(), "PENDING");
        r.createRequest();
        requests.add(r);
        return null;
    }

    public static List<Request> getRequestsSentBy(int userId) {
        List<Request> result = new ArrayList<>();
        for (Request r : requests) {
            if (r.getStudentId() == userId) result.add(r);
        }
        return result;
    }

    /** Requests other students sent for items this user listed. */
    public static List<Request> getRequestsReceivedBy(int sellerId) {
        List<Request> result = new ArrayList<>();
        for (Request r : requests) {
            Material m = getMaterialById(r.getMaterialId());
            if (m != null && m.getUploadedByUserId() == sellerId) result.add(r);
        }
        return result;
    }

    public static Request getRequestById(int id) {
        for (Request r : requests) {
            if (r.getRequestId() == id) return r;
        }
        return null;
    }

    /** Seller accepts: item becomes RESERVED and a transaction is opened. */
    public static void acceptRequest(Request r) {
        Material m = getMaterialById(r.getMaterialId());
        r.updateStatus("ACCEPTED");
        if (m instanceof PhysicalMaterial) {
            ((PhysicalMaterial) m).updateAvailability(false);
        }
        m.changeStatus("RESERVED"); // set after updateAvailability, which writes its own status
        String payment = m.getPrice() > 0 ? "CASH_ON_HANDOVER" : "FREE";
        Transaction t = new Transaction(nextTransactionId++, m.getMaterialId(), r.getStudentId(),
                m.getUploadedByUserId(), payment, "PENDING_HANDOVER");
        t.createTransaction();
        transactions.add(t);
    }

    public static void declineRequest(Request r) {
        r.updateStatus("DECLINED");
    }

    public static void cancelRequest(Request r) {
        r.cancelRequest(); // Member 2's method sets status to CANCELLED
    }

    // ======================= Transactions =======================

    public static List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    public static Transaction getTransactionById(int id) {
        for (Transaction t : transactions) {
            if (t.getTransactionId() == id) return t;
        }
        return null;
    }

    /** Finds the open transaction that belongs to an accepted request. */
    public static Transaction findTransactionFor(Request r) {
        for (Transaction t : transactions) {
            if (t.getMaterialId() == r.getMaterialId() && t.getBuyerId() == r.getStudentId()
                    && t.getStatus().equals("PENDING_HANDOVER")) {
                return t;
            }
        }
        return null;
    }

    /** Book handed over and cash paid: close everything. */
    public static void completeTransaction(Transaction t) {
        t.completeTransaction(); // Member 2's method sets status to COMPLETED
        Material m = getMaterialById(t.getMaterialId());
        if (m != null) m.changeStatus("SOLD");
        for (Request r : requests) {
            if (r.getMaterialId() == t.getMaterialId() && r.getStudentId() == t.getBuyerId()
                    && r.getStatus().equals("ACCEPTED")) {
                r.updateStatus("COMPLETED");
            }
        }
    }
}
