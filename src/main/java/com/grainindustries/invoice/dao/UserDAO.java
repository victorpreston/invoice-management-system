package com.grainindustries.invoice.dao;

import com.grainindustries.invoice.model.User;
import com.grainindustries.invoice.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;

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
