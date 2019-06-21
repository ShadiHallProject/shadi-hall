package org.by9steps.shadihall.model;

import com.orm.SugarRecord;

import java.io.Serializable;

public class Bookings implements Serializable {

    String id;
    String BookingID;
    String ClientName;
    String ClientMobile;
    String ClientAddress;
    String ClientNic;
    String EventName;
    String BookingDate;
    String EventDate;
    String ArrangePersons;
    String ChargesTotal;
    String Description;
    String ClientID;
    String ClientUserID;
    String NetCode;
    String SysCode;
    String UpdatedDate;
    String Amount;

    public Bookings() {
    }

    public Bookings(String bookingID, String clientName, String clientMobile, String clientAddress, String clientNic, String eventName, String bookingDate, String eventDate, String arrangePersons, String chargesTotal, String description, String clientID, String clientUserID, String netCode, String sysCode, String updatedDate) {
        BookingID = bookingID;
        ClientName = clientName;
        ClientMobile = clientMobile;
        ClientAddress = clientAddress;
        ClientNic = clientNic;
        EventName = eventName;
        BookingDate = bookingDate;
        EventDate = eventDate;
        ArrangePersons = arrangePersons;
        ChargesTotal = chargesTotal;
        Description = description;
        ClientID = clientID;
        ClientUserID = clientUserID;
        NetCode = netCode;
        SysCode = sysCode;
        UpdatedDate = updatedDate;
    }

    public Bookings(String bookingID, String clientName, String clientMobile, String clientAddress, String clientNic, String eventName, String bookingDate, String eventDate, String arrangePersons, String chargesTotal, String description, String clientID, String clientUserID, String netCode, String sysCode, String updatedDate, String amount) {
        BookingID = bookingID;
        ClientName = clientName;
        ClientMobile = clientMobile;
        ClientAddress = clientAddress;
        ClientNic = clientNic;
        EventName = eventName;
        BookingDate = bookingDate;
        EventDate = eventDate;
        ArrangePersons = arrangePersons;
        ChargesTotal = chargesTotal;
        Description = description;
        ClientID = clientID;
        ClientUserID = clientUserID;
        NetCode = netCode;
        SysCode = sysCode;
        UpdatedDate = updatedDate;
        Amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientMobile() {
        return ClientMobile;
    }

    public void setClientMobile(String clientMobile) {
        ClientMobile = clientMobile;
    }

    public String getClientAddress() {
        return ClientAddress;
    }

    public void setClientAddress(String clientAddress) {
        ClientAddress = clientAddress;
    }

    public String getClientNic() {
        return ClientNic;
    }

    public void setClientNic(String clientNic) {
        ClientNic = clientNic;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
        BookingDate = bookingDate;
    }

    public String getEventDate() {
        return EventDate;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public String getArrangePersons() {
        return ArrangePersons;
    }

    public void setArrangePersons(String arrangePersons) {
        ArrangePersons = arrangePersons;
    }

    public String getChargesTotal() {
        return ChargesTotal;
    }

    public void setChargesTotal(String chargesTotal) {
        ChargesTotal = chargesTotal;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
