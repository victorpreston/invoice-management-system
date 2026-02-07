package com.grainindustries.invoice.ui;

import com.grainindustries.invoice.model.*;
import com.grainindustries.invoice.util.SessionManager;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class InvoiceForm extends JFrame {
    
    // Color Constants matching the screenshot
    private static final Color HEADER_BG_COLOR = new Color(74, 98, 120); // Blue-gray header
    private static final Color PRIMARY_BUTTON_COLOR = new Color(52, 119, 174); // Blue for Add & New
    private static final Color SECONDARY_BUTTON_COLOR = new Color(108, 117, 125); // Gray for other buttons
    private static final Color CANCEL_BUTTON_COLOR = new Color(108, 117, 125); // Dark gray for Cancel
    private static final Color TABLE_ALT_ROW_COLOR = new Color(240, 248, 255); // Light blue for alternating rows
    private static final Color TABLE_HEADER_COLOR = new Color(200, 215, 230); // Light blue-gray for headers
    private static final Color FIELD_BG_COLOR = Color.WHITE;
    private static final Color REQUIRED_FIELD_COLOR = new Color(255, 255, 230); // Light yellow for required fields
    
    // Header Components - Left Section
    private JTextField customerCodeField;
    private JButton customerCodeButton;
    private JComboBox<String> customerNameCombo;
    private JButton customerNameButton;
    private JComboBox<String> contactPersonCombo;
    private JTextField customerRefNoField;
    private JComboBox<String> localCurrencyCombo;
    private JComboBox<String> applyCashDiscountCombo;
    private JComboBox<String> clientInhouseCombo;
    private JComboBox<String> draftOpenRRListCombo;
    private JTextField rebateRouteField;
    
    // Header Components - Right Section
    private JComboBox<String> invoiceTypeCombo;
    private JTextField invoiceNoField;
    private JTextField statusField;
    private JDateChooser postingDateChooser;
    private JDateChooser dueDateChooser;
    private JDateChooser documentDateChooser;
    private JTextField plateNoField;
    private JTextField gatePassNoField;
    private JTextField driverNameField;
    private JTextField driverIdField;
    
    // Approval Label
    private JLabel approvalLabel;
    
    // Table Components
    private JTable invoiceTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> itemServiceTypeCombo;
    private JComboBox<String> summaryTypeCombo;
    
    // Footer Components - Left Section
    private JComboBox<String> salesEmployeeCombo;
    private JTextField ownerField;
    private JTextField transporterCompanyField;
    private JTextField transportCodeField;
    private JTextField routeNameField;
    private JTextArea journalRemarkArea;
    private JTextArea remarksArea;
    
    // Footer Components - Right Section
    private JTextField totalBeforeDiscountField;
    private JTextField discountPercentField;
    private JTextField totalDownPaymentField;
    private JTextField freightField;
    private JCheckBox roundingCheckBox;
    private JTextField roundingField;
    private JTextField taxField;
    private JTextField totalField;
    private JTextField appliedAmountField;
    private JTextField balanceDueField;
    
    // Buttons
    private JButton addNewButton;
    private JButton addDraftNewButton;
    private JButton cancelButton;
    private JButton copyFromButton;
    private JButton copyToButton;
    
    // Current Invoice
    private Invoice currentInvoice;

    public InvoiceForm() {
        setTitle("Invoice Management System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1400, 900));
        
        currentInvoice = new Invoice();
        
        initComponents();
        layoutComponents();
        
        // Initialize with current user and date
        postingDateChooser.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        documentDateChooser.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        statusField.setText("Open");
        
        // Hide approval label initially
        approvalLabel.setVisible(false);
        
        // Generate next invoice number
        invoiceNoField.setText("14153777"); // TODO: Auto-increment from database
    }
    
    private void initComponents() {
        // Header - Left Section
        customerCodeField = new JTextField(15);
        customerCodeButton = new JButton("...");
        customerCodeButton.setPreferredSize(new Dimension(25, 20));
        
        customerNameCombo = new JComboBox<>();
        customerNameCombo.setEditable(true);
        customerNameButton = new JButton("...");
        customerNameButton.setPreferredSize(new Dimension(25, 20));
        
        contactPersonCombo = new JComboBox<>();
        contactPersonCombo.setEditable(true);
        
        customerRefNoField = new JTextField(15);
        
        localCurrencyCombo = new JComboBox<>(new String[]{"KES", "USD", "EUR", "GBP"});
        applyCashDiscountCombo = new JComboBox<>(new String[]{"No", "Yes"});
        clientInhouseCombo = new JComboBox<>(new String[]{"Client", "Inhouse"});
        draftOpenRRListCombo = new JComboBox<>();
        
        rebateRouteField = new JTextField(20);
        
        // Header - Right Section
        invoiceTypeCombo = new JComboBox<>(new String[]{"IN", "OUT"});
        invoiceTypeCombo.setSelectedItem("IN");
        
        invoiceNoField = new JTextField(15);
        invoiceNoField.setEditable(false);
        
        statusField = new JTextField(10);
        statusField.setEditable(false);
        
        postingDateChooser = new JDateChooser();
        postingDateChooser.setDateFormatString("MM/dd/yyyy");
        
        dueDateChooser = new JDateChooser();
        dueDateChooser.setDateFormatString("MM/dd/yyyy");
        
        documentDateChooser = new JDateChooser();
        documentDateChooser.setDateFormatString("MM/dd/yyyy");
        
        plateNoField = new JTextField(15);
        gatePassNoField = new JTextField(15);
        driverNameField = new JTextField(15);
        driverIdField = new JTextField(15);
        
        // Approval Label
        approvalLabel = new JLabel();
        approvalLabel.setForeground(Color.RED);
        approvalLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Table
        String[] columns = {
            "#", "Item/Service Type", "Item No.", "Item Description", "LoadedQty", "FreeQty",
            "ActualQty", "Qty in Whse", "UoM Code", "Open Qty", "Whse", "Unit Price",
            "Discount %", "Price after Discount", "VAT Code", "Gross Price after Desc.",
            "Total (LC)", "Gross Total (LC)"
        };
        
        tableModel = new DefaultTableModel(columns, 10) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Row number is not editable
            }
        };
        
        invoiceTable = new JTable(tableModel);
        invoiceTable.setRowHeight(25);
        invoiceTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        invoiceTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        invoiceTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        invoiceTable.setGridColor(new Color(220, 220, 220));
        invoiceTable.setShowGrid(true);
        
        // Style table header
        JTableHeader header = invoiceTable.getTableHeader();
        header.setBackground(TABLE_HEADER_COLOR);
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 28));
        
        // Add alternating row colors
        invoiceTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALT_ROW_COLOR);
                }
                return c;
            }
        });
        
        // Add row numbers
        for (int i = 0; i < 10; i++) {
            tableModel.setValueAt(i + 1, i, 0);
        }
        
        itemServiceTypeCombo = new JComboBox<>(new String[]{"Item", "Service"});
        summaryTypeCombo = new JComboBox<>(new String[]{"No Summary", "Summary"});
        
        // Footer - Left Section
        salesEmployeeCombo = new JComboBox<>();
        salesEmployeeCombo.setEditable(true);
        salesEmployeeCombo.addItem("-No Sales Employee-");
        
        ownerField = new JTextField(20);
        transporterCompanyField = new JTextField(20);
        transportCodeField = new JTextField(20);
        routeNameField = new JTextField(20);
        
        journalRemarkArea = new JTextArea(3, 20);
        journalRemarkArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        remarksArea = new JTextArea(3, 20);
        remarksArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        // Footer - Right Section (Financial Fields)
        totalBeforeDiscountField = createNumericField();
        discountPercentField = createNumericField();
        totalDownPaymentField = createNumericField();
        freightField = createNumericField();
        
        roundingCheckBox = new JCheckBox();
        roundingCheckBox.setSelected(true);
        roundingCheckBox.setBackground(Color.WHITE);
        
        roundingField = createNumericField();
        roundingField.setText("KES 0.00");
        taxField = createNumericField();
        totalField = createNumericField();
        totalField.setText("KES 0.00");
        appliedAmountField = createNumericField();
        balanceDueField = createNumericField();
        
        // Make certain financial fields bold
        totalField.setFont(new Font("Arial", Font.BOLD, 12));
        balanceDueField.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Buttons with styled colors matching screenshot
        addNewButton = createStyledButton("Add & New", PRIMARY_BUTTON_COLOR);
        addDraftNewButton = createStyledButton("Add Draft & New", PRIMARY_BUTTON_COLOR);
        cancelButton = createStyledButton("Cancel", CANCEL_BUTTON_COLOR);
        copyFromButton = createStyledButton("Copy From", SECONDARY_BUTTON_COLOR);
        copyToButton = createStyledButton("Copy To", SECONDARY_BUTTON_COLOR);
        
        // Add action listeners
        addNewButton.addActionListener(e -> saveInvoice(false));
        addDraftNewButton.addActionListener(e -> saveInvoice(true));
        cancelButton.addActionListener(e -> dispose());
        
        // Add listeners for validation and calculation
        remarksArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validateRemarks(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { validateRemarks(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { validateRemarks(); }
        });
    }
    
    private JTextField createNumericField() {
        JTextField field = new JTextField(15);
        field.setHorizontalAlignment(JTextField.RIGHT);
        field.setText("0.00");
        field.setBackground(FIELD_BG_COLOR);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        return field;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(120, 28));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Center Panel with Tabs and Table
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        // Footer Panel
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 10, 5, 10));
        
        // Title bar - thin blue-gray bar
        JLabel titleLabel = new JLabel("AR Invoice");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(HEADER_BG_COLOR);
        titleLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Form fields container
        JPanel formContainer = new JPanel(new BorderLayout(10, 5));
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(new EmptyBorder(10, 0, 5, 0));
        
        // Left side panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Customer Code
        addFormRow(leftPanel, gbc, row++, "CustomerCode", customerCodeField, customerCodeButton);
        
        // Name
        addFormRow(leftPanel, gbc, row++, "Name", customerNameCombo, customerNameButton);
        
        // Contact Person
        addFormRow(leftPanel, gbc, row++, "Contact Person", contactPersonCombo, null);
        
        // Customer Ref. No.
        addFormRow(leftPanel, gbc, row++, "Customer Ref. No.", customerRefNoField, null);
        
        // Local Currency
        addFormRow(leftPanel, gbc, row++, "Local Currency", localCurrencyCombo, null);
        
        // Apply Cash Discount
        addFormRow(leftPanel, gbc, row++, "Apply Cash Discount", applyCashDiscountCombo, null);
        
        // Client/Inhouse and Rebate Route on same row
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JLabel clientLabel = new JLabel("Client/Inhouse");
        clientLabel.setForeground(Color.BLACK);
        clientLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        leftPanel.add(clientLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        leftPanel.add(clientInhouseCombo, gbc);
        
        gbc.gridx = 2;
        gbc.weightx = 0;
        JLabel rebateLabel = new JLabel("Rebate Route");
        rebateLabel.setForeground(Color.BLACK);
        rebateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        leftPanel.add(rebateLabel, gbc);
        
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        leftPanel.add(rebateRouteField, gbc);
        row++;
        
        // Draft/Open RR List
        addFormRow(leftPanel, gbc, row++, "Draft/Open RR List", draftOpenRRListCombo, null);
        
        // Right side panel
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(3, 3, 3, 3);
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.anchor = GridBagConstraints.WEST;
        
        int rightRow = 0;
        
        // No. with dropdown
        gbc2.gridx = 0;
        gbc2.gridy = rightRow;
        gbc2.gridwidth = 1;
        gbc2.weightx = 0;
        JLabel noLabel = new JLabel("No.");
        noLabel.setForeground(Color.BLACK);
        noLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        rightPanel.add(noLabel, gbc2);
        
        gbc2.gridx = 1;
        gbc2.weightx = 0;
        invoiceTypeCombo.setPreferredSize(new Dimension(60, 20));
        rightPanel.add(invoiceTypeCombo, gbc2);
        
        gbc2.gridx = 2;
        gbc2.weightx = 1.0;
        rightPanel.add(invoiceNoField, gbc2);
        rightRow++;
        
        // Status
        addFormRow(rightPanel, gbc2, rightRow++, "Status", statusField, null);
        
        // Posting Date
        addFormRow(rightPanel, gbc2, rightRow++, "Posting Date", postingDateChooser, null);
        
        // Due Date
        addFormRow(rightPanel, gbc2, rightRow++, "Due Date", dueDateChooser, null);
        
        // Document Date
        addFormRow(rightPanel, gbc2, rightRow++, "Document Date", documentDateChooser, null);
        
        // Plate No.
        addFormRow(rightPanel, gbc2, rightRow++, "Plate No.", plateNoField, null);
        
        // Gate Pass No.
        addFormRow(rightPanel, gbc2, rightRow++, "Gate Pass No.", gatePassNoField, null);
        
        // Driver Name
        addFormRow(rightPanel, gbc2, rightRow++, "Driver Name", driverNameField, null);
        
        // Driver ID
        addFormRow(rightPanel, gbc2, rightRow++, "Driver ID", driverIdField, null);
        
        // Add approval label
        gbc2.gridx = 0;
        gbc2.gridy = rightRow;
        gbc2.gridwidth = 2;
        rightPanel.add(approvalLabel, gbc2);
        
        formContainer.add(leftPanel, BorderLayout.WEST);
        formContainer.add(rightPanel, BorderLayout.EAST);
        
        panel.add(formContainer, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent component, JButton button) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setForeground(Color.BLACK);
        labelComponent.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panel.add(labelComponent, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        
        // Style components
        if (component instanceof JTextField) {
            component.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        } else if (component instanceof JComboBox) {
            component.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        }
        
        if (button != null) {
            JPanel fieldPanel = new JPanel(new BorderLayout(2, 0));
            fieldPanel.setBackground(Color.WHITE);
            fieldPanel.add(component, BorderLayout.CENTER);
            fieldPanel.add(button, BorderLayout.EAST);
            panel.add(fieldPanel, gbc);
        } else {
            panel.add(component, gbc);
        }
    }
    
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        // Tabs Panel
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(0, 35));
        tabbedPane.addTab("Contents", null);
        tabbedPane.addTab("Logistics", null);
        tabbedPane.addTab("Accounting", null);
        tabbedPane.addTab("Attachments", null);
        tabbedPane.addTab("TIMS", null);
        tabbedPane.addTab("Email/SMS", null);
        tabbedPane.addTab("ETIMS", null);
        
        // Top controls panel
        JPanel topControlsPanel = new JPanel(new BorderLayout());
        topControlsPanel.setBackground(Color.WHITE);
        
        // Item/Service Type on left
        JPanel leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftControls.setBackground(Color.WHITE);
        leftControls.add(new JLabel("Item/Service Type"));
        leftControls.add(itemServiceTypeCombo);
        
        // Summary Type on right
        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightControls.setBackground(Color.WHITE);
        rightControls.add(new JLabel("Summary Type"));
        rightControls.add(summaryTypeCombo);
        
        topControlsPanel.add(leftControls, BorderLayout.WEST);
        topControlsPanel.add(rightControls, BorderLayout.EAST);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(tabbedPane, BorderLayout.NORTH);
        topPanel.add(topControlsPanel, BorderLayout.CENTER);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        //Table Panel
        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(5, 10, 10, 10));
        
        // Left Section
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        
        int row = 0;
        
        // Sales Employee
        addFormRow(leftPanel, gbc, row++, "Sales Employee", salesEmployeeCombo, null);
        
        // Owner
        addFormRow(leftPanel, gbc, row++, "Owner", ownerField, null);
        
        // Transporter Company
        addFormRow(leftPanel, gbc, row++, "Transporter Company", transporterCompanyField, null);
        
        // TransportCode
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JLabel transportLabel = new JLabel("TransportCode");
        transportLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        leftPanel.add(transportLabel, gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        leftPanel.add(transportCodeField, gbc);
        
        // Route Name
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JLabel routeLabel = new JLabel("Route Name");
        routeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        leftPanel.add(routeLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        leftPanel.add(routeNameField, gbc);
        row++;
        
        // Journal Remark
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JLabel journalLabel = new JLabel("Journal Remark");
        journalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        leftPanel.add(journalLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.gridwidth = 3;
        leftPanel.add(new JScrollPane(journalRemarkArea), gbc);
        row++;
        
        // Remarks
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JLabel remarksLabel = new JLabel("Remarks");
        remarksLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        leftPanel.add(remarksLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.gridwidth = 3;
        leftPanel.add(new JScrollPane(remarksArea), gbc);
        
        // Right Section (Financial Totals)
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(2, 5, 2, 5);
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.anchor = GridBagConstraints.EAST;
        
        int rightRow = 0;
        
        // Total Before Discount
        addFinancialRow(rightPanel, gbc2, rightRow++, "Total Before Discount", totalBeforeDiscountField);
        
        // Discount %
        addFinancialRow(rightPanel, gbc2, rightRow++, "Discount", discountPercentField);
        rightPanel.add(new JLabel("%"), createGBC(2, rightRow - 1, 1, 0));
        
        // Total Down Payment
        addFinancialRow(rightPanel, gbc2, rightRow++, "Total Down Payment", totalDownPaymentField);
        
        // Freight with arrow icon
        gbc2.gridx = 0;
        gbc2.gridy = rightRow;
        gbc2.gridwidth = 1;
        gbc2.weightx = 0;
        JLabel freightLabel = new JLabel("▼ Freight");
        freightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        rightPanel.add(freightLabel, gbc2);
        
        gbc2.gridx = 1;
        gbc2.weightx = 1.0;
        freightField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        rightPanel.add(freightField, gbc2);
        rightRow++;
        
        // Rounding with checkbox
        gbc2.gridx = 0;
        gbc2.gridy = rightRow;
        gbc2.gridwidth = 1;
        gbc2.weightx = 0;
        JPanel roundingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        roundingPanel.setBackground(Color.WHITE);
        roundingPanel.add(roundingCheckBox);
        roundingPanel.add(new JLabel("Rounding"));
        rightPanel.add(roundingPanel, gbc2);
        
        gbc2.gridx = 1;
        gbc2.weightx = 1.0;
        rightPanel.add(roundingField, gbc2);
        rightRow++;
        
        // Tax
        addFinancialRow(rightPanel, gbc2, rightRow++, "Tax", taxField);
        
        // Total
        addFinancialRow(rightPanel, gbc2, rightRow++, "Total", totalField);
        
        // Applied Amount
        addFinancialRow(rightPanel, gbc2, rightRow++, "Applied Amount", appliedAmountField);
        
        // Balance Due
        addFinancialRow(rightPanel, gbc2, rightRow++, "Balance Due", balanceDueField);
        
        panel.add(leftPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);
        
        // Bottom Buttons Panel  
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        
        // Left buttons: Add & New, Add Draft & New, Cancel
        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftButtons.setBackground(Color.WHITE);
        leftButtons.add(addNewButton);
        leftButtons.add(addDraftNewButton);
        leftButtons.add(cancelButton);
        
        // Right buttons: Copy From, Copy To
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightButtons.setBackground(Color.WHITE);
        rightButtons.add(copyFromButton);
        rightButtons.add(copyToButton);
        
        bottomPanel.add(leftButtons, BorderLayout.WEST);
        bottomPanel.add(rightButtons, BorderLayout.EAST);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void addFinancialRow(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panel.add(labelComponent, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panel.add(field, gbc);
    }
    
    private GridBagConstraints createGBC(int x, int y, int width, double weightx) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.weightx = weightx;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }
    
    private void validateRemarks() {
        String remarks = remarksArea.getText().trim();
        if (remarks.isEmpty()) {
            remarksArea.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        } else {
            remarksArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }
    }
    
    private void saveInvoice(boolean asDraft) {
        // Validate remarks
        if (remarksArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Remarks field is mandatory!",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate discount
        try {
            double discount = Double.parseDouble(discountPercentField.getText());
            if (discount > 50) {
                JOptionPane.showMessageDialog(this,
                        "Discount cannot exceed 50%!",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            // Ignore if field is empty or invalid
        }
        
        // Check approval requirement
        try {
            double total = Double.parseDouble(totalField.getText().replace(",", ""));
            if (total > 10000) {
                currentInvoice.setRequiresApproval(true);
                approvalLabel.setText("Invoice will go for approval – Amount: " + totalField.getText());
                approvalLabel.setVisible(true);
                
                JOptionPane.showMessageDialog(this,
                        "Invoice will go for approval – Amount: " + totalField.getText(),
                        "Approval Required",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            // Ignore
        }
        
        if (asDraft) {
            currentInvoice.setStatus("Draft");
        } else {
            currentInvoice.setStatus("Open");
        }
        
        JOptionPane.showMessageDialog(this,
                "Invoice saved successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
