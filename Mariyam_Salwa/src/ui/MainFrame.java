package ui;

import data.DataStore;
import models.Administrator;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MainFrame - the main window after login.
 *
 * Layout:
 *   +-----------+--------------------------------+
 *   | sidebar   |  current screen (CardLayout)   |
 *   | (menu)    |  Home / Explore / Upload / ... |
 *   +-----------+--------------------------------+
 *
 * CardLayout stacks all screens on top of each other and shows one at a time,
 * like switching between HTML pages but inside one window.
 * Students and Administrators see different menus (role-based UI).
 */
public class MainFrame extends JFrame {

    private final User user;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel screens = new JPanel(cardLayout);

    // Screen name -> screen. LinkedHashMap keeps the menu in the order we add them.
    private final Map<String, BasePanel> panels = new LinkedHashMap<>();
    private final Map<String, JButton> menuButtons = new LinkedHashMap<>();

    private ExplorePanel explorePanel;

    public MainFrame(User user) {
        this.user = user;
        setTitle("BookBridge - " + user.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 740);
        setMinimumSize(new Dimension(1000, 640));
        setLocationRelativeTo(null);
        setIconImage(FlatStyle.appIcon()); // BookBridge icon instead of the Java cup

        // Create the screens this user is allowed to see
        explorePanel = new ExplorePanel(this, user);
        if (user instanceof Administrator) {
            addScreen("Admin Panel", new AdminPanel(this, user));
            addScreen("Explore", explorePanel);
        } else {
            addScreen("Home", new HomePanel(this, user));
            addScreen("Explore", explorePanel);
            addScreen("Upload / List Book", new UploadPanel(this, user));
            addScreen("My Dashboard", new DashboardPanel(this, user));
        }

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(screens, BorderLayout.CENTER);
        setContentPane(root);

        // Open the first screen in the menu
        showScreen(panels.keySet().iterator().next());
    }

    private void addScreen(String name, BasePanel panel) {
        panels.put(name, panel);
        screens.add(panel, name);
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(Theme.PRIMARY);
        side.setPreferredSize(new Dimension(230, 0));
        side.setBorder(BorderFactory.createEmptyBorder(26, 16, 22, 16));

        JLabel logo = new JLabel("BookBridge");
        logo.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 22));
        logo.setForeground(Color.WHITE);
        logo.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        side.add(logo);

        // Small amber stripe under the logo
        JPanel stripe = new JPanel();
        stripe.setBackground(Theme.ACCENT);
        stripe.setPreferredSize(new Dimension(40, 4));
        JPanel stripeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        stripeRow.setOpaque(false);
        stripeRow.setMaximumSize(new Dimension(230, 14));
        stripeRow.add(stripe);
        side.add(Box.createVerticalStrut(8));
        side.add(stripeRow);
        side.add(Box.createVerticalStrut(30));

        // One menu button per screen
        for (String name : panels.keySet()) {
            JButton b = menuButton(name);
            b.addActionListener(e -> showScreen(name));
            menuButtons.put(name, b);
            side.add(b);
            side.add(Box.createVerticalStrut(4));
        }

        side.add(Box.createVerticalGlue());

        // Who is logged in + logout
        String role = (user instanceof Administrator) ? "Administrator" : "Student";
        JLabel who = new JLabel("<html><b>" + user.getName() + "</b><br>" + role + "</html>");
        who.setForeground(new Color(0xDBEAFE));
        who.setFont(Theme.FONT_BODY);
        who.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        side.add(who);

        JButton logout = menuButton("Log out");
        logout.addActionListener(e -> logout());
        side.add(logout);
        return side;
    }

    private JButton menuButton(String text) {
        JButton b = new JButton(text);
        b.setFont(Theme.FONT_BOLD);
        b.setForeground(Color.WHITE);
        b.setBackground(Theme.PRIMARY);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    /** Switches to a screen and reloads its data. */
    public void showScreen(String name) {
        BasePanel panel = panels.get(name);
        if (panel == null) return;
        panel.refresh(); // POLYMORPHISM: each screen's own refresh() runs
        cardLayout.show(screens, name);

        // Highlight the active menu item
        for (Map.Entry<String, JButton> entry : menuButtons.entrySet()) {
            boolean active = entry.getKey().equals(name);
            entry.getValue().setBackground(active ? Theme.PRIMARY_DARK : Theme.PRIMARY);
        }
    }

    /** Used by the Home screen's search box: jump to Explore with a keyword. */
    public void openExploreWithSearch(String keyword) {
        explorePanel.setKeyword(keyword);
        showScreen("Explore");
    }

    /** Called by dialogs after something changed, so the visible screen shows new data. */
    public void refreshAll() {
        for (BasePanel p : panels.values()) {
            p.refresh();
        }
    }

    private void logout() {
        if (JOptionPane.showConfirmDialog(this, "Log out of BookBridge?", "Log out",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            DataStore.logout();
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}
