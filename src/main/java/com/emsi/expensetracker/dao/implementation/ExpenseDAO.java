package com.emsi.expensetracker.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.emsi.expensetracker.dao.base.BaseDAOClass;
import com.emsi.expensetracker.model.Expense;
import com.emsi.expensetracker.util.DatabaseConnection;

/**
 * Data Access Object (DAO) for expense-related database operations. Handles
 * CRUD operations and custom queries for user expenses. Provides methods to
 * retrieve expenses by user and manage expense records.
 */
public class ExpenseDAO extends BaseDAOClass<Expense, Integer> {

    /**
     * Constructs a new ExpenseDAO with the specified database connection.
     *
     * @param dbConnection The database connection to use for operations
     */
    public ExpenseDAO(DatabaseConnection dbConnection) {
        super(dbConnection);
    }

    /**
     * Finds an expense by its unique identifier.
     *
     * @param id The expense ID to search for
     * @return The expense if found, null otherwise
     */
    @Override
    public Expense findById(Integer id) {
        try (Connection conn = dbConnection.getConnection(); var stmt = conn.prepareStatement("Select * from expenses where id=?")) {
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return new Expense(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getDouble("amount"),
                        rs.getInt("category_id"),
                        java.time.LocalDate.parse(rs.getString("date")),
                        rs.getInt("user_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all expenses from the database.
     *
     * @return A list of all expenses, empty list if none found
     */
    @Override
    public List<Expense> findAll() {
        List<Expense> expenses = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM expenses")) {

            while (rs.next()) {
                Expense expense = new Expense(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getDouble("amount"),
                        rs.getInt("category_id"),
                        java.time.LocalDate.parse(rs.getString("date")),
                        rs.getInt("user_id")
                );
                expenses.add(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

    /**
     * Saves a new expense to the database.
     *
     * @param expense The expense to save
     * @return true if saved successfully, false otherwise
     */
    @Override
    public boolean save(Expense expense) {
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO expenses (description, amount, date ,category_id, user_id) VALUES (?, ?, ?, ?, ?)"
        )) {
            stmt.setString(1, expense.getDescription());
            stmt.setDouble(2, expense.getAmount());
            stmt.setString(3, expense.getDate().toString());
            stmt.setInt(4, expense.getCategoryId());
            stmt.setInt(5, expense.getUserId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Updates an existing expense in the database.
     *
     * @param expense The expense with updated values
     * @return true if updated successfully, false otherwise
     */
    @Override
    public boolean update(Expense expense) {
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "UPDATE expenses SET description = ?, amount = ?, date = ? , category_id = ? WHERE id = ?"
        )) {
            stmt.setString(1, expense.getDescription());
            stmt.setDouble(2, expense.getAmount());
            stmt.setString(3, expense.getDate().toString());
            stmt.setInt(4, expense.getCategoryId());
            stmt.setInt(5, expense.getId());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes an expense from the database by its ID.
     *
     * @param id The expense ID to delete
     * @return true if deleted successfully, false otherwise
     */
    @Override
    public boolean delete(Integer id) {
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM expenses WHERE id = ?"
        )) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves all expenses for a specific user.
     *
     * @param userId The ID of the user whose expenses to retrieve
     * @return A list of expenses belonging to the user, empty list if none
     * found
     */
    public List<Expense> findByUserId(int userId) {
        List<Expense> expenses = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM expenses WHERE user_id = ?")) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Expense expense = new Expense(
                            rs.getInt("id"),
                            rs.getString("description"),
                            rs.getDouble("amount"),
                            rs.getInt("category_id"),
                            java.time.LocalDate.parse(rs.getString("date")),
                            rs.getInt("user_id")
                    );
                    expenses.add(expense);
                }
                return expenses;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }
}
