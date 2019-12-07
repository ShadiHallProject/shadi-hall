package org.by9steps.shadihall.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import org.by9steps.shadihall.model.Account1Type;
import org.by9steps.shadihall.model.Account2Group;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.Account5customGroup1;
import org.by9steps.shadihall.model.Account5customGroup2;
import org.by9steps.shadihall.model.ActiveClients;
import org.by9steps.shadihall.model.BalSheet;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.ChartOfAcc;
import org.by9steps.shadihall.model.Client;
import org.by9steps.shadihall.model.GeneralLedger;
import org.by9steps.shadihall.model.Item1Type;
import org.by9steps.shadihall.model.Item2Group;
import org.by9steps.shadihall.model.ItemLedger;
import org.by9steps.shadihall.model.JoinQueryAccount3Name;
import org.by9steps.shadihall.model.JoinQueryAddVehicle;
import org.by9steps.shadihall.model.JoinQueryDaliyEntryPage1;
import org.by9steps.shadihall.model.ModelForSalePur1page2;
import org.by9steps.shadihall.model.MonthTb;
import org.by9steps.shadihall.model.ProfitLoss;
import org.by9steps.shadihall.model.ProjectMenu;
import org.by9steps.shadihall.model.Projects;
import org.by9steps.shadihall.model.Recovery;
import org.by9steps.shadihall.model.Reports;
import org.by9steps.shadihall.model.Restaurant1Potion;
import org.by9steps.shadihall.model.Restaurant2Table;
import org.by9steps.shadihall.model.Spinner;
import org.by9steps.shadihall.model.Summerize;
import org.by9steps.shadihall.model.Vehicle1Group;
import org.by9steps.shadihall.model.Vehicle2Name;
import org.by9steps.shadihall.model.Vehicle3Booking;
import org.by9steps.shadihall.model.Voucher;
import org.by9steps.shadihall.model.item3name.Item3Name_;
import org.by9steps.shadihall.model.item3name.UpdatedDate;
import org.by9steps.shadihall.model.joinQueryCashCollection;
import org.by9steps.shadihall.model.joinQueryForResturent;
import org.by9steps.shadihall.model.joinQueryForResturentAddOrder;
import org.by9steps.shadihall.model.salepur1data.SPDate;
import org.by9steps.shadihall.model.salepur1data.Salepur1;
import org.by9steps.shadihall.model.salepur2data.SalePur2;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;


public class DatabaseHelper extends SQLiteAssetHelper {

    private SQLiteDatabase db;

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "EasySoftDataFile.db";

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

    private static final String TABLE_Vehicle1Group = "Vehicle1Group";
    private static final String TABLE_Vehicle2Name = "Vehicle2Name";
    private static final String TABLE_Vehicle3Booking = "Vehicle3Booking";

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

    // SalePur1ID---
    // EntryType---
    // SPDate----
    // AcNameID
    // Remarks---
    // ClientID--
    // ClientUserID---
    // NetCode-----
    // SysCode----
    // UpdatedDate---
    // NameOfPerson----
    // PayAfterDay-----

    //SalePur1 Table - column names
    private static final String KEY_SalePur1ID = "SalePur1ID";
    //private static final String KEY_SerialNo8 = "SerialNo";
    private static final String KEY_EntryType8 = "EntryType";
    private static final String KEY_SPDate = "SPDate";
    private static final String KEY_AccountID = "AcNameID";
    private static final String KEY_Remarks = "Remarks";
    private static final String KEY_ClientID8 = "ClientID";
    private static final String KEY_ClientUserID8 = "ClientUserID";
    private static final String KEY_NetCode8 = "NetCode";
    private static final String KEY_SysCode8 = "SysCode";
    private static final String KEY_UpdatedDate8 = "UpdatedDate";
    private static final String KEY_Name = "NameOfPerson";
    private static final String KEY_DueDate = "PayAfterDay";

    // SalePur1ID
// ItemSerial---
// EntryType---
// Item3NameID
// ItemDescription
// QtyAdd
// QtyLess
// Qty
// Price
// Total
// Location
// ClientID
// ClientUserID
// NetCode
// SysCode
// UpdatedDate
    // -----------------------------
    //SalePur2 Table - column names
    private static final String KEY_Item3NameIDD = "Item3NameID";
    private static final String KEY_SalePur1ID9 = "SalePur1ID";
    private static final String KEY_EntryType9 = "EntryType";///
    private static final String KEY_ItemCode = "ItemSerial";//
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

    //Vehicle1Group
    private static final String KEY_VehicleGroupID="VehicleGroupID";
    private static final String KEY_VehicleGroupName="VehicleGroupName";

    //Vehicle2Name
    private static final String KEY_VehicleID = "VehicleID";
    private static final String KEY_VehicleGroupID13 = "VehicleGroupID";
    private static final String KEY_VehicleName = "VehicleName";///
    private static final String KEY_Brand = "Brand";
    private static final String KEY_Model = "Model";//
    private static final String KEY_Colour = "Colour";
    private static final String KEY_RegistrationNo = "RegistrationNo";
    private static final String KEY_Account3ID13 = "Account3ID";
    private static final String KEY_Status = "Status";
    private static final String KEY_Lng13 = "Lng";
    private static final String KEY_Lat13 = "Lat";
    private static final String KEY_ContactNo = "ContactNo";
    private static final String KEY_SerialNo13 = "SerialNo";
    private static final String KEY_ClientID13 = "ClientID";
    private static final String KEY_ClientUserID13 = "ClientUserID";
    private static final String KEY_UpdatedDate13 = "UpdatedDate";
    private static final String KEY_NetCode13 = "NetCode";
    private static final String KEY_SysCode13 = "SysCode";

    //Vehicle3Booking

