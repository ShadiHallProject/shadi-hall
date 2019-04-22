package org.by9steps.shadihall.model;

public class Voucher {

    String CashBookID;
    String CBDate;
    String CBRemarks;
    String DebitAccount;
    String DebiterName;
    String DebiterAddress;
    String DebiterContactNo;
    String CreditAccount;
    String CrediterName;
    String CrediterAddress;
    String CrediterContactNo;
    String ClientUserID;
    String PreparedBy;
    String Amount;
    String ClientID;

    public Voucher() {
    }

    public Voucher(String cashBookID, String CBDate, String CBRemarks, String debitAccount, String debiterName, String debiterAddress, String debiterContactNo, String creditAccount, String crediterName, String crediterAddress, String crediterContactNo, String clientUserID, String preparedBy, String amount, String clientID) {
        CashBookID = cashBookID;
        this.CBDate = CBDate;
        this.CBRemarks = CBRemarks;
        DebitAccount = debitAccount;
        DebiterName = debiterName;
        DebiterAddress = debiterAddress;
        DebiterContactNo = debiterContactNo;
        CreditAccount = creditAccount;
        CrediterName = crediterName;
        CrediterAddress = crediterAddress;
        CrediterContactNo = crediterContactNo;
        ClientUserID = clientUserID;
        PreparedBy = preparedBy;
        Amount = amount;
        ClientID = clientID;
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

    public String getCBRemarks() {
        return CBRemarks;
    }

    public void setCBRemarks(String CBRemarks) {
        this.CBRemarks = CBRemarks;
    }

    public String getDebitAccount() {
        return DebitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        DebitAccount = debitAccount;
    }

    public String getDebiterName() {
        return DebiterName;
    }

    public void setDebiterName(String debiterName) {
        DebiterName = debiterName;
    }

    public String getDebiterAddress() {
        return DebiterAddress;
    }

    public void setDebiterAddress(String debiterAddress) {
        DebiterAddress = debiterAddress;
    }

    public String getDebiterContactNo() {
        return DebiterContactNo;
    }

    public void setDebiterContactNo(String debiterContactNo) {
        DebiterContactNo = debiterContactNo;
    }

    public String getCreditAccount() {
        return CreditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        CreditAccount = creditAccount;
    }

    public String getCrediterName() {
        return CrediterName;
    }

    public void setCrediterName(String crediterName) {
        CrediterName = crediterName;
    }

    public String getCrediterAddress() {
        return CrediterAddress;
    }

    public void setCrediterAddress(String crediterAddress) {
        CrediterAddress = crediterAddress;
    }

    public String getCrediterContactNo() {
        return CrediterContactNo;
    }

    public void setCrediterContactNo(String crediterContactNo) {
        CrediterContactNo = crediterContactNo;
    }

    public String getClientUserID() {
        return ClientUserID;
    }

    public void setClientUserID(String clientUserID) {
        ClientUserID = clientUserID;
    }

    public String getPreparedBy() {
        return PreparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        PreparedBy = preparedBy;
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
}
