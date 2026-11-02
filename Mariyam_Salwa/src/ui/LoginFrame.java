package ui;

import data.DataStore;
import models.User;

import javax.swing.*;
import java.awt.*;

/**
 * LoginFrame - the first window the user sees. (Replaces login.html)
 *
 * Left side: BookBridge branding. Right side: the login form.
 * The user's role (Student / Administrator) is decided by their account,
 * and the next screen changes depending on that role.
 */
public class LoginFrame extends JFrame {

    private final JTextField emailField = Theme.textField(22);
    private final JPasswordField passwordField = Theme.passwordField(22);
    private JButton loginButton;

    public LoginFrame() {
        setTitle("BookBridge - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 560);
        setMinimumSize(new Dimension(760, 500));
        setLocationRelativeTo(null);
        setIconImage(FlatStyle.appIcon()); // BookBridge icon instead of the Java cup // centre on screen

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.add(BrandPanel.create("Welcome back.",
                "Find notes, question papers and second-hand textbooks shared by students on your campus."));
        root.add(buildForm());
        setContentPane(root);

        // Pressing Enter in the password box = clicking Login
        getRootPane().setDefaultButton(loginButton);
    }

    private JPanel buildForm() {
        JPanel outer = new JPanel(new GridBagLayout()); // centres the form
        outer.setBackground(Theme.BG_CARD);

        FormPanel form = new FormPanel();

        form.addRow(Theme.heading("Log in"), 0);
        form.addRow(Theme.muted("Use your college email address."), 4);
        form.addField("Email", emailField);
        form.addField("Password", passwordField);

        loginButton = Theme.primaryButton("Log in");
        loginButton.addActionListener(e -> handleLogin());
        form.addRow(loginButton, 22);

        JButton registerLink = linkButton("New student? Create an account");
        registerLink.addActionListener(e -> {
            new RegisterFrame(this).setVisible(true);
            setVisible(false);
        });
        form.addRow(registerLink, 12);

        JLabel demo = new JLabel("<html><span style='color:#64748B'>Demo accounts: shamla@college.edu / pass123"
                + "<br>admin@bookbridge.org / adminSecure!</span></html>");
        demo.setFont(Theme.FONT_SMALL);
        form.addRow(demo, 26);
        form.finish();

        outer.add(form);
        return outer;
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Input validation before checking the account
        if (Validator.isEmpty(email) || Validator.isEmpty(password)) {
            showError("Please enter both email and password.");
            return;
        }
        if (!Validator.isValidEmail(email)) {
            showError("That does not look like an email address.");
            return;
        }

        User user = DataStore.login(email, password);
        if (user == null) {
            showError("Wrong email or password. Please try again.");
            passwordField.setText("");
            return;
        }

        // Success: open the main window and close this one
        new MainFrame(user).setVisible(true);
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Login failed", JOptionPane.ERROR_MESSAGE);
    }

    /** A button that looks like a text link. */
    static JButton linkButton(String text) {
        JButton b = new JButton(text);
        b.setFont(Theme.FONT_BODY);
        b.setForeground(Theme.PRIMARY);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(BorderFactory.createEmptyBorder());
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
