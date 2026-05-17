package com.moneyFlow.controller;

import com.moneyFlow.DAO.UserDAO;
import com.moneyFlow.service.AuthService;
import com.moneyFlow.view.LoginView;
import com.moneyFlow.view.SignUpView;
import com.moneyFlow.view.HomeView;
import javax.swing.*;

public class UserController {
    private LoginView loginView;
    private SignUpView signUpView;
    private HomeView homeView;

    public void start() {
        showLogin();
    }

    public void showLogin() {
        if (signUpView != null) signUpView.dispose();

        this.loginView = new LoginView();

        loginView.getSignUpBtn().addActionListener(e -> showSignUp());
        loginView.getLoginBtn().addActionListener(e -> handleLogin());

        loginView.setVisible(true);
    }

    public void showSignUp() {
        if (loginView != null) loginView.dispose();

        this.signUpView = new SignUpView();

        signUpView.getGoToLoginBtn().addActionListener(e -> showLogin());
        signUpView.getSignUpBtn().addActionListener(e -> handleSignUp());

        signUpView.setVisible(true);
    }

    public void showHome() {
        if (loginView != null) loginView.dispose();

        this.homeView = new HomeView();

        homeView.setVisible(true);
    }

    private void handleSignUp() {
        String user = signUpView.getUsernameField().getText();
        String pass = new String(signUpView.getPasswordField().getPassword());
        String confirmPass = new String(signUpView.getConfirmPasswordField().getPassword());

        if (user.length() < 5) {
            showError("O nome de usuário deve ter no mínimo 5 caracteres.");
            return;
        }

        if (pass.length() < 8) {
            showError("A senha deve conter no mínimo 8 caracteres.");
            return;
        }

        if (!pass.equals(confirmPass)) {
            showError("As senhas não coincidem.");
            return;
        }

        if (AuthService.signUp(user, pass)) {
            JOptionPane.showMessageDialog(signUpView, "Conta criada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            showLogin();
        }
        else {
            showError("Este nome de usuário já está em uso.");
        }
    }

    public void handleLogin() {
        String user = loginView.getUsernameField().getText();
        String pass = new String(loginView.getPasswordField().getPassword());

        if (AuthService.login(user, pass)) {
            JOptionPane.showMessageDialog(loginView, "Logado com sucesso!", "Sucesso",  JOptionPane.INFORMATION_MESSAGE);
            showHome();
        }
        else {
            showError("Verifique se o email e a senha estão corretos!");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(signUpView, message, "Erro de Validação", JOptionPane.ERROR_MESSAGE);
    }
}   