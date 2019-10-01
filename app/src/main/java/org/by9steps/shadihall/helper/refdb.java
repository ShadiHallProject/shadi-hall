package org.by9steps.shadihall.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.by9steps.shadihall.model.Item1Type;

import java.util.List;

public class refdb {

    public static class TableItem1{
        //Item1Type Table - column names
        public static final String KEY_Item1TypeID = "Item1TypeID";
        public static final String KEY_ItemType = "ItemType";
        public static List<Item1Type> GetItem1TypeList(DatabaseHelper databaseHelper, String query){
             return databaseHelper.getItem1TypeData(query);
        }
        public static long AddItem1Type(DatabaseHelper databaseHelper,Item1Type item1Type){
            long id=databaseHelper.createItem1Typeitem(item1Type);
            return id;
        }
    }



}
