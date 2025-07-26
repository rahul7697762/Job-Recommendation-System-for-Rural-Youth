package com.ruralyouth.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Utility class to switch between MySQL and Supabase database configurations
 */
public class DatabaseSwitcher {
    
    private static final String CONFIG_FILE = "database.properties";
    private static final String MYSQL_CONFIG = "database_mysql.properties";
    private static final String SUPABASE_CONFIG = "database_supabase.properties";
    
    /**
     * Switch to MySQL configuration
     */
    public static boolean switchToMySQL() {
        return switchConfig(MYSQL_CONFIG, "MySQL");
    }
    
    /**
     * Switch to Supabase configuration
     */
    public static boolean switchToSupabase() {
        return switchConfig(SUPABASE_CONFIG, "Supabase");
    }
    
    /**
     * Switch database configuration by copying the appropriate config file
     */
    private static boolean switchConfig(String sourceConfig, String dbType) {
        try {
            File sourceFile = new File(sourceConfig);
            File targetFile = new File(CONFIG_FILE);
            
            if (!sourceFile.exists()) {
                System.err.println("❌ Configuration file not found: " + sourceConfig);
                return false;
            }
            
            // Backup existing config if it exists
            if (targetFile.exists()) {
                Files.copy(targetFile.toPath(), 
                          new File(CONFIG_FILE + ".backup").toPath(), 
                          StandardCopyOption.REPLACE_EXISTING);
                System.out.println("📋 Backed up existing configuration");
            }
            
            // Copy new configuration
            Files.copy(sourceFile.toPath(), targetFile.toPath(), 
                      StandardCopyOption.REPLACE_EXISTING);
            
            System.out.println("✅ Switched to " + dbType + " configuration");
            System.out.println("📝 Please update the database credentials in " + CONFIG_FILE);
            
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ Failed to switch to " + dbType + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check current database configuration
     */
    public static void checkCurrentConfig() {
        File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            System.out.println("❌ No database configuration found");
            System.out.println("💡 Run switchToMySQL() or switchToSupabase() to set up configuration");
            return;
        }
        
        System.out.println("📋 Current database configuration:");
        DatabaseConfig.printConfig();
    }
    
    /**
     * Test database connection
     */
    public static void testConnection() {
        try {
            System.out.println("🔍 Testing database connection...");
            DatabaseManager.initialize();
            System.out.println("✅ Database connection successful!");
        } catch (Exception e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            System.out.println("💡 Please check your database configuration and credentials");
        }
    }
    
    /**
     * Main method for command-line usage
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }
        
        switch (args[0].toLowerCase()) {
            case "mysql":
                switchToMySQL();
                break;
            case "supabase":
                switchToSupabase();
                break;
            case "check":
                checkCurrentConfig();
                break;
            case "test":
                testConnection();
                break;
            default:
                printUsage();
        }
    }
    
    private static void printUsage() {
        System.out.println("Database Configuration Switcher");
        System.out.println("===============================");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java DatabaseSwitcher mysql     - Switch to MySQL configuration");
        System.out.println("  java DatabaseSwitcher supabase  - Switch to Supabase configuration");
        System.out.println("  java DatabaseSwitcher check     - Check current configuration");
        System.out.println("  java DatabaseSwitcher test      - Test database connection");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java -cp target/classes com.ruralyouth.database.DatabaseSwitcher mysql");
        System.out.println("  java -cp target/classes com.ruralyouth.database.DatabaseSwitcher supabase");
    }
} 