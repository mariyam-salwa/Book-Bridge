package ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * FlatStyle - gives Swing's built-in components a modern, flat look.
 *
 * WHY: Swing's default look ("Metal") is from the late 1990s: 3D tabs, grey
 * gradient arrows, chunky scrollbars. Every Swing component is drawn by a
 * "UI delegate" class. Here we INHERIT from Swing's basic delegates and
 * OVERRIDE only the drawing methods we want to change.
 *
 * Nothing here changes how the program works, only how it looks.
 */
public class FlatStyle {

    // ======================= Tabs =======================

    /** Tabs drawn as plain text with a blue underline on the selected tab. */
    public static class FlatTabsUI extends BasicTabbedPaneUI {

        @Override
        protected void installDefaults() {
            super.installDefaults();
            tabInsets = new Insets(10, 16, 10, 16);       // space inside each tab
            selectedTabPadInsets = new Insets(0, 0, 0, 0);
            contentBorderInsets = new Insets(1, 0, 0, 0); // only a line on top of the content
            tabAreaInsets = new Insets(0, 0, 0, 0);
        }

        @Override
        protected void paintTabBackground(Graphics g, int placement, int index,
                                          int x, int y, int w, int h, boolean isSelected) {
            // no background: tabs sit directly on the page
        }

        @Override
        protected void paintTabBorder(Graphics g, int placement, int index,
                                      int x, int y, int w, int h, boolean isSelected) {
            if (isSelected) {
                g.setColor(Theme.PRIMARY);
                g.fillRect(x + 6, y + h - 3, w - 12, 3); // the underline
            }
        }

        @Override
        protected void paintText(Graphics g, int placement, Font font, FontMetrics metrics, int index,
                                 String title, Rectangle textRect, boolean isSelected) {
            g.setFont(isSelected ? Theme.FONT_BOLD : Theme.FONT_BODY);
            g.setColor(isSelected ? Theme.PRIMARY : Theme.TEXT_MUTED);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            FontMetrics fm = g.getFontMetrics();
            int textX = textRect.x + (textRect.width - fm.stringWidth(title)) / 2;
            g.drawString(title, textX, textRect.y + fm.getAscent());
        }

        @Override
        protected int calculateTabWidth(int placement, int index, FontMetrics metrics) {
            // measure with the bold font so the tab does not resize when selected
            FontMetrics bold = tabPane.getFontMetrics(Theme.FONT_BOLD);
            return super.calculateTabWidth(placement, index, bold) + 4;
        }

        @Override
        protected void paintContentBorder(Graphics g, int placement, int selectedIndex) {
            int y = calculateTabAreaHeight(placement, runCount, maxTabHeight);
            g.setColor(Theme.BORDER);
            g.fillRect(0, y, tabPane.getWidth(), 1); // one thin line under the tabs
        }

        @Override
        protected void paintFocusIndicator(Graphics g, int placement, Rectangle[] rects, int index,
                                           Rectangle iconRect, Rectangle textRect, boolean isSelected) {
            // no dotted focus box
        }
    }

    // ======================= Drop-down lists =======================

    /** A combo box with a flat background and a thin chevron instead of the grey 3D arrow. */
    public static class FlatComboUI extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            JButton arrow = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Theme.TEXT_MUTED);
                    g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    int cx = getWidth() / 2, cy = getHeight() / 2;
                    g2.drawPolyline(new int[]{cx - 5, cx, cx + 5}, new int[]{cy - 2, cy + 3, cy - 2}, 3);
                    g2.dispose();
                }
            };
            arrow.setBorder(BorderFactory.createEmptyBorder());
            arrow.setContentAreaFilled(false);
            arrow.setFocusPainted(false);
            arrow.setPreferredSize(new Dimension(30, 30));
            return arrow;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            g.setColor(Theme.BG_CARD); // white, even when focused (Metal paints it blue)
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    /** Applies the flat style to one combo box. */
    public static void styleCombo(JComboBox<?> combo) {
        combo.setUI(new FlatComboUI());
        combo.setBackground(Theme.BG_CARD);
        combo.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        // padding for the selected text and the items in the open list
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(6, 9, 6, 9));
                if (isSelected && index >= 0) {
                    setBackground(Theme.PRIMARY_LIGHT);
                    setForeground(Theme.TEXT_MAIN);
                }
                return this;
            }
        });
    }

    // ======================= Table header =======================

    /** Plain left-aligned header cells with one line underneath (no 3D borders). */
    public static class FlatHeaderRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, false, false, row, column);
            setFont(Theme.FONT_BOLD);
            setForeground(Theme.TEXT_MUTED);
            setBackground(Theme.BG_MAIN);
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                    BorderFactory.createEmptyBorder(9, 10, 9, 10)));
            return this;
        }
    }

    // ======================= Scrollbars =======================

    /** Thin rounded scrollbar with no arrow buttons. */
    public static class FlatScrollBarUI extends BasicScrollBarUI {

        @Override
        protected void configureScrollBarColors() {
            trackColor = Theme.BG_CARD;
            thumbColor = new Color(0xCBD5E1);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return zeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return zeroButton();
        }

        private JButton zeroButton() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            return b;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            if (r.isEmpty()) return;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(r.x + 3, r.y + 3, r.width - 6, r.height - 6, 8, 8);
            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            g.setColor(trackColor);
            g.fillRect(r.x, r.y, r.width, r.height);
        }
    }

    /** Applies the thin scrollbars to a scroll pane. */
    public static void styleScrollPane(JScrollPane sp) {
        sp.getVerticalScrollBar().setUI(new FlatScrollBarUI());
        sp.getHorizontalScrollBar().setUI(new FlatScrollBarUI());
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
        sp.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 12));
        sp.getVerticalScrollBar().setUnitIncrement(16); // smoother mouse-wheel scrolling
    }

    // ======================= Window icon =======================

    /** Draws the small BookBridge icon shown in the title bar and taskbar (instead of the Java cup). */
    public static Image appIcon() {
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Theme.PRIMARY);
        g.fillRoundRect(0, 0, 64, 64, 16, 16);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawArc(8, 22, 48, 40, 0, 180); // bridge arch
        g.drawLine(8, 42, 56, 42);        // bridge deck
        g.setColor(Theme.ACCENT);
        g.fillRect(14, 50, 16, 4);
        g.dispose();
        return img;
    }
}
