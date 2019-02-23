package org.by9steps.shadihall.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.by9steps.shadihall.model.Booking;

import java.util.ArrayList;
import java.util.List;



public class BookingManager {

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;
    Context context;

    public BookingManager(Context context) {
        this.context = context;
        dbhelper = new DBHelper(context);
    }


    public void open() {
        Log.e("DB", "Database open!");
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        Log.e("DB", "Database close!");
        dbhelper.close();
    }


    public long insertBooking(Booking item) {
        database.beginTransaction();
        ContentValues values = new ContentValues();

     //   values.put(DBHelper.KEY_ID, item.getId());
        values.put(DBHelper.KEY_BOOKING_ID, item.getBook_id());
        values.put(DBHelper.KEY_Client_ID, item.getClientID());
        values.put(DBHelper.KEY_CLient_User_ID, item.getClientUserID());
        values.put(DBHelper.KEY_Net_Code, item.getNetCode());
        values.put(DBHelper.KEY_Sys_Code, item.getSysCode());
        values.put(DBHelper.KEY_Update_Date, item.getUpdateDate());
        values.put(DBHelper.KEY_BOOKING_Date, item.getBookingDate());
        values.put(DBHelper.KEY_EVENT_Date, item.getEventDate());

        values.put(DBHelper.KEY_EVENT_NAME, item.getEventName());
        values.put(DBHelper.KEY_ClientName, item.getClientName());
        values.put(DBHelper.KEY_ClientAddress, item.getClientAddress());
        values.put(DBHelper.KEY_ClientMobileNo, item.getClientMobile());
        values.put(DBHelper.KEY_ClientNICAddress, item.getClientCNIC());
        values.put(DBHelper.KEY_Charges, item.getCharges());
        values.put(DBHelper.KEY_Arrange_person, item.getTotalPersons());
        values.put(DBHelper.KEY_Description, item.getDescription());


        long id = database.insert(DBHelper.TABLE_BOOKING, null, values);//, SQLiteDatabase.CONFLICT_REPLACE);
        database.setTransactionSuccessful();
        database.endTransaction();
        return id;
    }

    public void updateBooking(Booking item, int id) {

        ContentValues values = new ContentValues();


        values.put(DBHelper.KEY_ID, item.getId());
        values.put(DBHelper.KEY_BOOKING_ID, item.getBook_id());
        values.put(DBHelper.KEY_ClientID, item.getClientID());
        values.put(DBHelper.KEY_ClientUserID, item.getClientUserID());
        values.put(DBHelper.KEY_NetCode, item.getNetCode());
        values.put(DBHelper.KEY_SysCode, item.getSysCode());
        values.put(DBHelper.KEY_UpdatedDate, item.getUpdateDate());
        values.put(DBHelper.KEY_BOOKING_Date, item.getBookingDate());
        values.put(DBHelper.KEY_EVENT_Date, item.getEventDate());

        values.put(DBHelper.KEY_EVENT_NAME, item.getEventName());
        values.put(DBHelper.KEY_ClientName, item.getClientName());
        values.put(DBHelper.KEY_ClientAddress, item.getClientAddress());
        values.put(DBHelper.KEY_ClientMobileNo, item.getClientMobile());
        values.put(DBHelper.KEY_ClientNICAddress, item.getClientCNIC());
        values.put(DBHelper.KEY_Charges, item.getCharges());
        values.put(DBHelper.KEY_Arrange_person, item.getTotalPersons());
        values.put(DBHelper.KEY_Description, item.getDescription());


        database.update(DBHelper.TABLE_BOOKING, values, DBHelper.KEY_ID + " = " + id, null);

    }

