package org.by9steps.shadihall.model;

public class Recovery {

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

    public Recovery(String clientID, String bookingID, String recieved, String expensed, String chargesTotal, String balance, String profit, String eventName, String eventDate, String clientName) {
        ClientID = clientID;
        BookingID = bookingID;
        Recieved = recieved;
        Expensed = expensed;
        ChargesTotal = chargesTotal;
        Balance = balance;
        Profit = profit;
        EventName = eventName;
        EventDate = eventDate;
        ClientName = clientName;
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
}
