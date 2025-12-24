package com.emsi.expensetracker.dao.implementation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.emsi.expensetracker.dao.base.BaseDAO;
import com.emsi.expensetracker.model.Expense;
import com.emsi.expensetracker.util.DatabaseConnection;



public class ExpenseDAO implements BaseDAO<Expense,Integer> {

    @Override 
     public Expense findById(Integer id) {
        try( Connection conn= DatabaseConnection.getConnection();
             var stmt= conn.prepareStatement("Select * from expenses where id=?")){
                stmt.setInt(1,id);
                var rs= stmt.executeQuery();
                if(rs.next()){
                    return new Expense(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getDouble("amount"),
                        rs.getInt("category_id"),
                        rs.getString("date"),
                        rs.getInt("user_id")
                    );  
             }
    }catch(SQLException e){
        e.printStackTrace();
    }
    return null;
     }

    @Override
    public List<Expense> findAll() {
        List<Expense> expenses = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM expenses")) {

            while (rs.next()) {
                Expense expense = new Expense(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getDouble("amount"),
                        rs.getInt("category_id"),
                        rs.getString("date"),
                        rs.getInt("user_id")
                );
                expenses.add(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }

     @Override
     public boolean save(Expense expense) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO expenses (description, amount, date ,category_id, user_id) VALUES (?, ?, ?, ?, ?)"
             )) {
            stmt.setString(1, expense.getDescription());
            stmt.setDouble(2, expense.getAmount());
            stmt.setString(3, expense.getDate());
            stmt.setInt(4, expense.getCategoryId()); 
            stmt.setInt(5, expense.getUserId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        return false;
     }

     }

     @Override
     public boolean update(Expense expense) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE expenses SET description = ?, amount = ?, date = ? , category_id = ? WHERE id = ?"
             )) {
            stmt.setString(1, expense.getDescription());
            stmt.setDouble(2, expense.getAmount());
            stmt.setString(3, expense.getDate());
            stmt.setInt(4, expense.getCategoryId());
            stmt.setInt(5, expense.getId());
        
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
     @Override
     public boolean delete(Integer id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM expenses WHERE id = ?"
             )) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
     }
}
