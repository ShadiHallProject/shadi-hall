package org.by9steps.shadihall.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.orm.SugarContext;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.ItemLedgerAdapter;
import org.by9steps.shadihall.fragments.SelectDateFragment;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.ItemLedgerRef;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.ItemLedger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class ItemLedgerActivity extends AppCompatActivity implements View.OnClickListener {

    //    add("Date");
//        spinner_list.add("BillNo");
//        spinner_list.add("EntryType");
//        spinner_list.add("Account");
//        spinner_list.add("Particular");
//        spinner_list.add("Price");
//        spinner_list.add("Add");
//        spinner_list.add("Less");
//        spinner_list.add("Balance");
//        spinner_list.add("Location");
    String colary[] = {"Date", "BillNo", "EntryType", "Account",
            "Particular", "Price", "Add", "Less", "Balance", "Location"};
    ImageView threedotmenugrid;
    RecyclerView recyclerView;
    List<ItemLedger> mList = new ArrayList<>();
    ItemLedgerAdapter adapter;
    Prefrence prefrence;
    /////////////////List Of Item on SpinnerDialog
    List<ItemLedgerRef> item3ledgerlist;
    DatabaseHelper databaseHelper;
    List<ItemLedger> itemLedgerList;
    List<ItemLedger> filterdList;
    /////////////////////////Get Current Account ID
    String acID = "";

    ArrayList<String> listDebit = new ArrayList<>();
    ArrayList<String> mDebit = new ArrayList<>();
    SpinnerDialog item3account, creditDialog;


    float lesscolsum = 0, balcolsum = 0, balance = 0;

    public static Button date_picker1, date_picker2;
    TextView debit_account;
    ImageView refresh;
    android.widget.Spinner sp_acgroup;
    private String spPosition;
    String query = "";

    //    EditText search;
    android.widget.Spinner spinner;
    SearchView searchView;
    String filterColName;

    String orderBy = "CBDate";
    int status = 0;
    String orderby = " ORDER BY " + orderBy + " ASC";

    //Print
    private static final String TAG = "PdfCreatorActivity";
    private File pdfFile;
    LinearLayout filterSearchViewlay;
    // private String Item3nameid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_ledger);
        filterSearchViewlay=findViewById(R.id.search_layout);
        filterSearchViewlay.setVisibility(View.GONE);
        SugarContext.init(this);
        threedotmenugrid = findViewById(R.id.Image3Dot);
        threedotmenugrid.setOnClickListener(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Item Ledger");
        }

        Intent intent = getIntent();
        if (intent != null) {
            acID = getIntent().getStringExtra("Item3nameid");
            //Item3nameid = intent.getStringExtra("Item3nameid");
            //Item3nameid= getIntent().getStringExtra("Item3nameid");
        }

        recyclerView = findViewById(R.id.recycler);
        date_picker1 = findViewById(R.id.date_picker1);
        date_picker2 = findViewById(R.id.date_picker2);
        refresh = findViewById(R.id.refresh);
        sp_acgroup = findViewById(R.id.sp_acgroup);
        searchView = findViewById(R.id.report_search);
        debit_account = findViewById(R.id.debit_account);
        spinner = findViewById(R.id.gl_spinner);

        debit_account.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop, 0);
        filterdList = new ArrayList<>();

        Date date = new Date();
        date_picker1.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
        date_picker2.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));

        date_picker1.setOnClickListener(this);
        date_picker2.setOnClickListener(this);

        databaseHelper = new DatabaseHelper(this);
        prefrence = new Prefrence(this);

        query = "SELECT ItemName,Item3NameID FROM Item3Name WHERE ClientID = " + prefrence.getClientIDSession();


        item3ledgerlist = databaseHelper.getItemLedgerSpinner(query);
        for (ItemLedgerRef ref : item3ledgerlist) {

            listDebit.add(ref.getItemname());
            mDebit.add(ref.getItem3ID());
        }

        debit_account.setOnClickListener(this);


