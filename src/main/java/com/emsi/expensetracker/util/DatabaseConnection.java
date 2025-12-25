package com.emsi.expensetracker.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConnection {

    private final HikariDataSource dataSource;

    // Private constructor - prevent instantiation
    public DatabaseConnection() {

        Properties props = loadConfig();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("db.url"));
        config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.pool.size")));

        // SQLite specific settings
        config.addDataSourceProperty("journal_mode", "WAL");
        config.addDataSourceProperty("synchronous", "NORMAL");

        this.dataSource = new HikariDataSource(config);

    }

    // Initialize the connection pool
    public void initialize() {

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
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Shutdown the pool when app closes
    public void shutdown() {
        if (dataSource != null) {
            dataSource.close();
            System.out.println("Database connection pool closed");
        }
    }

    private void createTables() {
        String createUsers = """
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT UNIQUE NOT NULL,
            password TEXT NOT NULL,
            email TEXT,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )
        """;

        String createExpenses = """
        CREATE TABLE IF NOT EXISTS expenses (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            description TEXT NOT NULL,
            amount REAL NOT NULL,
            date TEXT DEFAULT CURRENT_DATE,
            user_id INT REFERENCES users(id) ON DELETE CASCADE,
            category_id INT REFERENCES categories(id) ON DELETE SET NULL
        )
        """;

        String createCategories = """
        CREATE TABLE IF NOT EXISTS categories (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL UNIQUE,
            user_id INT REFERENCES users(id) ON DELETE CASCADE,
            description TEXT 
        )
        """;

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(createUsers);
            stmt.execute(createExpenses);
            stmt.execute(createCategories);
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    private void seedIfEmpty() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM categories")) {

            if (rs.getInt(1) == 0) {
                seedSampleData();
            }
        } catch (SQLException e) {
            System.err.println("Error checking database: " + e.getMessage());
        }
    }

    private void seedSampleData() {
        String seedCategories = """
            INSERT INTO categories (name) VALUES
            ('Food'), ('Transport'), ('Entertainment'),
            ('Shopping'), ('Bills'), ('Other')
            """;

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(seedCategories);
            System.out.println("Categories loaded");
        } catch (SQLException e) {
            System.err.println("Seed error: " + e.getMessage());
        }
    }
}
