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
import org.by9steps.shadihall.model.BalSheet;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.ChartOfAcc;
import org.by9steps.shadihall.model.GeneralLedger;
import org.by9steps.shadihall.model.MonthTb;
import org.by9steps.shadihall.model.ProfitLoss;
import org.by9steps.shadihall.model.Recovery;
import org.by9steps.shadihall.model.Reports;
import org.by9steps.shadihall.model.Spinner;
import org.by9steps.shadihall.model.Summerize;
import org.by9steps.shadihall.model.Voucher;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getName();

    // Database Version
    private static final int DATABASE_VERSION = 8;

    // Database Name
    private static final String DATABASE_NAME = "ShadiHallUser";

    // Table Names
    private static final String TABLE_Account3Name = "Account3Name";
    private static final String TABLE_Account1Type = "Account1Type";
    private static final String TABLE_Account2Group = "Account2Group";
    private static final String TABLE_CashBook = "CashBook";
    private static final String TABLE_Booking = "Booking";
    private static final String TABLE_Client = "Client";

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
    private static final String KEY_BookingID = "BookingID";

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

    // Table Create Statements
    // Account3Name table create statement
    private static final String CREATE_TABLE_Account3Name = "CREATE TABLE "
            + TABLE_Account3Name + "(" + KEY_AcNameID + " TEXT,"
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
            + KEY_SerialNo + " TEXT,"
            + KEY_UserRights + " TEXT,"
            + KEY_SecurityRights + " TEXT,"
            + KEY_Salary + " TEXT" + ")";

    // CashBook table create statement
    private static final String CREATE_TABLE_CashBook = "CREATE TABLE "
            + TABLE_CashBook + "("+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_CashBookID + " TEXT,"
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
            + KEY_BookingID + " TEXT" + ")";

    // Booking table create statement
    private static final String CREATE_TABLE_Booking = "CREATE TABLE "
            + TABLE_Booking + "(" + KEY_BookingID3 + " TEXT,"
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
            + KEY_UpdatedDate3 + " TEXT" + ")";

    // Account2Group table create statement
    private static final String CREATE_TABLE_Account2Group = "CREATE TABLE "
            + TABLE_Account2Group + "(" + KEY_AcGroupID + " TEXT,"
            + KEY_AcTypeID + " TEXT,"
            + KEY_AcGruopName + " TEXT" + ")";

    // Account1Type table create statement
    private static final String CREATE_TABLE_Account1Type = "CREATE TABLE "
            + TABLE_Account1Type + "(" + KEY_AcTypeID + " TEXT,"
            + KEY_AcTypeName + " TEXT" + ")";

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Account3Name);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CashBook);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Booking);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Account2Group);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Account1Type);

        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE "+TABLE_CashBook +" ADD COLUMN ID PRIMARY KEY AUTOINCREMENT");
        }

        // create new tables
        onCreate(db);
    }

    /**
     * Creating a Account3Name
     */
    public void createAccount3Name(Account3Name account3Name) {
        SQLiteDatabase db = this.getWritableDatabase();

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


        // insert row
        db.insert(TABLE_Account3Name, null, values);

    }

    /**
     * Deleting a Account3Name
     */
    public void deleteAccount3Name() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_Account3Name);
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
        values.put(KEY_BookingID, cashBook.getBookingID());

        Log.e("Values",values.toString());

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
        db.execSQL("DELETE FROM "+ TABLE_CashBook);
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
        values.put(KEY_BookingID, bookings.getBookingID());
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

        // insert row
        db.insert(TABLE_Booking, null, values);
    }

    /**
     * Deleting a Booking
     */
    public void deleteBooking() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_Booking);
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
        db.execSQL("DELETE FROM "+ TABLE_Account2Group);
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
        db.execSQL("DELETE FROM "+ TABLE_Account1Type);
    }

    //Get GeneralLedger

    public List<GeneralLedger> getGeneralLedger(String query) {
        List<GeneralLedger> mGeneralLedger = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS",String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                GeneralLedger generalLedger = new GeneralLedger();
                Log.e("SSSSS",c.getString(c.getColumnIndex("ClientID")));
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
        Log.e("SSSS",String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Bookings generalLedger = new Bookings();
                Log.e("SSSSS",c.getString(c.getColumnIndex("EventDate")));
                generalLedger.setBookingID(c.getString(c.getColumnIndex("BookingID")));
                generalLedger.setClientName(c.getString(c.getColumnIndex("ClientName")));
                generalLedger.setClientMobile(c.getString(c.getColumnIndex("ClientMobile")));
                generalLedger.setClientAddress(c.getString(c.getColumnIndex("ClientAddress")));
                generalLedger.setClientNic(c.getString(c.getColumnIndex("ClientNic")));
                generalLedger.setEventName(c.getString(c.getColumnIndex("EventName")));
                generalLedger.setBookingDate(c.getString(c.getColumnIndex("BookingDate")));
                generalLedger.setEventDate(c.getString(c.getColumnIndex("EventDate")));
                generalLedger.setChargesTotal(c.getString(c.getColumnIndex("ChargesTotal")));
                generalLedger.setDescription(c.getString(c.getColumnIndex("Description")));
                generalLedger.setClientID(c.getString(c.getColumnIndex("ClientID")));
                generalLedger.setClientUserID(c.getString(c.getColumnIndex("ClientUserID")));

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
        Log.e("SSSS",String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Recovery generalLedger = new Recovery();
                Log.e("SSSSS",c.getString(c.getColumnIndex("BookingID")));

                generalLedger.setClientID(c.getString(c.getColumnIndex("ClientID")));
                generalLedger.setBookingID(c.getString(c.getColumnIndex("BookingID")));
                generalLedger.setRecieved(c.getString(c.getColumnIndex("Recieved")));
                generalLedger.setExpensed(c.getString(c.getColumnIndex("Expensed")));
                generalLedger.setChargesTotal(c.getString(c.getColumnIndex("ChargesTotal")));
                generalLedger.setBalance(c.getString(c.getColumnIndex("Balance")));
                generalLedger.setProfit(c.getString(c.getColumnIndex("Profit")));
                generalLedger.setEventName(c.getString(c.getColumnIndex("EventName")));
                generalLedger.setEventDate(c.getString(c.getColumnIndex("EventDate")));
                generalLedger.setClientName(c.getString(c.getColumnIndex("ClientName")));;

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
        Log.e("CASHBOOKID",String.valueOf(c.getColumnName(0)));

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
                cashBook.setBookingID(c.getString(c.getColumnIndex("BookingID")));
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
        Log.e("SSSS",String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Reports reports = new Reports();
                Log.e("SSSSS",c.getString(c.getColumnIndex("AccountID")));

                reports.setAccountID(c.getString(c.getColumnIndex("AccountID")));
                reports.setDebit(c.getString(c.getColumnIndex("Debit")));
                reports.setCredit(c.getString(c.getColumnIndex("Credit")));
                reports.setBal(c.getString(c.getColumnIndex("Bal")));
                reports.setDebitBal(c.getString(c.getColumnIndex("DebitBal")));
                reports.setCreditBal(c.getString(c.getColumnIndex("CreditBal")));
                reports.setAcName(c.getString(c.getColumnIndex("AcName")));
                reports.setAcGroupID(c.getString(c.getColumnIndex("AcGroupID")));

                // adding to todo list
                mReports.add(reports);
            } while (c.moveToNext());
        }

        return mReports;
    }

    //Get Entry SummerizeTB
    public List<Summerize> getSummerizeTB(String query) {
        List<Summerize> mSummerize = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS",String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Summerize summerize = new Summerize();
                Log.e("SSSSS",c.getString(c.getColumnIndex("AcTypeID")));

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
        Log.e("SSSS",String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                MonthTb monthTb = new MonthTb();
                Log.e("SSSSS",c.getString(c.getColumnIndex("AcTypeID")));

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
        Log.e("SSSS",String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Account2Group account2Group = new Account2Group();
                Log.e("SSSSS",c.getString(c.getColumnIndex("AcGroupID")));

                account2Group.setAcGroupID(c.getString(c.getColumnIndex("AcGroupID")));
                account2Group.setAcTypeID(c.getString(c.getColumnIndex("AcTypeID")));
                account2Group.setAcGruopName(c.getString(c.getColumnIndex("AcGruopName")));

                // adding to todo list
                mAccount2Groups.add(account2Group);
            } while (c.moveToNext());
        }

        return mAccount2Groups;
    }

    //Get Entry Account1Type
    public List<Account1Type> getAccount1Type(String query) {
        List<Account1Type> mAccount1Type = new ArrayList<>();


        Log.e(LOG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        Log.e("SSSS",String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Account1Type account1Type = new Account1Type();
                Log.e("SSSSS",c.getString(c.getColumnIndex("AcTypeID")));

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
        Log.e("SSSS",String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ProfitLoss profitLoss = new ProfitLoss();
                Log.e("SSSSS",c.getString(c.getColumnIndex("CBDate")));

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
        Log.e("SSSS",String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                BalSheet balSheet = new BalSheet();
                Log.e("SSSSS",c.getString(c.getColumnIndex("CBDate")));

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
        Log.e("SSSS",String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ChartOfAcc chartOfAcc = new ChartOfAcc();
                Log.e("SSSSS",c.getString(c.getColumnIndex("AcTypeID")));

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
        Log.e("CashBook",String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                CashBook cashBook = new CashBook();
                Log.e("CashBookIDDD",c.getString(c.getColumnIndex(KEY_ID)));

                cashBook.setcId(c.getString(c.getColumnIndex(KEY_ID)));
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
                cashBook.setBookingID(c.getString(c.getColumnIndex(KEY_BookingID)));

                // adding to todo list
                cashBooks.add(cashBook);
            } while (c.moveToNext());
        }

        return cashBooks;
    }

    public String getAccountName(String query){
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

    public List<Spinner> getSpinnerAcName(String query){
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
        Log.e("CashBook",String.valueOf(c.moveToFirst()));

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Voucher voucher = new Voucher();
                Log.e("CashBookIDDD",c.getString(c.getColumnIndex("CashBookID")));

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
}
