
package org.by9steps.shadihall.model.salepur2data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.by9steps.shadihall.model.item3name.UpdatedDate;

public class SalePur2 {

    @SerializedName("ID")
    @Expose
    private int iD;
    @SerializedName("SalePur1ID")
    @Expose
    private int salePur1ID;
    @SerializedName("ItemSerial")
    @Expose
    private int itemSerial;
    @SerializedName("EntryType")
    @Expose
    private String entryType;
    @SerializedName("Item3NameID")
    @Expose
    private int item3NameID;
    @SerializedName("ItemDescription")
    @Expose
    private String itemDescription;
    @SerializedName("QtyAdd")
    @Expose
    private String qtyAdd;
    @SerializedName("QtyLess")
    @Expose
    private String qtyLess;
    @SerializedName("Qty")
    @Expose
    private String qty;
    @SerializedName("Price")
    @Expose
    private String price;
    @SerializedName("Total")
    @Expose
    private String total;
    @SerializedName("Location")
    @Expose
    private String location;
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

    public int getItemSerial() {
        return itemSerial;
    }

    public void setItemSerial(int itemSerial) {
        this.itemSerial = itemSerial;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public int getItem3NameID() {
        return item3NameID;
    }

    public void setItem3NameID(int item3NameID) {
        this.item3NameID = item3NameID;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getQtyAdd() {
        return qtyAdd;
    }

    public void setQtyAdd(String qtyAdd) {
        this.qtyAdd = qtyAdd;
    }

    public String getQtyLess() {
        return qtyLess;
    }

    public void setQtyLess(String qtyLess) {
        this.qtyLess = qtyLess;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    @Override
    public String toString() {
        return "SalePur2{" +
                "iD=" + iD +
                ", salePur1ID=" + salePur1ID +
                ", itemSerial=" + itemSerial +
                ", entryType='" + entryType + '\'' +
                ", item3NameID=" + item3NameID +
                ", itemDescription='" + itemDescription + '\'' +
                ", qtyAdd='" + qtyAdd + '\'' +
                ", qtyLess='" + qtyLess + '\'' +
                ", qty='" + qty + '\'' +
                ", price='" + price + '\'' +
                ", total='" + total + '\'' +
                ", location='" + location + '\'' +
                ", clientID=" + clientID +
                ", clientUserID=" + clientUserID +
                ", netCode='" + netCode + '\'' +
                ", sysCode='" + sysCode + '\'' +
                ", updatedDate=" + updatedDate.getDate() +
                '}';
    }
}
