-- Invoice Management System - Database Schema
-- SQL Server Database Schema

USE invoice_db;
GO

-- Users table for authentication
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Users]') AND type in (N'U'))
BEGIN
    CREATE TABLE Users (
        user_id INT IDENTITY(1,1) PRIMARY KEY,
        username NVARCHAR(50) NOT NULL UNIQUE,
        password_hash NVARCHAR(255) NOT NULL,
        full_name NVARCHAR(100) NOT NULL,
        email NVARCHAR(100),
        is_active BIT DEFAULT 1,
        created_date DATETIME DEFAULT GETDATE(),
        last_login DATETIME
    );
END
GO

-- Customer table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Customer]') AND type in (N'U'))
BEGIN
    CREATE TABLE Customer (
        customer_id INT IDENTITY(1,1) PRIMARY KEY,
        customer_code NVARCHAR(50) NOT NULL UNIQUE,
        customer_name NVARCHAR(200) NOT NULL,
        contact_person NVARCHAR(100),
        customer_ref_no NVARCHAR(50),
        local_currency NVARCHAR(10) DEFAULT 'KES',
        apply_cash_discount BIT DEFAULT 0,
        client_type NVARCHAR(20), -- 'Client' or 'Inhouse'
        phone NVARCHAR(50),
        email NVARCHAR(100),
        address NVARCHAR(500),
        is_active BIT DEFAULT 1,
        created_date DATETIME DEFAULT GETDATE()
    );
END
GO

-- Item table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Item]') AND type in (N'U'))
BEGIN
    CREATE TABLE Item (
        item_id INT IDENTITY(1,1) PRIMARY KEY,
        item_no NVARCHAR(50) NOT NULL UNIQUE,
        item_description NVARCHAR(500) NOT NULL,
        item_type NVARCHAR(20), -- 'Item' or 'Service'
        uom_code NVARCHAR(20), -- Unit of Measure
        unit_price DECIMAL(18,3) NOT NULL DEFAULT 0,
        vat_code NVARCHAR(20),
        is_active BIT DEFAULT 1,
        created_date DATETIME DEFAULT GETDATE()
    );
END
GO

-- Sales Employee table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[SalesEmployee]') AND type in (N'U'))
BEGIN
    CREATE TABLE SalesEmployee (
        employee_id INT IDENTITY(1,1) PRIMARY KEY,
        employee_code NVARCHAR(50) NOT NULL UNIQUE,
        employee_name NVARCHAR(100) NOT NULL,
        phone NVARCHAR(50),
        email NVARCHAR(100),
        is_active BIT DEFAULT 1,
        created_date DATETIME DEFAULT GETDATE()
    );
END
GO

-- Invoice table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Invoice]') AND type in (N'U'))
BEGIN
    CREATE TABLE Invoice (
        invoice_id INT IDENTITY(1,1) PRIMARY KEY,
        invoice_no INT NOT NULL UNIQUE,
        customer_id INT NOT NULL,
        status NVARCHAR(20) DEFAULT 'Open', -- 'Open', 'Draft', 'Approved', 'Closed'
        posting_date DATE NOT NULL DEFAULT GETDATE(),
        due_date DATE,
        document_date DATE,
        plate_no NVARCHAR(50),
        gate_pass_no NVARCHAR(50),
        driver_name NVARCHAR(100),
        driver_id NVARCHAR(50),
        rebate_route NVARCHAR(100),
        sales_employee_id INT,
        owner NVARCHAR(100),
        transporter_company NVARCHAR(200),
        transport_code NVARCHAR(50),
        route_name NVARCHAR(100),
        journal_remark NVARCHAR(500),
        remarks NVARCHAR(500) NOT NULL,
        total_before_discount DECIMAL(18,3) DEFAULT 0,
        discount_percent DECIMAL(5,2) DEFAULT 0,
        total_down_payment DECIMAL(18,3) DEFAULT 0,
        freight DECIMAL(18,3) DEFAULT 0,
        rounding DECIMAL(18,3) DEFAULT 0,
        tax DECIMAL(18,3) DEFAULT 0,
        total DECIMAL(18,3) DEFAULT 0,
        applied_amount DECIMAL(18,3) DEFAULT 0,
        balance_due DECIMAL(18,3) DEFAULT 0,
        requires_approval BIT DEFAULT 0,
        approved_by INT,
        approved_date DATETIME,
        created_by INT,
        created_date DATETIME DEFAULT GETDATE(),
        modified_by INT,
        modified_date DATETIME,
        FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
        FOREIGN KEY (sales_employee_id) REFERENCES SalesEmployee(employee_id),
        FOREIGN KEY (created_by) REFERENCES Users(user_id),
        FOREIGN KEY (approved_by) REFERENCES Users(user_id)
    );
