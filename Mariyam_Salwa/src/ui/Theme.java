package ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Theme - the BookBridge design system for Swing.
 *
 * This class replaces the old style.css / components.css files.
 * Every colour, font and "styled component" is defined in ONE place,
 * so all screens look the same. (Same idea as CSS variables in :root.)
 *
 * All members are static, so screens use it like: Theme.PRIMARY, Theme.primaryButton("Save")
 */
public class Theme {

    // ---------- Colour palette (same values as the old CSS :root variables) ----------
    public static final Color PRIMARY       = new Color(0x1E3A8A); // deep academic blue
    public static final Color PRIMARY_DARK  = new Color(0x172554);
    public static final Color PRIMARY_LIGHT = new Color(0xEFF6FF);
    public static final Color SECONDARY     = new Color(0x0D9488); // teal
    public static final Color ACCENT        = new Color(0xF59E0B); // amber (prices)
    public static final Color SUCCESS       = new Color(0x059669); // available / free
    public static final Color DANGER        = new Color(0xDC2626); // sold / removed / errors
    public static final Color TEXT_MAIN     = new Color(0x0F172A);
    public static final Color TEXT_MUTED    = new Color(0x64748B);
    public static final Color BORDER        = new Color(0xE2E8F0);
    public static final Color BG_MAIN       = new Color(0xF8FAFC);
    public static final Color BG_CARD       = Color.WHITE;

    // ---------- Fonts ----------
    // Use Segoe UI on Windows if present, otherwise Java's default sans-serif font.
    public static final String FONT_NAME = pickFont();
    public static final Font FONT_BODY    = new Font(FONT_NAME, Font.PLAIN, 14);
    public static final Font FONT_SMALL   = new Font(FONT_NAME, Font.PLAIN, 12);
    public static final Font FONT_BOLD    = new Font(FONT_NAME, Font.BOLD, 14);
    public static final Font FONT_HEADING = new Font(FONT_NAME, Font.BOLD, 22);
    public static final Font FONT_TITLE   = new Font(FONT_NAME, Font.BOLD, 30);

