package ui;

import data.DataStore;
import models.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * MaterialDetailDialog - popup window showing one material. (Replaces material-detail.html)
 *
 * The window changes depending on the kind of material (POLYMORPHISM in the UI):
 *  - DigitalMaterial  -> shows file details and a Download / Request access button
 *  - PhysicalMaterial -> shows book condition and a Request handover button (cash on meeting)
 */
public class MaterialDetailDialog extends JDialog {

    private final MainFrame mainFrame;
    private final User user;
    private final Material material;

    /** Convenience method so other screens can open this with one line. */
    public static void open(MainFrame mainFrame, User user, Material material) {
        if (material == null) return;
        new MaterialDetailDialog(mainFrame, user, material).setVisible(true);
    }

    private MaterialDetailDialog(MainFrame mainFrame, User user, Material material) {
        super(mainFrame, "Material details", true); // true = modal (blocks the main window)
        this.mainFrame = mainFrame;
        this.user = user;
        this.material = material;
        setSize(640, 580);
        setLocationRelativeTo(mainFrame);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_CARD);
        root.setBorder(BorderFactory.createEmptyBorder(26, 30, 22, 30));
        root.add(buildInfo(), BorderLayout.CENTER);
        root.add(buildButtons(), BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JPanel buildInfo() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        boolean digital = material instanceof DigitalMaterial;
        JLabel kind = new JLabel(digital ? "Digital resource" : "Physical textbook  -  cash on handover");
        kind.setFont(Theme.FONT_BOLD);
        kind.setForeground(digital ? Theme.SECONDARY : new Color(0xB45309));

        JTextArea title = Theme.wrappingText(material.getTitle(), Theme.FONT_HEADING, Theme.TEXT_MAIN);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64)); // room for 2 lines, no more

        JLabel price = new JLabel(Theme.price(material.getPrice()));
        price.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 24));
        price.setForeground(material.getPrice() > 0 ? new Color(0xB45309) : Theme.SUCCESS);

        // Details grid: label on the left, value on the right
        JPanel grid = new JPanel(new GridLayout(0, 2, 10, 8));
        grid.setOpaque(false);
        addDetail(grid, "Subject", material.getSubject());
        addDetail(grid, "Shared by", DataStore.getUserName(material.getUploadedByUserId()));
        JLabel status = addDetail(grid, "Status", material.getStatus());
        status.setForeground(Theme.statusColor(material.getStatus()));
        status.setFont(Theme.FONT_BOLD);

        // Type-specific details
        if (material instanceof DigitalMaterial) {
            DigitalMaterial dm = (DigitalMaterial) material;
            addDetail(grid, "File", dm.getFileName());
            addDetail(grid, "Format / size", dm.getFileType() + ", " + dm.getFileSize() + " MB");
        } else if (material instanceof PhysicalMaterial) {
            PhysicalMaterial pm = (PhysicalMaterial) material;
            addDetail(grid, "Condition", pm.getCondition().replace('_', ' '));
            addDetail(grid, "Payment", "Cash, paid when you meet the seller");
        }

        JTextArea description = Theme.textArea(4, 40);
        description.setText(material.getDescription());
        description.setEditable(false);
        description.setBackground(Theme.BG_MAIN);

        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, grid.getPreferredSize().height));
        for (JComponent c : new JComponent[]{kind, title, price, grid}) {
            c.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        p.add(kind);
        p.add(Box.createVerticalStrut(6));
        p.add(title);
        p.add(Box.createVerticalStrut(8));
        p.add(price);
        p.add(Box.createVerticalStrut(18));
        p.add(grid);
        p.add(Box.createVerticalStrut(18));
        JLabel descLabel = Theme.fieldLabel("Description");
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(descLabel);
        p.add(Box.createVerticalStrut(6));
        JScrollPane descScroll = Theme.scroll(description);
        descScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(descScroll);
        return p;
    }

    private JLabel addDetail(JPanel grid, String label, String value) {
        grid.add(Theme.muted(label));
        JLabel v = new JLabel(value);
        v.setFont(Theme.FONT_BODY);
        v.setForeground(Theme.TEXT_MAIN);
        grid.add(v);
        return v;
    }

    private JPanel buildButtons() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));

        JButton close = Theme.secondaryButton("Close");
        close.addActionListener(e -> dispose());

        // Administrators only look at listings here; they manage them in the Admin Panel
        if (user instanceof Administrator) {
            bar.add(close);
            return bar;
        }

        boolean mine = material.getUploadedByUserId() == user.getUserId();
        boolean available = material.getStatus().equals("AVAILABLE");

        JButton save = Theme.secondaryButton(savedText());
        save.addActionListener(e -> {
            DataStore.toggleSaved(user.getUserId(), material.getMaterialId());
            save.setText(savedText());
            mainFrame.refreshAll();
        });
        bar.add(save);

              if (mine) {
            JLabel note = Theme.muted("This is your listing.   ");
            bar.add(note, 0);
        } else if (!available) {
            // Reserved or sold: show a clear message instead of a dead button
            bar.add(Theme.muted("Not available right now.   "), 0);
        } else {
            JButton action = Theme.primaryButton(actionText());
            action.addActionListener(e -> doMainAction());
            bar.add(action);
        }
        bar.add(close);
        return bar;
    }

    private String savedText() {
        return DataStore.isSaved(user.getUserId(), material.getMaterialId()) ? "Saved (click to remove)" : "Save for later";
    }

    /** The main button text depends on the material type and price. */
    private String actionText() {
        if (material instanceof DigitalMaterial) {
            return material.getPrice() == 0 ? "Download" : "Request access";
        }
        return "Request handover";
    }

    private void doMainAction() {
        if (material instanceof DigitalMaterial && material.getPrice() == 0) {
            download((DigitalMaterial) material);
            return;
        }

        String message = (material instanceof PhysicalMaterial)
                ? "Send a handover request to " + DataStore.getUserName(material.getUploadedByUserId()) + "?\n\n"
                  + "If they accept, you meet on campus and pay " + Theme.price(material.getPrice()) + " in cash."
                : "Request access to this paid resource (" + Theme.price(material.getPrice()) + ")?";
        if (JOptionPane.showConfirmDialog(this, message, "Send request", JOptionPane.YES_NO_OPTION)
                != JOptionPane.YES_OPTION) {
            return;
        }

        String error = DataStore.createRequest(user, material);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Request not sent", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Request sent. You can track it in My Dashboard.",
                    "Request sent", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.refreshAll();
            dispose();
        }
    }

    /** Lets the user choose where to save the file, then copies it there. */
    private void download(DigitalMaterial dm) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(dm.getFileName()));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File target = chooser.getSelectedFile();

        try {
            File source = DataStore.getUploadedFile(dm.getMaterialId());
            if (source != null && source.exists()) {
                // A real file uploaded in this session: copy it
                Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                // Sample record without a real file: save a small placeholder text file
                try (FileWriter w = new FileWriter(target)) {
                    w.write("BookBridge sample file\n\nTitle: " + dm.getTitle()
                            + "\nSubject: " + dm.getSubject() + "\n\n" + dm.getDescription() + "\n");
                }
            }
            dm.downloadFile(); // Member 2's method (logs the download)
            JOptionPane.showMessageDialog(this, "Saved to:\n" + target.getAbsolutePath(),
                    "Download complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not save the file:\n" + ex.getMessage(),
                    "Download failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
