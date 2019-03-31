package org.by9steps.shadihall.model;

import java.io.Serializable;

public class Recovery implements Serializable {

    String ClientID;
    String BookingID;
    String Recieved;
    String Expensed;
    String ChargesTotal;
    String Balance;
    String Profit;
    String EventName;
    String EventDate;
    String ClientName;
    String month;

    private int isRow;

    public static Recovery createRow(String clientID, String bookingID, String recieved, String expensed, String chargesTotal, String balance, String profit, String eventName, String eventDate, String clientName){
        Recovery recovery = new Recovery();
        recovery.isRow = 1;
        recovery.ClientID = clientID;
        recovery.BookingID = bookingID;
        recovery.Recieved = recieved;
        recovery.Expensed = expensed;
        recovery.ChargesTotal = chargesTotal;
        recovery.Balance = balance;
        recovery.Profit = profit;
        recovery.EventName = eventName;
        recovery.EventDate = eventDate;
        recovery.ClientName = clientName;
        return recovery;
    }

    public static Recovery createTotal(String recieved, String expensed, String chargesTotal, String balance, String profit){
        Recovery recovery = new Recovery();
        recovery.isRow = 2;
        recovery.Recieved = recieved;
        recovery.Expensed = expensed;
        recovery.ChargesTotal = chargesTotal;
        recovery.Balance = balance;
        recovery.Profit = profit;
        return recovery;
    }

    public static Recovery createSection(String month){
        Recovery recovery = new Recovery();
        recovery.isRow = 0;
        recovery.month = month;

        return recovery;
    }

    public int isRow() {
        return isRow;
    }


    public String getClientID() {
        return ClientID;
    }

    public String getBookingID() {
        return BookingID;
    }

    public String getRecieved() {
        return Recieved;
    }

    public String getExpensed() {
        return Expensed;
    }

    public String getChargesTotal() {
        return ChargesTotal;
    }

    public String getBalance() {
        return Balance;
    }

    public String getProfit() {
        return Profit;
    }

    public String getEventName() {
        return EventName;
    }

    public String getEventDate() {
        return EventDate;
    }

    public String getClientName() {
        return ClientName;
    }

    public String getMonth() {
        return month;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }

    public void setRecieved(String recieved) {
        Recieved = recieved;
    }

    public void setExpensed(String expensed) {
        Expensed = expensed;
    }

    public void setChargesTotal(String chargesTotal) {
        ChargesTotal = chargesTotal;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public void setProfit(String profit) {
        Profit = profit;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

}
