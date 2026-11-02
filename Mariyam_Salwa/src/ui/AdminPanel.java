package ui;

import data.DataStore;
import models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * AdminPanel - the administrator's control screen. (Replaces admin.html)
 *
 * Tabs:
 *  1. Students      - view and remove student accounts
 *  2. Materials     - remove or restore any listing (moderation)
 *  3. Transactions  - audit handovers and close them if needed
 */
public class AdminPanel extends BasePanel {

    private static final String[] STUDENT_COLUMNS = {"ID", "Name", "Email", "Student ID", "Department", "Sem", "Listings"};
    private static final String[] TX_COLUMNS = {"ID", "Material", "Buyer", "Seller", "Payment", "Status"};

    private final Administrator admin;
    private final JTabbedPane tabs = Theme.tabs();

    private final DefaultTableModel studentModel = Theme.readOnlyModel(STUDENT_COLUMNS);
    private final DefaultTableModel materialModel = Theme.readOnlyModel(MATERIAL_COLUMNS);
    private final DefaultTableModel txModel = Theme.readOnlyModel(TX_COLUMNS);

    private final JTable studentTable = new JTable(studentModel);
    private final JTable materialTable = materialTable(materialModel);
    private final JTable txTable = new JTable(txModel);

    public AdminPanel(MainFrame mainFrame, User user) {
        super(mainFrame, user, "Administrator panel", "Manage students, moderate listings and audit handovers.");
        this.admin = (Administrator) user;
        Theme.styleTable(studentTable);
        Theme.styleTable(txTable);
        studentTable.getColumnModel().getColumn(0).setMaxWidth(60);
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(230);
        studentTable.getColumnModel().getColumn(4).setMinWidth(150);
        studentTable.getColumnModel().getColumn(5).setMaxWidth(90);
        studentTable.getColumnModel().getColumn(6).setMaxWidth(80);
        txTable.getColumnModel().getColumn(0).setMaxWidth(60);
        txTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        txTable.getColumnModel().getColumn(5).setMinWidth(170);

        JButton removeStudent = Theme.dangerButton("Remove student");
        removeStudent.addActionListener(e -> removeStudent());
        tabs.addTab("Students", tab(studentTable, removeStudent));

        JButton view = Theme.secondaryButton("View details");
        view.addActionListener(e -> openDetails(materialTable));
        JButton restore = Theme.successButton("Restore");
        restore.addActionListener(e -> restoreMaterial());
        JButton remove = Theme.dangerButton("Remove listing");
        remove.addActionListener(e -> removeMaterial());
        tabs.addTab("Materials", tab(materialTable, view, restore, remove));

        JButton complete = Theme.primaryButton("Mark completed");
        complete.addActionListener(e -> completeTransaction());
        tabs.addTab("Transactions", tab(txTable, complete));

        content.add(tabs, BorderLayout.CENTER);
    }

    @Override
    public void refresh() {
        studentModel.setRowCount(0);
        for (Student s : DataStore.getStudents()) {
            studentModel.addRow(new Object[]{s.getUserId(), s.getName(), s.getEmail(), s.getStudentId(),
                    s.getDepartment(), s.getSemester(), DataStore.getMaterialsUploadedBy(s.getUserId()).size()});
        }

        // Admin sees ALL materials, including removed ones
        materialModel.setRowCount(0);
        for (Material m : DataStore.getAllMaterials()) {
            materialModel.addRow(materialRow(m));
        }

        txModel.setRowCount(0);
        for (Transaction t : DataStore.getAllTransactions()) {
            Material m = DataStore.getMaterialById(t.getMaterialId());
            txModel.addRow(new Object[]{t.getTransactionId(), m == null ? "-" : m.getTitle(),
                    DataStore.getUserName(t.getBuyerId()), DataStore.getUserName(t.getSellerId()),
                    t.getPaymentType().replace('_', ' '), t.getStatus()});
        }

        tabs.setTitleAt(0, "Students (" + studentModel.getRowCount() + ")");
        tabs.setTitleAt(1, "Materials (" + materialModel.getRowCount() + ")");
        tabs.setTitleAt(2, "Transactions (" + txModel.getRowCount() + ")");
    }

    // ---------- Button actions ----------

    private void removeStudent() {
        int id = selectedId(studentTable);
        if (id < 0) return;
        User s = DataStore.getUserById(id);
        if (confirm("Remove " + s.getName() + "? Their listings will also be hidden.")) {
            admin.manageUsers(s, "REMOVE"); // Member 2's Administrator method (logs the action)
            DataStore.removeUser(s);
            refresh();
        }
    }

    private void removeMaterial() {
        int id = selectedId(materialTable);
        if (id < 0) return;
        Material m = DataStore.getMaterialById(id);
        if (m.getStatus().equals("REMOVED")) {
            warn("This listing is already removed.");
            return;
        }
        if (confirm("Remove \"" + m.getTitle() + "\" from BookBridge?")) {
            admin.manageMaterials(m, "REMOVE");
            DataStore.removeMaterial(m);
            refresh();
        }
    }

    private void restoreMaterial() {
        int id = selectedId(materialTable);
        if (id < 0) return;
        Material m = DataStore.getMaterialById(id);
        if (!m.getStatus().equals("REMOVED")) {
            warn("Only removed listings can be restored.");
            return;
        }
        admin.manageMaterials(m, "RESTORE");
        DataStore.restoreMaterial(m);
        refresh();
    }

    private void completeTransaction() {
        int id = selectedId(txTable);
        if (id < 0) return;
        Transaction t = DataStore.getTransactionById(id);
        if (!t.getStatus().equals("PENDING_HANDOVER")) {
            warn("This transaction is already closed.");
            return;
        }
        if (confirm("Mark transaction #" + id + " as completed?")) {
            DataStore.completeTransaction(t);
            refresh();
        }
    }

    // ---------- helper ----------

    private static JPanel tab(JTable table, JButton... buttons) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        for (JButton b : buttons) bar.add(b);

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG_MAIN);
        p.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));
        p.add(Theme.scroll(table), BorderLayout.CENTER);
        p.add(bar, BorderLayout.SOUTH);
        return p;
    }
}
