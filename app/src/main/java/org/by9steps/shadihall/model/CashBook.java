package org.by9steps.shadihall.model;

import java.io.Serializable;

public class CashBook implements Serializable {

    String cId;
    String CashBookID;
    String CBDate;
    String DebitAccount;
    String CreditAccount;
    String CBRemarks;
    String Amount;
    String ClientID;
    String ClientUserID;
    String NetCode;
    String SysCode;
    String UpdatedDate;
    String BookingID;

    String DebitAccountName;
    String CreditAccountName;
    String UserName;

    public CashBook() {
    }

    public CashBook(String cashBookID, String CBDate, String debitAccount, String creditAccount, String CBRemarks, String amount, String clientID, String clientUserID, String netCode, String sysCode, String updatedDate, String bookingID) {
        CashBookID = cashBookID;
        this.CBDate = CBDate;
        DebitAccount = debitAccount;
        CreditAccount = creditAccount;
        this.CBRemarks = CBRemarks;
        Amount = amount;
        ClientID = clientID;
        ClientUserID = clientUserID;
        NetCode = netCode;
        SysCode = sysCode;
        UpdatedDate = updatedDate;
        BookingID = bookingID;
    }

    public String getCashBookID() {
        return CashBookID;
    }

    public void setCashBookID(String cashBookID) {
        CashBookID = cashBookID;
    }

    public String getCBDate() {
        return CBDate;
    }

    public void setCBDate(String CBDate) {
        this.CBDate = CBDate;
    }

    public String getDebitAccount() {
        return DebitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        DebitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return CreditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        CreditAccount = creditAccount;
    }

    public String getCBRemarks() {
        return CBRemarks;
    }

    public void setCBRemarks(String CBRemarks) {
        this.CBRemarks = CBRemarks;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public String getClientUserID() {
        return ClientUserID;
    }

    public void setClientUserID(String clientUserID) {
        ClientUserID = clientUserID;
    }

    public String getNetCode() {
        return NetCode;
    }

    public void setNetCode(String netCode) {
        NetCode = netCode;
    }

    public String getSysCode() {
        return SysCode;
    }

    public void setSysCode(String sysCode) {
        SysCode = sysCode;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }

    public String getDebitAccountName() {
        return DebitAccountName;
    }

    public void setDebitAccountName(String debitAccountName) {
        DebitAccountName = debitAccountName;
    }

    public String getCreditAccountName() {
        return CreditAccountName;
    }

    public void setCreditAccountName(String creditAccountName) {
        CreditAccountName = creditAccountName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }
}
