package org.by9steps.shadihall.model;

import java.io.Serializable;

public class Summerize  implements Serializable {

    String AcTypeID;
    String AcTypeName;
    String AcGroupID;
    String AcGruopName;
    String Debit;
    String Credit;
    String ClientID;
    String Bal;
    String DebitBL;
    String CreditBL;
    String month;

    private int isRow;

    public static Summerize createRow(String acTypeID, String acTypeName, String acGroupID, String acGruopName, String debit, String credit, String clientID, String bal, String debitBL, String creditBL) {
        Summerize summerize = new Summerize();
        summerize.isRow = 1;
        summerize.AcTypeID = acTypeID;
        summerize.AcTypeName = acTypeName;
        summerize.AcGroupID = acGroupID;
        summerize.AcGruopName = acGruopName;
        summerize.Debit = debit;
        summerize.Credit = credit;
        summerize.ClientID = clientID;
        summerize.Bal = bal;
        summerize.DebitBL = debitBL;
        summerize.CreditBL = creditBL;
        return summerize;
    }

    public static Summerize createTotal(String debitBL, String creditBL) {
        Summerize summerize = new Summerize();
        summerize.isRow = 2;
        summerize.DebitBL = debitBL;
        summerize.CreditBL = creditBL;
        return summerize;
    }

    public static Summerize createSection(String month) {
        Summerize summerize = new Summerize();
        summerize.isRow = 0;
        summerize.month = month;
        return summerize;
    }

    public String getAcTypeID() {
        return AcTypeID;
    }

    public String getAcTypeName() {
        return AcTypeName;
    }

    public String getAcGroupID() {
        return AcGroupID;
    }

    public String getAcGruopName() {
        return AcGruopName;
    }

    public String getDebit() {
        return Debit;
    }

    public String getCredit() {
        return Credit;
    }

    public String getClientID() {
        return ClientID;
    }

    public String getBal() {
        return Bal;
    }

    public String getDebitBL() {
        return DebitBL;
    }

    public String getCreditBL() {
        return CreditBL;
    }

    public int isRow() {
        return isRow;
    }

    public String getMonth() {
        return month;
    }

    public void setAcTypeID(String acTypeID) {
        AcTypeID = acTypeID;
    }

    public void setAcTypeName(String acTypeName) {
        AcTypeName = acTypeName;
    }

    public void setAcGroupID(String acGroupID) {
        AcGroupID = acGroupID;
    }

    public void setAcGruopName(String acGruopName) {
        AcGruopName = acGruopName;
    }

    public void setDebit(String debit) {
        Debit = debit;
    }

    public void setCredit(String credit) {
        Credit = credit;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public void setBal(String bal) {
        Bal = bal;
    }

    public void setDebitBL(String debitBL) {
        DebitBL = debitBL;
    }

    public void setCreditBL(String creditBL) {
        CreditBL = creditBL;
    }
}
