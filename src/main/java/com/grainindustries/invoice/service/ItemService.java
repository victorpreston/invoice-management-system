package com.grainindustries.invoice.service;

import com.grainindustries.invoice.dao.ItemDAO;
import com.grainindustries.invoice.model.Item;

import java.util.List;

/**
 * Service layer for Item operations
 * Handles business logic and coordinates between UI and DAO
 */
public class ItemService {
    
    private final ItemDAO itemDAO;
    
    public ItemService() {
        this.itemDAO = new ItemDAO();
    }
    
    /**
     * Get all active items
     */
    public List<Item> getAllItems() {
        return itemDAO.getAllItems();
    }
    
    /**
     * Get item by ID
     */
    public Item getItemById(int itemId) {
        return itemDAO.getItemById(itemId);
    }
    
    /**
     * Get item by item number
     */
    public Item getItemByNo(String itemNo) {
        if (itemNo == null || itemNo.trim().isEmpty()) {
            return null;
        }
        return itemDAO.getItemByNo(itemNo.trim());
    }
    
    /**
     * Search items by item number or description
     */
    public List<Item> searchItems(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllItems();
        }
        return itemDAO.searchItems(searchTerm.trim());
    }
    
    /**
     * Get items by type (Item or Service)
     */
    public List<Item> getItemsByType(String itemType) {
        if (itemType == null || itemType.trim().isEmpty()) {
            return getAllItems();
        }
        return itemDAO.getItemsByType(itemType.trim());
    }
    
    /**
     * Create new item
     */
    public Item createItem(Item item) {
        // Validate item data
        if (item.getItemNo() == null || item.getItemNo().trim().isEmpty()) {
            throw new IllegalArgumentException("Item number is required");
        }
        if (item.getItemDescription() == null || item.getItemDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Item description is required");
        }
        if (item.getUnitPrice() == null) {
            throw new IllegalArgumentException("Unit price is required");
        }
        
        return itemDAO.createItem(item);
    }
    
    /**
     * Validate item exists by item number
     */
    public boolean itemExists(String itemNo) {
        return getItemByNo(itemNo) != null;
    }
}
