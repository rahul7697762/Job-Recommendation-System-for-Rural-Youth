package com.ruralyouth.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Database configuration supporting both MySQL and Supabase/PostgreSQL
 * Automatically detects database type and configures appropriate connection
 */
public class DatabaseConfig {
    
    private static final String CONFIG_FILE = "database.properties";
    private static final Properties properties = new Properties();
    
    // Database types
    public enum DatabaseType {
        MYSQL, SUPABASE, POSTGRESQL
    }
    
    // Default Supabase configuration
    private static final String DEFAULT_HOST = "localhost";
    private static final String DEFAULT_PORT = "5432";
    private static final String DEFAULT_DATABASE = "rural_youth_jobs";
    private static final String DEFAULT_USERNAME = "postgres";
    private static final String DEFAULT_PASSWORD = "password";
    private static final DatabaseType DEFAULT_DB_TYPE = DatabaseType.SUPABASE;
    
    static {
        loadProperties();
    }
    
    /**
     * Load database properties from file or use defaults
     */
    private static void loadProperties() {
        try {
            // Try to load from properties file
            FileInputStream fis = new FileInputStream(CONFIG_FILE);
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            // Use default values if file not found
            System.out.println("Database config file not found, using defaults and environment variables");
        }
    }
    
    /**
     * Get database type from environment variable or properties file
     */
    public static DatabaseType getDatabaseType() {
        String dbType = getProperty("DB_TYPE", "database.type", DEFAULT_DB_TYPE.name());
        try {
            return DatabaseType.valueOf(dbType.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid database type: " + dbType + ". Using default: " + DEFAULT_DB_TYPE);
            return DEFAULT_DB_TYPE;
        }
    }
    
    /**
     * Get database host from environment variable or properties file
     */
    public static String getHost() {
        return getProperty("DB_HOST", "database.host", DEFAULT_HOST);
    }
    
    /**
     * Get database port from environment variable or properties file
     */
    public static String getPort() {
        DatabaseType dbType = getDatabaseType();
        String defaultPort = dbType == DatabaseType.MYSQL ? "3306" : "5432";
        return getProperty("DB_PORT", "database.port", defaultPort);
    }
    
    /**
     * Get database name from environment variable or properties file
     */
    public static String getDatabase() {
        return getProperty("DB_NAME", "database.name", DEFAULT_DATABASE);
    }
    
    /**
     * Get database username from environment variable or properties file
     */
    public static String getUsername() {
        return getProperty("DB_USER", "database.username", DEFAULT_USERNAME);
    }
    
    /**
     * Get database password from environment variable or properties file
     */
    public static String getPassword() {
        return getProperty("DB_PASSWORD", "database.password", DEFAULT_PASSWORD);
    }
    
    /**
     * Get database URL based on database type
     */
    public static String getDatabaseUrl() {
        DatabaseType dbType = getDatabaseType();
        
        switch (dbType) {
            case MYSQL:
                return String.format("jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", 
                                   getHost(), getPort(), getDatabase());
            case SUPABASE:
            case POSTGRESQL:
                return String.format("jdbc:postgresql://%s:%s/%s", 
                                   getHost(), getPort(), getDatabase());
            default:
                throw new IllegalStateException("Unsupported database type: " + dbType);
        }
    }
    
    /**
     * Get appropriate JDBC driver class name
     */
    public static String getDriverClassName() {
        DatabaseType dbType = getDatabaseType();
        
        switch (dbType) {
            case MYSQL:
                return "com.mysql.cj.jdbc.Driver";
            case SUPABASE:
            case POSTGRESQL:
                return "org.postgresql.Driver";
            default:
                throw new IllegalStateException("Unsupported database type: " + dbType);
        }
    }
    
    /**
     * Get connection pool size
     */
    public static int getMaxPoolSize() {
        String poolSize = getProperty("DB_POOL_SIZE", "database.pool.size", "10");
        return Integer.parseInt(poolSize);
    }
    
    /**
     * Get connection timeout
     */
    public static int getConnectionTimeout() {
        String timeout = getProperty("DB_TIMEOUT", "database.timeout", "30000");
        return Integer.parseInt(timeout);
    }
    
    /**
     * Get property with fallback order: environment variable -> properties file -> default
     */
    private static String getProperty(String envKey, String propKey, String defaultValue) {
        // First try environment variable
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue;
        }
        
        // Then try properties file
        String propValue = properties.getProperty(propKey);
        if (propValue != null && !propValue.trim().isEmpty()) {
            return propValue;
        }
        
        // Finally use default
        return defaultValue;
    }
    
    /**
     * Check if database configuration is valid
     */
    public static boolean isValid() {
        return getHost() != null && !getHost().isEmpty() &&
               getPort() != null && !getPort().isEmpty() &&
               getDatabase() != null && !getDatabase().isEmpty() &&
               getUsername() != null && !getUsername().isEmpty() &&
               getPassword() != null && !getPassword().isEmpty();
    }
    
    /**
     * Print current database configuration (without password)
     */
    public static void printConfig() {
        System.out.println("Database Configuration:");
        System.out.println("  Type: " + getDatabaseType());
        System.out.println("  Host: " + getHost());
        System.out.println("  Port: " + getPort());
        System.out.println("  Database: " + getDatabase());
        System.out.println("  Username: " + getUsername());
        System.out.println("  Driver: " + getDriverClassName());
        System.out.println("  Pool Size: " + getMaxPoolSize());
        System.out.println("  Timeout: " + getConnectionTimeout() + "ms");
        System.out.println("  URL: " + getDatabaseUrl());
    }
} 