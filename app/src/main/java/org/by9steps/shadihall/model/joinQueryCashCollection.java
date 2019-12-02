package org.by9steps.shadihall.model;

public class joinQueryCashCollection {
    String ClientID,BillNo,EntryType,BillAmount,Received,BillBalance,BillStatus,Remarks,SPDate,User;

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public String getBillNo() {
        return BillNo;
    }

    public void setBillNo(String billNo) {
        BillNo = billNo;
    }

    public String getEntryType() {
        return EntryType;
    }

    public void setEntryType(String entryType) {
        EntryType = entryType;
    }

    public String getBillAmount() {
        return BillAmount;
    }

    public void setBillAmount(String billAmount) {
        BillAmount = billAmount;
    }

    public String getReceived() {
        return Received;
    }

    public void setReceived(String received) {
        Received = received;
    }

    public String getBillBalance() {
        return BillBalance;
    }

    public void setBillBalance(String billBalance) {
        BillBalance = billBalance;
    }

    public String getBillStatus() {
        return BillStatus;
    }

    public void setBillStatus(String billStatus) {
        BillStatus = billStatus;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getSPDate() {
        return SPDate;
    }

    public void setSPDate(String SPDate) {
        this.SPDate = SPDate;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }
}
