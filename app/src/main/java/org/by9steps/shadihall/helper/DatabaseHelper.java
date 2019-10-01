package org.by9steps.shadihall.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.by9steps.shadihall.model.Account1Type;
import org.by9steps.shadihall.model.Account2Group;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.ActiveClients;
import org.by9steps.shadihall.model.BalSheet;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.ChartOfAcc;
import org.by9steps.shadihall.model.Client;
import org.by9steps.shadihall.model.GeneralLedger;
import org.by9steps.shadihall.model.Item1Type;
import org.by9steps.shadihall.model.Item2Group;
import org.by9steps.shadihall.model.MonthTb;
import org.by9steps.shadihall.model.ProfitLoss;
import org.by9steps.shadihall.model.ProjectMenu;
import org.by9steps.shadihall.model.Projects;
import org.by9steps.shadihall.model.Recovery;
import org.by9steps.shadihall.model.Reports;
import org.by9steps.shadihall.model.Spinner;
import org.by9steps.shadihall.model.Summerize;
import org.by9steps.shadihall.model.Voucher;
import org.by9steps.shadihall.model.item3name.Item3Name_;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "ShadiHallUser";

    // Table Names
    private static final String TABLE_Account3Name = "Account3Name";
    private static final String TABLE_Account1Type = "Account1Type";
    private static final String TABLE_Account2Group = "Account2Group";
    private static final String TABLE_CashBook = "CashBook";
    private static final String TABLE_Booking = "Booking";
    private static final String TABLE_Client = "Client";
    private static final String TABLE_Project = "Project";
    private static final String TABLE_ProjectMenu = "ProjectMenu";
    private static final String TABLE_Account4UserRights = "Account4UserRights";
    private static final String TABLE_Item1Type = "Item1Type";
    private static final String TABLE_Item2Group = "Item2Group";
    private static final String TABLE_Item3Name = "Item3Name";
    private static final String TABLE_SalePur1 = "SalePur1";
    private static final String TABLE_SalePur2 = "SalePur2";
    private static final String TABLE_SalePurLocation = "SalePurLocation";
    private static final String TABLE_ActiveAccounts = "ActiveAccounts";

    // Account3Name Table - column names
    private static final String KEY_AcNameID = "AcNameID";
    private static final String KEY_AcName = "AcName";
    private static final String KEY_AcGroupID = "AcGroupID";
    private static final String KEY_AcAddress = "AcAddress";
    private static final String KEY_AcMobileNo = "AcMobileNo";
    private static final String KEY_AcContactNo = "AcContactNo";
    private static final String KEY_AcEmailAddress = "AcEmailAddress";
    private static final String KEY_AcDebitBal = "AcDebitBal";
    private static final String KEY_AcCreditBal = "AcCreditBal";
    private static final String KEY_AcPassward = "AcPassward";
    private static final String KEY_ClientID = "ClientID";
    private static final String KEY_ClientUserID = "ClientUserID";
    private static final String KEY_SysCode = "SysCode";
    private static final String KEY_NetCode = "NetCode";
    private static final String KEY_UpdatedDate = "UpdatedDate";
    private static final String KEY_SerialNo = "SerialNo";
    private static final String KEY_UserRights = "UserRights";
    private static final String KEY_SecurityRights = "SecurityRights";
    private static final String KEY_Salary = "Salary";

    // Account1Type Table - column names
    private static final String KEY_AcTypeID = "AcTypeID";
    private static final String KEY_AcTypeName = "AcTypeName";

    // Account2Group Table - column names
    private static final String KEY_AcGroupID2 = "AcGroupID";
    private static final String KEY_AcTypeID2 = "AcTypeID";
    private static final String KEY_AcGruopName = "AcGruopName";

    // CashBook Table - column names
    private static final String KEY_ID = "ID";
    private static final String KEY_CashBookID = "CashBookID";
    private static final String KEY_CBDate = "CBDate";
    private static final String KEY_DebitAccount = "DebitAccount";
    private static final String KEY_CreditAccount = "CreditAccount";
    private static final String KEY_CBRemarks = "CBRemarks";
    private static final String KEY_Amount = "Amount";
    private static final String KEY_ClientID2 = "ClientID";
    private static final String KEY_ClientUserID2 = "ClientUserID";
    private static final String KEY_NetCode2 = "NetCode";
    private static final String KEY_SysCode2 = "SysCode";
    private static final String KEY_UpdatedDate2 = "UpdatedDate";
    private static final String KEY_TableID = "TableID";
    private static final String KEY_SerialNo2 = "SerialNo";
    private static final String KEY_TableName = "TableName";

    // Booking Table - column names
    private static final String KEY_BookingID3 = "BookingID";
    private static final String KEY_ClientName = "ClientName";
    private static final String KEY_ClientMobile = "ClientMobile";
    private static final String KEY_ClientAddress = "ClientAddress";
    private static final String KEY_ClientNic = "ClientNic";
    private static final String KEY_EventName = "EventName";
    private static final String KEY_BookingDate = "BookingDate";
    private static final String KEY_EventDate = "EventDate";
    private static final String KEY_ArrangePersons = "ArrangePersons";
    private static final String KEY_ChargesTotal = "ChargesTotal";
    private static final String KEY_Description = "Description";
    private static final String KEY_ClientID3 = "ClientID";
    private static final String KEY_ClientUserID3 = "ClientUserID";
    private static final String KEY_NetCode3 = "NetCode";
    private static final String KEY_SysCode3 = "SysCode";
    private static final String KEY_UpdatedDate3 = "UpdatedDate";
    private static final String KEY_Advance = "Advance";
    private static final String KEY_Shift = "Shift";
    private static final String KEY_SerialNo3 = "SerialNo";

    //New Tables

    //Client Table - column names
    private static final String KEY_ClientID4 = "ClientID";
    private static final String KEY_ClientParentID = "ClientParentID";
    private static final String KEY_EntryType = "EntryType";
    private static final String KEY_LoginMobileNo = "LoginMobileNo";
    private static final String KEY_CompanyName = "CompanyName";
    private static final String KEY_CompanyAddress = "CompanyAddress";
    private static final String KEY_CompanyNumber = "CompanyNumber";
    private static final String KEY_NameOfPerson = "NameOfPerson";
    private static final String KEY_Email = "Email";
    private static final String KEY_WebSite = "WebSite";
    private static final String KEY_Password = "Password";
    private static final String KEY_ActiveClient = "ActiveClient";
    private static final String KEY_Country = "Country";
    private static final String KEY_City = "City";
    private static final String KEY_SubCity = "SubCity";
    private static final String KEY_CapacityOfPersons = "CapacityOfPersons";
    private static final String KEY_ClientUserID4 = "ClientUserID";
    private static final String KEY_SysCode4 = "SysCode";
    private static final String KEY_NetCode4 = "NetCode";
    private static final String KEY_UpdatedDate4 = "UpdatedDate";
    private static final String KEY_Lat = "Lat";
    private static final String KEY_Lng = "Lng";
    private static final String KEY_BookingTermsAndCondition = "BookingTermsAndCondition";
    private static final String KEY_ProjectID = "ProjectID";
    private static final String KEY_UserRights4 = "UserRights";

    //Project Table - column names
    private static final String KEY_ProjectID5 = "ProjectID";
    private static final String KEY_ProjectName = "ProjectName";
    private static final String KEY_ProjectDescription = "ProjectDescription";
    private static final String KEY_ProjectType = "ProjectType";

    //ProjectMenu Table - column names
    private static final String KEY_MenuID = "MenuID";
    private static final String KEY_ProjectID6 = "ProjectID";
    private static final String KEY_MenuGroup = "MenuGroup";
    private static final String KEY_MenuName = "MenuName";
    private static final String KEY_PageOpen = "PageOpen";
    private static final String KEY_ValuePass = "ValuePass";
    private static final String KEY_ImageName = "ImageName";
    private static final String KEY_GroupSortBy = "GroupSortBy";
    private static final String KEY_SortBy = "SortBy";

    //Account4UserRights Table - column names
    private static final String KEY_AcRightsID = "AcRightsID";
    private static final String KEY_Account3ID = "Account3ID";
    private static final String KEY_MenuName11 = "MenuName";
    private static final String KEY_PageOpen11 = "PageOpen";
    private static final String KEY_ValuePass11 = "ValuePass";
    private static final String KEY_ImageName11 = "ImageName";
    private static final String KEY_Inserting = "Inserting";
    private static final String KEY_Edting = "Edting";
    private static final String KEY_Deleting = "Deleting";
    private static final String KEY_Reporting = "Reporting";
    private static final String KEY_ClientID11 = "ClientID";
    private static final String KEY_ClientUserID11 = "ClientUserID";
    private static final String KEY_NetCode11 = "NetCode";
    private static final String KEY_SysCode11 = "SysCode";
    private static final String KEY_UpdatedDate11 = "UpdateDate";
    private static final String KEY_SortBy11 = "SortBy";

    //Item1Type Table - column names
    private static final String KEY_Item1TypeID = "Item1TypeID";
    private static final String KEY_ItemType = "ItemType";

    //Item2Group Table - column names
    private static final String KEY_Item2GroupID = "Item2GroupID";
    private static final String KEY_Item1TID = "Item1TypeID";
    private static final String KEY_Item2GroupName = "Item2GroupName";
    private static final String KEY_ClientID6 = "ClientID";
    private static final String KEY_ClientUserID6 = "ClientUserID";
    private static final String KEY_NetCode6 = "NetCode";
    private static final String KEY_SysCode6 = "SysCode";
    private static final String KEY_UpdatedDate6 = "UpdatedDate";


    //Item3Name Table - column names
    private static final String KEY_Item3NameID = "Item3NameID";
    private static final String KEY_Item2GroupID7 = "Item2GroupID";
    private static final String KEY_ItemName = "ItemName";
    private static final String KEY_ClientID7 = "ClientID";
    private static final String KEY_ClientUserID7 = "ClientUserID";
    private static final String KEY_NetCode7 = "NetCode";
    private static final String KEY_SysCode7 = "SysCode";
    private static final String KEY_UpdatedDate7 = "UpdatedDate";
    private static final String KEY_SalePrice7 = "SalePrice";
    private static final String KEY_ItemCode7 = "ItemCode";
    private static final String KEY_Stock7 = "Stock";

    //SalePur1 Table - column names
    private static final String KEY_SalePur1ID = "SalePur1ID";
    private static final String KEY_SerialNo8 = "SerialNo";
    private static final String KEY_EntryType8 = "EntryType";
    private static final String KEY_SPDate = "SPDate";
    private static final String KEY_AccountID = "AccountID";
    private static final String KEY_Remarks = "Remarks";
    private static final String KEY_ClientID8 = "ClientID";
    private static final String KEY_ClientUserID8 = "ClientUserID";
    private static final String KEY_NetCode8 = "NetCode";
    private static final String KEY_SysCode8 = "SysCode";
    private static final String KEY_UpdatedDate8 = "UpdatedDate";
    private static final String KEY_Name = "Name";
    private static final String KEY_DueDate = "DueDate";

    //SalePur2 Table - column names
    private static final String KEY_SalePur2ID = "SalePur2ID";
    private static final String KEY_SalePur1ID9 = "SalePur1ID";
    private static final String KEY_EntryType9 = "EntryType";
    private static final String KEY_ItemCode = "ItemCode";
    private static final String KEY_ItemDescription = "ItemDescription";
    private static final String KEY_QtyAdd = "QtyAdd";
    private static final String KEY_QtyLess = "QtyLess";
    private static final String KEY_Qty = "Qty";
    private static final String KEY_Price = "Price";
    private static final String KEY_Total = "Total";
    private static final String KEY_Location = "Location";
    private static final String KEY_ClientID9 = "ClientID";
    private static final String KEY_ClientUserID9 = "ClientUserID";
    private static final String KEY_NetCode9 = "NetCode";
    private static final String KEY_SysCode9 = "SysCode";
    private static final String KEY_UpdatedDate9 = "UpdatedDate";

    //SalePurLocation Table - column names
    private static final String KEY_LocationID = "LocationID";
    private static final String KEY_LocationName = "LocationName";
    private static final String KEY_ClientID10 = "ClientID";
    private static final String KEY_ClientUserID10 = "ClientUserID";
    private static final String KEY_NetCode10 = "NetCode";
    private static final String KEY_SysCode10 = "SysCode";
    private static final String KEY_UpdatedDate10 = "UpdatedDate";

    //ActiveAccounts Table - column names
    private static final String KEY_AcMobileNo12 = "AcMobileNo";
    private static final String KEY_UserRights12 = "UserRights";
    private static final String KEY_AcName12 = "AcName";
    private static final String KEY_ClientID12 = "ClientID";
    private static final String KEY_CompanyName12 = "CompanyName";
    private static final String KEY_CompanyAddress12 = "CompanyAddress";
    private static final String KEY_ProjectID12 = "ProjectID";
    private static final String KEY_ProjectName12 = "ProjectName";
    private static final String KEY_ClientUserID12 = "ClientUserID";

    // Table Create Statements
    // Account3Name table create statement
    private static final String CREATE_TABLE_Account3Name = "CREATE TABLE "
            + TABLE_Account3Name + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_AcNameID + " TEXT,"
            + KEY_AcName + " TEXT,"
            + KEY_AcGroupID + " TEXT,"
            + KEY_AcAddress + " TEXT,"
            + KEY_AcMobileNo + " TEXT,"
            + KEY_AcContactNo + " TEXT,"
            + KEY_AcEmailAddress + " TEXT,"
            + KEY_AcDebitBal + " TEXT,"
            + KEY_AcCreditBal + " TEXT,"
            + KEY_AcPassward + " TEXT,"
            + KEY_ClientID + " TEXT,"
            + KEY_ClientUserID + " TEXT,"
            + KEY_SysCode + " TEXT,"
            + KEY_NetCode + " TEXT,"
            + KEY_UpdatedDate + " TEXT,"
            + KEY_SerialNo + " INTEGER,"
            + KEY_UserRights + " TEXT,"
            + KEY_SecurityRights + " TEXT,"
            + KEY_Salary + " TEXT" + ")";

    // CashBook table create statement
    private static final String CREATE_TABLE_CashBook = "CREATE TABLE "
            + TABLE_CashBook + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CashBookID + " TEXT,"
            + KEY_CBDate + " TEXT,"
            + KEY_DebitAccount + " TEXT,"
            + KEY_CreditAccount + " TEXT,"
            + KEY_CBRemarks + " TEXT,"
            + KEY_Amount + " TEXT,"
            + KEY_ClientID2 + " TEXT,"
            + KEY_ClientUserID2 + " TEXT,"
            + KEY_NetCode2 + " TEXT,"
            + KEY_SysCode2 + " TEXT,"
            + KEY_UpdatedDate2 + " TEXT,"
            + KEY_TableID + " TEXT,"
            + KEY_SerialNo2 + " TEXT,"
            + KEY_TableName + " INTEGER" + ")";

    // Booking table create statement
    private static final String CREATE_TABLE_Booking = "CREATE TABLE "
            + TABLE_Booking + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_BookingID3 + " TEXT,"
            + KEY_ClientName + " TEXT,"
            + KEY_ClientMobile + " TEXT,"
            + KEY_ClientAddress + " TEXT,"
            + KEY_ClientNic + " TEXT,"
            + KEY_EventName + " TEXT,"
            + KEY_BookingDate + " TEXT,"
            + KEY_EventDate + " TEXT,"
            + KEY_ArrangePersons + " TEXT,"
            + KEY_ChargesTotal + " TEXT,"
            + KEY_Description + " TEXT,"
            + KEY_ClientID3 + " TEXT,"
            + KEY_ClientUserID3 + " TEXT,"
            + KEY_NetCode3 + " TEXT,"
            + KEY_SysCode3 + " TEXT,"
            + KEY_UpdatedDate3 + " TEXT,"
            + KEY_Advance + " TEXT,"
            + KEY_Shift + " TEXT,"
            + KEY_SerialNo3 + " INTEGER" + ")";

    // Account2Group table create statement
    private static final String CREATE_TABLE_Account2Group = "CREATE TABLE "
            + TABLE_Account2Group + "(" + KEY_AcGroupID + " TEXT,"
            + KEY_AcTypeID + " TEXT,"
            + KEY_AcGruopName + " TEXT" + ")";

    // Account1Type table create statement
    private static final String CREATE_TABLE_Account1Type = "CREATE TABLE "
            + TABLE_Account1Type + "(" + KEY_AcTypeID + " TEXT,"
            + KEY_AcTypeName + " TEXT" + ")";

    // Client table create statement
    private static final String CREATE_TABLE_Client = "CREATE TABLE "
            + TABLE_Client + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ClientID4 + " TEXT,"
            + KEY_ClientParentID + " TEXT,"
            + KEY_EntryType + " TEXT,"
            + KEY_LoginMobileNo + " TEXT,"
            + KEY_CompanyName + " TEXT,"
            + KEY_CompanyAddress + " TEXT,"
            + KEY_CompanyNumber + " TEXT,"
            + KEY_NameOfPerson + " TEXT,"
            + KEY_Email + " TEXT,"
            + KEY_WebSite + " TEXT,"
            + KEY_Password + " TEXT,"
            + KEY_ActiveClient + " TEXT,"
            + KEY_Country + " TEXT,"
            + KEY_City + " TEXT,"
            + KEY_SubCity + " TEXT,"
            + KEY_CapacityOfPersons + " TEXT,"
            + KEY_ClientUserID4 + " TEXT,"
            + KEY_SysCode4 + " TEXT,"
            + KEY_NetCode4 + " TEXT,"
            + KEY_UpdatedDate4 + " TEXT,"
            + KEY_Lat + " TEXT,"
            + KEY_Lng + " TEXT,"
            + KEY_BookingTermsAndCondition + " TEXT,"
            + KEY_ProjectID + " TEXT,"
            + KEY_UserRights4 + " TEXT" + ")";

    // Project table create statement
    private static final String CREATE_TABLE_Project = "CREATE TABLE "
            + TABLE_Project + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ProjectID + " TEXT,"
            + KEY_ProjectName + " TEXT,"
            + KEY_ProjectDescription + " TEXT,"
            + KEY_ProjectType + " TEXT" + ")";

    // ProjectMenu table create statement
    private static final String CREATE_TABLE_ProjectMenu = "CREATE TABLE "
            + TABLE_ProjectMenu + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MenuID + " TEXT,"
            + KEY_ProjectID6 + " TEXT,"
            + KEY_MenuGroup + " TEXT,"
            + KEY_MenuName + " TEXT,"
            + KEY_PageOpen + " TEXT,"
            + KEY_ValuePass + " TEXT,"
            + KEY_ImageName + " TEXT,"
            + KEY_GroupSortBy + " TEXT,"
            + KEY_SortBy + " TEXT" + ")";

    // Account4UserRights table create statement
    private static final String CREATE_TABLE_Account4UserRights = "CREATE TABLE "
            + TABLE_Account4UserRights + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_AcRightsID + " TEXT,"
            + KEY_Account3ID + " TEXT,"
            + KEY_MenuName11 + " TEXT,"
            + KEY_PageOpen11 + " TEXT,"
            + KEY_ValuePass11 + " TEXT,"
            + KEY_ImageName11 + " TEXT,"
            + KEY_Inserting + " TEXT,"
            + KEY_Edting + " TEXT,"
            + KEY_Deleting + " TEXT,"
            + KEY_Reporting + " TEXT,"
            + KEY_ClientID11 + " TEXT,"
            + KEY_ClientUserID11 + " TEXT,"
            + KEY_NetCode11 + " TEXT,"
            + KEY_SysCode11 + " TEXT,"
            + KEY_UpdatedDate11 + " TEXT,"
            + KEY_SortBy11 + " TEXT" + ")";

    // Item1Type table create statement
    private static final String CREATE_TABLE_Item1Type = "CREATE TABLE "
            + TABLE_Item1Type + "(" + refdb.TableItem1.KEY_Item1TypeID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + refdb.TableItem1.KEY_ItemType + " TEXT" + ")";

    // Item2Group table create statement
    private static final String CREATE_TABLE_Item2Group = "CREATE TABLE "
            + TABLE_Item2Group + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_Item2GroupID + " TEXT,"
            + KEY_Item1TID + " TEXT,"
            + KEY_Item2GroupName + " TEXT,"
            + KEY_ClientID6 + " TEXT,"
            + KEY_ClientUserID6 + " TEXT,"
            + KEY_NetCode6 + " TEXT,"
            + KEY_SysCode6 + " TEXT,"
            + KEY_UpdatedDate6 + " TEXT" + ")";

    // Item3Name table create statement


    private static final String CREATE_TABLE_Item3Name = "CREATE TABLE "
            + TABLE_Item3Name + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_Item3NameID + " TEXT,"
            + KEY_Item2GroupID7 + " TEXT,"
            + KEY_ItemName + " TEXT,"
            + KEY_ClientID7 + " TEXT,"
            + KEY_ClientUserID7 + " TEXT,"
            + KEY_NetCode7 + " TEXT,"
            + KEY_SysCode7 + " TEXT,"
            + KEY_SalePrice7 + " TEXT,"
            + KEY_ItemCode7 + " TEXT,"
            + KEY_Stock7 + " TEXT,"
            + KEY_UpdatedDate7 + " TEXT" + ")";

    // SalePur1 table create statement
    private static final String CREATE_TABLE_SalePur1 = "CREATE TABLE "
            + TABLE_SalePur1 + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_SalePur1ID + " TEXT,"
            + KEY_SerialNo8 + " TEXT,"
            + KEY_EntryType8 + " TEXT,"
            + KEY_SPDate + " TEXT,"
            + KEY_AccountID + " TEXT,"
            + KEY_Remarks + " TEXT,"
            + KEY_ClientID8 + " TEXT,"
            + KEY_ClientUserID8 + " TEXT,"
            + KEY_NetCode8 + " TEXT,"
            + KEY_SysCode8 + " TEXT,"
            + KEY_UpdatedDate8 + " TEXT,"
            + KEY_Name + " TEXT,"
            + KEY_DueDate + " TEXT" + ")";

    // SalePur2 table create statement
    private static final String CREATE_TABLE_SalePur2 = "CREATE TABLE "
            + TABLE_SalePur2 + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_SalePur2ID + " TEXT,"
            + KEY_SalePur1ID9 + " TEXT,"
            + KEY_EntryType9 + " TEXT,"
            + KEY_ItemCode + " TEXT,"
            + KEY_ItemDescription + " TEXT,"
            + KEY_QtyAdd + " TEXT,"
            + KEY_QtyLess + " TEXT,"
            + KEY_Qty + " TEXT,"
            + KEY_Price + " TEXT,"
            + KEY_Total + " TEXT,"
            + KEY_Location + " TEXT,"
            + KEY_ClientID9 + " TEXT,"
            + KEY_ClientUserID9 + " TEXT,"
            + KEY_NetCode9 + " TEXT,"
            + KEY_SysCode9 + " TEXT,"
            + KEY_UpdatedDate9 + " TEXT" + ")";

    // SalePurLocation table create statement
    private static final String CREATE_TABLE_SalePurLocation = "CREATE TABLE "
            + TABLE_SalePurLocation + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_LocationID + " TEXT,"
            + KEY_LocationName + " TEXT,"
            + KEY_ClientID10 + " TEXT,"
            + KEY_ClientUserID10 + " TEXT,"
            + KEY_NetCode10 + " TEXT,"
            + KEY_SysCode10 + " TEXT,"
            + KEY_UpdatedDate10 + " TEXT" + ")";

    // ActiveAccounts table create statement
    private static final String CREATE_TABLE_ActiveAccounts = "CREATE TABLE "
            + TABLE_ActiveAccounts + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_AcMobileNo12 + " TEXT,"
            + KEY_UserRights12 + " TEXT,"
            + KEY_AcName12 + " TEXT,"
            + KEY_ClientID12 + " TEXT,"
            + KEY_CompanyName12 + " TEXT,"
            + KEY_CompanyAddress12 + " TEXT,"
            + KEY_ProjectID12 + " TEXT,"
            + KEY_ProjectName12 + " TEXT,"
            + KEY_ClientUserID12 + " TEXT" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_Account3Name);
        db.execSQL(CREATE_TABLE_CashBook);
        db.execSQL(CREATE_TABLE_Booking);
        db.execSQL(CREATE_TABLE_Account2Group);
        db.execSQL(CREATE_TABLE_Account1Type);

        db.execSQL(CREATE_TABLE_Client);
        db.execSQL(CREATE_TABLE_Project);
        db.execSQL(CREATE_TABLE_ProjectMenu);
        db.execSQL(CREATE_TABLE_Account4UserRights);
        db.execSQL(CREATE_TABLE_Item1Type);
        db.execSQL(CREATE_TABLE_Item2Group);
        db.execSQL(CREATE_TABLE_Item3Name);
        db.execSQL(CREATE_TABLE_SalePur1);
        db.execSQL(CREATE_TABLE_SalePur2);
        db.execSQL(CREATE_TABLE_SalePurLocation);
        db.execSQL(CREATE_TABLE_ActiveAccounts);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Account3Name);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CashBook);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Booking);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Account2Group);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Account1Type);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Client);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Project);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ProjectMenu);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Account4UserRights);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Item1Type);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Item2Group);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Item3Name);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SalePur1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SalePur2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SalePurLocation);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ActiveAccounts);

