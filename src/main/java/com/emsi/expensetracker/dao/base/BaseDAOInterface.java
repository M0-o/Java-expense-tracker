package com.emsi.expensetracker.dao.base;

import java.util.List;

public interface BaseDAOInterface<T, ID> {
    T findById(ID id);
    List<T> findAll();
    boolean save(T entity);
    boolean update(T entity);
    boolean delete(ID id);
}