package org.by9steps.shadihall.model;

import com.orm.SugarRecord;

public class Bookings extends SugarRecord {

    String BookingID;
    String ClientName;
    String ClientMobile;
    String ClientAddress;
    String ClientNic;
    String EventName;
    String BookingDate;
    String EventDate;
    String ChargesTotal;
    String Description;
    String ClientID;
    String ClientUserID;

    public Bookings() {
    }

    public Bookings(String bookingID, String clientName, String clientMobile, String clientAddress, String clientNic, String eventName, String bookingDate, String eventDate, String chargesTotal, String description, String clientID, String clientUserID) {
        BookingID = bookingID;
        ClientName = clientName;
        ClientMobile = clientMobile;
        ClientAddress = clientAddress;
        ClientNic = clientNic;
        EventName = eventName;
        BookingDate = bookingDate;
        EventDate = eventDate;
        ChargesTotal = chargesTotal;
        Description = description;
        ClientID = clientID;
        ClientUserID = clientUserID;
    }

    public String getBookingID() {
        return BookingID;
    }

    public String getClientName() {
        return ClientName;
    }

    public String getClientMobile() {
        return ClientMobile;
    }

    public String getClientAddress() {
        return ClientAddress;
    }

    public String getClientNic() {
        return ClientNic;
    }

    public String getEventName() {
        return EventName;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public String getEventDate() {
        return EventDate;
    }

    public String getChargesTotal() {
        return ChargesTotal;
    }

    public String getDescription() {
        return Description;
    }

    public String getClientID() {
        return ClientID;
    }

    public String getClientUserID() {
        return ClientUserID;
    }
}
