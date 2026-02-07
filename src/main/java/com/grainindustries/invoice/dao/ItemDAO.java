package com.grainindustries.invoice.dao;

import com.grainindustries.invoice.model.Item;
import com.grainindustries.invoice.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    
    /**
     * Get all active items
     */
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM Item WHERE is_active = 1 ORDER BY item_no";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                items.add(mapResultSetToItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
    
    /**
     * Get item by ID
     */
    public Item getItemById(int itemId) {
        String sql = "SELECT * FROM Item WHERE item_id = ? AND is_active = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, itemId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToItem(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get item by item number
     */
    public Item getItemByNo(String itemNo) {
        String sql = "SELECT * FROM Item WHERE item_no = ? AND is_active = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, itemNo);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToItem(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Search items by item number or description
     */
    public List<Item> searchItems(String searchTerm) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM Item WHERE is_active = 1 " +
                     "AND (item_no LIKE ? OR item_description LIKE ?) " +
                     "ORDER BY item_no";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(mapResultSetToItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
    
    /**
     * Get items by type
     */
    public List<Item> getItemsByType(String itemType) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM Item WHERE is_active = 1 AND item_type = ? ORDER BY item_no";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, itemType);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                items.add(mapResultSetToItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
    
    /**
     * Create new item
     */
    public Item createItem(Item item) {
        String sql = "INSERT INTO Item (item_no, item_description, item_type, uom_code, " +
                     "unit_price, vat_code, is_active, created_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 1, GETDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, item.getItemNo());
            ps.setString(2, item.getItemDescription());
            ps.setString(3, item.getItemType());
            ps.setString(4, item.getUomCode());
            ps.setBigDecimal(5, item.getUnitPrice());
            ps.setString(6, item.getVatCode());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    item.setItemId(rs.getInt(1));
                }
                return item;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Map ResultSet to Item object
     */
    private Item mapResultSetToItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setItemId(rs.getInt("item_id"));
        item.setItemNo(rs.getString("item_no"));
        item.setItemDescription(rs.getString("item_description"));
        item.setItemType(rs.getString("item_type"));
        item.setUomCode(rs.getString("uom_code"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setVatCode(rs.getString("vat_code"));
        item.setActive(rs.getBoolean("is_active"));
        Timestamp createdTs = rs.getTimestamp("created_date");
        if (createdTs != null) {
            item.setCreatedDate(createdTs.toLocalDateTime());
        }
        return item;
    }
}
