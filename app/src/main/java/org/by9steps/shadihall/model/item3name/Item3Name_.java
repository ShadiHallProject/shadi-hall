
package org.by9steps.shadihall.model.item3name;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item3Name_ {

    @SerializedName("ID")
    @Expose
    private int iD;
    @SerializedName("Item3NameID")
    @Expose
    private int item3NameID;
    @SerializedName("Item2GroupID")
    @Expose
    private int item2GroupID;
    @SerializedName("ItemName")
    @Expose
    private String itemName;
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
    @SerializedName("SalePrice")
    @Expose
    private String salePrice;
    @SerializedName("ItemCode")
    @Expose
    private String itemCode;
    @SerializedName("Stock")
    @Expose
    private String stock;

    public int getID() {
        return iD;
    }

    public void setID(int iD) {
        this.iD = iD;
    }

    public int getItem3NameID() {
        return item3NameID;
    }

    public void setItem3NameID(int item3NameID) {
        this.item3NameID = item3NameID;
    }

    public int getItem2GroupID() {
        return item2GroupID;
    }

    public void setItem2GroupID(int item2GroupID) {
        this.item2GroupID = item2GroupID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

}
