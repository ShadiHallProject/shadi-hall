package org.by9steps.shadihall.model;

import com.orm.SugarRecord;

public class Account3Name extends SugarRecord {

    String AcTypeID;
    String AcTypeName;
    String AcGroupID;
    String AcGruopName;
    String AccountID;
    String AcName;
    String Debit;
    String Credit;
    String ClientID;
    String MaxDate;
    String Bal;
    String DebitBL;
    String CreditBL;

    public Account3Name(String acTypeID, String acTypeName, String acGroupID, String acGruopName, String accountID, String acName, String debit, String credit, String clientID, String maxDate, String bal, String debitBL, String creditBL) {
        AcTypeID = acTypeID;
        AcTypeName = acTypeName;
        AcGroupID = acGroupID;
        AcGruopName = acGruopName;
        AccountID = accountID;
        AcName = acName;
        Debit = debit;
        Credit = credit;
        ClientID = clientID;
        MaxDate = maxDate;
        Bal = bal;
        DebitBL = debitBL;
        CreditBL = creditBL;
    }

    public Account3Name() {
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

    public String getAccountID() {
        return AccountID;
    }

    public String getAcName() {
        return AcName;
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

    public String getMaxDate() {
        return MaxDate;
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
}
