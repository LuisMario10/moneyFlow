package com.moneyFlow.service;

import com.moneyFlow.DAO.UserDAO;
import com.moneyFlow.model.UserModel;
import com.moneyFlow.security.PasswordHasher;

public class AuthService {

    public static UserModel loggedUser;

    public static boolean signUp(String username, String password) {
        UserDAO userDAO = new UserDAO();

        if (userDAO.findByAccessName(username) != null) {
            return false;
        }
        String hashedPassword = PasswordHasher.hash(password);
        userDAO.create(new UserModel(username, hashedPassword));
        return true;
    }

    public static boolean login(String username, String password) {
        UserModel user = new UserDAO().findByAccessName(username);
        if (user == null) {
            return false;
        }

        if(PasswordHasher.check(password, user.getPassword()) == false) {
            return false;
        }

        loggedUser = user;
        return true;
    }
}