    private static String pickFont() {
        String[] available = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String name : available) {
            if (name.equals("Segoe UI")) {
                return "Segoe UI";
            }
        }
        return Font.SANS_SERIF;
    }

    /** Called once at start-up: applies our fonts to every standard Swing component. */
    public static void applyGlobalDefaults() {
        String[] keys = {"Label.font", "Button.font", "TextField.font", "PasswordField.font",
                "TextArea.font", "ComboBox.font", "Table.font", "TableHeader.font",
                "TabbedPane.font", "OptionPane.messageFont", "OptionPane.buttonFont",
                "RadioButton.font", "CheckBox.font", "List.font"};
        for (String key : keys) {
            UIManager.put(key, FONT_BODY);
        }
        UIManager.put("Panel.background", BG_MAIN);
        UIManager.put("OptionPane.background", BG_CARD);
        UIManager.put("TabbedPane.selected", BG_CARD);
    }

    // ---------- Buttons ----------

    /** Main action button (blue). */
    public static JButton primaryButton(String text) {
        return makeButton(text, PRIMARY, Color.WHITE, PRIMARY_DARK);
    }

    /** Secondary action button (white with blue text). */
    public static JButton secondaryButton(String text) {
        JButton b = makeButton(text, BG_CARD, PRIMARY, PRIMARY_LIGHT);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        b.setBorderPainted(true);
        return b;
    }

    /** Destructive action button (red), e.g. Remove, Decline. */
    public static JButton dangerButton(String text) {
        return makeButton(text, DANGER, Color.WHITE, new Color(0xB91C1C));
    }

    /** Teal button, used for positive actions like Accept / Complete. */
    public static JButton successButton(String text) {
        return makeButton(text, SECONDARY, Color.WHITE, new Color(0x0F766E));
    }

    // Shared code for all buttons (avoids repeating the same 8 lines four times)
    private static JButton makeButton(String text, Color bg, Color fg, Color hoverBg) {
        JButton b = new JButton(text);
        b.setFont(FONT_BOLD);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Hover effect: change colour when the mouse is over the button
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (b.isEnabled()) b.setBackground(hoverBg);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                b.setBackground(bg);
            }
        });
        return b;
    }

    // ---------- Text ----------

    public static JLabel heading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_HEADING);
        l.setForeground(TEXT_MAIN);
        return l;
    }

    public static JLabel muted(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_BODY);
        l.setForeground(TEXT_MUTED);
        return l;
    }

    /**
     * Text that wraps onto several lines to fit the space it is given.
     * (A read-only JTextArea that looks like a label.)
     */
    public static JTextArea wrappingText(String text, Font font, Color color) {
        JTextArea a = new JTextArea(text);
        a.setFont(font);
        a.setForeground(color);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setEditable(false);
        a.setFocusable(false);
        a.setOpaque(false);
        a.setBorder(BorderFactory.createEmptyBorder());
        return a;
    }

    public static JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_BOLD);
        l.setForeground(TEXT_MAIN);
        return l;
    }

    // ---------- Inputs ----------

    private static final Border FIELD_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(7, 9, 7, 9));

    public static JTextField textField(int columns) {
        JTextField f = new JTextField(columns);
        f.setBorder(FIELD_BORDER);
        return f;
    }

    public static JPasswordField passwordField(int columns) {
        JPasswordField f = new JPasswordField(columns);
        f.setBorder(FIELD_BORDER);
        return f;
    }

    public static JTextArea textArea(int rows, int columns) {
        JTextArea a = new JTextArea(rows, columns);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBorder(BorderFactory.createEmptyBorder(7, 9, 7, 9));
        return a;
    }

    public static <T> JComboBox<T> comboBox(T[] items) {
        JComboBox<T> c = new JComboBox<>(items);
        FlatStyle.styleCombo(c); // modern flat look instead of Metal's 3D arrow
        // Same height as text fields, so form rows line up
        c.setPreferredSize(new Dimension(c.getPreferredSize().width, 34));
        c.setMinimumSize(c.getPreferredSize());
        return c;
    }

    // ---------- Containers ----------

    /** Tabs with the flat underline style (see FlatStyle). */
    public static JTabbedPane tabs() {
        JTabbedPane t = new JTabbedPane();
        t.setUI(new FlatStyle.FlatTabsUI());
        t.setOpaque(false);
        return t;
    }

    /** A white panel with a thin border and padding (like a CSS "card"). */
    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        return p;
    }

    // ---------- Tables ----------

    /** Gives every JTable in the app the same clean look and colours the Status column. */
    public static void styleTable(JTable table) {
        table.setRowHeight(32);
        table.setShowVerticalLines(false);
        table.setGridColor(BORDER);
        table.setSelectionBackground(PRIMARY_LIGHT);
        table.setSelectionForeground(TEXT_MAIN);
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true); // click a column header to sort
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(BG_MAIN);
        header.setForeground(TEXT_MUTED);
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new FlatStyle.FlatHeaderRenderer());

        table.setDefaultRenderer(Object.class, new StatusAwareRenderer());
    }

    /** Puts a table inside a scroll pane with a clean border. */
    public static JScrollPane scroll(JComponent c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(BorderFactory.createLineBorder(BORDER));
        sp.getViewport().setBackground(BG_CARD);
        FlatStyle.styleScrollPane(sp);
        return sp;
    }

    /** Returns the colour used for a status word, e.g. AVAILABLE = green. */
    public static Color statusColor(String status) {
        if (status == null) return TEXT_MAIN;
        switch (status) {
            case "AVAILABLE": case "ACCEPTED": case "COMPLETED": case "FREE":
                return SUCCESS;
            case "PENDING": case "RESERVED": case "PENDING_HANDOVER":
                return new Color(0xB45309); // dark amber, readable on white
            case "SOLD": case "REMOVED": case "DECLINED": case "CANCELLED":
                return DANGER;
            default:
                return TEXT_MAIN;
        }
    }

    /**
     * Custom cell renderer: draws status words (AVAILABLE, PENDING, SOLD...) in colour.
     * OOP: this class INHERITS from Swing's DefaultTableCellRenderer and OVERRIDES one method.
     */
    private static class StatusAwareRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            String text = value == null ? "" : value.toString();
            Color c = statusColor(text);
            setForeground(c);
            setFont(c.equals(TEXT_MAIN) ? FONT_BODY : FONT_BOLD);
            return this;
        }
    }

    /** Makes a table model whose cells cannot be edited by typing in them. */
    public static javax.swing.table.DefaultTableModel readOnlyModel(String[] columns) {
        return new javax.swing.table.DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    /** Formats a price for display: 0 becomes "Free". */
    public static String price(double amount) {
        if (amount <= 0) return "Free";
        return "Rs. " + String.format("%.0f", amount);
    }
}
