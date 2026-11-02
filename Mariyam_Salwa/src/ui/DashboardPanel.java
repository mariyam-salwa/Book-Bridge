package ui;

import data.DataStore;
import models.Material;
import models.Request;
import models.Transaction;
import models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * DashboardPanel - everything about the logged-in student. (Replaces dashboard.html)
 *
 * Tabs:
 *  1. My listings        - materials this student shared
 *  2. Requests received  - other students asking for this student's books (accept / decline / handed over)
 *  3. My requests        - requests this student sent (cancel)
 *  4. Saved              - bookmarked materials
 */
public class DashboardPanel extends BasePanel {

    private static final String[] REQUEST_COLUMNS = {"ID", "Material", "Student", "Date", "Status"};

    private final JTabbedPane tabs = Theme.tabs();

    private final DefaultTableModel listingsModel = Theme.readOnlyModel(MATERIAL_COLUMNS);
    private final DefaultTableModel receivedModel = Theme.readOnlyModel(REQUEST_COLUMNS);
    private final DefaultTableModel sentModel = Theme.readOnlyModel(REQUEST_COLUMNS);
    private final DefaultTableModel savedModel = Theme.readOnlyModel(MATERIAL_COLUMNS);

    private final JTable listingsTable = materialTable(listingsModel);
    private final JTable receivedTable = requestTable(receivedModel);
    private final JTable sentTable = requestTable(sentModel);
    private final JTable savedTable = materialTable(savedModel);

    public DashboardPanel(MainFrame mainFrame, User user) {
        super(mainFrame, user, "My dashboard", "Track your listings, requests and saved materials.");

        // ----- Tab 1: My listings -----
        JButton viewListing = Theme.secondaryButton("View details");
        viewListing.addActionListener(e -> openDetails(listingsTable));
        JButton removeListing = Theme.dangerButton("Remove listing");
        removeListing.addActionListener(e -> removeListing());
        tabs.addTab("My listings", tab(listingsTable, viewListing, removeListing));

        // ----- Tab 2: Requests received -----
        JButton accept = Theme.successButton("Accept");
        accept.addActionListener(e -> acceptRequest());
        JButton decline = Theme.dangerButton("Decline");
        decline.addActionListener(e -> declineRequest());
        JButton handedOver = Theme.primaryButton("Mark as handed over");
        handedOver.addActionListener(e -> markHandedOver());
        tabs.addTab("Requests received", tab(receivedTable, accept, decline, handedOver));

        // ----- Tab 3: My requests -----
        JButton cancel = Theme.dangerButton("Cancel request");
        cancel.addActionListener(e -> cancelRequest());
        tabs.addTab("My requests", tab(sentTable, cancel));

        // ----- Tab 4: Saved -----
        JButton viewSaved = Theme.secondaryButton("View details");
        viewSaved.addActionListener(e -> openDetails(savedTable));
        JButton unsave = Theme.dangerButton("Remove from saved");
        unsave.addActionListener(e -> unsave());
        tabs.addTab("Saved", tab(savedTable, viewSaved, unsave));

        content.add(tabs, BorderLayout.CENTER);
    }

    @Override
    public void refresh() {
        int me = user.getUserId();

        listingsModel.setRowCount(0);
        for (Material m : DataStore.getMaterialsUploadedBy(me)) {
            listingsModel.addRow(materialRow(m));
        }

        receivedModel.setRowCount(0);
        int waiting = 0;
        for (Request r : DataStore.getRequestsReceivedBy(me)) {
            receivedModel.addRow(requestRow(r, DataStore.getUserName(r.getStudentId())));
            if (r.getStatus().equals("PENDING")) waiting++;
        }

        sentModel.setRowCount(0);
        for (Request r : DataStore.getRequestsSentBy(me)) {
            Material m = DataStore.getMaterialById(r.getMaterialId());
            String seller = m == null ? "-" : DataStore.getUserName(m.getUploadedByUserId());
            sentModel.addRow(requestRow(r, seller));
        }

        savedModel.setRowCount(0);
        for (Material m : DataStore.getSavedMaterials(me)) {
            savedModel.addRow(materialRow(m));
        }

        // Show a count on tabs so the student notices new requests
        tabs.setTitleAt(0, "My listings (" + listingsModel.getRowCount() + ")");
        tabs.setTitleAt(1, waiting > 0 ? "Requests received (" + waiting + " new)" : "Requests received");
        tabs.setTitleAt(2, "My requests (" + sentModel.getRowCount() + ")");
        tabs.setTitleAt(3, "Saved (" + savedModel.getRowCount() + ")");
    }

