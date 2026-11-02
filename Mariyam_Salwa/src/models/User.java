package models;

/**
 * OOP CONCEPT: Encapsulation & Base Class (Inheritance)
 * User is the parent class for all users in Book Bridge (Students & Admins).
 */
public class User {
    // Encapsulation: fields are private, protected from outside direct tampering
    private int userId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role; // "STUDENT" or "ADMIN"

    // Default Constructor
    public User() {
    }

    // Parameterized Constructor
    public User(int userId, String name, String email, String phone, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }

    // Common Methods
    public boolean login(String enteredEmail, String enteredPassword) {
        if (this.email.equalsIgnoreCase(enteredEmail) && this.password.equals(enteredPassword)) {
            System.out.println("Login successful! Welcome, " + this.name + " (" + this.role + ")");
            return true;
        } else {
            System.out.println("Login failed: Invalid email or password.");
            return false;
        }
    }

    public void register() {
        System.out.println("User registered successfully: " + this.name + " with email: " + this.email);
    }

    public void updateProfile(String newName, String newPhone) {
        this.name = newName;
        this.phone = newPhone;
        System.out.println("Profile updated for user ID: " + this.userId);
    }

    // Getters and Setters (Encapsulation)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
