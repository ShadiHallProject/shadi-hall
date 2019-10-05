package org.by9steps.shadihall.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.model.Account1Type;
import org.by9steps.shadihall.model.Account2Group;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.Item1Type;
import org.by9steps.shadihall.model.Item2Group;
import org.by9steps.shadihall.model.item3name.Item3Name_;
import org.by9steps.shadihall.model.salepur1data.Salepur1;
import org.by9steps.shadihall.model.salepur2data.SalePur2;

import java.util.ArrayList;
import java.util.List;

public class ViewDBAllData extends AppCompatActivity {

    DatabaseHelper helper;
    List<Account1Type> account1Types;
    List<Account2Group> account2GroupList;
    List<Account3Name> account3NameList;
    List<CashBook> cashBooksList;
    List<Item1Type> item1TypeList;
    List<Item2Group> item2GroupList;
    List<Item3Name_> item3NameList;
    List<Salepur1> salepur1s;
    List<SalePur2> salePur2s;
    StringBuilder builder = new StringBuilder();
    TextView showAlldata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dball_data);
        getSupportActionBar().setTitle("View Data");
        showAlldata = findViewById(R.id.textviewShowdata);
        helper = new DatabaseHelper(this);
        GetAccount1TypeData();
        builder.append("End OF Account 1 Type Table ------\n\n\n\n");
        builder.append("\n");
        GetAccount2GroupData();
        builder.append("End OF Account2GroupData ------\n\n\n\n");
        builder.append("\n");
        GetAccount3Name();
        builder.append("End OF Account3Name ------\n\n\n\n");
        builder.append("\n");
        GetCashBookData();
        builder.append("End OF CashBookData ------\n\n\n\n");
        builder.append("\n");
        GetItem1Type();
        builder.append("End OF GetItem1Type ------\n\n\n\n");
        builder.append("\n");
        GetItem2Group();
        builder.append("End OF GetItem2Group ------\n\n\n\n");
        builder.append("\n");
        GetItem3Name();
        builder.append("End OF GetItem3Name ------\n\n\n\n");
        builder.append("\n");
        GetSalePur1DAta();
        builder.append("End OF SalePur1Data ------\n\n\n\n");
        builder.append("\n");
        GetSalePur2DAta();
        builder.append("End OF SalePur2Data ------\n\n\n\n");
        builder.append("\n");
        getAllTableNameFromSqlite();

        builder.append("------------------------TAble Info ");
        getAllTableNameFromSqlite();
        builder.append("\n\n\n\n\n\n\n\n\n\n DB INFO");
        getDatabaseStructure(helper.getReadableDatabase());
        showAlldata.setText(builder);

    }

    private void GetSalePur2DAta() {
        salePur2s= helper.getSalePur2Data("Select * from "+refdb.SalePur2.TABLE_SalePur2);
        builder.append("Table Name SalePur2Data("+salePur2s.size()+")\n");



        for (int i = 0; i < salePur2s.size(); i++) {
            builder.append("*****START Object"+(i+1)+"\n");
            builder.append(salePur2s.get(i).toString() + "\n");
            builder.append("-----END OBject "+(i+1)+"\n");
        }
    }

    private void getAllTableNameFromSqlite() {
        Cursor c = helper.getReadableDatabase().rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
       builder.append("All Tables Names List -------");
       int i=1;
        if (c.moveToFirst()) {
           do {
                builder.append(i+")"+c.getString(0)+"\n");
i++;
            }while(c.moveToNext());
        }
    }

    private void GetAccount1TypeData() {
        account1Types = helper.getAccount1Type("Select * from Account1Type");
        builder.append("Table Name Account1Type("+account1Types.size()+")\n");
        for (int i = 0; i < account1Types.size(); i++) {
            builder.append(account1Types.get(i).toString() + "\n");
        }
    }
    private void GetAccount2GroupData() {
        account2GroupList = helper.getAccount2Group("Select * from Account2Group");
        builder.append("Table Name Account2GroupData("+account2GroupList.size()+")\n");
        for (int i = 0; i < account2GroupList.size(); i++) {
            builder.append(account2GroupList.get(i).toString() + "\n");
        }
    }
    private void GetAccount3Name() {
        account3NameList = helper.getAccount3Name("Select * from Account3Name");
        builder.append("Table Name Account3Name("+account3NameList.size()+")\n");
        for (int i = 0; i < account3NameList.size(); i++) {
            builder.append("*****START Object "+(i+1)+"\n");
            builder.append(account3NameList.get(i).toString() + "\n");
            builder.append("-----END OBject "+(i+1)+"\n");
        }
    }
    private void GetCashBookData() {
        cashBooksList = helper.getCashBook("Select * from CashBook");
        builder.append("Table Name CashBookData("+cashBooksList.size()+")\n");
        for (int i = 0; i < cashBooksList.size(); i++) {
            builder.append("*****START Object"+(i+1)+"\n");
            builder.append(cashBooksList.get(i).toString() + "\n");
            builder.append("-----END OBject "+(i+1)+"\n");
        }
    }
    private void GetItem1Type() {
        item1TypeList = helper.getItem1TypeData("Select * from Item1Type");
        builder.append("Table Name Item1Type("+item1TypeList.size()+")\n");



        for (int i = 0; i < item1TypeList.size(); i++) {
            builder.append("*****START Object"+(i+1)+"\n");
            builder.append(item1TypeList.get(i).toString() + "\n");
            builder.append("-----END OBject "+(i+1)+"\n");
        }
    }
    private void GetItem2Group() {
        item2GroupList = helper.getItem2GroupData("Select * from "+refdb.Table2Group.TABLE_Item2Group);
        builder.append("Table Name Item2Group("+item2GroupList.size()+")\n");



        for (int i = 0; i < item2GroupList.size(); i++) {
            builder.append("*****START Object"+(i+1)+"\n");
            builder.append(item2GroupList.get(i).toString() + "\n");
            builder.append("-----END OBject "+(i+1)+"\n");
        }
    }
    private void GetItem3Name() {
        item3NameList = helper.getItem3NameData("Select * from "+refdb.Table3Name.TABLE_Item3Name);
        builder.append("Table Name Item3Name("+item3NameList.size()+")\n");



        for (int i = 0; i < item3NameList.size(); i++) {
            builder.append("*****START Object"+(i+1)+"\n");
            builder.append(item3NameList.get(i).toString() + "\n");
            builder.append("-----END OBject "+(i+1)+"\n");
        }
    }
    private void GetSalePur1DAta() {

        salepur1s = helper.getSalePur1Data("Select * from "+refdb.SlePur1.TABLE_SalePur1);
        builder.append("Table Name SalePur1DAta("+salepur1s.size()+")\n");



        for (int i = 0; i < salepur1s.size(); i++) {
            builder.append("*****START Object"+(i+1)+"\n");
            builder.append(salepur1s.get(i).toString() + "\n");
            builder.append("-----END OBject "+(i+1)+"\n");
        }
    }
    public void getDatabaseStructure(SQLiteDatabase db) {
builder.append("-------@@@@@");
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        ArrayList<String[]> result = new ArrayList<String[]>();
        int i = 0;
        result.add(c.getColumnNames());
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String[] temp = new String[c.getColumnCount()];
            builder.append("TABLE - \n\n\n\n\n\n\n\n");
            for (i = 0; i < temp.length; i++) {
                temp[i] = c.getString(i);
                builder.append((i+1)+"TABLE - "+temp[i]+"\n");


                Cursor c1 = db.rawQuery(
                        "SELECT * FROM "+temp[i], null);
                c1.moveToFirst();
                String[] COLUMNS = c1.getColumnNames();
                for(int j=0;j<COLUMNS.length;j++){
                    c1.move(j);
                    builder.append((j+1)+"    COLUMN - "+COLUMNS[j]+"\n");
                }
            }
            result.add(temp);
        }
    }
}
