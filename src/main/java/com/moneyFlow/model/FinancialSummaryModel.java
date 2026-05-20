package com.moneyFlow.model;

public class FinancialSummaryModel { 
    private int id;
    private int userId;
    private String dateInit;
    private String dateEnd;
    private int totalCreditInCents;
    private int totalDebitInCents;
    private int resultInCents;

    public FinancialSummaryModel(int userId, String dateInit, String dateEnd, int totalCreditInCents, int totalDebitInCents, int resultInCents) {
        this.setUserId(userId);
        this.setDateInit(dateInit);
        this.setDateEnd(dateEnd);
        this.setTotalCreditInCents(totalCreditInCents);
        this.setTotalDebitInCents(totalDebitInCents);
        this.setResultInCents(resultInCents);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDateInit() {
        return dateInit;
    }

    public void setDateInit(String dateInit) {
        this.dateInit = dateInit;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public int getTotalCreditInCents() {
        return totalCreditInCents;
    }

    public void setTotalCreditInCents(int totalCreditInCents) {
        this.totalCreditInCents = totalCreditInCents;
    }

    public int getTotalDebitInCents() {
        return totalDebitInCents;
    }

    public void setTotalDebitInCents(int totalDebitInCents) {
        this.totalDebitInCents = totalDebitInCents;
    }

    public int getResultInCents() {
        return resultInCents;
    }

    public void setResultInCents(int resultInCents) {
        this.resultInCents = resultInCents;
    }
}