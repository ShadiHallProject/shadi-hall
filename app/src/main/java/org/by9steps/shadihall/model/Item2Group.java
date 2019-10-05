package org.by9steps.shadihall.model;

public class Item2Group {
    private int ID;
    private String Item2GroupID,Item1TypeID, ClientID,ClientUserID;
    private String Item2GroupName ,NetCode,SysCode ,UpdatedDate;



    public String getItem2GroupID() {
        return Item2GroupID;
    }

    public void setItem2GroupID(String item2GroupID) {
        Item2GroupID = item2GroupID;
    }

    public String getItem1TypeID() {
        return Item1TypeID;
    }

    public void setItem1TypeID(String item1TypeID) {
        Item1TypeID = item1TypeID;
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

    public String getItem2GroupName() {
        return Item2GroupName;
    }

    public void setItem2GroupName(String item2GroupName) {
        Item2GroupName = item2GroupName;
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

    @Override
    public String toString() {
        return "Item2Group{" +
                "ID='" + ID + '\'' +
                ", Item2GroupID='" + Item2GroupID + '\'' +
                ", Item1TypeID='" + Item1TypeID + '\'' +
                ", ClientID='" + ClientID + '\'' +
                ", ClientUserID='" + ClientUserID + '\'' +
                ", Item2GroupName='" + Item2GroupName + '\'' +
                ", NetCode='" + NetCode + '\'' +
                ", SysCode='" + SysCode + '\'' +
                ", UpdatedDate='" + UpdatedDate + '\'' +
                '}';
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
