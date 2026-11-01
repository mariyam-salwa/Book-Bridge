package models;

/**
 * OOP CONCEPT: Encapsulation & Domain Modeling
 * Request represents a student's expression of interest in accessing a study resource
 * or requesting a meetup to buy/exchange a physical textbook.
 */
public class Request {
    private int requestId;
    private int studentId;
    private int materialId;
    private String requestDate;
    private String status; // "PENDING", "ACCEPTED", "DECLINED", "COMPLETED"

    // Default Constructor
    public Request() {
    }

    // Parameterized Constructor
    public Request(int requestId, int studentId, int materialId, String requestDate, String status) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.materialId = materialId;
        this.requestDate = requestDate;
        this.status = status;
    }

    // Methods
    public void createRequest() {
        this.status = "PENDING";
        System.out.println("Request #" + this.requestId + " created on " + this.requestDate + " with status PENDING.");
    }

    public void cancelRequest() {
        this.status = "CANCELLED";
        System.out.println("Request #" + this.requestId + " has been cancelled by the student.");
    }

    public void updateStatus(String newStatus) {
        this.status = newStatus;
        System.out.println("Request #" + this.requestId + " status updated to: " + newStatus);
    }

    // Getters and Setters
    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
