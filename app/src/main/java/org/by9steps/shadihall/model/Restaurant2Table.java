package org.by9steps.shadihall.model;

public class Restaurant2Table {
    private int ID;
    private String ClientUserID,ClientID ,NetCode,SysCode ,UpdatedDate;
    private String TableID,TabelName,PotionID,TableDescription,TableStatus,SalPur1ID;

    public int getID() {
        return ID;
    }

    public String getSalPur1ID() {
        return SalPur1ID;
    }

    public void setSalPur1ID(String salPur1ID) {
        SalPur1ID = salPur1ID;
    }

    public String getTabelName() {
        return TabelName;
    }

    public void setTabelName(String tabelName) {
        TabelName = tabelName;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getClientUserID() {
        return ClientUserID;
    }

    public void setClientUserID(String clientUserID) {
        ClientUserID = clientUserID;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
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

    public String getTableID() {
        return TableID;
    }

    public void setTableID(String tableID) {
        TableID = tableID;
    }

    public String getPotionID() {
        return PotionID;
    }

    public void setPotionID(String potionID) {
        PotionID = potionID;
    }

    public String getTableDescription() {
        return TableDescription;
    }

    public void setTableDescription(String tableDescription) {
        TableDescription = tableDescription;
    }

    public String getTableStatus() {
        return TableStatus;
    }

    public void setTableStatus(String tableStatus) {
        TableStatus = tableStatus;
    }
}
