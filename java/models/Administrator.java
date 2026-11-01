package models;

/**
 * OOP CONCEPT: Inheritance
 * Administrator inherits from User and adds platform moderation responsibilities.
 */
public class Administrator extends User {
    private String adminId;
    private String adminRole; // e.g. "SUPER_ADMIN", "CONTENT_MODERATOR"

    // Default Constructor
    public Administrator() {
        super();
        this.setRole("ADMIN");
    }

    // Parameterized Constructor
    public Administrator(int userId, String name, String email, String phone, String password,
                         String adminId, String adminRole) {
        super(userId, name, email, phone, password, "ADMIN");
        this.adminId = adminId;
        this.adminRole = adminRole;
    }

    // Administrator Methods
    public void manageUsers(User user, String action) {
        System.out.println("Admin " + getName() + " performed action '" + action + "' on user: " + user.getName());
    }

    public void manageMaterials(Material material, String action) {
        System.out.println("Admin " + getName() + " performed action '" + action + "' on material: " + material.getTitle());
    }

    public void manageListings(PhysicalMaterial book, String action) {
        System.out.println("Admin " + getName() + " modified physical listing status: " + book.getTitle() + " -> " + action);
    }

    public void manageAccess(User user, boolean grantAccess) {
        System.out.println("Admin " + getName() + " set access for user " + user.getName() + " to: " + (grantAccess ? "ALLOWED" : "BLOCKED"));
    }

    // Getters and Setters
    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminRole() {
        return adminRole;
    }

    public void setAdminRole(String adminRole) {
        this.adminRole = adminRole;
    }
}
