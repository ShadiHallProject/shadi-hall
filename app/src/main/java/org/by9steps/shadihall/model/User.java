package org.by9steps.shadihall.model;

import com.orm.SugarRecord;

public class User extends SugarRecord {
    String ClientID;
    String CashID;
    String BookingIncomeID;
    String BookingExpenseID;
    String AcNameID;
    String ClientUserID;
    String AcName;

    public User() {
    }

    public User(String clientID, String cashID, String bookingIncomeID, String bookingExpenseID, String acNameID, String clientUserID, String acName) {
        ClientID = clientID;
        CashID = cashID;
        BookingIncomeID = bookingIncomeID;
        BookingExpenseID = bookingExpenseID;
        AcNameID = acNameID;
        ClientUserID = clientUserID;
        AcName = acName;
    }

    public String getClientID() {
        return ClientID;
    }

    public String getCashID() {
        return CashID;
    }

    public String getBookingIncomeID() {
        return BookingIncomeID;
    }

    public String getBookingExpenseID() {
        return BookingExpenseID;
    }

    public String getAcNameID() {
        return AcNameID;
    }

    public String getClientUserID() {
        return ClientUserID;
    }

    public String getAcName() {
        return AcName;
    }
}
