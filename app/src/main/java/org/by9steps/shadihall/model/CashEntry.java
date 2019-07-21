package org.by9steps.shadihall.model;

public class CashEntry {

    String CashBookID;
    String CBDate;
    String DebitAccount;
    String CreditAccount;
    String CBRemarks;
    String Amount;
    String ClientID;
    String ClientUserID;
    String TableID;
    String DebitAccountName;
    String CreditAccountName;
    String UserName;
    String UpdatedDate;
    String month;

    private int isRow;

    public static CashEntry createRow(String cashBookID, String CBDate, String debitAccount, String creditAccount, String CBRemarks, String amount, String clientID, String clientUserID, String tableID, String debitAccountName, String creditAccountName, String userName, String updatedDate) {
        CashEntry cashEntry = new CashEntry();
        cashEntry.isRow = 1;
        cashEntry.CashBookID = cashBookID;
        cashEntry.CBDate = CBDate;
        cashEntry.DebitAccount = debitAccount;
        cashEntry.CreditAccount = creditAccount;
        cashEntry.CBRemarks = CBRemarks;
        cashEntry.Amount = amount;
        cashEntry.ClientID = clientID;
        cashEntry.ClientUserID = clientUserID;
        cashEntry.TableID = tableID;
        cashEntry.DebitAccountName = debitAccountName;
        cashEntry.CreditAccountName = creditAccountName;
        cashEntry.UserName = userName;
        cashEntry.UpdatedDate = updatedDate;
        return cashEntry;
    }

    public static CashEntry createTotal(String amount) {
        CashEntry cashEntry = new CashEntry();
        cashEntry.isRow = 2;
        cashEntry.Amount = amount;
        return cashEntry;
    }

    public static CashEntry createSection(String month) {
        CashEntry cashEntry = new CashEntry();
        cashEntry.isRow = 0;
        cashEntry.month = month;
        return cashEntry;
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

    public String getClientID() {
        return ClientID;
    }

    public String getClientUserID() {
        return ClientUserID;
    }

    public String getTableID() {
        return TableID;
    }

    public String getDebitAccountName() {
        return DebitAccountName;
    }

    public String getCreditAccountName() {
        return CreditAccountName;
    }

    public String getUserName() {
        return UserName;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public int isRow() {
        return isRow;
    }

    public String getMonth() {
        return month;
    }
}