//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner_list);
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // attaching data adapter to spinner
//        spinner.setAdapter(dataAdapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                switch (position) {
//                    case 0:
//                        filter = 0;
//                        searchView.setQuery("", false);
//                        searchView.clearFocus();
//                        break;
//                    case 1:
//                        filter = 1;
//                        searchView.setQuery("", false);
//                        searchView.clearFocus();
//                        break;
//                    case 2:
//                        filter = 2;
//                        searchView.setQuery("", false);
//                        searchView.clearFocus();
//                        break;
//                    case 3:
//                        filter = 3;
//                        searchView.setQuery("", false);
//                        searchView.clearFocus();
//                        break;
//                    case 4:
//                        filter = 4;
//                        searchView.setQuery("", false);
//                        searchView.clearFocus();
//                        break;
//                    case 5:
//                        filter = 5;
//                        searchView.setQuery("", false);
//                        searchView.clearFocus();
//                        break;
//                    case 6:
//                        filter = 6;
//                        searchView.setQuery("", false);
//                        searchView.clearFocus();
//                        break;
//                    case 7:
//                        filter = 7;
//                        searchView.setQuery("", false);
//                        searchView.clearFocus();
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

        getGeneralLedger();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                acID = ((Account2Group) sp_acgroup.getSelectedItem()).getAcGroupID();
                Log.e("asdfasdfasdf", acID);
                getGeneralLedger();
                filterColName = "";
                searchView.setQuery("", false);
                searchView.clearFocus();
            }
        });

    }

    public void getGeneralLedger() {
        mList.clear();
        lesscolsum = 0;
        balcolsum = 0;
        balance = 0;
        // Log.e("ACID",acID);
        Prefrence prefrence = new Prefrence(this);


//           String query="Select\n" +
//                   "    SalePur2.ClientID,\n" +
//                   "    Max(SalePur1.SPDate) As Date,\n" +
//                   "    SalePur2.Item3NameID As ItemID,\n" +
//                   "    '' As EntryType,\n" +
//                   "    '' As BillNo,\n" +
//                   "    '' As AccountName,\n" +
//                   "    'Balance Forword' As Reamrsk,\n" +
//                   "    Sum(SalePur2.QtyAdd) As \"Add\",\n" +
//                   "    Sum(SalePur2.QtyLess) As Less,\n" +
//                   "    Sum(SalePur2.QtyAdd) - Sum(SalePur2.QtyLess) As Balance,\n" +
//                   "    '' As User,\n" +
//                   "    '' As Location\n" +
//                   "From\n" +
//                   "    SalePur2 Left Join\n" +
//                   "    SalePur1 On SalePur1.SalePur1ID = SalePur2.SalePur1ID\n" +
//                   "            And SalePur1.ClientID = SalePur2.ClientID\n" +
//                   "            And SalePur1.EntryType = SalePur2.EntryType\n" +
//                   "Where\n" +
//                   "    SalePur2.ClientID = "+prefrence.getClientIDSession()+" And\n" +
//                   "    SalePur2.Item3NameID = '"+acID+"'\n" +
//                   "Group By\n" +
//                   "    SalePur2.ClientID,\n" +
//                   "    SalePur2.Item3NameID\n" +
//                   "Having\n" +
//                   "    Max(SalePur1.SPDate) < '"+date_picker1.getText().toString()+"'  \n" +
//                   "    Union All\n" +
//                   "    \n" +
//                   "    \n" +
//                   "    \n" +
//                   "Select\n" +
//                   "    Item3Name.ClientID,\n" +
//                   "    SalePur1.SPDate,\n" +
//                   "    Item3Name.Item3NameID,\n" +
//                   "    SalePur2.EntryType,\n" +
//                   "    SalePur1.SalePur1ID  As BillNo,\n" +
//                   "    Account3Name.AcName,\n" +
//                   "    SalePur1.Remarks || ', ' || SalePur2.ItemDescription As Remarks,\n" +
//                   "    SalePur2.QtyAdd,\n" +
//                   "    SalePur2.QtyLess,\n" +
//                   "    SalePur2.QtyAdd - SalePur2.QtyLess As Balance,\n" +
//                   "    Account3Name1.AcName As User,\n" +
//                   "    SalePurLocation.LocationName\n" +
//                   "From\n" +
//                   "    Item3Name Left Join\n" +
//                   "    SalePur2 On Item3Name.Item3NameID = SalePur2.Item3NameID\n" +
//                   "            And Item3Name.ClientID = SalePur2.ClientID Left Join\n" +
//                   "    Item2Group On Item2Group.Item2GroupID = Item3Name.Item2GroupID\n" +
//                   "            And Item2Group.ClientID = Item3Name.ClientID Inner Join\n" +
//                   "    SalePur1 On SalePur1.SalePur1ID = SalePur2.SalePur1ID\n" +
//                   "            And SalePur1.ClientID = SalePur2.ClientID\n" +
//                   "            And SalePur1.EntryType = SalePur2.EntryType Left Join\n" +
//                   "    Account3Name On Account3Name.AcNameID = SalePur1.AcNameID\n" +
//                   "            And Account3Name.ClientID = SalePur1.ClientID Left Join\n" +
//                   "    Account3Name Account3Name1 On Account3Name1.AcNameID = SalePur2.ClientUserID\n" +
//                   "            And Account3Name1.ClientID = SalePur2.ClientID Left Join\n" +
//                   "    SalePurLocation On SalePurLocation.LocationID = SalePur2.Location\n" +
//                   "            And SalePurLocation.ClientID = SalePur2.ClientID\n" +
//                   "Where\n" +
//                   "    Item3Name.ClientID = "+prefrence.getClientIDSession()+" And\n" +
//                   "    SalePur1.SPDate >= '"+date_picker1.getText().toString()+"' And\n" +
//                   "    SalePur1.SPDate <= '"+date_picker2.getText().toString()+"' And\n" +
//                   "    Item3Name.Item3NameID = '"+acID+"'";
//
        String query = "Select\n" +
                "    SalePur2.ClientID,\n" +
                "    Max(SalePur1.SPDate) As Date,\n" +
                "    SalePur2.Item3NameID As ItemID,\n" +
                "    '' As EntryType,\n" +
                "    '' As BillNo,\n" +
                "    '' As AccountName,\n" +
                "    'Balance Forword' As Reamrsk,\n" +
                "    Sum(SalePur2.QtyAdd) As \"Add\",\n" +
                "    Sum(SalePur2.QtyLess) As Less,\n" +
                "    '' As Price,\n" +
                "    Sum(SalePur2.QtyAdd) - Sum(SalePur2.QtyLess) As Balance,\n" +
                "    '' As User,\n" +
                "    '' As Location\n" +
                "From\n" +
                "    SalePur2 Left Join\n" +
                "    SalePur1 On SalePur1.SalePur1ID = SalePur2.SalePur1ID\n" +
                "            And SalePur1.ClientID = SalePur2.ClientID\n" +
                "            And SalePur1.EntryType = SalePur2.EntryType\n" +
                "Where\n" +
                "    SalePur2.ClientID = " + prefrence.getClientIDSession() + " And\n" +
                "    SalePur2.Item3NameID = '1'\n" +
                "Group By\n" +
                "    SalePur2.ClientID,\n" +
                "    SalePur2.Item3NameID\n" +
                "Having\n" +
                "    Max(SalePur1.SPDate) < '" + date_picker1.getText().toString() + "'  \n" +
                "    Union All\n" +
                "    \n" +
                "    \n" +
                "    \n" +
                "Select\n" +
                "    Item3Name.ClientID,\n" +
                "    SalePur1.SPDate,\n" +
                "    Item3Name.Item3NameID,\n" +
                "    SalePur2.EntryType,\n" +
                "    SalePur1.SalePur1ID As BillNo,\n" +
                "    Account3Name.AcName,\n" +
                "    SalePur1.Remarks || ', ' || SalePur2.ItemDescription As Remarks,\n" +
                "    SalePur2.QtyAdd,\n" +
                "    SalePur2.QtyLess,\n" +
                "    SalePur2.Price,\n" +
                "    SalePur2.QtyAdd - SalePur2.QtyLess As Balance,\n" +
                "    Account3Name1.AcName As User,\n" +
                "    SalePurLocation.LocationName\n" +
                "From\n" +
                "    Item3Name Left Join\n" +
                "    SalePur2 On Item3Name.Item3NameID = SalePur2.Item3NameID\n" +
                "            And Item3Name.ClientID = SalePur2.ClientID Left Join\n" +
                "    Item2Group On Item2Group.Item2GroupID = Item3Name.Item2GroupID\n" +
                "            And Item2Group.ClientID = Item3Name.ClientID Inner Join\n" +
                "    SalePur1 On SalePur1.SalePur1ID = SalePur2.SalePur1ID\n" +
                "            And SalePur1.ClientID = SalePur2.ClientID\n" +
                "            And SalePur1.EntryType = SalePur2.EntryType Left Join\n" +
                "    Account3Name On Account3Name.AcNameID = SalePur1.AcNameID\n" +
                "            And Account3Name.ClientID = SalePur1.ClientID Left Join\n" +
                "    Account3Name Account3Name1 On Account3Name1.AcNameID = SalePur2.ClientUserID\n" +
                "            And Account3Name1.ClientID = SalePur2.ClientID Left Join\n" +
                "    SalePurLocation On SalePurLocation.LocationID = SalePur2.Location\n" +
                "            And SalePurLocation.ClientID = SalePur2.ClientID\n" +
                "Where\n" +
                "    Item3Name.ClientID = " + prefrence.getClientIDSession() + " And\n" +
                "    SalePur1.SPDate >= '" + date_picker1.getText().toString() + "' And\n" +
                "    SalePur1.SPDate <= '" + date_picker2.getText().toString() + "' And\n" +
                "    Item3Name.Item3NameID = '1'";


//        String query="Select\n" +
//                "    Item3Name.ClientID,\n" +
//                "    SalePur1.SPDate,\n" +
//                "    Item3Name.Item3NameID,\n" +
//                "    SalePur2.EntryType,\n" +
//                "    SalePur1.SalePur1ID As BillNo,\n" +
//                "    Account3Name.AcName,\n" +
//                "    SalePur1.Remarks || ', ' || SalePur2.ItemDescription As Remarks,\n" +
//                "    SalePur2.Price,\n" +
//                "    SalePur2.QtyAdd,\n" +
//                "    SalePur2.QtyLess,\n" +
//                "    SalePur2.QtyAdd - SalePur2.QtyLess As Balance,\n" +
//                "    Account3Name1.AcName As User,\n" +
//                "    SalePurLocation.LocationName\n" +
//                "From\n" +
//                "    Item3Name Left Join\n" +
//                "    SalePur2 On Item3Name.Item3NameID = SalePur2.Item3NameID\n" +
//                "            And Item3Name.ClientID = SalePur2.ClientID Left Join\n" +
//                "    Item2Group On Item2Group.Item2GroupID = Item3Name.Item2GroupID\n" +
//                "            And Item2Group.ClientID = Item3Name.ClientID Inner Join\n" +
//                "    SalePur1 On SalePur1.SalePur1ID = SalePur2.SalePur1ID\n" +
//                "            And SalePur1.ClientID = SalePur2.ClientID\n" +
//                "            And SalePur1.EntryType = SalePur2.EntryType Left Join\n" +
//                "    Account3Name On Account3Name.AcNameID = SalePur1.AcNameID\n" +
//                "            And Account3Name.ClientID = SalePur1.ClientID Left Join\n" +
//                "    Account3Name Account3Name1 On Account3Name1.AcNameID = SalePur2.ClientUserID\n" +
//                "            And Account3Name1.ClientID = SalePur2.ClientID Left Join\n" +
//                "    SalePurLocation On SalePurLocation.LocationID = SalePur2.Location\n" +
//                "            And SalePurLocation.ClientID = SalePur2.ClientID\n" +
//                "Where\n" +
//                "    Item3Name.ClientID = "+prefrence.getClientIDSession()+" And\n" +
//                "    SalePur1.SPDate >= '2010-10-05' And\n" +
//                "    SalePur1.SPDate <= '2020-10-20' And\n" +
//                "    Item3Name.Item3NameID = '1'\n" +
//                "\t";


        itemLedgerList = databaseHelper.getItemLedger(query);

        Log.e("Sizeresult", "res::" + itemLedgerList.size());
        for (ItemLedger g : itemLedgerList) {
            lesscolsum = lesscolsum + Float.valueOf(g.columnsData[7] + "");
            balcolsum = balcolsum + Float.valueOf(g.columnsData[8] + "");
            Log.e("a;sdfkjas;ldkf", "ListSize:" + itemLedgerList.size() + ":lesscolsum:" + lesscolsum + " balcolsum:" + balcolsum);
            //credit = credit + Integer.valueOf(g.getCredit());
        }
        ItemLedger itemLedgertem = new ItemLedger(ItemLedger.columnNames.length);
        itemLedgertem.rowForSum(lesscolsum + "", balcolsum + "", 7, 8);
        itemLedgerList.add(itemLedgertem);
//        mList.add(GeneralLedger.createTotal("","Total","","", "","","",String.valueOf(debit),String.valueOf(credit),"",String.valueOf(debit - credit)));
        float translesscolval = 0, transbalcolval = 0;
        if (itemLedgerList.size() > 0) {
            translesscolval = lesscolsum - Float.parseFloat(itemLedgerList.get(0).columnsData[7]);
            transbalcolval = balcolsum - Float.parseFloat(itemLedgerList.get(0).columnsData[8]);
            Log.e("atarasdfj", "lesscolsum:" + translesscolval + " balcolsum:" + transbalcolval);

            // mList.add(GeneralLedger.createTotal("", "Transaction Total", "", "", "", "", "", String.valueOf(debit - Integer.valueOf(generalLedgerList.get(0).getDebit())), String.valueOf(credit - Integer.valueOf(generalLedgerList.get(0).getCredit())), "", ""));
        }
//        }else if (generalLedgerList.size() > 0 && !generalLedgerList.get(0).getEntryNo().equals("0")){
//            mList.add(GeneralLedger.createTotal("","Transaction Total","","", "","","",String.valueOf(debit),String.valueOf(credit),"",""));
//        }
        ItemLedger itemLedgertemTrans = new ItemLedger(ItemLedger.columnNames.length);
        itemLedgertemTrans.rowForSumTransaction(translesscolval + "", transbalcolval + "", 7, 8);
        itemLedgerList.add(itemLedgertemTrans);
// Spinner Drop down elements
//        List<String> spinner_list = new ArrayList<String>();
//
//        try {
//            for (int i = 0; i <ItemLedger.columnNames.length ; i++) {
//                spinner_list.add(ItemLedger.columnNames[i]);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }

//////////////////////////////////////Creating Replicated List For Filter
        mList = itemLedgerList;
        adapter = new ItemLedgerAdapter(this, itemLedgerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        /////////////////////////////////////Setting VAlue On Spiner For Column Filter
        final List<String> spinner_list = new ArrayList<String>();


        //  for (int i = 0; i <ItemLedger.columnNames.length ; i++) {
        for (int i = 0; i < colary.length; i++) {
            spinner_list.add(colary[i]);
        }
//        spinner_list.add("Date");
//        spinner_list.add("BillNo");
//        spinner_list.add("EntryType");
//        spinner_list.add("Account");
//        spinner_list.add("Particular");
//        spinner_list.add("Price");
//        spinner_list.add("Add");
//        spinner_list.add("Less");
//        spinner_list.add("Balance");
//        spinner_list.add("Location");


        // }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner_list);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                Log.e("FilterListDataRef", "->" + position + "   " + spinner.getSelectedItem());
//                switch (position) {
//                    case 0:
                filterColName = (String) spinner.getSelectedItem();
                searchView.setQuery("", false);
                searchView.clearFocus();
//                        break;
//                    case 1:
//                      ///  filter = 1;
//                        searchView.setQuery("", false);
//                        searchView.clearFocus();
//                        break;
//                    case 2:
//                        filterColName = "";
//                        searchView.setQuery("", false);
//                        searchView.clearFocus();
//                        break;
//                    case 3:
//                        filterColName = "";
//                        searchView.setQuery("", false);
//                        searchView.clearFocus();
//                        break;
//                    case 4:
//                        filterColName = "";
//                        searchView.setQuery("", false);
//                        searchView.clearFocus();
//                        break;
//                    case 5:
//                        filterColName = "";
//                        searchView.setQuery("", false);
//                        searchView.clearFocus();
//                        break;
//                    case 6:
//                        filterColName = "";
//                        searchView.setQuery("", false);
//                        searchView.clearFocus();
//                        break;
//                    case 7:
//                        filterColName = "";
//                        searchView.setQuery("", false);
//                        searchView.clearFocus();
//                        break;
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_picker1:
                AppController.date = "GL1";
                FragmentManager fm = getSupportFragmentManager();
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(fm, "DatePicker");
                break;
            case R.id.date_picker2:
                AppController.date = "GL2";
                FragmentManager fm1 = getSupportFragmentManager();
                DialogFragment newFragment1 = new SelectDateFragment();
                newFragment1.show(fm1, "DatePicker");
                break;
            case R.id.debit_account:
                item3account = new SpinnerDialog(ItemLedgerActivity.this, listDebit, "Select Item");
                item3account.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String s, int i) {
                        acID = mDebit.get(i);
                        debit_account.setText(s);
                    }
                });
                item3account.showSpinerDialog();
                break;
            case R.id.Image3Dot:
                showPopupMenuOnClick();
                break;
        }
    }

    private void showPopupMenuOnClick() {
        View view = findViewById(R.id.Image3Dot);
        PopupMenu popup = new PopupMenu(this, view);

        popup.inflate(R.menu.item_ledger_menu);
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if(filterSearchViewlay.isShown()){
                    filterSearchViewlay.setVisibility(View.GONE);
                }else{
                    filterSearchViewlay.setVisibility(View.VISIBLE);
                }


                return false;


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cb_menu, menu);
        MenuItem referesh = menu.findItem(R.id.action_refresh);
        referesh.setVisible(false);
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_print:
//                try {
//                  //  createPdf();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (DocumentException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filter(String text) {


        filterdList = new ArrayList<>();
        Log.e("flagForfilter", "-->" + filterColName);
        //looping through existing elements
        if (!text.isEmpty()) {
            for (ItemLedger s : mList) {
                if (s.isRow == 0) {
                    if (filterColName.equals(colary[0])) {
                        if (s.Datecol.toLowerCase().contains(text.toLowerCase()))
                            filterdList.add(s);
                    } else if (filterColName.equals(colary[1])) {
                        if (s.BillNocol.toLowerCase().contains(text.toLowerCase()))                               //adding the element to filtered list
                            filterdList.add(s);
                    } else if (filterColName.equals(colary[2])) {
                        if (s.EntryTypecol.toLowerCase().contains(text.toLowerCase()))                               //adding the element to filtered list
                            filterdList.add(s);
                    } else if (filterColName.equals(colary[3])) {
                        if (s.AccountNameCol.toLowerCase().contains(text.toLowerCase()))                               //adding the element to filtered list
                            filterdList.add(s);
                    } else if (filterColName.equals(colary[4])) {
                        if (s.RemarksCol.toLowerCase().contains(text.toLowerCase()))                               //adding the element to filtered list
                            filterdList.add(s);
                    } else if (filterColName.equals(colary[5])) {
                        if (s.PriceCol.toLowerCase().contains(text.toLowerCase()))                               //adding the element to filtered list
                            filterdList.add(s);
                    } else if (filterColName.equals(colary[6])) {
                        if (s.AddCol.toLowerCase().contains(text.toLowerCase()))                               //adding the element to filtered list
                            filterdList.add(s);
                    } else if (filterColName.equals(colary[7])) {
                        if (s.LessCol.toLowerCase().contains(text.toLowerCase()))                               //adding the element to filtered list
                            filterdList.add(s);
                    } else if (filterColName.equals(colary[8])) {
                        if (s.BalCol.toLowerCase().contains(text.toLowerCase()))                               //adding the element to filtered list
                            filterdList.add(s);
                    } else if (filterColName.equals(colary[9])) {
                        if (s.LocCol.toLowerCase().contains(text.toLowerCase()))                               //adding the element to filtered list
                            filterdList.add(s);
                    }
                }
            }
        } else {
            filterdList = mList;
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdList);

    }