//        if (newVersion > oldVersion) {
//            db.execSQL("ALTER TABLE " + TABLE_CashBook + " ADD COLUMN ID PRIMARY KEY AUTOINCREMENT");
//        }

        // create new tables
        onCreate(db);
    }

    /**
     * Creating a Account3Name
     */
    public void createAccount3Name(Account3Name account3Name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("key", account3Name.getClientID());
        ContentValues values = new ContentValues();
        values.put(KEY_AcNameID, account3Name.getAcNameID());
        values.put(KEY_AcName, account3Name.getAcName());
        values.put(KEY_AcGroupID, account3Name.getAcGroupID());
        values.put(KEY_AcAddress, account3Name.getAcAddress());
        values.put(KEY_AcMobileNo, account3Name.getAcMobileNo());
        values.put(KEY_AcContactNo, account3Name.getAcContactNo());
        values.put(KEY_AcEmailAddress, account3Name.getAcEmailAddress());
        values.put(KEY_AcDebitBal, account3Name.getAcDebitBal());
        values.put(KEY_AcCreditBal, account3Name.getAcCreditBal());
        values.put(KEY_AcPassward, account3Name.getAcPassward());
        values.put(KEY_ClientID, account3Name.getClientID());
        values.put(KEY_ClientUserID, account3Name.getClientUserID());
        values.put(KEY_SysCode, account3Name.getSysCode());
        values.put(KEY_NetCode, account3Name.getNetCode());
        values.put(KEY_UpdatedDate, account3Name.getUpdatedDate());
        values.put(KEY_SerialNo, account3Name.getSerialNo());
        values.put(KEY_UserRights, account3Name.getUserRights());
        values.put(KEY_SecurityRights, account3Name.getSecurityRights());
        values.put(KEY_Salary, account3Name.getSalary());

//////////////////BillaAddition

        // insert row
        long ii = db.insert(TABLE_Account3Name, null, values);


        Log.e("key", "After Inserting data ID " + ii);
//        List<Account3Name> list=getAccount3Name("Select * from Account3Name");
//        Log.e("key","--------------------------------------------"+ii);
//        for (int i = 0; i <list.size() ; i++) {
//            Log.e("key",list.get(i).toString());
//
//        }

    }

    /**
     * Update a Account3Name
     */
    public void updateAccount3Name(String query) {
        Log.e("updated query ", query);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    /**
     * Deleting a Account3Name
     */
    public void deleteAccount3Name() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_Account3Name);
    }

    /**
     * Deleting a Account3Name
     */
    public void deleteAccount3NameEntry(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    /**
     * Creating a CashBook
     */
    public String createCashBook(CashBook cashBook) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CashBookID, cashBook.getCashBookID());
        values.put(KEY_CBDate, cashBook.getCBDate());
        values.put(KEY_DebitAccount, cashBook.getDebitAccount());
        values.put(KEY_CreditAccount, cashBook.getCreditAccount());
        values.put(KEY_CBRemarks, cashBook.getCBRemarks());
        values.put(KEY_Amount, cashBook.getAmount());
        values.put(KEY_ClientID2, cashBook.getClientID());
        values.put(KEY_ClientUserID, cashBook.getCashBookID());
        values.put(KEY_NetCode2, cashBook.getNetCode());
        values.put(KEY_SysCode2, cashBook.getSysCode());
        values.put(KEY_UpdatedDate2, cashBook.getUpdatedDate());
        values.put(KEY_TableID, cashBook.getTableID());
        values.put(KEY_SerialNo2, cashBook.getSerialNo());
        values.put(KEY_TableName, cashBook.getTableName());

        // insert row
        String cid = String.valueOf(db.insert(TABLE_CashBook, null, values));
