package com.moneyFlow.controller;

import com.moneyFlow.view.LoginView;
import com.moneyFlow.view.SignUpView;

public class UserController {
    private LoginView loginView;
    private SignUpView signUpView;

    public void iniciar() {
        showLogin();
    }

    public void showLogin() {
        if (signUpView != null) {
            signUpView.dispose();
        }

        this.loginView = new LoginView();

        loginView.getSignUpBtn().addActionListener(e -> showSignUp());

        loginView.setVisible(true);
    }

    public void showSignUp() {
        if (loginView != null) {
            loginView.dispose();
        }

        this.signUpView = new SignUpView();

        signUpView.getGoToLoginBtn().addActionListener(e -> showLogin());

        signUpView.setVisible(true);
    }
}