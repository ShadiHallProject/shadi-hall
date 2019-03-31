package org.by9steps.shadihall.model;

import java.io.Serializable;

public class Account3Name implements Serializable {

    String AcNameID;
    String AcName;
    String AcGroupID;
    String AcAddress;
    String AcMobileNo;
    String AcContactNo;
    String AcEmailAddress;
    String AcDebitBal;
    String AcCreditBal;
    String AcPassward;
    String ClientID;
    String ClientUserID;
    String SysCode;
    String NetCode;
    String UpdatedDate;
    String SerialNo;
    String UserRights;
    String SecurityRights;
    String Salary;

    public Account3Name(String acNameID, String acName, String acGroupID, String acAddress, String acMobileNo, String acContactNo, String acEmailAddress, String acDebitBal, String acCreditBal, String acPassward, String clientID, String clientUserID, String sysCode, String netCode, String updatedDate, String serialNo, String userRights, String securityRights, String salary) {
        AcNameID = acNameID;
        AcName = acName;
        AcGroupID = acGroupID;
        AcAddress = acAddress;
        AcMobileNo = acMobileNo;
        AcContactNo = acContactNo;
        AcEmailAddress = acEmailAddress;
        AcDebitBal = acDebitBal;
        AcCreditBal = acCreditBal;
        AcPassward = acPassward;
        ClientID = clientID;
        ClientUserID = clientUserID;
        SysCode = sysCode;
        NetCode = netCode;
        UpdatedDate = updatedDate;
        SerialNo = serialNo;
        UserRights = userRights;
        SecurityRights = securityRights;
        Salary = salary;
    }

    public Account3Name() {
    }

    public String getAcNameID() {
        return AcNameID;
    }

    public void setAcNameID(String acNameID) {
        AcNameID = acNameID;
    }

    public String getAcName() {
        return AcName;
    }

    public void setAcName(String acName) {
        AcName = acName;
    }

    public String getAcGroupID() {
        return AcGroupID;
    }

    public void setAcGroupID(String acGroupID) {
        AcGroupID = acGroupID;
    }

    public String getAcAddress() {
        return AcAddress;
    }

    public void setAcAddress(String acAddress) {
        AcAddress = acAddress;
    }

    public String getAcMobileNo() {
        return AcMobileNo;
    }

    public void setAcMobileNo(String acMobileNo) {
        AcMobileNo = acMobileNo;
    }

    public String getAcContactNo() {
        return AcContactNo;
    }

    public void setAcContactNo(String acContactNo) {
        AcContactNo = acContactNo;
    }

    public String getAcEmailAddress() {
        return AcEmailAddress;
    }

    public void setAcEmailAddress(String acEmailAddress) {
        AcEmailAddress = acEmailAddress;
    }

    public String getAcDebitBal() {
        return AcDebitBal;
    }

    public void setAcDebitBal(String acDebitBal) {
        AcDebitBal = acDebitBal;
    }

    public String getAcCreditBal() {
        return AcCreditBal;
    }

    public void setAcCreditBal(String acCreditBal) {
        AcCreditBal = acCreditBal;
    }

    public String getAcPassward() {
        return AcPassward;
    }

    public void setAcPassward(String acPassward) {
        AcPassward = acPassward;
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

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public String getUserRights() {
        return UserRights;
    }

    public void setUserRights(String userRights) {
        UserRights = userRights;
    }

    public String getSecurityRights() {
        return SecurityRights;
    }

    public void setSecurityRights(String securityRights) {
        SecurityRights = securityRights;
    }

    public String getSalary() {
        return Salary;
    }

    public void setSalary(String salary) {
        Salary = salary;
    }
}
