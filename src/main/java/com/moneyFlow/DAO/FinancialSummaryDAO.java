package com.moneyFlow.DAO;

import java.util.List;

import com.moneyFlow.model.FinancialTransactionModel;


public class FinancialSummaryDAO implements IGenericDAO<FinancialTransactionModel> {
    @Override
    public List<FinancialTransactionModel> findAll() {
        return null;
    }

    @Override
    public FinancialTransactionModel findByID(int id) {
        return null;
    }

    @Override
    public int create(FinancialTransactionModel entity) {
        return 0;
    }

    @Override
    public void update(FinancialTransactionModel entity) {
        
    }

    @Override
    public void delete(int id) {

    }
}
