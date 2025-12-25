package com.emsi.expensetracker.service.implementation;

import com.emsi.expensetracker.dao.implementation.AuthDAO;
import com.emsi.expensetracker.model.User;
import com.emsi.expensetracker.model.utils.UserKey;
import com.emsi.expensetracker.service.base.BaseService;

public class AuthService  extends BaseService{
    private  User currentUser;

    public AuthService(AuthDAO dao) {
        super(dao);
    }

    public boolean register(String username, String password, String email) {
      User user = new User(username, email, password);
      boolean result = dao.save(user);
      return result;
    }

    public boolean login(String username, String password) {
        UserKey key = new UserKey(username, password);
        currentUser = (User) dao.findById(key);
        return currentUser != null;
    }

    public  void logout() {
        currentUser = null;
    }

    public  User getCurrentUser() {
        return currentUser;
    }

  
}
