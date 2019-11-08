package org.by9steps.shadihall.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.by9steps.shadihall.model.CashBook;
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

        public static List<Item2Group> GetItem2GroupList(DatabaseHelper databaseHelper, String query) {
            return databaseHelper.getItem2GroupData(query);
        }

        public static int UpdateItem2Group(DatabaseHelper databaseHelper, Item2Group item2Group, int updatetype) {
            int idd = databaseHelper.UpdateItem2GroupTable(item2Group, updatetype);
            return idd;
        }

        public static String getMaxDAtaFromitem2Group(DatabaseHelper databaseHelper, String clientID, int type) {
            return databaseHelper.getMaxIDItem2Group(type, clientID);

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

        public static long UpdatedItem3GroupID(DatabaseHelper databaseHelper, String oldid, Item3Name_ item3Name_) {
            return databaseHelper.UpdateItem3NameTable(item3Name_, oldid, 1);
        }

        public static long UpdatedItem3NameDateID(DatabaseHelper databaseHelper, String oldid, Item3Name_ item3Name_) {
            return databaseHelper.UpdateItem3NameTable(item3Name_, oldid, 2);
        }

        //////////////////////////////UpdateOnlyDate In DB
        //public static long UpdatedItem3NameDate(DatabaseHelper databaseHelper,String oldid,Item3Name_ item3Name_){
//            return databaseHelper.UpdateItem3NameTable(item3Name_,oldid,3);
//        }
        public static int getmaxItem3NameID(DatabaseHelper databaseHelper, String cliid) {
            return databaseHelper.getMaxValueOfItem3Name(cliid);
        }

        public static String getmaxUpdateItem3Name(DatabaseHelper databaseHelper, String cliid) {
            return databaseHelper.getMaxUpDateOfItem3Name(cliid);
        }

        public static int UpdateTheWholeObject(DatabaseHelper databaseHelper, String oldid, Item3Name_ item3Name_) {
            return databaseHelper.UpdateItem3NameTable(item3Name_, oldid, 4);
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

        public static int GetMaximumID(DatabaseHelper databaseHelper, String ClientID, String entryType) {
            int id = databaseHelper.GetMaximunSalePur1ID(ClientID, entryType);
            return id;
        }

        public static List<Salepur1> GetSalePur1Data(DatabaseHelper databaseHelper, String query) {

            List<Salepur1> list = databaseHelper.getSalePur1Data(query);
            return list;

        }

        //////////////UpdateDataFromServerToSqlite that is edited on server
        public static int updateSalepur1fromServer(DatabaseHelper databaseHelper, Salepur1 salepur1) {
            return databaseHelper.updateSalePur1Tablefromserver(salepur1);
        }

        public static int updateSalePur1IdinCashbook(DatabaseHelper databaseHelper,
                                                     String cliid,
                                                     String entryType,
                                                     String oldsalepurid,
                                                     String newsalepur1id) {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();


            ContentValues contentValues = new ContentValues();
            contentValues.put("UpdatedDate",GenericConstants.NullFieldStandardText);

            contentValues.put("TableID", newsalepur1id);
            Log.e("cashbkstatusb", "Click:" + cliid + " entrtype:" + entryType +
                    " odlid:" + oldsalepurid + " newID:" + newsalepur1id + "updated Statu:");
            entryType = "SalePur1_".concat(entryType);


            int status = db.update("CashBook",
                    contentValues,
                    "ClientID = " + cliid + "" +
                            " AND TableName = '" + entryType + "'" +
                            " AND TableID = " + oldsalepurid + "",
                    null);
            Log.e("cashbkstatusa", "Click:" + cliid + " entrtype:" + entryType +
                    " odlid:" + oldsalepurid + " newID:" + newsalepur1id + "updated Statu:" + status);
            return status;

        }

        ////////////////////Update ACNameID in Salepur1
        public static int updateAcNameIDFromAc3NameTable(DatabaseHelper databaseHelper,
                                                         String oldid,
                                                         String clientid,
                                                         String newid) {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();


            ContentValues contentValues = new ContentValues();
            contentValues.put("AcNameID", newid);
            contentValues.put("UpdatedDate",GenericConstants.NullFieldStandardText);


            int status = db.update("SalePur1",
                    contentValues,
                    "ClientID = " + clientid + "" +
                            " AND AcNameID = '" + oldid + "'",
                    null);

            return status;
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
        //////////5-8-9-10
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

        public static List<org.by9steps.shadihall.model.salepur2data.SalePur2> GetSalePurItemList(DatabaseHelper databaseHelper, String query) {
            return databaseHelper.getSalePur2Data(query);
        }

        //////////////UpdateDataFromServerToSqlite that is edited on server
        public static int updateSalepur2fromServer(DatabaseHelper databaseHelper, org.by9steps.shadihall.model.salepur2data.SalePur2 salePur2) {
            return databaseHelper.updateSalePur2Tablefromserver(salePur2);
        }

        public static long updateItem3NameidInSalePur2(DatabaseHelper databaseHelper, String cliid, String olda3namid, String newid) {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(refdb.SalePur2.KEY_1, newid);
            values.put("UpdatedDate",GenericConstants.NullFieldStandardText);


            int sta = db.update(refdb.SalePur2.TABLE_SalePur2,
                    values,
                    "ClientID = '" + cliid + "'" +
                            " AND Item3NameID= " + olda3namid
                    , null);
            return sta;
        }

    }

    public static class CashBookTableRef {
        public static final String TABLE_Name_CashBook = "CashBook";

        public static final String KEY_ID = "ID";
        public static final String KEY_CashBookID = "CashBookID";
        public static final String KEY_CBDate = "CBDate";
        public static final String KEY_DebitAccount = "DebitAccount";
        public static final String KEY_CreditAccount = "CreditAccount";
        public static final String KEY_CBRemarks = "CBRemarks";
        public static final String KEY_Amount = "Amount";
        public static final String KEY_ClientID2 = "ClientID";
        public static final String KEY_ClientUserID2 = "ClientUserID";
        public static final String KEY_NetCode2 = "NetCode";
        public static final String KEY_SysCode2 = "SysCode";
        public static final String KEY_UpdatedDate2 = "UpdatedDate";
        public static final String KEY_TableID = "TableID";
        public static final String KEY_SerialNo2 = "SerialNo";
        public static final String KEY_TableName = "TableName";

        public static String calcGrandTotal(DatabaseHelper databaseHelper, String salepur1id, String cliid, String EntryType) {
            String grandtotal = "0";
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            String qq = "SELECT SUM(Ifnull(Total,0))\n" +
                    "FROM SalePur2 where ClientID =" + cliid + "" +
                    " AND SalePur1ID = '" + salepur1id + "'" +
                    " AND EntryType = '" + EntryType + "'";

            Cursor cc = db.rawQuery(qq, null);
            if (cc.getCount() > 0) {
                cc.moveToFirst();
                grandtotal = cc.getString(0);
            }
            Log.e("grandtotal", grandtotal);
            return grandtotal;

        }

        public static int getmaxCashBookID(DatabaseHelper databaseHelper, String cliid) {

            String qq = "select -(Max(Abs(Ifnull(CashBookID,0)))+1) from CashBook " +
                    " where ClientID=" + cliid + "";
            Log.e("TEMP", "(" + qq);
            SQLiteDatabase dbtem = databaseHelper.getReadableDatabase();
            Cursor ctem = dbtem.rawQuery(qq, null);
            ctem.moveToFirst();


            return ctem.getInt(0);
        }

        public static long updateCashBook(DatabaseHelper databaseHelper, String pkid, String amnt) {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("Amount", amnt);
            contentValues.put("UpdatedDate",GenericConstants.NullFieldStandardText);

            return db.update("CashBook",
                    contentValues,
                    "ID=" + pkid, null);
        }

        public static long updateCashBookwholeObject(DatabaseHelper databaseHelper, CashBook cashBook) {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_CashBookID, cashBook.getCashBookID());
            values.put(KEY_CBDate, cashBook.getCBDate());
            values.put(KEY_DebitAccount, cashBook.getDebitAccount());
            values.put(KEY_CreditAccount, cashBook.getCreditAccount());
            values.put(KEY_CBRemarks, cashBook.getCBRemarks());
            values.put(KEY_Amount, cashBook.getAmount());
            values.put(KEY_ClientID2, cashBook.getClientID());
            values.put(KEY_ClientUserID2, cashBook.getCashBookID());
            values.put(KEY_NetCode2, cashBook.getNetCode());
            values.put(KEY_SysCode2, cashBook.getSysCode());
            values.put(KEY_UpdatedDate2, cashBook.getUpdatedDate());
            values.put(KEY_TableID, cashBook.getTableID());
            values.put(KEY_SerialNo2, cashBook.getSerialNo());
            values.put(KEY_TableName, cashBook.getTableName());

            ///////////////Splitting to get EntryType
//            String ss[]=cashBook.getTableName().split("_");
//            String entrytyp="";
//            if(ss.length>0){
//                entrytyp=ss[1];
//            }
            // insert row
            long cid = db.update(TABLE_Name_CashBook, values, "" +
                            " TableID = '" + cashBook.getTableID() + "'" +
                            " AND TableName='" + cashBook.getTableName() + "'" +
                            " AND ClientID = " + cashBook.getClientID(),
                    null);


//        Log.e("OKK",String.valueOf(db.insert(TABLE_CashBook, null, values)));
            return cid;
        }

        public static long addCashBookDataToSqlite(DatabaseHelper databaseHelper, CashBook cashBook){
           return databaseHelper.createCashBook(cashBook);
        }

        public static int delteAllbyClientID(DatabaseHelper databaseHelper, String clientid) {
           int idd= databaseHelper.getWritableDatabase()
                    .delete("CashBook","ClientID ="+clientid,null);
        return idd;

        }
    }

    public static class Account3NameTableFun {

        public static String UpdateAcNameIDInCashBook(DatabaseHelper helper,
                                                    String cliid,
                                                      String acnameidold,
                                                    String acnameidnew) {
            SQLiteDatabase db = helper.getWritableDatabase();
              final String TABLE_CashBook = "CashBook";

              final String KEY_ID = "ID";
              final String KEY_CashBookID = "CashBookID";
              final String KEY_CBDate = "CBDate";
              final String KEY_DebitAccount = "DebitAccount";
              final String KEY_CreditAccount = "CreditAccount";
              final String KEY_CBRemarks = "CBRemarks";
              final String KEY_Amount = "Amount";
              final String KEY_ClientID2 = "ClientID";
              final String KEY_ClientUserID2 = "ClientUserID";
              final String KEY_NetCode2 = "NetCode";
              final String KEY_SysCode2 = "SysCode";
              final String KEY_UpdatedDate2 = "UpdatedDate";
              final String KEY_TableID = "TableID";
              final String KEY_SerialNo2 = "SerialNo";
              final String KEY_TableName = "TableName";

            ContentValues cvcradit=new ContentValues();
              ContentValues cvdebit=new ContentValues();
            cvcradit.put("CreditAccount",acnameidnew);
            cvdebit.put("DebitAccount",acnameidnew);
            cvcradit.put("UpdatedDate",GenericConstants.NullFieldStandardText);
            int cradit=db.update("CashBook",cvcradit,
                            "ClientID = '"+cliid+"'" +
                                    " AND CreditAccount = '"+acnameidold+"'",null);
            int dabit=  helper.getWritableDatabase()
                    .update("CashBook",cvdebit,
                            "ClientID = '"+cliid+"'" +
                                    " AND DebitAccount = '"+acnameidold+"'"
                            ,null);
            return cradit+"_"+dabit;

        }
    }

    public static class BookingTable{
        public static int GetMaximumBookingIDFromDb(DatabaseHelper temref,String ClientID){
            String qq="select -(Max(Abs(Ifnull(BookingID,0)))+1) from Booking"+
                    " where ClientID=" + ClientID;
            Log.e("TEMP","("+qq);
            SQLiteDatabase dbtem = temref.getReadableDatabase();
            Cursor ctem = dbtem.rawQuery(qq, null);
            ctem.moveToFirst();
            Log.e("TEMP" + "TEMP", "Maxium ID " + ctem.getInt(0));
            ctem.moveToFirst();
            return ctem.getInt(0);
        }
        public static String GetMaximumUpdatedDateFromBooking(DatabaseHelper temref,String ClientID){
            String date = "0";
            String query = "SELECT max(UpdatedDate) FROM Booking WHERE ClientID = " + ClientID;
            SQLiteDatabase db = temref.getReadableDatabase();
            Cursor c = db.rawQuery(query, null);

            // looping through all rows and adding to list
            if (c.moveToFirst()) {
                do {

                    date = c.getString(0);

                } while (c.moveToNext());
            }

            return date;
        }
    }

}
