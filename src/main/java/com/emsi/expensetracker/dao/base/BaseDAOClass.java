package com.emsi.expensetracker.dao.base;

import com.emsi.expensetracker.util.DatabaseConnection;


public abstract class BaseDAOClass<T, ID> implements BaseDAOInterface<T, ID> {

    
 protected final DatabaseConnection dbConnection ;

    public BaseDAOClass(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

}