    // ---------- Button actions ----------

    private void removeListing() {
        int id = selectedId(listingsTable);
        if (id < 0) return;
        Material m = DataStore.getMaterialById(id);
        if (confirm("Remove \"" + m.getTitle() + "\" from BookBridge?")) {
            DataStore.removeMaterial(m);
            mainFrame.refreshAll();
        }
    }

    private void acceptRequest() {
        Request r = selectedRequest(receivedTable);
        if (r == null) return;
        if (!r.getStatus().equals("PENDING")) {
            warn("Only PENDING requests can be accepted.");
            return;
        }
        Material m = DataStore.getMaterialById(r.getMaterialId());
        if (!m.getStatus().equals("AVAILABLE")) {
            warn("This item is already reserved or sold.");
            return;
        }
        DataStore.acceptRequest(r);
        info("Accepted. Contact " + DataStore.getUserName(r.getStudentId()) + " to arrange the handover.\n"
                + "Phone: " + DataStore.getUserById(r.getStudentId()).getPhone());
        mainFrame.refreshAll();
    }

    private void declineRequest() {
        Request r = selectedRequest(receivedTable);
        if (r == null) return;
        if (!r.getStatus().equals("PENDING")) {
            warn("Only PENDING requests can be declined.");
            return;
        }
        DataStore.declineRequest(r);
        mainFrame.refreshAll();
    }

    private void markHandedOver() {
        Request r = selectedRequest(receivedTable);
        if (r == null) return;
        Transaction t = DataStore.findTransactionFor(r);
        if (!r.getStatus().equals("ACCEPTED") || t == null) {
            warn("Accept the request first. You can mark it handed over after you meet the buyer.");
            return;
        }
        if (confirm("Confirm that you handed over the item and received the cash payment?")) {
            DataStore.completeTransaction(t);
            info("Done. The item is now marked as SOLD.");
            mainFrame.refreshAll();
        }
    }

    private void cancelRequest() {
        Request r = selectedRequest(sentTable);
        if (r == null) return;
        if (!r.getStatus().equals("PENDING")) {
            warn("Only PENDING requests can be cancelled.");
            return;
        }
        if (confirm("Cancel this request?")) {
            DataStore.cancelRequest(r);
            mainFrame.refreshAll();
        }
    }

    private void unsave() {
        int id = selectedId(savedTable);
        if (id < 0) return;
        DataStore.toggleSaved(user.getUserId(), id);
        mainFrame.refreshAll();
    }

    // ---------- helpers ----------

    private Request selectedRequest(JTable table) {
        int id = selectedId(table);
        return id < 0 ? null : DataStore.getRequestById(id);
    }

    private static Object[] requestRow(Request r, String personName) {
        Material m = DataStore.getMaterialById(r.getMaterialId());
        String title = m == null ? "(removed)" : m.getTitle();
        return new Object[]{r.getRequestId(), title, personName, r.getRequestDate(), r.getStatus()};
    }

    private static JTable requestTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        Theme.styleTable(table);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(320);
        table.getColumnModel().getColumn(4).setMinWidth(150);
        table.getColumnModel().getColumn(4).setMaxWidth(170);
        return table;
    }

    /** Builds one tab: the table on top and a row of buttons underneath. */
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
