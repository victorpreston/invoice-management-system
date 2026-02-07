package com.grainindustries.invoice.service;

import com.grainindustries.invoice.dao.InvoiceDAO;
import com.grainindustries.invoice.model.Invoice;
import com.grainindustries.invoice.model.InvoiceLine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Service layer for Invoice operations
 * Handles business logic, calculations, and coordinates between UI and DAO
 */
public class InvoiceService {
    
    private final InvoiceDAO invoiceDAO;
    
    public InvoiceService() {
        this.invoiceDAO = new InvoiceDAO();
    }
    
    /**
     * Get next invoice number
     */
    public int getNextInvoiceNumber() {
        return invoiceDAO.getNextInvoiceNumber();
    }
    
    /**
     * Create new invoice with lines
     */
    public Invoice createInvoice(Invoice invoice, List<InvoiceLine> lines) {
        // Validate invoice data
        if (invoice.getCustomerId() == 0) {
            throw new IllegalArgumentException("Customer is required");
        }
        if (invoice.getRemarks() == null || invoice.getRemarks().trim().isEmpty()) {
            throw new IllegalArgumentException("Remarks are required");
        }
        
        // Validate discount
        if (invoice.getDiscountPercent() != null && 
            invoice.getDiscountPercent().compareTo(new BigDecimal("50")) > 0) {
            throw new IllegalArgumentException("Discount cannot exceed 50%");
        }
        
        // Calculate totals
        calculateInvoiceTotals(invoice, lines);
        
        // Set requires approval if total > 10,000
        if (invoice.getTotal() != null && 
            invoice.getTotal().compareTo(new BigDecimal("10000")) > 0) {
            invoice.setRequiresApproval(true);
        }
        
        // Set default status if not set
        if (invoice.getStatus() == null || invoice.getStatus().trim().isEmpty()) {
            invoice.setStatus("Open");
        }
        
        return invoiceDAO.createInvoice(invoice, lines);
    }
    
    /**
     * Calculate invoice totals based on lines
     */
    public void calculateInvoiceTotals(Invoice invoice, List<InvoiceLine> lines) {
        BigDecimal totalBeforeDiscount = BigDecimal.ZERO;
        
        // Calculate total from lines
        if (lines != null && !lines.isEmpty()) {
            for (InvoiceLine line : lines) {
                calculateLineTotals(line);
                if (line.getTotalLc() != null) {
                    totalBeforeDiscount = totalBeforeDiscount.add(line.getTotalLc());
                }
            }
        }
        
        invoice.setTotalBeforeDiscount(totalBeforeDiscount.setScale(3, RoundingMode.HALF_UP));
        
        // Apply discount
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (invoice.getDiscountPercent() != null && invoice.getDiscountPercent().compareTo(BigDecimal.ZERO) > 0) {
            discountAmount = totalBeforeDiscount
                    .multiply(invoice.getDiscountPercent())
                    .divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
        }
        
        BigDecimal totalAfterDiscount = totalBeforeDiscount.subtract(discountAmount);
        
        // Add freight
        BigDecimal freight = invoice.getFreight() != null ? invoice.getFreight() : BigDecimal.ZERO;
        totalAfterDiscount = totalAfterDiscount.add(freight);
        
        // Add rounding
        BigDecimal rounding = invoice.getRounding() != null ? invoice.getRounding() : BigDecimal.ZERO;
        totalAfterDiscount = totalAfterDiscount.add(rounding);
        
        // Calculate tax (16% VAT)
        BigDecimal tax = totalAfterDiscount
                .multiply(new BigDecimal("0.16"))
                .setScale(3, RoundingMode.HALF_UP);
        invoice.setTax(tax);
        
        // Calculate final total
        BigDecimal total = totalAfterDiscount.add(tax);
        invoice.setTotal(total.setScale(3, RoundingMode.HALF_UP));
        
        // Calculate balance due
        BigDecimal downPayment = invoice.getTotalDownPayment() != null ? 
                invoice.getTotalDownPayment() : BigDecimal.ZERO;
        BigDecimal appliedAmount = invoice.getAppliedAmount() != null ? 
                invoice.getAppliedAmount() : BigDecimal.ZERO;
        
        BigDecimal balanceDue = total.subtract(downPayment).subtract(appliedAmount);
        invoice.setBalanceDue(balanceDue.setScale(3, RoundingMode.HALF_UP));
    }
    
    /**
     * Calculate line totals
     */
    public void calculateLineTotals(InvoiceLine line) {
        if (line.getActualQty() == null || line.getUnitPrice() == null) {
            return;
        }
        
        // Calculate price after discount
        BigDecimal priceAfterDiscount = line.getUnitPrice();
        if (line.getDiscountPercent() != null && line.getDiscountPercent().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountAmount = line.getUnitPrice()
                    .multiply(line.getDiscountPercent())
                    .divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
            priceAfterDiscount = line.getUnitPrice().subtract(discountAmount);
        }
        line.setPriceAfterDiscount(priceAfterDiscount.setScale(3, RoundingMode.HALF_UP));
        
        // Calculate gross price after discount (same as price after discount for now)
        line.setGrossPriceAfterDisc(priceAfterDiscount.setScale(3, RoundingMode.HALF_UP));
        
        // Calculate total LC
        BigDecimal totalLc = priceAfterDiscount.multiply(line.getActualQty());
        line.setTotalLc(totalLc.setScale(3, RoundingMode.HALF_UP));
        
        // Calculate gross total LC (same as total LC for now)
        line.setGrossTotalLc(totalLc.setScale(3, RoundingMode.HALF_UP));
    }
    
    /**
     * Get invoice by ID
     */
    public Invoice getInvoiceById(int invoiceId) {
        return invoiceDAO.getInvoiceById(invoiceId);
    }
    
    /**
     * Get invoice by invoice number
     */
    public Invoice getInvoiceByNo(int invoiceNo) {
        return invoiceDAO.getInvoiceByNo(invoiceNo);
    }
    
    /**
     * Get all invoices
     */
    public List<Invoice> getAllInvoices() {
        return invoiceDAO.getAllInvoices();
    }
    
    /**
     * Get invoices by status
     */
    public List<Invoice> getInvoicesByStatus(String status) {
        return invoiceDAO.getInvoicesByStatus(status);
    }
    
    /**
     * Update invoice status
     */
    public boolean updateInvoiceStatus(int invoiceId, String status) {
        return invoiceDAO.updateInvoiceStatus(invoiceId, status);
    }
    
    /**
     * Validate invoice before saving
     */
    public boolean validateInvoice(Invoice invoice) {
        if (invoice.getCustomerId() == 0) {
            return false;
        }
        if (invoice.getRemarks() == null || invoice.getRemarks().trim().isEmpty()) {
            return false;
        }
        if (invoice.getDiscountPercent() != null && 
            invoice.getDiscountPercent().compareTo(new BigDecimal("50")) > 0) {
            return false;
        }
        return true;
    }
}
