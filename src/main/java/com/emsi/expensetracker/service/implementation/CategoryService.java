package com.emsi.expensetracker.service.implementation;

import java.util.List;

import com.emsi.expensetracker.dao.implementation.CategoryDAO;
import com.emsi.expensetracker.model.Category;
import com.emsi.expensetracker.service.base.BaseService;

/**
 * Service class for category management operations. Provides business logic for
 * creating, retrieving, updating, and deleting expense categories. Handles both
 * user-specific categories and default system categories. Enforces business
 * rules such as uniqueness validation and ownership verification.
 */
public class CategoryService extends BaseService {

    /**
     * The CategoryDAO instance for database operations.
     */
    private final CategoryDAO categoryDAO;

    /**
     * Constructs a new CategoryService with the specified CategoryDAO.
     *
     * @param dao The CategoryDAO to use for category-related database
     * operations
     */
    public CategoryService(CategoryDAO dao) {
        super(dao);
        this.categoryDAO = dao;
    }

    /**
     * Create a new category for a user
     *
     * @param name Category name
     * @param description Category description
     * @param userId User's ID
     * @return true if created successfully, false otherwise
     */
    public boolean createCategory(String name, String description, int userId) {
        // Validation
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        // Check if category already exists for this user
        if (categoryDAO.findByNameAndUserId(name, userId) != null) {
            return false; // Category with this name already exists for user
        }

        Category category = new Category(name.trim(), description, userId);
        return categoryDAO.save(category);
    }

    /**
     * Get a category by ID
     *
     * @param categoryId Category ID
     * @return Category object or null
     */
    public Category getCategoryById(int categoryId) {
        return categoryDAO.findById(categoryId);
    }

    /**
     * Get all categories for a specific user
     *
     * @param userId User's ID
     * @return List of user's categories
     */
    public List<Category> getUserCategories(int userId) {
        return categoryDAO.findByUserId(userId);
    }

    /**
     * Get all categories (including default system categories)
     *
     * @return List of all categories
     */
    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    /**
     * Get default/system categories
     *
     * @return List of default categories
     */
    public List<Category> getDefaultCategories() {
        return categoryDAO.findDefaultCategories();
    }

    /**
     * Get categories available to a user (user's categories + default
     * categories)
     *
     * @param userId User's ID
     * @return Combined list of user and default categories
     */
    public List<Category> getAvailableCategories(int userId) {
        List<Category> categories = categoryDAO.findByUserId(userId);
        categories.addAll(categoryDAO.findDefaultCategories());
        return categories;
    }

    /**
     * Update an existing category
     *
     * @param categoryId Category ID
     * @param name New name
     * @param description New description
     * @param userId User ID (for verification)
     * @return true if updated successfully, false otherwise
     */
    public boolean updateCategory(int categoryId, String name, String description, int userId) {
        // Validation
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        // Get existing category
        Category existingCategory = categoryDAO.findById(categoryId);
        if (existingCategory == null) {
            return false;
        }

        // Verify ownership
        if (existingCategory.getUserId() != userId) {
            return false; // User doesn't own this category
        }

        // Update fields
        existingCategory.setName(name.trim());
        existingCategory.setDescription(description);

        return categoryDAO.update(existingCategory);
    }

    /**
     * Delete a category
     *
     * @param categoryId Category ID
     * @param userId User ID (for verification)
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteCategory(int categoryId, int userId) {
        // Get existing category
        Category existingCategory = categoryDAO.findById(categoryId);
        if (existingCategory == null) {
            return false;
        }

        // Verify ownership
        if (existingCategory.getUserId() != userId) {
            return false; // User doesn't own this category
        }

        return categoryDAO.delete(categoryId);
    }

    /**
     * Check if a category name already exists for a user
     *
     * @param name Category name
     * @param userId User's ID
     * @return true if exists, false otherwise
     */
    public boolean categoryExists(String name, int userId) {
        return categoryDAO.findByNameAndUserId(name, userId) != null;
    }

    /**
     * Get category by name for a specific user
     *
     * @param name Category name
     * @param userId User's ID
     * @return Category if found, null otherwise
     */
    public Category getCategoryByName(String name, int userId) {
        return categoryDAO.findByNameAndUserId(name, userId);
    }
}
