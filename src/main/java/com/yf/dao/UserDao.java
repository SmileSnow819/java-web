package com.yf.dao;
import com.yf.model.User;



/**
 * Data access object interface for User operations.
 */
public interface UserDao {
    /**
     * Adds a new user to the database (Registration).
     * @param user The user object to add.
     * @return true if successful, false otherwise.
     */
    boolean addUser(User user);

    /**
     * Checks user credentials for login.
     * @param uName Username.
     * @param uPwd Password.
     * @return A User object if login is successful, null otherwise.
     */
    User login(String uName, String uPwd);
}