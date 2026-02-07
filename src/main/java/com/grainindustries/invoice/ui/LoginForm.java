package com.grainindustries.invoice.ui;

import com.grainindustries.invoice.dao.UserDAO;
import com.grainindustries.invoice.model.User;
import com.grainindustries.invoice.util.SessionManager;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private boolean succeeded;

    public LoginForm(Frame parent) {
        super(parent, "Login - Invoice Management System", true);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;
        cs.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("Invoice Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(titleLabel, cs);

        cs.gridwidth = 1;
        cs.gridy++;
        JLabel usernameLabel = new JLabel("Username:");
        panel.add(usernameLabel, cs);

        usernameField = new JTextField(20);
        cs.gridx = 1;
        panel.add(usernameField, cs);

        cs.gridx = 0;
        cs.gridy++;
        JLabel passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel, cs);

        passwordField = new JPasswordField(20);
        cs.gridx = 1;
        panel.add(passwordField, cs);

        JPanel buttonPanel = new JPanel();
        loginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        cs.gridx = 0;
        cs.gridy++;
        cs.gridwidth = 2;
        panel.add(buttonPanel, cs);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginForm.this,
                        "Username and Password cannot be empty",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            UserDAO userDAO = new UserDAO();
            User user = userDAO.authenticate(username, password);

            if (user != null) {
                SessionManager.getInstance().setCurrentUser(user);
                succeeded = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginForm.this,
                        "Invalid username or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        });

        cancelButton.addActionListener(e -> {
            succeeded = false;
            dispose();
        });

        getRootPane().setDefaultButton(loginButton);

        setContentPane(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}
