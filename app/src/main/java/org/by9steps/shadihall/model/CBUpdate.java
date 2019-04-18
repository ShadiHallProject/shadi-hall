package org.by9steps.shadihall.model;

import com.orm.SugarRecord;

public class CBUpdate extends SugarRecord {

    String CashBookID;
    String CBDate;
    String DebitAccount;
    String CreditAccount;
    String CBRemarks;
    String Amount;

    public CBUpdate(String cashBookID, String CBDate, String debitAccount, String creditAccount, String CBRemarks, String amount) {
        CashBookID = cashBookID;
        this.CBDate = CBDate;
        DebitAccount = debitAccount;
        CreditAccount = creditAccount;
        this.CBRemarks = CBRemarks;
        Amount = amount;
    }

    public CBUpdate() {
    }

    public String getCashBookID() {
        return CashBookID;
    }

    public String getCBDate() {
        return CBDate;
    }

    public String getDebitAccount() {
        return DebitAccount;
    }

    public String getCreditAccount() {
        return CreditAccount;
    }

    public String getCBRemarks() {
        return CBRemarks;
    }

    public String getAmount() {
        return Amount;
    }
}
