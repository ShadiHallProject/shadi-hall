package org.by9steps.shadihall.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.CashBookSettingActivity;
import org.by9steps.shadihall.activities.CashCollectionActivity;
import org.by9steps.shadihall.activities.SplashActivity;
import org.by9steps.shadihall.adapters.CashBookAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.CBSetting;
import org.by9steps.shadihall.model.CBUpdate;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.CashEntry;
import org.by9steps.shadihall.model.TableSession;
import org.by9steps.shadihall.model.UpdateDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import android.widget.AdapterView.OnItemSelectedListener;

import static com.itextpdf.text.html.HtmlTags.ALIGN_LEFT;
import static com.itextpdf.text.html.HtmlTags.FONT;
///server to sqlite
////server get updated record insert sqlite
/////////sqlite to server
/////////update sqlite to server
/**
 * A simple {@link Fragment} subclass.
 */
public class CashBookFragment extends Fragment implements OnItemSelectedListener, View.OnClickListener {

    ProgressDialog mProgress;
    RecyclerView recyclerView;
//    EditText search;
    Spinner spinner;
    SearchView searchView;
//    ScrollView scrollView;
    HorizontalScrollView scrollView;
    LinearLayout header, pdfView;
    TextView tv_date,tv_id,tv_debit,tv_credit,tv_remarks,tv_amount;

    List<CashEntry> mList;
    List<CashBook> cashBooksList;
    List<CashEntry> filterd;
    CashBookAdapter adapter;
    DatabaseHelper databaseHelper;
    Prefrence prefrence;

    int m = 0, amount, gAmount , filter;
    Boolean listSorting = false;
    public static String fDate1, fDate2;
    public static Button date1;
    public static Button date2;
    String orderBy = "CBDate";
    int status = 0;
    String orderby = " ORDER BY " + orderBy + " DESC";

    private static final String TAG = "PdfCreatorActivity";
    private File pdfFile;

    String d = "0";
    int tot = 0;
    String value = "Complete CashBook", f = "No";
    String updatedDate;
    String cashID, incomeID, expenseID;

