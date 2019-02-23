package org.by9steps.shadihall.model;


import android.os.Parcel;
import android.os.Parcelable;

public class AccountName3 implements Parcelable {


    public static final Creator<AccountName3> CREATOR = new Creator<AccountName3>() {
        @Override
        public AccountName3 createFromParcel(Parcel in) {
            return new AccountName3(in);
        }

        @Override
        public AccountName3[] newArray(int size) {
            return new AccountName3[size];
        }
    };
    public int id;
    public String ClientID;
    public String NetCode;
    public String SysCode;
    public String UpdatedDate;
    public String AsGroupID;
    public String AcName;
    public String AcAddress;
    public String AcMobileNo;
    public String AcEmailAddress;
    public String Salary;
    public String AcContactNo;
    public String AcPassword;
    public String UserRights;


    public AccountName3() {
    }


    protected AccountName3(Parcel in) {
        id = in.readInt();
        ClientID = in.readString();
        NetCode = in.readString();
        SysCode = in.readString();
        UpdatedDate = in.readString();
        AsGroupID = in.readString();

        AcName = in.readString();
        AcAddress = in.readString();
        AcMobileNo = in.readString();
        AcEmailAddress = in.readString();
        Salary = in.readString();

        AcContactNo = in.readString();
        AcPassword = in.readString();
        UserRights = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(ClientID);
        dest.writeString(NetCode);
        dest.writeString(SysCode);
        dest.writeString(UpdatedDate);
        dest.writeString(AsGroupID);

        dest.writeString(AcName);
        dest.writeString(AcAddress);
        dest.writeString(AcMobileNo);
        dest.writeString(AcEmailAddress);
        dest.writeString(Salary);

        dest.writeString(AcContactNo);
        dest.writeString(AcPassword);
        dest.writeString(UserRights);

    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getAsGroupID() {
        return AsGroupID;
    }

    public void setAsGroupID(String asGroupID) {
        AsGroupID = asGroupID;
    }

    public String getAcName() {
        return AcName;
    }

    public void setAcName(String acName) {
        AcName = acName;
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

    public String getAcEmailAddress() {
        return AcEmailAddress;
    }

    public void setAcEmailAddress(String acEmailAddress) {
        AcEmailAddress = acEmailAddress;
    }

    public String getSalary() {
        return Salary;
    }

    public void setSalary(String salary) {
        Salary = salary;
    }

    public String getAcContactNo() {
        return AcContactNo;
    }

    public void setAcContactNo(String acContactNo) {
        AcContactNo = acContactNo;
    }

    public String getAcPassword() {
        return AcPassword;
    }

    public void setAcPassword(String acPassword) {
        AcPassword = acPassword;
    }

    public String getUserRights() {
        return UserRights;
    }

    public void setUserRights(String userRights) {
        UserRights = userRights;
    }
}