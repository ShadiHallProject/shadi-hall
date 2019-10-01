package org.by9steps.shadihall.model;

public class Item1Type {
    private String Item1TypeID;
    private String ItemType;


    public String getItem1TypeID() {
        return Item1TypeID;
    }

    public void setItem1TypeID(String item1TypeID) {
        Item1TypeID = item1TypeID;
    }

    public String getItemType() {
        return ItemType;
    }

    public void setItemType(String itemType) {
        ItemType = itemType;
    }

    @Override
    public String toString() {
        return "Item1Type{" +
                "Item1TypeID='" + Item1TypeID + '\'' +
                ", ItemType='" + ItemType + '\'' +
                '}';
    }
}