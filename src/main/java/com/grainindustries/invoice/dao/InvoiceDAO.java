package com.grainindustries.invoice.dao;

import com.grainindustries.invoice.model.Invoice;
import com.grainindustries.invoice.model.InvoiceLine;
import com.grainindustries.invoice.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    
    /**
     * Get next invoice number
     */
    public int getNextInvoiceNumber() {
        String sql = "SELECT ISNULL(MAX(invoice_no), 0) + 1 AS next_no FROM Invoice";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("next_no");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }
    
    /**
     * Create new invoice with lines
     */
    public Invoice createInvoice(Invoice invoice, List<InvoiceLine> lines) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Insert invoice
            String invoiceSql = "INSERT INTO Invoice (invoice_no, customer_id, status, posting_date, " +
                    "due_date, document_date, plate_no, gate_pass_no, driver_name, driver_id, " +
                    "rebate_route, sales_employee_id, owner, transporter_company, transport_code, " +
                    "route_name, journal_remark, remarks, total_before_discount, discount_percent, " +
                    "total_down_payment, freight, rounding, tax, total, applied_amount, balance_due, " +
                    "requires_approval, created_by, created_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
            
            PreparedStatement ps = conn.prepareStatement(invoiceSql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, invoice.getInvoiceNo());
            ps.setInt(2, invoice.getCustomerId());
            ps.setString(3, invoice.getStatus());
            ps.setDate(4, invoice.getPostingDate() != null ? java.sql.Date.valueOf(invoice.getPostingDate()) : null);
            ps.setDate(5, invoice.getDueDate() != null ? java.sql.Date.valueOf(invoice.getDueDate()) : null);
            ps.setDate(6, invoice.getDocumentDate() != null ? java.sql.Date.valueOf(invoice.getDocumentDate()) : null);
            ps.setString(7, invoice.getPlateNo());
            ps.setString(8, invoice.getGatePassNo());
            ps.setString(9, invoice.getDriverName());
            ps.setString(10, invoice.getDriverId());
            ps.setString(11, invoice.getRebateRoute());
            
            if (invoice.getSalesEmployeeId() != null) {
                ps.setInt(12, invoice.getSalesEmployeeId());
            } else {
                ps.setNull(12, Types.INTEGER);
            }
            
            ps.setString(13, invoice.getOwner());
            ps.setString(14, invoice.getTransporterCompany());
            ps.setString(15, invoice.getTransportCode());
            ps.setString(16, invoice.getRouteName());
            ps.setString(17, invoice.getJournalRemark());
            ps.setString(18, invoice.getRemarks());
            ps.setBigDecimal(19, invoice.getTotalBeforeDiscount());
            ps.setBigDecimal(20, invoice.getDiscountPercent());
            ps.setBigDecimal(21, invoice.getTotalDownPayment());
            ps.setBigDecimal(22, invoice.getFreight());
            ps.setBigDecimal(23, invoice.getRounding());
            ps.setBigDecimal(24, invoice.getTax());
            ps.setBigDecimal(25, invoice.getTotal());
            ps.setBigDecimal(26, invoice.getAppliedAmount());
            ps.setBigDecimal(27, invoice.getBalanceDue());
            ps.setBoolean(28, invoice.isRequiresApproval());
            ps.setInt(29, invoice.getCreatedBy());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int invoiceId = rs.getInt(1);
                    invoice.setInvoiceId(invoiceId);
                    
                    // Insert invoice lines
                    if (lines != null && !lines.isEmpty()) {
                        String lineSql = "INSERT INTO InvoiceLine (invoice_id, line_number, item_id, " +
                                "item_type, loaded_qty, free_qty, actual_qty, qty_in_whse, open_qty, " +
                                "whse, unit_price, discount_percent, price_after_discount, vat_code, " +
                                "gross_price_after_disc, total_lc, gross_total_lc, created_date) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
                        
                        PreparedStatement linePs = conn.prepareStatement(lineSql, Statement.RETURN_GENERATED_KEYS);
                        
                        for (InvoiceLine line : lines) {
                            linePs.setInt(1, invoiceId);
                            linePs.setInt(2, line.getLineNumber());
                            linePs.setInt(3, line.getItemId());
                            linePs.setString(4, line.getItemType());
                            linePs.setBigDecimal(5, line.getLoadedQty());
                            linePs.setBigDecimal(6, line.getFreeQty());
                            linePs.setBigDecimal(7, line.getActualQty());
                            linePs.setBigDecimal(8, line.getQtyInWhse());
                            linePs.setBigDecimal(9, line.getOpenQty());
                            linePs.setString(10, line.getWhse());
                            linePs.setBigDecimal(11, line.getUnitPrice());
                            linePs.setBigDecimal(12, line.getDiscountPercent());
                            linePs.setBigDecimal(13, line.getPriceAfterDiscount());
                            linePs.setString(14, line.getVatCode());
                            linePs.setBigDecimal(15, line.getGrossPriceAfterDisc());
                            linePs.setBigDecimal(16, line.getTotalLc());
                            linePs.setBigDecimal(17, line.getGrossTotalLc());
                            
                            linePs.addBatch();
                        }
                        
                        linePs.executeBatch();
                    }
                }
            }
            
            conn.commit();
            return invoice;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    /**
     * Get invoice by ID with lines
     */
    public Invoice getInvoiceById(int invoiceId) {
        String sql = "SELECT * FROM Invoice WHERE invoice_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, invoiceId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Invoice invoice = mapResultSetToInvoice(rs);
                invoice.setInvoiceLines(getInvoiceLines(invoiceId));
                return invoice;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get invoice by invoice number
     */
    public Invoice getInvoiceByNo(int invoiceNo) {
        String sql = "SELECT * FROM Invoice WHERE invoice_no = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, invoiceNo);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Invoice invoice = mapResultSetToInvoice(rs);
                invoice.setInvoiceLines(getInvoiceLines(invoice.getInvoiceId()));
                return invoice;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get invoice lines for an invoice
     */
    public List<InvoiceLine> getInvoiceLines(int invoiceId) {
        List<InvoiceLine> lines = new ArrayList<>();
        String sql = "SELECT * FROM InvoiceLine WHERE invoice_id = ? ORDER BY line_number";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, invoiceId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lines.add(mapResultSetToInvoiceLine(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lines;
    }
    
    /**
     * Get all invoices
     */
    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM Invoice ORDER BY invoice_no DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                invoices.add(mapResultSetToInvoice(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }
    
    /**
     * Get invoices by status
     */
    public List<Invoice> getInvoicesByStatus(String status) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM Invoice WHERE status = ? ORDER BY invoice_no DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                invoices.add(mapResultSetToInvoice(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }
    
    /**
     * Update invoice status
     */
    public boolean updateInvoiceStatus(int invoiceId, String status) {
        String sql = "UPDATE Invoice SET status = ?, modified_date = GETDATE() WHERE invoice_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setInt(2, invoiceId);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Map ResultSet to Invoice object
     */
    private Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(rs.getInt("invoice_id"));
        invoice.setInvoiceNo(rs.getInt("invoice_no"));
        invoice.setCustomerId(rs.getInt("customer_id"));
        invoice.setStatus(rs.getString("status"));
        java.sql.Date postingDate = rs.getDate("posting_date");
        if (postingDate != null) {
            invoice.setPostingDate(postingDate.toLocalDate());
        }
        java.sql.Date dueDate = rs.getDate("due_date");
        if (dueDate != null) {
            invoice.setDueDate(dueDate.toLocalDate());
        }
        java.sql.Date documentDate = rs.getDate("document_date");
        if (documentDate != null) {
            invoice.setDocumentDate(documentDate.toLocalDate());
        }
        invoice.setPlateNo(rs.getString("plate_no"));
        invoice.setGatePassNo(rs.getString("gate_pass_no"));
        invoice.setDriverName(rs.getString("driver_name"));
        invoice.setDriverId(rs.getString("driver_id"));
        invoice.setRebateRoute(rs.getString("rebate_route"));
        
        int salesEmpId = rs.getInt("sales_employee_id");
        if (!rs.wasNull()) {
            invoice.setSalesEmployeeId(salesEmpId);
        }
        
        invoice.setOwner(rs.getString("owner"));
        invoice.setTransporterCompany(rs.getString("transporter_company"));
        invoice.setTransportCode(rs.getString("transport_code"));
        invoice.setRouteName(rs.getString("route_name"));
        invoice.setJournalRemark(rs.getString("journal_remark"));
        invoice.setRemarks(rs.getString("remarks"));
        invoice.setTotalBeforeDiscount(rs.getBigDecimal("total_before_discount"));
        invoice.setDiscountPercent(rs.getBigDecimal("discount_percent"));
        invoice.setTotalDownPayment(rs.getBigDecimal("total_down_payment"));
        invoice.setFreight(rs.getBigDecimal("freight"));
        invoice.setRounding(rs.getBigDecimal("rounding"));
        invoice.setTax(rs.getBigDecimal("tax"));
        invoice.setTotal(rs.getBigDecimal("total"));
        invoice.setAppliedAmount(rs.getBigDecimal("applied_amount"));
        invoice.setBalanceDue(rs.getBigDecimal("balance_due"));
        invoice.setRequiresApproval(rs.getBoolean("requires_approval"));
        
        int approvedBy = rs.getInt("approved_by");
        if (!rs.wasNull()) {
            invoice.setApprovedBy(approvedBy);
        }
        
        Timestamp approvedTs = rs.getTimestamp("approved_date");
        if (approvedTs != null) {
            invoice.setApprovedDate(approvedTs.toLocalDateTime());
        }
        invoice.setCreatedBy(rs.getInt("created_by"));
        Timestamp createdTs = rs.getTimestamp("created_date");
        if (createdTs != null) {
            invoice.setCreatedDate(createdTs.toLocalDateTime());
        }
        
        int modifiedBy = rs.getInt("modified_by");
        if (!rs.wasNull()) {
            invoice.setModifiedBy(modifiedBy);
        }
        
        Timestamp modifiedTs = rs.getTimestamp("modified_date");
        if (modifiedTs != null) {
            invoice.setModifiedDate(modifiedTs.toLocalDateTime());
        }
        return invoice;
    }
    
    /**
     * Map ResultSet to InvoiceLine object
     */
    private InvoiceLine mapResultSetToInvoiceLine(ResultSet rs) throws SQLException {
        InvoiceLine line = new InvoiceLine();
        line.setLineId(rs.getInt("line_id"));
        line.setInvoiceId(rs.getInt("invoice_id"));
        line.setLineNumber(rs.getInt("line_number"));
        line.setItemId(rs.getInt("item_id"));
        line.setItemType(rs.getString("item_type"));
        line.setLoadedQty(rs.getBigDecimal("loaded_qty"));
        line.setFreeQty(rs.getBigDecimal("free_qty"));
        line.setActualQty(rs.getBigDecimal("actual_qty"));
        line.setQtyInWhse(rs.getBigDecimal("qty_in_whse"));
        line.setOpenQty(rs.getBigDecimal("open_qty"));
        line.setWhse(rs.getString("whse"));
        line.setUnitPrice(rs.getBigDecimal("unit_price"));
        line.setDiscountPercent(rs.getBigDecimal("discount_percent"));
        line.setPriceAfterDiscount(rs.getBigDecimal("price_after_discount"));
        line.setVatCode(rs.getString("vat_code"));
        line.setGrossPriceAfterDisc(rs.getBigDecimal("gross_price_after_disc"));
        line.setTotalLc(rs.getBigDecimal("total_lc"));
        line.setGrossTotalLc(rs.getBigDecimal("gross_total_lc"));
        Timestamp createdTs = rs.getTimestamp("created_date");
        if (createdTs != null) {
            line.setCreatedDate(createdTs.toLocalDateTime());
        }
        return line;
    }
}
