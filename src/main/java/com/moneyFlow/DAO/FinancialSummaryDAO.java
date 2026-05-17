package com.moneyFlow.DAO;

import java.util.ArrayList;
import java.util.List;

import com.moneyFlow.model.FinancialSummaryModel;
import com.moneyFlow.config.ConnectionDataBase;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class FinancialSummaryDAO implements IGenericDAO<FinancialSummaryModel> {

    private String sql;
    private PreparedStatement ps;
    private ResultSet rs;

    @Override
    public List<FinancialSummaryModel> findAll() {
        return null;
    }

    public List<FinancialSummaryModel> findAll(int userId) {

        List<FinancialSummaryModel> list = new ArrayList<>();

        this.sql = "SELECT * FROM financial_summary WHERE user_id = ?";
        try {
            ps = ConnectionDataBase.getConnectionWithDataBase().prepareStatement(this.sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                FinancialSummaryModel financialSummary = new FinancialSummaryModel(
                    rs.getInt("user_id"),
                    rs.getString("date_init"),
                    rs.getString("date_end"),
                    rs.getInt("total_credit_in_cents"),
                    rs.getInt("total_debit_in_cents"),
                    rs.getInt("result_in_cents")
                );
                financialSummary.setId(rs.getInt("id"));
                list.add(financialSummary);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public FinancialSummaryModel findByID(int id) {
        return null;
    }

    public FinancialSummaryModel findByID(int userId, int id) {
        this.sql = "SELECT * FROM financial_summary WHERE id = ? AND user_id = ?";
        try {
            ps = ConnectionDataBase.getConnectionWithDataBase().prepareStatement(this.sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                FinancialSummaryModel financialSummary = new FinancialSummaryModel(
                    rs.getInt("user_id"),
                    rs.getString("date_init"),
                    rs.getString("date_end"),
                    rs.getInt("total_credit_in_cents"),
                    rs.getInt("total_debit_in_cents"),
                    rs.getInt("result_in_cents")
                );
                financialSummary.setId(rs.getInt("id"));
                return financialSummary;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (rs != null) rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public FinancialSummaryModel findLatestByUserId(int userId) {
        this.sql = "SELECT * FROM financial_summary WHERE user_id = ? ORDER BY id DESC LIMIT 1";
        try {
            ps = ConnectionDataBase.getConnectionWithDataBase().prepareStatement(this.sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                FinancialSummaryModel financialSummary = new FinancialSummaryModel(
                    rs.getInt("user_id"),
                    rs.getString("date_init"),
                    rs.getString("date_end"),
                    rs.getInt("total_credit_in_cents"),
                    rs.getInt("total_debit_in_cents"),
                    rs.getInt("result_in_cents")
                );
                financialSummary.setId(rs.getInt("id"));
                return financialSummary;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (rs != null) rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public int create(FinancialSummaryModel entity) {
        this.sql = "INSERT INTO financial_summary (user_id, date_init, date_end, total_credit_in_cents, total_debit_in_cents, result_in_cents) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            ps = ConnectionDataBase.getConnectionWithDataBase().prepareStatement(this.sql);
            ps.setInt(1, entity.getUserId());
            ps.setString(2, entity.getDateInit());
            ps.setString(3, entity.getDateEnd());
            ps.setInt(4, entity.getTotalCreditInCents());
            ps.setInt(5, entity.getTotalDebitInCents());
            ps.setInt(6, entity.getResultInCents());
            
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public void updateCredit(int userId, int id, int totalCreditInCents) {
        this.sql = "UPDATE financial_summary SET total_credit_in_cents = ? WHERE id = ? AND user_id = ?";
        try {
            ps = ConnectionDataBase.getConnectionWithDataBase().prepareStatement(this.sql);
            ps.setInt(1, totalCreditInCents);
            ps.setInt(2, id);
            ps.setInt(3, userId);
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

    @Override
    public void update(FinancialSummaryModel entity) {   
    }

    public void updateDebit(int userId, int id, int totalDebitInCents) {
        this.sql = "UPDATE financial_summary SET total_debit_in_cents = ? WHERE id = ? AND user_id = ?";
        try {
            ps = ConnectionDataBase.getConnectionWithDataBase().prepareStatement(this.sql);
            ps.setInt(1, totalDebitInCents);
            ps.setInt(2, id);
            ps.setInt(3, userId);
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

    public void updateResult(int userId, int id, int resultInCents) {
        this.sql = "UPDATE financial_summary SET result_in_cents = ? WHERE id = ? AND user_id = ?";
        try {
            ps = ConnectionDataBase.getConnectionWithDataBase().prepareStatement(this.sql);
            ps.setInt(1, resultInCents);
            ps.setInt(2, id);
            ps.setInt(3, userId);
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

    public void delete(int id) {}

    public void delete(int userId, int id) {
        this.sql = "DELETE FROM financial_summary WHERE id = ? AND user_id = ?";
        try {
            ps = ConnectionDataBase.getConnectionWithDataBase().prepareStatement(this.sql);
            ps.setInt(1, id);
            ps.setInt(2, userId);
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
