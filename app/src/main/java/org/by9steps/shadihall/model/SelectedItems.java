package org.by9steps.shadihall.model;

public class SelectedItems {
    private String itemName,price;
    int qty,totalPrice;

    public SelectedItems(String itemName, String price, int qty,int totalPrice) {
        this.itemName = itemName;
        this.price = price;
        this.qty = qty;
        this.totalPrice=totalPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
