package com.moneyFlow.controller;

import com.moneyFlow.model.FinancialTransactionModel;
import com.moneyFlow.DAO.FinancialTransactionDAO;

public class FinancialTransactionController {
    public void post(FinancialTransactionModel financialTransaction) {
        FinancialTransactionDAO financialTransactionDAO = new FinancialTransactionDAO();
        financialTransactionDAO.create(financialTransaction);
    }
}