//        Log.e("OKK",String.valueOf(db.insert(TABLE_CashBook, null, values)));
        return cid;

    }

    /**
     * Deleting a CashBook
     */
    public void deleteCashBook() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CashBook);
    }

    /**
     * Update a CashBook
     */
    public void updateCashBook(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    /**
     * Creating a Booking
     */
    public void createBooking(Bookings bookings) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BookingID3, bookings.getBookingID());
        values.put(KEY_ClientName, bookings.getClientName());
        values.put(KEY_ClientMobile, bookings.getClientMobile());
        values.put(KEY_ClientAddress, bookings.getClientAddress());
        values.put(KEY_ClientNic, bookings.getClientNic());
        values.put(KEY_EventName, bookings.getEventName());
        values.put(KEY_BookingDate, bookings.getBookingDate());
        values.put(KEY_EventDate, bookings.getEventDate());
        values.put(KEY_ArrangePersons, bookings.getArrangePersons());
        values.put(KEY_ChargesTotal, bookings.getChargesTotal());
        values.put(KEY_Description, bookings.getDescription());
        values.put(KEY_ClientID3, bookings.getClientID());
        values.put(KEY_ClientUserID3, bookings.getClientUserID());
        values.put(KEY_NetCode3, bookings.getNetCode());
        values.put(KEY_SysCode3, bookings.getSysCode());
        values.put(KEY_UpdatedDate3, bookings.getUpdatedDate());
        values.put(KEY_Advance, bookings.getAmount());
        values.put(KEY_Shift, bookings.getShift());
        values.put(KEY_SerialNo3, bookings.getSerialNo());

        // insert row
        String s = String.valueOf(db.insert(TABLE_Booking, null, values));
        Log.e("BOOKING", s);
    }

    /**
     * Update a Booking
     */
    public void updateBooking(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    /**
     * Deleting a Booking
     */
    public void deleteBooking() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_Booking);
    }

    /**
     * Creating a Account2Group
     */
    public void createAccount2Group(Account2Group account2Group) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AcGroupID, account2Group.getAcGroupID());
        values.put(KEY_AcTypeID, account2Group.getAcTypeID());
        values.put(KEY_AcGruopName, account2Group.getAcGruopName());

        // insert row
        db.insert(TABLE_Account2Group, null, values);
    }

    /**
     * Deleting a Booking
     */
    public void deleteAccount2Group() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_Account2Group);
    }

    /**
     * Creating a Account1Type
     */
    public void createAccount1Type(Account1Type account1Type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AcTypeID, account1Type.getAcTypeID());
        values.put(KEY_AcTypeName, account1Type.getAcTypeName());

        // insert row
        db.insert(TABLE_Account1Type, null, values);
    }

    /**
     * Deleting a Account1Type
     */
    public void deleteAccount1Type() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_Account1Type);
    }

    //Get GeneralLedger

    public List<GeneralLedger> getGeneralLedger(String query) {
        List<GeneralLedger> mGeneralLedger = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                GeneralLedger generalLedger = new GeneralLedger();
                Log.e("SSSSS", c.getString(c.getColumnIndex("ClientID")));
                generalLedger.setClientID(c.getString(c.getColumnIndex("ClientID")));
                generalLedger.setEntryNo(c.getString(c.getColumnIndex("EntryNo")));
                generalLedger.setDate(c.getString(c.getColumnIndex("Date")));
                generalLedger.setSelectedAc(c.getString(c.getColumnIndex("SelectedAc")));
                generalLedger.setAgainstAc(c.getString(c.getColumnIndex("AgainstAc")));
                generalLedger.setAccountName(c.getString(c.getColumnIndex("AccountName")));
                generalLedger.setParticulars(c.getString(c.getColumnIndex("Particulars")));
                generalLedger.setDebit(c.getString(c.getColumnIndex("Debit")));
                generalLedger.setCredit(c.getString(c.getColumnIndex("Credit")));
                generalLedger.setEntryOf(c.getString(c.getColumnIndex("EntryOf")));
                generalLedger.setBalance(c.getString(c.getColumnIndex("Balance")));

                // adding to todo list
                mGeneralLedger.add(generalLedger);
            } while (c.moveToNext());
        }

        return mGeneralLedger;
    }

    public List<Bookings> getBookings(String query) {
        List<Bookings> mGeneralLedger = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Bookings generalLedger = new Bookings();
                generalLedger.setId(c.getString(c.getColumnIndex(KEY_ID)));
                generalLedger.setBookingID(c.getString(c.getColumnIndex("BookingID")));
                generalLedger.setClientName(c.getString(c.getColumnIndex("ClientName")));
                generalLedger.setClientMobile(c.getString(c.getColumnIndex("ClientMobile")));
                generalLedger.setClientAddress(c.getString(c.getColumnIndex("ClientAddress")));
                generalLedger.setClientNic(c.getString(c.getColumnIndex("ClientNic")));
                generalLedger.setEventName(c.getString(c.getColumnIndex("EventName")));
                generalLedger.setBookingDate(c.getString(c.getColumnIndex("BookingDate")));
                generalLedger.setEventDate(c.getString(c.getColumnIndex("EventDate")));
                Log.e("EVENTDATE", c.getString(c.getColumnIndex("EventDate")));
                generalLedger.setChargesTotal(c.getString(c.getColumnIndex("ChargesTotal")));
                generalLedger.setDescription(c.getString(c.getColumnIndex("Description")));
                generalLedger.setClientID(c.getString(c.getColumnIndex("ClientID")));
                generalLedger.setClientUserID(c.getString(c.getColumnIndex("ClientUserID")));
                generalLedger.setArrangePersons(c.getString(c.getColumnIndex(KEY_ArrangePersons)));
                generalLedger.setAmount(c.getString(c.getColumnIndex(KEY_Advance)));
                generalLedger.setShift(c.getString(c.getColumnIndex(KEY_Shift)));
                generalLedger.setSerialNo(c.getString(c.getColumnIndex(KEY_SerialNo3)));

                // adding to todo list
                mGeneralLedger.add(generalLedger);
            } while (c.moveToNext());
        }

        return mGeneralLedger;
    }

    public List<Recovery> getRecoveries(String query) {
        List<Recovery> mRecoveries = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Recovery generalLedger = new Recovery();
                Log.e("SSSSS", c.getString(c.getColumnIndex("BookingID")));

                generalLedger.setClientID(c.getString(c.getColumnIndex("ClientID")));
                generalLedger.setBookingID(c.getString(c.getColumnIndex("BookingID")));
                generalLedger.setRecieved(c.getString(c.getColumnIndex("Recieved")));
                generalLedger.setExpensed(c.getString(c.getColumnIndex("Expensed")));
                generalLedger.setChargesTotal(c.getString(c.getColumnIndex("ChargesTotal")));
                generalLedger.setBalance(c.getString(c.getColumnIndex("Balance")));
                generalLedger.setProfit(c.getString(c.getColumnIndex("Profit")));
                generalLedger.setEventName(c.getString(c.getColumnIndex("EventName")));
                generalLedger.setEventDate(c.getString(c.getColumnIndex("EventDate")));
                generalLedger.setClientName(c.getString(c.getColumnIndex("ClientName")));
                ;

                // adding to todo list
                mRecoveries.add(generalLedger);
            } while (c.moveToNext());
        }

        return mRecoveries;
    }

    //Get Entry CashBook
    public List<CashBook> getCashBookEntry(String query) {
        List<CashBook> mCashbook = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("CASHBOOKID", String.valueOf(c.getColumnName(0)));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                CashBook cashBook = new CashBook();
//                Log.e("CashBook",c.getString(c.getColumnIndex("ID")));
//                cashBook.setcId(c.getString(c.getColumnIndex("ID")));
                cashBook.setCashBookID(c.getString(c.getColumnIndex("CashBookID")));
                cashBook.setCBDate(c.getString(c.getColumnIndex("CBDate")));
                cashBook.setDebitAccount(c.getString(c.getColumnIndex("DebitAccount")));
                cashBook.setCreditAccount(c.getString(c.getColumnIndex("CreditAccount")));
                cashBook.setCBRemarks(c.getString(c.getColumnIndex("CBRemarks")));
                cashBook.setAmount(c.getString(c.getColumnIndex("Amount")));
                cashBook.setClientID(c.getString(c.getColumnIndex("ClientID")));
                cashBook.setClientUserID(c.getString(c.getColumnIndex("ClientUserID")));
                cashBook.setTableID(c.getString(c.getColumnIndex("TableID")));
                cashBook.setDebitAccountName(c.getString(c.getColumnIndex("DebitAccountName")));
                cashBook.setCreditAccountName(c.getString(c.getColumnIndex("CreditAccountName")));
                cashBook.setUserName(c.getString(c.getColumnIndex("UserName")));
                // adding to todo list
                mCashbook.add(cashBook);
            } while (c.moveToNext());
        }
        return mCashbook;
    }

    //Get Entry Reports
    public List<Reports> getReports(String query) {
        List<Reports> mReports = new ArrayList<>();
        Log.e(LOG, query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS", String.valueOf(c.moveToFirst()));
        Log.e("key","No of Coumns "+c.getColumnCount());
        //looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Reports reports = new Reports();
                reports.SqliteDbID=c.getString(c.getColumnIndex("ID"));
                reports.setAccountID(c.getString(c.getColumnIndex("AccountID")));
                reports.setDebit(c.getString(c.getColumnIndex("Debit")));
                reports.setCredit(c.getString(c.getColumnIndex("Credit")));
                reports.setBal(c.getString(c.getColumnIndex("Bal")));
                reports.setDebitBal(c.getString(c.getColumnIndex("DebitBal")));
                reports.setCreditBal(c.getString(c.getColumnIndex("CreditBal")));
                reports.setAcName(c.getString(c.getColumnIndex("AcName")));
                reports.setAcGroupID(c.getString(c.getColumnIndex("AcGroupID")));
                reports.setUpdatedDate(c.getString(c.getColumnIndex("UpdatedDate")));
                //adding to todo list
                  mReports.add(reports);
            } while (c.moveToNext());
        }

        return mReports;
