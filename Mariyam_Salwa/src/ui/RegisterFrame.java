package ui;

import data.DataStore;

import javax.swing.*;
import java.awt.*;

/**
 * RegisterFrame - lets a new student create an account. (Replaces register.html)
 *
 * Every field is validated before the account is created.
 */
public class RegisterFrame extends JFrame {

    private final JFrame loginWindow; // so we can go back to it

    private final JTextField nameField = Theme.textField(16);
    private final JTextField emailField = Theme.textField(16);
    private final JTextField phoneField = Theme.textField(16);
    private final JTextField studentIdField = Theme.textField(16);
    private final JComboBox<String> departmentBox = Theme.comboBox(DataStore.DEPARTMENTS);
    private final JComboBox<Integer> semesterBox = Theme.comboBox(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8});
    private final JPasswordField passwordField = Theme.passwordField(16);
    private final JPasswordField confirmField = Theme.passwordField(16);

    public RegisterFrame(JFrame loginWindow) {
        this.loginWindow = loginWindow;
        setTitle("BookBridge - Create account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 680);
        setMinimumSize(new Dimension(860, 620));
        setLocationRelativeTo(null);
        setIconImage(FlatStyle.appIcon()); // BookBridge icon instead of the Java cup

        JPanel root = new JPanel(new BorderLayout());
        BrandPanel brand = BrandPanel.create("Join BookBridge.",
                "One account lets you download free notes, list your old textbooks and request books from seniors.");
        brand.setPreferredSize(new Dimension(380, 0));
        root.add(brand, BorderLayout.WEST);
        root.add(buildForm(), BorderLayout.CENTER);
        setContentPane(root);
    }

    private JComponent buildForm() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(Theme.BG_CARD);

        // Two columns of fields side by side
        FormPanel left = new FormPanel();
        left.addField("Full name", nameField);
        left.addField("College email", emailField);
        left.addField("Phone (10 digits)", phoneField);
        left.addField("Password (min. 6 characters)", passwordField);
        left.finish();

        FormPanel right = new FormPanel();
        right.addField("Student ID", studentIdField);
        right.addField("Department", departmentBox);
        right.addField("Semester", semesterBox);
        right.addField("Confirm password", confirmField);
        right.finish();

        JPanel columns = new JPanel(new GridLayout(1, 2, 24, 0));
        columns.setOpaque(false);
        columns.add(left);
        columns.add(right);

        JButton createButton = Theme.primaryButton("Create account");
        createButton.addActionListener(e -> handleRegister());
        JButton backButton = LoginFrame.linkButton("Already registered? Log in");
        backButton.addActionListener(e -> goBackToLogin());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttons.setOpaque(false);
        buttons.add(createButton);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(backButton);

        FormPanel page = new FormPanel();
        page.addRow(Theme.heading("Create your student account"), 0);
        page.addRow(Theme.muted("Administrator accounts are created by the college, not here."), 4);
        page.addRow(columns, 8);
        page.addRow(buttons, 26);
        page.finish();

        outer.add(page);
        return outer;
    }

    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String studentId = studentIdField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        // Check every field; stop at the first problem and tell the user what to fix
        String error = null;
        if (!Validator.isValidName(name)) {
            error = "Name must be 3-50 letters (spaces and dots allowed).";
        } else if (!Validator.isValidEmail(email)) {
            error = "Please enter a valid email address.";
        } else if (DataStore.isEmailTaken(email)) {
            error = "An account with this email already exists.";
        } else if (!Validator.isValidPhone(phone)) {
            error = "Phone number must be 10 digits and start with 6, 7, 8 or 9.";
        } else if (Validator.isEmpty(studentId)) {
            error = "Please enter your student ID.";
        } else if (!Validator.isStrongEnoughPassword(password)) {
            error = "Password must be at least 6 characters.";
        } else if (!password.equals(confirm)) {
            error = "The two passwords do not match.";
        }

        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Please check the form", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DataStore.registerStudent(name, email, phone, password, studentId,
                (String) departmentBox.getSelectedItem(), (Integer) semesterBox.getSelectedItem());
        JOptionPane.showMessageDialog(this, "Account created. You can log in now.",
                "Registration successful", JOptionPane.INFORMATION_MESSAGE);
        goBackToLogin();
    }

    private void goBackToLogin() {
        loginWindow.setVisible(true);
        dispose();
    }
}
