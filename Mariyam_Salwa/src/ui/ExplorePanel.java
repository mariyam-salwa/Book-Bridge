package ui;

import data.DataStore;
import models.Material;
import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * ExplorePanel - search and filter all materials. (Replaces explore.html)
 *
 * Filters: keyword, type (digital/physical), price (free/paid) and subject.
 * Results appear in a table; double-click or "View details" opens a material.
 */
public class ExplorePanel extends BasePanel {

    private final JTextField keywordField = Theme.textField(22);
    private final JComboBox<String> typeBox = Theme.comboBox(new String[]{"All", "Digital", "Physical"});
    private final JComboBox<String> priceBox = Theme.comboBox(new String[]{"All", "Free", "Paid"});
    private final JComboBox<String> subjectBox;
    private final JLabel resultCount = Theme.muted("");
    private final DefaultTableModel model = Theme.readOnlyModel(MATERIAL_COLUMNS);
    private final JTable table;

    public ExplorePanel(MainFrame mainFrame, User user) {
        super(mainFrame, user, "Explore materials", "Notes, question papers and textbooks from every department.");

        // Subject list = "All" + the subjects from DataStore
        String[] subjects = new String[DataStore.SUBJECTS.length + 1];
        subjects[0] = "All";
        System.arraycopy(DataStore.SUBJECTS, 0, subjects, 1, DataStore.SUBJECTS.length);
        subjectBox = Theme.comboBox(subjects);

        // --- Filter bar ---
        JButton searchButton = Theme.primaryButton("Search");
        JButton clearButton = Theme.secondaryButton("Clear");
        searchButton.addActionListener(e -> refresh());
        keywordField.addActionListener(e -> refresh()); // Enter key
        clearButton.addActionListener(e -> clearFilters());
        // Changing a drop-down filters immediately
        typeBox.addActionListener(e -> refresh());
        priceBox.addActionListener(e -> refresh());
        subjectBox.addActionListener(e -> refresh());

        // Row 1: keyword box (stretches) + buttons
        JPanel row1 = new JPanel(new BorderLayout(10, 0));
        row1.setOpaque(false);
        row1.add(keywordField, BorderLayout.CENTER);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(searchButton);
        buttons.add(clearButton);
        row1.add(buttons, BorderLayout.EAST);

        // Row 2: the three drop-down filters
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row2.setOpaque(false);
        row2.add(Theme.fieldLabel("Type  "));
        row2.add(typeBox);
        row2.add(Box.createHorizontalStrut(22));
        row2.add(Theme.fieldLabel("Price  "));
        row2.add(priceBox);
        row2.add(Box.createHorizontalStrut(22));
        row2.add(Theme.fieldLabel("Subject  "));
        row2.add(subjectBox);

        JPanel filters = Theme.card();
        filters.setLayout(new BorderLayout(0, 12));
        filters.add(row1, BorderLayout.NORTH);
        filters.add(row2, BorderLayout.SOUTH);

        // --- Results ---
        table = materialTable(model);

        JButton detailsButton = Theme.primaryButton("View details");
        detailsButton.addActionListener(e -> openDetails(table));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        bottom.add(resultCount, BorderLayout.WEST);
        bottom.add(detailsButton, BorderLayout.EAST);

        JPanel tableArea = new JPanel(new BorderLayout());
        tableArea.setOpaque(false);
        tableArea.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
        tableArea.add(Theme.scroll(table), BorderLayout.CENTER);
        tableArea.add(bottom, BorderLayout.SOUTH);

        content.add(filters, BorderLayout.NORTH);
        content.add(tableArea, BorderLayout.CENTER);
    }

    /** Used when the Home screen sends a search keyword. */
    public void setKeyword(String keyword) {
        keywordField.setText(keyword);
    }

    private void clearFilters() {
        keywordField.setText("");
        typeBox.setSelectedIndex(0);
        priceBox.setSelectedIndex(0);
        subjectBox.setSelectedIndex(0);
        refresh();
    }

    @Override
    public void refresh() {
        List<Material> results = DataStore.search(keywordField.getText(),
                (String) typeBox.getSelectedItem(), (String) priceBox.getSelectedItem(),
                (String) subjectBox.getSelectedItem());

        model.setRowCount(0); // clear old rows
        for (Material m : results) {
            model.addRow(materialRow(m));
        }
        resultCount.setText(results.isEmpty()
                ? "No materials match these filters."
                : results.size() + " result" + (results.size() == 1 ? "" : "s"));
    }
}
