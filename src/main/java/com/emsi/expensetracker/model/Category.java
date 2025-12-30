package com.emsi.expensetracker.model;

public class Category {

    private int id;
    private String name;
    private String description;
    private int userId;

    public Category(int id, String name, String description, int userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.userId = userId;
    }

    public Category(String name, String description, int userId) {
        this.name = name;
        this.description = description;
        this.userId = userId;
    }

    public Category() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getUserId() {
        return userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return name; // Display category name in ChoiceBox
    }
}
