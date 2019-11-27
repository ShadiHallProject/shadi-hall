package org.by9steps.shadihall.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.genericgrid.DialogForSearchGrid;
import org.by9steps.shadihall.genericgrid.GenericGridAdapter;
import org.by9steps.shadihall.genericgrid.MediatorClass;
import org.by9steps.shadihall.genericgrid.ViewModeRef;
import org.by9steps.shadihall.model.Account1Type;
import org.by9steps.shadihall.model.Account2Group;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.Item1Type;
import org.by9steps.shadihall.model.Item2Group;
import org.by9steps.shadihall.model.item3name.Item3Name_;
import org.by9steps.shadihall.model.salepur1data.Salepur1;
import org.by9steps.shadihall.model.salepur2data.SalePur2;

import java.util.ArrayList;
import java.util.List;

public class ViewDBAllData extends AppCompatActivity implements View.OnClickListener {

    //////////////////////////Container For Btn
    LinearLayout linearLayout;
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
    List<Bookings> bookingsList;
    StringBuilder builder = new StringBuilder();
    TextView showAlldata;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dball_data);
        linearLayout=findViewById(R.id.containerforbtn);
        addBtnTolinearylayout();
        recyclerView=findViewById(R.id.recyclerviewmgrid);
        getSupportActionBar().setTitle("View Data");
        showAlldata = findViewById(R.id.textviewShowdata);
       // helper = new DatabaseHelper(this);
        GetAccount1TypeData();
        builder.append("End OF Account 1 Type Table ------\n\n\n\n");
        builder.append("\n");
//        GetAccount2GroupData();
//        builder.append("End OF Account2GroupData ------\n\n\n\n");
//        builder.append("\n");
//        GetAccount3Name();
//        builder.append("End OF Account3Name ------\n\n\n\n");
        builder.append("\n");
        GetCashBookData();
        builder.append("End OF CashBookData ------\n\n\n\n");
        builder.append("\n");
