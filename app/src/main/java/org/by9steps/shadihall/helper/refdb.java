package org.by9steps.shadihall.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.by9steps.shadihall.model.Item1Type;
import org.by9steps.shadihall.model.Item2Group;
import org.by9steps.shadihall.model.item3name.Item3Name_;
import org.by9steps.shadihall.model.salepur1data.Salepur1;
import org.by9steps.shadihall.model.salepur2data.SalePur2;

import java.util.List;

public class refdb {

    public static class TableItem1 {
        public static final String TABLE_Item1Type = "Item1Type";

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
        public static final String KEY_2 = "Item1TypeID";
        public static final String KEY_3 = "Item2GroupName";
        public static final String KEY_4 = "ClientID";
        public static final String KEY_5 = "ClientUserID";
        public static final String KEY_6 = "NetCode";
        public static final String KEY_7 = "SysCode";
        public static final String KEY_8 = "UpdatedDate";
        public static long AddItem2Group(DatabaseHelper databaseHelper, Item2Group item2Group) {
            long id = databaseHelper.createItem2GroupData(item2Group);
            return id;
        }

    }

    public static class Table3Name {
        public static final String TABLE_Item3Name = "Item3Name";


        public static final String KEY_1 = "Item3NameID";
        public static final String KEY_2 = "Item2GroupID";
        public static final String KEY_3 = "ItemName";
        public static final String KEY_4 = "ClientID";
        public static final String KEY_5 = "ClientUserID";
        public static final String KEY_6 = "NetCode";
        public static final String KEY_7 = "SysCode";
        public static final String KEY_8 = "SalePrice";
        public static final String KEY_9 = "ItemCode";
        public static final String KEY_10 = "Stock";
        public static final String KEY_11 = "UpdatedDate";
        public static long AddItem3Name(DatabaseHelper databaseHelper, Item3Name_ item3Name_) {
            long id = databaseHelper.createItem3NameData(item3Name_);
            return id;
        }


    }

    public static class SlePur1 {
        public static final String TABLE_SalePur1 = "SalePur1";

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
        public static final String KEY_1 = "SalePur1ID";
        //public static final String KEY_2 = "SerialNo";
        public static final String KEY_2 = "EntryType";
        public static final String KEY_3 = "SPDate";
        public static final String KEY_4 = "AcNameID";
        public static final String KEY_5 = "Remarks";
        public static final String KEY_6 = "ClientID";
        public static final String KEY_7 = "ClientUserID";
        public static final String KEY_8 = "NetCode";
        public static final String KEY_9 = "SysCode";
        public static final String KEY_10 = "UpdatedDate";
        public static final String KEY_11 = "NameOfPerson";
        public static final String KEY_12 = "PayAfterDay";
        public static long AddItemSalePur1(DatabaseHelper databaseHelper, Salepur1 salepur1) {
            long id = databaseHelper.createSalePur1Data(salepur1);
            return id;
        }
        public static int GetMaximumID(DatabaseHelper databaseHelper) {
            int id = databaseHelper.GetMaximunSalePur1ID();
            return id;
        }
    }

    public static class SalePur2 {
        public static final String TABLE_SalePur2 = "SalePur2";

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
        public static final String KEY_1 = "Item3NameID";
        public static final String KEY_2 = "SalePur1ID";
        public static final String KEY_3 = "EntryType";
        public static final String KEY_4 = "ItemSerial";
        public static final String KEY_5 = "ItemDescription";
        public static final String KEY_6 = "QtyAdd";
        public static final String KEY_7 = "QtyLess";
        public static final String KEY_8 = "Qty";
        public static final String KEY_9 = "Price";
        public static final String KEY_10 = "Total";
        public static final String KEY_11 = "Location";
        public static final String KEY_12 = "ClientID";
        public static final String KEY_13 = "ClientUserID";
        public static final String KEY_14 = "NetCode";
        public static final String KEY_15 = "SysCode";
        public static final String KEY_16 = "UpdatedDate";
        public static long AddItemSalePur2(DatabaseHelper databaseHelper, org.by9steps.shadihall.model.salepur2data.SalePur2 salePur2) {
            long id = databaseHelper.createSalePur2Data(salePur2);
            return id;
        }
    }


}
