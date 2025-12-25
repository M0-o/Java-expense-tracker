package com.emsi.expensetracker.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.emsi.expensetracker.dao.base.BaseDAOClass;
import com.emsi.expensetracker.model.Category;
import com.emsi.expensetracker.util.DatabaseConnection;

/**
 * Data Access Object (DAO) for category-related database operations. Handles
 * CRUD operations and custom queries for expense categories. Supports both
 * user-specific and default system categories.
 */
public class CategoryDAO extends BaseDAOClass<Category, Integer> {

    /**
     * Constructs a new CategoryDAO with the specified database connection.
     *
     * @param dbConnection The database connection to use for operations
     */
    public CategoryDAO(DatabaseConnection dbConnection) {
        super(dbConnection);
    }

    /**
     * Finds a category by its unique identifier.
     *
     * @param id The category ID to search for
     * @return The category if found, null otherwise
     */
    @Override
    public Category findById(Integer id) {
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM categories WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("user_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all categories from the database.
     *
     * @return A list of all categories, empty list if none found
     */
    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM categories")) {

            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("user_id")
                );
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    /**
     * Saves a new category to the database.
     *
     * @param category The category to save
     * @return true if saved successfully, false otherwise
     */
    @Override
    public boolean save(Category category) {
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO categories (name, description, user_id) VALUES (?, ?, ?)")) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getUserId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates an existing category in the database.
     *
     * @param category The category with updated values
     * @return true if updated successfully, false otherwise
     */
    @Override
    public boolean update(Category category) {
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "UPDATE categories SET name = ?, description = ?, user_id = ? WHERE id = ?")) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getUserId());
            stmt.setInt(4, category.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a category from the database by its ID.
     *
     * @param id The category ID to delete
     * @return true if deleted successfully, false otherwise
     */
    @Override
    public boolean delete(Integer id) {
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM categories WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Find all categories for a specific user
     *
     * @param userId The user's ID
     * @return List of categories belonging to the user
     */
    public List<Category> findByUserId(int userId) {
        List<Category> categories = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM categories WHERE user_id = ?")) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("user_id")
                    );
                    categories.add(category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    /**
     * Find a category by name and user ID
     *
     * @param name Category name
     * @param userId User's ID
     * @return Category if found, null otherwise
     */
    public Category findByNameAndUserId(String name, int userId) {
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM categories WHERE name = ? AND user_id = ?")) {
            stmt.setString(1, name);
            stmt.setInt(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("user_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get default/system categories (where user_id is 0 or NULL)
     *
     * @return List of default categories
     */
    public List<Category> findDefaultCategories() {
        List<Category> categories = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM categories WHERE user_id = 0 OR user_id IS NULL")) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("user_id")
                    );
                    categories.add(category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
}
