package org.by9steps.shadihall.model;

public class ActiveClients {

    String AcMobileNo;
    String UserRights;
    String AcName;
    String ClientID;
    String CompanyName;
    String CompanyAddress;
    String ProjectID;
    String ProjectName;
    String ClientUserID;

    public ActiveClients(String acMobileNo, String userRights, String acName, String clientID, String companyName, String companyAddress, String projectID, String projectName, String clientUserID) {
        AcMobileNo = acMobileNo;
        UserRights = userRights;
        AcName = acName;
        ClientID = clientID;
        CompanyName = companyName;
        CompanyAddress = companyAddress;
        ProjectID = projectID;
        ProjectName = projectName;
        ClientUserID = clientUserID;
    }

    public ActiveClients() {
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

    public String getClientUserID() {
        return ClientUserID;
    }

    public void setClientUserID(String clientUserID) {
        ClientUserID = clientUserID;
    }
}
