package com.ruralyouth.database.dao;

import com.ruralyouth.database.DatabaseManager;
import com.ruralyouth.model.User;

import java.sql.*;
import java.util.*;

/**
 * Data Access Object for User operations
 * Handles database persistence for user data
 */
public class UserDAO {
    
    /**
     * Save a user to the database
     */
    public static void saveUser(User user) throws SQLException {
        String sql = "INSERT INTO users (id, name, age, education, location, latitude, longitude, max_distance) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON CONFLICT (id) DO UPDATE SET " +
                    "name = EXCLUDED.name, age = EXCLUDED.age, education = EXCLUDED.education, " +
                    "location = EXCLUDED.location, latitude = EXCLUDED.latitude, " +
                    "longitude = EXCLUDED.longitude, max_distance = EXCLUDED.max_distance";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setInt(3, user.getAge());
            pstmt.setString(4, user.getEducation());
            pstmt.setString(5, user.getLocation());
            pstmt.setDouble(6, user.getLatitude());
            pstmt.setDouble(7, user.getLongitude());
            pstmt.setDouble(8, user.getMaxDistance());
            
            pstmt.executeUpdate();
            
            // Save user skills
            saveUserSkills(user);
            
            // Save user preferences
            saveUserPreferences(user);
        }
    }
    
    /**
     * Load a user from the database by ID
     */
    public static User loadUser(String userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = new User(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("education"),
                    rs.getString("location")
                );
                
                user.setLatitude(rs.getDouble("latitude"));
                user.setLongitude(rs.getDouble("longitude"));
                user.setMaxDistance(rs.getDouble("max_distance"));
                
                // Load skills and preferences
                loadUserSkills(user);
                loadUserPreferences(user);
                
                return user;
            }
        }
        
        return null;
    }
    
    /**
     * Load all users from the database
     */
    public static List<User> loadAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY name";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = new User(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("education"),
                    rs.getString("location")
                );
                
                user.setLatitude(rs.getDouble("latitude"));
                user.setLongitude(rs.getDouble("longitude"));
                user.setMaxDistance(rs.getDouble("max_distance"));
                
                // Load skills and preferences
                loadUserSkills(user);
                loadUserPreferences(user);
                
                users.add(user);
            }
        }
        
        return users;
    }
    
    /**
     * Delete a user from the database
     */
    public static void deleteUser(String userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Save user skills to the database
     */
    private static void saveUserSkills(User user) throws SQLException {
        // First, ensure all skills exist in the skills table
        for (Map.Entry<String, Integer> skillEntry : user.getSkills().entrySet()) {
            ensureSkillExists(skillEntry.getKey());
        }
        
        // Delete existing user skills
        String deleteSql = "DELETE FROM user_skills WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
            pstmt.setString(1, user.getId());
            pstmt.executeUpdate();
        }
        
        // Insert new user skills
        String insertSql = "INSERT INTO user_skills (user_id, skill_id, proficiency) " +
                          "SELECT ?, s.id, ? FROM skills s WHERE s.name = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            
            for (Map.Entry<String, Integer> skillEntry : user.getSkills().entrySet()) {
                pstmt.setString(1, user.getId());
                pstmt.setInt(2, skillEntry.getValue());
                pstmt.setString(3, skillEntry.getKey());
                pstmt.executeUpdate();
            }
        }
    }
    
    /**
     * Load user skills from the database
     */
    private static void loadUserSkills(User user) throws SQLException {
        String sql = "SELECT s.name, us.proficiency FROM user_skills us " +
                    "JOIN skills s ON us.skill_id = s.id " +
                    "WHERE us.user_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getId());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String skillName = rs.getString("name");
                int proficiency = rs.getInt("proficiency");
                user.addSkill(skillName, proficiency);
            }
        }
    }
    
    /**
     * Save user preferences to the database
     */
    private static void saveUserPreferences(User user) throws SQLException {
        // Delete existing preferences
        String deleteSql = "DELETE FROM user_preferences WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
            pstmt.setString(1, user.getId());
            pstmt.executeUpdate();
        }
        
        // Insert new preferences
        String insertSql = "INSERT INTO user_preferences (user_id, preference) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            
            for (String preference : user.getPreferences()) {
                pstmt.setString(1, user.getId());
                pstmt.setString(2, preference);
                pstmt.executeUpdate();
            }
        }
    }
    
    /**
     * Load user preferences from the database
     */
    private static void loadUserPreferences(User user) throws SQLException {
        String sql = "SELECT preference FROM user_preferences WHERE user_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getId());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String preference = rs.getString("preference");
                user.addPreference(preference);
            }
        }
    }
    
    /**
     * Ensure a skill exists in the skills table
     */
    private static void ensureSkillExists(String skillName) throws SQLException {
        String sql = "INSERT INTO skills (name) VALUES (?) ON CONFLICT (name) DO NOTHING";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, skillName.toLowerCase());
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Get user count from database
     */
    public static int getUserCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        
        return 0;
    }
    
    /**
     * Search users by location
     */
    public static List<User> searchUsersByLocation(String location) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE location ILIKE ? ORDER BY name";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + location + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User user = new User(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("education"),
                    rs.getString("location")
                );
                
                user.setLatitude(rs.getDouble("latitude"));
                user.setLongitude(rs.getDouble("longitude"));
                user.setMaxDistance(rs.getDouble("max_distance"));
                
                loadUserSkills(user);
                loadUserPreferences(user);
                
                users.add(user);
            }
        }
        
        return users;
    }
} 