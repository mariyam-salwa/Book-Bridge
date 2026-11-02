package models;

/**
 * OOP CONCEPT: Inheritance & Polymorphism
 * PhysicalMaterial inherits from Material.
 * Represents physical textbooks, lab manuals, and guides listed for campus cash exchange.
 */
public class PhysicalMaterial extends Material {
    private String condition; // "NEW", "LIKE_NEW", "GOOD", "FAIR"
    private boolean availability;

    // Default Constructor
    public PhysicalMaterial() {
        super();
        this.setType("PHYSICAL");
        this.availability = true;
    }

    // Parameterized Constructor
    public PhysicalMaterial(int materialId, String title, String subject, String description,
                            double price, String status, int uploadedByUserId,
                            String condition, boolean availability) {
        super(materialId, title, subject, description, "PHYSICAL", price, 
              "CASH_ON_HANDOVER", status, uploadedByUserId);
        this.condition = condition;
        this.availability = availability;
    }

    // OOP CONCEPT: Polymorphism (Method Overriding)
    @Override
    public void displayMaterial() {
        System.out.println("--------------------------------------------------");
        System.out.println("📚 [PHYSICAL BOOK LISTING]");
        System.out.println("Title:        " + getTitle());
        System.out.println("Subject:      " + getSubject());
        System.out.println("Description:  " + getDescription());
        System.out.println("Condition:    " + this.condition);
        System.out.println("Price:        " + (getPrice() == 0 ? "FREE DONATION" : "₹" + getPrice()));
        System.out.println("Payment:      Cash on Campus Handover");
        System.out.println("Available:    " + (this.availability ? "YES (In Stock)" : "NO (Reserved/Sold)"));
        System.out.println("Status:       " + getStatus());
        System.out.println("--------------------------------------------------");
    }

    // Physical Material Specific Methods
    public void listBook() {
        this.availability = true;
        this.setStatus("AVAILABLE");
        System.out.println("Book '" + getTitle() + "' has been listed for campus exchange.");
    }

    public void updateBookDetails(String condition, double price) {
        this.condition = condition;
        setPrice(price);
        System.out.println("Book details updated: Condition=" + condition + ", Price=₹" + price);
    }

    public void updateAvailability(boolean isAvailable) {
        this.availability = isAvailable;
        if (!isAvailable) {
            this.setStatus("RESERVED_OR_SOLD");
        } else {
            this.setStatus("AVAILABLE");
        }
        System.out.println("Availability updated for '" + getTitle() + "' -> " + (isAvailable ? "Available" : "Not Available"));
    }

    // Getters and Setters
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}
