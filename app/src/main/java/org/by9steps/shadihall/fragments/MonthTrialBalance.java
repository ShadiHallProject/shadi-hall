package org.by9steps.shadihall.fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.MonthTrialBalanceAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.MonthTb;
import org.by9steps.shadihall.model.Summerize;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthTrialBalance extends Fragment implements View.OnClickListener {

    DatabaseHelper databaseHelper;
    Prefrence prefrence;
    Boolean section = true;

    static Button date_picker1, date_picker2;
    ImageView refresh;
    RecyclerView recyclerView;
    SearchView searchView;
    Spinner spinner;

    List<MonthTb> monthTbList;
    List<MonthTb> filterdList;
    List<MonthTb> mList = new ArrayList<>();

    MonthTrialBalanceAdapter adapter;
    int filter;
    //Print
    private static final String TAG = "PdfCreatorActivity";
    private File pdfFile;

    String orderBy = "AcName";
    int status = 0;
    String orderby = " ORDER BY " + orderBy + " ASC";

    int preDebit = 0, preCredit = 0, traDebit = 0, traCredit = 0, cloDebit = 0, cloCredit = 0;
    int gPreDebit = 0, gPreCredit = 0, gTraDebit = 0, gTraCredit = 0, gCloDebit = 0, gCloCredit = 0;
    int mPreDebit = 0, mPreCredit = 0, mTraDebit = 0, mTraCredit = 0, mCloDebit = 0, mCloCredit = 0;
    Boolean group = false;

    public MonthTrialBalance() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_month_trial_balance, container, false);

        setHasOptionsMenu(true);
        databaseHelper = new DatabaseHelper(getContext());
        prefrence = new Prefrence(getContext());

        date_picker1 = view.findViewById(R.id.date_picker1);
        date_picker2 = view.findViewById(R.id.date_picker2);
        refresh = view.findViewById(R.id.refresh);
        spinner = view.findViewById(R.id.mtb_trial_spinner);
        searchView = view.findViewById(R.id.mtb_trial_search);
        recyclerView = view.findViewById(R.id.recycler);

        date_picker1.setOnClickListener(this);
        date_picker2.setOnClickListener(this);
        refresh.setOnClickListener(this);

        //Current Date
        Date date = new Date();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        date_picker1.setText(curFormater.format(date));
        date_picker2.setText(curFormater.format(date));

        // Spinner Drop down elements
        List<String> spinner_list = new ArrayList<String>();
        spinner_list.add("Select");
        spinner_list.add("AcType");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinner_list);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        filter = 0;
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        break;
                    case 1:
                        filter = 1;
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        break;
                    case 2:
                        filter = 2;
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        break;
                    case 3:
                        filter = 3;
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        break;
                    case 4:
                        filter = 4;
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

        getMonthTB();
        return  view;
    }

    public void getMonthTB() {
        preDebit = 0; preCredit = 0; traDebit = 0; traCredit = 0; cloDebit = 0; cloCredit = 0;
        gPreDebit = 0; gPreCredit = 0; gTraDebit = 0; gTraCredit = 0; gCloDebit = 0; gCloCredit = 0;
        mPreDebit = 0; mPreCredit = 0; mTraDebit = 0; mTraCredit = 0; mCloDebit = 0; mCloCredit = 0;
        mList.clear();

            String query = "SELECT        SixColumnTB.ClientID, SixColumnTB.AccountID, SUM(SixColumnTB.Debit) AS Debit, SUM(SixColumnTB.Credit) AS Credit, SUM(SixColumnTB.PrvBal) AS PrvBal, SUM(SixColumnTB.PrvDebit) AS PrvDebit, \n" +
                    "                         SUM(SixColumnTB.PrvCredit) AS PrvCredit, SUM(SixColumnTB.TraDebit) AS TraDebit, SUM(SixColumnTB.TraCredit) AS TraCredit, SUM(SixColumnTB.TraBalance) AS TraBalance, \n" +
                    "                         SUM(SixColumnTB.PrvBal + SixColumnTB.TraBalance) AS ClosingBalnce, CASE WHEN (SUM(PrvBal + TraBalance) > 0) THEN SUM(PrvBal + TraBalance) ELSE 0 END AS ClosingDebit, CASE WHEN (SUM(PrvBal + TraBalance) \n" +
                    "                         < 0) THEN SUM(PrvBal + TraBalance) ELSE 0 END AS ClosingCredit, Account3Name.AcName, Account2Group.AcGroupID, Account2Group.AcGruopName, Account1Type.AcTypeID, Account1Type.AcTypeName\n" +
                    "FROM            (SELECT        ClientID, AccountID, SUM(Debit) AS Debit, SUM(Credit) AS Credit, SUM(Debit) - SUM(Credit) AS PrvBal, CASE WHEN (SUM(Debit) - SUM(Credit)) > 0 THEN (SUM(Debit) - SUM(Credit)) ELSE 0 END AS PrvDebit, \n" +
                    "                                                    CASE WHEN (SUM(Debit) - SUM(Credit)) < 0 THEN (SUM(Debit) - SUM(Credit)) ELSE 0 END AS PrvCredit, 0 AS TraDebit, 0 AS TraCredit, 0 AS TraBalance\n" +
                    "                          FROM            (SELECT        CreditAccount AS AccountID, 0 AS Debit, Amount AS Credit, ClientID, CBDate\n" +
                    "                                                    FROM            CashBook AS CashBook\n" +
                    "                                                    WHERE        (ClientID = " + prefrence.getClientIDSession() + ") AND (CBDate < '" + date_picker1.getText().toString() + "')\n" +
                    "                                                    UNION ALL\n" +
                    "                                                    SELECT        DebitAccount AS AccountID, Amount AS Debit, 0 AS Credit, ClientID, CBDate\n" +
                    "                                                    FROM            CashBook AS CashBook_1\n" +
                    "                                                    WHERE        (ClientID = " + prefrence.getClientIDSession() + ") AND (CBDate < '" + date_picker1.getText().toString() + "')) AS derivedtbl_1\n" +
                    "                          GROUP BY ClientID, AccountID\n" +
                    "                          UNION ALL\n" +
                    "                          SELECT        ClientID, AccountID, 0 AS Debit, 0 AS Credit, 0 AS PrvBalance, 0 AS PrvDebit, 0 AS PrvCredit, SUM(Debit) AS TraDebit, SUM(Credit) AS TraCredit, SUM(Debit) - SUM(Credit) AS TraBal\n" +
                    "                          FROM            (SELECT        CreditAccount AS AccountID, 0 AS Debit, Amount AS Credit, ClientID, CBDate\n" +
                    "                                                    FROM            CashBook AS CashBook\n" +
                    "                                                    WHERE        (ClientID = " + prefrence.getClientIDSession() + ") AND (CBDate >= '" + date_picker1.getText().toString() + "') AND (CBDate <= '" + date_picker2.getText().toString() + "')\n" +
                    "                                                    UNION ALL\n" +
                    "                                                    SELECT        DebitAccount AS AccountID, Amount AS Debit, 0 AS Credit, ClientID, CBDate\n" +
                    "                                                    FROM            CashBook AS CashBook_1\n" +
                    "                                                    WHERE        (ClientID = " + prefrence.getClientIDSession() + ") AND (CBDate >= '" + date_picker1.getText().toString() + "') AND (CBDate <= '" + date_picker2.getText().toString() + "')) AS derivedtbl_1_1\n" +
                    "                          GROUP BY ClientID, AccountID) AS SixColumnTB INNER JOIN\n" +
                    "                         Account3Name ON SixColumnTB.AccountID = Account3Name.AcNameID INNER JOIN\n" +
                    "                         Account2Group ON Account3Name.AcGroupID = Account2Group.AcGroupID INNER JOIN\n" +
                    "                         Account1Type ON Account2Group.AcTypeID = Account1Type.AcTypeID\n" +
                    "GROUP BY SixColumnTB.ClientID, SixColumnTB.AccountID, Account3Name.AcName, Account2Group.AcGroupID, Account2Group.AcGruopName, Account1Type.AcTypeID, Account1Type.AcTypeName "+ orderby;

            monthTbList = databaseHelper.getMonthTB(query);


        gPreDebit = 0; gPreCredit = 0; gTraDebit = 0; gTraCredit = 0; gCloDebit = 0; gCloCredit = 0;
        mList.add(MonthTb.createSection("Assets And Liability"));
        createRow("Assets And Liability");
        mList.add(MonthTb.createTotal("Total",String.valueOf(gPreDebit),String.valueOf(gPreCredit),String.valueOf(gTraDebit),String.valueOf(gTraCredit),String.valueOf(gCloDebit),String.valueOf(gCloCredit)));

        gPreDebit = 0; gPreCredit = 0; gTraDebit = 0; gTraCredit = 0; gCloDebit = 0; gCloCredit = 0;
        mList.add(MonthTb.createSection("Expense"));
        createRow("Expense");
        mList.add(MonthTb.createTotal("Total",String.valueOf(gPreDebit),String.valueOf(gPreCredit),String.valueOf(gTraDebit),String.valueOf(gTraCredit),String.valueOf(gCloDebit),String.valueOf(gCloCredit)));

        gPreDebit = 0; gPreCredit = 0; gTraDebit = 0; gTraCredit = 0; gCloDebit = 0; gCloCredit = 0;
        mList.add(MonthTb.createSection("Revenue"));
        createRow("Revenue");
        mList.add(MonthTb.createTotal("Total",String.valueOf(gPreDebit),String.valueOf(gPreCredit),String.valueOf(gTraDebit),String.valueOf(gTraCredit),String.valueOf(gCloDebit),String.valueOf(gCloCredit)));

        gPreDebit = 0; gPreCredit = 0; gTraDebit = 0; gTraCredit = 0; gCloDebit = 0; gCloCredit = 0;
        mList.add(MonthTb.createSection("Capital"));
        createRow("Capital");
        mList.add(MonthTb.createTotal("Total",String.valueOf(gPreDebit),String.valueOf(gPreCredit),String.valueOf(gTraDebit),String.valueOf(gTraCredit),String.valueOf(gCloDebit),String.valueOf(gCloCredit)));


        mList.add(MonthTb.createSection("Grand Total"));
        mList.add(MonthTb.createTotal("Total",String.valueOf(mPreDebit),String.valueOf(mPreCredit),String.valueOf(mTraDebit),String.valueOf(mTraCredit),String.valueOf(mCloDebit),String.valueOf(mCloCredit)));

        adapter = new MonthTrialBalanceAdapter(getContext(), mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    public void createRow(String type){
        preDebit = 0; preCredit = 0; traDebit = 0; traCredit = 0; cloDebit = 0; cloCredit = 0;
        section = true;
        for (MonthTb m : monthTbList){
            Log.e("ROW",m.getAcGruopName() +"     "+m.getAcTypeName());
            if (m.getAcTypeName().equals(type) && m.getAcGruopName().equals("Cash And Bank")){
                if (section) {
                    mList.add(MonthTb.createSection("Cash And Bank"));
                    section = false;
                    group = true;
                }
                mList.add(MonthTb.createRow(m.getClientID(),m.getAccountID(),m.getDebit(),m.getCredit(),m.getPrvBal(),m.getPrvDebit(),m.getPrvCredit()
                        ,m.getTraDebit(),m.getTraCredit(),m.getTraBalance(),m.getClosingBalnce(),m.getClosingDebit(),m.getClosingCredit(),
                        m.getAcName(),m.getAcGroupID(),m.getAcGruopName(),m.getAcTypeID(),m.getAcTypeName()));
                preDebit = preDebit + Integer.valueOf(m.getPrvDebit());
                preCredit = preCredit + Integer.valueOf(m.getPrvCredit());
                traDebit = traDebit + Integer.valueOf(m.getTraDebit());
                traCredit = traCredit + Integer.valueOf(m.getTraCredit());
                cloDebit = cloDebit + Integer.valueOf(m.getClosingDebit());
                cloCredit = cloCredit + Integer.valueOf(m.getClosingCredit());

                mPreDebit = mPreDebit + Integer.valueOf(m.getPrvDebit());
                mPreCredit = mPreCredit + Integer.valueOf(m.getPrvCredit());
                mTraDebit = mTraDebit + Integer.valueOf(m.getTraDebit());
                mTraCredit = mTraCredit + Integer.valueOf(m.getTraCredit());
                mCloDebit = mCloDebit + Integer.valueOf(m.getClosingDebit());
                mCloCredit = mCloCredit + Integer.valueOf(m.getClosingCredit());

                gPreDebit = gPreDebit + Integer.valueOf(m.getPrvDebit());
                gPreCredit = gPreCredit + Integer.valueOf(m.getPrvCredit());
                gTraDebit = gTraDebit + Integer.valueOf(m.getTraDebit());
                gTraCredit = gTraCredit + Integer.valueOf(m.getTraCredit());
                gCloDebit = gCloDebit + Integer.valueOf(m.getClosingDebit());
                gCloCredit = gCloCredit + Integer.valueOf(m.getClosingCredit());
            }
        }
        if (group) {
            mList.add(MonthTb.createTotal("Total", String.valueOf(preDebit), String.valueOf(preCredit), String.valueOf(traDebit), String.valueOf(traCredit), String.valueOf(cloDebit), String.valueOf(cloCredit)));
            group = false;
        }

        preDebit = 0; preCredit = 0; traDebit = 0; traCredit = 0; cloDebit = 0; cloCredit = 0;
        section = true;
        for (MonthTb m : monthTbList){
            if (m.getAcTypeName().equals(type) && m.getAcGruopName().equals("Employee")){
                if (section) {
                    mList.add(MonthTb.createSection("Employee"));
                    section = false;
                    group = true;
                }
                mList.add(MonthTb.createRow(m.getClientID(),m.getAccountID(),m.getDebit(),m.getCredit(),m.getPrvBal(),m.getPrvDebit(),m.getPrvCredit()
                        ,m.getTraDebit(),m.getTraCredit(),m.getTraBalance(),m.getClosingBalnce(),m.getClosingDebit(),m.getClosingCredit(),
                        m.getAcName(),m.getAcGroupID(),m.getAcGruopName(),m.getAcTypeID(),m.getAcTypeName()));
                preDebit = preDebit + Integer.valueOf(m.getPrvDebit());
                preCredit = preCredit + Integer.valueOf(m.getPrvCredit());
                traDebit = traDebit + Integer.valueOf(m.getTraDebit());
                traCredit = traCredit + Integer.valueOf(m.getTraCredit());
                cloDebit = cloDebit + Integer.valueOf(m.getClosingDebit());
                cloCredit = cloCredit + Integer.valueOf(m.getClosingCredit());

                mPreDebit = mPreDebit + Integer.valueOf(m.getPrvDebit());
                mPreCredit = mPreCredit + Integer.valueOf(m.getPrvCredit());
                mTraDebit = mTraDebit + Integer.valueOf(m.getTraDebit());
                mTraCredit = mTraCredit + Integer.valueOf(m.getTraCredit());
                mCloDebit = mCloDebit + Integer.valueOf(m.getClosingDebit());
                mCloCredit = mCloCredit + Integer.valueOf(m.getClosingCredit());

                gPreDebit = gPreDebit + Integer.valueOf(m.getPrvDebit());
                gPreCredit = gPreCredit + Integer.valueOf(m.getPrvCredit());
                gTraDebit = gTraDebit + Integer.valueOf(m.getTraDebit());
                gTraCredit = gTraCredit + Integer.valueOf(m.getTraCredit());
                gCloDebit = gCloDebit + Integer.valueOf(m.getClosingDebit());
                gCloCredit = gCloCredit + Integer.valueOf(m.getClosingCredit());
            }
        }
        if (group) {
            mList.add(MonthTb.createTotal("Total", String.valueOf(preDebit), String.valueOf(preCredit), String.valueOf(traDebit), String.valueOf(traCredit), String.valueOf(cloDebit), String.valueOf(cloCredit)));
            group = false;
        }
        preDebit = 0; preCredit = 0; traDebit = 0; traCredit = 0; cloDebit = 0; cloCredit = 0;
        section = true;
        for (MonthTb m : monthTbList){
            if (m.getAcTypeName().equals(type) && m.getAcGruopName().equals("General Expense")){
                if (section) {
                    mList.add(MonthTb.createSection("General Expense"));
                    section = false;
                    group = true;
                }
                mList.add(MonthTb.createRow(m.getClientID(),m.getAccountID(),m.getDebit(),m.getCredit(),m.getPrvBal(),m.getPrvDebit(),m.getPrvCredit()
                        ,m.getTraDebit(),m.getTraCredit(),m.getTraBalance(),m.getClosingBalnce(),m.getClosingDebit(),m.getClosingCredit(),
                        m.getAcName(),m.getAcGroupID(),m.getAcGruopName(),m.getAcTypeID(),m.getAcTypeName()));
                preDebit = preDebit + Integer.valueOf(m.getPrvDebit());
                preCredit = preCredit + Integer.valueOf(m.getPrvCredit());
                traDebit = traDebit + Integer.valueOf(m.getTraDebit());
                traCredit = traCredit + Integer.valueOf(m.getTraCredit());
                cloDebit = cloDebit + Integer.valueOf(m.getClosingDebit());
                cloCredit = cloCredit + Integer.valueOf(m.getClosingCredit());

                mPreDebit = mPreDebit + Integer.valueOf(m.getPrvDebit());
                mPreCredit = mPreCredit + Integer.valueOf(m.getPrvCredit());
                mTraDebit = mTraDebit + Integer.valueOf(m.getTraDebit());
                mTraCredit = mTraCredit + Integer.valueOf(m.getTraCredit());
                mCloDebit = mCloDebit + Integer.valueOf(m.getClosingDebit());
                mCloCredit = mCloCredit + Integer.valueOf(m.getClosingCredit());

                gPreDebit = gPreDebit + Integer.valueOf(m.getPrvDebit());
                gPreCredit = gPreCredit + Integer.valueOf(m.getPrvCredit());
                gTraDebit = gTraDebit + Integer.valueOf(m.getTraDebit());
                gTraCredit = gTraCredit + Integer.valueOf(m.getTraCredit());
                gCloDebit = gCloDebit + Integer.valueOf(m.getClosingDebit());
                gCloCredit = gCloCredit + Integer.valueOf(m.getClosingCredit());
            }
        }
        if (group) {
            mList.add(MonthTb.createTotal("Total", String.valueOf(preDebit), String.valueOf(preCredit), String.valueOf(traDebit), String.valueOf(traCredit), String.valueOf(cloDebit), String.valueOf(cloCredit)));
            group = false;
        }
        preDebit = 0; preCredit = 0; traDebit = 0; traCredit = 0; cloDebit = 0; cloCredit = 0;
        section = true;
        for (MonthTb m : monthTbList){
            if (m.getAcTypeName().equals(type) && m.getAcGruopName().equals("Fixed Assets")){
                if (section) {
                    mList.add(MonthTb.createSection("Fixed Assets"));
                    section = false;
                    group = true;
                }
                mList.add(MonthTb.createRow(m.getClientID(),m.getAccountID(),m.getDebit(),m.getCredit(),m.getPrvBal(),m.getPrvDebit(),m.getPrvCredit()
                        ,m.getTraDebit(),m.getTraCredit(),m.getTraBalance(),m.getClosingBalnce(),m.getClosingDebit(),m.getClosingCredit(),
                        m.getAcName(),m.getAcGroupID(),m.getAcGruopName(),m.getAcTypeID(),m.getAcTypeName()));
                preDebit = preDebit + Integer.valueOf(m.getPrvDebit());
                preCredit = preCredit + Integer.valueOf(m.getPrvCredit());
                traDebit = traDebit + Integer.valueOf(m.getTraDebit());
                traCredit = traCredit + Integer.valueOf(m.getTraCredit());
                cloDebit = cloDebit + Integer.valueOf(m.getClosingDebit());
                cloCredit = cloCredit + Integer.valueOf(m.getClosingCredit());

                mPreDebit = mPreDebit + Integer.valueOf(m.getPrvDebit());
                mPreCredit = mPreCredit + Integer.valueOf(m.getPrvCredit());
                mTraDebit = mTraDebit + Integer.valueOf(m.getTraDebit());
                mTraCredit = mTraCredit + Integer.valueOf(m.getTraCredit());
                mCloDebit = mCloDebit + Integer.valueOf(m.getClosingDebit());
                mCloCredit = mCloCredit + Integer.valueOf(m.getClosingCredit());

                gPreDebit = gPreDebit + Integer.valueOf(m.getPrvDebit());
                gPreCredit = gPreCredit + Integer.valueOf(m.getPrvCredit());
                gTraDebit = gTraDebit + Integer.valueOf(m.getTraDebit());
                gTraCredit = gTraCredit + Integer.valueOf(m.getTraCredit());
                gCloDebit = gCloDebit + Integer.valueOf(m.getClosingDebit());
                gCloCredit = gCloCredit + Integer.valueOf(m.getClosingCredit());
            }
        }
        if (group) {
            mList.add(MonthTb.createTotal("Total", String.valueOf(preDebit), String.valueOf(preCredit), String.valueOf(traDebit), String.valueOf(traCredit), String.valueOf(cloDebit), String.valueOf(cloCredit)));
            group = false;
        }
        preDebit = 0; preCredit = 0; traDebit = 0; traCredit = 0; cloDebit = 0; cloCredit = 0;
        section = true;
        for (MonthTb m : monthTbList){
            if (m.getAcTypeName().equals(type) && m.getAcGruopName().equals("Client")){
                if (section) {
                    mList.add(MonthTb.createSection("Client"));
                    section = false;
                    group = true;
                }
                mList.add(MonthTb.createRow(m.getClientID(),m.getAccountID(),m.getDebit(),m.getCredit(),m.getPrvBal(),m.getPrvDebit(),m.getPrvCredit()
                        ,m.getTraDebit(),m.getTraCredit(),m.getTraBalance(),m.getClosingBalnce(),m.getClosingDebit(),m.getClosingCredit(),
                        m.getAcName(),m.getAcGroupID(),m.getAcGruopName(),m.getAcTypeID(),m.getAcTypeName()));
                preDebit = preDebit + Integer.valueOf(m.getPrvDebit());
                preCredit = preCredit + Integer.valueOf(m.getPrvCredit());
                traDebit = traDebit + Integer.valueOf(m.getTraDebit());
                traCredit = traCredit + Integer.valueOf(m.getTraCredit());
                cloDebit = cloDebit + Integer.valueOf(m.getClosingDebit());
                cloCredit = cloCredit + Integer.valueOf(m.getClosingCredit());

                mPreDebit = mPreDebit + Integer.valueOf(m.getPrvDebit());
                mPreCredit = mPreCredit + Integer.valueOf(m.getPrvCredit());
                mTraDebit = mTraDebit + Integer.valueOf(m.getTraDebit());
                mTraCredit = mTraCredit + Integer.valueOf(m.getTraCredit());
                mCloDebit = mCloDebit + Integer.valueOf(m.getClosingDebit());
                mCloCredit = mCloCredit + Integer.valueOf(m.getClosingCredit());

                gPreDebit = gPreDebit + Integer.valueOf(m.getPrvDebit());
                gPreCredit = gPreCredit + Integer.valueOf(m.getPrvCredit());
                gTraDebit = gTraDebit + Integer.valueOf(m.getTraDebit());
                gTraCredit = gTraCredit + Integer.valueOf(m.getTraCredit());
                gCloDebit = gCloDebit + Integer.valueOf(m.getClosingDebit());
                gCloCredit = gCloCredit + Integer.valueOf(m.getClosingCredit());
            }
        }
        if (group) {
            mList.add(MonthTb.createTotal("Total", String.valueOf(preDebit), String.valueOf(preCredit), String.valueOf(traDebit), String.valueOf(traCredit), String.valueOf(cloDebit), String.valueOf(cloCredit)));
            group = false;
        }

        preDebit = 0; preCredit = 0; traDebit = 0; traCredit = 0; cloDebit = 0; cloCredit = 0;
        section = true;
        for (MonthTb m : monthTbList){
            if (m.getAcTypeName().equals(type) && m.getAcGruopName().equals("Suppliers")){
                if (section) {
                    mList.add(MonthTb.createSection("Suppliers"));
                    section = false;
                    group = true;
                }
                mList.add(MonthTb.createRow(m.getClientID(),m.getAccountID(),m.getDebit(),m.getCredit(),m.getPrvBal(),m.getPrvDebit(),m.getPrvCredit()
                        ,m.getTraDebit(),m.getTraCredit(),m.getTraBalance(),m.getClosingBalnce(),m.getClosingDebit(),m.getClosingCredit(),
                        m.getAcName(),m.getAcGroupID(),m.getAcGruopName(),m.getAcTypeID(),m.getAcTypeName()));
                preDebit = preDebit + Integer.valueOf(m.getPrvDebit());
                preCredit = preCredit + Integer.valueOf(m.getPrvCredit());
                traDebit = traDebit + Integer.valueOf(m.getTraDebit());
                traCredit = traCredit + Integer.valueOf(m.getTraCredit());
                cloDebit = cloDebit + Integer.valueOf(m.getClosingDebit());
                cloCredit = cloCredit + Integer.valueOf(m.getClosingCredit());

                mPreDebit = mPreDebit + Integer.valueOf(m.getPrvDebit());
                mPreCredit = mPreCredit + Integer.valueOf(m.getPrvCredit());
                mTraDebit = mTraDebit + Integer.valueOf(m.getTraDebit());
                mTraCredit = mTraCredit + Integer.valueOf(m.getTraCredit());
                mCloDebit = mCloDebit + Integer.valueOf(m.getClosingDebit());
                mCloCredit = mCloCredit + Integer.valueOf(m.getClosingCredit());

                gPreDebit = gPreDebit + Integer.valueOf(m.getPrvDebit());
                gPreCredit = gPreCredit + Integer.valueOf(m.getPrvCredit());
                gTraDebit = gTraDebit + Integer.valueOf(m.getTraDebit());
                gTraCredit = gTraCredit + Integer.valueOf(m.getTraCredit());
                gCloDebit = gCloDebit + Integer.valueOf(m.getClosingDebit());
                gCloCredit = gCloCredit + Integer.valueOf(m.getClosingCredit());
            }
        }
        if (group) {
            mList.add(MonthTb.createTotal("Total", String.valueOf(preDebit), String.valueOf(preCredit), String.valueOf(traDebit), String.valueOf(traCredit), String.valueOf(cloDebit), String.valueOf(cloCredit)));
            group = false;
        }

        preDebit = 0; preCredit = 0; traDebit = 0; traCredit = 0; cloDebit = 0; cloCredit = 0;
        section = true;
        for (MonthTb m : monthTbList){
            if (m.getAcTypeName().equals(type) && m.getAcGruopName().equals("Incom")){
                if (section) {
                    mList.add(MonthTb.createSection("Incom"));
                    section = false;
                    group = true;
                }
                mList.add(MonthTb.createRow(m.getClientID(),m.getAccountID(),m.getDebit(),m.getCredit(),m.getPrvBal(),m.getPrvDebit(),m.getPrvCredit()
                        ,m.getTraDebit(),m.getTraCredit(),m.getTraBalance(),m.getClosingBalnce(),m.getClosingDebit(),m.getClosingCredit(),
                        m.getAcName(),m.getAcGroupID(),m.getAcGruopName(),m.getAcTypeID(),m.getAcTypeName()));
                preDebit = preDebit + Integer.valueOf(m.getPrvDebit());
                preCredit = preCredit + Integer.valueOf(m.getPrvCredit());
                traDebit = traDebit + Integer.valueOf(m.getTraDebit());
                traCredit = traCredit + Integer.valueOf(m.getTraCredit());
                cloDebit = cloDebit + Integer.valueOf(m.getClosingDebit());
                cloCredit = cloCredit + Integer.valueOf(m.getClosingCredit());

                mPreDebit = mPreDebit + Integer.valueOf(m.getPrvDebit());
                mPreCredit = mPreCredit + Integer.valueOf(m.getPrvCredit());
                mTraDebit = mTraDebit + Integer.valueOf(m.getTraDebit());
                mTraCredit = mTraCredit + Integer.valueOf(m.getTraCredit());
                mCloDebit = mCloDebit + Integer.valueOf(m.getClosingDebit());
                mCloCredit = mCloCredit + Integer.valueOf(m.getClosingCredit());

                gPreDebit = gPreDebit + Integer.valueOf(m.getPrvDebit());
                gPreCredit = gPreCredit + Integer.valueOf(m.getPrvCredit());
                gTraDebit = gTraDebit + Integer.valueOf(m.getTraDebit());
                gTraCredit = gTraCredit + Integer.valueOf(m.getTraCredit());
                gCloDebit = gCloDebit + Integer.valueOf(m.getClosingDebit());
                gCloCredit = gCloCredit + Integer.valueOf(m.getClosingCredit());
            }
        }
        if (group) {
            mList.add(MonthTb.createTotal("Total", String.valueOf(preDebit), String.valueOf(preCredit), String.valueOf(traDebit), String.valueOf(traCredit), String.valueOf(cloDebit), String.valueOf(cloCredit)));
            group = false;
        }

        preDebit = 0; preCredit = 0; traDebit = 0; traCredit = 0; cloDebit = 0; cloCredit = 0;
        section = true;
        for (MonthTb m : monthTbList){
            if (m.getAcTypeName().equals(type) && m.getAcGruopName().equals("Capital")){
                if (section) {
                    mList.add(MonthTb.createSection("Capital"));
                    section = false;
                    group = true;
                }
                mList.add(MonthTb.createRow(m.getClientID(),m.getAccountID(),m.getDebit(),m.getCredit(),m.getPrvBal(),m.getPrvDebit(),m.getPrvCredit()
                        ,m.getTraDebit(),m.getTraCredit(),m.getTraBalance(),m.getClosingBalnce(),m.getClosingDebit(),m.getClosingCredit(),
                        m.getAcName(),m.getAcGroupID(),m.getAcGruopName(),m.getAcTypeID(),m.getAcTypeName()));
                preDebit = preDebit + Integer.valueOf(m.getPrvDebit());
                preCredit = preCredit + Integer.valueOf(m.getPrvCredit());
                traDebit = traDebit + Integer.valueOf(m.getTraDebit());
                traCredit = traCredit + Integer.valueOf(m.getTraCredit());
                cloDebit = cloDebit + Integer.valueOf(m.getClosingDebit());
                cloCredit = cloCredit + Integer.valueOf(m.getClosingCredit());

                mPreDebit = mPreDebit + Integer.valueOf(m.getPrvDebit());
                mPreCredit = mPreCredit + Integer.valueOf(m.getPrvCredit());
                mTraDebit = mTraDebit + Integer.valueOf(m.getTraDebit());
                mTraCredit = mTraCredit + Integer.valueOf(m.getTraCredit());
                mCloDebit = mCloDebit + Integer.valueOf(m.getClosingDebit());
                mCloCredit = mCloCredit + Integer.valueOf(m.getClosingCredit());

                gPreDebit = gPreDebit + Integer.valueOf(m.getPrvDebit());
                gPreCredit = gPreCredit + Integer.valueOf(m.getPrvCredit());
                gTraDebit = gTraDebit + Integer.valueOf(m.getTraDebit());
                gTraCredit = gTraCredit + Integer.valueOf(m.getTraCredit());
                gCloDebit = gCloDebit + Integer.valueOf(m.getClosingDebit());
                gCloCredit = gCloCredit + Integer.valueOf(m.getClosingCredit());
            }
        }
        if (group) {
            mList.add(MonthTb.createTotal("Total", String.valueOf(preDebit), String.valueOf(preCredit), String.valueOf(traDebit), String.valueOf(traCredit), String.valueOf(cloDebit), String.valueOf(cloCredit)));
            group = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.date_picker1:
                AppController.date = "MonthTB1";
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
                break;
            case R.id.date_picker2:
                AppController.date = "MonthTB2";
                DialogFragment newFragment1 = new SelectDateFragment();
                newFragment1.show(getFragmentManager(), "DatePicker");
                break;
            case R.id.refresh:
                getMonthTB();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cb_menu,menu);
        MenuItem referesh = menu.findItem(R.id.action_refresh);
        referesh.setVisible(false);
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
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
        }
        return super.onOptionsItemSelected(item);
    }

    private void filter(String text) {
        filterdList = new ArrayList<>();

        //looping through existing elements
        if (!text.isEmpty()) {
            for (MonthTb s : mList) {
                if (s.isRow() == 1) {
                    switch (filter) {
                        case 1:
                            if (s.getAcName().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
//                        case 2:
//                            if (s.getAcGruopName().toLowerCase().contains(text.toLowerCase())) {
//                                //adding the element to filtered list
//                                filterdList.add(s);
//                            }
//                            break;
//                        case 3:
//                            if (s.getDebitBL().toLowerCase().contains(text.toLowerCase())) {
//                                //adding the element to filtered list
//                                filterdList.add(s);
//                            }
//                            break;
//                        case 4:
//                            if (s.getCreditBL().toLowerCase().contains(text.toLowerCase())) {
//                                //adding the element to filtered list
//                                filterdList.add(s);
//                            }
//                            break;
                    }
                }
            }
        } else {
            filterdList = mList;
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdList);

    }


    public void createPdf() throws IOException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }

        String pdfname = "TrialBalance.pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);

        Document document = new Document(PageSize.A4.rotate());
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

        PdfPTable title = new PdfPTable(new float[]{3, 5, 3});
        title.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        title.getDefaultCell().setFixedHeight(30);
        title.setTotalWidth(PageSize.A4.getWidth());
        title.setWidthPercentage(100);
        title.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.setSpacingBefore(5);
        title.addCell(footerCell("", PdfPCell.ALIGN_CENTER));
        PdfPCell cell = new PdfPCell(new Phrase("Month Wise Trial Balance",chapterFont));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.addCell(cell);
        title.addCell(footerCell("",PdfPCell.ALIGN_CENTER));

        title.addCell(footerCell("",PdfPCell.ALIGN_CENTER));
        title.addCell(footerCell("",PdfPCell.ALIGN_CENTER));
        PdfPCell pCell = new PdfPCell(new Phrase(spinner.getSelectedItem()+": "+searchView.getQuery()));
        pCell.setBorder(PdfPCell.NO_BORDER);
        pCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        pCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.addCell(pCell);

        title.addCell(footerCell("",PdfPCell.ALIGN_CENTER));
        title.addCell(footerCell("",PdfPCell.ALIGN_CENTER));
        PdfPCell dCell1 = new PdfPCell(new Phrase("From"+" "+date_picker1.getText().toString()));
        dCell1.setBorder(PdfPCell.NO_BORDER);
        dCell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        dCell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.addCell(dCell1);

        title.addCell(footerCell("",PdfPCell.ALIGN_CENTER));
        title.addCell(footerCell("",PdfPCell.ALIGN_CENTER));
        PdfPCell dCell2 = new PdfPCell(new Phrase("To"+"     "+date_picker2.getText().toString()));
        dCell2.setBorder(PdfPCell.NO_BORDER);
        dCell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        dCell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.addCell(dCell2);

        PdfPTable tab = new PdfPTable(new float[]{4, 6, 6, 6}); //one page contains 15 records
        tab.setWidthPercentage(100);
        tab.setSpacingBefore(10);
        Font voucherFount = FontFactory.getFont(FontFactory.HELVETICA, 15, Font.BOLD);
        voucherFount.setColor(BaseColor.WHITE);
        PdfPCell pre = new PdfPCell(new Phrase("Previous Balance",voucherFount));
        pre.setHorizontalAlignment(Element.ALIGN_CENTER);
        pre.setVerticalAlignment(Element.ALIGN_CENTER);
        pre.setBackgroundColor(BaseColor.BLACK);
        PdfPCell tra = new PdfPCell(new Phrase("Transaction",voucherFount));
        tra.setHorizontalAlignment(Element.ALIGN_CENTER);
        tra.setVerticalAlignment(Element.ALIGN_CENTER);
        tra.setBackgroundColor(BaseColor.BLACK);
        PdfPCell clo = new PdfPCell(new Phrase("Closing Balance",voucherFount));
        clo.setHorizontalAlignment(Element.ALIGN_CENTER);
        clo.setVerticalAlignment(Element.ALIGN_CENTER);
        clo.setBackgroundColor(BaseColor.BLACK);
        tab.addCell(footerCell("",PdfPCell.ALIGN_CENTER));
        tab.addCell(pre);
        tab.addCell(tra);
        tab.addCell(clo);

        PdfPTable table = new PdfPTable(new float[]{4, 3, 3, 3, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(40);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
//        table.setSpacingBefore(20);
        table.addCell("AcName");
        table.addCell("Debit");
        table.addCell("Credit");
        table.addCell("Debit");
        table.addCell("Credit");
        table.addCell("Debit");
        table.addCell("Credit");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.PINK);
        }

        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD);
        PdfPCell total = new PdfPCell(new Phrase("Total",totalFont));
        total.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        total.setVerticalAlignment(Element.ALIGN_MIDDLE);
        total.setFixedHeight(35);

        gPreDebit = 0; gPreCredit = 0; gTraDebit = 0; gTraCredit = 0; gCloDebit = 0; gCloCredit = 0;
        PdfPCell mSection = new PdfPCell(new Phrase("Assets And Liability", totalFont));
        mSection.setBorder(PdfPCell.NO_BORDER);
        mSection.setFixedHeight(30);
        mSection.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        mSection.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(mSection);
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        createPrintRow("Assets And Liability",table);
        table.addCell(getCell("Type Total", PdfPCell.ALIGN_CENTER));
        table.addCell(getCell(String.valueOf(gPreDebit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gPreCredit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gTraDebit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gTraCredit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gCloDebit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gCloCredit), PdfPCell.ALIGN_RIGHT));

        gPreDebit = 0; gPreCredit = 0; gTraDebit = 0; gTraCredit = 0; gCloDebit = 0; gCloCredit = 0;
        PdfPCell mSection1 = new PdfPCell(new Phrase("Expense", totalFont));
        mSection1.setBorder(PdfPCell.NO_BORDER);
        mSection1.setFixedHeight(30);
        mSection1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        mSection1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(mSection1);
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        createPrintRow("Expense",table);
        table.addCell(getCell("Type Total", PdfPCell.ALIGN_CENTER));
        table.addCell(getCell(String.valueOf(gPreDebit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gPreCredit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gTraDebit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gTraCredit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gCloDebit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gCloCredit), PdfPCell.ALIGN_RIGHT));

        gPreDebit = 0; gPreCredit = 0; gTraDebit = 0; gTraCredit = 0; gCloDebit = 0; gCloCredit = 0;
        PdfPCell mSection2 = new PdfPCell(new Phrase("Revenue", totalFont));
        mSection2.setBorder(PdfPCell.NO_BORDER);
        mSection2.setFixedHeight(30);
        mSection2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        mSection2.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(mSection2);
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        createPrintRow("Revenue",table);
        table.addCell(getCell("Type Total", PdfPCell.ALIGN_CENTER));
        table.addCell(getCell(String.valueOf(gPreDebit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gPreCredit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gTraDebit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gTraCredit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gCloDebit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gCloCredit), PdfPCell.ALIGN_RIGHT));

        gPreDebit = 0; gPreCredit = 0; gTraDebit = 0; gTraCredit = 0; gCloDebit = 0; gCloCredit = 0;
        PdfPCell mSection3 = new PdfPCell(new Phrase("Capital", totalFont));
        mSection3.setBorder(PdfPCell.NO_BORDER);
        mSection3.setFixedHeight(30);
        mSection3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        mSection3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(mSection3);
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
        createPrintRow("Capital",table);
        table.addCell(getCell("Type Total", PdfPCell.ALIGN_CENTER));
        table.addCell(getCell(String.valueOf(gPreDebit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gPreCredit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gTraDebit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gTraCredit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gCloDebit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gCloCredit), PdfPCell.ALIGN_RIGHT));

        table.addCell(getCell("Grand Total", PdfPCell.ALIGN_CENTER));
        table.addCell(getCell(String.valueOf(mPreDebit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(mPreCredit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(mTraDebit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(mTraCredit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(mCloDebit), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(mCloCredit), PdfPCell.ALIGN_RIGHT));

        document.open();

        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.BLACK);
        Paragraph paragraph = new Paragraph("Cash Book \n\n", f);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(chunk);
        document.add(name);
        document.add(contact);
        document.add(title);
        document.add(tab);
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

            PdfPTable table = new PdfPTable(new float[]{4, 6, 2});
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setFixedHeight(20);
            table.setTotalWidth(PageSize.A4.rotate().getWidth());
            table.getDefaultCell().setBorder(Rectangle.BOTTOM);
            Date dat = new Date();
            SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
            table.addCell(footerCell(df.format(dat), PdfPCell.ALIGN_LEFT));
            table.addCell(footerCell("www.easysoft.com.pk",PdfPCell.ALIGN_LEFT));
//            table.addCell(footerCell("",PdfPCell.ALIGN_LEFT));
            table.addCell(footerCell(String.format("Page %d ", writer.getPageNumber() -1),PdfPCell.ALIGN_LEFT));

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

    public void createPrintRow(String type, PdfPTable table){
        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
        PdfPCell total = new PdfPCell(new Phrase("Total",totalFont));
        total.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        total.setVerticalAlignment(Element.ALIGN_MIDDLE);
        total.setFixedHeight(35);

        section = true;
        preDebit = 0; preCredit = 0; traDebit = 0; traCredit = 0; cloDebit = 0; cloCredit = 0;
        for (MonthTb m : monthTbList){
            if (m.getAcTypeName().equals(type) && m.getAcGruopName().equals("Cash And Bank")){
                if (section) {
                    PdfPCell mSection = new PdfPCell(new Phrase("Cash And Bank", totalFont));
                    mSection.setBorder(PdfPCell.NO_BORDER);
                    mSection.setFixedHeight(30);
                    mSection.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    mSection.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(mSection);
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    section = false;
                    group = true;
                }
                table.addCell(getCell(m.getAcName(), PdfPCell.ALIGN_LEFT));
                table.addCell(getCell(m.getPrvDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getPrvCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getTraDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getTraCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getClosingDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getClosingCredit(), PdfPCell.ALIGN_RIGHT));

                preDebit = preDebit + Integer.valueOf(m.getPrvDebit());
                preCredit = preCredit + Integer.valueOf(m.getPrvCredit());
                traDebit = traDebit + Integer.valueOf(m.getTraDebit());
                traCredit = traCredit + Integer.valueOf(m.getTraCredit());
                cloDebit = cloDebit + Integer.valueOf(m.getClosingDebit());
                cloCredit = cloCredit + Integer.valueOf(m.getClosingCredit());

                mPreDebit = mPreDebit + Integer.valueOf(m.getPrvDebit());
                mPreCredit = mPreCredit + Integer.valueOf(m.getPrvCredit());
                mTraDebit = mTraDebit + Integer.valueOf(m.getTraDebit());
                mTraCredit = mTraCredit + Integer.valueOf(m.getTraCredit());
                mCloDebit = mCloDebit + Integer.valueOf(m.getClosingDebit());
                mCloCredit = mCloCredit + Integer.valueOf(m.getClosingCredit());

                gPreDebit = gPreDebit + Integer.valueOf(m.getPrvDebit());
                gPreCredit = gPreCredit + Integer.valueOf(m.getPrvCredit());
                gTraDebit = gTraDebit + Integer.valueOf(m.getTraDebit());
                gTraCredit = gTraCredit + Integer.valueOf(m.getTraCredit());
                gCloDebit = gCloDebit + Integer.valueOf(m.getClosingDebit());
                gCloCredit = gCloCredit + Integer.valueOf(m.getClosingCredit());

            }
        }
        if (group) {
            table.addCell(total);
            table.addCell(getCell(String.valueOf(preDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(preCredit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(traDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(traCredit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(cloDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(cloCredit), PdfPCell.ALIGN_RIGHT));
            group = false;
        }

        section = true;
        preDebit = 0; preCredit = 0; traDebit = 0; traCredit = 0; cloDebit = 0; cloCredit = 0;
        for (MonthTb m : monthTbList){
            if (m.getAcTypeName().equals(type) && m.getAcGruopName().equals("Employee")){
                if (section) {
                    PdfPCell mSection = new PdfPCell(new Phrase("Employee", totalFont));
                    mSection.setBorder(PdfPCell.NO_BORDER);
                    mSection.setFixedHeight(30);
                    mSection.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    mSection.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(mSection);
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    section = false;
                    group = true;
                }
                table.addCell(getCell(m.getAcName(), PdfPCell.ALIGN_LEFT));
                table.addCell(getCell(m.getPrvDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getPrvCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getTraDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getTraCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getClosingDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getClosingCredit(), PdfPCell.ALIGN_RIGHT));

                preDebit = preDebit + Integer.valueOf(m.getPrvDebit());
                preCredit = preCredit + Integer.valueOf(m.getPrvCredit());
                traDebit = traDebit + Integer.valueOf(m.getTraDebit());
                traCredit = traCredit + Integer.valueOf(m.getTraCredit());
                cloDebit = cloDebit + Integer.valueOf(m.getClosingDebit());
                cloCredit = cloCredit + Integer.valueOf(m.getClosingCredit());

                mPreDebit = mPreDebit + Integer.valueOf(m.getPrvDebit());
                mPreCredit = mPreCredit + Integer.valueOf(m.getPrvCredit());
                mTraDebit = mTraDebit + Integer.valueOf(m.getTraDebit());
                mTraCredit = mTraCredit + Integer.valueOf(m.getTraCredit());
                mCloDebit = mCloDebit + Integer.valueOf(m.getClosingDebit());
                mCloCredit = mCloCredit + Integer.valueOf(m.getClosingCredit());

                gPreDebit = gPreDebit + Integer.valueOf(m.getPrvDebit());
                gPreCredit = gPreCredit + Integer.valueOf(m.getPrvCredit());
                gTraDebit = gTraDebit + Integer.valueOf(m.getTraDebit());
                gTraCredit = gTraCredit + Integer.valueOf(m.getTraCredit());
                gCloDebit = gCloDebit + Integer.valueOf(m.getClosingDebit());
                gCloCredit = gCloCredit + Integer.valueOf(m.getClosingCredit());
            }
        }
        if (group) {
            table.addCell(total);
            table.addCell(getCell(String.valueOf(preDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(preCredit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(traDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(traCredit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(cloDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(cloCredit), PdfPCell.ALIGN_RIGHT));
            group = false;
        }

        section = true;
        preDebit = 0; preCredit = 0; traDebit = 0; traCredit = 0; cloDebit = 0; cloCredit = 0;
        for (MonthTb m : monthTbList){
            if (m.getAcTypeName().equals(type) && m.getAcGruopName().equals("General Expense")){
                if (section) {
                    PdfPCell mSection = new PdfPCell(new Phrase("General Expense", totalFont));
                    mSection.setBorder(PdfPCell.NO_BORDER);
                    mSection.setFixedHeight(30);
                    mSection.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    mSection.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(mSection);
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    section = false;
                    group = true;
                }
                table.addCell(getCell(m.getAcName(), PdfPCell.ALIGN_LEFT));
                table.addCell(getCell(m.getPrvDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getPrvCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getTraDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getTraCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getClosingDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getClosingCredit(), PdfPCell.ALIGN_RIGHT));

                preDebit = preDebit + Integer.valueOf(m.getPrvDebit());
                preCredit = preCredit + Integer.valueOf(m.getPrvCredit());
                traDebit = traDebit + Integer.valueOf(m.getTraDebit());
                traCredit = traCredit + Integer.valueOf(m.getTraCredit());
                cloDebit = cloDebit + Integer.valueOf(m.getClosingDebit());
                cloCredit = cloCredit + Integer.valueOf(m.getClosingCredit());

                mPreDebit = mPreDebit + Integer.valueOf(m.getPrvDebit());
                mPreCredit = mPreCredit + Integer.valueOf(m.getPrvCredit());
                mTraDebit = mTraDebit + Integer.valueOf(m.getTraDebit());
                mTraCredit = mTraCredit + Integer.valueOf(m.getTraCredit());
                mCloDebit = mCloDebit + Integer.valueOf(m.getClosingDebit());
                mCloCredit = mCloCredit + Integer.valueOf(m.getClosingCredit());

                gPreDebit = gPreDebit + Integer.valueOf(m.getPrvDebit());
                gPreCredit = gPreCredit + Integer.valueOf(m.getPrvCredit());
                gTraDebit = gTraDebit + Integer.valueOf(m.getTraDebit());
                gTraCredit = gTraCredit + Integer.valueOf(m.getTraCredit());
                gCloDebit = gCloDebit + Integer.valueOf(m.getClosingDebit());
                gCloCredit = gCloCredit + Integer.valueOf(m.getClosingCredit());
            }
        }
        if (group) {
            table.addCell(total);
            table.addCell(getCell(String.valueOf(preDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(preCredit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(traDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(traCredit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(cloDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(cloCredit), PdfPCell.ALIGN_RIGHT));
            group = false;
        }

        section = true;
        preDebit = 0; preCredit = 0; traDebit = 0; traCredit = 0; cloDebit = 0; cloCredit = 0;
        for (MonthTb m : monthTbList){
            if (m.getAcTypeName().equals(type) && m.getAcGruopName().equals("Fixed Assets")){
                if (section) {
                    PdfPCell mSection = new PdfPCell(new Phrase("Fixed Assets", totalFont));
                    mSection.setBorder(PdfPCell.NO_BORDER);
                    mSection.setFixedHeight(30);
                    mSection.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    mSection.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(mSection);
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    section = false;
                    group = true;
                }
                table.addCell(getCell(m.getAcName(), PdfPCell.ALIGN_LEFT));
                table.addCell(getCell(m.getPrvDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getPrvCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getTraDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getTraCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getClosingDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getClosingCredit(), PdfPCell.ALIGN_RIGHT));

                preDebit = preDebit + Integer.valueOf(m.getPrvDebit());
                preCredit = preCredit + Integer.valueOf(m.getPrvCredit());
                traDebit = traDebit + Integer.valueOf(m.getTraDebit());
                traCredit = traCredit + Integer.valueOf(m.getTraCredit());
                cloDebit = cloDebit + Integer.valueOf(m.getClosingDebit());
                cloCredit = cloCredit + Integer.valueOf(m.getClosingCredit());

                mPreDebit = mPreDebit + Integer.valueOf(m.getPrvDebit());
                mPreCredit = mPreCredit + Integer.valueOf(m.getPrvCredit());
                mTraDebit = mTraDebit + Integer.valueOf(m.getTraDebit());
                mTraCredit = mTraCredit + Integer.valueOf(m.getTraCredit());
                mCloDebit = mCloDebit + Integer.valueOf(m.getClosingDebit());
                mCloCredit = mCloCredit + Integer.valueOf(m.getClosingCredit());

                gPreDebit = gPreDebit + Integer.valueOf(m.getPrvDebit());
                gPreCredit = gPreCredit + Integer.valueOf(m.getPrvCredit());
                gTraDebit = gTraDebit + Integer.valueOf(m.getTraDebit());
                gTraCredit = gTraCredit + Integer.valueOf(m.getTraCredit());
                gCloDebit = gCloDebit + Integer.valueOf(m.getClosingDebit());
                gCloCredit = gCloCredit + Integer.valueOf(m.getClosingCredit());
            }
        }
        if (group) {
            table.addCell(total);
            table.addCell(getCell(String.valueOf(preDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(preCredit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(traDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(traCredit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(cloDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(cloCredit), PdfPCell.ALIGN_RIGHT));
            group = false;
        };

        section = true;
        preDebit = 0; preCredit = 0; traDebit = 0; traCredit = 0; cloDebit = 0; cloCredit = 0;
        for (MonthTb m : monthTbList){
            if (m.getAcTypeName().equals(type) && m.getAcGruopName().equals("Client")){
                if (section) {
                    PdfPCell mSection = new PdfPCell(new Phrase("Client", totalFont));
                    mSection.setBorder(PdfPCell.NO_BORDER);
                    mSection.setFixedHeight(30);
                    mSection.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    mSection.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(mSection);
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    section = false;
                    group = true;
                }
                table.addCell(getCell(m.getAcName(), PdfPCell.ALIGN_LEFT));
                table.addCell(getCell(m.getPrvDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getPrvCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getTraDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getTraCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getClosingDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getClosingCredit(), PdfPCell.ALIGN_RIGHT));

                preDebit = preDebit + Integer.valueOf(m.getPrvDebit());
                preCredit = preCredit + Integer.valueOf(m.getPrvCredit());
                traDebit = traDebit + Integer.valueOf(m.getTraDebit());
                traCredit = traCredit + Integer.valueOf(m.getTraCredit());
                cloDebit = cloDebit + Integer.valueOf(m.getClosingDebit());
                cloCredit = cloCredit + Integer.valueOf(m.getClosingCredit());

                mPreDebit = mPreDebit + Integer.valueOf(m.getPrvDebit());
                mPreCredit = mPreCredit + Integer.valueOf(m.getPrvCredit());
                mTraDebit = mTraDebit + Integer.valueOf(m.getTraDebit());
                mTraCredit = mTraCredit + Integer.valueOf(m.getTraCredit());
                mCloDebit = mCloDebit + Integer.valueOf(m.getClosingDebit());
                mCloCredit = mCloCredit + Integer.valueOf(m.getClosingCredit());

                gPreDebit = gPreDebit + Integer.valueOf(m.getPrvDebit());
                gPreCredit = gPreCredit + Integer.valueOf(m.getPrvCredit());
                gTraDebit = gTraDebit + Integer.valueOf(m.getTraDebit());
                gTraCredit = gTraCredit + Integer.valueOf(m.getTraCredit());
                gCloDebit = gCloDebit + Integer.valueOf(m.getClosingDebit());
                gCloCredit = gCloCredit + Integer.valueOf(m.getClosingCredit());
            }
        }
        if (group) {
            table.addCell(total);
            table.addCell(getCell(String.valueOf(preDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(preCredit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(traDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(traCredit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(cloDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(cloCredit), PdfPCell.ALIGN_RIGHT));
            group = false;
        }

        section = true;
        preDebit = 0; preCredit = 0; traDebit = 0; traCredit = 0; cloDebit = 0; cloCredit = 0;
        for (MonthTb m : monthTbList){
            if (m.getAcTypeName().equals(type) && m.getAcGruopName().equals("Suppliers")){
                if (section) {
                    PdfPCell mSection = new PdfPCell(new Phrase("Suppliers", totalFont));
                    mSection.setBorder(PdfPCell.NO_BORDER);
                    mSection.setFixedHeight(30);
                    mSection.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    mSection.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(mSection);
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    section = false;
                    group = true;
                }
                table.addCell(getCell(m.getAcName(), PdfPCell.ALIGN_LEFT));
                table.addCell(getCell(m.getPrvDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getPrvCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getTraDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getTraCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getClosingDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getClosingCredit(), PdfPCell.ALIGN_RIGHT));

                preDebit = preDebit + Integer.valueOf(m.getPrvDebit());
                preCredit = preCredit + Integer.valueOf(m.getPrvCredit());
                traDebit = traDebit + Integer.valueOf(m.getTraDebit());
                traCredit = traCredit + Integer.valueOf(m.getTraCredit());
                cloDebit = cloDebit + Integer.valueOf(m.getClosingDebit());
                cloCredit = cloCredit + Integer.valueOf(m.getClosingCredit());

                mPreDebit = mPreDebit + Integer.valueOf(m.getPrvDebit());
                mPreCredit = mPreCredit + Integer.valueOf(m.getPrvCredit());
                mTraDebit = mTraDebit + Integer.valueOf(m.getTraDebit());
                mTraCredit = mTraCredit + Integer.valueOf(m.getTraCredit());
                mCloDebit = mCloDebit + Integer.valueOf(m.getClosingDebit());
                mCloCredit = mCloCredit + Integer.valueOf(m.getClosingCredit());

                gPreDebit = gPreDebit + Integer.valueOf(m.getPrvDebit());
                gPreCredit = gPreCredit + Integer.valueOf(m.getPrvCredit());
                gTraDebit = gTraDebit + Integer.valueOf(m.getTraDebit());
                gTraCredit = gTraCredit + Integer.valueOf(m.getTraCredit());
                gCloDebit = gCloDebit + Integer.valueOf(m.getClosingDebit());
                gCloCredit = gCloCredit + Integer.valueOf(m.getClosingCredit());
            }
        }
        if (group) {
            table.addCell(total);
            table.addCell(getCell(String.valueOf(preDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(preCredit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(traDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(traCredit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(cloDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(cloCredit), PdfPCell.ALIGN_RIGHT));
            group = false;
        }

        section = true;
        preDebit = 0; preCredit = 0; traDebit = 0; traCredit = 0; cloDebit = 0; cloCredit = 0;
        for (MonthTb m : monthTbList){
            if (m.getAcTypeName().equals(type) && m.getAcGruopName().equals("Incom")){
                if (section) {
                    PdfPCell mSection = new PdfPCell(new Phrase("Incom", totalFont));
                    mSection.setBorder(PdfPCell.NO_BORDER);
                    mSection.setFixedHeight(30);
                    mSection.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    mSection.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(mSection);
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    section = false;
                    group = true;
                }
                table.addCell(getCell(m.getAcName(), PdfPCell.ALIGN_LEFT));
                table.addCell(getCell(m.getPrvDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getPrvCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getTraDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getTraCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getClosingDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getClosingCredit(), PdfPCell.ALIGN_RIGHT));

                preDebit = preDebit + Integer.valueOf(m.getPrvDebit());
                preCredit = preCredit + Integer.valueOf(m.getPrvCredit());
                traDebit = traDebit + Integer.valueOf(m.getTraDebit());
                traCredit = traCredit + Integer.valueOf(m.getTraCredit());
                cloDebit = cloDebit + Integer.valueOf(m.getClosingDebit());
                cloCredit = cloCredit + Integer.valueOf(m.getClosingCredit());

                mPreDebit = mPreDebit + Integer.valueOf(m.getPrvDebit());
                mPreCredit = mPreCredit + Integer.valueOf(m.getPrvCredit());
                mTraDebit = mTraDebit + Integer.valueOf(m.getTraDebit());
                mTraCredit = mTraCredit + Integer.valueOf(m.getTraCredit());
                mCloDebit = mCloDebit + Integer.valueOf(m.getClosingDebit());
                mCloCredit = mCloCredit + Integer.valueOf(m.getClosingCredit());

                gPreDebit = gPreDebit + Integer.valueOf(m.getPrvDebit());
                gPreCredit = gPreCredit + Integer.valueOf(m.getPrvCredit());
                gTraDebit = gTraDebit + Integer.valueOf(m.getTraDebit());
                gTraCredit = gTraCredit + Integer.valueOf(m.getTraCredit());
                gCloDebit = gCloDebit + Integer.valueOf(m.getClosingDebit());
                gCloCredit = gCloCredit + Integer.valueOf(m.getClosingCredit());
            }
        }
        if (group) {
            table.addCell(total);
            table.addCell(getCell(String.valueOf(preDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(preCredit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(traDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(traCredit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(cloDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(cloCredit), PdfPCell.ALIGN_RIGHT));
            group = false;
        }

        section = true;
        preDebit = 0; preCredit = 0; traDebit = 0; traCredit = 0; cloDebit = 0; cloCredit = 0;
        for (MonthTb m : monthTbList){
            if (m.getAcTypeName().equals(type) && m.getAcGruopName().equals("Capital")){
                if (section) {
                    PdfPCell mSection = new PdfPCell(new Phrase("Capital", totalFont));
                    mSection.setBorder(PdfPCell.NO_BORDER);
                    mSection.setFixedHeight(30);
                    mSection.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                    mSection.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(mSection);
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    section = false;
                    group = true;
                }
                table.addCell(getCell(m.getAcName(), PdfPCell.ALIGN_LEFT));
                table.addCell(getCell(m.getPrvDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getPrvCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getTraDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getTraCredit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getClosingDebit(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(m.getClosingCredit(), PdfPCell.ALIGN_RIGHT));

                preDebit = preDebit + Integer.valueOf(m.getPrvDebit());
                preCredit = preCredit + Integer.valueOf(m.getPrvCredit());
                traDebit = traDebit + Integer.valueOf(m.getTraDebit());
                traCredit = traCredit + Integer.valueOf(m.getTraCredit());
                cloDebit = cloDebit + Integer.valueOf(m.getClosingDebit());
                cloCredit = cloCredit + Integer.valueOf(m.getClosingCredit());

                mPreDebit = mPreDebit + Integer.valueOf(m.getPrvDebit());
                mPreCredit = mPreCredit + Integer.valueOf(m.getPrvCredit());
                mTraDebit = mTraDebit + Integer.valueOf(m.getTraDebit());
                mTraCredit = mTraCredit + Integer.valueOf(m.getTraCredit());
                mCloDebit = mCloDebit + Integer.valueOf(m.getClosingDebit());
                mCloCredit = mCloCredit + Integer.valueOf(m.getClosingCredit());

                gPreDebit = gPreDebit + Integer.valueOf(m.getPrvDebit());
                gPreCredit = gPreCredit + Integer.valueOf(m.getPrvCredit());
                gTraDebit = gTraDebit + Integer.valueOf(m.getTraDebit());
                gTraCredit = gTraCredit + Integer.valueOf(m.getTraCredit());
                gCloDebit = gCloDebit + Integer.valueOf(m.getClosingDebit());
                gCloCredit = gCloCredit + Integer.valueOf(m.getClosingCredit());
            }
        }

        if (group) {
            table.addCell(total);
            table.addCell(getCell(String.valueOf(preDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(preCredit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(traDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(traCredit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(cloDebit), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(cloCredit), PdfPCell.ALIGN_RIGHT));
            group = false;
        }
    }
}
