package ui;

import data.DataStore;
import models.Material;
import models.PhysicalMaterial;
import models.Request;
import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * HomePanel - the student's start screen. (Replaces index.html)
 *
 * Shows a quick search box, a few numbers about the platform,
 * and the most recently added materials.
 */
public class HomePanel extends BasePanel {

    private final JTextField searchField = Theme.textField(30);
    private final DefaultTableModel recentModel = Theme.readOnlyModel(MATERIAL_COLUMNS);

    // Labels for the numbers row (updated in refresh)
    private final JLabel materialsCount = bigNumber();
    private final JLabel freeCount = bigNumber();
    private final JLabel booksCount = bigNumber();
    private final JLabel requestsCount = bigNumber();

    public HomePanel(MainFrame mainFrame, User user) {
        super(mainFrame, user, "Hello, " + firstName(user.getName()),
                "Search notes and textbooks shared by students in your college.");

        // --- Search row ---
        JButton searchButton = Theme.primaryButton("Search");
        searchButton.addActionListener(e -> mainFrame.openExploreWithSearch(searchField.getText()));
        searchField.addActionListener(e -> mainFrame.openExploreWithSearch(searchField.getText())); // Enter key

        JPanel searchRow = new JPanel(new BorderLayout(10, 0));
        searchRow.setOpaque(false);
        searchRow.add(searchField, BorderLayout.CENTER);
        searchRow.add(searchButton, BorderLayout.EAST);

        // --- Numbers row ---
        JPanel numbers = new JPanel(new GridLayout(1, 4, 14, 0));
        numbers.setOpaque(false);
        numbers.add(numberCard(materialsCount, "materials available"));
        numbers.add(numberCard(freeCount, "free to download"));
        numbers.add(numberCard(booksCount, "textbooks for sale"));
        numbers.add(numberCard(requestsCount, "requests waiting"));

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);
        searchRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        numbers.setAlignmentX(Component.LEFT_ALIGNMENT);
        top.add(searchRow);
        top.add(Box.createVerticalStrut(20));
        top.add(numbers);
        top.add(Box.createVerticalStrut(26));
        JLabel recentTitle = Theme.fieldLabel("Recently added  (double-click a row to open it)");
        recentTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        top.add(recentTitle);
        top.add(Box.createVerticalStrut(8));

        content.add(top, BorderLayout.NORTH);
        content.add(Theme.scroll(materialTable(recentModel)), BorderLayout.CENTER);
    }

    @Override
    public void refresh() {
        List<Material> all = DataStore.search("", "All", "All", "All");

        int available = 0, free = 0, books = 0;
        for (Material m : all) {
            if (m.getStatus().equals("AVAILABLE")) available++;
            if (m.getPrice() == 0 && !(m instanceof PhysicalMaterial)) free++;
            if (m instanceof PhysicalMaterial && m.getStatus().equals("AVAILABLE")) books++;
        }
        int pending = 0;
        for (Request r : DataStore.getRequestsSentBy(user.getUserId())) {
            if (r.getStatus().equals("PENDING")) pending++;
        }
        materialsCount.setText(String.valueOf(available));
        freeCount.setText(String.valueOf(free));
        booksCount.setText(String.valueOf(books));
        requestsCount.setText(String.valueOf(pending));

        // Newest first: the list is in the order items were added, so read it backwards
        recentModel.setRowCount(0);
        for (int i = all.size() - 1; i >= 0 && i >= all.size() - 6; i--) {
            recentModel.addRow(materialRow(all.get(i)));
        }
    }

    // ---------- small helpers ----------

    private static String firstName(String fullName) {
        return fullName.split(" ")[0];
    }

    private static JLabel bigNumber() {
        JLabel l = new JLabel("0");
        l.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 28));
        l.setForeground(Theme.PRIMARY);
        return l;
    }

    private static JPanel numberCard(JLabel number, String caption) {
        JPanel card = Theme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.add(number);
        card.add(Theme.muted(caption));
        return card;
    }
}
