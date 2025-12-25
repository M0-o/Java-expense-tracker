package com.emsi.expensetracker.dao.implementation;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.emsi.expensetracker.dao.base.BaseDAOClass;
import com.emsi.expensetracker.model.User;
import com.emsi.expensetracker.model.utils.UserKey;
import com.emsi.expensetracker.util.DatabaseConnection;

/**
 * Data Access Object (DAO) for authentication-related database operations.
 * Handles user authentication, registration, and password hashing. Uses a
 * composite key (username and password) for user identification.
 */
public class AuthDAO extends BaseDAOClass<User, UserKey> {

    /**
     * Constructs a new AuthDAO with the specified database connection.
     *
     * @param dbConnection The database connection to use for operations
     */
    public AuthDAO(DatabaseConnection dbConnection) {
        super(dbConnection);
    }

    /**
     * Saves a new user to the database with a hashed password. The password is
     * automatically hashed using SHA-256 before storage.
     *
     * @param user The user to save (password will be hashed)
     * @return true if the user was saved successfully, false otherwise
     */
    @Override
    public boolean save(User user) {
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (username, password, email) VALUES (?, ?, ?)"
        )) {
            String hashedPassword = hashPassword(user.getPassword());
            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, user.getEmail());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates an existing user in the database. Currently not implemented.
     *
     * @param user The user to update
     * @return false (not implemented)
     */
    @Override
    public boolean update(User user) {
        // Implementation here if needed
        return false;
    }

    /**
     * Deletes a user from the database. Currently not implemented.
     *
     * @param key The composite key containing username and password
     * @return false (not implemented)
     */
    @Override
    public boolean delete(UserKey key) {

        return false;
    }

    /**
     * Finds a user by their username and password credentials. The password is
     * hashed and compared against the stored hash.
     *
     * @param key The composite key containing username and password
     * @return The user if found with matching credentials, null otherwise
     */
    @Override
    public User findById(UserKey key) {
        try (Connection conn = dbConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "SELECT id, username, email FROM users WHERE username = ? AND password = ?"
        )) {
            String hashedPassword = hashPassword(key.getPassword());
            stmt.setString(1, key.getUsername());
            stmt.setString(2, hashedPassword);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            hashedPassword
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Retrieves all users from the database. Currently not implemented.
     *
     * @return null (not implemented)
     */
    @Override
    public List<User> findAll() {
        // Implementation here if needed
        return null;
    }

    /**
     * Hashes a password using SHA-256 algorithm. This method is private and
     * used internally to secure passwords.
     *
     * @param password The plain text password to hash
     * @return The hashed password as a hexadecimal string
     * @throws RuntimeException if the SHA-256 algorithm is not available
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
