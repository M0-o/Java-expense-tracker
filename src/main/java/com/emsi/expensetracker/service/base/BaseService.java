package com.emsi.expensetracker.service.base;

import com.emsi.expensetracker.dao.base.BaseDAOClass;

public abstract class BaseService {
    protected BaseDAOClass dao;

    public BaseService(BaseDAOClass dao) {
        this.dao = dao;
    }


}