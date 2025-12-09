package com.emsi.expensetracker;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConnection {
    private static HikariDataSource dataSource;

    // Private constructor - prevent instantiation
    private DatabaseConnection() {}
    // Initialize the connection pool
    public static void initialize() {
        if (dataSource != null) return;

        Properties props = loadConfig();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("db.url", "jdbc:sqlite:expense_tracker.db"));
        config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.pool.size", "5")));
        
        // SQLite specific settings
        config.addDataSourceProperty("journal_mode", "WAL");
        config.addDataSourceProperty("synchronous", "NORMAL");

        dataSource = new HikariDataSource(config);

        createTables();
        seedIfEmpty();

        System.out.println("Database initialized");
    }

    // Load configuration from properties file
    private static Properties loadConfig() {
        Properties props = new Properties();
        try (InputStream input = DatabaseConnection.class.getResourceAsStream("/config.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (IOException e) {
            System.err.println("Could not load config, using defaults");
        }
        return props;
    }

    // Get a connection from the pool
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            initialize();
        }
        return dataSource.getConnection();
    }

    // Shutdown the pool when app closes
    public static void shutdown() {
        if (dataSource != null) {
            dataSource.close();
            System.out.println("Database connection pool closed");
        }
    }

    private static void createTables() {
        String createExpenses = """
            CREATE TABLE IF NOT EXISTS expenses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                description TEXT NOT NULL,
                amount REAL NOT NULL,
                category TEXT,
                date TEXT DEFAULT CURRENT_DATE
            )
            """;

        String createCategories = """
            CREATE TABLE IF NOT EXISTS categories (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE
            )
            """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createExpenses);
            stmt.execute(createCategories);
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    private static void seedIfEmpty() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM expenses")) {
            
            if (rs.getInt(1) == 0) {
                seedSampleData();
            }
        } catch (SQLException e) {
            System.err.println("Error checking database: " + e.getMessage());
        }
    }

    private static void seedSampleData() {
        String seedCategories = """
            INSERT INTO categories (name) VALUES
            ('Food'), ('Transport'), ('Entertainment'),
            ('Shopping'), ('Bills'), ('Other')
            """;

        String seedExpenses = """
            INSERT INTO expenses (description, amount, category, date) VALUES
            ('Groceries', 45.50, 'Food', '2025-12-01'),
            ('Bus ticket', 2.50, 'Transport', '2025-12-02'),
            ('Cinema', 12.00, 'Entertainment', '2025-12-03')
            """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(seedCategories);
            stmt.execute(seedExpenses);
            System.out.println("Sample data loaded");
        } catch (SQLException e) {
            System.err.println("Seed error: " + e.getMessage());
        }
    }
}