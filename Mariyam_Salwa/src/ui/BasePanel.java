package ui;

import models.DigitalMaterial;
import models.Material;
import models.PhysicalMaterial;
import models.User;

import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import java.awt.*;

/**
 * BasePanel - parent class of every screen inside the main window
 * (Home, Explore, Upload, Dashboard, Admin).
 *
 * OOP concepts shown here:
 *  - ABSTRACTION: refresh() is abstract, every screen must say how it reloads its data.
 *  - INHERITANCE: all screens extend BasePanel and get the title area + user for free.
 *  - POLYMORPHISM: MainFrame calls refresh() on a BasePanel without knowing which screen it is.
 */
public abstract class BasePanel extends JPanel {

    protected final User user;          // the logged-in user
    protected final MainFrame mainFrame; // lets a screen ask to open another screen
    protected final JPanel content;      // each screen puts its own components in here

    public BasePanel(MainFrame mainFrame, User user, String title, String subtitle) {
        this.mainFrame = mainFrame;
        this.user = user;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(28, 32, 28, 32));

        // Title area shared by all screens
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.add(Theme.heading(title));
        header.add(Box.createVerticalStrut(4));
        header.add(Theme.muted(subtitle));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        add(content, BorderLayout.CENTER);
    }

    /** Reload data from the DataStore. Called every time the screen is opened. */
    public abstract void refresh();

    /** Small helper for showing a message box. */
    protected void info(String message) {
        JOptionPane.showMessageDialog(this, message, "BookBridge", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void warn(String message) {
        JOptionPane.showMessageDialog(this, message, "BookBridge", JOptionPane.WARNING_MESSAGE);
    }

    protected boolean confirm(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Please confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    /** Returns the ID in column 0 of the selected table row, or -1 if nothing is selected. */
    protected int selectedId(JTable table) {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            warn("Please select a row in the table first.");
            return -1;
        }
        int modelRow = table.convertRowIndexToModel(viewRow); // needed because tables can be sorted
        return (Integer) table.getModel().getValueAt(modelRow, 0);
    }

    // ---------- Shared material table (used by Home, Explore, Dashboard, Admin) ----------

    protected static final String[] MATERIAL_COLUMNS = {"ID", "Title", "Subject", "Type", "Price", "Status"};

    /**
     * Turns one Material into a table row.
     * POLYMORPHISM in the UI: digital and physical materials show different "Type" text.
     */
    protected static Object[] materialRow(Material m) {
        String type;
        if (m instanceof DigitalMaterial) {
            type = "Digital (" + ((DigitalMaterial) m).getFileType() + ")";
        } else {
            type = "Book (" + ((PhysicalMaterial) m).getCondition().replace('_', ' ') + ")";
        }
        return new Object[]{m.getMaterialId(), m.getTitle(), m.getSubject(), type,
                Theme.price(m.getPrice()), m.getStatus()};
    }

    /** Creates a styled table for materials. Double-clicking a row opens its details. */
    protected JTable materialTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        Theme.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(280);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setMinWidth(150);
        table.getColumnModel().getColumn(4).setMaxWidth(100);
        table.getColumnModel().getColumn(5).setMinWidth(150);
        table.getColumnModel().getColumn(5).setMaxWidth(170);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() >= 0) {
                    openDetails(table);
                }
            }
        });
        return table;
    }

    /** Opens the details window for the selected material in a table. */
    protected void openDetails(JTable table) {
        int id = selectedId(table);
        if (id < 0) return;
        MaterialDetailDialog.open(mainFrame, user, data.DataStore.getMaterialById(id));
    }
}
