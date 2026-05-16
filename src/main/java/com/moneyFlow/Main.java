package com.moneyFlow;

import com.moneyFlow.controller.UserController;
import com.moneyFlow.view.HomeView;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
           UserController userController = new UserController();
           userController.start();
        });
    }
}