//    public void createPdf() throws IOException, DocumentException {
//
//        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
//        if (!docsFolder.exists()) {
//            docsFolder.mkdir();
//            Log.i(TAG, "Created a new directory for PDF");
//        }
//
//        String pdfname = "GeneralLedger.pdf";
//        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
//        OutputStream output = new FileOutputStream(pdfFile);
//
//        Document document = new Document(PageSize.A4);
//        PdfWriter writer = PdfWriter.getInstance(document, output);
//        writer.createXmpMetadata();
//        writer.setTagged();
//        writer.setPageEvent(new Footer());
//        document.open();
//        document.addLanguage("en-us");
//
//        PdfDictionary parameters = new PdfDictionary();Log.e("PDFDocument","Created2");
//        parameters.put(PdfName.MODDATE, new PdfDate());
//
//        Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
//        Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
//        Chunk chunk = new Chunk("Client Name", chapterFont);
//        Paragraph name = new Paragraph("Address",paragraphFont);
//        name.setIndentationLeft(0);
//        Paragraph contact = new Paragraph("Contact",paragraphFont);
//        contact.setIndentationLeft(0);
//
//        PdfPTable title = new PdfPTable(new float[]{3, 5, 3});
//        title.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//        title.getDefaultCell().setMinimumHeight(25);
//        title.setTotalWidth(PageSize.A4.getWidth());
//        title.setWidthPercentage(100);
//        title.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
//        title.setSpacingBefore(5);
//        title.setSpacingAfter(3);
//        title.addCell(footerCell("", PdfPCell.ALIGN_CENTER));
//        PdfPCell cell = new PdfPCell(new Phrase("General Ledger",chapterFont));
//        cell.setBorder(PdfPCell.NO_BORDER);
//        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        title.addCell(cell);
//        title.addCell(footerCell("",PdfPCell.ALIGN_CENTER));
//
//        String query = "SELECT * FROM Account3Name WHERE AcNameID = "+acID;
//
//        List<Account3Name> list = databaseHelper.getAccount3Name(query);
//
//        for (Account3Name a : list){
//
//            title.addCell(titleCell(a.getAcName(),PdfPCell.ALIGN_LEFT));
//            title.addCell(titleCell("",PdfPCell.ALIGN_CENTER));
//            title.addCell(titleCell(spinner.getSelectedItem()+": "+searchView.getQuery(),PdfPCell.ALIGN_CENTER));
//
//            title.addCell(titleCell(a.getAcAddress(),PdfPCell.ALIGN_LEFT));
//            title.addCell(titleCell("",PdfPCell.ALIGN_CENTER));
//            title.addCell(titleCell("From"+" "+date_picker1.getText().toString(),PdfPCell.ALIGN_CENTER));
//
//            title.addCell(titleCell(a.getAcContactNo(),PdfPCell.ALIGN_LEFT));
//            title.addCell(titleCell("",PdfPCell.ALIGN_CENTER));
////            PdfPCell dCell2 = new PdfPCell(new Phrase());
////            dCell2.setBorder(PdfPCell.NO_BORDER);
////            dCell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
////            dCell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            title.addCell(titleCell("To"+"     "+date_picker2.getText().toString(),PdfPCell.ALIGN_CENTER));
//
//        }
//
////        PdfPTable tab = new PdfPTable(new float[]{4, 6, 6, 6}); //one page contains 15 records
////        tab.setWidthPercentage(100);
////        tab.setSpacingBefore(10);
////        Font voucherFount = FontFactory.getFont(FontFactory.HELVETICA, 15, Font.BOLD);
////        voucherFount.setColor(BaseColor.WHITE);
////        PdfPCell pre = new PdfPCell(new Phrase("Previous Balance",voucherFount));
////        pre.setHorizontalAlignment(Element.ALIGN_CENTER);
////        pre.setVerticalAlignment(Element.ALIGN_CENTER);
////        pre.setBackgroundColor(BaseColor.BLACK);
////        PdfPCell tra = new PdfPCell(new Phrase("Transaction",voucherFount));
////        tra.setHorizontalAlignment(Element.ALIGN_CENTER);
////        tra.setVerticalAlignment(Element.ALIGN_CENTER);
////        tra.setBackgroundColor(BaseColor.BLACK);
////        PdfPCell clo = new PdfPCell(new Phrase("Closing Balance",voucherFount));
////        clo.setHorizontalAlignment(Element.ALIGN_CENTER);
////        clo.setVerticalAlignment(Element.ALIGN_CENTER);
////        clo.setBackgroundColor(BaseColor.BLACK);
////        tab.addCell(footerCell("",PdfPCell.ALIGN_CENTER));
////        tab.addCell(pre);
////        tab.addCell(tra);
////        tab.addCell(clo);
//
//        PdfPTable table = new PdfPTable(new float[]{4, 3, 3, 3, 3, 3, 3});
//        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//        table.getDefaultCell().setFixedHeight(40);
//        table.setTotalWidth(PageSize.A4.getWidth());
//        table.setWidthPercentage(100);
//        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
////        table.setSpacingBefore(20);
//        table.addCell("CB ID");
//        table.addCell("Date");
//        table.addCell("Ac Name");
//        table.addCell("Remarks");
//        table.addCell("Debit");
//        table.addCell("Credit");
//        table.addCell("Balance");
//        table.setHeaderRows(1);
//        PdfPCell[] cells = table.getRow(0).getCells();
//        for (int j = 0; j < cells.length; j++) {
//            cells[j].setBackgroundColor(BaseColor.PINK);
//        }
//
//        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD);
//        PdfPCell total = new PdfPCell(new Phrase("Total",totalFont));
//        total.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//        total.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        total.setFixedHeight(35);
//
//        debit = 0; credit = 0; balance = 0;
//
////        if (filter > 0){
////            for (GeneralLedger g : filterdList){
////                balance = balance + Integer.valueOf(g.getBalance());
////                debit = debit + Integer.valueOf(g.getDebit());
////                credit = credit + Integer.valueOf(g.getCredit());
////
////                table.addCell(getCell(g.getEntryNo(), PdfPCell.ALIGN_RIGHT));
////                table.addCell(getCell(g.getDate(), PdfPCell.ALIGN_RIGHT));
////                table.addCell(getCell(g.getAccountName(), PdfPCell.ALIGN_RIGHT));
////                table.addCell(getCell(g.getParticulars(), PdfPCell.ALIGN_RIGHT));
////                table.addCell(getCell(g.getDebit(), PdfPCell.ALIGN_RIGHT));
////                table.addCell(getCell(g.getCredit(), PdfPCell.ALIGN_RIGHT));
////                table.addCell(getCell(String.valueOf(balance), PdfPCell.ALIGN_RIGHT));
////            }
////            table.addCell(total);
////            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
////            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
////            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
////            table.addCell(getCell(String.valueOf(debit), PdfPCell.ALIGN_RIGHT));
////            table.addCell(getCell(String.valueOf(credit), PdfPCell.ALIGN_RIGHT));
////            table.addCell(getCell(String.valueOf(debit - credit), PdfPCell.ALIGN_RIGHT));
////
////            PdfPCell traTotal = new PdfPCell(new Phrase("Transaction Total",totalFont));
////            total.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
////            total.setVerticalAlignment(Element.ALIGN_MIDDLE);
////            total.setFixedHeight(35);
////
////            table.addCell(traTotal);
////            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
////            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
////            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
////            table.addCell(getCell(String.valueOf(debit - Integer.valueOf(filterdList.get(0).getDebit())), PdfPCell.ALIGN_RIGHT));
////            table.addCell(getCell(String.valueOf(credit - Integer.valueOf(filterdList.get(0).getCredit())), PdfPCell.ALIGN_RIGHT));
////            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
////        }else {
////            for (GeneralLedger g : generalLedgerList){
////
////                balance = balance + Integer.valueOf(g.getBalance());
////                debit = debit + Integer.valueOf(g.getDebit());
////                credit = credit + Integer.valueOf(g.getCredit());
////
////                table.addCell(getCell(g.getEntryNo(), PdfPCell.ALIGN_RIGHT));
////                table.addCell(getCell(g.getDate(), PdfPCell.ALIGN_RIGHT));
////                table.addCell(getCell(g.getAccountName(), PdfPCell.ALIGN_RIGHT));
////                table.addCell(getCell(g.getParticulars(), PdfPCell.ALIGN_RIGHT));
////                table.addCell(getCell(g.getDebit(), PdfPCell.ALIGN_RIGHT));
////                table.addCell(getCell(g.getCredit(), PdfPCell.ALIGN_RIGHT));
////                table.addCell(getCell(String.valueOf(balance), PdfPCell.ALIGN_RIGHT));
////            }
//
//            table.addCell(total);
//            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
//            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
//            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
//            table.addCell(getCell(String.valueOf(debit), PdfPCell.ALIGN_RIGHT));
//            table.addCell(getCell(String.valueOf(credit), PdfPCell.ALIGN_RIGHT));
//            table.addCell(getCell(String.valueOf(debit - credit), PdfPCell.ALIGN_RIGHT));
//
//            PdfPCell traTotal = new PdfPCell(new Phrase("Transaction Total",totalFont));
//            total.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//            total.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            total.setFixedHeight(35);
//
//            table.addCell(traTotal);
//            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
//            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
//            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
//            table.addCell(getCell(String.valueOf(debit - Integer.valueOf(filterdList.get(0).getDebit())), PdfPCell.ALIGN_RIGHT));
//            table.addCell(getCell(String.valueOf(credit - Integer.valueOf(filterdList.get(0).getCredit())), PdfPCell.ALIGN_RIGHT));
//            table.addCell(getCell("", PdfPCell.ALIGN_RIGHT));
//        }


