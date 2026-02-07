package com.grainindustries.invoice.model;

import java.time.LocalDateTime;

public class Customer {
    private int customerId;
    private String customerCode;
    private String customerName;
    private String contactPerson;
    private String customerRefNo;
    private String localCurrency;
    private boolean applyCashDiscount;
    private String clientType;
    private String phone;
    private String email;
    private String address;
    private boolean isActive;
    private LocalDateTime createdDate;

    public Customer() {
    }

    public Customer(int customerId, String customerCode, String customerName) {
        this.customerId = customerId;
        this.customerCode = customerCode;
        this.customerName = customerName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getCustomerRefNo() {
        return customerRefNo;
    }

    public void setCustomerRefNo(String customerRefNo) {
        this.customerRefNo = customerRefNo;
    }

    public String getLocalCurrency() {
        return localCurrency;
    }

    public void setLocalCurrency(String localCurrency) {
        this.localCurrency = localCurrency;
    }

    public boolean isApplyCashDiscount() {
        return applyCashDiscount;
    }

    public void setApplyCashDiscount(boolean applyCashDiscount) {
        this.applyCashDiscount = applyCashDiscount;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return customerCode + " - " + customerName;
    }
}
