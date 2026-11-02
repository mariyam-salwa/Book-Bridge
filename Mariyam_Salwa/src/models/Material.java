package models;

/**
 * OOP CONCEPT: Abstraction & Base Class (Inheritance & Polymorphism)
 * Material represents any academic resource (PDF, Notes, Question Papers, Physical Books).
 */
public abstract class Material {
    private int materialId;
    private String title;
    private String subject;
    private String description;
    private String type; // "DIGITAL" or "PHYSICAL"
    private double price; // 0.0 for Free
    private String paymentType; // "FREE", "PAID", or "CASH_ON_HANDOVER"
    private String status; // "AVAILABLE", "RESERVED", "DELIVERED"
    private int uploadedByUserId;

    // Default Constructor
    public Material() {
    }

    // Parameterized Constructor
    public Material(int materialId, String title, String subject, String description,
                    String type, double price, String paymentType, String status, int uploadedByUserId) {
        this.materialId = materialId;
        this.title = title;
        this.subject = subject;
        this.description = description;
        this.type = type;
        setPrice(price); // Uses setter for validation
        this.paymentType = paymentType;
        this.status = status;
        this.uploadedByUserId = uploadedByUserId;
    }

    // OOP CONCEPT: Polymorphism (Abstract method to be overridden differently by child classes)
    public abstract void displayMaterial();

    // General Material Methods
    public void addMaterial() {
        System.out.println("Material added to database: " + this.title + " (" + this.type + ")");
    }

    public void updateMaterial(String newTitle, double newPrice) {
        this.title = newTitle;
        setPrice(newPrice);
        System.out.println("Material ID " + this.materialId + " updated successfully.");
    }

    public void deleteMaterial() {
        this.status = "DELETED";
        System.out.println("Material ID " + this.materialId + " marked as DELETED.");
    }

    public void changeStatus(String newStatus) {
        this.status = newStatus;
        System.out.println("Status of '" + this.title + "' changed to: " + newStatus);
    }

    // Encapsulation: Getters and Setters with validation logic
    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    // Encapsulation rule: Price cannot be negative. If free, paymentType is FREE
    public void setPrice(double price) {
        if (price < 0) {
            this.price = 0.0;
            this.paymentType = "FREE";
        } else {
            this.price = price;
            if (price == 0.0) {
                this.paymentType = "FREE";
            }
        }
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUploadedByUserId() {
        return uploadedByUserId;
    }

    public void setUploadedByUserId(int uploadedByUserId) {
        this.uploadedByUserId = uploadedByUserId;
    }
}
