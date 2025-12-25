package com.emsi.expensetracker.service.implementation;

import com.emsi.expensetracker.dao.implementation.AuthDAO;
import com.emsi.expensetracker.model.User;
import com.emsi.expensetracker.model.utils.UserKey;
import com.emsi.expensetracker.service.base.BaseService;

/**
 * Service class for authentication and user management operations. Handles user
 * registration, login, logout, and session management. Maintains the current
 * authenticated user's session.
 */
public class AuthService extends BaseService<AuthDAO> {

    /**
     * The currently authenticated user, null if no user is logged in.
     */
    private User currentUser;

    /**
     * Constructs a new AuthService with the specified AuthDAO.
     *
     * @param dao The AuthDAO to use for authentication-related database
     * operations
     */
    public AuthService(AuthDAO dao) {
        super(dao);
    }

    /**
     * Registers a new user in the system. Creates a new user with the provided
     * credentials and saves to database.
     *
     * @param username The username for the new account
     * @param password The password for the new account (will be hashed)
     * @param email The email address for the new account
     * @return true if registration was successful, false otherwise
     */
    public boolean register(String username, String password, String email) {
        User user = new User(username, email, password);
        boolean result = dao.save(user);
        return result;
    }

    /**
     * Authenticates a user with the provided credentials. If successful, sets
     * the current user session.
     *
     * @param username The username to authenticate
     * @param password The password to verify
     * @return true if authentication was successful, false otherwise
     */
    public boolean login(String username, String password) {
        UserKey key = new UserKey(username, password);
        currentUser = (User) dao.findById(key);
        return currentUser != null;
    }

    /**
     * Logs out the current user by clearing the session. Sets the current user
     * to null.
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Retrieves the currently authenticated user.
     *
     * @return The current user if logged in, null otherwise
     */
    public User getCurrentUser() {
        return currentUser;
    }

}
