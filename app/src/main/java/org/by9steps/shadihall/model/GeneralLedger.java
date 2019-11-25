package org.by9steps.shadihall.model;

import java.io.Serializable;

public class GeneralLedger implements Serializable {

    String ClientID;
    String EntryNo;
    String Date;
    String SelectedAc;
    String AgainstAc;
    String AccountName;
    String Particulars;
    String Debit;
    String Credit;
    String EntryOf;
    String Balance;
    String month;
    String tablename,tableid;

    private int isRow;

    public static GeneralLedger createRow(String clientID, String entryNo, String date, String selectedAc, String againstAc, String accountName, String particulars, String debit, String credit, String entryOf, String balance,String tblname,String tblid) {
        GeneralLedger generalLedger = new GeneralLedger();
        generalLedger.isRow = 1;
        generalLedger.ClientID = clientID;
        generalLedger.EntryNo = entryNo;
        generalLedger.Date = date;
        generalLedger.SelectedAc = selectedAc;
        generalLedger.AgainstAc = againstAc;
        generalLedger.AccountName = accountName;
        generalLedger.Particulars = particulars;
        generalLedger.Debit = debit;
        generalLedger.Credit = credit;
        generalLedger.EntryOf = entryOf;
        generalLedger.Balance = balance;
        generalLedger.tablename = tblname;
        generalLedger.tableid = tblid;
        return generalLedger;
    }

    public static GeneralLedger createTotal(String clientID, String entryNo, String date, String selectedAc, String againstAc, String accountName, String particulars, String debit, String credit, String entryOf, String balance) {
        GeneralLedger generalLedger = new GeneralLedger();
        generalLedger.isRow = 2;
        generalLedger.ClientID = clientID;
        generalLedger.EntryNo = entryNo;
        generalLedger.Date = date;
        generalLedger.SelectedAc = selectedAc;
        generalLedger.AgainstAc = againstAc;
        generalLedger.AccountName = accountName;
        generalLedger.Particulars = particulars;
        generalLedger.Debit = debit;
        generalLedger.Credit = credit;
        generalLedger.EntryOf = entryOf;
        generalLedger.Balance = balance;
        return generalLedger;
    }

    public static GeneralLedger createSection(String month) {
        GeneralLedger generalLedger = new GeneralLedger();
        generalLedger.isRow = 0;
        generalLedger.month = month;
        return generalLedger;
    }

    public GeneralLedger() {
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public String getEntryNo() {
        return EntryNo;
    }

    public void setEntryNo(String entryNo) {
        EntryNo = entryNo;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getSelectedAc() {
        return SelectedAc;
    }

    public void setSelectedAc(String selectedAc) {
        SelectedAc = selectedAc;
    }

    public String getAgainstAc() {
        return AgainstAc;
    }

    public void setAgainstAc(String againstAc) {
        AgainstAc = againstAc;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getParticulars() {
        return Particulars;
    }

    public void setParticulars(String particulars) {
        Particulars = particulars;
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

    public String getEntryOf() {
        return EntryOf;
    }

    public void setEntryOf(String entryOf) {
        EntryOf = entryOf;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public int isRow() {
        return isRow;
    }

    public String getMonth() {
        return month;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }
}
