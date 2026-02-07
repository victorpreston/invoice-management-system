package com.grainindustries.invoice;

import com.formdev.flatlaf.FlatLightLaf;
import com.grainindustries.invoice.ui.LoginForm;
import com.grainindustries.invoice.ui.InvoiceForm;
import com.grainindustries.invoice.util.SessionManager;

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
            LoginForm loginForm = new LoginForm(null);
            loginForm.setVisible(true);
            
            if (loginForm.isSucceeded() && SessionManager.getInstance().isLoggedIn()) {
                InvoiceForm invoiceForm = new InvoiceForm();
                invoiceForm.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}
