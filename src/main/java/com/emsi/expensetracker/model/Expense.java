
package com.emsi.expensetracker.model;
import java.time.LocalDate ;

public class Expense {
    private int id;
    private String description;
    private double amount;
    private LocalDate date;
    private int categoryId;
    private int userId;

    public Expense(int id, String description, double amount, int categoryId, LocalDate date, int userId) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.categoryId = categoryId;
        this.date = date;
    }
    
    public Expense( String description, double amount, int categoryId, LocalDate date, int userId){
        this.description = description;
        this.amount = amount;
        this.categoryId = categoryId;
        this.date = date;
        this.userId = userId;
    }
    
    public int getId() { return id; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public int getCategoryId() { return categoryId; }
    public int getUserId() { return userId; }
    public void setId(int id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public void setUserId(int userId) { this.userId = userId; }
}