//        List<Reports> list=new ArrayList<>();
//        for (int i = 0; i <10 ; i++) {
//            list.add(new Reports("113"+i,"23","12","100","90","80","Temp","123"));
//        }
//        return list;
    }

    //Get Entry SummerizeTB
    public List<Summerize> getSummerizeTB(String query) {
        List<Summerize> mSummerize = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Summerize summerize = new Summerize();
                Log.e("SSSSS", c.getString(c.getColumnIndex("AcTypeID")));

                summerize.setAcTypeID(c.getString(c.getColumnIndex("AcTypeID")));
                summerize.setAcTypeName(c.getString(c.getColumnIndex("AcTypeName")));
                summerize.setAcGroupID(c.getString(c.getColumnIndex("AcGroupID")));
                summerize.setAcGruopName(c.getString(c.getColumnIndex("AcGruopName")));
                summerize.setDebit(c.getString(c.getColumnIndex("Debit")));
                summerize.setCredit(c.getString(c.getColumnIndex("Credit")));
                summerize.setClientID(c.getString(c.getColumnIndex("ClientID")));
                summerize.setBal(c.getString(c.getColumnIndex("Bal")));
                summerize.setDebitBL(c.getString(c.getColumnIndex("DebitBL")));
                summerize.setCreditBL(c.getString(c.getColumnIndex("CreditBL")));

                // adding to todo list
                mSummerize.add(summerize);
            } while (c.moveToNext());
        }

        return mSummerize;
    }

    //Get MonthTB
    public List<MonthTb> getMonthTB(String query) {
        List<MonthTb> mMonthTb = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                MonthTb monthTb = new MonthTb();
                Log.e("SSSSS", c.getString(c.getColumnIndex("AcTypeID")));

                monthTb.setClientID(c.getString(c.getColumnIndex("ClientID")));
                monthTb.setAccountID(c.getString(c.getColumnIndex("AccountID")));
                monthTb.setDebit(c.getString(c.getColumnIndex("Debit")));
                monthTb.setCredit(c.getString(c.getColumnIndex("Credit")));
                monthTb.setPrvBal(c.getString(c.getColumnIndex("PrvBal")));
                monthTb.setPrvDebit(c.getString(c.getColumnIndex("PrvDebit")));
                monthTb.setPrvCredit(c.getString(c.getColumnIndex("PrvCredit")));
                monthTb.setTraDebit(c.getString(c.getColumnIndex("TraDebit")));
                monthTb.setTraCredit(c.getString(c.getColumnIndex("TraCredit")));
                monthTb.setTraBalance(c.getString(c.getColumnIndex("TraBalance")));
                monthTb.setClosingBalnce(c.getString(c.getColumnIndex("ClosingBalnce")));
                monthTb.setClosingDebit(c.getString(c.getColumnIndex("ClosingDebit")));
                monthTb.setClosingCredit(c.getString(c.getColumnIndex("ClosingCredit")));
                monthTb.setAcName(c.getString(c.getColumnIndex("AcName")));
                monthTb.setAcGroupID(c.getString(c.getColumnIndex("AcGroupID")));
                monthTb.setAcGruopName(c.getString(c.getColumnIndex("AcGruopName")));
                monthTb.setAcTypeID(c.getString(c.getColumnIndex("AcTypeID")));
                monthTb.setAcTypeName(c.getString(c.getColumnIndex("AcTypeName")));


                // adding to todo list
                mMonthTb.add(monthTb);
            } while (c.moveToNext());
        }

        return mMonthTb;
    }

    //Get Entry Account2Group
    public List<Account2Group> getAccount2Group(String query) {
        List<Account2Group> mAccount2Groups = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Account2Group account2Group = new Account2Group();
                Log.e("SSSSS", c.getString(c.getColumnIndex("AcGroupID")));

                account2Group.setAcGroupID(c.getString(c.getColumnIndex("AcGroupID")));
                account2Group.setAcTypeID(c.getString(c.getColumnIndex("AcTypeID")));
                account2Group.setAcGruopName(c.getString(c.getColumnIndex("AcGruopName")));

                // adding to todo list
                mAccount2Groups.add(account2Group);
            } while (c.moveToNext());
        }

        return mAccount2Groups;
    }

    //Get Entry Account2Group
    public List<Account3Name> getAccount3Name(String query) {
        List<Account3Name> mAccount3Name = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Account3Name account3Name = new Account3Name();

                Log.e("SSSSS", c.getString(c.getColumnIndex(KEY_AcNameID)));

                account3Name.setId(c.getString(c.getColumnIndex(KEY_ID)));
                account3Name.setClientID(c.getString(c.getColumnIndex(KEY_ClientID)));
                account3Name.setUserRights(c.getString(c.getColumnIndex(KEY_UserRights)));
                account3Name.setClientUserID(c.getString(c.getColumnIndex(KEY_ClientUserID)));
                account3Name.setAcNameID(c.getString(c.getColumnIndex(KEY_AcNameID)));
                account3Name.setAcName(c.getString(c.getColumnIndex(KEY_AcName)));
                account3Name.setAcGroupID(c.getString(c.getColumnIndex(KEY_AcGroupID)));
                account3Name.setAcAddress(c.getString(c.getColumnIndex(KEY_AcAddress)));
                account3Name.setAcMobileNo(c.getString(c.getColumnIndex(KEY_AcMobileNo)));
                account3Name.setSysCode(c.getString(c.getColumnIndex(KEY_SysCode)));
                account3Name.setNetCode(c.getString(c.getColumnIndex(KEY_NetCode)));
                account3Name.setUpdatedDate(c.getString(c.getColumnIndex(KEY_UpdatedDate)));

                account3Name.setAcMobileNo(c.getString(c.getColumnIndex(KEY_AcMobileNo)));
                account3Name.setAcContactNo(c.getString(c.getColumnIndex(KEY_AcContactNo)));
                account3Name.setAcEmailAddress(c.getString(c.getColumnIndex(KEY_AcEmailAddress)));
                account3Name.setAcDebitBal(c.getString(c.getColumnIndex(KEY_AcDebitBal)));
                account3Name.setAcCreditBal(c.getString(c.getColumnIndex(KEY_AcCreditBal)));
                account3Name.setSalary(c.getString(c.getColumnIndex(KEY_Salary)));
                account3Name.setAcPassward(c.getString(c.getColumnIndex(KEY_AcPassward)));
                account3Name.setSecurityRights(c.getString(c.getColumnIndex(KEY_SecurityRights)));
                account3Name.setSerialNo(c.getString(c.getColumnIndex(KEY_SerialNo)));

                // adding to todo list
                mAccount3Name.add(account3Name);
            } while (c.moveToNext());
        }

        return mAccount3Name;
    }

    //Get Entry Account2Group
    public Boolean findAccount3Name(String query) {

        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list

        if (c.moveToFirst()) {
            Log.e("key", "CCCCLUMN:" + c.getString(0) + ":");

            if (c.getString(0).isEmpty())
                return false;
            else return true;
        }

        return false;
    }

    ////////////////////GEt the Maximum Date from Account3NameTable
    public String getAccount3NameMaxUpdatedDate(String clientIDSession) {

        String query = "Select MAX(UpdatedDate) from Account3Name where ClientID =" + clientIDSession;

        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS", String.valueOf(c.moveToFirst()));

        String maxDate = c.getString(0);
        Log.e("key", "Account3Name Last UPDATED Date " + c.getString(0));

        return maxDate;
    }


    //Get Entry Account1Type
    public List<Account1Type> getAccount1Type(String query) {
        List<Account1Type> mAccount1Type = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Account1Type account1Type = new Account1Type();
                Log.e("SSSSS", c.getString(c.getColumnIndex("AcTypeID")));

                account1Type.setAcTypeID(c.getString(c.getColumnIndex("AcTypeID")));
                account1Type.setAcTypeName(c.getString(c.getColumnIndex("AcTypeName")));

                // adding to todo list
                mAccount1Type.add(account1Type);
            } while (c.moveToNext());
        }

        return mAccount1Type;
    }

    //Get Entry ProfitLoss
    public List<ProfitLoss> getProfitLoss(String query) {
        List<ProfitLoss> mProfitLoss = new ArrayList<>();


        Log.e("Query", query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ProfitLoss profitLoss = new ProfitLoss();
                Log.e("SSSSS", c.getString(c.getColumnIndex("CBDate")));

                profitLoss.setClientID(c.getString(c.getColumnIndex("ClientID")));
                profitLoss.setCBDate(c.getString(c.getColumnIndex("CBDate")));
                profitLoss.setIncome(c.getString(c.getColumnIndex("Income")));
                profitLoss.setExpense(c.getString(c.getColumnIndex("Expense")));
                profitLoss.setProfit(c.getString(c.getColumnIndex("Profit")));

                // adding to todo list
                mProfitLoss.add(profitLoss);
            } while (c.moveToNext());
        }

        return mProfitLoss;
    }

    //Get Entry BalSheet
    public List<BalSheet> getBalSheet(String query) {
        List<BalSheet> mBalSheet = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                BalSheet balSheet = new BalSheet();
                Log.e("SSSSS", c.getString(c.getColumnIndex("CBDate")));

                balSheet.setCBDate(c.getString(c.getColumnIndex("CBDate")));
                balSheet.setCapital(c.getString(c.getColumnIndex("Capital")));
                balSheet.setProfitLoss(c.getString(c.getColumnIndex("ProfitLoss")));
                balSheet.setLiabilities(c.getString(c.getColumnIndex("Liabilities")));
                balSheet.setC_P_L(c.getString(c.getColumnIndex("C + P + L")));
                balSheet.setAssets(c.getString(c.getColumnIndex("Assets")));
                balSheet.setClientID(c.getString(c.getColumnIndex("ClientID")));

                // adding to todo list
                mBalSheet.add(balSheet);
            } while (c.moveToNext());
        }

        return mBalSheet;
    }

    //Get Entry ChartofAccount
    public List<ChartOfAcc> getChartofAccount(String query) {
        List<ChartOfAcc> mChartOfAcc = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ChartOfAcc chartOfAcc = new ChartOfAcc();
                Log.e("SSSSS", c.getString(c.getColumnIndex("AcTypeID")));

                chartOfAcc.setAcTypeID(c.getString(c.getColumnIndex("AcTypeID")));
                chartOfAcc.setAcTypeName(c.getString(c.getColumnIndex("AcTypeName")));
                chartOfAcc.setAcGroupID(c.getString(c.getColumnIndex("AcGroupID")));
                chartOfAcc.setAcGruopName(c.getString(c.getColumnIndex("AcGruopName")));
                chartOfAcc.setAccountID(c.getString(c.getColumnIndex("AccountID")));
                chartOfAcc.setAcName(c.getString(c.getColumnIndex("AcName")));
                chartOfAcc.setDebit(c.getString(c.getColumnIndex("Debit")));
                chartOfAcc.setCredit(c.getString(c.getColumnIndex("Credit")));
                chartOfAcc.setClientID(c.getString(c.getColumnIndex("ClientID")));
                chartOfAcc.setBal(c.getString(c.getColumnIndex("Bal")));
                chartOfAcc.setDebitBL(c.getString(c.getColumnIndex("DebitBL")));
                chartOfAcc.setCreditBL(c.getString(c.getColumnIndex("CreditBL")));
                chartOfAcc.setMaxDate(c.getString(c.getColumnIndex("MaxDate")));

                // adding to todo list
                mChartOfAcc.add(chartOfAcc);
            } while (c.moveToNext());
        }

        return mChartOfAcc;
    }

    //Get Entry CashBook
    public List<CashBook> getCashBook(String query) {
        List<CashBook> cashBooks = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("CashBook", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                CashBook cashBook = new CashBook();
                Log.e("CashBookIDDD", c.getString(c.getColumnIndex(KEY_ID)));

                cashBook.setcId(c.getString(c.getColumnIndex(KEY_ID)));
                cashBook.setCashBookID(c.getString(c.getColumnIndex(KEY_CashBookID)));
                cashBook.setCBDate(c.getString(c.getColumnIndex(KEY_CBDate)));
                cashBook.setDebitAccount(c.getString(c.getColumnIndex(KEY_DebitAccount)));
                cashBook.setCreditAccount(c.getString(c.getColumnIndex(KEY_CreditAccount)));
                cashBook.setCBRemarks(c.getString(c.getColumnIndex(KEY_CBRemarks)));
                cashBook.setAmount(c.getString(c.getColumnIndex(KEY_Amount)));
                cashBook.setClientID(c.getString(c.getColumnIndex(KEY_ClientID2)));
                cashBook.setClientUserID(c.getString(c.getColumnIndex(KEY_ClientUserID2)));
                cashBook.setNetCode(c.getString(c.getColumnIndex(KEY_NetCode2)));
                cashBook.setSysCode(c.getString(c.getColumnIndex(KEY_SysCode2)));
                cashBook.setUpdatedDate(c.getString(c.getColumnIndex(KEY_UpdatedDate2)));
                cashBook.setTableID(c.getString(c.getColumnIndex(KEY_TableID)));
                cashBook.setSerialNo(c.getString(c.getColumnIndex(KEY_SerialNo2)));
                cashBook.setTableName(c.getString(c.getColumnIndex(KEY_TableName)));

                // adding to todo list
                cashBooks.add(cashBook);
            } while (c.moveToNext());
        }

        return cashBooks;
    }

    public String getAccountName(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        String name = "";

        if (c.moveToFirst()) {
            do {
//                cashBook.setcId(c.getString(c.getColumnIndex(KEY_ID)));
                name = c.getString(c.getColumnIndex("AcName"));

            } while (c.moveToNext());
        }
        return name;
    }

    public List<Spinner> getSpinnerAcName(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        List<Spinner> spinners = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                Spinner spinner = new Spinner();
//                cashBook.setcId(c.getString(c.getColumnIndex(KEY_ID)));
                spinner.setName(c.getString(c.getColumnIndex("AcName")));
                spinner.setAcId(c.getString(c.getColumnIndex("AcNameID")));

                spinners.add(spinner);
            } while (c.moveToNext());
        }
        return spinners;
    }

    //Get Voucher
    public List<Voucher> getPrintValues(String query) {
        List<Voucher> vouchers = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("CashBook", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Voucher voucher = new Voucher();
                Log.e("CashBookIDDD", c.getString(c.getColumnIndex("CashBookID")));

                voucher.setCashBookID(c.getString(c.getColumnIndex("CashBookID")));
                voucher.setCBDate(c.getString(c.getColumnIndex("CBDate")));
                voucher.setCBRemarks(c.getString(c.getColumnIndex("CBRemarks")));
                voucher.setDebitAccount(c.getString(c.getColumnIndex("DebitAccount")));
                voucher.setDebiterName(c.getString(c.getColumnIndex("DebiterName")));
                voucher.setDebiterAddress(c.getString(c.getColumnIndex("DebiterAddress")));
                voucher.setDebiterContactNo(c.getString(c.getColumnIndex("DebiterContactNo")));
                voucher.setCreditAccount(c.getString(c.getColumnIndex("CreditAccount")));
                voucher.setCrediterName(c.getString(c.getColumnIndex("CrediterName")));
                voucher.setCrediterAddress(c.getString(c.getColumnIndex("CrediterAddress")));
                voucher.setCrediterContactNo(c.getString(c.getColumnIndex("CrediterContactNo")));
                voucher.setClientUserID(c.getString(c.getColumnIndex("ClientUserID")));
                voucher.setPreparedBy(c.getString(c.getColumnIndex("PreparedBy")));
                voucher.setAmount(c.getString(c.getColumnIndex("Amount")));
                voucher.setClientID(c.getString(c.getColumnIndex("ClientID")));


                // adding to todo list
                vouchers.add(voucher);
            } while (c.moveToNext());
        }

        return vouchers;
    }

    //Get Voucher
    public Boolean getBookingShift(String query) {

        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("CashBook", String.valueOf(c.moveToFirst()));

        return c.moveToFirst();
    }

    /**
     * Creating a Project
     */
    public void createProjects(Projects projects) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ProjectID, projects.getProjectID());
        values.put(KEY_ProjectName, projects.getProjectName());
        values.put(KEY_ProjectType, projects.getProjectType());
        Log.e("Values", values.toString());
        db.insert(TABLE_Project, null, values);
        // insert row
