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
    String BookingID;
    String DebitAccountName;
    String CreditAccountName;
    String UserName;
    String UpdatedDate;

    public CashEntry(String cashBookID, String CBDate, String debitAccount, String creditAccount, String CBRemarks, String amount, String clientID, String clientUserID, String bookingID, String debitAccountName, String creditAccountName, String userName, String updatedDate) {
        CashBookID = cashBookID;
        this.CBDate = CBDate;
        DebitAccount = debitAccount;
        CreditAccount = creditAccount;
        this.CBRemarks = CBRemarks;
        Amount = amount;
        ClientID = clientID;
        ClientUserID = clientUserID;
        BookingID = bookingID;
        DebitAccountName = debitAccountName;
        CreditAccountName = creditAccountName;
        UserName = userName;
        UpdatedDate = updatedDate;
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

    public String getBookingID() {
        return BookingID;
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
}
