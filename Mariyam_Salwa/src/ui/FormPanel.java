package ui;

import javax.swing.*;
import java.awt.*;

/**
 * FormPanel - a reusable panel that lays out forms as "label above field" rows.
 *
 * Used by Login, Register and Upload screens, so the layout code is written once.
 * OOP: FormPanel INHERITS from JPanel, so it can be used anywhere a JPanel can.
 */
public class FormPanel extends JPanel {

    private final GridBagConstraints gbc = new GridBagConstraints();
    private int row = 0;

    public FormPanel() {
        setLayout(new GridBagLayout());
        setOpaque(false);
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
    }

    /** Adds a bold label with the input field underneath it. */
    public void addField(String label, JComponent field) {
        gbc.gridy = row++;
        gbc.insets = new Insets(10, 0, 4, 0);
        add(Theme.fieldLabel(label), gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(field, gbc);
    }

    /** Adds any component (a button, a hint text...) as a full-width row. */
    public void addRow(JComponent component, int topGap) {
        gbc.gridy = row++;
        gbc.insets = new Insets(topGap, 0, 0, 0);
        add(component, gbc);
    }

    /** Adds empty space at the bottom so rows stay at the top of the panel. */
    public void finish() {
        gbc.gridy = row++;
        gbc.weighty = 1;
        add(Box.createVerticalGlue(), gbc);
        gbc.weighty = 0;
    }
}
