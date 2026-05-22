package com.moneyFlow.DAO;

import com.moneyFlow.model.FinancialTransactionModel;
import com.moneyFlow.model.EFinancialType;
import com.moneyFlow.config.ConnectionDataBase;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

    public List<FinancialTransactionModel> findAllByUserId(int userId) {
        String sql = "SELECT * FROM financial_transaction WHERE user_id = ?";
        List<FinancialTransactionModel> list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = ConnectionDataBase.getConnectionWithDataBase().prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                String typeStr = rs.getString("type");
                int amount = rs.getInt("amount_in_cents");
                // Se for DEBIT, representamos como valor negativo em centavos
                if ("DEBIT".equalsIgnoreCase(typeStr)) {
                    amount = -amount;
                }
                
                FinancialTransactionModel transaction = new FinancialTransactionModel(
                    rs.getInt("user_id"),
                    rs.getString("title"),
                    EFinancialType.VARIABLE,
                    amount,
                    rs.getInt("result_in_cents"),
                    rs.getString("date"),
                    rs.getString("category"),
                    rs.getString("description")
                );
                list.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}