package org.by9steps.shadihall.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.by9steps.shadihall.model.CashBook;

import java.util.ArrayList;
import java.util.List;


public class DBManager {

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;
    Context context;

    public DBManager(Context context) {
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

    public long insertItem(CashBook item) {

        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_ClientID, item.getClientID());
        values.put(DBHelper.KEY_ClientUserID, item.getClientUserID());
        values.put(DBHelper.KEY_NetCode, item.getNetCode());
        values.put(DBHelper.KEY_SysCode, item.getSysCode());
        values.put(DBHelper.KEY_UpdatedDate, item.getUpdatedDate());
        values.put(DBHelper.KEY_CBDate, item.getCBDate());
        values.put(DBHelper.KEY_CreditAccount, item.getCreditAccount());
        values.put(DBHelper.KEY_DebitAccount, item.getDebitAccount());
        values.put(DBHelper.KEY_CBRemarks, item.getCBRemarks());
        values.put(DBHelper.KEY_Amount, item.getAmount());


        long id = database.insert(DBHelper.TABLE_CASH, null, values);
        return id;
    }

    public void update(CashBook item, int id) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.KEY_ClientID, item.getClientID());
        values.put(DBHelper.KEY_ClientUserID, item.getClientUserID());
        values.put(DBHelper.KEY_NetCode, item.getNetCode());
        values.put(DBHelper.KEY_SysCode, item.getSysCode());
        values.put(DBHelper.KEY_UpdatedDate, item.getUpdatedDate());
        values.put(DBHelper.KEY_CBDate, item.getCBDate());
        values.put(DBHelper.KEY_CreditAccount, item.getCreditAccount());
        values.put(DBHelper.KEY_DebitAccount, item.getDebitAccount());
        values.put(DBHelper.KEY_CBRemarks, item.getCBRemarks());
        values.put(DBHelper.KEY_Amount, item.getAmount());

        database.update(DBHelper.TABLE_CASH, values, DBHelper.KEY_ID + " = " + id, null);
    }

    public List<CashBook> getAllItem() {

        List<CashBook> itemList = new ArrayList<>();
        String select = "SELECT * FROM " + DBHelper.TABLE_CASH;
        Cursor cursor = database.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                CashBook item = new CashBook();
                item.id = cursor.getInt(0);
                item.ClientID = cursor.getString(1);
                item.ClientUserID = cursor.getString(2);
                item.NetCode = cursor.getString(3);
                item.SysCode = cursor.getString(4);
                item.UpdatedDate = cursor.getString(5);
                item.CBDate = cursor.getString(6);
                item.DebitAccount = cursor.getString(7);
                item.CreditAccount = cursor.getString(8);
                item.CBRemarks = cursor.getString(9);
                item.Amount = cursor.getString(10);


                Log.i("@ANWAR", item.getClientUserID() + "");
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return itemList;
    }

    public int remove(int id) {
        return database.delete(DBHelper.TABLE_CASH, DBHelper.KEY_ID + " = " + id, null);
    }


}