    private static final String KEY_BookingID = "BookingID";
    private static final String KEY_VehicleID14 = "VehicleID";
    private static final String KEY_BookingDate14 = "BookingDate";
    private static final String KEY_BookingDetail = "BookingDetail";
    private static final String KEY_BookingCharges = "BookingCharges";
    private static final String KEY_SerialNo14 = "SerialNo";
    private static final String KEY_ClientID14 = "ClientID";
    private static final String KEY_ClientUserID14 = "ClientUserID";
    private static final String KEY_UpdatedDate14 = "UpdatedDate";
    private static final String KEY_NetCode14 = "NetCode";
    private static final String KEY_SysCode14 = "SysCode";

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
            + TABLE_Item1Type + "(" + refdb.TableItem1.KEY_Item1TypeID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
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
            + TABLE_SalePur1 + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_SalePur1ID + " TEXT,"
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
            + TABLE_SalePur2 + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_Item3NameIDD + " TEXT,"
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

//    @Override
//    public void onCreate(SQLiteDatabase db) {
//
//        // creating required tables
//        db.execSQL(CREATE_TABLE_Account3Name);
//        db.execSQL(CREATE_TABLE_CashBook);
//        db.execSQL(CREATE_TABLE_Booking);
//        db.execSQL(CREATE_TABLE_Account2Group);
//        db.execSQL(CREATE_TABLE_Account1Type);
//
//        db.execSQL(CREATE_TABLE_Client);
//        db.execSQL(CREATE_TABLE_Project);
//        db.execSQL(CREATE_TABLE_ProjectMenu);
//        db.execSQL(CREATE_TABLE_Account4UserRights);
//        db.execSQL(CREATE_TABLE_Item1Type);
//        db.execSQL(CREATE_TABLE_Item2Group);
//        db.execSQL(CREATE_TABLE_Item3Name);
//        db.execSQL(CREATE_TABLE_SalePur1);
//        db.execSQL(CREATE_TABLE_SalePur2);
//        db.execSQL(CREATE_TABLE_SalePurLocation);
//        db.execSQL(CREATE_TABLE_ActiveAccounts);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // on upgrade drop older tables
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Account3Name);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CashBook);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Booking);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Account2Group);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Account1Type);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Client);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Project);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ProjectMenu);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Account4UserRights);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Item1Type);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Item2Group);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Item3Name);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SalePur1);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SalePur2);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SalePurLocation);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ActiveAccounts);
//
////        if (newVersion > oldVersion) {
////            db.execSQL("ALTER TABLE " + TABLE_CashBook + " ADD COLUMN ID PRIMARY KEY AUTOINCREMENT");
////        }
//
//        // create new tables
//        onCreate(db);
//    }

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
     * create Vehicle1Group
     */
    public void createVehicle1Group(Vehicle1Group vehicle1Group)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(KEY_VehicleGroupID,vehicle1Group.getVehicleGroupID());
        cv.put(KEY_VehicleGroupName,vehicle1Group.getVehicleGroupName());
        long status=database.insert(TABLE_Vehicle1Group,null,cv);
        Log.e("status", "createVehicle1Group: "+status);

    }

    //updating Vehicle1Group
    public void updateVehicle1Group(){

    }

    //deleting Vehicle1Group
    public void deleteVehicle1Group(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_Vehicle1Group);
    }

    public void deleteVehicle1GroupEntry(){

    }
    public String getAcNameAccount3Name(String q){
        String name=null;
//        SQLiteDatabase db=this.getReadableDatabase();
//        Cursor cursor=db.rawQuery(q,null);
//        if(cursor.moveToNext()){
//           name=cursor.getString(cursor.getColumnIndex("AcName"));
//        }
        return name;
    }

    public List<Vehicle1Group> getVehicle1Group(String query) {
        List<Vehicle1Group> vehicle1GroupsList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToNext()){
            do{
                Vehicle1Group vehicle1Group=new Vehicle1Group();
                vehicle1Group.setVehicleGroupID(cursor.getString(0));
                vehicle1Group.setVehicleGroupName(cursor.getString(1));
                vehicle1GroupsList.add(vehicle1Group);
            }while (cursor.moveToNext());
        }
        return vehicle1GroupsList;
    }

    public List<Restaurant1Potion> getRestaurant1Potion(String query) {
        List<Restaurant1Potion> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToNext()){
            do{
                Restaurant1Potion restaurant1Potion=new Restaurant1Potion();
                restaurant1Potion.setID(cursor.getInt(cursor.getColumnIndex("ID")));
                restaurant1Potion.setPotionID(cursor.getString(cursor.getColumnIndex("PotionID")));
                restaurant1Potion.setPotionName(cursor.getString(cursor.getColumnIndex("PotionName")));
                restaurant1Potion.setPotionDescriptions(cursor.getString(cursor.getColumnIndex("PotioDescriptions")));
                restaurant1Potion.setClientID(cursor.getString(cursor.getColumnIndex("ClientID")));
                restaurant1Potion.setClientUserID(cursor.getString(cursor.getColumnIndex("ClientUserID")));
                restaurant1Potion.setNetCode(cursor.getString(cursor.getColumnIndex("NetCode")));
                restaurant1Potion.setSysCode(cursor.getString(cursor.getColumnIndex("SysCode")));
                restaurant1Potion.setUpdatedDate(cursor.getString(cursor.getColumnIndex("UpdatedDate")));
                list.add(restaurant1Potion);
            }while (cursor.moveToNext());
        }
        return list;
    }

    public String getVehicle1GroupSpinnerVehicleGroupName(String query) {

        Log.e("TAG", "getVehicle1GroupSpinnerVehicleGroupName: "+query );

        String Value=null;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToNext()){
            do{
                Value=cursor.getString(0);
                Log.e("Tag", "getVehicle1GroupSpinnerVehicleGroupName: value of data "  +Value  );
            }while (cursor.moveToNext());
        }

        // Value=cursor.getString(0);
        return Value;
    }


    public long createRestaurant1Potion(Restaurant1Potion restaurant1Potion){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("PotionID",restaurant1Potion.getPotionID());
        contentValues.put("PotionName",restaurant1Potion.getPotionName());
        contentValues.put("PotioDescriptions",restaurant1Potion.getPotionDescriptions());
        contentValues.put("ClientID",restaurant1Potion.getClientID());
        contentValues.put("ClientUserID",restaurant1Potion.getClientUserID());
        contentValues.put("NetCode",restaurant1Potion.getNetCode());
        contentValues.put("SysCode",restaurant1Potion.getSysCode());
        contentValues.put("UpdatedDate",restaurant1Potion.getUpdatedDate());
        long abc=database.insert("Restaurant1Potion",null,contentValues);
        return abc;

    }

    /**
     * create Vehicle2Name
     */
    public long createVehicle2Name(Vehicle2Name vehicle2Name)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(KEY_VehicleID , vehicle2Name.getVehicleID());
        cv.put(KEY_VehicleGroupID13,vehicle2Name.getVehicleGroupID());
        cv.put(KEY_VehicleName ,vehicle2Name.getVehicleName());
        cv.put(KEY_Brand ,vehicle2Name.getBrand());
        cv.put(KEY_Model,vehicle2Name.getModel());
        cv.put(KEY_Colour,vehicle2Name.getColour());
        cv.put(KEY_RegistrationNo,vehicle2Name.getRegistrationNo());
        cv.put(KEY_Account3ID13,vehicle2Name.getAccount3ID());
        cv.put(KEY_Status,vehicle2Name.getStatus());
        cv.put(KEY_Lng13,vehicle2Name.getLng());
        cv.put(KEY_Lat13,vehicle2Name.getLat());
        cv.put(KEY_ContactNo,vehicle2Name.getContactNo());
        cv.put(KEY_SerialNo13,vehicle2Name.getSerialNo());
        cv.put(KEY_ClientID13,vehicle2Name.getClientID());
        cv.put(KEY_ClientUserID13,vehicle2Name.getClientUserID());
        cv.put(KEY_UpdatedDate13,vehicle2Name.getUpdatedDate());
        cv.put(KEY_NetCode13,vehicle2Name.getNetCode());
        cv.put(KEY_SysCode13,vehicle2Name.getSysCode());
        long status=database.insert(TABLE_Vehicle2Name,null,cv);

        Log.e("staus", "createVehicle2Name: "+status);

        return status;
    }

    //updating Vehicle2Name
    public void updateVehicle2NameLocal(String query){
        Log.e("updated query ", query);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    //deleting Vehicle2Name
    public void deleteVehicle2Name(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_Vehicle2Name);

    }

    public void deleteVehicle2NameEntry(){


    }
    public  List<Vehicle2Name> getVehicle2Name(String query){
        List<Vehicle2Name> vehicle2NameList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.moveToNext()){
            do{
                Vehicle2Name vehicle2Name=new Vehicle2Name();
                vehicle2Name.setID(c.getInt(0));
                vehicle2Name.setVehicleID(c.getString(1));
                vehicle2Name.setVehicleGroupID(c.getString(2));
                vehicle2Name.setVehicleName(c.getString(3));
                vehicle2Name.setBrand(c.getString(4));
                vehicle2Name.setModel(c.getString(5));
                vehicle2Name.setColour(c.getString(6));
                vehicle2Name.setRegistrationNo(c.getString(7));
                vehicle2Name.setAccount3ID(c.getString(8));
                vehicle2Name.setStatus(c.getString(9));
                vehicle2Name.setLng(c.getString(10));
                vehicle2Name.setLat(c.getString(11));
                vehicle2Name.setContactNo(c.getString(12));
                vehicle2Name.setSerialNo(c.getString(13));
                vehicle2Name.setClientID(c.getString(14));
                vehicle2Name.setClientUserID(c.getString(15));
                vehicle2Name.setUpdatedDate(c.getString(16));
                vehicle2Name.setNetCode(c.getString(17));
                vehicle2Name.setSysCode(c.getString(18));
                vehicle2NameList.add(vehicle2Name);
            }while (c.moveToNext());
        }
        return vehicle2NameList;
    }

    /**
     * create Vehicle3Booking
     */
    public void createVehicle3Booking(Vehicle3Booking vehicle3Booking)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(KEY_BookingID ,vehicle3Booking.getBookingID());
        cv.put(KEY_VehicleID14,vehicle3Booking.getVehicleID());
        cv.put(KEY_BookingDate14,vehicle3Booking.getBookingDate());
        cv.put(KEY_BookingDetail,vehicle3Booking.getBookingDetail());
        cv.put(KEY_BookingCharges,vehicle3Booking.getBookingCharges());
        cv.put(KEY_SerialNo14,vehicle3Booking.getSerialNo());
        cv.put(KEY_ClientID13,vehicle3Booking.getClientID());
        cv.put(KEY_ClientUserID14,vehicle3Booking.getClientUserID());
        cv.put(KEY_UpdatedDate14,vehicle3Booking.getUpdatedDate());
        cv.put(KEY_NetCode14,vehicle3Booking.getNetCode());
        cv.put(KEY_SysCode14,vehicle3Booking.getSysCode());
        long status=database.insert(TABLE_Vehicle3Booking,null,cv);
        Log.e("staus", "createVehicle3Booking "+status);
    }

    //updating Vehicle3Booking
    public void updateVehicle3Booking(){

    }

    public List<Vehicle3Booking> getVehicle3Booking(String query) {
        List<Vehicle3Booking> vehicle3BookingList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToNext()){
            do{
                Vehicle3Booking vehicle3Booking=new Vehicle3Booking();
                vehicle3Booking.setID(cursor.getInt(0));
                vehicle3Booking.setBookingID(cursor.getString(1));
                vehicle3Booking.setVehicleID(cursor.getString(2));
                vehicle3Booking.setBookingDate(cursor.getString(3));
                vehicle3Booking.setBookingDetail(cursor.getString(4));
                vehicle3Booking.setBookingCharges(cursor.getString(5));
                vehicle3Booking.setSerialNo(cursor.getString(6));
                vehicle3Booking.setClientID(cursor.getString(7));
                vehicle3Booking.setClientUserID(cursor.getString(8));
                vehicle3Booking.setUpdatedDate(cursor.getString(9));
                vehicle3Booking.setNetCode(cursor.getString(10));
                vehicle3Booking.setSysCode(cursor.getString(11));
                vehicle3BookingList.add(vehicle3Booking);
            }while (cursor.moveToNext());
        }
        return vehicle3BookingList;
    }

    //deleting Vehicle3booking
    public void deleteVehicle3Booking(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_Vehicle3Booking);
    }


    //While uploading data into server
    public  int updateVehicle2Name(int pk, String VehicleID, String updateDate, @Nullable Vehicle2Name vehicle2Name){

        //only update the UpdateDate on local storage
        if (VehicleID == null && updateDate != null) {
            /////Editing While User Edit The data From Form
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_UpdatedDate13, updateDate);

            int status = database.update(TABLE_Vehicle2Name,
                    values,
                    "ID = " + pk, null);
            return status;
        }else {
            //updating the VehicleID and updateDate on local storage as well
            //////Editing While Updating Data On Cloud
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_VehicleID, VehicleID);
            contentValues.put(KEY_UpdatedDate13, updateDate);
            int status = database.update(TABLE_Vehicle2Name,
                    contentValues,
                    "ID = " + pk, null);
            return status;
        }


    }


    //While uploading data into server
    public  int updateAccount5customGroup1(int pk, String CustomGroup1ID, String updateDate, @Nullable Account5customGroup1 account5customGroup1){

        //only update the UpdateDate on local storage
        if (CustomGroup1ID == null && updateDate != null) {
            /////Editing While User Edit The data From Form
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put("Account5customGroup1", updateDate);

            int status = database.update("Account5customGroup1",
                    values,
                    "ID = " + pk, null);
            return status;
        }else {
            //updating the CustomGroup1ID and updateDate on local storage as well
            //////Editing While Updating Data On Cloud
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("CustomGroup1ID", CustomGroup1ID);
            contentValues.put("UpdatedDate", updateDate);
            int status = database.update("Account5customGroup1",
                    contentValues,
                    "ID = " + pk, null);
            return status;
        }


    }

    ////////Update Vehicle2Name data from records that are edited on server
    public int updateVehicle2NameTablefromserver(Vehicle2Name vehicle2Name) {
        /////Editing While User Edit The data From Form


        Log.e("TAG", "updateVehicle2NameTablefromserver: "+"function update " );

        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(KEY_VehicleID , vehicle2Name.getVehicleID());
        cv.put(KEY_VehicleGroupID13,vehicle2Name.getVehicleGroupID());
        cv.put(KEY_VehicleName ,vehicle2Name.getVehicleName());
        cv.put(KEY_Brand ,vehicle2Name.getBrand());
        cv.put(KEY_Model,vehicle2Name.getModel());
        cv.put(KEY_Colour,vehicle2Name.getColour());
        cv.put(KEY_RegistrationNo,vehicle2Name.getRegistrationNo());
        cv.put(KEY_Account3ID13,vehicle2Name.getAccount3ID());
        cv.put(KEY_Status,vehicle2Name.getStatus());
        cv.put(KEY_Lng13,vehicle2Name.getLng());
        cv.put(KEY_Lat13,vehicle2Name.getLat());
        cv.put(KEY_ContactNo,vehicle2Name.getContactNo());
        cv.put(KEY_SerialNo13,vehicle2Name.getSerialNo());
        cv.put(KEY_ClientID13,vehicle2Name.getClientID());
        cv.put(KEY_ClientUserID13,vehicle2Name.getClientUserID());
        cv.put(KEY_UpdatedDate13,vehicle2Name.getUpdatedDate());
        cv.put(KEY_NetCode13,vehicle2Name.getNetCode());
        cv.put(KEY_SysCode13,vehicle2Name.getSysCode());
        int status=database.update(TABLE_Vehicle2Name,cv,
                "ClientID = " + vehicle2Name.getClientID() +
                        " AND VehicleID= '" + vehicle2Name.getVehicleID() + "'" +
                        ""
                , null);

        return status;
    }
    public String getVehicle2NameMaxUpdatedDate(String clientIDSession) {

        String query = "Select MAX(UpdatedDate) from Vehicle2Name where ClientID =" + clientIDSession;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS", String.valueOf(c.moveToFirst()));
        String maxDate = c.getString(0);
        Log.e("key", "Vehicle2Name Last UPDATED Date " + c.getString(0));

        return maxDate;
    }
    public List<JoinQueryAddVehicle> GetDataFromJoinQueryAddVehicle(String query){
        Log.e("amir", "GetDataFromJoinQueryAddVehicle: " +query );
        List<JoinQueryAddVehicle> joinQueryAddVehicleList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor c=database.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                JoinQueryAddVehicle mylist=new JoinQueryAddVehicle();
                mylist.setVehicleID(c.getString(0));
                mylist.setVehicleGroupID(c.getString(1));
                mylist.setVehicleName(c.getString(2));
                mylist.setBrand(c.getString(3));
                mylist.setModel(c.getString(4));
                mylist.setColour(c.getString(5));
                mylist.setRegistrationNo(c.getString(6));
                mylist.setStatus(c.getString(7));
                mylist.setContactNo(c.getString(8));
                mylist.setClientID(c.getString(9));
                mylist.setUpdatedDate(c.getString(10));
                mylist.setDriverName(c.getString(11));
                joinQueryAddVehicleList.add(mylist);
            }while (c.moveToNext());
        }
        return joinQueryAddVehicleList;
    }
    public int getMaxValueOfVehicle2Name(String ClientID){
        String query="select -(Max(Abs(Ifnull(VehicleID,0)))+1) from "+ TABLE_Vehicle2Name+
                " where ClientID = " + ClientID+"";
        Log.e("TEMP","("+query);
        SQLiteDatabase dbtem = this.getReadableDatabase();
        Cursor ctem = dbtem.rawQuery(query, null);
        ctem.moveToFirst();
        Log.e("TEMP" + "TEMP", "Maxium ID " + ctem.getInt(0));
        int valu=-1;
        if(ctem.getInt(0)==0){
            valu=-1;
        }else {
            valu=ctem.getInt(0);
        }
        return valu;
    }

    public int getMaxValueOfRestaurant2Table(String ClientID){
        String query="select -(Max(Abs(Ifnull(TableID,0)))+1) from Restaurant2Table where ClientID = " + ClientID+"";
        Log.e("TEMP","("+query);
        SQLiteDatabase dbtem = this.getReadableDatabase();
        Cursor ctem = dbtem.rawQuery(query, null);
        ctem.moveToFirst();
        Log.e("TEMP" + "TEMP", "Maxium ID " + ctem.getInt(0));
        int valu=-1;
        if(ctem.getInt(0)==0){
            valu=-1;
        }else {
            valu=ctem.getInt(0);
        }
        return valu;
    }


    public int getMaxValueOfRestaurant1Potion(String ClientID){
        String query="select -(Max(Abs(Ifnull(PotionID,0)))+1) from Restaurant1Potion where ClientID = " + ClientID+"";
        Log.e("TEMP","("+query);
        SQLiteDatabase dbtem = this.getReadableDatabase();
        Cursor ctem = dbtem.rawQuery(query, null);
        ctem.moveToFirst();
        Log.e("TEMP" + "TEMP", "Maxium ID " + ctem.getInt(0));
        int valu=-1;
        if(ctem.getInt(0)==0){
            valu=-1;
        }else {
            valu=ctem.getInt(0);
        }
        return valu;
    }

    public int getMaxValueOfAccount5customGroup1(String ClientID){
        String query="select -(Max(Abs(Ifnull(CustomGroup1ID,0)))+1) from Account5customGroup1 where ClientID = " + ClientID+"";
        Log.e("TEMP","("+query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Log.e("TEMP" + "TEMP", "Maxium ID " + cursor.getInt(0));

        int valu=-1;
        if(cursor.getInt(0)==0){
            valu=-1;
        }else {
            valu=cursor.getInt(0);
        }
        return valu;
    }
//    public  List<Account5customGroup1> getAccount5customGroup1(String query){
//        List<Account5customGroup1> account5customGroup1List=new ArrayList<>();
//        SQLiteDatabase db=this.getReadableDatabase();
//        Cursor c=db.rawQuery(query,null);
//        if(c.moveToNext()){
//            do{
//                Account5customGroup1 account5customGroup1=new Account5customGroup1();
//                account5customGroup1.setID(c.getInt(0));
//                account5customGroup1.setCustomGroup1ID(c.getString(1));
//                account5customGroup1.setCustomGroupName(c.getString(2));
//                account5customGroup1.setClientID(c.getString(3));
//                account5customGroup1.setClientUserID(c.getString(4));
//                account5customGroup1.setNetCode(c.getString(5));
//                account5customGroup1.setSysCode(c.getString(6));
//                account5customGroup1.setUpdatedDate(c.getString(7));
//
//                account5customGroup1List.add(account5customGroup1);
//
//            }while (c.moveToNext());
//        }
//        return account5customGroup1List;
//    }

    /**
     * Creating a CashBook
     */
    public long createCashBook(CashBook cashBook) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CashBookID, cashBook.getCashBookID());
        values.put(KEY_CBDate, cashBook.getCBDate());
        values.put(KEY_DebitAccount, cashBook.getDebitAccount());
        values.put(KEY_CreditAccount, cashBook.getCreditAccount());
        values.put(KEY_CBRemarks, cashBook.getCBRemarks());
        values.put(KEY_Amount, cashBook.getAmount());
        values.put(KEY_ClientID2, cashBook.getClientID());
        values.put(KEY_ClientUserID, cashBook.getClientUserID());
        values.put(KEY_NetCode2, cashBook.getNetCode());
        values.put(KEY_SysCode2, cashBook.getSysCode());
        values.put(KEY_UpdatedDate2, cashBook.getUpdatedDate());
        values.put(KEY_TableID, cashBook.getTableID());
        values.put(KEY_SerialNo2, cashBook.getSerialNo());
        values.put(KEY_TableName, cashBook.getTableName());

        // insert row
        long cid = db.insert(TABLE_CashBook, null, values);
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
        Log.e("dataforsave",bookings.toString());
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


    public void deleteAccount5customGroup1() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Account5customGroup1" );
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
                generalLedger.setTablename(c.getString(c.getColumnIndex("TableName")));
                generalLedger.setTableid(c.getString(c.getColumnIndex("TableID")));
                // adding to todo list
                mGeneralLedger.add(generalLedger);
            } while (c.moveToNext());
        }

        return mGeneralLedger;
    }
    public List<ItemLedger> getItemLedger(String query) {
        List<ItemLedger> mItemLedger = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS", String.valueOf(c.moveToFirst()));
        ItemLedger.columnNames=new String[c.getColumnCount()];
        ItemLedger.columnNames=c.getColumnNames();
        for (int i = 0; i <c.getColumnCount() ; i++) {
            Log.e("ItemLedgerSequ","ColNo"+i+")"+c.getColumnName(i));
        }

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ItemLedger itemLedger=new ItemLedger(c.getColumnCount());
                for (int i = 0; i <c.getColumnCount() ; i++) {
                    itemLedger.columnsData[i]=c.getString(i);
                }
                ///////////////////////////////AddingManualEachColumn
                itemLedger.ClientIDcol=c.getString(0);
                itemLedger.Datecol=c.getString(1);
                itemLedger.ItemIDcol=c.getString(2);
                itemLedger.EntryTypecol=c.getString(3);
                itemLedger.BillNocol=c.getString(4);
                itemLedger.AccountNameCol=c.getString(5);
                itemLedger.RemarksCol=c.getString(6);
                itemLedger.AddCol=c.getString(7);
                itemLedger.LessCol=c.getString(8);
                itemLedger.PriceCol=c.getString(9);
                itemLedger.BalCol=c.getString(10);
                itemLedger.UserCol=c.getString(11);
                itemLedger.LocCol=c.getString(12);
                mItemLedger.add(itemLedger);
            } while (c.moveToNext());
        }

        return mItemLedger;
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
                generalLedger.setUpdatedDate(c.getString(c.getColumnIndex("UpdatedDate")));
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


        Log.e(LOG+"me", query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if(c!=null && c.getCount()>0){
            for (int i = 0; i <c.getColumnCount() ; i++) {
                Log.e("colutest","ColumnName("+i+")"+c.getColumnName(i));
            }
        }

        Log.e("SSSS", String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Recovery generalLedger = new Recovery();
                Log.e("SSSSS", ""+c.getInt(c.getColumnIndex("BookingID")));

                generalLedger.setClientID(c.getString(c.getColumnIndex("ClientNic")));
                generalLedger.setBookingID(c.getString(c.getColumnIndex("BookingID")));
                generalLedger.setRecieved(c.getString(c.getColumnIndex("Received"))+"");
                generalLedger.setExpensed(c.getString(c.getColumnIndex("Expense"))+"");
//                generalLedger.setRecieved("0");
//                generalLedger.setExpensed("0");
                generalLedger.setChargesTotal(c.getString(c.getColumnIndex("ChargesTotal"))+"");
               // generalLedger.setChargesTotal("0");
                generalLedger.setBalance(c.getString(c.getColumnIndex("Balance")));
                generalLedger.setProfit(c.getString(c.getColumnIndex("Profit")));
                generalLedger.setEventName(c.getString(c.getColumnIndex("EventName")));
                //generalLedger.setEventName("Null");
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
        Log.e("key", "No of Coumns " + c.getColumnCount());
        //looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Reports reports = new Reports();
                reports.SqliteDbID = c.getString(c.getColumnIndex("ID"));
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
    public List<joinQueryCashCollection> getDataFromJoinQueryCashCollection(String query){
        Log.e("amir", "getDataFromJoinQueryCashCollection: " +query );
        List<joinQueryCashCollection> list=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor c=database.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                joinQueryCashCollection mylist=new joinQueryCashCollection();
                mylist.setClientID(c.getString(c.getColumnIndex("ClientID")));
                mylist.setBillNo(c.getString(c.getColumnIndex("BillNo")));
                mylist.setEntryType(c.getString(c.getColumnIndex("EntryType")));
                mylist.setBillAmount(c.getString(c.getColumnIndex("BillAmount")));
                mylist.setReceived(c.getString(c.getColumnIndex("Received")));
                mylist.setBillBalance(c.getString(c.getColumnIndex("BillBalance")));
                mylist.setBillStatus(c.getString(c.getColumnIndex("BillStatus")));
                mylist.setRemarks(c.getString(c.getColumnIndex("Remarks")));
                mylist.setSPDate(c.getString(c.getColumnIndex("SPDate")));
                mylist.setUser(c.getString(c.getColumnIndex("User")));
                list.add(mylist);
            }while (c.moveToNext());
        }
        return list;
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

    public int getMaxAccount3NameMaxAcNameID(String cliid){
        String qq="select -(Max(Abs(Ifnull(AcNameID,0)))+1) from Account3Name"+
                " where ClientID=" + cliid;
        Log.e("TEMP","("+qq);
        SQLiteDatabase dbtem = this.getReadableDatabase();
        Cursor ctem = dbtem.rawQuery(qq, null);
        ctem.moveToFirst();
        Log.e("TEMP" + "TEMP", "Maxium ID " + ctem.getInt(0));
        ctem.moveToFirst();
        return ctem.getInt(0);
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

    public List<String> getPortationName(String query){
        List<String> list=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("Restaurant1Potion", String.valueOf(c.moveToFirst()));

        if (c.moveToFirst()) {
            do {
                String temp= c.getString(c.getColumnIndex("PotionName"));
                list.add(temp);
            } while (c.moveToNext());
        }
        return list;
    }

    public List<joinQueryForResturent> GetDataFromjoinQueryForResturent(String query){
        Log.e("amir", "joinQueryForResturent: " +query );
        List<joinQueryForResturent> joinQueryForResturentList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor c=database.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                joinQueryForResturent mylist=new joinQueryForResturent();
                mylist.setTableName(c.getString(c.getColumnIndex("TableName")));
                mylist.setPotionName(c.getString(c.getColumnIndex("PotionName")));
                mylist.setBillAmount(c.getString(c.getColumnIndex("BillAmount")));
                mylist.setTableStatus(c.getString(c.getColumnIndex("TableStatus"))) ;
                mylist.setClientID(c.getString(c.getColumnIndex("ClientID")));
                mylist.setSalPur1ID(c.getString(c.getColumnIndex("SalPur1ID")));
                mylist.setTableID(c.getString(c.getColumnIndex("TableID")));
                joinQueryForResturentList.add(mylist);
            }while (c.moveToNext());
        }
        return joinQueryForResturentList;
    }

    public List<joinQueryForResturentAddOrder> GetDataFromjoinQueryForResturentAddOrder(String query){
        Log.e("amir", "joinQueryForResturent: " +query );
        List<joinQueryForResturentAddOrder> joinQueryForResturentAddOrderList=new ArrayList<>();
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor c=database.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                joinQueryForResturentAddOrder mylist=new joinQueryForResturentAddOrder();
                mylist.setItem2GroupID(c.getString(0));
                mylist.setItem2GroupName(c.getString(1));
                mylist.setItem3NameID(c.getString(2));
                mylist.setItemName(c.getString(3)); ;
                mylist.setSalePrice(c.getString(4));
                mylist.setStock(c.getString(5));
                mylist.setItemStatus(c.getString(6));
                mylist.setItemType(c.getString(7)); ;
                mylist.setClientID(c.getString(8));
                joinQueryForResturentAddOrderList.add(mylist);
            }while (c.moveToNext());
        }
        return joinQueryForResturentAddOrderList;
    }
    public List<String> getItem2GroupName(String query){
        List<String> list=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("Item2Group", String.valueOf(c.moveToFirst()));

        if (c.moveToFirst()) {
            do {
                String temp= c.getString(c.getColumnIndex("Item2GroupName"));
                list.add(temp);
            } while (c.moveToNext());
        }
        return list;
    }

    public void deleteRestaurant2Table() {
        Log.e("delete", "deleteRestaurant2Table: delete fun " );
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Restaurant2Table");
        Log.e("delete", "deleteRestaurant2Table: delete fun execute " );
    }
    public long createRestaurant2Table(Restaurant2Table restaurant2Table) {
        Log.d("aaaaaaaaaaaaa", "createRestaurant2Table: fun1");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Log.d("aaaaaaaaaaaaa", "createRestaurant2Table: fun2");

        values.put("TableID", restaurant2Table.getTableID());
        values.put("PotionID", restaurant2Table.getPotionID());
        values.put("TableName",restaurant2Table.getTabelName());
        values.put("TableDescription", restaurant2Table.getTableDescription());
        values.put("TableStatus", restaurant2Table.getTableStatus());
        values.put("ClientID", restaurant2Table.getClientID());
        values.put("ClientUserID", restaurant2Table.getClientUserID());
        values.put("NetCode",restaurant2Table.getNetCode());
        values.put("SysCode", restaurant2Table.getSysCode());
        values.put("UpdatedDate", restaurant2Table.getUpdatedDate());
        values.put("SalPur1ID",restaurant2Table.getSalPur1ID());

        Log.d("aaaaaaaaaaaaa", "createRestaurant2Table: fun3");
        // insert row
        return db.insert("Restaurant2Table", null, values);
    }

    public int updateSalePur1BillAmount(int salepur1id,int billAmount){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("BillAmount",billAmount);
        int status=db.update(refdb.SlePur1.TABLE_SalePur1,values,"SalePur1ID = '"+salepur1id+"'",null);
        return  status;


    }
    public int updateSalePur1BillSatus(String salepur1id,String  BillSatus,String ClientID){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("BillStatus",BillSatus);
        int status=db.update(refdb.SlePur1.TABLE_SalePur1,values,"SalePur1ID = '"+ salepur1id +"' AND ClientID = '"+ ClientID +"'",null);
        return  status;


    }

    public int updateResturent1Portation(int PrimaryKey,String  PortaionName){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("PotionName",PortaionName);
        int status=db.update("Restaurant1Potion",values,"ID = '"+ PrimaryKey +"'",null);
        return  status;


    }

    public int updateResturent2TableSalePur1ID(String tableName, int newSalepur1ID,String TableStatus,String TableID ){

        Log.d("amir", "updateResturent2TableSalePur1ID: "+newSalepur1ID+ " "+TableID+ "  "+tableName);
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("SalPur1ID",newSalepur1ID);
        values.put("TableStatus",TableStatus);

        int status=db.update("Restaurant2Table",values,"TableID = '"+TableID+"' AND TableName = '"+tableName+"'",null);
        //int status=db.update("Restaurant2Table",values,"TableName = '"+tableName+"'",null);
        return  status;


    }
    public int updateResturent2TableTableStatus(String tableName, String Salepur1ID,String TableStatus,String ClientID,String TableID ){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("TableStatus",TableStatus);

        int status=db.update("Restaurant2Table",values,"TableID = '"+TableID+"' AND TableName = '"+tableName+"' AND ClientID = '"+ClientID+"'",null);
        return  status;


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
    public List<ItemLedgerRef> getItemLedgerSpinner(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        List<ItemLedgerRef> spinners = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                ItemLedgerRef itemLedger=new ItemLedgerRef();
                itemLedger.setItemname(c.getString(0));
                itemLedger.setItem3ID(c.getString(1));
spinners.add(itemLedger);
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
        values.put(KEY_UserRights4,client.getUserRights());
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


    //Getting Maximun Value of Item3Name
    public int getMaxValueOfItem3Name(String ClientID){
        String qq="select -(Max(Abs(Ifnull(Item3NameID,0)))+1) from "+ refdb.Table3Name.TABLE_Item3Name+
                " where ClientID=" + ClientID+"";
        Log.e("TEMP","("+qq);
        SQLiteDatabase dbtem = this.getReadableDatabase();
        Cursor ctem = dbtem.rawQuery(qq, null);
        ctem.moveToFirst();
        Log.e("TEMP" + "TEMP", "Maxium ID " + ctem.getInt(0));
        int returnval=-1;
        if(ctem.getInt(0)==0){
            returnval=-1;
        }else{
            returnval=ctem.getInt(0);
        }
        Log.e("aaaaa", "getMaxValueOfItem3Name: "+returnval );
        return returnval;
    }
    //Getting Maximun Value of Item3Name
    public String getMaxUpDateOfItem3Name(String ClientID){
        String qq="select (Max(UpdatedDate)) from "+ refdb.Table3Name.TABLE_Item3Name+
                " where ClientID=" + ClientID+"";
        Log.e("TEMP","("+qq);
        SQLiteDatabase dbtem = this.getReadableDatabase();
        Cursor ctem = dbtem.rawQuery(qq, null);
        ctem.moveToFirst();
        Log.e("TEMP" + "TEMP", "Maxium ID " + ctem.getInt(0));
        return ctem.getString(0);
    }
    //getting Maximun Value of Item2Group
    public int getMaxValueOfItem2Group(String ClientID){
        String qq="select -(Max(Abs(Ifnull(Item2GroupID,0)))+1) from "+ refdb.Table2Group.TABLE_Item2Group+
                " where ClientID=" + ClientID+"";
        Log.e("TEMP","("+qq);
        SQLiteDatabase dbtem = this.getReadableDatabase();
        Cursor ctem = dbtem.rawQuery(qq, null);
        ctem.moveToFirst();
        Log.e("TEMP" + "TEMP", "Maxium ID " + ctem.getInt(0));

        int returnval=-1;
        if(ctem.getInt(0)==0){
            returnval=-1;
        }else{
            returnval=ctem.getInt(0);
        }
        Log.e("aaaaa", "getMaxValueOfItem3Name: "+returnval );
        return returnval;

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

    //update item2Group
    public void updateItem2Group(String query) {
        Log.e("updated query ", query);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }
    public void updateItem3Name(String query) {
        Log.e("updated query ", query);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
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

    /////////////////////////////////item 1 Type Data Related Fun
    public List<Item1Type> getItem1TypeData(String query) {
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
                item1Type.setItemType(c.getString(c.getColumnIndex(refdb.TableItem1.KEY_ItemType)));
                item1TypeList.add(item1Type);
            } while (c.moveToNext());
        }

        return item1TypeList;
    }

    public List<Account5customGroup1> getAccount5customGroup1(String query){
        Log.e("amir", "getAccount5customGroup1: "+query );
        List<Account5customGroup1> customGroup1List=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                Account5customGroup1 list=new Account5customGroup1();
                list.setID(c.getInt(c.getColumnIndex("ID")));
                list.setCustomGroup1ID(c.getString(c.getColumnIndex("CustomGroup1ID")));
                list.setCustomGroupName(c.getString(c.getColumnIndex("CustomGroupName")));
                list.setClientID(c.getString(c.getColumnIndex("ClientID")));
                list.setClientUserID(c.getString(c.getColumnIndex("ClientUserID")));
                list.setNetCode(c.getString(c.getColumnIndex("NetCode")));
                list.setSysCode(c.getString(c.getColumnIndex("SysCode")));
                list.setUpdatedDate(c.getString(c.getColumnIndex("UpdatedDate")));
                Log.e("amir if", "getAccount5customGroup1: "+list );
                customGroup1List.add(list);
            }while (c.moveToNext());
        }
        return customGroup1List;
    }

    public List<Account5customGroup2> getAccount5customGroup2(String query){
        Log.e("amir", "getAccount5customGroup2: "+query );
        List<Account5customGroup2> customGroup2List=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                Account5customGroup2 list=new Account5customGroup2();
                list.setID(c.getInt(c.getColumnIndex("ID")));
                list.setCustomGroup2ID(c.getString(c.getColumnIndex("CustomGroup2ID")));
                list.setCustomGroup1ID(c.getString(c.getColumnIndex("CustomGroup1ID")));
                list.setAcNameID(c.getString(c.getColumnIndex("AcNameID")));
                list.setClientID(c.getString(c.getColumnIndex("ClientID")));
                list.setClientUserID(c.getString(c.getColumnIndex("ClientUserID")));
                list.setNetCode(c.getString(c.getColumnIndex("NetCode")));
                list.setSysCode(c.getString(c.getColumnIndex("SysCode")));
                list.setUpdatedDate(c.getString(c.getColumnIndex("UpdatedDate")));
                Log.e("amir ", "getAccount5customGroup2: "+list );
                customGroup2List.add(list);
            }while (c.moveToNext());
        }
        return customGroup2List;
    }

    public long createAccount5customGroup1(Account5customGroup1 account5customGroup1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CustomGroup1ID", account5customGroup1.getCustomGroup1ID());
        values.put("CustomGroupName", account5customGroup1.getCustomGroupName());
        values.put("ClientID", account5customGroup1.getClientID());
        values.put("ClientUserID", account5customGroup1.getClientUserID());
        values.put("NetCode", account5customGroup1.getNetCode());
        values.put("SysCode", account5customGroup1.getSysCode());
        values.put("UpdatedDate", account5customGroup1.getUpdatedDate());
        // insert row
        return db.insert("Account5customGroup1", null, values);
    }

    public long createItem1Typeitem(Item1Type item1Type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(refdb.TableItem1.KEY_Item1TypeID, item1Type.getItem1TypeID());
        values.put(refdb.TableItem1.KEY_ItemType, item1Type.getItemType());
        // insert row
        return db.insert(TABLE_Item1Type, null, values);
    }

    public void deleteAllItem1Type() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + refdb.TableItem1.TABLE_Item1Type);
    }

    ////////////////item 2 Group DAta Functions
    public String getMaxIDItem2Group(int type,String cid){
       if(type==1){
           String qq="select Item2GroupID as maxid from Item2Group ";
           SQLiteDatabase db = this.getReadableDatabase();
           Cursor c = db.rawQuery(qq, null);
           Log.e("Item2Groupmaxid", qq);
           int max=0;
          if(c.moveToFirst()){
              do{
                  Log.e("ItemID","--"+c.getInt(0));
                  if(c.getInt(0)>max){
                      max=c.getInt(0);
                  }
              }while (c.moveToNext());
          }

           Log.e("Item2Groupmaxid", "Item2Group Max Item2GroupID  " +max);
           return ""+max;
       }else if(type==2){
           String qq="select max(UpdatedDate) as maxid from Item2Group where ClientID="+cid;
           SQLiteDatabase db = this.getReadableDatabase();
           Cursor c = db.rawQuery(qq, null);
           Log.e("Item2Groupmaxid", String.valueOf(c.moveToFirst()));

           String update = c.getString(0);
           Log.e("Item2Groupmaxid", "Item2Group Max UpdatedDate  " + c.getString(0));
           return update;
       }
       return "Null";
    }

    public int UpdateItem2GroupTable(Item2Group refobj,int updatetype){
        if(updatetype==1){
            //////////////////Update Only Item2GroupID,UpdatedDate
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(refdb.Table2Group.KEY_1, refobj.getItem2GroupID());
            values.put(refdb.Table2Group.KEY_8, refobj.getUpdatedDate());


            int sta = database.update(refdb.Table2Group.TABLE_Item2Group,
                    values,
                    "ID = " + refobj.getID()
                    , null);
            return sta;
        }else  if(updatetype==2){
            //////////////////Update Only Item2GroupID,UpdatedDate
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(refdb.Table2Group.KEY_1, refobj.getItem2GroupID());
            values.put(refdb.Table2Group.KEY_2, refobj.getItem1TypeID());
            values.put(refdb.Table2Group.KEY_3, refobj.getItem2GroupName());
            values.put(refdb.Table2Group.KEY_4, refobj.getClientID());
            values.put(refdb.Table2Group.KEY_5, refobj.getClientUserID());
            values.put(refdb.Table2Group.KEY_6, refobj.getNetCode());
            values.put(refdb.Table2Group.KEY_7, refobj.getSysCode());
            values.put(refdb.Table2Group.KEY_8, refobj.getUpdatedDate().toString());


            int sta = database.update(refdb.Table2Group.TABLE_Item2Group,
                    values,
                    " Item2GroupID ="+refobj.getItem2GroupID()+
                            " AND ClientID="+refobj.getClientID()
                    , null);
            return sta;
        }else  if(updatetype==3){
            //////////////////Update Only Item2GroupID,UpdatedDate
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();

           // values.put(refdb.Table2Group.KEY_1, refobj.getItem2GroupID());
            values.put(refdb.Table2Group.KEY_8, refobj.getUpdatedDate());


            int sta = database.update(refdb.Table2Group.TABLE_Item2Group,
                    values,
                    "ID = " + refobj.getID()
                    , null);
            return sta;
        }
        return -9;
    }

    public long createItem2GroupData(Item2Group item2Group) {
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

    public List<Item2Group> getItem2GroupData(String query) {
        List<Item2Group> item2GroupList = new ArrayList<>();
        Log.e(LOG, query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("Item 2 Group", String.valueOf(c.moveToFirst()));
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Item2Group item2Group = new Item2Group();
                item2Group.setID(c.getInt(0));
                item2Group.setItem2GroupID(c.getString(1));
                item2Group.setItem1TypeID(c.getString(2));
                item2Group.setItem2GroupName(c.getString(3));
                item2Group.setClientID(c.getString(4));
                item2Group.setClientUserID(c.getString(5));
                item2Group.setNetCode(c.getString(6));
                item2Group.setSysCode(c.getString(7));
                item2Group.setUpdatedDate(c.getString(8));
                item2GroupList.add(item2Group);
            } while (c.moveToNext());
        }

        return item2GroupList;
    }

    public void deleteAllItem2Group() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + refdb.Table2Group.TABLE_Item2Group);
    }

    ////////////////item 3 Name DAta Functions
    public long createItem3NameData(Item3Name_ item3Name_) {
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
        return db.insert(refdb.Table3Name.TABLE_Item3Name, null, values);
    }
    public void updateAccount5CustomGroup1DataOnLocal(String query){
        Log.e("updated query ", query);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }
    public List<JoinQueryAccount3Name> GetDataFromJoinQueryAccount3Name(String query){
        List<JoinQueryAccount3Name> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                JoinQueryAccount3Name account3Name=new JoinQueryAccount3Name();
                account3Name.setAcNameID(cursor.getString(0));
                account3Name.setAcName(cursor.getString(1));
                account3Name.setAcNameID1(cursor.getString(2));
                account3Name.setAcNameID1(cursor.getString(3));
                list.add(account3Name);
            }while (cursor.moveToNext());
        }
        return list;
    }
    public int getMaxValueOfAccount5customGroup2(String ClientID){
        String qq="select -(Max(Abs(Ifnull(CustomGroup2ID,0)))+1) from Account5customGroup2 where ClientID=" + ClientID+"";
        Log.e("TEMP","("+qq);
        SQLiteDatabase dbtem = this.getReadableDatabase();
        Cursor ctem = dbtem.rawQuery(qq, null);
        ctem.moveToFirst();
        Log.e("TEMP" + "TEMP", "Maxium ID " + ctem.getInt(0));
        return ctem.getInt(0);
    }
    public long createAccount5customGroup2(Account5customGroup2 account5customGroup2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CustomGroup2ID", account5customGroup2.getCustomGroup2ID());
        values.put("CustomGroup1ID", account5customGroup2.getCustomGroup1ID());
        values.put("AcNameID", account5customGroup2.getAcNameID());
        values.put("ClientID", account5customGroup2.getClientID());
        values.put("ClientUserID", account5customGroup2.getClientUserID());
        values.put("NetCode", account5customGroup2.getNetCode());
        values.put("SysCode", account5customGroup2.getSysCode());
        values.put("UpdatedDate", account5customGroup2.getUpdatedDate());
        // insert row
        return db.insert("Account5customGroup2", null, values);
    }

    public List<Item3Name_> getItem3NameData(String query) {
        List<Item3Name_> item3Name_list = new ArrayList<>();
        Log.e(LOG, query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("Item3Name", String.valueOf(c.moveToFirst()));
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                Item3Name_ item3Name = new Item3Name_();
                item3Name.setID(c.getInt(0));
                item3Name.setItem3NameID(c.getInt(1));
                item3Name.setItem2GroupID(c.getInt(2));
                item3Name.setItemName(c.getString(3));
                item3Name.setClientID(c.getInt(4));
                item3Name.setClientUserID(c.getInt(5));
                item3Name.setNetCode(c.getString(6));
                item3Name.setSysCode(c.getString(7));
                item3Name.setSalePrice(c.getString(8));
                item3Name.setItemCode(c.getString(9));
                item3Name.setStock(c.getString(10));
                UpdatedDate updatedDate = new UpdatedDate();
                updatedDate.setDate(c.getString(11));
                item3Name.setUpdatedDate(updatedDate);


          Log.e("SendDataToDB","INGET"+item3Name.toString());
                item3Name_list.add(item3Name);
            } while (c.moveToNext());
        }
        Log.e("SendDataToDB","Sizeeitem3Name"+item3Name_list.size());
        return item3Name_list;
    }
    public int UpdateItem3NameTable(Item3Name_ refobj,@Nullable String oldid,int updatetype){

        Log.e(LOG,"item "+refobj.toString()+"\n old id:"+oldid+" updatetype:"+updatetype);
        if(updatetype==1){
            //////////////////Update Only Item2GroupID,UpdatedDate
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(refdb.Table3Name.KEY_2, refobj.getItem2GroupID());
            values.put("UpdatedDate",GenericConstants.NullFieldStandardText);

            int sta = database.update(refdb.Table3Name.TABLE_Item3Name,
                    values,
                    "Item2GroupID = " + oldid+
                            " AND ClientID ="+refobj.getClientID()
                    , null);
            return sta;
        }else if(updatetype==2){
            ///////////Updated The Updated DAte and item3NameID
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(refdb.Table3Name.KEY_1, refobj.getItem3NameID());
            values.put(refdb.Table3Name.KEY_11, refobj.getUpdatedDate().getDate());
            int sta = database.update(refdb.Table3Name.TABLE_Item3Name,
                    values,
                    "ID = " + refobj.getID()
                    , null);
            return sta;
        }else if(updatetype==3){
            ///////////Updated The Updated DAte and item3NameID
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(refdb.Table3Name.KEY_11, refobj.getUpdatedDate().getDate());
            int sta = database.update(refdb.Table3Name.TABLE_Item3Name,
                    values,
                    "ID = " + refobj.getID()
                    , null);
            return sta;
        }else if(updatetype==4){
            ///////////Updated The Updated DAte and item3NameID
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(refdb.Table3Name.KEY_1, refobj.getItem3NameID());
            values.put(refdb.Table3Name.KEY_2, refobj.getItem2GroupID());
            values.put(refdb.Table3Name.KEY_3, refobj.getItemName());
            values.put(refdb.Table3Name.KEY_4, refobj.getClientID());
            values.put(refdb.Table3Name.KEY_5, refobj.getClientUserID());
            values.put(refdb.Table3Name.KEY_6, refobj.getNetCode());
            values.put(refdb.Table3Name.KEY_7, refobj.getSysCode());
            values.put(refdb.Table3Name.KEY_8, refobj.getSalePrice());
            values.put(refdb.Table3Name.KEY_9, refobj.getItemCode());
            values.put(refdb.Table3Name.KEY_10, refobj.getStock());
            values.put(refdb.Table3Name.KEY_11, refobj.getUpdatedDate().getDate());
            int sta = database.update(refdb.Table3Name.TABLE_Item3Name,
                    values,
                    "Item3NameID = " + refobj.getItem3NameID()+
                            " AND ClientID = "+refobj.getClientID()
                    , null);
            return sta;
        }
        return -9;
    }
    public void deleteAllItem3Name() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + refdb.Table3Name.TABLE_Item3Name);

    }

    ////////////////SalePur1 DAta Functions
    ////////Update salepur1 data from records that are edited on server
    public int updateSalePur1Tablefromserver(Salepur1 salepur1) {
        /////Editing While User Edit The data From Form
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(refdb.SlePur1.KEY_1, salepur1.getSalePur1ID());
        values.put(refdb.SlePur1.KEY_2, salepur1.getEntryType());
        values.put(refdb.SlePur1.KEY_3, salepur1.getSPDate().getDate());
        values.put(refdb.SlePur1.KEY_4, salepur1.getAcNameID());
        values.put(refdb.SlePur1.KEY_5, salepur1.getRemarks());
        values.put(refdb.SlePur1.KEY_6, salepur1.getClientID());
        values.put(refdb.SlePur1.KEY_7, salepur1.getClientUserID());
        values.put(refdb.SlePur1.KEY_8, salepur1.getNetCode());
        values.put(refdb.SlePur1.KEY_9, salepur1.getSysCode());
        values.put(refdb.SlePur1.KEY_10, salepur1.getUpdatedDate().getDate());
        values.put(refdb.SlePur1.KEY_11, salepur1.getNameOfPerson());
        values.put(refdb.SlePur1.KEY_12, salepur1.getPayAfterDay());
        int sta = database.update(refdb.SlePur1.TABLE_SalePur1,
                values,
                "ClientID = " + salepur1.getClientID() +
                        " AND SalePur1ID=" + salepur1.getSalePur1ID() +
                        " AND EntryType= '" + salepur1.getEntryType() + "'" +
                        ""
                , null);
        return sta;
    }

    public List<JoinQueryDaliyEntryPage1> GetDataFroJoinQuery(String query) {
        Log.e(LOG, query);
        List<JoinQueryDaliyEntryPage1> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("GetDataFroJoinQuery", String.valueOf(c.moveToFirst()));

        String[] names = c.getColumnNames();
        Log.e("GetDataFroJoinQuery", " Size::" + c.getCount());

        ///Expected Columns lists
        // SalePur1ID,EntryType,SPDate,AcName,Remarks,BillAmt,ClientID,NameOfPerson,PayAfterDay
        for (String str : names) {

            Log.e("GetDataFroJoinQuery", " CloumnNames::" + str);
        }
        if (c.moveToFirst()) {
            do {
                JoinQueryDaliyEntryPage1 page1 = new JoinQueryDaliyEntryPage1();
                page1.setSalePur1ID(c.getString(0));
                page1.setEntryType(c.getString(1));
                page1.setSPDate(c.getString(2));
                page1.setAcName(c.getString(3));
                page1.setRemarks(c.getString(4));
                page1.setBillAmt(c.getString(5));
                page1.setClientID(c.getString(6));
                page1.setNameOfPerson(c.getString(7));
                page1.setPayAfterDay(c.getString(8));
                page1.PkID = c.getString(9);
                list.add(page1);

            } while (c.moveToNext());
        }
        // looping through all rows and adding to list
//        if (c.moveToFirst()) {
//            do {
//                Item1Type item1Type = new Item1Type();
//                item1Type.setItem1TypeID(c.getString(c.getColumnIndex(refdb.TableItem1.KEY_Item1TypeID)));
//                item1Type.setItemType(c.getString(c.getColumnIndex(refdb.TableItem1.KEY_ItemType)));
//                item1TypeList.add(item1Type);
//            } while (c.moveToNext());
//        }

        return list;

    }

    public List<ModelForSalePur1page2> GetDataFroJoinQuerySalerpurpage2(String query) {
        Log.e("GetDataFroJoinQuerySale", query);
        List<ModelForSalePur1page2> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        Log.e("GetDataFroJoinQuerySale", String.valueOf(c.moveToFirst()));

        String[] names = c.getColumnNames();
        Log.e("GetDataFroJoinQuerySale", " Size::" + c.getCount());


        for (String str : names) {

            Log.e("GetDataFroJoinQuerySale", " CloumnNames::" + str);
        }

///:AcNameID AcName AcGruopName Balance
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String data[] = new String[c.getColumnCount()];
            for (int i = 0; i < data.length; i++) {
                data[i] = c.getString(i);
            }
            list.add(new ModelForSalePur1page2(data));
        }
