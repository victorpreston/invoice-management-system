package com.grainindustries.invoice.dao;

import com.grainindustries.invoice.model.User;
import com.grainindustries.invoice.util.DatabaseConnection;

import java.sql.*;

public class UserDAO {
    
    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password_hash = ? AND is_active = 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = mapResultSetToUser(rs);
                updateLastLogin(user.getUserId());
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public User createUser(String username, String password, String fullName, String email) {
        // Check if username already exists
        if (usernameExists(username)) {
            return null; // Username already taken
        }
        
        String sql = "INSERT INTO Users (username, password_hash, full_name, email, is_active, created_date) " +
                     "VALUES (?, ?, ?, ?, 1, GETDATE())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, fullName);
            ps.setString(4, email);
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int userId = rs.getInt(1);
                    User user = new User();
                    user.setUserId(userId);
                    user.setUsername(username);
                    user.setFullName(fullName);
                    user.setEmail(email);
                    user.setActive(true);
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM Users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private void updateLastLogin(int userId) {
        String sql = "UPDATE Users SET last_login = GETDATE() WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setActive(rs.getBoolean("is_active"));
        
        Timestamp createdTs = rs.getTimestamp("created_date");
        if (createdTs != null) {
            user.setCreatedDate(createdTs.toLocalDateTime());
        }
        
        Timestamp lastLoginTs = rs.getTimestamp("last_login");
        if (lastLoginTs != null) {
            user.setLastLogin(lastLoginTs.toLocalDateTime());
        }
        
        return user;
    }
}
