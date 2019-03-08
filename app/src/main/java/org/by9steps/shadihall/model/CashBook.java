package org.by9steps.shadihall.model;

import com.orm.SugarRecord;

public class CashBook extends SugarRecord {

    String AccountID;
    String Debit;
    String Credit;
    String Bal;
    String DebitBal;
    String CreditBal;
    String AcName;
    String AcGroupID;

    public CashBook(String accountID, String debit, String credit, String bal, String debitBal, String creditBal, String acName, String acGroupID) {
        AccountID = accountID;
        Debit = debit;
        Credit = credit;
        Bal = bal;
        DebitBal = debitBal;
        CreditBal = creditBal;
        AcName = acName;
        AcGroupID = acGroupID;
    }

    public CashBook() {
    }

    public String getAccountID() {
        return AccountID;
    }

    public String getDebit() {
        return Debit;
    }

    public String getCredit() {
        return Credit;
    }

    public String getBal() {
        return Bal;
    }

    public String getDebitBal() {
        return DebitBal;
    }

    public String getCreditBal() {
        return CreditBal;
    }

    public String getAcName() {
        return AcName;
    }

    public String getAcGroupID() {
        return AcGroupID;
    }
}
