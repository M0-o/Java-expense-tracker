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

public class AuthDAO extends BaseDAOClass<User, UserKey> {


    public AuthDAO(DatabaseConnection dbConnection) {
        super(dbConnection);
    }

    @Override
    public  boolean save(User user) {
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

    @Override
    public boolean update(User user) {
        // Implementation here if needed
        return false;
    }

    @Override
    public boolean delete(UserKey key) {
        
        return false;
    }

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
    @Override
    public List<User> findAll() {
        // Implementation here if needed
        return null;
    }

    private  String hashPassword(String password) {
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
