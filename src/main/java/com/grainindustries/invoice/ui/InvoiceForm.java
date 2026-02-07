package com.grainindustries.invoice.ui;

import com.grainindustries.invoice.model.*;
import com.grainindustries.invoice.util.SessionManager;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class InvoiceForm extends JFrame {
    
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
        setTitle("AR Invoice");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        currentInvoice = new Invoice();
        
        initComponents();
        layoutComponents();
        
        // Initialize with current user and date
        postingDateChooser.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        statusField.setText("Open");
        
        // Hide approval label initially
        approvalLabel.setVisible(false);
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
        
        // Add row numbers
        for (int i = 0; i < 10; i++) {
            tableModel.setValueAt(i + 1, i, 0);
        }
        
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
        roundingField = createNumericField();
        taxField = createNumericField();
        totalField = createNumericField();
        appliedAmountField = createNumericField();
        balanceDueField = createNumericField();
        
        // Make certain financial fields bold
        totalField.setFont(new Font("Arial", Font.BOLD, 12));
        balanceDueField.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Buttons
        addNewButton = new JButton("Add & New");
        addDraftNewButton = new JButton("Add Draft & New");
        cancelButton = new JButton("Cancel");
        copyFromButton = new JButton("Copy From");
        copyToButton = new JButton("Copy To");
        
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
        return field;
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
        panel.setBorder(new EmptyBorder(10, 10, 5, 10));
        
        // Left side panel
        JPanel leftPanel = new JPanel(new GridBagLayout());
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
        
        // Client/Inhouse
        addFormRow(leftPanel, gbc, row++, "Client/Inhouse", clientInhouseCombo, null);
        
        // Draft/Open RR List
        addFormRow(leftPanel, gbc, row++, "Draft/Open RR List", draftOpenRRListCombo, null);
        
        // Rebate Route
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        leftPanel.add(new JLabel("Rebate Route"), gbc);
        gbc.gridx = 3;
        gbc.gridwidth = 2;
        leftPanel.add(rebateRouteField, gbc);
        
        // Right side panel
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(3, 3, 3, 3);
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.anchor = GridBagConstraints.WEST;
        
        int rightRow = 0;
        
        // No.
        addFormRow(rightPanel, gbc2, rightRow++, "No.", invoiceNoField, null);
        
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
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent component, JButton button) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        if (button != null) {
            JPanel fieldPanel = new JPanel(new BorderLayout(2, 0));
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
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Summary Type combo on the right
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        summaryPanel.add(new JLabel("Summary Type"));
        summaryPanel.add(summaryTypeCombo);
        topPanel.add(summaryPanel, BorderLayout.EAST);
        
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
        panel.setBorder(new EmptyBorder(5, 10, 10, 10));
        
        // Left Section
        JPanel leftPanel = new JPanel(new GridBagLayout());
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
        leftPanel.add(new JLabel("TransportCode"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        leftPanel.add(transportCodeField, gbc);
        
        // Route Name
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        leftPanel.add(new JLabel("Route Name"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        leftPanel.add(routeNameField, gbc);
        row++;
        
        // Journal Remark
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        leftPanel.add(new JLabel("Journal Remark"), gbc);
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
        leftPanel.add(new JLabel("Remarks"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.gridwidth = 3;
        leftPanel.add(new JScrollPane(remarksArea), gbc);
        
        // Right Section (Financial Totals)
        JPanel rightPanel = new JPanel(new GridBagLayout());
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
        
        // Freight
        addFinancialRow(rightPanel, gbc2, rightRow++, "Freight", freightField);
        
        // Rounding
        addFinancialRow(rightPanel, gbc2, rightRow++, "Rounding", roundingField);
        
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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addNewButton);
        buttonPanel.add(addDraftNewButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(copyFromButton);
        buttonPanel.add(copyToButton);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.WEST);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void addFinancialRow(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
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
