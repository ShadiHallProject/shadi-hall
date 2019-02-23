package org.by9steps.shadihall.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Booking implements Parcelable {


    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };
    public int id;
public int book_id;
    public String ClientID;
    public String ClientUserID;
    public String NetCode;
    public String SysCode;
    public String BookingDate;
    public String UpdateDate;
    public String EventDate;
    public String ClientName;
    public String EventName;
    public String ClientAddress;
    public String ClientMobile;
    public String ClientCNIC;
    public String Description;
    public String Charges;
    public String TotalPersons;


    public Booking() {
    }


    protected Booking(Parcel in) {
        id = in.readInt();
        ClientID = in.readString();
        NetCode = in.readString();
        SysCode = in.readString();
        BookingDate = in.readString();

        ClientName = in.readString();
        ClientAddress = in.readString();
        ClientMobile = in.readString();
        ClientCNIC = in.readString();
        Charges = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(ClientID);
        dest.writeString(NetCode);
        dest.writeString(SysCode);
        dest.writeString(BookingDate);

        dest.writeString(ClientName);
        dest.writeString(ClientAddress);
        dest.writeString(ClientMobile);
        dest.writeString(ClientCNIC);
        dest.writeString(Charges);


    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }


    public String getEventDate() {
        return EventDate;
    }

    public void setEventDate(String eventDate) {
        EventDate = eventDate;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }


    public String getNetCode() {
        return NetCode;
    }

    public void setNetCode(String netCode) {
        NetCode = netCode;
    }


    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSysCode() {
        return SysCode;
    }

    public void setSysCode(String sysCode) {
        SysCode = sysCode;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
        BookingDate = bookingDate;
    }

    public String getTotalPersons() {
        return TotalPersons;
    }

    public void setTotalPersons(String totalPersons) {
        TotalPersons = totalPersons;
    }


    public String getClientUserID() {
        return ClientUserID;
    }

    public void setClientUserID(String clientUserID) {
        ClientUserID = clientUserID;
    }


    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientAddress() {
        return ClientAddress;
    }

    public void setClientAddress(String clientAddress) {
        ClientAddress = clientAddress;
    }

    public String getClientMobile() {
        return ClientMobile;
    }

    public void setClientMobile(String clientMobile) {
        ClientMobile = clientMobile;
    }

    public String getClientCNIC() {
        return ClientCNIC;
    }

    public void setClientCNIC(String clientCNIC) {
        ClientCNIC = clientCNIC;
    }

    public String getCharges() {
        return Charges;
    }

    public void setCharges(String charges) {
        Charges = charges;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }


}