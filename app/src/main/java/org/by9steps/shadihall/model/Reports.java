package org.by9steps.shadihall.model;

import java.io.Serializable;

public class Reports implements Serializable {

    String AccountID;
    String Debit;
    String Credit;
    String Bal;
    String DebitBal;
    String CreditBal;
    String AcName;
    String AcGroupID;

    public Reports() {
    }

    public Reports(String accountID, String debit, String credit, String bal, String debitBal, String creditBal, String acName, String acGroupID) {
        AccountID = accountID;
        Debit = debit;
        Credit = credit;
        Bal = bal;
        DebitBal = debitBal;
        CreditBal = creditBal;
        AcName = acName;
        AcGroupID = acGroupID;
    }

    public String getAccountID() {
        return AccountID;
    }

    public void setAccountID(String accountID) {
        AccountID = accountID;
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

    public String getBal() {
        return Bal;
    }

    public void setBal(String bal) {
        Bal = bal;
    }

    public String getDebitBal() {
        return DebitBal;
    }

    public void setDebitBal(String debitBal) {
        DebitBal = debitBal;
    }

    public String getCreditBal() {
        return CreditBal;
    }

    public void setCreditBal(String creditBal) {
        CreditBal = creditBal;
    }

    public String getAcName() {
        return AcName;
    }

    public void setAcName(String acName) {
        AcName = acName;
    }

    public String getAcGroupID() {
        return AcGroupID;
    }

    public void setAcGroupID(String acGroupID) {
        AcGroupID = acGroupID;
    }
}
