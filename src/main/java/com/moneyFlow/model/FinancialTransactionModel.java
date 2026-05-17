package com.moneyFlow.model;

public class FinancialTransactionModel { 
    
    private int userId;
    private String title;
    private EFinancialType type;
    private int amountInCents;
    private String date;
    private String category;
    private String description;
    
    public FinancialTransactionModel(int userId, String title, EFinancialType type, int amountInCents, String date, String category, String description) {
        this.setUserId(userId);
        this.setTitle(title);
        this.setType(type);
        this.setAmountInCents(amountInCents);
        this.setDate(date);
        this.setCategory(category);
        this.setDescription(description);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EFinancialType getType() {
        return type;
    }

    public void setType(EFinancialType type) {
        this.type = type;
    }

    public int getAmountInCents() {
        return amountInCents;
    }

    public void setAmountInCents(int amountInCents) {
        this.amountInCents = amountInCents;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}