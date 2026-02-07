package com.grainindustries.invoice.dao;

import com.grainindustries.invoice.model.SalesEmployee;
import com.grainindustries.invoice.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesEmployeeDAO {
    
    /**
     * Get all active sales employees
     */
    public List<SalesEmployee> getAllSalesEmployees() {
        List<SalesEmployee> employees = new ArrayList<>();
        String sql = "SELECT * FROM SalesEmployee WHERE is_active = 1 ORDER BY employee_code";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                employees.add(mapResultSetToSalesEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
    
    /**
     * Get sales employee by ID
     */
    public SalesEmployee getSalesEmployeeById(int employeeId) {
        String sql = "SELECT * FROM SalesEmployee WHERE employee_id = ? AND is_active = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSalesEmployee(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get sales employee by code
     */
    public SalesEmployee getSalesEmployeeByCode(String employeeCode) {
        String sql = "SELECT * FROM SalesEmployee WHERE employee_code = ? AND is_active = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, employeeCode);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSalesEmployee(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Search sales employees by code or name
     */
    public List<SalesEmployee> searchSalesEmployees(String searchTerm) {
        List<SalesEmployee> employees = new ArrayList<>();
        String sql = "SELECT * FROM SalesEmployee WHERE is_active = 1 " +
                     "AND (employee_code LIKE ? OR employee_name LIKE ?) " +
                     "ORDER BY employee_code";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                employees.add(mapResultSetToSalesEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
    
    /**
     * Get employee names for dropdown
     */
    public List<String> getEmployeeNames() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT employee_name FROM SalesEmployee WHERE is_active = 1 ORDER BY employee_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                names.add(rs.getString("employee_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }
    
    /**
     * Get sales employee by name
     */
    public SalesEmployee getSalesEmployeeByName(String employeeName) {
        String sql = "SELECT * FROM SalesEmployee WHERE employee_name = ? AND is_active = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, employeeName);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSalesEmployee(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Create new sales employee
     */
    public SalesEmployee createSalesEmployee(SalesEmployee employee) {
        String sql = "INSERT INTO SalesEmployee (employee_code, employee_name, phone, email, " +
                     "is_active, created_date) VALUES (?, ?, ?, ?, 1, GETDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, employee.getEmployeeCode());
            ps.setString(2, employee.getEmployeeName());
            ps.setString(3, employee.getPhone());
            ps.setString(4, employee.getEmail());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    employee.setEmployeeId(rs.getInt(1));
                }
                return employee;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Map ResultSet to SalesEmployee object
     */
    private SalesEmployee mapResultSetToSalesEmployee(ResultSet rs) throws SQLException {
        SalesEmployee employee = new SalesEmployee();
        employee.setEmployeeId(rs.getInt("employee_id"));
        employee.setEmployeeCode(rs.getString("employee_code"));
        employee.setEmployeeName(rs.getString("employee_name"));
        employee.setPhone(rs.getString("phone"));
        employee.setEmail(rs.getString("email"));
        employee.setActive(rs.getBoolean("is_active"));
        Timestamp createdTs = rs.getTimestamp("created_date");
        if (createdTs != null) {
            employee.setCreatedDate(createdTs.toLocalDateTime());
        }
        return employee;
    }
}
