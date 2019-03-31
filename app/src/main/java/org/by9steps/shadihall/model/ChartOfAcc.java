package org.by9steps.shadihall.model;

import java.io.Serializable;

public class ChartOfAcc implements Serializable {
    String AcTypeID;
    String AcTypeName;
    String AcGroupID;
    String AcGruopName;
    String AccountID;
    String AcName;
    String Debit;
    String Credit;
    String ClientID;
    String Bal;
    String DebitBL;
    String CreditBL;
    String MaxDate;

    public ChartOfAcc() {
    }

    public ChartOfAcc(String acTypeID, String acTypeName, String acGroupID, String acGruopName, String accountID, String acName, String debit, String credit, String clientID, String bal, String debitBL, String creditBL, String maxDate) {
        AcTypeID = acTypeID;
        AcTypeName = acTypeName;
        AcGroupID = acGroupID;
        AcGruopName = acGruopName;
        AccountID = accountID;
        AcName = acName;
        Debit = debit;
        Credit = credit;
        ClientID = clientID;
        Bal = bal;
        DebitBL = debitBL;
        CreditBL = creditBL;
        MaxDate = maxDate;
    }

    public String getAcTypeID() {
        return AcTypeID;
    }

    public void setAcTypeID(String acTypeID) {
        AcTypeID = acTypeID;
    }

    public String getAcTypeName() {
        return AcTypeName;
    }

    public void setAcTypeName(String acTypeName) {
        AcTypeName = acTypeName;
    }

    public String getAcGroupID() {
        return AcGroupID;
    }

    public void setAcGroupID(String acGroupID) {
        AcGroupID = acGroupID;
    }

    public String getAcGruopName() {
        return AcGruopName;
    }

    public void setAcGruopName(String acGruopName) {
        AcGruopName = acGruopName;
    }

    public String getAccountID() {
        return AccountID;
    }

    public void setAccountID(String accountID) {
        AccountID = accountID;
    }

    public String getAcName() {
        return AcName;
    }

    public void setAcName(String acName) {
        AcName = acName;
    }

    public String getDebit() {
        return Debit;
    }

    public void setDebit(String debit) {
        Debit = debit;
    }

    public String getCredit() {
        return Credit;
    }

    public void setCredit(String credit) {
        Credit = credit;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public String getBal() {
        return Bal;
    }

    public void setBal(String bal) {
        Bal = bal;
    }

    public String getDebitBL() {
        return DebitBL;
    }

    public void setDebitBL(String debitBL) {
        DebitBL = debitBL;
    }

    public String getCreditBL() {
        return CreditBL;
    }

    public void setCreditBL(String creditBL) {
        CreditBL = creditBL;
    }

    public String getMaxDate() {
        return MaxDate;
    }

    public void setMaxDate(String maxDate) {
        MaxDate = maxDate;
    }
}