//        document.open();
//
//        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.BLACK);
//        Paragraph paragraph = new Paragraph("Cash Book \n\n", f);
//        paragraph.setAlignment(Element.ALIGN_CENTER);
//        document.add(chunk);
//        document.add(name);
//        document.add(contact);
//        document.add(title);
////        document.add(tab);
//        document.add(table);
//
//        document.close();
//        customPDFView();
//        Log.e("PDFDocument","Created");


    public PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(alignment);
        cell.setMinimumHeight(35);
        cell.setPadding(3);
        return cell;
    }

    public PdfPCell footerCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setMinimumHeight(25);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public PdfPCell titleCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setMinimumHeight(20);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public void customPDFView() {
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            startActivity(intent);
        } else {
            Toast.makeText(ItemLedgerActivity.this, "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
        }
    }

    class Footer extends PdfPageEventHelper {
        Font font;
        PdfTemplate t;
        Image total;

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            t = writer.getDirectContent().createTemplate(30, 16);
            try {
                total = Image.getInstance(t);
                total.setRole(PdfName.ARTIFACT);
                font = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.BLACK);
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {

            PdfPTable table = new PdfPTable(new float[]{3, 4, 2});
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setFixedHeight(20);
            table.setTotalWidth(PageSize.A4.getWidth());
            table.getDefaultCell().setBorder(Rectangle.BOTTOM);
            Date dat = new Date();
            SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
            table.addCell(footerCell(df.format(dat), PdfPCell.ALIGN_LEFT));
            table.addCell(footerCell("www.easysoft.com.pk", PdfPCell.ALIGN_LEFT));
            Log.e("PAGE NUMBER", String.valueOf(writer.getPageNumber()));
            table.addCell(footerCell(String.format("Page %d ", writer.getPageNumber() - 1), PdfPCell.ALIGN_LEFT));
//            table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
//            table.addCell(footerCell("www.easysoft.com.pk",PdfPCell.ALIGN_LEFT));
//            table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));

            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
            table.writeSelectedRows(0, -1, 36, 30, canvas);
            canvas.endMarkedContentSequence();

        }

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(t, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber()), font),
                    2, 4, 0);
        }
    }
}
