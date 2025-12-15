package com.emsi.expensetracker.service;

import com.emsi.expensetracker.util.DatabaseConnection;
import com.emsi.expensetracker.model.User;
import java.security.MessageDigest;
import java.sql.*;

public class AuthService {
    private static User currentUser;

    public static boolean register(String username, String password, String email) {
        try {
            String hashedPassword = hashPassword(password);
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO users (username, password, email) VALUES (?, ?, ?)"
            );
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, email);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean login(String username, String password) {
        try {
            String hashedPassword = hashPassword(password);
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT id, username, email FROM users WHERE username = ? AND password = ?"
            );
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                currentUser = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email")
                );
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    private static String hashPassword(String password) {
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
