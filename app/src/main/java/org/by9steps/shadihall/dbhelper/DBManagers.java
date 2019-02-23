package org.by9steps.shadihall.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.by9steps.shadihall.model.AccountName3;

import java.util.ArrayList;
import java.util.List;


public class DBManagers {

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;
    Context context;

    public DBManagers(Context context) {
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

    public long insertAccountName3(AccountName3 item) {

        ContentValues values = new ContentValues();

        values.put(DBHelper.KEY_ClientID, item.getClientID());
        values.put(DBHelper.KEY_NetCode, item.getNetCode());
        values.put(DBHelper.KEY_SysCode, item.getSysCode());
        values.put(DBHelper.KEY_UpdatedDate, item.getUpdatedDate());
        values.put(DBHelper.KEY_GroupId, item.getAsGroupID());

        values.put(DBHelper.KEY_AcName, item.getAcName());
        values.put(DBHelper.KEY_AcAddress, item.getAcAddress());
        values.put(DBHelper.KEY_AcMobileNo, item.getAcMobileNo());
        values.put(DBHelper.KEY_AcEmailAddress, item.getAcEmailAddress());
        values.put(DBHelper.KEY_Salary, item.getSalary());

        values.put(DBHelper.KEY_AcContactNo, item.getAcContactNo());
        values.put(DBHelper.KEY_AcPassword, item.getAcPassword());
        values.put(DBHelper.KEY_UserRights, item.getUserRights());


        long id = database.insert(DBHelper.TABLE_ACCOUNT3, null, values);
        return id;
    }

    public void updateAccountName3(AccountName3 item, int id) {

        ContentValues values = new ContentValues();

        values.put(DBHelper.KEY_ClientID, item.getClientID());
        values.put(DBHelper.KEY_NetCode, item.getNetCode());
        values.put(DBHelper.KEY_SysCode, item.getSysCode());
        values.put(DBHelper.KEY_UpdatedDate, item.getUpdatedDate());
        values.put(DBHelper.KEY_GroupId, item.getAsGroupID());

        values.put(DBHelper.KEY_AcName, item.getAcName());
        values.put(DBHelper.KEY_AcAddress, item.getAcAddress());
        values.put(DBHelper.KEY_AcMobileNo, item.getAcMobileNo());
        values.put(DBHelper.KEY_AcEmailAddress, item.getAcEmailAddress());
        values.put(DBHelper.KEY_Salary, item.getSalary());

        values.put(DBHelper.KEY_AcContactNo, item.getAcContactNo());
        values.put(DBHelper.KEY_AcPassword, item.getAcPassword());
        values.put(DBHelper.KEY_UserRights, item.getUserRights());

        database.update(DBHelper.TABLE_ACCOUNT3, values, DBHelper.KEY_ID + " = " + id, null);

    }

    public List<AccountName3> getAccountName3() {

        List<AccountName3> itemList = new ArrayList<>();
        String select = "SELECT * FROM " + DBHelper.TABLE_ACCOUNT3;
        Cursor cursor = database.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                AccountName3 item = new AccountName3();
                item.id = cursor.getInt(0);
                item.ClientID = cursor.getString(1);
                item.NetCode = cursor.getString(2);
                item.SysCode = cursor.getString(3);
                item.UpdatedDate = cursor.getString(4);
                item.AsGroupID = cursor.getString(5);

                item.AcName = cursor.getString(6);
                item.AcAddress = cursor.getString(7);
                item.AcMobileNo = cursor.getString(8);
                item.AcEmailAddress = cursor.getString(9);
                item.Salary = cursor.getString(10);

                item.AcContactNo = cursor.getString(11);
                item.AcPassword = cursor.getString(12);
                item.UserRights = cursor.getString(13);


                Log.i("@ANWAR", item.getAsGroupID() + "");
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return itemList;
    }

    public int removeAccountName3(int id) {
        return database.delete(DBHelper.TABLE_ACCOUNT3, DBHelper.KEY_ID + " = " + id, null);
    }
}
