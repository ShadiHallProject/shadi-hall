package org.by9steps.shadihall.model;

import java.io.Serializable;

public class BalSheet implements Serializable {

    String CBDate;
    String Capital;
    String ProfitLoss;
    String Liabilities;
    String C_P_L;
    String Assets;
    String ClientID;
    String Sorting;
    String month;

    private int isRow;

//    public BalSheet(String CBDate, String capital, String profitLoss, String liabilities, String c_P_L, String assets, String clientID, String sorting) {
//        this.CBDate = CBDate;
//        Capital = capital;
//        ProfitLoss = profitLoss;
//        Liabilities = liabilities;
//        C_P_L = c_P_L;
//        Assets = assets;
//        ClientID = clientID;
//        Sorting = sorting;
//    }
    public static BalSheet createRow(String CBDate, String capital, String profitLoss, String liabilities, String c_P_L, String assets, String clientID) {

        BalSheet balSheet = new BalSheet();
        balSheet.isRow = 1;
        balSheet.CBDate = CBDate;
        balSheet.Capital = capital;
        balSheet.ProfitLoss = profitLoss;
        balSheet.Liabilities = liabilities;
        balSheet.C_P_L = c_P_L;
        balSheet.Assets = assets;
        balSheet.ClientID = clientID;

        return balSheet;
    }

    public static BalSheet createTotal(String capital, String profitLoss, String liabilities, String c_P_L, String assets) {

        BalSheet balSheet = new BalSheet();
        balSheet.isRow = 2;
        balSheet.Capital = capital;
        balSheet.ProfitLoss = profitLoss;
        balSheet.Liabilities = liabilities;
        balSheet.C_P_L = c_P_L;
        balSheet.Assets = assets;

        return balSheet;
    }

    public static BalSheet createSection(String month) {
        BalSheet balSheet = new BalSheet();
        balSheet.isRow = 0;
        balSheet.month = month;
        return balSheet;
    }

    public String getCBDate() {
        return CBDate;
    }

    public String getCapital() {
        return Capital;
    }

    public String getProfitLoss() {
        return ProfitLoss;
    }

    public String getLiabilities() {
        return Liabilities;
    }

    public String getC_P_L() {
        return C_P_L;
    }

    public String getAssets() {
        return Assets;
    }

    public String getClientID() {
        return ClientID;
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

    public void setCBDate(String CBDate) {
        this.CBDate = CBDate;
    }

    public void setCapital(String capital) {
        Capital = capital;
    }

    public void setProfitLoss(String profitLoss) {
        ProfitLoss = profitLoss;
    }

    public void setLiabilities(String liabilities) {
        Liabilities = liabilities;
    }

    public void setC_P_L(String c_P_L) {
        C_P_L = c_P_L;
    }

    public void setAssets(String assets) {
        Assets = assets;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public void setSorting(String sorting) {
        Sorting = sorting;
    }
}
