package com.moneyFlow;

import com.moneyFlow.view.UserAccountManagementView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserAccountManagementView userAcc = new UserAccountManagementView();
            userAcc.setVisible(true);
        });
    }
}