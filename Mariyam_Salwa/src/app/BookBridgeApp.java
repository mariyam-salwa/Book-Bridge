package app;

import ui.LoginFrame;
import ui.Theme;

import javax.swing.*;

/**
 * BookBridgeApp - the starting point of the program (contains main).
 *
 * Run this file to open the BookBridge desktop application.
 */
public class BookBridgeApp {

    public static void main(String[] args) {
        // Swing rule: all screen work must happen on the "Event Dispatch Thread".
        // invokeLater() puts our start-up code on that thread.
        SwingUtilities.invokeLater(() -> {
            Theme.applyGlobalDefaults();
            new LoginFrame().setVisible(true);
        });
    }
}
