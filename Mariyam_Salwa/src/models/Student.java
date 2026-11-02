package models;

/**
 * OOP CONCEPT: Inheritance
 * Student inherits all attributes and methods from User (User is the parent/super class).
 * Student also adds student-specific attributes (studentId, department, semester).
 */
public class Student extends User {
    private String studentId;
    private String department;
    private int semester;

    // Default Constructor
    public Student() {
        super();
        this.setRole("STUDENT");
    }

    // Parameterized Constructor using 'super' to initialize parent User properties
    public Student(int userId, String name, String email, String phone, String password,
                   String studentId, String department, int semester) {
        super(userId, name, email, phone, password, "STUDENT");
        this.studentId = studentId;
        this.department = department;
        this.semester = semester;
    }

    // Student-specific Methods
    public void uploadMaterial(Material material) {
        System.out.println("Student " + getName() + " uploaded a new material: '" + material.getTitle() + "'");
    }

    public void searchMaterial(String keyword) {
        System.out.println("Student " + getName() + " is searching for materials with keyword: \"" + keyword + "\"");
    }

    public void downloadMaterial(DigitalMaterial material) {
        System.out.println("Student " + getName() + " downloaded digital resource: " + material.getTitle() + 
                           " (" + material.getFileName() + ")");
    }

    public void saveMaterial(Material material) {
        System.out.println("Material '" + material.getTitle() + "' added to saved items for " + getName());
    }

    public void listBook(PhysicalMaterial book) {
        System.out.println("Student " + getName() + " listed physical book for sale/exchange: " + 
                           book.getTitle() + " [Price: ₹" + book.getPrice() + ", Condition: " + book.getCondition() + "]");
    }

    public Request requestMaterial(Material material) {
        System.out.println("Student " + getName() + " submitted a request for: " + material.getTitle());
        return new Request(1, this.getUserId(), material.getMaterialId(), "2026-10-31", "PENDING");
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
}
