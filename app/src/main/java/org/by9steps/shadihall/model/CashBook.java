package org.by9steps.shadihall.model;


import android.os.Parcel;
import android.os.Parcelable;

public class CashBook implements Parcelable {


    public static final Creator<CashBook> CREATOR = new Creator<CashBook>() {
        @Override
        public CashBook createFromParcel(Parcel in) {
            return new CashBook(in);
        }

        @Override
        public CashBook[] newArray(int size) {
            return new CashBook[size];
        }
    };
    public int id;
    public String ClientID;
    public String ClientUserID;
    public String NetCode;
    public String SysCode;
    public String UpdatedDate;
    public String CBDate;
    public String DebitAccount;
    public String CreditAccount;
    public String CBRemarks;
    public String Amount;


    public CashBook() {
    }


    protected CashBook(Parcel in) {
        id = in.readInt();
        ClientID = in.readString();
        ClientUserID = in.readString();
        NetCode = in.readString();
        SysCode = in.readString();
        UpdatedDate = in.readString();
        CBDate = in.readString();
        DebitAccount = in.readString();
        CreditAccount = in.readString();
        CBRemarks = in.readString();
        Amount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(ClientID);
        dest.writeString(ClientUserID);
        dest.writeString(NetCode);
        dest.writeString(SysCode);
        dest.writeString(UpdatedDate);
        dest.writeString(CBDate);
        dest.writeString(DebitAccount);
        dest.writeString(CreditAccount);
        dest.writeString(CBRemarks);
        dest.writeString(Amount);

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

    public String getCBDate() {
        return CBDate;
    }

    public void setCBDate(String CBDate) {
        this.CBDate = CBDate;
    }

    public String getDebitAccount() {
        return DebitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        DebitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return CreditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        CreditAccount = creditAccount;
    }

    public String getCBRemarks() {
        return CBRemarks;
    }

    public void setCBRemarks(String CBRemarks) {
        this.CBRemarks = CBRemarks;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}