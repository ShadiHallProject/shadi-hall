package org.by9steps.shadihall.model;

import java.io.Serializable;

public class ProfitLoss implements Serializable {

    String ClientID;
    String CBDate;
    String Income;
    String Expense;
    String Profit;
    String Sorting;
    String month;

    private int isRow;

    public static ProfitLoss createRow(String clientID, String CBDate, String income, String expense, String profit) {
        ProfitLoss profitLoss = new ProfitLoss();
        profitLoss.isRow = 1;
        profitLoss.ClientID = clientID;
        profitLoss.CBDate = CBDate;
        profitLoss.Income = income;
        profitLoss.Expense = expense;
        profitLoss.Profit = profit;
        return profitLoss;
    }

    public static ProfitLoss createTotal(String income, String expense, String profit) {
        ProfitLoss profitLoss = new ProfitLoss();
        profitLoss.isRow = 2;
        profitLoss.Income = income;
        profitLoss.Expense = expense;
        profitLoss.Profit = profit;
        return profitLoss;
    }

    public static ProfitLoss createSection(String month) {
        ProfitLoss profitLoss = new ProfitLoss();
        profitLoss.isRow = 0;
        profitLoss.month = month;
        return profitLoss;
    }

    public String getClientID() {
        return ClientID;
    }

    public String getCBDate() {
        return CBDate;
    }

    public String getIncome() {
        return Income;
    }

    public String getExpense() {
        return Expense;
    }

    public String getProfit() {
        return Profit;
    }

    public String getSorting() {
        return Sorting;
    }

    public int isRow() {
        return isRow;
    }

    public String getMonth() {
        return month;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public void setCBDate(String CBDate) {
        this.CBDate = CBDate;
    }

    public void setIncome(String income) {
        Income = income;
    }

    public void setExpense(String expense) {
        Expense = expense;
    }

    public void setProfit(String profit) {
        Profit = profit;
    }

    public void setSorting(String sorting) {
        Sorting = sorting;
    }
}