//        String cid = String.valueOf(db.insert(TABLE_CashBook, null, values));
//        Log.e("OKK",String.valueOf(db.insert(TABLE_CashBook, null, values)));
    }

    //Get Voucher
    public List<Projects> getProjects(String query) {
        List<Projects> projects = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("Projects", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Projects project = new Projects();
                Log.e("ProjectIDDD", c.getString(c.getColumnIndex(KEY_ProjectID)));

                project.setProjectID(c.getString(c.getColumnIndex(KEY_ProjectID)));
                project.setProjectName(c.getString(c.getColumnIndex(KEY_ProjectName)));
                project.setProjectType(c.getString(c.getColumnIndex(KEY_ProjectType)));


                // adding to todo list
                projects.add(project);
            } while (c.moveToNext());
        }

        return projects;
    }

    /**
     * Deleting a Project
     */
    public void deleteProject() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_Project);
    }

    /**
     * Creating a ActiveAccounts
     */
    public void createActiveAccounts(ActiveClients client) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AcMobileNo12, client.getAcMobileNo());
        values.put(KEY_UserRights12, client.getUserRights());
        values.put(KEY_AcName12, client.getAcName());
        values.put(KEY_ClientID12, client.getClientID());
        values.put(KEY_CompanyName12, client.getCompanyName());
        values.put(KEY_CompanyAddress12, client.getCompanyAddress());
        values.put(KEY_ProjectID12, client.getProjectID());
        values.put(KEY_ProjectName, client.getProjectName());
        values.put(KEY_ClientUserID12, client.getClientUserID());

        // insert row
        db.insert(TABLE_ActiveAccounts, null, values);

    }

    //Get ActiveAccounts
    public List<ActiveClients> getActiveAccounts(String query) {
        List<ActiveClients> clients = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("CashBook", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ActiveClients client = new ActiveClients();

                client.setAcMobileNo(c.getString(c.getColumnIndex(KEY_AcMobileNo12)));
                client.setUserRights(c.getString(c.getColumnIndex(KEY_UserRights12)));
                client.setAcName(c.getString(c.getColumnIndex(KEY_AcName12)));
                client.setClientID(c.getString(c.getColumnIndex(KEY_ClientID12)));
                client.setCompanyName(c.getString(c.getColumnIndex(KEY_CompanyName12)));
                client.setCompanyAddress(c.getString(c.getColumnIndex(KEY_CompanyAddress12)));
                client.setProjectID(c.getString(c.getColumnIndex(KEY_ProjectID12)));
                client.setProjectName(c.getString(c.getColumnIndex(KEY_ProjectName)));
                client.setClientUserID(c.getString(c.getColumnIndex(KEY_ClientUserID12)));


                // adding to todo list
                clients.add(client);
            } while (c.moveToNext());
        }

        return clients;
    }

    /**
     * Deleting a Project
     */
    public void deleteActiveAccounts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ActiveAccounts + " WHERE EXISTS (SELECT * FROM ActiveAccounts)");
    }

    /**
     * Creating a Client
     */
    public void createClient(Client client) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ClientID4, client.getClientID());
        values.put(KEY_ClientParentID, client.getClientParentID());
        values.put(KEY_EntryType, client.getEntryType());
        values.put(KEY_LoginMobileNo, client.getLoginMobileNo());
        values.put(KEY_CompanyName, client.getCompanyName());
        values.put(KEY_CompanyAddress, client.getCompanyAddress());
        values.put(KEY_CompanyNumber, client.getCompanyNumber());
        values.put(KEY_NameOfPerson, client.getNameOfPerson());
        values.put(KEY_Email, client.getEmail());
        values.put(KEY_WebSite, client.getWebSite());
        values.put(KEY_Password, client.getPassword());
        values.put(KEY_ActiveClient, client.getActiveClient());
        values.put(KEY_Country, client.getCountry());
        values.put(KEY_City, client.getCity());
        values.put(KEY_SubCity, client.getSubCity());
        values.put(KEY_CapacityOfPersons, client.getCapacityOfPersons());
        values.put(KEY_ClientUserID, client.getClientUserID());
        values.put(KEY_SysCode4, client.getSysCode());
        values.put(KEY_NetCode4, client.getNetCode());
        values.put(KEY_UpdatedDate4, client.getUpdatedDate());
        values.put(KEY_Lat, client.getLat());
        values.put(KEY_Lng, client.getLng());
        values.put(KEY_ProjectID, client.getProjectID());

        // insert row
        db.insert(TABLE_Client, null, values);
    }

    //Get Client
    public List<Client> getClient(String query) {
        List<Client> clients = new ArrayList<>();

        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("CashBook", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Client client = new Client();

                client.setClientID(c.getString(c.getColumnIndex(KEY_ClientID4)));
                client.setClientParentID(c.getString(c.getColumnIndex(KEY_ClientParentID)));
                client.setEntryType(c.getString(c.getColumnIndex(KEY_EntryType)));
                client.setLoginMobileNo(c.getString(c.getColumnIndex(KEY_LoginMobileNo)));
                client.setCompanyName(c.getString(c.getColumnIndex(KEY_CompanyName)));
                client.setCompanyAddress(c.getString(c.getColumnIndex(KEY_CompanyAddress)));
                client.setCompanyNumber(c.getString(c.getColumnIndex(KEY_CompanyNumber)));
                client.setNameOfPerson(c.getString(c.getColumnIndex(KEY_NameOfPerson)));
                client.setEmail(c.getString(c.getColumnIndex(KEY_Email)));
                client.setWebSite(c.getString(c.getColumnIndex(KEY_WebSite)));
                client.setPassword(c.getString(c.getColumnIndex(KEY_Password)));
                client.setActiveClient(c.getString(c.getColumnIndex(KEY_ActiveClient)));
                client.setCountry(c.getString(c.getColumnIndex(KEY_Country)));
                client.setCity(c.getString(c.getColumnIndex(KEY_City)));
                client.setSubCity(c.getString(c.getColumnIndex(KEY_SubCity)));
                client.setCapacityOfPersons(c.getString(c.getColumnIndex(KEY_CapacityOfPersons)));
                client.setClientUserID(c.getString(c.getColumnIndex(KEY_ClientUserID)));
                client.setSysCode(c.getString(c.getColumnIndex(KEY_SysCode4)));
                client.setNetCode(c.getString(c.getColumnIndex(KEY_NetCode4)));
                client.setUpdatedDate(c.getString(c.getColumnIndex(KEY_UpdatedDate4)));
                client.setLat(c.getString(c.getColumnIndex(KEY_Lat)));
                client.setLng(c.getString(c.getColumnIndex(KEY_Lng)));
                client.setProjectID(c.getString(c.getColumnIndex(KEY_ProjectID)));


                // adding to todo list
                clients.add(client);
            } while (c.moveToNext());
        }

        return clients;
    }

    /**
     * Update a Client
     */
    public void updateClient(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    /**
     * Creating a ProjectMenu
     */
    public void createProjectMenu(ProjectMenu projectMenu) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MenuID, projectMenu.getMenuID());
        values.put(KEY_ProjectID6, projectMenu.getProjectID());
        values.put(KEY_MenuGroup, projectMenu.getMenuGroup());
        values.put(KEY_MenuName, projectMenu.getMenuName());
        values.put(KEY_PageOpen, projectMenu.getPageOpen());
        values.put(KEY_ValuePass, projectMenu.getValuePass());
        values.put(KEY_ImageName, projectMenu.getImageName());
        values.put(KEY_GroupSortBy, projectMenu.getGroupSortBy());
        values.put(KEY_SortBy, projectMenu.getSortBy());

        // insert row
        db.insert(TABLE_ProjectMenu, null, values);
    }

    //Get ProjectMenu
    public List<ProjectMenu> getProjectMenu(String query) {
        List<ProjectMenu> projectMenus = new ArrayList<>();

        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("CashBook", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ProjectMenu projectMenu = new ProjectMenu();

                projectMenu.setMenuID(c.getString(c.getColumnIndex(KEY_MenuID)));
                projectMenu.setProjectID(c.getString(c.getColumnIndex(KEY_ProjectID6)));
                projectMenu.setMenuGroup(c.getString(c.getColumnIndex(KEY_MenuGroup)));
                projectMenu.setMenuName(c.getString(c.getColumnIndex(KEY_MenuName)));
                projectMenu.setPageOpen(c.getString(c.getColumnIndex(KEY_PageOpen)));
                projectMenu.setValuePass(c.getString(c.getColumnIndex(KEY_ValuePass)));
                projectMenu.setImageName(c.getString(c.getColumnIndex(KEY_ImageName)));
                projectMenu.setGroupSortBy(c.getString(c.getColumnIndex(KEY_GroupSortBy)));
                projectMenu.setSortBy(c.getString(c.getColumnIndex(KEY_SortBy)));

                // adding to todo list
                projectMenus.add(projectMenu);
            } while (c.moveToNext());
        }

        return projectMenus;
    }

    /**
     * Deleting ProjectMenu
     */
    public void deleteProjectMenu() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ProjectMenu + " WHERE EXISTS (SELECT * FROM ActiveAccounts)");
    }

    //Get MaxValue
    public int getMaxValue(String query) {
        int maxID = 0;

        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                maxID = c.getInt(0);
                Log.e("MAXIDDD", String.valueOf(maxID));

            } while (c.moveToNext());
        }

        return maxID;
    }

    public String getID(String query) {
        String id = "0";

        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                id = c.getString(0);

            } while (c.moveToNext());
        }

        return id;
    }

    public String getClientUpdatedDate(String id) {
        String date = "0";
        String query = "SELECT UpdatedDate FROM Client WHERE ClientID = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                date = c.getString(0);

            } while (c.moveToNext());
        }

        return date;
    }

    public List<Item1Type> getItem1TypeData(String query)
    {
        List<Item1Type> item1TypeList = new ArrayList<>();
        Log.e(LOG, query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("Item 1 Type", String.valueOf(c.moveToFirst()));
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Item1Type item1Type = new Item1Type();
               item1Type.setItem1TypeID(c.getString(c.getColumnIndex(refdb.TableItem1.KEY_Item1TypeID)));
              item1Type.setItemType(c.getString(c.getColumnIndex(refdb.TableItem1.KEY_Item1TypeID)));
                item1TypeList.add(item1Type);
            } while (c.moveToNext());
        }

        return item1TypeList;
    }

    public long createItem1Typeitem(Item1Type item1Type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(refdb.TableItem1.KEY_Item1TypeID, item1Type.getItem1TypeID());
        values.put(refdb.TableItem1.KEY_ItemType, item1Type.getItemType());
        // insert row
        return db.insert(TABLE_Item1Type, null, values);
    }

    ////////////////item 2 Group DAta Functions
    public long createItem2GroupData(Item2Group item2Group){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(refdb.Table2Group.KEY_1, item2Group.getItem2GroupID());
        values.put(refdb.Table2Group.KEY_2, item2Group.getItem1TypeID());
        values.put(refdb.Table2Group.KEY_3, item2Group.getItem2GroupName());
        values.put(refdb.Table2Group.KEY_4, item2Group.getClientID());
        values.put(refdb.Table2Group.KEY_5, item2Group.getClientUserID());
        values.put(refdb.Table2Group.KEY_6, item2Group.getNetCode());
        values.put(refdb.Table2Group.KEY_7, item2Group.getSysCode());
        values.put(refdb.Table2Group.KEY_8, item2Group.getUpdatedDate().toString());

        // insert row
        return db.insert(refdb.Table2Group.TABLE_Item2Group, null, values);
    }
    ////////////////item 3 Name DAta Functions
    public long createItem3NameData(Item3Name_ item3Name_){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(refdb.Table3Name.KEY_1, item3Name_.getItem3NameID());
        values.put(refdb.Table3Name.KEY_2, item3Name_.getItem2GroupID());
        values.put(refdb.Table3Name.KEY_3, item3Name_.getItemName());
        values.put(refdb.Table3Name.KEY_4, item3Name_.getClientID());
        values.put(refdb.Table3Name.KEY_5, item3Name_.getClientUserID());
        values.put(refdb.Table3Name.KEY_6, item3Name_.getNetCode());
        values.put(refdb.Table3Name.KEY_7, item3Name_.getSysCode());
        values.put(refdb.Table3Name.KEY_8, item3Name_.getSalePrice());
        values.put(refdb.Table3Name.KEY_9, item3Name_.getItemCode());
        values.put(refdb.Table3Name.KEY_10, item3Name_.getStock());
        values.put(refdb.Table3Name.KEY_11, item3Name_.getUpdatedDate().getDate());

        // insert row
        return db.insert(refdb.Table2Group.TABLE_Item2Group, null, values);
    }


}
