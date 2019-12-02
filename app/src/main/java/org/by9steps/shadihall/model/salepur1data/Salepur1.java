
package org.by9steps.shadihall.model.salepur1data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.by9steps.shadihall.model.item3name.UpdatedDate;

public class Salepur1 {

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getBillSatus() {
        return BillSatus;
    }

    public void setBillSatus(String billSatus) {
        BillSatus = billSatus;
    }

    private String BillSatus;

    @SerializedName("ID")
    @Expose
    private int iD;
    @SerializedName("SalePur1ID")
    @Expose
    private int salePur1ID;
    @SerializedName("EntryType")
    @Expose
    private String entryType;
    @SerializedName("SPDate")
    @Expose
    private SPDate sPDate;
    @SerializedName("AcNameID")
    @Expose
    private int acNameID;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("ClientID")
    @Expose
    private int clientID;
    @SerializedName("ClientUserID")
    @Expose
    private int clientUserID;
    @SerializedName("NetCode")
    @Expose
    private String netCode;
    @SerializedName("SysCode")
    @Expose
    private String sysCode;
    @SerializedName("UpdatedDate")
    @Expose
    private UpdatedDate updatedDate;
    @SerializedName("NameOfPerson")
    @Expose
    private String nameOfPerson;
    @SerializedName("PayAfterDay")
    @Expose
    private int payAfterDay;

    public int getID() {
        return iD;
    }

    public void setID(int iD) {
        this.iD = iD;
    }

    public int getSalePur1ID() {
        return salePur1ID;
    }

    public void setSalePur1ID(int salePur1ID) {
        this.salePur1ID = salePur1ID;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public SPDate getSPDate() {
        return sPDate;
    }

    public void setSPDate(SPDate sPDate) {
        this.sPDate = sPDate;
    }

    public int getAcNameID() {
        return acNameID;
    }

    public void setAcNameID(int acNameID) {
        this.acNameID = acNameID;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getClientUserID() {
        return clientUserID;
    }

    public void setClientUserID(int clientUserID) {
        this.clientUserID = clientUserID;
    }

    public String getNetCode() {
        return netCode;
    }

    public void setNetCode(String netCode) {
        this.netCode = netCode;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public UpdatedDate getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(UpdatedDate updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getNameOfPerson() {
        return nameOfPerson;
    }

    public void setNameOfPerson(String nameOfPerson) {
        this.nameOfPerson = nameOfPerson;
    }

    public int getPayAfterDay() {
        return payAfterDay;
    }

    public void setPayAfterDay(int payAfterDay) {
        this.payAfterDay = payAfterDay;
    }

    @Override
    public String toString() {
        return "Salepur1{" +
                "iD=" + iD +
                ", salePur1ID=" + salePur1ID +
                ", entryType='" + entryType + '\'' +
                ", sPDate=" + sPDate.getDate() +
                ", acNameID=" + acNameID +
                ", remarks='" + remarks + '\'' +
                ", clientID=" + clientID +
                ", clientUserID=" + clientUserID +
                ", netCode='" + netCode + '\'' +
                ", sysCode='" + sysCode + '\'' +
                ", updatedDate=" + updatedDate.getDate() +
                ", nameOfPerson='" + nameOfPerson + '\'' +
                ", payAfterDay=" + payAfterDay +
                '}';
    }
}
