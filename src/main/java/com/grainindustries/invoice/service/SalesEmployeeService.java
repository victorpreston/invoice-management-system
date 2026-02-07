package com.grainindustries.invoice.service;

import com.grainindustries.invoice.dao.SalesEmployeeDAO;
import com.grainindustries.invoice.model.SalesEmployee;

import java.util.List;

/**
 * Service layer for SalesEmployee operations
 * Handles business logic and coordinates between UI and DAO
 */
public class SalesEmployeeService {
    
    private final SalesEmployeeDAO salesEmployeeDAO;
    
    public SalesEmployeeService() {
        this.salesEmployeeDAO = new SalesEmployeeDAO();
    }
    
    /**
     * Get all active sales employees
     */
    public List<SalesEmployee> getAllSalesEmployees() {
        return salesEmployeeDAO.getAllSalesEmployees();
    }
    
    /**
     * Get sales employee by ID
     */
    public SalesEmployee getSalesEmployeeById(int employeeId) {
        return salesEmployeeDAO.getSalesEmployeeById(employeeId);
    }
    
    /**
     * Get sales employee by code
     */
    public SalesEmployee getSalesEmployeeByCode(String employeeCode) {
        if (employeeCode == null || employeeCode.trim().isEmpty()) {
            return null;
        }
        return salesEmployeeDAO.getSalesEmployeeByCode(employeeCode.trim());
    }
    
    /**
     * Get sales employee by name
     */
    public SalesEmployee getSalesEmployeeByName(String employeeName) {
        if (employeeName == null || employeeName.trim().isEmpty()) {
            return null;
        }
        return salesEmployeeDAO.getSalesEmployeeByName(employeeName.trim());
    }
    
    /**
     * Search sales employees by code or name
     */
    public List<SalesEmployee> searchSalesEmployees(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllSalesEmployees();
        }
        return salesEmployeeDAO.searchSalesEmployees(searchTerm.trim());
    }
    
    /**
     * Get employee names for dropdown
     */
    public List<String> getEmployeeNames() {
        return salesEmployeeDAO.getEmployeeNames();
    }
    
    /**
     * Create new sales employee
     */
    public SalesEmployee createSalesEmployee(SalesEmployee employee) {
        // Validate employee data
        if (employee.getEmployeeCode() == null || employee.getEmployeeCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee code is required");
        }
        if (employee.getEmployeeName() == null || employee.getEmployeeName().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name is required");
        }
        
        return salesEmployeeDAO.createSalesEmployee(employee);
    }
    
    /**
     * Validate employee exists by code
     */
    public boolean employeeExists(String employeeCode) {
        return getSalesEmployeeByCode(employeeCode) != null;
    }
}
