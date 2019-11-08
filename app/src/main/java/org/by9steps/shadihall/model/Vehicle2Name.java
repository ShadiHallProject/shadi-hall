package org.by9steps.shadihall.model;

public class Vehicle2Name {
    private int ID;
    private String VehicleID, VehicleGroupID,VehicleName,Brand,Model,Colour,RegistrationNo,Account3ID,Status,Lng,Lat,ContactNo,SerialNo;
    private String ClientUserID,ClientID ,NetCode,SysCode ,UpdatedDate;

    public Vehicle2Name() {
    }

    public Vehicle2Name(int ID, String vehicleID, String vehicleGroupID, String vehicleName, String brand, String model, String colour, String registrationNo, String account3ID, String status, String lng, String lat, String contactNo, String serialNo, String clientUserID, String clientID, String netCode, String sysCode, String updatedDate) {
        this.ID = ID;
        VehicleID = vehicleID;
        VehicleGroupID = vehicleGroupID;
        VehicleName = vehicleName;
        Brand = brand;
        Model = model;
        Colour = colour;
        RegistrationNo = registrationNo;
        Account3ID = account3ID;
        Status = status;
        Lng = lng;
        Lat = lat;
        ContactNo = contactNo;
        SerialNo = serialNo;
        ClientUserID = clientUserID;
        ClientID = clientID;
        NetCode = netCode;
        SysCode = sysCode;
        UpdatedDate = updatedDate;
    }

    public Vehicle2Name(String vehicleID, String vehicleGroupID, String vehicleName, String brand, String model, String colour, String registrationNo, String account3ID, String status, String lng, String lat, String contactNo, String serialNo, String clientUserID, String clientID, String netCode, String sysCode, String updatedDate) {
        VehicleID = vehicleID;
        VehicleGroupID = vehicleGroupID;
        VehicleName = vehicleName;
        Brand = brand;
        Model = model;
        Colour = colour;
        RegistrationNo = registrationNo;
        Account3ID = account3ID;
        Status = status;
        Lng = lng;
        Lat = lat;
        ContactNo = contactNo;
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

    public String getVehicleID() {
        return VehicleID;
    }

    public String getVehicleGroupID() {
        return VehicleGroupID;
    }

    public String getVehicleName() {
        return VehicleName;
    }

    public String getBrand() {
        return Brand;
    }

    public String getModel() {
        return Model;
    }

    public String getColour() {
        return Colour;
    }

    public String getRegistrationNo() {
        return RegistrationNo;
    }

    public String getAccount3ID() {
        return Account3ID;
    }

    public String getStatus() {
        return Status;
    }

    public String getLng() {
        return Lng;
    }

    public String getLat() {
        return Lat;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public String getSerialNo() {
        return SerialNo;
    }

    public String getClientUserID() {
        return ClientUserID;
    }

    public String getClientID() {
        return ClientID;
    }

    public String getNetCode() {
        return NetCode;
    }

    public String getSysCode() {
        return SysCode;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setVehicleID(String vehicleID) {
        VehicleID = vehicleID;
    }

    public void setVehicleGroupID(String vehicleGroupID) {
        VehicleGroupID = vehicleGroupID;
    }

    public void setVehicleName(String vehicleName) {
        VehicleName = vehicleName;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public void setModel(String model) {
        Model = model;
    }

    public void setColour(String colour) {
        Colour = colour;
    }

    public void setRegistrationNo(String registrationNo) {
        RegistrationNo = registrationNo;
    }

    public void setAccount3ID(String account3ID) {
        Account3ID = account3ID;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setLng(String lng) {
        Lng = lng;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public void setClientUserID(String clientUserID) {
        ClientUserID = clientUserID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public void setNetCode(String netCode) {
        NetCode = netCode;
    }

    public void setSysCode(String sysCode) {
        SysCode = sysCode;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }
}
