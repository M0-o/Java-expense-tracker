package com.emsi.expensetracker.model.utils;

import java.util.Objects;

public class UserKey {

    private final String username;
    private final String password;

    public UserKey(String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserKey userKey = (UserKey) o;
        return username.equals(userKey.username) && password.equals(userKey.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
