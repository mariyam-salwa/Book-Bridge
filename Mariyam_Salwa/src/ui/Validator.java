package ui;

/**
 * Validator - checks what the user typed before we use it.
 * (Replaces the old js/validation.js file.)
 *
 * Each method returns true if the value is acceptable.
 */
public class Validator {

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /** Simple email check: something@something.something */
    public static boolean isValidEmail(String email) {
        return email != null && email.trim().matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    }

    /** Indian mobile number: exactly 10 digits, starting with 6, 7, 8 or 9. */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.trim().matches("^[6-9]\\d{9}$");
    }

    /** Names may contain letters, spaces and dots only, at least 3 characters. */
    public static boolean isValidName(String name) {
        return name != null && name.trim().matches("^[A-Za-z. ]{3,50}$");
    }

    public static boolean isStrongEnoughPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Reads a price typed by the user.
     * @return the price, or -1 if it is not a valid number from 0 to 10000
     */
    public static double parsePrice(String text) {
        if (isEmpty(text)) return 0; // empty price means free
        try {
            double value = Double.parseDouble(text.trim());
            if (value < 0 || value > 10000) return -1;
            return value;
        } catch (NumberFormatException e) {
            return -1; // user typed letters, e.g. "abc"
        }
    }
}
