package ui;

import data.DataStore;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * UploadPanel - share a digital file or list a physical book. (Replaces upload.html)
 *
 * Two tabs, one for each kind of Material:
 *  - "Digital material" creates a DigitalMaterial (needs a file)
 *  - "Physical book"    creates a PhysicalMaterial (needs a condition)
 */
public class UploadPanel extends BasePanel {

    private static final long MAX_FILE_BYTES = 20L * 1024 * 1024; // 20 MB

    // Digital tab fields
    private final JTextField dTitle = Theme.textField(30);
    private final JComboBox<String> dSubject = Theme.comboBox(DataStore.SUBJECTS);
    private final JTextArea dDescription = Theme.textArea(3, 30);
    private final JTextField dPrice = Theme.textField(10);
    private final JLabel fileLabel = Theme.muted("No file chosen");
    private File chosenFile;

    // Physical tab fields
    private final JTextField pTitle = Theme.textField(30);
    private final JComboBox<String> pSubject = Theme.comboBox(DataStore.SUBJECTS);
    private final JTextArea pDescription = Theme.textArea(3, 30);
    private final JComboBox<String> pCondition = Theme.comboBox(DataStore.CONDITIONS);
    private final JTextField pPrice = Theme.textField(10);

    public UploadPanel(MainFrame mainFrame, User user) {
        super(mainFrame, user, "Share a material",
                "Upload notes or papers as a file, or list a textbook you want to sell or give away.");

        JTabbedPane tabs = Theme.tabs();
        tabs.addTab("Digital material", wrap(buildDigitalForm()));
        tabs.addTab("Physical book", wrap(buildPhysicalForm()));
        content.add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildDigitalForm() {
        JButton chooseButton = Theme.secondaryButton("Choose file...");
        chooseButton.addActionListener(e -> chooseFile());
        JPanel fileRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fileRow.setOpaque(false);
        fileRow.add(chooseButton);
        fileRow.add(Box.createHorizontalStrut(12));
        fileRow.add(fileLabel);

        JButton submit = Theme.primaryButton("Upload material");
        submit.addActionListener(e -> submitDigital());

        FormPanel form = new FormPanel();
        form.addField("Title", dTitle);
        form.addField("Subject", dSubject);
        form.addField("Description", Theme.scroll(dDescription));
        form.addField("Price in Rs. (leave empty for free)", dPrice);
        form.addField("File (PDF, DOCX, PPTX, ZIP - max 20 MB)", fileRow);
        form.addRow(leftAligned(submit), 22);
        form.finish();
        return form;
    }

    private JPanel buildPhysicalForm() {
        JButton submit = Theme.primaryButton("List book");
        submit.addActionListener(e -> submitPhysical());

        FormPanel form = new FormPanel();
        form.addField("Book title and author", pTitle);
        form.addField("Subject", pSubject);
        form.addField("Description (edition, markings, anything a buyer should know)", Theme.scroll(pDescription));
        form.addField("Condition", pCondition);
        form.addField("Price in Rs. (0 to give it away; paid in cash at handover)", pPrice);
        form.addRow(leftAligned(submit), 22);
        form.finish();
        return form;
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Study files (pdf, docx, pptx, zip)", "pdf", "docx", "pptx", "zip"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            if (f.length() > MAX_FILE_BYTES) {
                warn("That file is larger than 20 MB. Please choose a smaller file.");
                return;
            }
            chosenFile = f;
            fileLabel.setText(f.getName());
            fileLabel.setForeground(Theme.TEXT_MAIN);
        }
    }

    private void submitDigital() {
        String title = dTitle.getText().trim();
        double price = Validator.parsePrice(dPrice.getText());

        if (title.length() < 3) {
            warn("Please enter a title (at least 3 characters).");
        } else if (price < 0) {
            warn("Price must be a number between 0 and 10000.");
        } else if (chosenFile == null) {
            warn("Please choose the file you want to upload.");
        } else {
            DataStore.addDigitalMaterial(title, (String) dSubject.getSelectedItem(),
                    dDescription.getText().trim(), price, chosenFile);
            info("\"" + title + "\" is now on BookBridge.");
            dTitle.setText("");
            dDescription.setText("");
            dPrice.setText("");
            chosenFile = null;
            fileLabel.setText("No file chosen");
            fileLabel.setForeground(Theme.TEXT_MUTED);
            mainFrame.refreshAll();
        }
    }

    private void submitPhysical() {
        String title = pTitle.getText().trim();
        double price = Validator.parsePrice(pPrice.getText());

        if (title.length() < 3) {
            warn("Please enter the book title (at least 3 characters).");
        } else if (price < 0) {
            warn("Price must be a number between 0 and 10000.");
        } else {
            DataStore.addPhysicalMaterial(title, (String) pSubject.getSelectedItem(),
                    pDescription.getText().trim(), price, (String) pCondition.getSelectedItem());
            info("\"" + title + "\" is listed. You will see buyer requests in My Dashboard.");
            pTitle.setText("");
            pDescription.setText("");
            pPrice.setText("");
            mainFrame.refreshAll();
        }
    }

    @Override
    public void refresh() {
        // Nothing to reload: this screen only collects input.
    }

    // ---------- layout helpers ----------

    /** Puts a form inside a white card, limited to a readable width. */
    private JComponent wrap(JPanel form) {
        JPanel card = Theme.card();
        card.setLayout(new BorderLayout());
        form.setPreferredSize(new Dimension(560, form.getPreferredSize().height));
        JPanel holder = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        holder.setOpaque(false);
        holder.add(form);
        card.add(holder, BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane(card);
        sp.setBorder(BorderFactory.createEmptyBorder());
        FlatStyle.styleScrollPane(sp);
        return sp;
    }

    private static JPanel leftAligned(JComponent c) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        p.add(c);
        return p;
    }
}
