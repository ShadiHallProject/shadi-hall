package org.by9steps.shadihall.model;

public class joinQueryForResturentAddOrder {
   private boolean isselected=false;

    public boolean getIsselected() {
        return isselected;
    }

    public void setIsselected(boolean isselected) {
        this.isselected = isselected;
    }

    private String Item2GroupID,Item2GroupName,Item3NameID,
            ItemName,SalePrice,Stock,ItemStatus,ItemType,ClientID;

    public String getItem2GroupID() {
        return Item2GroupID;
    }

    public void setItem2GroupID(String item2GroupID) {
        Item2GroupID = item2GroupID;
    }

    public String getItem2GroupName() {
        return Item2GroupName;
    }

    public void setItem2GroupName(String item2GroupName) {
        Item2GroupName = item2GroupName;
    }

    public String getItem3NameID() {
        return Item3NameID;
    }

    public void setItem3NameID(String item3NameID) {
        Item3NameID = item3NameID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(String salePrice) {
        SalePrice = salePrice;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }

    public String getItemStatus() {
        return ItemStatus;
    }

    public void setItemStatus(String itemStatus) {
        ItemStatus = itemStatus;
    }

    public String getItemType() {
        return ItemType;
    }

    public void setItemType(String itemType) {
        ItemType = itemType;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }
}
