package org.by9steps.shadihall.model;

public class joinQueryForResturent {
    private String TableName;
    private String PotionName;
    private String BillAmount;
    private String TableStatus;
    private String ClientID;
    private String TableID;

    public String getTableID() {
        return TableID;
    }

    public void setTableID(String tableID) {
        TableID = tableID;
    }

    public String getSalPur1ID() {
        return SalPur1ID;
    }

    public void setSalPur1ID(String salPur1ID) {
        SalPur1ID = salPur1ID;
    }

    private String SalPur1ID;

    public String getTableName() {
        return TableName;
    }

    public void setTableName(String tableName) {
        TableName = tableName;
    }

    public String getPotionName() {
        return PotionName;
    }

    public void setPotionName(String potionName) {
        PotionName = potionName;
    }

    public String getBillAmount() {
        return BillAmount;
    }

    public void setBillAmount(String billAmount) {
        BillAmount = billAmount;
    }

    public String getTableStatus() {
        return TableStatus;
    }

    public void setTableStatus(String tableStatus) {
        TableStatus = tableStatus;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }
}
