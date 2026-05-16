package com.moneyFlow.service;

import com.moneyFlow.config.ConnectionDataBase;
import com.moneyFlow.security.PasswordHasher;
import com.moneyFlow.DAO.UserDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    public static boolean signUp(String username, String password) {
        if (UserDAO.isUsernameTaken(username)) {
            return false;
        }
        return UserDAO.saveUser(username, password);
    }

    public static boolean login(String username, String password) {
        return UserDAO.validateLogin(username, password);
    }
}