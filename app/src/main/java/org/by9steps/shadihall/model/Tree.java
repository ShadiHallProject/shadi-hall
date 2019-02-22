package org.by9steps.shadihall.model;

import com.orm.SugarRecord;

public class Tree extends SugarRecord {

    String ClientID;
    String ClientParentID;
    String EntryType;
    String LoginMobile;
    String CompanyName;
    String CompanyAddress;
    String CompanyNumber;
    String NameofPerson;
    String Email;

    public Tree(String clientID, String clientParentID, String entryType, String loginMobile, String companyName, String companyAddress, String companyNumber, String nameofPerson, String email) {
        ClientID = clientID;
        ClientParentID = clientParentID;
        EntryType = entryType;
        LoginMobile = loginMobile;
        CompanyName = companyName;
        CompanyAddress = companyAddress;
        CompanyNumber = companyNumber;
        NameofPerson = nameofPerson;
        Email = email;
    }

    public Tree() {
    }

    public String getClientID() {
        return ClientID;
    }

    public String getClientParentID() {
        return ClientParentID;
    }

    public String getEntryType() {
        return EntryType;
    }

    public String getLoginMobile() {
        return LoginMobile;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public String getCompanyAddress() {
        return CompanyAddress;
    }

    public String getCompanyNumber() {
        return CompanyNumber;
    }

    public String getNameofPerson() {
        return NameofPerson;
    }

    public String getEmail() {
        return Email;
    }
}
