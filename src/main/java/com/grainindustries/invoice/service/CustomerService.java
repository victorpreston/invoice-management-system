package com.grainindustries.invoice.service;

import com.grainindustries.invoice.dao.CustomerDAO;
import com.grainindustries.invoice.model.Customer;

import java.util.List;

/**
 * Service layer for Customer operations
 * Handles business logic and coordinates between UI and DAO
 */
public class CustomerService {
    
    private final CustomerDAO customerDAO;
    
    public CustomerService() {
        this.customerDAO = new CustomerDAO();
    }
    
    /**
     * Get all active customers
     */
    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }
    
    /**
     * Get customer by ID
     */
    public Customer getCustomerById(int customerId) {
        return customerDAO.getCustomerById(customerId);
    }
    
    /**
     * Get customer by code
     */
    public Customer getCustomerByCode(String customerCode) {
        if (customerCode == null || customerCode.trim().isEmpty()) {
            return null;
        }
        return customerDAO.getCustomerByCode(customerCode.trim());
    }
    
    /**
     * Get customer by name
     */
    public Customer getCustomerByName(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            return null;
        }
        return customerDAO.getCustomerByName(customerName.trim());
    }
    
    /**
     * Search customers by code or name
     */
    public List<Customer> searchCustomers(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllCustomers();
        }
        return customerDAO.searchCustomers(searchTerm.trim());
    }
    
    /**
     * Get customer names for dropdown
     */
    public List<String> getCustomerNames() {
        return customerDAO.getCustomerNames();
    }
    
    /**
     * Create new customer
     */
    public Customer createCustomer(Customer customer) {
        // Validate customer data
        if (customer.getCustomerCode() == null || customer.getCustomerCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer code is required");
        }
        if (customer.getCustomerName() == null || customer.getCustomerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        
        return customerDAO.createCustomer(customer);
    }
    
    /**
     * Validate customer exists by code
     */
    public boolean customerExists(String customerCode) {
        return getCustomerByCode(customerCode) != null;
    }
}
