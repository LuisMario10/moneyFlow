package com.moneyFlow.DAO;

import com.moneyFlow.model.FinancialTransactionModel;
import com.moneyFlow.config.ConnectionDataBase;
import java.sql.PreparedStatement;

public class FinancialTransactionDAO {
    public void create(FinancialTransactionModel financialTransaction) {
        String sql = "INSERT INTO financial_transaction (title, user_id, type, amount_in_cents, result_in_cents, date, category, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = ConnectionDataBase.getConnectionWithDataBase().prepareStatement(sql);
            ps.setString(1, financialTransaction.getTitle());
            ps.setInt(2, financialTransaction.getUserId());
            // Mapeia EFinancialType para CREDIT/DEBIT conforme o CHECK constraint da tabela
            String type = financialTransaction.getAmountInCents() >= 0 ? "CREDIT" : "DEBIT";
            ps.setString(3, type);
            ps.setInt(4, Math.abs(financialTransaction.getAmountInCents()));
            ps.setInt(5, financialTransaction.getResultInCents());
            ps.setString(6, financialTransaction.getDate());
            ps.setString(7, financialTransaction.getCategory());
            ps.setString(8, financialTransaction.getDescription());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}