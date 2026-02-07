package com.grainindustries.invoice;

import com.formdev.flatlaf.FlatLightLaf;
import com.grainindustries.invoice.ui.LoginForm;

import javax.swing.*;

/**
 * Main application entry point
 */
public class Main {

    public static void main(String[] args) {
        // Set FlatLaf look and feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize FlatLaf Look and Feel");
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginForm();
        });
    }
}
