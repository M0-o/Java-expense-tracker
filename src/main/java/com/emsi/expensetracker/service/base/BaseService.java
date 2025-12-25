package com.emsi.expensetracker.service.base;

import com.emsi.expensetracker.dao.base.BaseDAOClass;

/**
 * Abstract base class for all service layer implementations. This class
 * provides a common structure for services by maintaining a reference to a Data
 * Access Object (DAO) for database operations. Services implement business
 * logic and act as an intermediary between controllers and data access layers.
 */
public abstract class BaseService {

    /**
     * The Data Access Object used for database operations. Protected to allow
     * subclasses to access it directly.
     */
    protected BaseDAOClass dao;

    /**
     * Constructs a new BaseService with the specified DAO.
     *
     * @param dao The Data Access Object to use for database operations
     */
    public BaseService(BaseDAOClass dao) {
        this.dao = dao;
    }

}
