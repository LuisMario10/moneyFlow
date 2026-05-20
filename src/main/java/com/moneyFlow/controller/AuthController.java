package com.moneyFlow.controller;

import com.moneyFlow.service.AuthService;
import com.moneyFlow.view.HomeView;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class AuthController {
    
    public void login(String username, String password, JFrame loginView) {
        boolean isValid = AuthService.login(username, password);
        
        if (isValid) {
            JOptionPane.showMessageDialog(loginView, "Logado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            if (loginView != null) {
                loginView.dispose();
            }
            HomeView homeView = new HomeView();
            homeView.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(loginView, "login invalido", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
