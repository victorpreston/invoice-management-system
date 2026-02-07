package com.grainindustries.invoice.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private int invoiceId;
    private int invoiceNo;
    private int customerId;
    private String status;
    private LocalDate postingDate;
    private LocalDate dueDate;
    private LocalDate documentDate;
    private String plateNo;
    private String gatePassNo;
    private String driverName;
    private String driverId;
    private String rebateRoute;
    private Integer salesEmployeeId;
    private String owner;
    private String transporterCompany;
    private String transportCode;
    private String routeName;
    private String journalRemark;
    private String remarks;
    private BigDecimal totalBeforeDiscount;
    private BigDecimal discountPercent;
    private BigDecimal totalDownPayment;
    private BigDecimal freight;
    private BigDecimal rounding;
    private BigDecimal tax;
    private BigDecimal total;
    private BigDecimal appliedAmount;
    private BigDecimal balanceDue;
    private boolean requiresApproval;
    private Integer approvedBy;
    private LocalDateTime approvedDate;
    private Integer createdBy;
    private LocalDateTime createdDate;
    private Integer modifiedBy;
    private LocalDateTime modifiedDate;
    
    private List<InvoiceLine> invoiceLines;
    private Customer customer;
    private SalesEmployee salesEmployee;

    public Invoice() {
        this.invoiceLines = new ArrayList<>();
        this.postingDate = LocalDate.now();
        this.status = "Open";
        this.totalBeforeDiscount = BigDecimal.ZERO;
        this.discountPercent = BigDecimal.ZERO;
        this.totalDownPayment = BigDecimal.ZERO;
        this.freight = BigDecimal.ZERO;
        this.rounding = BigDecimal.ZERO;
        this.tax = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.appliedAmount = BigDecimal.ZERO;
        this.balanceDue = BigDecimal.ZERO;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(int invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(LocalDate postingDate) {
        this.postingDate = postingDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(LocalDate documentDate) {
        this.documentDate = documentDate;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getGatePassNo() {
        return gatePassNo;
    }

    public void setGatePassNo(String gatePassNo) {
        this.gatePassNo = gatePassNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getRebateRoute() {
        return rebateRoute;
    }

    public void setRebateRoute(String rebateRoute) {
        this.rebateRoute = rebateRoute;
    }

    public Integer getSalesEmployeeId() {
        return salesEmployeeId;
    }

    public void setSalesEmployeeId(Integer salesEmployeeId) {
        this.salesEmployeeId = salesEmployeeId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTransporterCompany() {
        return transporterCompany;
    }

    public void setTransporterCompany(String transporterCompany) {
        this.transporterCompany = transporterCompany;
    }

    public String getTransportCode() {
        return transportCode;
    }

    public void setTransportCode(String transportCode) {
        this.transportCode = transportCode;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getJournalRemark() {
        return journalRemark;
    }

    public void setJournalRemark(String journalRemark) {
        this.journalRemark = journalRemark;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getTotalBeforeDiscount() {
        return totalBeforeDiscount;
    }

    public void setTotalBeforeDiscount(BigDecimal totalBeforeDiscount) {
        this.totalBeforeDiscount = totalBeforeDiscount;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getTotalDownPayment() {
        return totalDownPayment;
    }

    public void setTotalDownPayment(BigDecimal totalDownPayment) {
        this.totalDownPayment = totalDownPayment;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public BigDecimal getRounding() {
        return rounding;
    }

    public void setRounding(BigDecimal rounding) {
        this.rounding = rounding;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getAppliedAmount() {
        return appliedAmount;
    }

    public void setAppliedAmount(BigDecimal appliedAmount) {
        this.appliedAmount = appliedAmount;
    }

    public BigDecimal getBalanceDue() {
        return balanceDue;
    }

    public void setBalanceDue(BigDecimal balanceDue) {
        this.balanceDue = balanceDue;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(LocalDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public List<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(List<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public SalesEmployee getSalesEmployee() {
        return salesEmployee;
    }

    public void setSalesEmployee(SalesEmployee salesEmployee) {
        this.salesEmployee = salesEmployee;
    }
}
