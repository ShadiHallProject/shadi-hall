package org.by9steps.shadihall.model;

import java.io.Serializable;

public class Client implements Serializable {

    String ClientID;
    String ClientParentID;
    String EntryType;
    String LoginMobileNo;
    String Distance;
    String CompanyName;
    String CompanyAddress;
    String CompanyNumber;
    String NameOfPerson;
    String Email;
    String WebSite;
    String Password;
    String ActiveClient;
    String Country;
    String City;
    String SubCity;
    String CapacityOfPersons;
    String ClientUserID;
    String SysCode;
    String NetCode;
    String UpdatedDate;
    String UserRights;
    String Lat;
    String Lng;
    String ProjectID;

    public Client(String clientID, String clientParentID, String entryType, String loginMobileNo, String companyName, String companyAddress, String companyNumber, String nameOfPerson, String email, String webSite, String password, String activeClient, String country, String city, String subCity, String capacityOfPersons, String clientUserID, String sysCode, String netCode, String updatedDate, String lat, String lng, String projectID) {
        ClientID = clientID;
        ClientParentID = clientParentID;
        EntryType = entryType;
        LoginMobileNo = loginMobileNo;
        CompanyName = companyName;
        CompanyAddress = companyAddress;
        CompanyNumber = companyNumber;
        NameOfPerson = nameOfPerson;
        Email = email;
        WebSite = webSite;
        Password = password;
        ActiveClient = activeClient;
        Country = country;
        City = city;
        SubCity = subCity;
        CapacityOfPersons = capacityOfPersons;
        ClientUserID = clientUserID;
        SysCode = sysCode;
        NetCode = netCode;
        UpdatedDate = updatedDate;
        Lat = lat;
        Lng = lng;
        ProjectID = projectID;
    }

    public Client(String clientID, String lat, String lng, String distance, String companyName, String companyAddress, String companyNumber, String webSite, String country, String city, String subCity, String projectID, String email, String capacityOfPersons) {
        ClientID = clientID;
        Lat = lat;
        Lng = lng;
        Distance = distance;
        CompanyName = companyName;
        CompanyAddress = companyAddress;
        CompanyNumber = companyNumber;
        WebSite = webSite;
        Country = country;
        City = city;
        SubCity = subCity;
        ProjectID = projectID;
        Email = email;
        CapacityOfPersons = capacityOfPersons;
    }

    public Client(String clientID) {
        ClientID = clientID;
    }

    //For Login Session
//    public Client(String clientID, String clientUserID, String userRights) {
//        ClientID = clientID;
//        ClientUserID = clientUserID;
//        UserRights = userRights;
//    }

    public Client() {
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLng() {
        return Lng;
    }

    public void setLng(String lng) {
        Lng = lng;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCompanyAddress() {
        return CompanyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        CompanyAddress = companyAddress;
    }

    public String getCompanyNumber() {
        return CompanyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        CompanyNumber = companyNumber;
    }

    public String getWebSite() {
        return WebSite;
    }

    public void setWebSite(String webSite) {
        WebSite = webSite;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getSubCity() {
        return SubCity;
    }

    public void setSubCity(String subCity) {
        SubCity = subCity;
    }

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCapacityOfPersons() {
        return CapacityOfPersons;
    }

    public void setCapacityOfPersons(String capacityOfPersons) {
        CapacityOfPersons = capacityOfPersons;
    }

    public String getClientUserID() {
        return ClientUserID;
    }

    public void setClientUserID(String clientUserID) {
        ClientUserID = clientUserID;
    }

    public String getUserRights() {
        return UserRights;
    }

    public void setUserRights(String userRights) {
        UserRights = userRights;
    }

    public String getClientParentID() {
        return ClientParentID;
    }

    public void setClientParentID(String clientParentID) {
        ClientParentID = clientParentID;
    }

    public String getEntryType() {
        return EntryType;
    }

    public void setEntryType(String entryType) {
        EntryType = entryType;
    }

    public String getLoginMobileNo() {
        return LoginMobileNo;
    }

    public void setLoginMobileNo(String loginMobileNo) {
        LoginMobileNo = loginMobileNo;
    }

    public String getNameOfPerson() {
        return NameOfPerson;
    }

    public void setNameOfPerson(String nameOfPerson) {
        NameOfPerson = nameOfPerson;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getActiveClient() {
        return ActiveClient;
    }

    public void setActiveClient(String activeClient) {
        ActiveClient = activeClient;
    }

    public String getSysCode() {
        return SysCode;
    }

    public void setSysCode(String sysCode) {
        SysCode = sysCode;
    }

    public String getNetCode() {
        return NetCode;
    }

    public void setNetCode(String netCode) {
        NetCode = netCode;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }
}
