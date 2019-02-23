package org.by9steps.shadihall.dbhelper;

public class KEYS {
    public static final String DB_NAME = "simple_db";
    public static final int VERSION = 1;
    public static final String TABLE_CASH = "cash_book";
    public static final String KEY_ID = "_id";
    public static final String KEY_ClientID = "ClientID";
    public static final String KEY_ClientUserID = "ClientUserID";
    public static final String KEY_NetCode = "NetCode";
    public static final String KEY_SysCode = "SysCode";
    public static final String KEY_UpdatedDate = "BookingDate";
    public static final String KEY_CBDate = "CBDate";
    public static final String KEY_DebitAccount = "DebitAccount";
    public static final String KEY_CreditAccount = "CreditAccount";
    public static final String KEY_CBRemarks = "CBRemarks";
    public static final String KEY_Amount = "Amount";
    public static final String TABLE_QUERY = "CREATE TABLE " + TABLE_CASH + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_ClientID + " TEXT," +
            KEY_ClientUserID + " TEXT," +
            KEY_NetCode + " TEXT," +
            KEY_SysCode + " TEXT," +
            KEY_UpdatedDate + " TEXT," +
            KEY_CBDate + " TEXT," +
            KEY_DebitAccount + " TEXT," +
            KEY_CreditAccount + " TEXT," +
            KEY_CBRemarks + " TEXT," +
            KEY_Amount + " TEXT)";
    public static final String TABLE_ACCOUNT3 = "account_name";
    public static final String KEY_GroupId = "AsGroupID";
    public static final String KEY_AcName = "Name";
    public static final String KEY_AcAddress = "YourAddress";
    public static final String KEY_AcMobileNo = "ClientMobile";
    public static final String KEY_AcEmailAddress = "CNICAddress";
    public static final String KEY_Salary = "Charges";
    public static final String KEY_AcContactNo = "ClientContactNo";
    public static final String KEY_AcPassword = "AcPassword";
    public static final String KEY_UserRights = "UserRights";
    public static final String TABLE_QUERY_ACCOUNT_NAME3 = "CREATE TABLE " + TABLE_ACCOUNT3 + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_ClientID + " TEXT," +
            KEY_NetCode + " TEXT," +
            KEY_SysCode + " TEXT," +
            KEY_UpdatedDate + " TEXT," +
            KEY_GroupId + " TEXT," +

            KEY_AcName + " TEXT," +
            KEY_AcAddress + " TEXT," +
            KEY_AcMobileNo + " TEXT," +
            KEY_AcEmailAddress + " TEXT," +
            KEY_Salary + " TEXT," +

            KEY_AcContactNo + " TEXT," +
            KEY_AcPassword + " TEXT," +
            KEY_UserRights + " TEXT)";
    public static final String TABLE_BOOKING = "booking";
    public static final String KEY_Update_Date = "UpdateDate";
    public static final String KEY_EVENT_Date = "EventDate";
    public static final String KEY_EVENT_NAME = "EventName";
    public static final String KEY_ClientName = "ClientName";
    public static final String KEY_ClientAddress = "ClientAddress";
    public static final String KEY_ClientMobileNo = "ClientMobileNo";
    public static final String KEY_ClientNICAddress = "ClientNicAddress";
    public static final String KEY_Charges = "Charges";
    public static final String KEY_Arrange_person = "Persons";
    public static final String KEY_Description = "Description";
    public static final String KEY_Client_ID = "ClientID";
    public static final String KEY_BOOKING_ID = "Book_ID";
    public static final String KEY_CLient_User_ID = "User_ID";
    public static final String KEY_Net_Code = "NetCode";
    public static final String KEY_Sys_Code = "SysCode";
    public static final String TABLE_QUERY_BOOKINGS = "CREATE TABLE " + TABLE_BOOKING + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_BOOKING_ID + " TEXT," +
            KEY_Net_Code + " TEXT," +
            KEY_Sys_Code + " TEXT," +
            KEY_Update_Date + " TEXT," +

            KEY_ClientName + " TEXT," +
            KEY_ClientAddress + " TEXT," +
            KEY_ClientMobileNo + " TEXT," +
            KEY_ClientNICAddress + " TEXT," +
            KEY_Charges + " TEXT," +
            KEY_Arrange_person + " TEXT," +
            KEY_EVENT_Date + " TEXT," +
            KEY_EVENT_NAME + " TEXT," +

            KEY_Description + " TEXT," +
            KEY_Client_ID + " TEXT," +
            KEY_CLient_User_ID + " TEXT)";

    public static final String TABLE_REQUEST = "Request";
//    public static final String KEY_EVENT_Date = "EventDate";
//    public static final String KEY_EVENT_NAME = "EventName";

    //   public static final String KEY_Update_Date = "UpdateDate";
    public static final String KEY_Request_Date = "UpdateDate";
    //   public static final String KEY_ClientName = "ClientName";
    public static final String KEY_Address = "ClientAddress";
    //   public static final String KEY_ClientMobileNo = "ClientMobileNo";
    //   public static final String KEY_ClientNICAddress = "ClientNicAddress";
    public static final String KEY_Read_REQUEST = "read";
    //   public static final String KEY_Client_ID = "ClientID";
    public static final String KEY_REQUEST_ID = "Request_ID";
    //   public static final String KEY_Description = "Description";
    //   public static final String KEY_Arrange_person = "Persons";
    //   public static final String KEY_CLient_User_ID = "User_ID";
    //   public static final String KEY_Net_Code = "NetCode";
    //   public static final String KEY_Sys_Code = "SysCode";
    public static final String TABLE_QUERY_REQUEST = "CREATE TABLE " + TABLE_REQUEST + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_REQUEST_ID + " TEXT," +
            KEY_Net_Code + " TEXT," +
            KEY_Sys_Code + " TEXT," +
            KEY_Update_Date + " TEXT," +
            KEY_Request_Date + " TEXT," +
            KEY_EVENT_Date + " TEXT," +
            KEY_EVENT_NAME + " TEXT," +

            KEY_ClientName + " TEXT," +
            KEY_Address + " TEXT," +
            KEY_ClientMobileNo + " TEXT," +
            KEY_Arrange_person + " TEXT," +

            KEY_Description + " TEXT," +
            KEY_Read_REQUEST + " TEXT," +
            KEY_Client_ID + " TEXT," +
            KEY_CLient_User_ID + " TEXT)";

    private static final String TAG = "simple";

}
