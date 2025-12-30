package com.emsi.expensetracker.service.implementation;

import java.util.List;

import com.emsi.expensetracker.dao.implementation.ExpenseDAO;
import com.emsi.expensetracker.model.Expense;
import com.emsi.expensetracker.service.base.BaseService;

/**
 * Service class for expense management operations. Provides business logic for
 * creating, retrieving, updating, and deleting user expenses. Acts as an
 * intermediary between controllers and the ExpenseDAO.
 */
public class ExpenseService extends BaseService<ExpenseDAO> {

    public ExpenseService(ExpenseDAO dao) {
        super(dao);
    }

    /**
     * Create a new expense for a user
     *
     * @param expense Expense object containing details
     * @return true if created successfully, false otherwise
     */
    public boolean createExpense(Expense expense) {
        // Validation
        if (expense == null) {
            return false;
        }
        if (expense.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        return dao.save(expense);
    }

    /**
     * Get an expense by its ID
     *
     * @param id The expense ID
     * @return Expense object or null if not found
     */
    public Expense getExpenseById(int id) {
        return dao.findById(id);
    }

    /**
     * Get all expenses for a specific user
     *
     * @param userId The user's ID
     * @return List of user's expenses
     */
    public List<Expense> getExpensesByUserId(int userId) {
        return dao.findByUserId(userId);
    }

    /**
     * Get all expenses
     *
     * @return List of all expenses
     */
    public List<Expense> getAllExpenses() {
        return dao.findAll();
    }

    /**
     * Update an existing expense
     *
     * @param expense The expense with updated values
     * @return true if updated successfully, false otherwise
     */
    public boolean updateExpense(Expense expense) {
        if (expense == null || expense.getId() <= 0) {
            return false;
        }
        if (expense.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        return dao.update(expense);
    }

    /**
     * Delete an expense by its ID
     *
     * @param id The expense ID to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteExpense(int id) {
        if (id <= 0) {
            return false;
        }
        return dao.delete(id);
    }

    /**
     * Calculate total expenses for a user
     *
     * @param userId The user's ID
     * @return Total amount of all expenses
     */
    public double getTotalExpensesByUser(int userId) {
        List<Expense> expenses = dao.findByUserId(userId);
        return expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }
}