END
GO

-- Invoice Line table
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[InvoiceLine]') AND type in (N'U'))
BEGIN
    CREATE TABLE InvoiceLine (
        line_id INT IDENTITY(1,1) PRIMARY KEY,
        invoice_id INT NOT NULL,
        line_number INT NOT NULL,
        item_id INT NOT NULL,
        item_type NVARCHAR(20), -- 'Item' or 'Service'
        loaded_qty DECIMAL(18,3),
        free_qty DECIMAL(18,3),
        actual_qty DECIMAL(18,3),
        qty_in_whse DECIMAL(18,3),
        open_qty DECIMAL(18,3),
        whse NVARCHAR(50),
        unit_price DECIMAL(18,3) NOT NULL,
        discount_percent DECIMAL(5,2) DEFAULT 0,
        price_after_discount DECIMAL(18,3),
        vat_code NVARCHAR(20),
        gross_price_after_disc DECIMAL(18,3),
        total_lc DECIMAL(18,3),
        gross_total_lc DECIMAL(18,3),
        created_date DATETIME DEFAULT GETDATE(),
        FOREIGN KEY (invoice_id) REFERENCES Invoice(invoice_id) ON DELETE CASCADE,
        FOREIGN KEY (item_id) REFERENCES Item(item_id)
    );
END
GO

-- Create indexes for better performance
CREATE NONCLUSTERED INDEX IX_Customer_Code ON Customer(customer_code);
CREATE NONCLUSTERED INDEX IX_Customer_Name ON Customer(customer_name);
CREATE NONCLUSTERED INDEX IX_Item_No ON Item(item_no);
CREATE NONCLUSTERED INDEX IX_Item_Description ON Item(item_description);
CREATE NONCLUSTERED INDEX IX_Invoice_No ON Invoice(invoice_no);
CREATE NONCLUSTERED INDEX IX_Invoice_Customer ON Invoice(customer_id);
CREATE NONCLUSTERED INDEX IX_Invoice_PostingDate ON Invoice(posting_date);
CREATE NONCLUSTERED INDEX IX_InvoiceLine_Invoice ON InvoiceLine(invoice_id);
GO

-- Insert default admin user (password: admin123)
IF NOT EXISTS (SELECT * FROM Users WHERE username = 'admin')
BEGIN
    INSERT INTO Users (username, password_hash, full_name, email, is_active)
    VALUES ('admin', 'admin123', 'System Administrator', 'admin@grainindustries.com', 1);
END
GO

-- Insert sample customers
IF NOT EXISTS (SELECT * FROM Customer WHERE customer_code = 'CUST001')
BEGIN
    INSERT INTO Customer (customer_code, customer_name, contact_person, local_currency, client_type, phone, email)
    VALUES 
    ('CUST001', 'ABC Corporation', 'John Doe', 'KES', 'Client', '+254712345678', 'john@abc.com'),
    ('CUST002', 'XYZ Limited', 'Jane Smith', 'KES', 'Client', '+254723456789', 'jane@xyz.com'),
    ('CUST003', 'Global Traders', 'Mike Johnson', 'KES', 'Inhouse', '+254734567890', 'mike@global.com');
END
GO

-- Insert sample items
IF NOT EXISTS (SELECT * FROM Item WHERE item_no = 'ITEM001')
BEGIN
    INSERT INTO Item (item_no, item_description, item_type, uom_code, unit_price, vat_code)
    VALUES 
    ('ITEM001', 'Maize - Grade A', 'Item', 'KG', 50.000, 'VAT16'),
    ('ITEM002', 'Wheat Flour - Premium', 'Item', 'KG', 75.500, 'VAT16'),
    ('ITEM003', 'Rice - Long Grain', 'Item', 'KG', 120.000, 'VAT16'),
    ('ITEM004', 'Transportation Service', 'Service', 'TRIP', 5000.000, 'VAT16'),
    ('ITEM005', 'Storage Service', 'Service', 'DAY', 150.000, 'VAT16');
END
GO

-- Insert sample sales employees
IF NOT EXISTS (SELECT * FROM SalesEmployee WHERE employee_code = 'EMP001')
BEGIN
    INSERT INTO SalesEmployee (employee_code, employee_name, phone, email)
    VALUES 
    ('EMP001', 'Sarah Williams', '+254745678901', 'sarah.williams@grainindustries.com'),
    ('EMP002', 'David Brown', '+254756789012', 'david.brown@grainindustries.com'),
    ('EMP003', 'Linda Davis', '+254767890123', 'linda.davis@grainindustries.com');
END
GO

PRINT 'Database schema created successfully!';
GO
