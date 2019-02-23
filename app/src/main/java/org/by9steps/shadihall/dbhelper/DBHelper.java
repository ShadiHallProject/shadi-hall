package org.by9steps.shadihall.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "simple_db";
    public static final int VERSION = 1;
    public static final String TABLE_CASH = "cash_book";
    public static final String KEY_ID = "_id";
    public static final String KEY_ClientID = "ClientID";
    public static final String KEY_ClientUserID = "ClientUserID";
    public static final String KEY_NetCode = "NetCode";
    public static final String KEY_SysCode = "SysCode";
    public static final String KEY_UpdatedDate = "UpdatedDate";
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
    public static final String KEY_AcName = "AcName";
    public static final String KEY_AcAddress = "AcAddress";
    public static final String KEY_AcMobileNo = "AcMobileNo";
    public static final String KEY_AcEmailAddress = "AcEmailAddress";
    public static final String KEY_Salary = "Salary";
    public static final String KEY_AcContactNo = "AcContactNo";
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
    public static final String KEY_BOOKING_Date ="book_date" ;
    public static final String TABLE_BOOKING ="booking" ;
    public static final String KEY_Description ="description" ;
    public static final String KEY_Arrange_person = "total_person";
    public static final String KEY_Charges ="charges" ;
    public static final String KEY_ClientNICAddress ="CNIC" ;
    public static final String KEY_ClientMobileNo = "mobile";
    public static final String KEY_ClientAddress = "address";
    public static final String KEY_ClientName = "client_name";
    public static final String KEY_EVENT_NAME = "event_name";
    public static final String KEY_EVENT_Date ="event_date" ;
    public static final String KEY_BOOKING_ID ="book_id" ;
    public static final String KEY_Client_ID ="client_id" ;
    public static final String KEY_CLient_User_ID ="client_user_id" ;
    public static final String KEY_Net_Code = "net_code";
    public static final String KEY_Sys_Code = "sys_code";
    public static final String KEY_Update_Date ="update_date" ;
    public static final String TABLE_REQUEST ="Request" ;
    public static final String TABLE_QUERY_BOOKING = "CREATE TABLE " + TABLE_BOOKING + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_BOOKING_Date + " TEXT," +
            KEY_Description + " TEXT," +
            KEY_Arrange_person + " TEXT," +
            KEY_Charges + " TEXT," +
            KEY_ClientNICAddress + " TEXT," +

            KEY_ClientMobileNo + " TEXT," +
            KEY_ClientAddress + " TEXT," +
            KEY_ClientName + " TEXT," +
            KEY_EVENT_NAME + " TEXT," +
            KEY_EVENT_Date + " TEXT," +
            KEY_BOOKING_ID + " TEXT," +
            KEY_Client_ID + " TEXT," +

            KEY_CLient_User_ID + " TEXT," +
            KEY_Net_Code + " TEXT," +
            KEY_Sys_Code + " TEXT," +
            KEY_Update_Date + " TEXT)";

    public static final String TABLE_QUERY_REQUEST = "CREATE TABLE " + TABLE_REQUEST + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
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

    private static final String TAG = "simple";
    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_QUERY);
        db.execSQL(TABLE_QUERY_ACCOUNT_NAME3);
        db.execSQL(TABLE_QUERY_BOOKING);
//        db.execSQL(TABLE_QUERY_REQUEST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING);
  //      db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUEST);

        onCreate(db);
    }


}