    public List<Booking> getBooking() {
        List<Booking> itemList = new ArrayList<>();
        String select = "SELECT * FROM " + DBHelper.TABLE_BOOKING;
        Cursor cursor = database.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                Booking item = new Booking();
                item.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID)));
                item.setBook_id(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_BOOKING_ID)));
                item.setClientID(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_Client_ID)));
                item.setClientUserID(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_CLient_User_ID)));
                item.setNetCode(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_Net_Code)));
                item.setSysCode(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_Sys_Code)));
                item.setUpdateDate(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_Update_Date)));
                item.setBookingDate(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_BOOKING_Date)));
                item.setEventDate(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_EVENT_Date)));
                item.setEventName(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_EVENT_NAME)));
                item.setClientName(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ClientName)));
                item.setClientAddress(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ClientAddress)));
                item.setClientMobile(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ClientMobileNo)));
                item.setClientCNIC(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ClientNICAddress)));
                item.setCharges(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_Charges)));
                item.setTotalPersons(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_Arrange_person)));
                item.setDescription(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_Description)));

                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return itemList;
    }

    public int removeBooking(int id) {
        return database.delete(DBHelper.TABLE_BOOKING, DBHelper.KEY_ID + " = " + id, null);
    }

    public void deleteAllBooking() {
        database.execSQL("delete from " + DBHelper.TABLE_BOOKING);
    }

    public long insertRequest(Booking item) {

        ContentValues values = new ContentValues();

//        values.put(DBHelper.KEY_ID, item.getId());
        values.put(DBHelper.KEY_BOOKING_ID, item.getBook_id());
        values.put(DBHelper.KEY_ClientID, item.getClientID());
        values.put(DBHelper.KEY_ClientUserID, item.getClientUserID());
        values.put(DBHelper.KEY_NetCode, item.getNetCode());
        values.put(DBHelper.KEY_SysCode, item.getSysCode());
        values.put(DBHelper.KEY_UpdatedDate, item.getUpdateDate());
        values.put(DBHelper.KEY_BOOKING_Date, item.getBookingDate());
        values.put(DBHelper.KEY_EVENT_Date, item.getEventDate());

        values.put(DBHelper.KEY_EVENT_NAME, item.getEventName());
        values.put(DBHelper.KEY_ClientName, item.getClientName());
        values.put(DBHelper.KEY_ClientAddress, item.getClientAddress());
        values.put(DBHelper.KEY_ClientMobileNo, item.getClientMobile());
        values.put(DBHelper.KEY_ClientNICAddress, item.getClientCNIC());
        values.put(DBHelper.KEY_Charges, item.getCharges());
        values.put(DBHelper.KEY_Arrange_person, item.getTotalPersons());
        values.put(DBHelper.KEY_Description, item.getDescription());


        long id = database.insert(DBHelper.TABLE_REQUEST, null, values);
        return id;
    }

    public void updateRequest(Booking item, int id) {

        ContentValues values = new ContentValues();

        values.put(DBHelper.KEY_ID, item.getId());
        values.put(DBHelper.KEY_BOOKING_ID, item.getBook_id());
        values.put(DBHelper.KEY_ClientID, item.getClientID());
        values.put(DBHelper.KEY_ClientUserID, item.getClientUserID());
        values.put(DBHelper.KEY_NetCode, item.getNetCode());
        values.put(DBHelper.KEY_SysCode, item.getSysCode());
        values.put(DBHelper.KEY_UpdatedDate, item.getUpdateDate());
        values.put(DBHelper.KEY_BOOKING_Date, item.getBookingDate());
        values.put(DBHelper.KEY_EVENT_Date, item.getEventDate());

        values.put(DBHelper.KEY_EVENT_NAME, item.getEventName());
        values.put(DBHelper.KEY_ClientName, item.getClientName());
        values.put(DBHelper.KEY_ClientAddress, item.getClientAddress());
        values.put(DBHelper.KEY_ClientMobileNo, item.getClientMobile());
        values.put(DBHelper.KEY_ClientNICAddress, item.getClientCNIC());
        values.put(DBHelper.KEY_Charges, item.getCharges());
        values.put(DBHelper.KEY_Arrange_person, item.getTotalPersons());
        values.put(DBHelper.KEY_Description, item.getDescription());

        database.update(DBHelper.TABLE_REQUEST, values, DBHelper.KEY_ID + " = " + id, null);

    }

    public List<Booking> getRequests() {

        List<Booking> itemList = new ArrayList<>();
        String select = "SELECT * FROM " + DBHelper.TABLE_REQUEST;
        Cursor cursor = database.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                Booking item = new Booking();
                item.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID)));
                item.setBook_id(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_BOOKING_ID)));
                item.setClientID(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ClientID)));
                item.setClientUserID(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ClientUserID)));
                item.setNetCode(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NetCode)));
                item.setSysCode(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_SysCode)));
                item.setUpdateDate(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_UpdatedDate)));
                item.setBookingDate(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_BOOKING_Date)));
                item.setEventDate(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_EVENT_Date)));
                item.setEventName(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_EVENT_NAME)));
                item.setClientName(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ClientName)));
                item.setClientAddress(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ClientAddress)));
                item.setClientMobile(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ClientMobileNo)));
                item.setClientCNIC(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ClientNICAddress)));
                item.setCharges(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_Charges)));
                item.setTotalPersons(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_Arrange_person)));
                item.setDescription(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_Description)));

                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return itemList;
    }

    public int removeRequest(int id) {
        return database.delete(DBHelper.TABLE_REQUEST, DBHelper.KEY_ID + " = " + id, null);
    }

}
