package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import org.by9steps.shadihall.adapters.SummerizeTrialBalanceAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.CashEntry;
import org.by9steps.shadihall.model.Recovery;
import org.by9steps.shadihall.model.Summerize;
import org.by9steps.shadihall.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummerizeTrailBalFragment extends Fragment implements View.OnClickListener {

    ProgressDialog mProgress;
    String currentDate;

    DatabaseHelper databaseHelper;
    List<Summerize> summerizeList;
    List<Summerize> filterdList;

    RecyclerView recyclerView;
    TextView acType, acGroup, debitBal, creditBal;
    List<Summerize> mList = new ArrayList<>();
    String m = "First";
    int debBal, creBal;
    int gDebBal, gCreBal;

    static Button date_picker;
    ImageView refresh;

    SearchView searchView;
    Spinner spinner;
    int filter;
    SummerizeTrialBalanceAdapter adapter;

    //Print
    private static final String TAG = "PdfCreatorActivity";
    private File pdfFile;

    //Sorting
    String orderBy = "AcTypeName";
    int status = 0;
    String orderby = " ORDER BY " + orderBy + " DESC";


    public SummerizeTrailBalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summerize_trail_bal, container, false);

        setHasOptionsMenu(true);

        date_picker = view.findViewById(R.id.date_picker);
        refresh = view.findViewById(R.id.refresh);
        recyclerView = view.findViewById(R.id.recycler);
        spinner = view.findViewById(R.id.sum_trial_spinner);
        searchView = view.findViewById(R.id.sum_trial_search);
//        acType = view.findViewById(R.id.stb_acType);
        acGroup = view.findViewById(R.id.stb_acGroup);
        debitBal = view.findViewById(R.id.stb_debitBal);
        creditBal = view.findViewById(R.id.stb_creditBal);

//        acType.setOnClickListener(this);
        acGroup.setOnClickListener(this);
        debitBal.setOnClickListener(this);
        creditBal.setOnClickListener(this);
        refresh.setOnClickListener(this);
        date_picker.setOnClickListener(this);

        databaseHelper = new DatabaseHelper(getContext());

        Date date = new Date();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        date_picker.setText(curFormater.format(date));

//        m = "First";
        getSummerize();

        // Spinner Drop down elements
        List<String> spinner_list = new ArrayList<String>();
        spinner_list.add("Select");
        spinner_list.add("AcType");
        spinner_list.add("AcGroup");
        spinner_list.add("Debit Bal");
        spinner_list.add("Credit Bal");

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

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.stb_acGroup:
                orderBy = "AcGruopName";
                orderBy(orderBy);
                break;
