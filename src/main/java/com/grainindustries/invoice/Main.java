package com.grainindustries.invoice;

import com.formdev.flatlaf.FlatLightLaf;
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
            JFrame frame = new JFrame("Invoice Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            
            JLabel label = new JLabel("Invoice Management System", SwingConstants.CENTER);
            frame.add(label);
            
            frame.setVisible(true);
        });
    }
}
