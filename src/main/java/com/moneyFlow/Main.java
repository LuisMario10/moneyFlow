package com.moneyFlow;

import com.moneyFlow.view.HomeView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HomeView homeView = new HomeView();
            homeView.setVisible(true);
        });
    }
}