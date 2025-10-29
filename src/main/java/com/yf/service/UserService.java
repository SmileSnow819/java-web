package com.yf.service;

import com.yf.model.User;

/**
 * Business logic service interface for User operations.
 */
public interface UserService {
    /**
     * Handles user registration business logic.
     * @param uName Username.
     * @param uPwd Password.
     * @return true if registration is successful, false otherwise.
     */
    boolean addUser(String uName, String uPwd);

    /**
     * Handles user login business logic.
     * @param uName Username.
     * @param uPwd Password.
     * @return User object if login is successful, null otherwise.
     */
    User login(String uName, String uPwd);
}

