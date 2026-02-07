package com.grainindustries.invoice.ui;

import com.grainindustries.invoice.dao.UserDAO;
import com.grainindustries.invoice.model.User;
import com.grainindustries.invoice.util.SessionManager;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField regUsernameField;
    private JTextField regFullNameField;
    private JTextField regEmailField;
    private JPasswordField regPasswordField;
    private JPasswordField regConfirmPasswordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton switchToRegisterButton;
    private JButton switchToLoginButton;
    private JPanel loginPanel;
    private JPanel registerPanel;
    private boolean succeeded;

    // Color scheme matching the invoice form
    private static final Color PRIMARY_COLOR = new Color(74, 98, 120);
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);

    private static final Color DARK_TEXT = new Color(33, 37, 41);

    public LoginForm() {
        setTitle("Invoice Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Left side background
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, getWidth() / 2, getHeight());
                
                // Right side background with gradient
                GradientPaint gradient = new GradientPaint(
                    getWidth() / 2, 0, PRIMARY_COLOR,
                    getWidth(), getHeight(), ACCENT_COLOR
                );
                g2d.setPaint(gradient);
                g2d.fillRect(getWidth() / 2, 0, getWidth() / 2, getHeight());
                
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Top logo panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        topPanel.setOpaque(false);
        JLabel logoLabel = new JLabel("GRAIN INDUSTRIES");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logoLabel.setForeground(PRIMARY_COLOR);
        topPanel.add(logoLabel);
        
        JLabel subtitleLabel = new JLabel("Invoice Management System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(DARK_TEXT);
        topPanel.add(subtitleLabel);
        
        // Left panel - Login/Register forms
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 80, 0, 50));
        
        // Card panel to switch between login and register
        JPanel cardPanel = new JPanel(new CardLayout());
        cardPanel.setOpaque(false);
        
        // Login Panel
        loginPanel = createLoginPanel();
        
        // Register Panel
        registerPanel = createRegisterPanel();
        
        cardPanel.add(loginPanel, "LOGIN");
        cardPanel.add(registerPanel, "REGISTER");
        
        leftPanel.add(cardPanel);
        
        // Right panel - Image/Illustration
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        
        JLabel illustrationLabel = new JLabel();
        illustrationLabel.setIcon(createIllustrationIcon());
        illustrationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel descLabel = new JLabel("<html><center>Manage your invoices efficiently<br>with our comprehensive system</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descLabel.setForeground(new Color(255, 255, 255, 200));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        gbc.gridy = 0;
        rightPanel.add(illustrationLabel, gbc);
        gbc.gridy = 1;
        rightPanel.add(welcomeLabel, gbc);
        gbc.gridy = 2;
        rightPanel.add(descLabel, gbc);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.setOpaque(false);
        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        
        // Action listeners for switching
        switchToRegisterButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "REGISTER");
        });
        
        switchToLoginButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, "LOGIN");
        });
        
        loginButton.addActionListener(e -> performLogin());
        registerButton.addActionListener(e -> performRegister());
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setMaximumSize(new Dimension(450, 600));
        
        // Title
        JLabel titleLabel = new JLabel("Sign In");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Enter your credentials to access your account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(DARK_TEXT);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(subtitleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Username field
        panel.add(createFieldLabel("Username"));
        usernameField = createStyledTextField();
        usernameField.setMaximumSize(new Dimension(400, 45));
        panel.add(usernameField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Password field
        panel.add(createFieldLabel("Password"));
        passwordField = createStyledPasswordField();
        passwordField.setMaximumSize(new Dimension(400, 45));
        panel.add(passwordField);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Login button
        loginButton = createPrimaryButton("Sign In");
        loginButton.setMaximumSize(new Dimension(400, 45));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(loginButton);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Switch to register
        JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        switchPanel.setOpaque(false);
        switchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel noAccountLabel = new JLabel("Don't have an account? ");
        noAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        noAccountLabel.setForeground(DARK_TEXT);
        
        switchToRegisterButton = new JButton("Sign Up");
        switchToRegisterButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        switchToRegisterButton.setForeground(ACCENT_COLOR);
        switchToRegisterButton.setBorderPainted(false);
        switchToRegisterButton.setContentAreaFilled(false);
        switchToRegisterButton.setFocusPainted(false);
        switchToRegisterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        switchPanel.add(noAccountLabel);
        switchPanel.add(switchToRegisterButton);
        panel.add(switchPanel);
        
        return panel;
    }
    
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setMaximumSize(new Dimension(450, 700));
        
        // Title
        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Fill in the details to create your account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(DARK_TEXT);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(subtitleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Full Name
        panel.add(createFieldLabel("Full Name"));
        regFullNameField = createStyledTextField();
        regFullNameField.setMaximumSize(new Dimension(400, 45));
        panel.add(regFullNameField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Username
        panel.add(createFieldLabel("Username"));
        regUsernameField = createStyledTextField();
        regUsernameField.setMaximumSize(new Dimension(400, 45));
        panel.add(regUsernameField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Email
        panel.add(createFieldLabel("Email"));
        regEmailField = createStyledTextField();
        regEmailField.setMaximumSize(new Dimension(400, 45));
        panel.add(regEmailField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Password
        panel.add(createFieldLabel("Password"));
        regPasswordField = createStyledPasswordField();
        regPasswordField.setMaximumSize(new Dimension(400, 45));
        panel.add(regPasswordField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Confirm Password
        panel.add(createFieldLabel("Confirm Password"));
        regConfirmPasswordField = createStyledPasswordField();
        regConfirmPasswordField.setMaximumSize(new Dimension(400, 45));
        panel.add(regConfirmPasswordField);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Register button
        registerButton = createPrimaryButton("Create Account");
        registerButton.setMaximumSize(new Dimension(400, 45));
        registerButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(registerButton);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Switch to login
        JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        switchPanel.setOpaque(false);
        switchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel hasAccountLabel = new JLabel("Already have an account? ");
        hasAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        hasAccountLabel.setForeground(DARK_TEXT);
        
        switchToLoginButton = new JButton("Sign In");
        switchToLoginButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        switchToLoginButton.setForeground(ACCENT_COLOR);
        switchToLoginButton.setBorderPainted(false);
        switchToLoginButton.setContentAreaFilled(false);
        switchToLoginButton.setFocusPainted(false);
        switchToLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        switchPanel.add(hasAccountLabel);
        switchPanel.add(switchToLoginButton);
        panel.add(switchPanel);
        
        return panel;
    }
    
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(DARK_TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }
    
    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(ACCENT_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
        });
        return button;
    }
    
    private ImageIcon createIllustrationIcon() {
        // Create a simple illustration icon
        int size = 200;
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw a document icon
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.fillRoundRect(50, 30, 100, 140, 10, 10);
        
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.fillRect(70, 50, 60, 8);
        g2d.fillRect(70, 70, 60, 8);
        g2d.fillRect(70, 90, 40, 8);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.authenticate(username, password);

        if (user != null) {
            SessionManager.getInstance().setCurrentUser(user);
            succeeded = true;
            dispose();
            SwingUtilities.invokeLater(() -> {
                InvoiceForm invoiceForm = new InvoiceForm();
                invoiceForm.setVisible(true);
            });
        } else {
            showError("Invalid username or password");
            passwordField.setText("");
        }
    }
    
    private void performRegister() {
        String fullName = regFullNameField.getText().trim();
        String username = regUsernameField.getText().trim();
        String email = regEmailField.getText().trim();
        String password = new String(regPasswordField.getPassword());
        String confirmPassword = new String(regConfirmPasswordField.getPassword());
        
        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("All fields are required");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }
        
        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return;
        }
        
        // Register user in database
        UserDAO userDAO = new UserDAO();
        User newUser = userDAO.createUser(username, password, fullName, email);
        
        if (newUser != null) {
            showSuccess("Account created successfully! Please sign in.");
            
            // Clear registration fields
            regFullNameField.setText("");
            regUsernameField.setText("");
            regEmailField.setText("");
            regPasswordField.setText("");
            regConfirmPasswordField.setText("");
            
            // Switch to login panel
            CardLayout cl = (CardLayout) loginPanel.getParent().getLayout();
            cl.show(loginPanel.getParent(), "LOGIN");
            usernameField.setText(username);
        } else {
            showError("Username already exists. Please choose a different username.");
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}
