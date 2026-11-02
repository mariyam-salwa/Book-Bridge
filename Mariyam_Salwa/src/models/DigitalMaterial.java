package models;

/**
 * OOP CONCEPT: Inheritance & Polymorphism
 * DigitalMaterial inherits from Material.
 * Overrides displayMaterial() to print digital-specific properties (File size, file type, downloads).
 */
public class DigitalMaterial extends Material {
    private String fileName;
    private String fileType; // "PDF", "DOCX", "ZIP"
    private double fileSize; // Size in MB

    // Default Constructor
    public DigitalMaterial() {
        super();
        this.setType("DIGITAL");
    }

    // Parameterized Constructor
    public DigitalMaterial(int materialId, String title, String subject, String description,
                           double price, String status, int uploadedByUserId,
                           String fileName, String fileType, double fileSize) {
        super(materialId, title, subject, description, "DIGITAL", price, 
              (price > 0 ? "PAID" : "FREE"), status, uploadedByUserId);
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

    // OOP CONCEPT: Polymorphism (Method Overriding)
    @Override
    public void displayMaterial() {
        System.out.println("--------------------------------------------------");
        System.out.println("📄 [DIGITAL STUDY MATERIAL]");
        System.out.println("Title:       " + getTitle());
        System.out.println("Subject:     " + getSubject());
        System.out.println("Description: " + getDescription());
        System.out.println("File:        " + this.fileName + " (" + this.fileType + " - " + this.fileSize + " MB)");
        System.out.println("Access:      " + (getPrice() == 0 ? "FREE DOWNLOAD" : "₹" + getPrice()));
        System.out.println("Status:      " + getStatus());
        System.out.println("--------------------------------------------------");
    }

    // Digital Specific Methods
    public void uploadFile(String fileName, double size) {
        this.fileName = fileName;
        this.fileSize = size;
        System.out.println("Digital file '" + fileName + "' (" + size + " MB) successfully uploaded to server.");
    }

    public void viewFile() {
        System.out.println("Opening online PDF viewer for file: " + this.fileName);
    }

    public void downloadFile() {
        System.out.println("Starting download of: " + this.fileName + " [" + this.fileSize + " MB]...");
    }

    // Getters and Setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }
}
