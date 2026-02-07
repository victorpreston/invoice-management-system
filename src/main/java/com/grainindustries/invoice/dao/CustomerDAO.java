package com.grainindustries.invoice.dao;

import com.grainindustries.invoice.model.Customer;
import com.grainindustries.invoice.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    
    /**
     * Get all active customers
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer WHERE is_active = 1 ORDER BY customer_code";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
    
    /**
     * Get customer by ID
     */
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM Customer WHERE customer_id = ? AND is_active = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get customer by code
     */
    public Customer getCustomerByCode(String customerCode) {
        String sql = "SELECT * FROM Customer WHERE customer_code = ? AND is_active = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, customerCode);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Search customers by code or name
     */
    public List<Customer> searchCustomers(String searchTerm) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customer WHERE is_active = 1 " +
                     "AND (customer_code LIKE ? OR customer_name LIKE ?) " +
                     "ORDER BY customer_code";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
    
    /**
     * Get customer names for dropdown
     */
    public List<String> getCustomerNames() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT customer_name FROM Customer WHERE is_active = 1 ORDER BY customer_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                names.add(rs.getString("customer_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }
    
    /**
     * Get customer by name
     */
    public Customer getCustomerByName(String customerName) {
        String sql = "SELECT * FROM Customer WHERE customer_name = ? AND is_active = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, customerName);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Create new customer
     */
    public Customer createCustomer(Customer customer) {
        String sql = "INSERT INTO Customer (customer_code, customer_name, contact_person, " +
                     "customer_ref_no, local_currency, apply_cash_discount, client_type, " +
                     "phone, email, address, is_active, created_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1, GETDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, customer.getCustomerCode());
            ps.setString(2, customer.getCustomerName());
            ps.setString(3, customer.getContactPerson());
            ps.setString(4, customer.getCustomerRefNo());
            ps.setString(5, customer.getLocalCurrency());
            ps.setBoolean(6, customer.isApplyCashDiscount());
            ps.setString(7, customer.getClientType());
            ps.setString(8, customer.getPhone());
            ps.setString(9, customer.getEmail());
            ps.setString(10, customer.getAddress());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    customer.setCustomerId(rs.getInt(1));
                }
                return customer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Map ResultSet to Customer object
     */
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setCustomerCode(rs.getString("customer_code"));
        customer.setCustomerName(rs.getString("customer_name"));
        customer.setContactPerson(rs.getString("contact_person"));
        customer.setCustomerRefNo(rs.getString("customer_ref_no"));
        customer.setLocalCurrency(rs.getString("local_currency"));
        customer.setApplyCashDiscount(rs.getBoolean("apply_cash_discount"));
        customer.setClientType(rs.getString("client_type"));
        customer.setPhone(rs.getString("phone"));
        customer.setEmail(rs.getString("email"));
        customer.setAddress(rs.getString("address"));
        customer.setActive(rs.getBoolean("is_active"));
        Timestamp createdTs = rs.getTimestamp("created_date");
        if (createdTs != null) {
            customer.setCreatedDate(createdTs.toLocalDateTime());
        }
        return customer;
    }
}
