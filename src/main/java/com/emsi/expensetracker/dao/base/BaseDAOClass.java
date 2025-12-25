package com.emsi.expensetracker.dao.base;

import com.emsi.expensetracker.util.DatabaseConnection;

/**
 * Abstract base class for all Data Access Object (DAO) implementations. This
 * class provides common functionality and enforces a consistent structure for
 * database operations across all DAO classes.
 *
 * @param <T> The entity type that this DAO manages
 * @param <ID> The type of the entity's identifier
 */
public abstract class BaseDAOClass<T, ID> implements BaseDAOInterface<T, ID> {

    /**
     * Database connection instance used for all database operations. Protected
     * to allow subclasses to access it directly.
     */
    protected final DatabaseConnection dbConnection;

    /**
     * Constructor that initializes the DAO with a database connection.
     *
     * @param dbConnection The database connection instance to use
     */
    public BaseDAOClass(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

}
