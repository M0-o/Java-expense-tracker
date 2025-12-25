package com.emsi.expensetracker.service.implementation;

import com.emsi.expensetracker.dao.implementation.ExpenseDAO;
import com.emsi.expensetracker.model.Expense;
import com.emsi.expensetracker.service.base.BaseService;

/**
 * Service class for expense management operations. Provides business logic for
 * creating, retrieving, updating, and deleting user expenses. Acts as an
 * intermediary between controllers and the ExpenseDAO.
 *
 * TODO: Implement expense CRUD operations and business logic.
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
        
        return dao.save(expense);
    }

}
