package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;

/**
 * BrandPanel - the blue BookBridge panel shown on the Login and Register screens.
 *
 * It draws a simple bridge shape at the bottom using Java 2D graphics.
 * OOP: we OVERRIDE paintComponent() from JPanel to do our own drawing.
 */
public class BrandPanel extends JPanel {

    /** Builds the panel with a headline and a short description. */
    public static BrandPanel create(String headline, String description) {
        BrandPanel p = new BrandPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(48, 44, 48, 44));

        JLabel logo = new JLabel("BookBridge");
        logo.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 20));
        logo.setForeground(new Color(0xBFDBFE));

        JLabel title = new JLabel(headline);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Color.WHITE);

        JTextArea text = Theme.wrappingText(description, new Font(Theme.FONT_NAME, Font.PLAIN, 15),
                new Color(0xDBEAFE));
        text.setMaximumSize(new Dimension(320, 120));

        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(logo);
        p.add(Box.createVerticalStrut(70));
        p.add(title);
        p.add(Box.createVerticalStrut(14));
        p.add(text);
        p.add(Box.createVerticalGlue());
        return p;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background colour
        g2.setColor(Theme.PRIMARY);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // A bridge: one wide arch, a deck line and vertical cables
        int w = getWidth();
        int deckY = getHeight() - 70;
        g2.setColor(new Color(255, 255, 255, 60));
        g2.setStroke(new BasicStroke(3f));
        g2.draw(new Arc2D.Double(-w * 0.1, deckY - 120, w * 1.2, 240, 0, 180, Arc2D.OPEN));
        g2.drawLine(0, deckY, w, deckY);

        g2.setStroke(new BasicStroke(1.5f));
        for (int x = 30; x < w; x += 34) {
            // height of the arch above this x position (ellipse formula)
            double cx = w / 2.0, rx = w * 0.6, ry = 120;
            double dx = (x - cx) / rx;
            if (Math.abs(dx) >= 1) continue;
            int archY = (int) (deckY - ry * Math.sqrt(1 - dx * dx));
            g2.drawLine(x, archY, x, deckY);
        }

        // Accent stripe in amber, like the price colour used in the app
        g2.setColor(Theme.ACCENT);
        g2.fillRect(44, deckY + 24, 56, 4);
        g2.dispose();
    }
}
