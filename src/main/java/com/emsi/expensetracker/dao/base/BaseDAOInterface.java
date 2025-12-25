package com.emsi.expensetracker.dao.base;

import java.util.List;

/**
 * Base Data Access Object (DAO) interface that defines common CRUD operations.
 * This interface provides a contract for all DAO implementations to ensure
 * consistency across different data access layers.
 *
 * @param <T> The entity type that this DAO manages
 * @param <ID> The type of the entity's identifier
 */
public interface BaseDAOInterface<T, ID> {

    /**
     * Finds an entity by its identifier.
     *
     * @param id The identifier of the entity to find
     * @return The entity if found, null otherwise
     */
    T findById(ID id);

    /**
     * Retrieves all entities of type T from the database.
     *
     * @return A list of all entities, empty list if none found
     */
    List<T> findAll();

    /**
     * Persists a new entity to the database.
     *
     * @param entity The entity to save
     * @return true if the entity was saved successfully, false otherwise
     */
    boolean save(T entity);

    /**
     * Updates an existing entity in the database.
     *
     * @param entity The entity with updated values
     * @return true if the entity was updated successfully, false otherwise
     */
    boolean update(T entity);

    /**
     * Deletes an entity from the database by its identifier.
     *
     * @param id The identifier of the entity to delete
     * @return true if the entity was deleted successfully, false otherwise
     */
    boolean delete(ID id);
}
