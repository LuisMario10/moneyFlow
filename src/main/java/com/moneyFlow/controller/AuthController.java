package com.moneyFlow.controller;

import com.moneyFlow.service.AuthService;
import com.moneyFlow.view.HomeView;
import javax.swing.JOptionPane;


public class AuthController {
    
    public static void login(String username, String password) {
        System.out.printf("Login %s - Senha %s", username, password);
        if(AuthService.login(username, password)) {
            JOptionPane.showMessageDialog(null, "Login realizado com sucesso, aproveite o APP !", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            HomeView homeView = new HomeView();
            homeView.setVisible(true);
            
       } else {
            JOptionPane.showMessageDialog(null, "Usuário ou senha inválidos!", "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
       }
    }
}
