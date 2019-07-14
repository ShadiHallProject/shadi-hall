package org.by9steps.shadihall.model;

import java.io.Serializable;

public class ShadiHallList implements Serializable {

    String AcMobileNo;
    String UserRights;
    String AcName;
    String ClientID;
    String CompanyName;
    String CompanyAddress;
    String ProjectID;
    String ProjectName;

    public ShadiHallList(String acMobileNo, String userRights, String acName, String clientID, String companyName, String companyAddress, String projectID, String projectName) {
        AcMobileNo = acMobileNo;
        UserRights = userRights;
        AcName = acName;
        ClientID = clientID;
        CompanyName = companyName;
        CompanyAddress = companyAddress;
        ProjectID = projectID;
        ProjectName = projectName;
    }

    public String getAcMobileNo() {
        return AcMobileNo;
    }

    public void setAcMobileNo(String acMobileNo) {
        AcMobileNo = acMobileNo;
    }

    public String getUserRights() {
        return UserRights;
    }

    public void setUserRights(String userRights) {
        UserRights = userRights;
    }

    public String getAcName() {
        return AcName;
    }

    public void setAcName(String acName) {
        AcName = acName;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
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

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }
}
