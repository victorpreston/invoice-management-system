package com.grainindustries.invoice.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InvoiceLine {
    private int lineId;
    private int invoiceId;
    private int lineNumber;
    private int itemId;
    private String itemType;
    private BigDecimal loadedQty;
    private BigDecimal freeQty;
    private BigDecimal actualQty;
    private BigDecimal qtyInWhse;
    private BigDecimal openQty;
    private String whse;
    private BigDecimal unitPrice;
    private BigDecimal discountPercent;
    private BigDecimal priceAfterDiscount;
    private String vatCode;
    private BigDecimal grossPriceAfterDisc;
    private BigDecimal totalLc;
    private BigDecimal grossTotalLc;
    private LocalDateTime createdDate;
    
    private Item item;

    public InvoiceLine() {
        this.loadedQty = BigDecimal.ZERO;
        this.freeQty = BigDecimal.ZERO;
        this.actualQty = BigDecimal.ZERO;
        this.qtyInWhse = BigDecimal.ZERO;
        this.openQty = BigDecimal.ZERO;
        this.unitPrice = BigDecimal.ZERO;
        this.discountPercent = BigDecimal.ZERO;
        this.priceAfterDiscount = BigDecimal.ZERO;
        this.grossPriceAfterDisc = BigDecimal.ZERO;
        this.totalLc = BigDecimal.ZERO;
        this.grossTotalLc = BigDecimal.ZERO;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public BigDecimal getLoadedQty() {
        return loadedQty;
    }

    public void setLoadedQty(BigDecimal loadedQty) {
        this.loadedQty = loadedQty;
    }

    public BigDecimal getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(BigDecimal freeQty) {
        this.freeQty = freeQty;
    }

    public BigDecimal getActualQty() {
        return actualQty;
    }

    public void setActualQty(BigDecimal actualQty) {
        this.actualQty = actualQty;
    }

    public BigDecimal getQtyInWhse() {
        return qtyInWhse;
    }

    public void setQtyInWhse(BigDecimal qtyInWhse) {
        this.qtyInWhse = qtyInWhse;
    }

    public BigDecimal getOpenQty() {
        return openQty;
    }

    public void setOpenQty(BigDecimal openQty) {
        this.openQty = openQty;
    }

    public String getWhse() {
        return whse;
    }

    public void setWhse(String whse) {
        this.whse = whse;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getPriceAfterDiscount() {
        return priceAfterDiscount;
    }

    public void setPriceAfterDiscount(BigDecimal priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    public String getVatCode() {
        return vatCode;
    }

    public void setVatCode(String vatCode) {
        this.vatCode = vatCode;
    }

    public BigDecimal getGrossPriceAfterDisc() {
        return grossPriceAfterDisc;
    }

    public void setGrossPriceAfterDisc(BigDecimal grossPriceAfterDisc) {
        this.grossPriceAfterDisc = grossPriceAfterDisc;
    }

    public BigDecimal getTotalLc() {
        return totalLc;
    }

    public void setTotalLc(BigDecimal totalLc) {
        this.totalLc = totalLc;
    }

    public BigDecimal getGrossTotalLc() {
        return grossTotalLc;
    }

    public void setGrossTotalLc(BigDecimal grossTotalLc) {
        this.grossTotalLc = grossTotalLc;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
