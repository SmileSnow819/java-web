package com.yf.service.impl;

import com.yf.dao.UserDao;
import com.yf.dao.impl.UserDaoImpl;
import com.yf.model.User;
import com.yf.service.UserService;

/**
 * Implementation of UserService interface.
 * Contains core business logic (though minimal here, usually for validation).
 */
public class UserServiceImpl implements UserService {
    // Dependency on DAO layer
    private UserDao userDao = new UserDaoImpl();

    @Override
    public boolean addUser(String uName, String uPwd) {
        // Simple validation: check if inputs are present
        if (uName == null || uName.trim().isEmpty() || uPwd == null || uPwd.trim().isEmpty()) {
            return false;
        }

        User user = new User(uName, uPwd);
        // Call DAO layer to persist the data
        return userDao.addUser(user);
    }

    @Override
    public User login(String uName, String uPwd) {
        // Simple validation
        if (uName == null || uName.trim().isEmpty() || uPwd == null || uPwd.trim().isEmpty()) {
            return null;
        }

        // Call DAO layer to check credentials
        return userDao.login(uName, uPwd);
    }
}

