package com.moneyFlow.controller;

import com.moneyFlow.model.FinancialSummaryModel;
import com.moneyFlow.DAO.FinancialSummaryDAO;

public class FinancialSummaryController {

    private FinancialSummaryDAO financialSummaryDAO = new FinancialSummaryDAO();

    public void post(FinancialSummaryModel financialSummary) {
        // Busca o último resumo financeiro do usuário (mês anterior)
        FinancialSummaryModel lastSummary = financialSummaryDAO.findLatestByUserId(financialSummary.getUserId());

        if (lastSummary != null) {
            int previousBalance = lastSummary.getResultInCents();

            // Puxa o saldo total do mês anterior e coloca no atual
            if (previousBalance > 0) {
                financialSummary.setTotalCreditInCents(financialSummary.getTotalCreditInCents() + previousBalance);
            } else if (previousBalance < 0) {
                financialSummary.setTotalDebitInCents(financialSummary.getTotalDebitInCents() + Math.abs(previousBalance));
            }
        }

        // Calcula o resultado inicial do novo mês
        int result = financialSummary.getTotalCreditInCents() - financialSummary.getTotalDebitInCents();
        financialSummary.setResultInCents(result);

        int id = financialSummaryDAO.create(financialSummary);
    }

    /**
     * Atualiza o financial_summary quando uma transação é criada.
     * Se não existir um summary para o usuário, cria um novo.
     * Retorna o novo result_in_cents (saldo atualizado).
     */
    public int updateSummaryWithTransaction(int userId, int amountInCents, boolean isIncome) {
        FinancialSummaryModel summary = financialSummaryDAO.findLatestByUserId(userId);

        if (summary == null) {
            // Cria um novo summary para o usuário
            int credit = isIncome ? Math.abs(amountInCents) : 0;
            int debit = isIncome ? 0 : Math.abs(amountInCents);
            int result = credit - debit;

            FinancialSummaryModel newSummary = new FinancialSummaryModel(
                userId,
                java.time.LocalDate.now().withDayOfMonth(1).toString(),
                java.time.LocalDate.now().withDayOfMonth(java.time.LocalDate.now().lengthOfMonth()).toString(),
                credit,
                debit,
                result
            );
            financialSummaryDAO.create(newSummary);
            return result;
        }

        // Atualiza o summary existente
        int summaryId = summary.getId();

        if (isIncome) {
            int newCredit = summary.getTotalCreditInCents() + Math.abs(amountInCents);
            financialSummaryDAO.updateCredit(userId, summaryId, newCredit);
            int newResult = newCredit - summary.getTotalDebitInCents();
            financialSummaryDAO.updateResult(userId, summaryId, newResult);
            return newResult;
        } else {
            int newDebit = summary.getTotalDebitInCents() + Math.abs(amountInCents);
            financialSummaryDAO.updateDebit(userId, summaryId, newDebit);
            int newResult = summary.getTotalCreditInCents() - newDebit;
            financialSummaryDAO.updateResult(userId, summaryId, newResult);
            return newResult;
        }
    }

    /**
     * Retorna o saldo atual (result_in_cents) do último summary do usuário.
     * Retorna 0 se não existir nenhum summary.
     */
    public int getBalance(int userId) {
        FinancialSummaryModel summary = financialSummaryDAO.findLatestByUserId(userId);
        if (summary != null) {
            return summary.getResultInCents();
        }
        return 0;
    }
}