package org.by9steps.shadihall.fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintJob;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.print.PrintHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.CashBookSettingActivity;
import org.by9steps.shadihall.activities.CashCollectionActivity;
import org.by9steps.shadihall.activities.SplashActivity;
import org.by9steps.shadihall.adapters.CashBookAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.CBSetting;
import org.by9steps.shadihall.model.CBUpdate;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.CashEntry;
import org.by9steps.shadihall.model.UpdateDate;
import org.by9steps.shadihall.model.User;
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

import android.widget.AdapterView.OnItemSelectedListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashBookFragment extends Fragment implements OnItemSelectedListener, View.OnClickListener {

    ProgressDialog mProgress;
    RecyclerView recyclerView;
    EditText search;
    Spinner spinner;
//    ScrollView scrollView;
    HorizontalScrollView scrollView;
    LinearLayout header, pdfView;
    TextView tv_date,tv_id,tv_debit,tv_credit,tv_remarks,tv_amount;

    List<CashEntry> mList;
    List<CashBook> cashBooksList;
    List<CashEntry> filterd;
    CashBookAdapter adapter;
    DatabaseHelper databaseHelper;

    int m = 0, amount, gAmount , filter;
    Boolean listSorting = false;
    private Bitmap bitmap;
    public static String fDate1, fDate2;
    public static Button date1;
    public static Button date2;
    String orderBy = "CBDate";

    private static final String TAG = "PdfCreatorActivity";
    private File pdfFile;

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
        search = view.findViewById(R.id.search);
        spinner = view.findViewById(R.id.spinner);
        tv_date = view.findViewById(R.id.tv_date);
        tv_id = view.findViewById(R.id.tv_id);
        tv_debit = view.findViewById(R.id.tv_debit);
        tv_credit = view.findViewById(R.id.tv_credit);
        tv_remarks = view.findViewById(R.id.tv_remarks);
        tv_amount = view.findViewById(R.id.tv_amount);

        databaseHelper = new DatabaseHelper(getContext());

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

//        header.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listSorting = !listSorting;
//                getCashBook();
////                doPrint();
//            }
//        });

        List<CBSetting> list = CBSetting.listAll(CBSetting.class);
        if (list.size() == 0){
            CBSetting cbSetting = new CBSetting(true,true,true,true,true,true);
            cbSetting.save();
        }


//        getCashBook();
//        getRecoveries();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (search.getText().toString().equals("")){
                    filter = 0;
//                    spinner.setSelection(0);
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_date:
                orderBy = "CBDate";
                getCashBook();
                break;
            case R.id.tv_id:
                orderBy = "CashBookID";
                getCashBook();
                break;
            case R.id.tv_debit:
                orderBy = "DebitAccount";
                getCashBook();
                break;
            case R.id.tv_credit:
                orderBy = "CreditAccount";
                getCashBook();
                break;
            case R.id.tv_remarks:
                orderBy = "CBRemarks";
                getCashBook();
                break;
            case R.id.tv_amount:
                orderBy = "Amount";
                getCashBook();
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
                customPDF();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            return true;
        }else if (item.getItemId() == R.id.action_settings){
            startActivity(new Intent(getContext(), CashBookSettingActivity.class));
            return true;
        }else if (item.getItemId() == R.id.action_refresh){
            if (isConnected()){
                updateCashBook();
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

        List<User> list = User.listAll(User.class);
        for (User u : list){
//            query = "SELECT        CashBook.CashBookID, CashBook.CBDate, CashBook.DebitAccount, CashBook.CreditAccount, CashBook.CBRemarks, CashBook.Amount, CashBook.ClientID, CashBook.ClientUserID, CashBook.BookingID, \n" +
//                    "                         Account3Name.AcName AS DebitAccountName, Account3Name_1.AcName AS CreditAccountName, Account3Name_2.AcName AS UserName, CashBook.UpdatedDate\n" +
//                    "FROM            CashBook INNER JOIN\n" +
//                    "                         Account3Name ON CashBook.DebitAccount = Account3Name.AcNameID INNER JOIN\n" +
//                    "                         Account3Name AS Account3Name_1 ON CashBook.CreditAccount = Account3Name_1.AcNameID INNER JOIN\n" +
//                    "                         Account3Name AS Account3Name_2 ON CashBook.ClientUserID = Account3Name_2.AcNameID\n" +
//                    "WHERE        (CashBook.ClientID = "+u.getClientID()+")";
            query = "SELECT      CashBook.ID, CashBook.CashBookID, CashBook.CBDate, CashBook.DebitAccount, CashBook.CreditAccount, CashBook.CBRemarks, CashBook.Amount, CashBook.ClientID, CashBook.ClientUserID, CashBook.BookingID, \n" +
                    "                         Account3Name.AcName AS DebitAccountName, Account3Name_1.AcName AS CreditAccountName, Account3Name_2.AcName AS UserName, CashBook.UpdatedDate\n" +
                    "FROM            CashBook LEFT OUTER JOIN\n" +
                    "                         Account3Name AS Account3Name_1 ON CashBook.CreditAccount = Account3Name_1.AcNameID LEFT OUTER JOIN\n" +
                    "                         Account3Name AS Account3Name_2 ON CashBook.ClientUserID = Account3Name_2.AcNameID LEFT OUTER JOIN\n" +
                    "                         Account3Name ON CashBook.DebitAccount = Account3Name.AcNameID\n" +
                    "WHERE        (CashBook.ClientID = "+u.getClientID()+") ORDER BY "+orderBy+" DESC";
            cashBooksList = databaseHelper.getCashBookEntry(query);
        }

//        String s = "SELECT * FROM CashBook WHERE ClientID = 69";
//        databaseHelper.getCashBook(s);

//        if (!listSorting){
//            for (int i = cashBooksList.size()-1; i >= 0; i--){
//                String[] separated = cashBooksList.get(i).getCBDate().split("-");
//                if (m == 0) {
//                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
//                    mList.add(CashEntry.createRow(cashBooksList.get(i).getCashBookID(),cashBooksList.get(i).getCBDate(),cashBooksList.get(i).getDebitAccount(),cashBooksList.get(i).getCreditAccount(),cashBooksList.get(i).getCBRemarks(),cashBooksList.get(i).getAmount(),cashBooksList.get(i).getClientID(),cashBooksList.get(i).getClientUserID(),cashBooksList.get(i).getBookingID(),cashBooksList.get(i).getDebitAccountName(),cashBooksList.get(i).getCreditAccountName(),cashBooksList.get(i).getUserName(), cashBooksList.get(i).getUpdatedDate()));
//                    m = Integer.valueOf(separated[1]);
//
//                    amount = Integer.valueOf(cashBooksList.get(i).getAmount()) + amount;
//                    gAmount = Integer.valueOf(cashBooksList.get(i).getAmount()) + gAmount;
//                }else if (m == Integer.valueOf(separated[1])){
//                    amount = Integer.valueOf(cashBooksList.get(i).getAmount()) + amount;
//                    gAmount = Integer.valueOf(cashBooksList.get(i).getAmount()) + gAmount;
//                    mList.add(CashEntry.createRow(cashBooksList.get(i).getCashBookID(),cashBooksList.get(i).getCBDate(),cashBooksList.get(i).getDebitAccount(),cashBooksList.get(i).getCreditAccount(),cashBooksList.get(i).getCBRemarks(),cashBooksList.get(i).getAmount(),cashBooksList.get(i).getClientID(),cashBooksList.get(i).getClientUserID(),cashBooksList.get(i).getBookingID(),cashBooksList.get(i).getDebitAccountName(),cashBooksList.get(i).getCreditAccountName(),cashBooksList.get(i).getUserName(), cashBooksList.get(i).getUpdatedDate()));
//                }else {
//                    mList.add(CashEntry.createTotal(String.valueOf(amount)));
//                    amount = 0;
//                    amount = Integer.valueOf(cashBooksList.get(i).getAmount()) + amount;
//                    gAmount = Integer.valueOf(cashBooksList.get(i).getAmount()) + gAmount;
//
//                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
//                    mList.add(CashEntry.createRow(cashBooksList.get(i).getCashBookID(),cashBooksList.get(i).getCBDate(),cashBooksList.get(i).getDebitAccount(),cashBooksList.get(i).getCreditAccount(),cashBooksList.get(i).getCBRemarks(),cashBooksList.get(i).getAmount(),cashBooksList.get(i).getClientID(),cashBooksList.get(i).getClientUserID(),cashBooksList.get(i).getBookingID(),cashBooksList.get(i).getDebitAccountName(),cashBooksList.get(i).getCreditAccountName(),cashBooksList.get(i).getUserName(), cashBooksList.get(i).getUpdatedDate()));
//                    m = Integer.valueOf(separated[1]);
//                }
//            }
//        }else {
            for (CashBook c : cashBooksList){

                String[] separated = c.getCBDate().split("-");

                if (m == 0) {
                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]+"/"+separated[2]));
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getBookingID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
                    m = Integer.valueOf(separated[2]);

                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;
                }else if (m == Integer.valueOf(separated[2])){
                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getBookingID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
                }else {
                    mList.add(CashEntry.createTotal(String.valueOf(amount)));
                    amount = 0;
                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;

                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]+"/"+separated[2]));
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getBookingID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
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

    private void filter(String text) {Log.e("Search","SEARCH");
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
//                else if (s.isRow() == 0){
//                    filterd.add(s);
//                }else if (s.isRow() == 2){
//                    filterd.add(s);
//                }
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
                break;
            case 1:
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
                break;
            case 3:
                filter = 3;
                break;
            case 4:
                filter = 4;
                break;
            case 5:
                filter = 5;
                break;
            case 6:
                filter = 6;
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

        List<User> list = User.listAll(User.class);
        for (User u : list){
//            query = "SELECT        CashBook.CashBookID, CashBook.CBDate, CashBook.DebitAccount, CashBook.CreditAccount, CashBook.CBRemarks, CashBook.Amount, CashBook.ClientID, CashBook.ClientUserID, CashBook.BookingID, \n" +
//                    "                         Account3Name.AcName AS DebitAccountName, Account3Name_1.AcName AS CreditAccountName, Account3Name_2.AcName AS UserName, CashBook.UpdatedDate\n" +
//                    "FROM            CashBook INNER JOIN\n" +
//                    "                         Account3Name ON CashBook.DebitAccount = Account3Name.AcNameID INNER JOIN\n" +
//                    "                         Account3Name AS Account3Name_1 ON CashBook.CreditAccount = Account3Name_1.AcNameID INNER JOIN\n" +
//                    "                         Account3Name AS Account3Name_2 ON CashBook.ClientUserID = Account3Name_2.AcNameID\n" +
//                    "WHERE        (CashBook.ClientID = "+u.getClientID()+")";
            query = "SELECT        CashBook.CashBookID, CashBook.CBDate, CashBook.DebitAccount, CashBook.CreditAccount, CashBook.CBRemarks, CashBook.Amount, CashBook.ClientID, CashBook.ClientUserID, CashBook.BookingID, \n" +
                    "                         Account3Name.AcName AS DebitAccountName, Account3Name_1.AcName AS CreditAccountName, Account3Name_2.AcName AS UserName, CashBook.UpdatedDate\n" +
                    "FROM            CashBook LEFT OUTER JOIN\n" +
                    "                         Account3Name AS Account3Name_1 ON CashBook.CreditAccount = Account3Name_1.AcNameID LEFT OUTER JOIN\n" +
                    "                         Account3Name AS Account3Name_2 ON CashBook.ClientUserID = Account3Name_2.AcNameID LEFT OUTER JOIN\n" +
                    "                         Account3Name ON CashBook.DebitAccount = Account3Name.AcNameID\n" +
                    "WHERE        (CashBook.ClientID = "+u.getClientID()+" AND CashBook.CBDate >= Datetime('"+fDate1+"') AND CashBook.CBDate <= Datetime('"+fDate2+"'))";
            cashBooksList = databaseHelper.getCashBookEntry(query);
        }

//        String s = "SELECT * FROM CashBook WHERE ClientID = 69";
//        databaseHelper.getCashBook(s);

        if (listSorting){
            for (int i = cashBooksList.size()-1; i >= 0; i--){
                String[] separated = cashBooksList.get(i).getCBDate().split("-");
                if (m == 0) {
                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
                    mList.add(CashEntry.createRow(cashBooksList.get(i).getCashBookID(),cashBooksList.get(i).getCBDate(),cashBooksList.get(i).getDebitAccount(),cashBooksList.get(i).getCreditAccount(),cashBooksList.get(i).getCBRemarks(),cashBooksList.get(i).getAmount(),cashBooksList.get(i).getClientID(),cashBooksList.get(i).getClientUserID(),cashBooksList.get(i).getBookingID(),cashBooksList.get(i).getDebitAccountName(),cashBooksList.get(i).getCreditAccountName(),cashBooksList.get(i).getUserName(), cashBooksList.get(i).getUpdatedDate()));
                    m = Integer.valueOf(separated[1]);

                    amount = Integer.valueOf(cashBooksList.get(i).getAmount()) + amount;
                    gAmount = Integer.valueOf(cashBooksList.get(i).getAmount()) + gAmount;
                }else if (m == Integer.valueOf(separated[1])){
                    amount = Integer.valueOf(cashBooksList.get(i).getAmount()) + amount;
                    gAmount = Integer.valueOf(cashBooksList.get(i).getAmount()) + gAmount;
                    mList.add(CashEntry.createRow(cashBooksList.get(i).getCashBookID(),cashBooksList.get(i).getCBDate(),cashBooksList.get(i).getDebitAccount(),cashBooksList.get(i).getCreditAccount(),cashBooksList.get(i).getCBRemarks(),cashBooksList.get(i).getAmount(),cashBooksList.get(i).getClientID(),cashBooksList.get(i).getClientUserID(),cashBooksList.get(i).getBookingID(),cashBooksList.get(i).getDebitAccountName(),cashBooksList.get(i).getCreditAccountName(),cashBooksList.get(i).getUserName(), cashBooksList.get(i).getUpdatedDate()));
                }else {
                    mList.add(CashEntry.createTotal(String.valueOf(amount)));
                    amount = 0;
                    amount = Integer.valueOf(cashBooksList.get(i).getAmount()) + amount;
                    gAmount = Integer.valueOf(cashBooksList.get(i).getAmount()) + gAmount;

                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
                    mList.add(CashEntry.createRow(cashBooksList.get(i).getCashBookID(),cashBooksList.get(i).getCBDate(),cashBooksList.get(i).getDebitAccount(),cashBooksList.get(i).getCreditAccount(),cashBooksList.get(i).getCBRemarks(),cashBooksList.get(i).getAmount(),cashBooksList.get(i).getClientID(),cashBooksList.get(i).getClientUserID(),cashBooksList.get(i).getBookingID(),cashBooksList.get(i).getDebitAccountName(),cashBooksList.get(i).getCreditAccountName(),cashBooksList.get(i).getUserName(), cashBooksList.get(i).getUpdatedDate()));
                    m = Integer.valueOf(separated[1]);
                }
            }
        }else {
            for (CashBook c : cashBooksList){

                String[] separated = c.getCBDate().split("-");

                if (m == 0) {
                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getBookingID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
                    m = Integer.valueOf(separated[1]);

                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;
                }else if (m == Integer.valueOf(separated[1])){
                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getBookingID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
                }else {
                    mList.add(CashEntry.createTotal(String.valueOf(amount)));
                    amount = 0;
                    amount = Integer.valueOf(c.getAmount()) + amount;
                    gAmount = Integer.valueOf(c.getAmount()) + gAmount;

                    mList.add(CashEntry.createSection(separated[0]+"/"+separated[1]));
                    mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),c.getCBRemarks(),c.getAmount(),c.getClientID(),c.getClientUserID(),c.getBookingID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), c.getUpdatedDate()));
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

    public void updateCashBook(){

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/AddCashBook.php";

        mProgress = new ProgressDialog(getContext());
        mProgress.setMessage("Loading...");
        mProgress.setCancelable(false);
        mProgress.show();

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response",response);
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            String success = jsonObj.getString("success");
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
                                    String ed = jsonObject.getString("UpdatedDate");
                                    JSONObject jb = new JSONObject(ed);
                                    String UpdatedDat = jb.getString("date");
                                    String BookingID = jsonObject.getString("BookingID");

                                    List<UpdateDate> list = UpdateDate.listAll(UpdateDate.class);

                                    for (UpdateDate u : list){
                                        if (Integer.valueOf(CashBookID) > Integer.valueOf(u.getMaxID())){
                                            databaseHelper.createCashBook(new CashBook(CashBookID,CBDate1,DebitAccount,CreditAccount,CBRemark,Amount,ClientID,ClientUserID,NetCode,SysCode,UpdatedDat,BookingID));
                                        }else{
                                            String query = "UPDATE CashBook SET CBDate = '"+CBDate1+"', SET DebitAccount = '"+DebitAccount+"', CreditAccount = '"+CreditAccount+"', CBRemarks '"+CBRemark+"', Amount = '"+Amount+"', ClientID = '"+ClientID+"', ClientUserID = '"+ClientUserID+"', NetCode = '"+NetCode+"', SysCode = '"+SysCode+"', UpdatedDate = '"+UpdatedDat+"', BookingID = '"+BookingID+
                                                    "' WHERE CashBookID = "+CashBookID;
                                            databaseHelper.updateCashBook(query);
                                        }
                                    }


                                    Date da = new Date();
                                    SimpleDateFormat sff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String d = sff.format(da);

                                    if (i == jsonArray.length() - 1) {
                                        UpdateDate updatedDate = UpdateDate.findById(UpdateDate.class, 1);
                                        updatedDate.setCbDate(UpdatedDat);
                                        updatedDate.setMaxID(CashBookID);
                                        updatedDate.save();
                                    }
                                }

                                sendUpdtae();
                            }else if (success.equals("2")){
                                sendUpdtae();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
                mProgress.dismiss();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                List<User> list = User.listAll(User.class);
                List<UpdateDate> date = UpdateDate.listAll(UpdateDate.class);
                for (User u : list) {
                    for (UpdateDate d :date) {
                        params.put("CBDate", d.getCbDate());
                        params.put("ClientID", u.getClientID());
                    }
                }
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void sendUpdtae(){

        List<CBUpdate> list = CBUpdate.listAll(CBUpdate.class);
        if (list.size() == 0){
            addCashBook();
        }

        for (final CBUpdate c : list){
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/UpdateCashBook.php";

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Update",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("Success",success);
                                if (success.equals("1")){
                                    String message = jsonObject.getString("message");
//                                    Toast.makeText(CashCollectionActivity.this, message, Toast.LENGTH_SHORT).show();
//                                    CBUpdate.deleteAll(CBUpdate.class);
                                }
                                addCashBook();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error",error.toString());
                    mProgress.dismiss();
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    List<User> list = User.listAll(User.class);
                    for (User u : list) {
                        params.put("CashBookID", c.getCashBookID());
                        params.put("CBDate", c.getCBDate());
                        params.put("DebitAccount", c.getDebitAccount());
                        params.put("CreditAccount", c.getCreditAccount());
                        params.put("CBRemarks", c.getCBRemarks());
                        params.put("Amount", c.getAmount());
                        params.put("ClientID", u.getClientID());
                        params.put("ClientUserID", u.getClientUserID());
                        params.put("NetCode", "0");
                        params.put("SysCode", "0");
                        params.put("BookingID", "0");
                    }
                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
    }

    public void addCashBook(){
        String query = "SELECT * FROM CashBook WHERE CashBookID = 0";
        List<CashBook> addCashBook = databaseHelper.getCashBook(query);
        if (addCashBook.size() == 0){
            mProgress.dismiss();
        }

        for (final CashBook c : addCashBook){
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/AddCashBook.php";

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("ADD",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("Success",success);
                                if (success.equals("1")){
                                    String id = jsonObject.getString("CBID");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE CashBook SET CashBookID = '"+id+"' WHERE ID = "+c.getcId());
//                                    Toast.makeText(CashCollectionActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                                getCashBook();
                                mProgress.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error",error.toString());
                    mProgress.dismiss();
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    List<User> list = User.listAll(User.class);
                    for (User u : list) {
                        params.put("CBDate", c.getCBDate());
                        params.put("DebitAccount", c.getDebitAccount());
                        params.put("CreditAccount", c.getCreditAccount());
                        params.put("CBRemarks", c.getCBRemarks());
                        params.put("Amount", c.getAmount());
                        params.put("ClientID", u.getClientID());
                        params.put("ClientUserID", u.getClientUserID());
                        params.put("NetCode", "0");
                        params.put("SysCode", "0");
                        params.put("BookingID", c.getBookingID());
                    }
                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
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

    public void customPDF() throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }

        String pdfname = "CashBook.pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{3, 3, 3, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("Date");
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

        if (filter > 1){
            for (CashEntry c : filterd){
                table.addCell(c.getCBDate());
                table.addCell(c.getCashBookID());
                table.addCell(c.getDebitAccountName());
                table.addCell(c.getCreditAccountName());
                table.addCell(c.getCBRemarks());
                table.addCell(c.getAmount());
            }
        }else {
            for (CashBook c : cashBooksList){
                table.addCell(c.getCBDate());
                table.addCell(c.getCashBookID());
                table.addCell(c.getDebitAccountName());
                table.addCell(c.getCreditAccountName());
                table.addCell(c.getCBRemarks());
                table.addCell(c.getAmount());
            }
        }

        PdfWriter.getInstance(document, output);
        document.open();

        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.BLACK);
        Paragraph paragraph = new Paragraph("Cash Book \n\n", f);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        document.add(table);
        document.close();




        customPDFView();

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
}

