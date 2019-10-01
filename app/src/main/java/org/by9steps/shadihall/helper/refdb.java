package org.by9steps.shadihall.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.by9steps.shadihall.model.Item1Type;

import java.util.List;

public class refdb {

    public static class TableItem1 {
        //Item1Type Table - column names
        public static final String KEY_Item1TypeID = "Item1TypeID";
        public static final String KEY_ItemType = "ItemType";

        public static List<Item1Type> GetItem1TypeList(DatabaseHelper databaseHelper, String query) {
            return databaseHelper.getItem1TypeData(query);
        }

        public static long AddItem1Type(DatabaseHelper databaseHelper, Item1Type item1Type) {
            long id = databaseHelper.createItem1Typeitem(item1Type);
            return id;
        }
    }

    public static class Table2Group {
        public static final String TABLE_Item2Group = "Item2Group";

        public static final String KEY_1 = "Item2GroupID";
        public static final String KEY_2= "Item1TypeID";
        public static final String KEY_3 = "Item2GroupName";
        public static final String KEY_4 = "ClientID";
        public static final String KEY_5 = "ClientUserID";
        public static final String KEY_6 = "NetCode";
        public static final String KEY_7 = "SysCode";
        public static final String KEY_8 = "UpdatedDate";
    }
    
    public static class Table3Name{
        public static final String TABLE_Item3Name = "Item3Name";


        public static final String KEY_1 = "Item3NameID";
        public static final String KEY_2 = "Item2GroupID";
        public static final String KEY_3 = "ItemName";
        public static final String KEY_4= "ClientID";
        public static final String KEY_5 = "ClientUserID";
        public static final String KEY_6 = "NetCode";
        public static final String KEY_7 = "SysCode";
        public static final String KEY_8 = "SalePrice";
        public static final String KEY_9 = "ItemCode";
        public static final String KEY_10 = "Stock";
        public static final String KEY_11 = "UpdatedDate";
        
    }


}
