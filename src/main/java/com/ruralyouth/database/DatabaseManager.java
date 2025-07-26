package com.ruralyouth.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database connection manager using HikariCP for connection pooling
 * Provides efficient database connections for the job recommendation system
 */
public class DatabaseManager {
    
    private static HikariDataSource dataSource;
    private static boolean initialized = false;
    
    /**
     * Initialize the database connection pool
     */
    public static void initialize() {
        if (dataSource != null) {
            return; // Already initialized
        }
        
        try {
            // Configure HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(DatabaseConfig.getDatabaseUrl());
            config.setUsername(DatabaseConfig.getUsername());
            config.setPassword(DatabaseConfig.getPassword());
            config.setDriverClassName(DatabaseConfig.getDriverClassName());
            config.setMaximumPoolSize(DatabaseConfig.getMaxPoolSize());
            config.setConnectionTimeout(DatabaseConfig.getConnectionTimeout());
            config.setConnectionTestQuery("SELECT 1");
            
            // Additional MySQL-specific settings
            if (DatabaseConfig.getDatabaseType() == DatabaseConfig.DatabaseType.MYSQL) {
                config.addDataSourceProperty("cachePrepStmts", "true");
                config.addDataSourceProperty("prepStmtCacheSize", "250");
                config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            }
            
            dataSource = new HikariDataSource(config);
            
            // Test connection
            try (Connection conn = dataSource.getConnection()) {
                System.out.println("✅ Database connection established successfully!");
                DatabaseConfig.printConfig();
            }
            
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize database connection: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    
    /**
     * Get a database connection from the pool
     */
    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            initialize();
        }
        return dataSource.getConnection();
    }
    
    /**
     * Close the database connection pool
     */
    public static void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("Database connection pool closed");
        }
    }
    
    /**
     * Test database connectivity
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT 1");
            return true;
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get connection pool statistics
     */
    public static void printPoolStats() {
        if (dataSource != null) {
            System.out.println("Connection Pool Statistics:");
            System.out.println("  Active Connections: " + dataSource.getHikariPoolMXBean().getActiveConnections());
            System.out.println("  Idle Connections: " + dataSource.getHikariPoolMXBean().getIdleConnections());
            System.out.println("  Total Connections: " + dataSource.getHikariPoolMXBean().getTotalConnections());
            System.out.println("  Threads Awaiting Connection: " + dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
    }
    
    /**
     * Check if database is available
     */
    public static boolean isAvailable() {
        return initialized && dataSource != null && !dataSource.isClosed();
    }
} 