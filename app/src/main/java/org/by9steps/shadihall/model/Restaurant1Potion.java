package org.by9steps.shadihall.model;

public class Restaurant1Potion {

    private int ID;
    private String PotionID, PotionName,PotionDescriptions;
    private String ClientUserID,ClientID ,NetCode,SysCode ,UpdatedDate;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPotionID() {
        return PotionID;
    }

    public void setPotionID(String potionID) {
        PotionID = potionID;
    }

    public String getPotionName() {
        return PotionName;
    }

    public void setPotionName(String potionName) {
        PotionName = potionName;
    }

    public String getPotionDescriptions() {
        return PotionDescriptions;
    }

    public void setPotionDescriptions(String potionDescriptions) {
        PotionDescriptions = potionDescriptions;
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
}