//        GetItem1Type();
//        builder.append("End OF GetItem1Type ------\n\n\n\n");
//        builder.append("\n");
//        GetItem2Group();
//        builder.append("End OF GetItem2Group ------\n\n\n\n");
//        builder.append("\n");
//        GetItem3Name();
//        builder.append("End OF GetItem3Name ------\n\n\n\n");
//        builder.append("\n");
//        GetSalePur1DAta();
//        builder.append("End OF SalePur1Data ------\n\n\n\n");
//        builder.append("\n");
//        GetSalePur2DAta();
//        builder.append("End OF SalePur2Data ------\n\n\n\n");
//        builder.append("\n");
//        getAllTableNameFromSqlite();
//        builder.append("End OF TableNameData ------\n\n\n\n");
        builder.append("\n");
        getBookingDataFromSqlite();
        builder.append("------------------------TAble Info ");
        getAllTableNameFromSqlite();
        builder.append("\n\n\n\n\n\n\n\n\n\n DB INFO");
        getDatabaseStructure(helper.getReadableDatabase());
        builder.append("\n\n DB All Views INFO");
        getAllViews(helper.getReadableDatabase());

        //showAlldata.setText(builder);

    }



    private void getBookingDataFromSqlite() {
        bookingsList= helper.getBookings("Select * from Booking");
        builder.append("Table Name Booking("+bookingsList.size()+")\n");



        for (int i = 0; i < bookingsList.size(); i++) {
            builder.append("*****START Object"+(i+1)+"\n");
            builder.append(bookingsList.get(i).toString() + "\n");
            builder.append("-----END OBject "+(i+1)+"\n");
        }
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
//        cashBooksList = helper.getCashBook("Select * from CashBook");
//        builder.append("Table Name CashBookData("+cashBooksList.size()+")\n");
//        for (int i = 0; i < cashBooksList.size(); i++) {
//            builder.append("*****START Object"+(i+1)+"\n");
//            builder.append(cashBooksList.get(i).toString() + "\n");
//            builder.append("-----END OBject "+(i+1)+"\n");
//        }
//        Cursor cc=helper.getReadableDatabase().rawQuery("Select * from ShadiHallBookingProfit",null);
//        Log.e("aaaaaa",""+cc.getCount());
//       final MediatorClass mediatorClass=new MediatorClass(cc,recyclerView);
//        //mediatorClass.setSortingAllowed(true);
//        mediatorClass.ShowGrid();
//        mediatorClass.registerHeaderRowMenuClickListner(new DialogForSearchGrid.DialogClickListener() {
//            @Override
//            public void HandleClickLisnterOfDialog(String colName, String SearchText) {
//                Log.e("asdfasdfasdf",colName+"---"+SearchText);
//                String qq="Select * from ShadiHallBookingProfit where "+colName+" "
//                        +"LIKE '%"+SearchText+"%';";
//
//
//                Cursor cc = helper.getReadableDatabase().rawQuery(qq, null);
//                MNotificationClass.ShowToast(ViewDBAllData.this,cc.getCount()+" Row Found");
//                mediatorClass.FilterListByEnterText(cc);
//            }
//        });
//        mediatorClass.registerHeaderRowMenuClickListner(new GenericGridAdapter.MenuItemHeaderRowClickListner() {
//            @Override
//            public void ListenForDotMenuItemClick(MenuItem menuItem) {
//                MNotificationClass.ShowToastTem(ViewDBAllData.this,""+menuItem.getTitle());
//            }
//        });
//        mediatorClass.listenForSortClick(new GenericGridAdapter.ListenerForChange() {
//            @Override
//            public void listenForSortClick(String columnName, int index, char sorttype) {
//                Toast.makeText(ViewDBAllData.this, "ColName:(" + columnName + ")indx(" + index+") SrotType:("+sorttype+")", Toast.LENGTH_SHORT).show();
//                String orderby = "";
//                if (sorttype == 'A') {
//                    sorttype='D';
//                    orderby = " ORDER BY " + columnName + " DESC ";
//                }
//                else {
//                    sorttype='A';
//                    orderby = " ORDER BY " + columnName + " ASC ";
//                }
//                String qq="Select * from CashBook " + orderby;
//                Log.e("query",qq);
//                Cursor cc = helper.getReadableDatabase().rawQuery(qq, null);
//                mediatorClass.FilterList(cc, index,sorttype);
//            }
//        });
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

    public void getAllViews(SQLiteDatabase db){
        builder.append("-------@@@@@0000");
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'view' ", null);
        ArrayList<String[]> result = new ArrayList<String[]>();
        int i = 0;
        builder.append("\nview C sixe:"+result.size());
        result.add(c.getColumnNames());
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String[] temp = new String[c.getColumnCount()];
            builder.append("View - \n\n\n\n");
            for (i = 0; i < temp.length; i++) {
                temp[i] = c.getString(i);
                builder.append((i+1)+"View - "+temp[i]+"\n");


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

    private void addBtnTolinearylayout() {
        helper=new DatabaseHelper(this);
        Cursor c = helper.getReadableDatabase().rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        if (c.moveToFirst()) {
            do {
                Button button=new Button(linearLayout.getContext());
                button.setOnClickListener(this);
                button.setTextColor(this.getResources().getColor(R.color.contenttextcolor));
                button.setText(c.getString(0));
                button.setBackgroundResource(R.drawable.round_corner_btn);
                button.setPadding(10, 10, 10, 10);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8,8,8,8);
                button.setLayoutParams(params);
                linearLayout.addView(button);

            }while(c.moveToNext());
        }

    }
    @Override
    public void onClick(View v) {
        Button btn= (Button) v;
        final String TAbleName=btn.getText().toString();
        Toast.makeText(this, ""+btn.getText(), Toast.LENGTH_SHORT).show();
        Cursor cc=helper.getReadableDatabase().rawQuery("Select * from "+ TAbleName,null);
        Log.e("aaaaaa",""+cc.getCount());
        final MediatorClass mediatorClass=new MediatorClass(cc,recyclerView);
            mediatorClass.setSortingAllowed(true);
        mediatorClass.ShowGrid();
        mediatorClass.listenForSortClick(new GenericGridAdapter.ListenerForChange() {
            @Override
            public void listenForSortClick(String columnName, int index, char sorttype) {
                Toast.makeText(ViewDBAllData.this, "ColName:(" + columnName + ")indx(" + index+") SrotType:("+sorttype+")", Toast.LENGTH_SHORT).show();
                String orderby = "";
                if (sorttype == 'A') {
                    sorttype='D';
                    orderby = " ORDER BY " + columnName + " DESC ";
                }
                else {
                    sorttype='A';
                    orderby = " ORDER BY " + columnName + " ASC ";
                }
                String qq="Select * from "+TAbleName + orderby;
                Log.e("query",qq);
                Cursor cc = helper.getReadableDatabase().rawQuery(qq, null);
                mediatorClass.FilterList(cc, index,sorttype);
            }
        });
        mediatorClass.registerHeaderRowMenuClickListner(new DialogForSearchGrid.DialogClickListener() {
            @Override
            public void HandleClickLisnterOfDialog(String colName, String SearchText) {
                Log.e("asdfasdfasdf",colName+"---"+SearchText);
                String qq="Select * from "+TAbleName+" where "+colName+" "
                        +"LIKE '%"+SearchText+"%';";


                Cursor cc = helper.getReadableDatabase().rawQuery(qq, null);
                MNotificationClass.ShowToast(ViewDBAllData.this,cc.getCount()+" Row Found");
                mediatorClass.FilterListByEnterText(cc);
            }
        });
    }
}
