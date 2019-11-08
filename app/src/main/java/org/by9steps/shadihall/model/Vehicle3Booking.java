package org.by9steps.shadihall.model;

public class Vehicle3Booking {

    private int ID;
    private String BookingID,VehicleID,BookingDate,BookingDetail,BookingCharges;
    private String SerialNo,ClientUserID,ClientID ,NetCode,SysCode ,UpdatedDate;

    public Vehicle3Booking() {
    }

    public Vehicle3Booking(int ID, String bookingID, String vehicleID, String bookingDate, String bookingDetail, String bookingCharges, String serialNo, String clientUserID, String clientID, String netCode, String sysCode, String updatedDate) {
        this.ID = ID;
        BookingID = bookingID;
        VehicleID = vehicleID;
        BookingDate = bookingDate;
        BookingDetail = bookingDetail;
        BookingCharges = bookingCharges;
        SerialNo = serialNo;
        ClientUserID = clientUserID;
        ClientID = clientID;
        NetCode = netCode;
        SysCode = sysCode;
        UpdatedDate = updatedDate;
    }

    public Vehicle3Booking(String bookingID, String vehicleID, String bookingDate, String bookingDetail, String bookingCharges, String serialNo, String clientUserID, String clientID, String netCode, String sysCode, String updatedDate) {
        BookingID = bookingID;
        VehicleID = vehicleID;
        BookingDate = bookingDate;
        BookingDetail = bookingDetail;
        BookingCharges = bookingCharges;
        SerialNo = serialNo;
        ClientUserID = clientUserID;
        ClientID = clientID;
        NetCode = netCode;
        SysCode = sysCode;
        UpdatedDate = updatedDate;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }

    public String getVehicleID() {
        return VehicleID;
    }

    public void setVehicleID(String vehicleID) {
        VehicleID = vehicleID;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
        BookingDate = bookingDate;
    }

    public String getBookingDetail() {
        return BookingDetail;
    }

    public void setBookingDetail(String bookingDetail) {
        BookingDetail = bookingDetail;
    }

    public String getBookingCharges() {
        return BookingCharges;
    }

    public void setBookingCharges(String bookingCharges) {
        BookingCharges = bookingCharges;
    }

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public String getClientUserID() {
        return ClientUserID;
    }

    public void setClientUserID(String clientUserID) {
        ClientUserID = clientUserID;
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
}