//            case R.id.stb_acType:
//                orderBy = "AcTypeName";
//                orderBy(orderBy);
//                break;
            case R.id.stb_debitBal:
                orderBy = "DebitBL";
                orderBy(orderBy);
                break;
            case R.id.stb_creditBal:
                orderBy = "CreditBL";
                orderBy(orderBy);
                break;
            case R.id.date_picker:
                AppController.date = "Summarize";
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
                break;
            case R.id.refresh:
                getSummerize();
                filter = 0;
                searchView.setQuery("", false);
                searchView.clearFocus();
                break;
        }
    }

    public void orderBy(String order_by){
        if (status == 0) {
            status = 1;
            orderby = " ORDER BY " + order_by + " DESC";
        } else {
            status = 0;
            orderby = " ORDER BY " + order_by + " ASC";
        }
        getSummerize();
    }

    public void getSummerize(){

        mList.clear();
        debBal = 0; creBal = 0;
        gDebBal = 0; gCreBal = 0;
        m = "First";

        currentDate = date_picker.getText().toString();

        List<User> list = User.listAll(User.class);
        for (User u : list){
            String query = "SELECT        Account1Type.AcTypeID, Account1Type.AcTypeName, Account2Group.AcGroupID, Account2Group.AcGruopName, SUM(derivedtbl_1.Debit) AS Debit, SUM(derivedtbl_1.Credit) AS Credit, derivedtbl_1.ClientID,\n" +
                    "                         SUM(derivedtbl_1.Debit) - SUM(derivedtbl_1.Credit) AS Bal, CASE WHEN (SUM(Debit) - SUM(Credit)) > 0 THEN (SUM(Debit) - SUM(Credit)) ELSE 0 END AS DebitBL, CASE WHEN (SUM(Debit) - SUM(Credit)) < 0 THEN (SUM(Debit)\n" +
                    "                         - SUM(Credit)) ELSE 0 END AS CreditBL\n" +
                    "FROM            (SELECT        CreditAccount AS AccountID, 0 AS Debit, Amount AS Credit, ClientID, CBDate\n" +
                    "                          FROM            CashBook AS CashBook\n" +
                    "                          WHERE        (ClientID = "+u.getClientID()+") AND (CBDate <= '"+currentDate+"')\n" +
                    "                          UNION ALL\n" +
                    "                          SELECT        DebitAccount AS AccountID, Amount AS Debit, 0 AS Credit, ClientID, CBDate\n" +
                    "                          FROM            CashBook AS CashBook_1\n" +
                    "                          WHERE        (ClientID = "+u.getClientID()+") AND (CBDate <= '"+currentDate+"')) AS derivedtbl_1 INNER JOIN\n" +
                    "                         Account3Name ON derivedtbl_1.AccountID = Account3Name.AcNameID INNER JOIN\n" +
                    "                         Account2Group ON Account3Name.AcGroupID = Account2Group.AcGroupID INNER JOIN\n" +
                    "                         Account1Type ON Account2Group.AcTypeID = Account1Type.AcTypeID\n" +
                    "GROUP BY derivedtbl_1.ClientID, Account2Group.AcGroupID, Account2Group.AcGruopName, Account1Type.AcTypeName, Account1Type.AcTypeID" + orderby;
            summerizeList = databaseHelper.getSummerizeTB(query);
        }
//        int a = 0;
//        List<Summerize> list1 = new ArrayList<>();
//        List<Summerize> list2 = new ArrayList<>();
//        List<Summerize> list3 = new ArrayList<>();
//        List<Summerize> list4 = new ArrayList<>();
//        for (Summerize s : summerizeList){
//
//            m = s.getAcTypeName();
//
//            if(m.equals("Assets And Liability")){
//                list1.add(s);
//            }else if (m.equals("Expense")){
//                list2.add(s);
//            }else if (m.equals("Revenue")){
//                list3.add(s);
//            }else if (m.equals("Capital")){
//                list4.add(s);
//            }
//        }
//
//        debBal = 0; creBal = 0;
//        for (Summerize s : list1){
//            if (a == 0){
//                mList.add(Summerize.createSection(s.getAcTypeName()));
//                a = 1;
//            }
//            mList.add(Summerize.createRow(s.getAcTypeID(),s.getAcTypeName(),s.getAcGroupID(),s.getAcGruopName(),s.getDebit(),s.getCredit(),s.getClientID(),s.getBal(),s.getDebitBL(),s.getCreditBL()));
//            debBal = Integer.valueOf(s.getDebitBL()) + debBal;
//            creBal = Integer.valueOf(s.getCreditBL()) + creBal;
//            gDebBal = Integer.valueOf(s.getDebitBL()) + gDebBal;
//            gCreBal = Integer.valueOf(s.getCreditBL()) + gCreBal;
//        }
//        if (list1.size() > 0) {
//            mList.add(Summerize.createTotal(String.valueOf(debBal), String.valueOf(creBal)));
//        }
//
//        debBal = 0; creBal = 0;
//        for (Summerize s : list2){
//            if (a == 1){
//                mList.add(Summerize.createSection(s.getAcTypeName()));
//                a = 2;
//            }
//            mList.add(Summerize.createRow(s.getAcTypeID(),s.getAcTypeName(),s.getAcGroupID(),s.getAcGruopName(),s.getDebit(),s.getCredit(),s.getClientID(),s.getBal(),s.getDebitBL(),s.getCreditBL()));
//            debBal = Integer.valueOf(s.getDebitBL()) + debBal;
//            creBal = Integer.valueOf(s.getCreditBL()) + creBal;
//            gDebBal = Integer.valueOf(s.getDebitBL()) + gDebBal;
//            gCreBal = Integer.valueOf(s.getCreditBL()) + gCreBal;
//        }
//        if (list2.size() > 0) {
//            mList.add(Summerize.createTotal(String.valueOf(debBal), String.valueOf(creBal)));
//        }
//        debBal = 0; creBal = 0;
//        for (Summerize s : list3){
//            if (a == 2){
//                mList.add(Summerize.createSection(s.getAcTypeName()));
//                a = 3;
//            }
//            mList.add(Summerize.createRow(s.getAcTypeID(),s.getAcTypeName(),s.getAcGroupID(),s.getAcGruopName(),s.getDebit(),s.getCredit(),s.getClientID(),s.getBal(),s.getDebitBL(),s.getCreditBL()));
//            debBal = Integer.valueOf(s.getDebitBL()) + debBal;
//            creBal = Integer.valueOf(s.getCreditBL()) + creBal;
//            gDebBal = Integer.valueOf(s.getDebitBL()) + gDebBal;
//            gCreBal = Integer.valueOf(s.getCreditBL()) + gCreBal;
//        }
//        if (list3.size() > 0) {
//            mList.add(Summerize.createTotal(String.valueOf(debBal), String.valueOf(creBal)));
//        }
//        debBal = 0; creBal = 0;
//        for (Summerize s : list4){
//            if (a == 3){
//                mList.add(Summerize.createSection(s.getAcTypeName()));
//                a = 4;
//            }
//            mList.add(Summerize.createRow(s.getAcTypeID(),s.getAcTypeName(),s.getAcGroupID(),s.getAcGruopName(),s.getDebit(),s.getCredit(),s.getClientID(),s.getBal(),s.getDebitBL(),s.getCreditBL()));
//            debBal = Integer.valueOf(s.getDebitBL()) + debBal;
//            creBal = Integer.valueOf(s.getCreditBL()) + creBal;
//            gDebBal = Integer.valueOf(s.getDebitBL()) + gDebBal;
//            gCreBal = Integer.valueOf(s.getCreditBL()) + gCreBal;
//        }
//        if (list4.size() > 0) {
//            mList.add(Summerize.createTotal(String.valueOf(debBal), String.valueOf(creBal)));
//        }


        for (Summerize s : summerizeList){
            if (m.equals("First")) {
                mList.add(Summerize.createSection(s.getAcTypeName()));
                mList.add(Summerize.createRow(s.getAcTypeID(),s.getAcTypeName(),s.getAcGroupID(),s.getAcGruopName(),s.getDebit(),s.getCredit(),s.getClientID(),s.getBal(),s.getDebitBL(),s.getCreditBL()));
                m = s.getAcTypeName();

                debBal = Integer.valueOf(s.getDebitBL()) + debBal;
                creBal = Integer.valueOf(s.getCreditBL()) + creBal;
                gDebBal = Integer.valueOf(s.getDebitBL()) + gDebBal;
                gCreBal = Integer.valueOf(s.getCreditBL()) + gCreBal;

            }else if (m.equals(s.getAcTypeName())){
                debBal = Integer.valueOf(s.getDebitBL()) + debBal;
                creBal = Integer.valueOf(s.getCreditBL()) + creBal;
                gDebBal = Integer.valueOf(s.getDebitBL()) + gDebBal;
                gCreBal = Integer.valueOf(s.getCreditBL()) + gCreBal;
                mList.add(Summerize.createRow(s.getAcTypeID(),s.getAcTypeName(),s.getAcGroupID(),s.getAcGruopName(),s.getDebit(),s.getCredit(),s.getClientID(),s.getBal(),s.getDebitBL(),s.getCreditBL()));
            }else {
                mList.add(Summerize.createTotal(String.valueOf(debBal),String.valueOf(creBal)));
                debBal = 0; creBal = 0;
                debBal = Integer.valueOf(s.getDebitBL()) + debBal;
                creBal = Integer.valueOf(s.getCreditBL()) + creBal;
                gDebBal = Integer.valueOf(s.getDebitBL()) + gDebBal;
                gCreBal = Integer.valueOf(s.getCreditBL()) + gCreBal;
                mList.add(Summerize.createSection(s.getAcTypeName()));
                mList.add(Summerize.createRow(s.getAcTypeID(),s.getAcTypeName(),s.getAcGroupID(),s.getAcGruopName(),s.getDebit(),s.getCredit(),s.getClientID(),s.getBal(),s.getDebitBL(),s.getCreditBL()));
                m = s.getAcTypeName();
            }
        }

        mList.add(Summerize.createTotal(String.valueOf(debBal),String.valueOf(creBal)));
        mList.add(Summerize.createSection("Grand Total"));
        mList.add(Summerize.createTotal(String.valueOf(gDebBal),String.valueOf(gCreBal)));
        adapter = new SummerizeTrialBalanceAdapter(getContext(),mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

//    public void getSummerize(){
//        mProgress = new ProgressDialog(getContext());
//        mProgress.setTitle("Loading");
//        mProgress.setMessage("Please wait...");
//        mProgress.setCanceledOnTouchOutside(false);
//        mProgress.show();
//
//        String tag_json_obj = "json_obj_req";
//        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/SummerizeTrailBalance.php";
//
//        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        mProgress.dismiss();
//                        JSONObject jsonObj = null;
//
//                        try {
//                            jsonObj= new JSONObject(response);
//                            JSONArray jsonArray = jsonObj.getJSONArray("Summerize");
//                            Log.e("SSSSS",jsonArray.toString());
//                            String success = jsonObj.getString("success");
//                            if (success.equals("1")){
////                                Account3Name.deleteAll(Account3Name.class);
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                    Log.e("Recovery",jsonObject.toString());
//                                    String AcTypeID = jsonObject.getString("AcTypeID");
//                                    String AcTypeName = jsonObject.getString("AcTypeName");
//                                    String AcGroupID = jsonObject.getString("AcGroupID");
//                                    String AcGruopName = jsonObject.getString("AcGruopName");
//                                    String Debit = jsonObject.getString("Debit");
//                                    String Credit = jsonObject.getString("Credit");
//                                    String ClientID = jsonObject.getString("ClientID");
//                                    String Bal = jsonObject.getString("Bal");
//                                    String DebitBL = jsonObject.getString("DebitBL");
//                                    String CreditBL = jsonObject.getString("CreditBL");
//
//                                    if (m.equals("First")) {
//                                        mList.add(Summerize.createSection(AcTypeName));
//                                        mList.add(Summerize.createRow(AcTypeID,AcTypeName,AcGroupID,AcGruopName,Debit,Credit,ClientID,Bal,DebitBL,CreditBL));
//                                        m = AcTypeName;
//
//                                        debBal = Integer.valueOf(DebitBL) + debBal;
//                                        creBal = Integer.valueOf(CreditBL) + creBal;
//                                        gDebBal = Integer.valueOf(DebitBL) + gDebBal;
//                                        gCreBal = Integer.valueOf(CreditBL) + gCreBal;
//
//                                    }else if (m.equals(AcTypeName)){
//                                        debBal = Integer.valueOf(DebitBL) + debBal;
//                                        creBal = Integer.valueOf(CreditBL) + creBal;
//                                        gDebBal = Integer.valueOf(DebitBL) + gDebBal;
//                                        gCreBal = Integer.valueOf(CreditBL) + gCreBal;
//                                        mList.add(Summerize.createRow(AcTypeID,AcTypeName,AcGroupID,AcGruopName,Debit,Credit,ClientID,Bal,DebitBL,CreditBL));
//                                    }else {
//                                        mList.add(Summerize.createTotal(String.valueOf(debBal),String.valueOf(creBal)));
//                                        debBal = 0; creBal = 0;
//                                        debBal = Integer.valueOf(DebitBL) + debBal;
//                                        creBal = Integer.valueOf(CreditBL) + creBal;
//                                        gDebBal = Integer.valueOf(DebitBL) + gDebBal;
//                                        gCreBal = Integer.valueOf(CreditBL) + gCreBal;
//                                        mList.add(Summerize.createSection(AcTypeName));
//                                        mList.add(Summerize.createRow(AcTypeID,AcTypeName,AcGroupID,AcGruopName,Debit,Credit,ClientID,Bal,DebitBL,CreditBL));
//                                        m = AcTypeName;
//                                    }
//                                }
//                                mList.add(Summerize.createTotal(String.valueOf(debBal),String.valueOf(creBal)));
//                                mList.add(Summerize.createSection("Grand Total"));
//                                mList.add(Summerize.createTotal(String.valueOf(gDebBal),String.valueOf(gCreBal)));
//                                SummerizeTrialBalanceAdapter adapter = new SummerizeTrialBalanceAdapter(getContext(),mList);
//                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                                recyclerView.setAdapter(adapter);
//
//                                mProgress.dismiss();
//
//                            }else {
//                                String message = jsonObj.getString("message");
//                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mProgress.dismiss();
//                Log.e("Error",error.toString());
//                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                List<User> list = User.listAll(User.class);
//                for (User u: list) {
//                    params.put("ClientID", u.getClientID());
//                    params.put("CBDate", currentDate);
//                }
//                return params;
//            }
//        };
//        int socketTimeout = 10000;//10 seconds - change to what you want
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        jsonObjectRequest.setRetryPolicy(policy);
//        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
//    }

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
            for (Summerize s : mList) {
                if (s.isRow() == 1) {
                    switch (filter) {
                        case 1:
                            if (s.getAcTypeName().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 2:
                            if (s.getAcGruopName().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 3:
                            if (s.getDebitBL().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 4:
                            if (s.getCreditBL().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
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

        PdfPTable title = new PdfPTable(new float[]{3, 5, 3});
        title.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        title.getDefaultCell().setFixedHeight(30);
        title.setTotalWidth(PageSize.A4.getWidth());
        title.setWidthPercentage(100);
        title.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.setSpacingBefore(5);
        title.addCell(footerCell("",PdfPCell.ALIGN_CENTER));
        PdfPCell cell = new PdfPCell(new Phrase("Summarize Trial Balance",chapterFont));
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

        PdfPTable table = new PdfPTable(new float[]{3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(40);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.setSpacingBefore(20);
        table.addCell("AcGroup");
        table.addCell("Debit Bal");
        table.addCell("Credit Bal");
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

        List<Summerize> list1 = new ArrayList<>();
        List<Summerize> list2 = new ArrayList<>();
        List<Summerize> list3 = new ArrayList<>();
        List<Summerize> list4 = new ArrayList<>();

        for (Summerize c : summerizeList){

            m = c.getAcTypeName();

            if(m.equals("Assets And Liability")){
                list1.add(c);
            }else if (m.equals("Expense")){
                list2.add(c);
            }else if (m.equals("Revenue")){
                list3.add(c);
            }else if (m.equals("Capital")){
                list4.add(c);
            }
        }

        int a = 0;
        debBal = 0; creBal = 0; gDebBal = 0; gCreBal = 0;
        for (Summerize s : list1){
            if (a == 0) {
                PdfPCell section = new PdfPCell(new Phrase(s.getAcTypeName(), totalFont));
                section.setBorder(PdfPCell.NO_BORDER);
                section.setFixedHeight(30);
                section.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(section);
                table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                a = 1;
            }

            table.addCell(getCell(s.getAcGruopName(), PdfPCell.ALIGN_LEFT));
            table.addCell(getCell(s.getDebitBL(), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(s.getCreditBL(), PdfPCell.ALIGN_RIGHT));

            debBal = Integer.valueOf(s.getDebitBL()) + debBal;
            creBal = Integer.valueOf(s.getCreditBL()) + creBal;
            gDebBal = Integer.valueOf(s.getDebitBL()) + gDebBal;
            gCreBal = Integer.valueOf(s.getCreditBL()) + gCreBal;
        }
        if (list1.size() > 0) {
            table.addCell(total);
            table.addCell(getCell(String.valueOf(debBal), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(creBal), PdfPCell.ALIGN_RIGHT));
        }
        debBal = 0; creBal = 0;
        for (Summerize s : list2){
            if (a == 1) {
                PdfPCell section = new PdfPCell(new Phrase(s.getAcTypeName(), totalFont));
                section.setBorder(PdfPCell.NO_BORDER);
                section.setFixedHeight(30);
                section.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(section);
                table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                a = 2;
            }

            table.addCell(getCell(s.getAcGruopName(), PdfPCell.ALIGN_LEFT));
            table.addCell(getCell(s.getDebitBL(), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(s.getCreditBL(), PdfPCell.ALIGN_RIGHT));

            debBal = Integer.valueOf(s.getDebitBL()) + debBal;
            creBal = Integer.valueOf(s.getCreditBL()) + creBal;
            gDebBal = Integer.valueOf(s.getDebitBL()) + gDebBal;
            gCreBal = Integer.valueOf(s.getCreditBL()) + gCreBal;
        }
        if (list2.size() > 0) {
            table.addCell(total);
            table.addCell(getCell(String.valueOf(debBal), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(creBal), PdfPCell.ALIGN_RIGHT));
        }
        debBal = 0; creBal = 0;
        for (Summerize s : list3){
            if(a == 2) {
                PdfPCell section = new PdfPCell(new Phrase(s.getAcTypeName(), totalFont));
                section.setBorder(PdfPCell.NO_BORDER);
                section.setFixedHeight(30);
                section.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(section);
                table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                a = 3;
            }

            table.addCell(getCell(s.getAcGruopName(), PdfPCell.ALIGN_LEFT));
            table.addCell(getCell(s.getDebitBL(), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(s.getCreditBL(), PdfPCell.ALIGN_RIGHT));

            debBal = Integer.valueOf(s.getDebitBL()) + debBal;
            creBal = Integer.valueOf(s.getCreditBL()) + creBal;
            gDebBal = Integer.valueOf(s.getDebitBL()) + gDebBal;
            gCreBal = Integer.valueOf(s.getCreditBL()) + gCreBal;
        }
        if (list3.size() > 0) {
            table.addCell(total);
            table.addCell(getCell(String.valueOf(debBal), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(creBal), PdfPCell.ALIGN_RIGHT));
        }
        debBal = 0; creBal = 0;
        for (Summerize s : list4){
            if (a == 3) {
                PdfPCell section = new PdfPCell(new Phrase(s.getAcTypeName(), totalFont));
                section.setBorder(PdfPCell.NO_BORDER);
                section.setFixedHeight(30);
                section.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
                section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(section);
                table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                a = 4;
            }

            table.addCell(getCell(s.getAcGruopName(), PdfPCell.ALIGN_LEFT));
            table.addCell(getCell(s.getDebitBL(), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(s.getCreditBL(), PdfPCell.ALIGN_RIGHT));

            debBal = Integer.valueOf(s.getDebitBL()) + debBal;
            creBal = Integer.valueOf(s.getCreditBL()) + creBal;
            gDebBal = Integer.valueOf(s.getDebitBL()) + gDebBal;
            gCreBal = Integer.valueOf(s.getCreditBL()) + gCreBal;
        }
        if (list4.size() > 0) {
            table.addCell(total);
            table.addCell(getCell(String.valueOf(debBal), PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell(String.valueOf(creBal), PdfPCell.ALIGN_RIGHT));
        }

        table.addCell(getCell("Grand Total",PdfPCell.ALIGN_CENTER));
        table.addCell(getCell(String.valueOf(gDebBal), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gCreBal), PdfPCell.ALIGN_RIGHT));

//        for (Summerize c : summerizeList){
//            if (m.equals("First")){
//                table.addCell(getCell(c.getAcTypeName(), PdfPCell.ALIGN_LEFT));
//                table.addCell(getCell(c.getAcGruopName(), PdfPCell.ALIGN_LEFT));
//                table.addCell(getCell(c.getDebitBL(), PdfPCell.ALIGN_RIGHT));
//                table.addCell(getCell(c.getCreditBL(), PdfPCell.ALIGN_RIGHT));
//                m = c.getAcTypeName();
//
//                debBal = Integer.valueOf(c.getDebitBL()) + debBal;
//                creBal = Integer.valueOf(c.getCreditBL()) + creBal;
//                gDebBal = Integer.valueOf(c.getDebitBL()) + gDebBal;
//                gCreBal = Integer.valueOf(c.getCreditBL()) + gCreBal;
//            }else if (m.equals(c.getAcTypeName())){
//
//                table.addCell(getCell(c.getAcTypeName(), PdfPCell.ALIGN_LEFT));
//                table.addCell(getCell(c.getAcGruopName(), PdfPCell.ALIGN_LEFT));
//                table.addCell(getCell(c.getDebitBL(), PdfPCell.ALIGN_RIGHT));
//                table.addCell(getCell(c.getCreditBL(), PdfPCell.ALIGN_RIGHT));
//
//                debBal = Integer.valueOf(c.getDebitBL()) + debBal;
//                creBal = Integer.valueOf(c.getCreditBL()) + creBal;
//                gDebBal = Integer.valueOf(c.getDebitBL()) + gDebBal;
//                gCreBal = Integer.valueOf(c.getCreditBL()) + gCreBal;
//            }else if (!m.equals("First") && !m.equals(c.getAcTypeName())){
//                m = c.getAcTypeName();
//                table.addCell("");
//                table.addCell("Total");
//                table.addCell(getCell(String.valueOf(debBal),PdfPCell.ALIGN_RIGHT));
//                table.addCell(getCell(String.valueOf(creBal),PdfPCell.ALIGN_RIGHT));
//                debBal = 0; creBal = 0;
//
//                table.addCell(getCell(c.getAcTypeName(), PdfPCell.ALIGN_LEFT));
//                table.addCell(getCell(c.getAcGruopName(), PdfPCell.ALIGN_LEFT));
//                table.addCell(getCell(c.getDebitBL(), PdfPCell.ALIGN_RIGHT));
//                table.addCell(getCell(c.getCreditBL(), PdfPCell.ALIGN_RIGHT));
//
//                debBal = Integer.valueOf(c.getDebitBL()) + debBal;
//                creBal = Integer.valueOf(c.getCreditBL()) + creBal;
//                gDebBal = Integer.valueOf(c.getDebitBL()) + gDebBal;
//                gCreBal = Integer.valueOf(c.getCreditBL()) + gCreBal;
//            }
//        }
//
//        table.addCell("");
//        table.addCell("Total");
//        table.addCell(getCell(String.valueOf(debBal),PdfPCell.ALIGN_RIGHT));
//        table.addCell(getCell(String.valueOf(creBal),PdfPCell.ALIGN_RIGHT));
//
//        table.addCell("");
//        table.addCell("Grand Total");
//        table.addCell(getCell(String.valueOf(gDebBal),PdfPCell.ALIGN_RIGHT));
//        table.addCell(getCell(String.valueOf(gCreBal),PdfPCell.ALIGN_RIGHT));

//        Footer footer = new Footer();

        document.open();

        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.BLACK);
        Paragraph paragraph = new Paragraph("Cash Book \n\n", f);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(chunk);
        document.add(name);
        document.add(contact);
        document.add(title);
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
            table.addCell(footerCell(String.format("Page %d ", writer.getPageNumber() -1),PdfPCell.ALIGN_LEFT));
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
}
