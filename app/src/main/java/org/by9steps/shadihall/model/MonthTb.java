package org.by9steps.shadihall.model;

public class MonthTb {

    String ClientID;
    String AccountID;
    String Debit;
    String Credit;
    String PrvBal;
    String PrvDebit;
    String PrvCredit;
    String TraDebit;
    String TraCredit;
    String TraBalance;
    String ClosingBalnce;
    String ClosingDebit;
    String ClosingCredit;
    String AcName;
    String AcGroupID;
    String AcGruopName;
    String AcTypeID;
    String AcTypeName;

    private int isRow;

    public MonthTb() {
    }

    public static MonthTb createRow(String clientID, String accountID, String debit, String credit, String prvBal, String prvDebit, String prvCredit, String traDebit, String traCredit, String traBalance, String closingBalnce, String closingDebit, String closingCredit, String acName, String acGroupID, String acGruopName, String acTypeID, String acTypeName) {

        MonthTb monthTb = new MonthTb();

        monthTb.ClientID = clientID;
        monthTb.AccountID = accountID;
        monthTb.Debit = debit;
        monthTb.Credit = credit;
        monthTb.PrvBal = prvBal;
        monthTb.PrvDebit = prvDebit;
        monthTb.PrvCredit = prvCredit;
        monthTb.TraDebit = traDebit;
        monthTb.TraCredit = traCredit;
        monthTb.TraBalance = traBalance;
        monthTb.ClosingBalnce = closingBalnce;
        monthTb.ClosingDebit = closingDebit;
        monthTb.ClosingCredit = closingCredit;
        monthTb.AcName = acName;
        monthTb.AcGroupID = acGroupID;
        monthTb.AcGruopName = acGruopName;
        monthTb.AcTypeID = acTypeID;
        monthTb.AcTypeName = acTypeName;
        monthTb.isRow = 1;
        return monthTb;
    }

    public static MonthTb createTotal(String acName, String prvDebit, String prvCredit, String traDebit, String traCredit, String closingDebit, String closingCredit) {

        MonthTb monthTb = new MonthTb();

        monthTb.AcName = acName;
        monthTb.PrvDebit = prvDebit;
        monthTb.PrvCredit = prvCredit;
        monthTb.TraDebit = traDebit;
        monthTb.TraCredit = traCredit;
        monthTb.ClosingDebit = closingDebit;
        monthTb.ClosingCredit = closingCredit;
        monthTb.isRow = 2;
        return monthTb;
    }

    public static MonthTb createSection(String month) {
        MonthTb monthTb = new MonthTb();
        monthTb.isRow = 0;
        monthTb.AcTypeName = month;
        return monthTb;
    }

    public int isRow() {
        return isRow;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
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

    public String getPrvBal() {
        return PrvBal;
    }

    public void setPrvBal(String prvBal) {
        PrvBal = prvBal;
    }

    public String getPrvDebit() {
        return PrvDebit;
    }

    public void setPrvDebit(String prvDebit) {
        PrvDebit = prvDebit;
    }

    public String getPrvCredit() {
        return PrvCredit;
    }

    public void setPrvCredit(String prvCredit) {
        PrvCredit = prvCredit;
    }

    public String getTraDebit() {
        return TraDebit;
    }

    public void setTraDebit(String traDebit) {
        TraDebit = traDebit;
    }

    public String getTraCredit() {
        return TraCredit;
    }

    public void setTraCredit(String traCredit) {
        TraCredit = traCredit;
    }

    public String getTraBalance() {
        return TraBalance;
    }

    public void setTraBalance(String traBalance) {
        TraBalance = traBalance;
    }

    public String getClosingBalnce() {
        return ClosingBalnce;
    }

    public void setClosingBalnce(String closingBalnce) {
        ClosingBalnce = closingBalnce;
    }

    public String getClosingDebit() {
        return ClosingDebit;
    }

    public void setClosingDebit(String closingDebit) {
        ClosingDebit = closingDebit;
    }

    public String getClosingCredit() {
        return ClosingCredit;
    }

    public void setClosingCredit(String closingCredit) {
        ClosingCredit = closingCredit;
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

    public String getAcGruopName() {
        return AcGruopName;
    }

    public void setAcGruopName(String acGruopName) {
        AcGruopName = acGruopName;
    }

    public String getAcTypeID() {
        return AcTypeID;
    }

    public void setAcTypeID(String acTypeID) {
        AcTypeID = acTypeID;
    }

    public String getAcTypeName() {
        return AcTypeName;
    }

    public void setAcTypeName(String acTypeName) {
        AcTypeName = acTypeName;
    }
}