    public CashBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cash_book, container, false);

        recyclerView = view.findViewById(R.id.recycler);
        header = view.findViewById(R.id.header);
        scrollView = view.findViewById(R.id.scrollView);
        pdfView = view.findViewById(R.id.pdfView);
        searchView = view.findViewById(R.id.search);
        spinner = view.findViewById(R.id.spinner);
        tv_date = view.findViewById(R.id.tv_date);
        tv_id = view.findViewById(R.id.tv_id);
        tv_debit = view.findViewById(R.id.tv_debit);
        tv_credit = view.findViewById(R.id.tv_credit);
        tv_remarks = view.findViewById(R.id.tv_remarks);
        tv_amount = view.findViewById(R.id.tv_amount);

        databaseHelper = new DatabaseHelper(getContext());
        prefrence = new Prefrence(getContext());

        spinner.setOnItemSelectedListener(this);
        tv_date.setOnClickListener(this);
        tv_id.setOnClickListener(this);
        tv_debit.setOnClickListener(this);
        tv_credit.setOnClickListener(this);
        tv_remarks.setOnClickListener(this);
        tv_amount.setOnClickListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Select");
        categories.add("Date");
        categories.add("CB ID");
        categories.add("Deb Account");
        categories.add("Cre Account");
        categories.add("Remarks");
        categories.add("Amount");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                                                                                                                                // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getContext(), CashCollectionActivity.class);
                intent.putExtra("BookingID","0");
                intent.putExtra("Spinner","View");
                intent.putExtra("Type","Add");
                intent.putExtra("CashBookID","0");
                startActivity(intent);
            }
        });

        mList = new ArrayList<>();


        List<CBSetting> list = CBSetting.listAll(CBSetting.class);
        if (list.size() == 0){
            CBSetting cbSetting = new CBSetting(true,true,true,true,true,true);
            cbSetting.save();
        }

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

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_date:
                orderBy = "CBDate";
                orderBy(orderBy);
                break;
            case R.id.tv_id:
                orderBy = "CashBookID";
                orderBy(orderBy);
                break;
            case R.id.tv_debit:
                orderBy = "DebitAccount";
                orderBy(orderBy);
                break;
            case R.id.tv_credit:
                orderBy = "CreditAccount";
                orderBy(orderBy);
                break;
            case R.id.tv_remarks:
                orderBy = "CBRemarks";
                orderBy(orderBy);
                break;
            case R.id.tv_amount:
                orderBy = "Amount";
                orderBy(orderBy);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cb_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
        }else if (item.getItemId() == R.id.action_print){
            try {

                createPdf();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }else if (item.getItemId() == R.id.action_settings){
            startActivity(new Intent(getContext(), CashBookSettingActivity.class));
            return true;
        }else if (item.getItemId() == R.id.action_refresh){
            if (isConnected()){
                refereshTables(getContext());
            }else {
                Toast.makeText(getContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        getCashBook();

        List<CBSetting> list = CBSetting.listAll(CBSetting.class);
        for (CBSetting c : list){
            if (!c.getDste()){
                tv_date.setVisibility(View.GONE);
            }else {
                tv_date.setVisibility(View.VISIBLE);
            }

            if (!c.getCbId()){
                tv_id.setVisibility(View.GONE);
            }else {
                tv_id.setVisibility(View.VISIBLE);
            }

            if (!c.getDebit()){
                tv_debit.setVisibility(View.GONE);
            }else {
                tv_debit.setVisibility(View.VISIBLE);
            }

            if (!c.getCredit()){
                tv_credit.setVisibility(View.GONE);
            }else {
                tv_credit.setVisibility(View.VISIBLE);
            }

            if (!c.getRemarks()){
                tv_remarks.setVisibility(View.GONE);
            }else {
                tv_remarks.setVisibility(View.VISIBLE);
            }

            if (!c.getAmount()){
                tv_amount.setVisibility(View.GONE);
            }else {
                tv_amount.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getCashBook(){
        mList.clear();
        m = 0;
        amount = 0;
        gAmount = 0;
        String query = "";

            query = "SELECT      CashBook.ID, CashBook.CashBookID, CashBook.CBDate, CashBook.DebitAccount, CashBook.CreditAccount, CashBook.CBRemarks, CashBook.Amount, CashBook.ClientID, CashBook.ClientUserID, CashBook.TableID, \n" +
                    "                         Account3Name.AcName AS DebitAccountName, Account3Name_1.AcName AS CreditAccountName, Account3Name_2.AcName AS UserName, CashBook.UpdatedDate, CashBook.SerialNo\n" +
                    "FROM            CashBook LEFT OUTER JOIN\n" +
                    "                         Account3Name AS Account3Name_1 ON CashBook.CreditAccount = Account3Name_1.AcNameID LEFT OUTER JOIN\n" +
                    "                         Account3Name AS Account3Name_2 ON CashBook.ClientUserID = Account3Name_2.AcNameID LEFT OUTER JOIN\n" +
                    "                         Account3Name ON CashBook.DebitAccount = Account3Name.AcNameID\n" +
                    "WHERE        (CashBook.ClientID = "+prefrence.getClientIDSession()+")" + orderby;
            cashBooksList = databaseHelper.getCashBookEntry(query);

            for (CashBook c : cashBooksList){

                String[] separated = c.getCBDate().split("-");

                if (m == 0) {
                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]+"/"+separated[2]));
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getTableID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
                    m = Integer.valueOf(separated[2]);

                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;
                }else if (m == Integer.valueOf(separated[2])){
                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getTableID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
                }else {
                    mList.add(CashEntry.createTotal(String.valueOf(amount)));
                    amount = 0;
                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;

                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]+"/"+separated[2]));
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getTableID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
                    m = Integer.valueOf(separated[2]);
                }
            }
//        }

        mList.add(CashEntry.createTotal(String.valueOf(amount)));
        mList.add(CashEntry.createSection("Grand Total"));
        mList.add(CashEntry.createTotal(String.valueOf(gAmount)));
        adapter = new CashBookAdapter(getContext(),mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void filter(String text) {
        Log.e("Search","SEARCH");
        //new array list that will hold the filtered data
        filterd = new ArrayList<>();

        //looping through existing elements
        if (!text.isEmpty()) {
            for (CashEntry s : mList) {
                Log.e("Search", String.valueOf(s.isRow()));
                //if the existing elements contains the search input
                if (s.isRow() == 1) {
                    switch (filter){
                        case 2:
                            if (s.getCashBookID().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterd.add(s);
                            }
                            break;
                        case 3:
                            if (s.getDebitAccountName().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterd.add(s);
                            }
                            break;
                        case 4:
                            if (s.getCreditAccountName().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterd.add(s);
                            }
                            break;
                        case 5:
                            if (s.getCBRemarks().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterd.add(s);
                            }
                            break;
                        case 6:
                            if (s.getAmount().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterd.add(s);
                            }
                            break;
                    }
                }
            }
        }else {
            filterd = mList;
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterd);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position!=1 && position != 0){
            getCashBook();
        }
        switch (position){
            case 0:
                filter = 0;
                searchView.setQuery("",false);
                searchView.clearFocus();
                break;
            case 1:
                searchView.setQuery("",false);
                searchView.clearFocus();
                // custom dialog
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.date_filter_dialog);
                dialog.setTitle("Select Date");

                date1 = dialog.findViewById(R.id.date1);
                date2 = dialog.findViewById(R.id.date2);
                Button ok = dialog.findViewById(R.id.ok);
                Button cancel = dialog.findViewById(R.id.cancel);

                date1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppController.fDate1 = "Date1";
                        DialogFragment newFragment = new SelectDateFragment();
                        newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
                        fDate1 = date1.getText().toString();
                    }
                });

                date2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppController.fDate2 = "Date2";
                        DialogFragment newFragment = new SelectDateFragment();
                        newFragment.show(getActivity().getSupportFragmentManager(), "DatePicker");
                        fDate2 = date2.getText().toString();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fDate1 = date1.getText().toString();
                        fDate2 = date2.getText().toString();
                        dateFilter();
                        dialog.dismiss();
                        spinner.setSelection(0);
                        f = "Date";

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        spinner.setSelection(0);
                    }
                });

                dialog.show();

                break;
            case 2:
                filter = 2;
                searchView.setQuery("",false);
                searchView.clearFocus();
                break;
            case 3:
                filter = 3;
                searchView.setQuery("",false);
                searchView.clearFocus();
                break;
            case 4:
                filter = 4;
                searchView.setQuery("",false);
                searchView.clearFocus();
                break;
            case 5:
                filter = 5;
                searchView.setQuery("",false);
                searchView.clearFocus();
                break;
            case 6:
                filter = 6;
                searchView.setQuery("",false);
                searchView.clearFocus();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void dateFilter(){
        mList.clear();
        m = 0;
        amount = 0;
        gAmount = 0;
        String query = "";


            query = "SELECT        CashBook.CashBookID, CashBook.CBDate, CashBook.DebitAccount, CashBook.CreditAccount, CashBook.CBRemarks, CashBook.Amount, CashBook.ClientID, CashBook.ClientUserID, CashBook.BookingID, \n" +
                    "                         Account3Name.AcName AS DebitAccountName, Account3Name_1.AcName AS CreditAccountName, Account3Name_2.AcName AS UserName, CashBook.UpdatedDate\n" +
                    "FROM            CashBook LEFT OUTER JOIN\n" +
                    "                         Account3Name AS Account3Name_1 ON CashBook.CreditAccount = Account3Name_1.AcNameID LEFT OUTER JOIN\n" +
                    "                         Account3Name AS Account3Name_2 ON CashBook.ClientUserID = Account3Name_2.AcNameID LEFT OUTER JOIN\n" +
                    "                         Account3Name ON CashBook.DebitAccount = Account3Name.AcNameID\n" +
                    "WHERE        (CashBook.ClientID = "+prefrence.getClientIDSession()+" AND CashBook.CBDate >= Datetime('"+fDate1+"') AND CashBook.CBDate <= Datetime('"+fDate2+"'))";
            cashBooksList = databaseHelper.getCashBookEntry(query);


        if (listSorting){
            for (int i = cashBooksList.size()-1; i >= 0; i--){
                String[] separated = cashBooksList.get(i).getCBDate().split("-");
                if (m == 0) {
                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
                    mList.add(CashEntry.createRow(cashBooksList.get(i).getCashBookID(),cashBooksList.get(i).getCBDate(),cashBooksList.get(i).getDebitAccount(),cashBooksList.get(i).getCreditAccount(),cashBooksList.get(i).getCBRemarks(),cashBooksList.get(i).getAmount(),cashBooksList.get(i).getClientID(),cashBooksList.get(i).getClientUserID(),cashBooksList.get(i).getTableID(),cashBooksList.get(i).getDebitAccountName(),cashBooksList.get(i).getCreditAccountName(),cashBooksList.get(i).getUserName(), cashBooksList.get(i).getUpdatedDate()));
                    m = Integer.valueOf(separated[1]);

                    amount = Integer.valueOf(cashBooksList.get(i).getAmount()) + amount;
                    gAmount = Integer.valueOf(cashBooksList.get(i).getAmount()) + gAmount;
                }else if (m == Integer.valueOf(separated[1])){
                    amount = Integer.valueOf(cashBooksList.get(i).getAmount()) + amount;
                    gAmount = Integer.valueOf(cashBooksList.get(i).getAmount()) + gAmount;
                    mList.add(CashEntry.createRow(cashBooksList.get(i).getCashBookID(),cashBooksList.get(i).getCBDate(),cashBooksList.get(i).getDebitAccount(),cashBooksList.get(i).getCreditAccount(),cashBooksList.get(i).getCBRemarks(),cashBooksList.get(i).getAmount(),cashBooksList.get(i).getClientID(),cashBooksList.get(i).getClientUserID(),cashBooksList.get(i).getTableID(),cashBooksList.get(i).getDebitAccountName(),cashBooksList.get(i).getCreditAccountName(),cashBooksList.get(i).getUserName(), cashBooksList.get(i).getUpdatedDate()));
                }else {
                    mList.add(CashEntry.createTotal(String.valueOf(amount)));
                    amount = 0;
                    amount = Integer.valueOf(cashBooksList.get(i).getAmount()) + amount;
                    gAmount = Integer.valueOf(cashBooksList.get(i).getAmount()) + gAmount;

                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
                    mList.add(CashEntry.createRow(cashBooksList.get(i).getCashBookID(),cashBooksList.get(i).getCBDate(),cashBooksList.get(i).getDebitAccount(),cashBooksList.get(i).getCreditAccount(),cashBooksList.get(i).getCBRemarks(),cashBooksList.get(i).getAmount(),cashBooksList.get(i).getClientID(),cashBooksList.get(i).getClientUserID(),cashBooksList.get(i).getTableID(),cashBooksList.get(i).getDebitAccountName(),cashBooksList.get(i).getCreditAccountName(),cashBooksList.get(i).getUserName(), cashBooksList.get(i).getUpdatedDate()));
                    m = Integer.valueOf(separated[1]);
                }
            }
        }else {
            for (CashBook c : cashBooksList){

                String[] separated = c.getCBDate().split("-");

                if (m == 0) {
                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getTableID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
                    m = Integer.valueOf(separated[1]);

                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;
                }else if (m == Integer.valueOf(separated[1])){
                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getTableID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
                }else {
                    mList.add(CashEntry.createTotal(String.valueOf(amount)));
                    amount = 0;
                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;

                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getTableID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
                    m = Integer.valueOf(separated[1]);
                }
            }
        }

        mList.add(CashEntry.createTotal(String.valueOf(amount)));
        mList.add(CashEntry.createSection("Grand Total"));
        mList.add(CashEntry.createTotal(String.valueOf(gAmount)));
        adapter = new CashBookAdapter(getContext(),mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    //Check Internet Connection
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public void orderBy(String order_by){
        if (status == 0) {
            status = 1;
            orderby = " ORDER BY " + order_by + " DESC";
        } else {
            status = 0;
            orderby = " ORDER BY " + order_by + " ASC";
        }
        getCashBook();
    }

    public void createPdf() throws IOException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }

        String pdfname = "CashBook.pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, output);
        writer.createXmpMetadata();
        writer.setTagged();
        writer.setPageEvent(new Footer());
        document.open();
        document.addLanguage("en-us");

        PdfDictionary parameters = new PdfDictionary();Log.e("PDFDocument","Created2");
        parameters.put(PdfName.MODDATE, new PdfDate());

        Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
        Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
        Chunk chunk = new Chunk("Client Name", chapterFont);
        Paragraph name = new Paragraph("Address",paragraphFont);
        name.setIndentationLeft(0);
        Paragraph contact = new Paragraph("Contact",paragraphFont);
        contact.setIndentationLeft(0);

        PdfPTable title = new PdfPTable(new float[]{3, 3, 3});
        title.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        title.getDefaultCell().setFixedHeight(30);
        title.setTotalWidth(PageSize.A4.getWidth());
        title.setWidthPercentage(100);
        title.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.setSpacingBefore(5);
        title.addCell(footerCell("",PdfPCell.ALIGN_CENTER));
        PdfPCell cell = new PdfPCell(new Phrase("Cash Book",chapterFont));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.addCell(cell);
        title.addCell(footerCell("",PdfPCell.ALIGN_CENTER));

//        PdfPTable param = new PdfPTable(new float[]{3, 3, 3});
        title.addCell(footerCell("",PdfPCell.ALIGN_CENTER));
        title.addCell(footerCell("",PdfPCell.ALIGN_CENTER));

        PdfPCell pCell = null;
        if (f.equals("Date")){
            value = fDate1+" to "+fDate2;
            pCell = new PdfPCell(new Phrase("Date"+": "+value,paragraphFont));
        }else {
            value = searchView.getQuery().toString().trim();
            pCell = new PdfPCell(new Phrase(spinner.getSelectedItem().toString()+": "+value,paragraphFont));
        }
        pCell.setBorder(PdfPCell.NO_BORDER);
        pCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        pCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.addCell(pCell);

        PdfPTable table = new PdfPTable(new float[]{3, 4, 4, 5, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(40);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.setSpacingBefore(20);
        table.addCell("CB ID");
        table.addCell("Debit Account");
        table.addCell("Credit Account");
        table.addCell("Remarks");
        table.addCell("Amount");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.PINK);
        }

        tot = 0;
        int mTotal = 0;

        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD);
        PdfPCell total = new PdfPCell(new Phrase("Total",totalFont));
        total.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        total.setVerticalAlignment(Element.ALIGN_MIDDLE);
        total.setFixedHeight(35);

        if (filter > 1 && !searchView.getQuery().toString().equals("") && !f.equals("Date")){
            d = "0";
            for (CashEntry c : filterd){
                if (d.equals("0")){
                    PdfPCell section = new PdfPCell(new Phrase(c.getCBDate(),totalFont));
                    section.setBorder(PdfPCell.NO_BORDER);
                    section.setFixedHeight(30);
                    section.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(section);
                    table.addCell(footerCell("",PdfPCell.ALIGN_RIGHT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
//                    table.addCell(footerCell("",PdfPCell.ALIGN_RIGHT));


//                    table.addCell(getCell(c.getCBDate(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCashBookID(),PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getDebitAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCreditAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCBRemarks(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getAmount(),PdfPCell.ALIGN_RIGHT));

                    tot = tot + Integer.valueOf(c.getAmount());
                    mTotal = mTotal + Integer.valueOf(c.getAmount());
                    d = c.getCBDate();
                }else if(d.equals(c.getCBDate())){
//                    table.addCell(getCell(c.getCBDate(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCashBookID(),PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getDebitAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCreditAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCBRemarks(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getAmount(),PdfPCell.ALIGN_RIGHT));

                    tot = tot + Integer.valueOf(c.getAmount());
                    mTotal = mTotal + Integer.valueOf(c.getAmount());
                    d = c.getCBDate();
                }else if (!d.equals(c.getCBDate()) && !d.equals("0")){
//                    table.addCell("");
                    table.addCell("");
                    table.addCell("");
                    table.addCell("");
                    table.addCell(total);
                    table.addCell(getCell(String.valueOf(tot),PdfPCell.ALIGN_RIGHT));
                    tot = 0;

                    PdfPCell section = new PdfPCell(new Phrase(c.getCBDate(),totalFont));
                    section.setBorder(PdfPCell.NO_BORDER);
                    section.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    section.setFixedHeight(30);
                    table.addCell(section);
                    table.addCell(footerCell("",PdfPCell.ALIGN_RIGHT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
//                    table.addCell(footerCell("",PdfPCell.ALIGN_RIGHT));

//                    table.addCell(getCell(c.getCBDate(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCashBookID(),PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getDebitAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCreditAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCBRemarks(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getAmount(),PdfPCell.ALIGN_RIGHT));

                    tot = tot + Integer.valueOf(c.getAmount());
                    mTotal = mTotal + Integer.valueOf(c.getAmount());
                    d = c.getCBDate();
                }
            }
        }else if (filter > 1 && searchView.getQuery().toString().equals("") && f.equals("Date")){
            d = "0";
            for (CashEntry c : filterd){
                if (d.equals("0")){
                    PdfPCell section = new PdfPCell(new Phrase(c.getCBDate(),totalFont));
                    section.setBorder(PdfPCell.NO_BORDER);
                    section.setFixedHeight(30);
                    section.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(section);
                    table.addCell(footerCell("",PdfPCell.ALIGN_RIGHT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
//                    table.addCell(footerCell("",PdfPCell.ALIGN_RIGHT));


//                    table.addCell(getCell(c.getCBDate(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCashBookID(),PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getDebitAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCreditAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCBRemarks(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getAmount(),PdfPCell.ALIGN_RIGHT));

                    tot = tot + Integer.valueOf(c.getAmount());
                    mTotal = mTotal + Integer.valueOf(c.getAmount());
                    d = c.getCBDate();
                }else if(d.equals(c.getCBDate())){
//                    table.addCell(getCell(c.getCBDate(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCashBookID(),PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getDebitAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCreditAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCBRemarks(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getAmount(),PdfPCell.ALIGN_RIGHT));

                    tot = tot + Integer.valueOf(c.getAmount());
                    mTotal = mTotal + Integer.valueOf(c.getAmount());
                    d = c.getCBDate();
                }else if (!d.equals(c.getCBDate()) && !d.equals("0")){
//                    table.addCell("");
                    table.addCell("");
                    table.addCell("");
                    table.addCell("");
                    table.addCell(total);
                    table.addCell(getCell(String.valueOf(tot),PdfPCell.ALIGN_RIGHT));
                    tot = 0;

                    PdfPCell section = new PdfPCell(new Phrase(c.getCBDate(),totalFont));
                    section.setBorder(PdfPCell.NO_BORDER);
                    section.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    section.setFixedHeight(30);
                    table.addCell(section);
                    table.addCell(footerCell("",PdfPCell.ALIGN_RIGHT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
//                    table.addCell(footerCell("",PdfPCell.ALIGN_RIGHT));

//                    table.addCell(getCell(c.getCBDate(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCashBookID(),PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getDebitAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCreditAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCBRemarks(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getAmount(),PdfPCell.ALIGN_RIGHT));

                    tot = tot + Integer.valueOf(c.getAmount());
                    mTotal = mTotal + Integer.valueOf(c.getAmount());
                    d = c.getCBDate();
                }
            }
        } else {
            d = "0";
            for (CashBook c : cashBooksList){

                if (d.equals("0")){

                    PdfPCell section = new PdfPCell(new Phrase(c.getCBDate(),totalFont));
                    section.setBorder(PdfPCell.NO_BORDER);
                    section.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    section.setFixedHeight(30);
                    table.addCell(section);
                    table.addCell(footerCell("",PdfPCell.ALIGN_RIGHT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
//                    table.addCell(footerCell("",PdfPCell.ALIGN_RIGHT));

//                    table.addCell(getCell(c.getCBDate(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCashBookID(),PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getDebitAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCreditAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCBRemarks(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getAmount(),PdfPCell.ALIGN_RIGHT));

                    tot = tot + Integer.valueOf(c.getAmount());
                    mTotal = mTotal + Integer.valueOf(c.getAmount());
                    d = c.getCBDate();
                }else if(d.equals(c.getCBDate())){
//                    table.addCell(getCell(c.getCBDate(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCashBookID(),PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getDebitAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCreditAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCBRemarks(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getAmount(),PdfPCell.ALIGN_RIGHT));

                    tot = tot + Integer.valueOf(c.getAmount());
                    mTotal = mTotal + Integer.valueOf(c.getAmount());
                    d = c.getCBDate();
                }else if (!d.equals(c.getCBDate()) && !d.equals("0")){
//                    table.addCell("");
                    table.addCell("");
                    table.addCell("");
                    table.addCell("");
                    table.addCell(total);
                    table.addCell(getCell(String.valueOf(tot),PdfPCell.ALIGN_RIGHT));
                    tot = 0;

                    PdfPCell section = new PdfPCell(new Phrase(c.getCBDate(),totalFont));
                    section.setBorder(PdfPCell.NO_BORDER);
                    section.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    section.setFixedHeight(30);
                    table.addCell(section);
                    table.addCell(footerCell("",PdfPCell.ALIGN_RIGHT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
//                    table.addCell(footerCell("",PdfPCell.ALIGN_RIGHT));

//                    table.addCell(getCell(c.getCBDate(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCashBookID(),PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getDebitAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCreditAccountName(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getCBRemarks(),PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getAmount(),PdfPCell.ALIGN_RIGHT));

                    tot = tot + Integer.valueOf(c.getAmount());
                    mTotal = mTotal + Integer.valueOf(c.getAmount());
                    d = c.getCBDate();
                }
            }
        }

//        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell(total);
        table.addCell(getCell(String.valueOf(tot),PdfPCell.ALIGN_RIGHT));

//        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("Grand Total");
        table.addCell(getCell(String.valueOf(mTotal),PdfPCell.ALIGN_RIGHT));

//        Footer footer = new Footer();

        document.open();

        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.BLACK);
        Paragraph paragraph = new Paragraph("Cash Book \n\n", f);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(chunk);
        document.add(name);
        document.add(contact);
        document.add(title);
//        document.add(param);
        document.add(table);

        document.close();
        customPDFView();
        Log.e("PDFDocument","Created");
    }

    public PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(alignment);
        cell.setMinimumHeight(30);
        cell.setPadding(3);
        return cell;
    }

    public PdfPCell footerCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public void customPDFView(){
        PackageManager packageManager = getContext().getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            getContext().startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
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
                font =  new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.BLACK);
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
            table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
            Log.e("PAGE NUMBER",String.valueOf(writer.getPageNumber()));
            table.addCell(footerCell(String.format("Page %s", writer.getPageNumber() - 1),PdfPCell.ALIGN_LEFT));
            table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
            table.addCell(footerCell("www.easysoft.com.pk",PdfPCell.ALIGN_LEFT));
            table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));

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

    //FOR TESTING CASHBOOK

    public void refereshTables(Context context){

        cashID = databaseHelper.getID("SELECT AcNameID FROM Account3Name WHERE ClientID = "+prefrence.getClientIDSession()+" and AcName = 'Cash'");
        incomeID = databaseHelper.getID("SELECT AcNameID FROM Account3Name WHERE ClientID = "+prefrence.getClientIDSession()+" and AcName = 'Booking Income'");
        expenseID = databaseHelper.getID("SELECT AcNameID FROM Account3Name WHERE ClientID = "+prefrence.getClientIDSession()+" and AcName = 'Booking Expense'");

        databaseHelper = new DatabaseHelper(context);
        mProgress = new ProgressDialog(context);
        mProgress.setMessage("Loading...");
        mProgress.setCancelable(false);
        mProgress.show();
        getAccount3Name();

    }

    public void getAccount3Name(){
        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshAccount3Name.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            String success = jsonObj.getString("success");
                            Log.e("Account3Name1",jsonObj.toString());
                            if (success.equals("1")){
                                JSONArray jsonArray = jsonObj.getJSONArray("Account3Name");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Account3Name",jsonObject.toString());
                                    String AcNameID = jsonObject.getString("AcNameID");
                                    String AcName = jsonObject.getString("AcName");
                                    String AcGroupID = jsonObject.getString("AcGroupID");
                                    String AcAddress = jsonObject.getString("AcAddress");
                                    String AcMobileNo = jsonObject.getString("AcMobileNo");
                                    String AcContactNo = jsonObject.getString("AcContactNo");
                                    String AcEmailAddress = jsonObject.getString("AcEmailAddress");
                                    String AcDebitBal = jsonObject.getString("AcDebitBal");
                                    String AcCreditBal = jsonObject.getString("AcCreditBal");
                                    String AcPassward = jsonObject.getString("AcPassward");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String ClientUserID = jsonObject.getString("ClientUserID");
                                    String SysCode = jsonObject.getString("SysCode");
                                    String NetCode = jsonObject.getString("NetCode");
                                    String ed = jsonObject.getString("UpdatedDate");
                                    JSONObject jbb = new JSONObject(ed);
                                    String UpdatedDate = jbb.getString("date");
                                    String SerialNo = jsonObject.getString("SerialNo");
                                    String UserRights = jsonObject.getString("UserRights");
                                    String SecurityRights = jsonObject.getString("SecurityRights");
                                    String Salary = jsonObject.getString("Salary");
                                    String SessionDate = jsonObject.getString("SessionDate");

                                    databaseHelper.createAccount3Name(new Account3Name(AcNameID,AcName,AcGroupID,AcAddress,AcMobileNo,AcContactNo,AcEmailAddress,AcDebitBal,AcCreditBal,AcPassward,ClientID,ClientUserID,SysCode,NetCode,UpdatedDate,SerialNo,UserRights,SecurityRights,Salary));
                                    updatedDate = SessionDate;
//                                    if (i == jsonArray.length() - 1) {
//                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Account3Name");
//                                        for (TableSession s : se){
//                                            s.setMaxID(AcNameID);
//                                            s.setInsertDate(SessionDate);
//                                            s.save();
//                                        }
//
//                                    }

                                }

                            }else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            updateAccount3Name();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mProgress.dismiss();
                Log.e("Error",error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ClientID", prefrence.getClientIDSession());
                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(AcNameID AS Int)) FROM Account3Name");
                params.put("MaxID", String.valueOf(maxID));
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void updateAccount3Name(){
        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshAccount3Name.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            String success = jsonObj.getString("success");
                            Log.e("Account3Name2",jsonObj.toString());
                            if (success.equals("1")){
                                JSONArray jsonArray = jsonObj.getJSONArray("Account3Name");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Account3Name",jsonObject.toString());
                                    String AcNameID = jsonObject.getString("AcNameID");
                                    String AcName = jsonObject.getString("AcName");
                                    String AcGroupID = jsonObject.getString("AcGroupID");
                                    String AcAddress = jsonObject.getString("AcAddress");
                                    String AcMobileNo = jsonObject.getString("AcMobileNo");
                                    String AcContactNo = jsonObject.getString("AcContactNo");
                                    String AcEmailAddress = jsonObject.getString("AcEmailAddress");
                                    String AcDebitBal = jsonObject.getString("AcDebitBal");
                                    String AcCreditBal = jsonObject.getString("AcCreditBal");
                                    String AcPassward = jsonObject.getString("AcPassward");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String ClientUserID = jsonObject.getString("ClientUserID");
                                    String SysCode = jsonObject.getString("SysCode");
                                    String NetCode = jsonObject.getString("NetCode");
                                    String ed = jsonObject.getString("UpdatedDate");
                                    JSONObject jbb = new JSONObject(ed);
                                    String UpdatedDate = jbb.getString("date");
                                    String SerialNo = jsonObject.getString("SerialNo");
                                    String UserRights = jsonObject.getString("UserRights");
                                    String SecurityRights = jsonObject.getString("SecurityRights");
                                    String Salary = jsonObject.getString("Salary");
                                    String SessionDate = jsonObject.getString("SessionDate");

                                    String query = "UPDATE Account3Name SET AcNameID = '"+AcNameID+"', AcName = '"+AcName+"', AcGroupID = '"+AcGroupID+"', AcAddress = '"+AcAddress+"', AcMobileNo = '"+AcMobileNo
                                            + "', AcContactNo = '"+AcContactNo+"', AcEmailAddress = '"+AcEmailAddress+"', AcDebitBal = '"+AcDebitBal+"', AcCreditBal = '"+AcCreditBal+"', AcPassward = '"+AcPassward
                                            + "', ClientID = '"+ClientID+"', ClientUserID = '"+ClientUserID+"', SysCode = '"+SysCode+"', NetCode = '"+NetCode+"', UpdatedDate = '"+UpdatedDate+"', SerialNo = '"+SerialNo
                                            +"', UserRights = '"+UserRights+"', SecurityRights = '"+SecurityRights+"', Salary '"+Salary+"' WHERE AcNameID = "+AcNameID;
                                    databaseHelper.updateAccount3Name(query);
                                    updatedDate = SessionDate;
//                                    if (i == jsonArray.length() - 1) {
//                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Account3Name");
//                                        for (TableSession s : se){
////                                            s.setMaxID(AcNameID);
//                                            s.setUpdateDate(SessionDate);
//                                            s.save();
//                                        }
//                                    }

                                }

                            }else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }Log.e("Sarem","CashBook1");
                            getCashBook1();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mProgress.dismiss();
                Log.e("Error",error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ClientID", prefrence.getClientIDSession());
                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(AcNameID AS Int)) FROM Account3Name");
                params.put("MaxID",String.valueOf(maxID));
                String date = databaseHelper.getClientUpdatedDate(prefrence.getClientIDSession());
                params.put("SessionDate",date);

                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getCashBook1(){

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshCashBook.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            String success = jsonObj.getString("success");
                            Log.e("CashBook1",jsonObj.toString());
                            if (success.equals("1")){
                                JSONArray jsonArray = jsonObj.getJSONArray("CashBook");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String CashBookID = jsonObject.getString("CashBookID");
                                    String cb = jsonObject.getString("CBDate");
                                    JSONObject jbb = new JSONObject(cb);
                                    String CBDate = jbb.getString("date");
                                    SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = ss.parse(CBDate);
                                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                                    String CBDate1 = sf.format(date);
                                    String DebitAccount = jsonObject.getString("DebitAccount");
                                    String CreditAccount = jsonObject.getString("CreditAccount");
                                    String CBRemark = jsonObject.getString("CBRemarks");
                                    String Amount = jsonObject.getString("Amount");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String ClientUserID = jsonObject.getString("ClientUserID");
                                    String NetCode = jsonObject.getString("NetCode");
                                    String SysCode = jsonObject.getString("SysCode");
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
//                                    JSONObject jb = new JSONObject(ed);
//                                    String UpdatedDate = jb.getString("date");
                                    String TableID = jsonObject.getString("TableID");
                                    String SessionDate = jsonObject.getString("SessionDate");
                                    String SerialNo = jsonObject.getString("SerialNo");
                                    String TableName = jsonObject.getString("TableName");

                                    databaseHelper.createCashBook(new CashBook(CashBookID,CBDate1,DebitAccount,CreditAccount,CBRemark,Amount,ClientID,ClientUserID,NetCode,SysCode,UpdatedDate,TableID, SerialNo, TableName));
                                    updatedDate = SessionDate;
//                                    if (i == jsonArray.length() - 1) {
//                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","CashBook");
//                                        for (TableSession s : se){
//                                            s.setMaxID(CashBookID);
//                                            s.setInsertDate(SessionDate);
//                                            s.save();
//                                        }
//                                    }
                                    getCashBook();

                                }

                            }else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }Log.e("Sarem","CashBook2");
                            updateCashBook();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mProgress.dismiss();
                Log.e("Error",error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ClientID", prefrence.getClientIDSession());
                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(CashBookID AS Int)) FROM CashBook");
                Log.e("MAXIDCASH", String.valueOf(maxID));
                params.put("MaxID",String.valueOf(maxID));

                Log.e("MAXID",String.valueOf(maxID));

                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void updateCashBook(){

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshCashBook.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            Log.e("CashBook2",response);
                            String success = jsonObj.getString("success");
                            if (success.equals("1")){
                                JSONArray jsonArray = jsonObj.getJSONArray("CashBook");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Account3Name",jsonObject.toString());
                                    String CashBookID = jsonObject.getString("CashBookID");
                                    String cb = jsonObject.getString("CBDate");
                                    JSONObject jbb = new JSONObject(cb);
                                    String CBDate = jbb.getString("date");
                                    SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = ss.parse(CBDate);
                                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                                    String CBDate1 = sf.format(date);
                                    String DebitAccount = jsonObject.getString("DebitAccount");
                                    String CreditAccount = jsonObject.getString("CreditAccount");
                                    String CBRemark = jsonObject.getString("CBRemarks");
                                    String Amount = jsonObject.getString("Amount");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String ClientUserID = jsonObject.getString("ClientUserID");
                                    String NetCode = jsonObject.getString("NetCode");
                                    String SysCode = jsonObject.getString("SysCode");
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
//                                    JSONObject jb = new JSONObject(ed);
//                                    String UpdatedDate = jb.getString("date");
                                    String TableID = jsonObject.getString("TableID");
                                    String SessionDate = jsonObject.getString("SessionDate");

                                    String query = "UPDATE CashBook SET CBDate = '"+CBDate1+"', DebitAccount = '"+DebitAccount+"', CreditAccount = '"+CreditAccount+"', CBRemarks = '"+CBRemark+"', Amount = '"+Amount+"', ClientID = '"+ClientID+"', ClientUserID = '"+ClientUserID+"', NetCode = '"+NetCode+"', SysCode = '"+SysCode+"', UpdatedDate = '"+UpdatedDate+"', TableID = '"+TableID+
                                            "' WHERE CashBookID = "+CashBookID;
                                    databaseHelper.updateCashBook(query);
                                    updatedDate = SessionDate;
                                    getCashBook();
//                                    if (i == jsonArray.length() - 1) {
//                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","CashBook");
//                                        for (TableSession s : se){
////                                            s.setMaxID(CashBookID);
//                                            s.setUpdateDate(SessionDate);
//                                            s.save();
//                                        }
//                                    }


                                }

                            }else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }Log.e("Sarem","CashBook3");
                            getBookings1();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mProgress.dismiss();
                Log.e("Error",error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ClientID", prefrence.getClientIDSession());
                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(CashBookID AS Int)) FROM CashBook");
                Log.e("MAXIDCASH", String.valueOf(maxID));
                params.put("MaxID",String.valueOf(maxID));
                String date = databaseHelper.getClientUpdatedDate(prefrence.getClientIDSession());
                params.put("SessionDate",date);

                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getBookings1(){
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshBooking.php";


        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Booking1",response);
                        String text = "", BookingID = "", ClientName = "", ClientMobile = "", ClientAddress = "", ClientNic = "", EventName = "", BookingDate = "", EventDate = "",
                                ArrangePersons ="", ChargesTotal = "",Description = "", ClientID ="", ClientUserID = "", NetCode = "",SysCode = "", UpdatedDate = "";
                        try {
                            JSONObject json = new JSONObject(response);
                            String success = json.getString("success");

                            if (success.equals("1")) {
                                JSONArray jsonArray = json.getJSONArray("Bookings");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    BookingID = jsonObject.getString("BookingID");
                                    ClientName = jsonObject.getString("ClientName");
                                    ClientMobile = jsonObject.getString("ClientMobile");
                                    ClientAddress = jsonObject.getString("ClientAddress");
                                    ClientNic = jsonObject.getString("ClientNic");
                                    EventName = jsonObject.getString("EventName");
                                    String bd = jsonObject.getString("BookingDate");
                                    JSONObject jbbb = new JSONObject(bd);
                                    BookingDate = jbbb.getString("date");
                                    String ed = jsonObject.getString("EventDate");
                                    JSONObject jb = new JSONObject(ed);
                                    EventDate = jb.getString("date");
                                    Log.e("TEST",EventDate);
                                    ArrangePersons = jsonObject.getString("ArrangePersons");
                                    ChargesTotal = jsonObject.getString("ChargesTotal");
                                    Description = jsonObject.getString("Description");
                                    ClientID = jsonObject.getString("ClientID");
                                    ClientUserID = jsonObject.getString("ClientUserID");
                                    NetCode = jsonObject.getString("NetCode");
                                    SysCode = jsonObject.getString("SysCode");
                                    Log.e("TEST","TEST");
                                    String up = jsonObject.getString("UpdatedDate");
                                    JSONObject jbb = new JSONObject(up);
                                    UpdatedDate = jbb.getString("date");
                                    String SessionDate = jsonObject.getString("SessionDate");
                                    String Shift = jsonObject.getString("Shift");
                                    String SerialNo = jsonObject.getString("SerialNo");

                                    databaseHelper.createBooking(new Bookings(BookingID,ClientName,ClientMobile,ClientAddress,ClientNic,EventName,BookingDate,EventDate,ArrangePersons,ChargesTotal,Description,ClientID,ClientUserID,NetCode,SysCode,UpdatedDate,Shift, SerialNo));
                                    updatedDate = SessionDate;
//                                    if (i == jsonArray.length() - 1) {
//                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Bookings");
//                                        for (TableSession s : se){
//                                            s.setMaxID(BookingID);
//                                            s.setInsertDate(SessionDate);
//                                            s.save();
//                                        }
//                                    }
                                }

//                                FetchFromDb();
//                                pDialog.dismiss();
                            }else {
//                                pDialog.dismiss();
                            }Log.e("Sarem","CashBook4");
                            updateBookings();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
//                pDialog.dismiss();
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ClientID", prefrence.getClientIDSession());
                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(BookingID AS Int)) FROM Booking");
                params.put("MaxID",String.valueOf(maxID));

                return params;
            }
        };

        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void updateBookings(){
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshBooking.php";


        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String text = "", BookingID = "", ClientName = "", ClientMobile = "", ClientAddress = "", ClientNic = "", EventName = "", BookingDate = "", EventDate = "",
                                ArrangePersons ="", ChargesTotal = "",Description = "", ClientID ="", ClientUserID = "", NetCode = "",SysCode = "", UpdatedDate = "";
                        try {
                            JSONObject json = new JSONObject(response);
                            String success = json.getString("success");
                            Log.e("Booking2",json.toString());

                            if (success.equals("1")) {
                                JSONArray jsonArray = json.getJSONArray("Bookings");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    BookingID = jsonObject.getString("BookingID");
                                    ClientName = jsonObject.getString("ClientName");
                                    ClientMobile = jsonObject.getString("ClientMobile");
                                    ClientAddress = jsonObject.getString("ClientAddress");
                                    ClientNic = jsonObject.getString("ClientNic");
                                    EventName = jsonObject.getString("EventName");
                                    String bd = jsonObject.getString("BookingDate");
                                    JSONObject jbbb = new JSONObject(bd);
                                    BookingDate = jbbb.getString("date");
                                    String ed = jsonObject.getString("EventDate");
                                    JSONObject jb = new JSONObject(ed);
                                    EventDate = jb.getString("date");
                                    Log.e("TEST",EventDate);
                                    ArrangePersons = jsonObject.getString("ArrangePersons");
                                    ChargesTotal = jsonObject.getString("ChargesTotal");
                                    Description = jsonObject.getString("Description");
                                    ClientID = jsonObject.getString("ClientID");
                                    ClientUserID = jsonObject.getString("ClientUserID");
                                    NetCode = jsonObject.getString("NetCode");
                                    SysCode = jsonObject.getString("SysCode");
                                    Log.e("TEST","TEST");
                                    String up = jsonObject.getString("UpdatedDate");
                                    JSONObject jbb = new JSONObject(up);
                                    UpdatedDate = jbb.getString("date");
                                    String SessionDate = jsonObject.getString("SessionDate");

                                    String query = "UPDATE Booking SET BookingID = '"+BookingID+"', ClientName = '"+ClientName+"', ClientMobile = '"+ClientMobile+"', ClientAddress = '"+ClientAddress+"', ClientNic = '"+ClientNic
                                            +"', EventName = '"+EventName+"', BookingDate = '"+BookingDate+"', EventDate = '"+EventDate+"', ArrangePersons ='"+ArrangePersons+"', ChargesTotal = '"+ChargesTotal+"', Description = '"+Description
                                            +"', ClientID = '"+ClientID+"', ClientUserID = '"+ClientUserID+"', NetCode = '"+NetCode+"', SysCode = '"+SysCode+"', UpdatedDate = '"+UpdatedDate+"' WHERE BookingID = "+ BookingID;
                                    databaseHelper.updateBooking(query);
                                    updatedDate = SessionDate;

//                                    if (i == jsonArray.length() - 1) {
//                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Bookings");
//                                        for (TableSession s : se){
////                                            s.setMaxID(BookingID);
//                                            s.setUpdateDate(SessionDate);
//                                            s.save();
//                                        }
//                                    }
                                }


//                                FetchFromDb();
//                                mProgress.dismiss();
                            }else {
//                                mProgress.dismiss();
                            }Log.e("Sarem","CashBook5");
                            addCashBook();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
//                pDialog.dismiss();
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ClientID", prefrence.getClientIDSession());
                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(BookingID AS Int)) FROM Booking");
                params.put("MaxID",String.valueOf(maxID));
                String date = databaseHelper.getClientUpdatedDate(prefrence.getClientIDSession());
                params.put("SessionDate",date);

                return params;
            }
        };

        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void addCashBook(){
        String query = "SELECT * FROM CashBook WHERE CashBookID = 0 AND UpdatedDate = 0";
        List<CashBook> addCashBook = databaseHelper.getCashBook(query);

        for (final CashBook c : addCashBook){
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/AddCashBook.php";

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("CashBook3",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equals("1")){
                                    String id = jsonObject.getString("CBID");
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE CashBook SET CashBookID = '"+id+"', UpdatedDate = '"+UpdatedDate+"' WHERE ID = "+c.getcId());
                                    updatedDate = UpdatedDate;
//                                    getCashBook();
//                                    List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","CashBook");
//                                    for (TableSession s : se){
//                                        s.setMaxID(id);
//                                        s.setInsertDate(UpdatedDate);
//                                        s.save();
//                                    }
                                    getCashBook();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error",error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();

                        params.put("CBDate", c.getCBDate());
                        params.put("DebitAccount", c.getDebitAccount());
                        params.put("CreditAccount", c.getCreditAccount());
                        params.put("CBRemarks", c.getCBRemarks());
                        params.put("Amount", c.getAmount());
                        params.put("ClientID", prefrence.getClientIDSession());
                        params.put("ClientUserID", prefrence.getClientUserIDSession());
                        params.put("NetCode", "0");
                        params.put("SysCode", "0");
                        params.put("TableID", c.getTableID());
                        params.put("SerialNo", c.getSerialNo());
                        params.put("TableName", c.getTableName());

                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }Log.e("Sarem","CashBook5");
        addCashBook2();
    }

    public void addCashBook2(){
        String query = "SELECT * FROM CashBook WHERE UpdatedDate = 0";
        List<CashBook> addCashBook = databaseHelper.getCashBook(query);
        Log.e("CASHBOOK UP", String.valueOf(addCashBook.size()));

        for (final CashBook c : addCashBook){
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/UpdateCashBook.php";

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("CashBook4",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("Success CB",success);
                                if (success.equals("1")){
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE CashBook SET UpdatedDate = '"+UpdatedDate+"' WHERE ID = "+c.getcId());

                                    updatedDate = UpdatedDate;
//                                    getCashBook();
//                                    List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","CashBook");
//                                    for (TableSession s : se){
//                                        s.setUpdateDate(UpdatedDate);
//                                        s.save();
//                                    }
                                    getCashBook();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgress.dismiss();
                    Log.e("Error",error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                        params.put("CashBookID", c.getCashBookID());
                        params.put("CBDate", c.getCBDate());
                        params.put("DebitAccount", c.getDebitAccount());
                        params.put("CreditAccount", c.getCreditAccount());
                        params.put("CBRemarks", c.getCBRemarks());
                        params.put("Amount", c.getAmount());
                        params.put("ClientID", prefrence.getClientIDSession());
                        params.put("ClientUserID", prefrence.getClientUserIDSession());
                        params.put("NetCode", "0");
                        params.put("SysCode", "0");
                        params.put("TableID", c.getTableID());
                        params.put("SerialNo", c.getSerialNo());

                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
        Log.e("Sarem","CashBook6");
        addBooking();
        //For Refresh Recycler
//        mProgress.dismiss();
    }

    public void addBooking(){
        String query = "SELECT * FROM Booking WHERE BookingID = 'o' AND UpdatedDate = 0";
        final List<Bookings> addBooking = databaseHelper.getBookings(query);
        Log.e("BookingID UP", String.valueOf(addBooking.size()));

        for (final Bookings c : addBooking){
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/AddEvent.php";

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Booking3",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("Success Boo",success);
                                if (success.equals("1")){
                                    String id = jsonObject.getString("BookingID");
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE Booking SET BookingID = '"+ id +"', UpdatedDate = '"+UpdatedDate+"' WHERE ID = "+c.getId());
                                    updatedDate = UpdatedDate;
//                                    List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Bookings");
//                                    for (TableSession s : se){
//                                        s.setMaxID(id);
//                                        s.setInsertDate(UpdatedDate);
//                                        s.save();
//                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }Log.e("Sarem","CashBook7");
//                            addAccount3Name();
                            mProgress.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgress.dismiss();
                    Log.e("Error",error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                        params.put("ClientName", c.getClientName());
                        params.put("ClientMobile", c.getClientMobile());
                        params.put("ClientAddress", c.getClientAddress());
                        params.put("ClientNic", c.getClientNic());
                        params.put("EventName", c.getEventName());
                        params.put("BookingDate", c.getBookingDate());
                        params.put("EventDate", c.getEventDate());
                        params.put("ArrangePersons", c.getArrangePersons());
                        params.put("ChargesTotal", c.getChargesTotal());
                        params.put("Description", c.getDescription());
                        params.put("ClientID", prefrence.getClientIDSession());
                        params.put("ClientUserID", prefrence.getClientUserIDSession());
                        params.put("NetCode", "0");
                        params.put("SysCode", "0");
                        params.put("DebitAccount", cashID);
                        params.put("CreditAccount", incomeID);
                        params.put("Amount", c.getAmount());
                        params.put("Shift", c.getShift());
                        params.put("SerialNo", c.getSerialNo());

                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
        updateBooking();
    }

    public void updateBooking(){
        String query = "SELECT * FROM Booking WHERE UpdatedDate = 0 AND BookingID != 'o'";
        final List<Bookings> addBooking = databaseHelper.getBookings(query);
        Log.e("BookingID UP", String.valueOf(addBooking.size()));

        for (final Bookings c : addBooking){
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/UpdateEvent.php";

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Booking4",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("Success CB",success);
                                if (success.equals("1")){
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE Booking SET UpdatedDate = '"+UpdatedDate+"' WHERE ID = "+c.getId());
                                    updatedDate = UpdatedDate;
//                                    List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Bookings");
//                                    for (TableSession s : se){
//                                        s.setUpdateDate(UpdatedDate);
//                                        s.save();
//                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgress.dismiss();
                    Log.e("Error",error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                        params.put("BookingID", c.getBookingID());
                        params.put("ClientName", c.getClientName());
                        params.put("ClientMobile", c.getClientMobile());
                        params.put("ClientAddress", c.getClientAddress());
                        params.put("ClientNic", c.getClientNic());
                        params.put("EventName", c.getEventName());
                        params.put("BookingDate", c.getBookingDate());
                        params.put("EventDate", c.getEventDate());
                        params.put("ArrangePersons", c.getArrangePersons());
                        params.put("ChargesTotal", c.getChargesTotal());
                        params.put("Description", c.getDescription());
                        params.put("ClientID", prefrence.getClientIDSession());
                        params.put("ClientUserID", prefrence.getClientUserIDSession());
                        params.put("NetCode", "0");
                        params.put("SysCode", "0");

                        params.put("DebitAccount", cashID);
                        params.put("CreditAccount", incomeID);

                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
        addAccount3Name();
    }

    public void addAccount3Name(){
        String query = "SELECT * FROM Account3Name WHERE AcNameID = 0 AND UpdatedDate = 0";
        final List<Account3Name> addBooking = databaseHelper.getAccount3Name(query);
        Log.e("BookingID UP", String.valueOf(addBooking.size()));

        for (final Account3Name c : addBooking){
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/AddCharofAcc.php";

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Account3Name3",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("Success CB",success);
                                if (success.equals("1")){
                                    String id = jsonObject.getString("AcNameID");
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE Account3Name SET AcNameID = '"+ id +"', UpdatedDate = '"+UpdatedDate+"' WHERE ID = "+c.getId());
                                    updatedDate = UpdatedDate;
//                                    List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Account3Name");
//                                    for (TableSession s : se){
//                                        s.setMaxID(id);
//                                        s.setInsertDate(UpdatedDate);
//                                        s.save();
//                                    }
                                }else {
                                    databaseHelper.deleteAccount3NameEntry("DELETE FROM Account3Name WHERE ID = "+c.getId());
                                    String message = jsonObject.getString("message");
                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mProgress.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgress.dismiss();
                    Log.e("Error",error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();

                        params.put("AcName", c.getAcName());
                        params.put("AcAddress", c.getAcAddress());
                        params.put("AcContactNo", c.getAcContactNo());
                        params.put("AcEmailAddress", c.getAcEmailAddress());
                        params.put("Salary", c.getSalary());
                        params.put("AcMobileNo", c.getAcMobileNo());
                        params.put("AcPassward", c.getAcPassward());
                        params.put("SecurityRights", c.getSecurityRights());
                        params.put("ClientID", prefrence.getClientIDSession());
                        params.put("AcGroupID", c.getAcGroupID());
                        params.put("SerialNo", c.getSerialNo());

                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
//        mProgress.dismiss();
        updateAccount3Name1();
    }

    public void updateAccount3Name1(){
        String query = "SELECT * FROM Account3Name WHERE UpdatedDate = 0";
        final List<Account3Name> addBooking = databaseHelper.getAccount3Name(query);
        Log.e("BookingID UP", String.valueOf(addBooking.size()));

        for (final Account3Name c : addBooking){
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/UpdateCharofAcc.php";

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Account3Name4",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("Success CB",success);
                                if (success.equals("1")){
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE Account3Name SET UpdatedDate = '"+UpdatedDate+"' WHERE ID = "+c.getId());
                                    updatedDate = UpdatedDate;
//                                    List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Account3Name");
//                                    for (TableSession s : se){
//                                        s.setUpdateDate(UpdatedDate);
//                                        s.save();
//                                    }
                                    databaseHelper.updateClient("UPDATE Client SET UpdatedDate = '"+updatedDate+"' WHERE ClientID = "+prefrence.getClientIDSession());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mProgress.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgress.dismiss();
                    Log.e("Error",error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();

                        params.put("AcNameID", c.getAcNameID());
                        params.put("AcName", c.getAcName());
                        params.put("AcAddress", c.getAcAddress());
                        params.put("AcContactNo", c.getAcContactNo());
                        params.put("AcEmailAddress", c.getAcEmailAddress());
                        params.put("Salary", c.getSalary());
                        params.put("AcMobileNo", c.getAcMobileNo());
                        params.put("AcPassward", c.getAcPassward());
                        params.put("SecurityRights", c.getSecurityRights());
                        params.put("ClientID", prefrence.getClientIDSession());
                        params.put("AcGroupID", c.getAcGroupID());

                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
        getCashBook();
        if (addBooking.size() == 0){
            databaseHelper.updateClient("UPDATE Client SET UpdatedDate = '"+updatedDate+"' WHERE ClientID = "+prefrence.getClientIDSession());
        }
        mProgress.dismiss();
    }
}