//        if (c.moveToFirst()) {
//            do{
//                JoinQueryDaliyEntryPage1 page1=new JoinQueryDaliyEntryPage1();
//                page1.setSalePur1ID(c.getString(0));
//                page1.setEntryType(c.getString(1));
//                page1.setSPDate(c.getString(2));
//                page1.setAcName(c.getString(3));
//                page1.setRemarks(c.getString(4));
//                page1.setBillAmt(c.getString(5));
//                page1.setClientID(c.getString(6));
//                page1.setNameOfPerson(c.getString(7));
//                page1.setPayAfterDay(c.getString(8));
//                list.add(page1);
//
//            }while (c.moveToNext());
//        }
        // looping through all rows and adding to list
//        if (c.moveToFirst()) {
//            do {
//                Item1Type item1Type = new Item1Type();
//                item1Type.setItem1TypeID(c.getString(c.getColumnIndex(refdb.TableItem1.KEY_Item1TypeID)));
//                item1Type.setItemType(c.getString(c.getColumnIndex(refdb.TableItem1.KEY_ItemType)));
//                item1TypeList.add(item1Type);
//            } while (c.moveToNext());
//        }
        c.close();
        return list;

    }

    public int GetMaximunSalePur1ID(String ClientID,String entryType) {

        String qq="select -(Max(Abs(Ifnull(SalePur1ID,0)))+1) from "+ refdb.SlePur1.TABLE_SalePur1+
                " where ClientID=" + ClientID+" AND EntryType= '"+entryType+"'";
        Log.e("TEMP","("+qq);
        SQLiteDatabase dbtem = this.getReadableDatabase();
        Cursor ctem = dbtem.rawQuery(qq, null);
        ctem.moveToFirst();
        Log.e("TEMP" + "TEMP", "Maxium ID " + ctem.getInt(0));

        int returnval=-1;
        if(ctem.getInt(0)==0){
            returnval=-1;
        }else {
            returnval=ctem.getInt(0);
        }
        return returnval;

        ///////////////////////////////////////////////////Above code is temp

//        String querymax = "select max(abs(SalePur1ID)) as maxid from " + refdb.SlePur1.TABLE_SalePur1 + "" +
//                " where ClientID=" + ClientID+" AND EntryType= '"+entryType+"'";
//
//        String querymin = "select SalePur1ID as maxid from " + refdb.SlePur1.TABLE_SalePur1 + "" +
//                " where ClientID=" + ClientID+" AND EntryType= '"+entryType+"'";
//        Log.e(LOG + "ID", "MinID:" + querymin);
//        Log.e(LOG + "ID", "MaxID:" + querymax);
//     //for Editing and inserting (differance)
//        int maxid = -99; // for Editing
//                int minid = -9999; //for inserting
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery(querymax, null);
//        Cursor cmin = db.rawQuery(querymin, null);
//        for (int i = 0; i < c.getColumnCount(); i++) {
//            Log.e("col", c.getColumnName(i));
//        }
//
//        if (c != null && c.getCount() > 0) {
//            c.moveToFirst();
//            Log.e(LOG + "ID", "Maxium ID " + c.getInt(0));
//            maxid = c.getInt(0);
//        } else
//            Log.e(LOG + "ID", "Maxium ID -99");
//        if (cmin != null && cmin.getCount() > 0) {
//            cmin.moveToFirst();
//            int pre, next, min = 0;
//            pre = next = cmin.getInt(0);
//            do {
//
//                next = cmin.getInt(0);
//                if (pre > next)
//                    min = next;
//                pre = next;
//
//            } while (cmin.moveToNext());
//            minid = min;
//            Log.e(LOG + "ID", "Menium ID " + minid);
//
//        } else
//            Log.e(LOG + "ID", "Menium ID -99");
//        ///////////////Criterial for alogrith
//
//        if (minid >= 0) {
//            minid = -maxid - 1;
//        } else {
//            minid = minid - 1;
//        }
////        if (minid < 0)
////            minid = minid * -1;
//
//        Log.e(LOG + "ID", "MINIMID::" + minid);
//        return minid;

    }

    public long createSalePur1Data(Salepur1 salepur1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(refdb.SlePur1.KEY_1, salepur1.getSalePur1ID());
        values.put(refdb.SlePur1.KEY_2, salepur1.getEntryType());
        //  values.put(refdb.SlePur1.KEY_3, salepur1.getSPDate().getDate());
        values.put(refdb.SlePur1.KEY_3, salepur1.getData());
        values.put(refdb.SlePur1.KEY_4, salepur1.getAcNameID());
        values.put(refdb.SlePur1.KEY_5, salepur1.getRemarks());
        values.put(refdb.SlePur1.KEY_6, salepur1.getClientID());
        values.put(refdb.SlePur1.KEY_7, salepur1.getClientUserID());
        values.put(refdb.SlePur1.KEY_8, salepur1.getNetCode());
        values.put(refdb.SlePur1.KEY_9, salepur1.getSysCode());
        values.put(refdb.SlePur1.KEY_10, salepur1.getUpdatedDate().getDate());
        values.put(refdb.SlePur1.KEY_11, salepur1.getNameOfPerson());
        values.put(refdb.SlePur1.KEY_12, salepur1.getPayAfterDay());
        values.put("BillStatus", salepur1.getBillSatus());

        // insert row
        return db.insert(refdb.SlePur1.TABLE_SalePur1, null, values);
    }

    public List<Salepur1> getSalePur1Data(String query) {
        List<Salepur1> salepur1List = new ArrayList<>();
        Log.e(LOG, query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SalePur1", String.valueOf(c.moveToFirst()));
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Salepur1 salepur1 = new Salepur1();
                salepur1.setID(c.getInt(0));
                salepur1.setSalePur1ID(c.getInt(1));
                salepur1.setEntryType(c.getString(2));
                SPDate spDate = new SPDate();
                spDate.setDate(c.getString(3));
                salepur1.setSPDate(spDate);
                salepur1.setAcNameID(Integer.parseInt(c.getString(4)));
                salepur1.setRemarks(c.getString(5));
                salepur1.setClientID(c.getInt(6));
                salepur1.setClientUserID(Integer.parseInt(c.getString(7)));
                salepur1.setNetCode(c.getString(8));
                salepur1.setSysCode(c.getString(9));
                UpdatedDate updatedDate = new UpdatedDate();
                updatedDate.setDate(c.getString(10));
                salepur1.setUpdatedDate(updatedDate);
                salepur1.setNameOfPerson(c.getString(11));
                salepur1.setPayAfterDay(Integer.valueOf(c.getString(12)));


                salepur1List.add(salepur1);
            } while (c.moveToNext());
        }

        return salepur1List;
    }

    public int UpdateSalePur1Data(int pk, String salpur1id, String updateDate, @Nullable Salepur1 salepur1) {
        if (salpur1id == null && updateDate != null) {
            /////Editing While User Edit The data From Form
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(refdb.SlePur1.KEY_10, updateDate);

            int sta = database.update(refdb.SlePur1.TABLE_SalePur1,
                    values,
                    "ID = " + pk, null);
            return sta;
        } else if (salpur1id == null && updateDate == null) {
            /////Editing While User Edit The data From Form
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            //values.put(refdb.SlePur1.KEY_1, salepur1.getSalePur1ID());
            //values.put(refdb.SlePur1.KEY_2, salepur1.getEntryType());
            values.put(refdb.SlePur1.KEY_3, salepur1.getSPDate().getDate());
            values.put(refdb.SlePur1.KEY_4, salepur1.getAcNameID());
            values.put(refdb.SlePur1.KEY_5, salepur1.getRemarks());
            //values.put(refdb.SlePur1.KEY_6, salepur1.getClientID());
            //values.put(refdb.SlePur1.KEY_7, salepur1.getClientUserID());
            values.put(refdb.SlePur1.KEY_8, salepur1.getNetCode());
            values.put(refdb.SlePur1.KEY_9, salepur1.getSysCode());
            values.put(refdb.SlePur1.KEY_10, salepur1.getUpdatedDate().getDate());
            values.put(refdb.SlePur1.KEY_11, salepur1.getNameOfPerson());
            values.put(refdb.SlePur1.KEY_12, salepur1.getPayAfterDay());
            int sta = database.update(refdb.SlePur1.TABLE_SalePur1,
                    values,
                    "ID = " + salepur1.getID(), null);
            return sta;
        } else {
            //////Editing While Updating Data On Cloud
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(refdb.SlePur1.KEY_1, salpur1id);
            contentValues.put(refdb.SlePur1.KEY_10, updateDate);
            int sta = database.update(refdb.SlePur1.TABLE_SalePur1,
                    contentValues,
                    "ID = " + pk, null);
            return sta;
        }


    }

    public void deleteAllSalePur1() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + refdb.SlePur1.TABLE_SalePur1);

    }

    public String getSalePur1MaxUpdatedDate(String clientIDSession) {

        String query = "Select MAX(UpdatedDate) from SalePur1 where ClientID =" + clientIDSession;

        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS", String.valueOf(c.moveToFirst()));

        String maxDate = c.getString(0);
        Log.e("key", "SalePur1 Last UPDATED Date " + c.getString(0));

        return maxDate;
    }

    //////////////////////////////////////////////////End Of SalePur1 Functions
    ////////////////SalePur2 DAta Functions
    /////////////updateSalePur2Tablefromserver:Update Record From Server Record
    //////////that are edited on server
    public int updateSalePur2Tablefromserver(SalePur2 salePur2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(refdb.SalePur2.KEY_1, salePur2.getItem3NameID());
        values.put(refdb.SalePur2.KEY_2, salePur2.getSalePur1ID());
        values.put(refdb.SalePur2.KEY_3, salePur2.getEntryType());
        values.put(refdb.SalePur2.KEY_4, salePur2.getItemSerial());
        values.put(refdb.SalePur2.KEY_5, salePur2.getItemDescription());
        values.put(refdb.SalePur2.KEY_6, salePur2.getQtyAdd());
        values.put(refdb.SalePur2.KEY_7, salePur2.getQtyLess());
        values.put(refdb.SalePur2.KEY_8, salePur2.getQty());
        values.put(refdb.SalePur2.KEY_9, salePur2.getPrice());
        values.put(refdb.SalePur2.KEY_10, salePur2.getTotal());
        values.put(refdb.SalePur2.KEY_11, salePur2.getLocation());
        values.put(refdb.SalePur2.KEY_12, salePur2.getClientID());
        values.put(refdb.SalePur2.KEY_13, salePur2.getClientUserID());
        values.put(refdb.SalePur2.KEY_14, salePur2.getNetCode());
        values.put(refdb.SalePur2.KEY_15, salePur2.getSysCode());
        values.put(refdb.SalePur2.KEY_16, salePur2.getUpdatedDate().getDate());


        int sta = db.update(refdb.SalePur2.TABLE_SalePur2,
                values,
                "ClientID = '" + salePur2.getClientID() + "'" +
                        " AND SalePur1ID='" + salePur2.getSalePur1ID() + "'" +
                        " AND EntryType= '" + salePur2.getEntryType() + "'" +
                        " AND ItemSerial= '" + salePur2.getItemSerial() + "'"
                , null);
        return sta;
    }

    public long createSalePur2Data(SalePur2 salePur2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(refdb.SalePur2.KEY_1, salePur2.getItem3NameID());
        values.put(refdb.SalePur2.KEY_2, salePur2.getSalePur1ID());
        values.put(refdb.SalePur2.KEY_3, salePur2.getEntryType());
        values.put(refdb.SalePur2.KEY_4, salePur2.getItemSerial());
        values.put(refdb.SalePur2.KEY_5, salePur2.getItemDescription());
        values.put(refdb.SalePur2.KEY_6, salePur2.getQtyAdd());
        values.put(refdb.SalePur2.KEY_7, salePur2.getQtyLess());
        values.put(refdb.SalePur2.KEY_8, salePur2.getQty());
        values.put(refdb.SalePur2.KEY_9, salePur2.getPrice());
        values.put(refdb.SalePur2.KEY_10, salePur2.getTotal());
        values.put(refdb.SalePur2.KEY_11, salePur2.getLocation());
        values.put(refdb.SalePur2.KEY_12, salePur2.getClientID());
        values.put(refdb.SalePur2.KEY_13, salePur2.getClientUserID());
        values.put(refdb.SalePur2.KEY_14, salePur2.getNetCode());
        values.put(refdb.SalePur2.KEY_15, salePur2.getSysCode());
        values.put(refdb.SalePur2.KEY_16, salePur2.getUpdatedDate().getDate());


        // insert row
        return db.insert(refdb.SalePur2.TABLE_SalePur2, null, values);
    }

    ////////////UdateSalePur1Id in salepur2table
    public int UpdateSalePur1IdInSalePur2Table(int oldsalepurid,
                                               String entrytypee,
                                               int cliid,
                                               String newsalepur1id) {
        Log.e("ambigous",GenericConstants.AmbigousStateTEmpForOnlyThiss);
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(refdb.SalePur2.KEY_2, newsalepur1id);

        int sta = database.update(refdb.SalePur2.TABLE_SalePur2,
                contentValues,
                "SalePur1ID = " + oldsalepurid+"" +
                        " AND EntryType= '"+entrytypee+"'" +
                        " AND ClientID = "+cliid,
                null);
        Log.e(LOG, "oldid:" + oldsalepurid + " newid:" + newsalepur1id + " Status:" + sta);
        return sta;
    }

    /////////////////Update Item Serial
    public int UpdateSalePur2Data(int pk, String itemserial, String updateDate) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(refdb.SalePur2.KEY_4, itemserial);
        contentValues.put(refdb.SalePur2.KEY_16, updateDate);
        int sta = database.update(refdb.SalePur2.TABLE_SalePur2,
                contentValues,
                "ID = " + pk, null);
        return sta;

    }

    /////////////////Update Whole Object of SalePur2
    public long UpdateDataForSalePur2(String id, SalePur2 salePur2, @Nullable boolean flag) {
        if (flag) {
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(refdb.SalePur2.KEY_16, salePur2.getUpdatedDate().getDate());

            int sta = database.update(refdb.SalePur2.TABLE_SalePur2,
                    contentValues,
                    "ID = " + id, null);
            return sta;
        } else {
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(refdb.SalePur2.KEY_5, salePur2.getItemDescription());
            contentValues.put(refdb.SalePur2.KEY_8, salePur2.getQty());
            contentValues.put(refdb.SalePur2.KEY_9, salePur2.getPrice());
            contentValues.put(refdb.SalePur2.KEY_10, salePur2.getTotal());
            contentValues.put(refdb.SalePur2.KEY_16, salePur2.getUpdatedDate().getDate());

            int sta = database.update(refdb.SalePur2.TABLE_SalePur2,
                    contentValues,
                    "ID = " + id,
                    null);
            return sta;
        }

    }

    public List<SalePur2> getSalePur2Data(String query) {
        List<SalePur2> salePur2s = new ArrayList<>();
        Log.e(LOG, query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SalePur2", String.valueOf(c.moveToFirst()));
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                SalePur2 salePur2 = new SalePur2();
                salePur2.setID(c.getInt(0));
                Log.e("id", "ID=" + c.getInt(0));
                salePur2.setItem3NameID(c.getInt(1));
                salePur2.setSalePur1ID(c.getInt(2));
                salePur2.setEntryType(c.getString(3));
                salePur2.setItemSerial(c.getInt(4));
                salePur2.setItemDescription(c.getString(5));
                salePur2.setQtyAdd(c.getString(6));
                salePur2.setQtyLess(c.getString(7));
                salePur2.setQty(c.getString(8));
                salePur2.setPrice(c.getString(9));
                salePur2.setTotal(c.getString(10));
                salePur2.setLocation(c.getString(11));
                salePur2.setClientID(c.getInt(12));
                salePur2.setClientUserID(c.getInt(13));
                salePur2.setNetCode(c.getString(14));
                salePur2.setSysCode(c.getString(15));
                UpdatedDate date = new UpdatedDate();
                date.setDate(c.getString(16));
                salePur2.setUpdatedDate(date);


                salePur2s.add(salePur2);
            } while (c.moveToNext());
        }

        return salePur2s;
    }

    public void deleteAllSalePur2() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + refdb.SalePur2.TABLE_SalePur2);

    }

    public String getSalePur2MaxUpdatedDate(String clientIDSession) {

        String query = "Select MAX(UpdatedDate) from SalePur2 where ClientID =" + clientIDSession;

        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS", String.valueOf(c.moveToFirst()));

        String maxDate = c.getString(0);
        Log.e("key", "SalePur2 Last UPDATED Date " + c.getString(0));

        return maxDate;
    }

}
