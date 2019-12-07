package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
import android.arch.lifecycle.ReportFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
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
import org.by9steps.shadihall.BuildConfig;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.ChaartOfAccAddActivity;
import org.by9steps.shadihall.activities.MenuClickActivity;
import org.by9steps.shadihall.adapters.ReportsAdapter;
import org.by9steps.shadihall.adapters.SpinnerAdapter;
import org.by9steps.shadihall.callingapi.Account3NameApis;
import org.by9steps.shadihall.chartofaccountdialog.CustomDialogOnDismisListener;
import org.by9steps.shadihall.chartofaccountdialog.DialogForAccountingTypeItem;
import org.by9steps.shadihall.chartofaccountdialog.DialogForCliSup;
import org.by9steps.shadihall.chartofaccountdialog.DialogForEmployee;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.ViewDBAllData;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.Account2Group;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.Reports;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsFragment extends Fragment implements View.OnClickListener,
        ReportsAdapter.PopupMyCustomCallBackMethod, AdapterView.OnItemSelectedListener {

    private static final String ARG_SPINNER_ID = "spinner_position";

    TextView r_acname, r_debitbal, r_creditbal, itemnewadded, itemupdated;
    int countForNewAdded = 0, countForUpdated = 0;
    String orderBy = "AcName";
    int status = 0;
    String orderby = " ORDER BY " + orderBy + " DESC";
    String updatedDate;

    ProgressDialog mProgress;
    RecyclerView recyclerView;
    String currentDate;
    int day, month, year;
    static Button date_picker;
    Spinner sp_acgroup;
    ImageView add;
    ReportsAdapter adapter;

    DatabaseHelper databaseHelper;
    Prefrence prefrence;

    List<Reports> reportsList;
    List<Reports> filterdList;

    //    EditText search;
    Spinner spinner;
    public SearchView searchView;
    public int filter;

    //Print
    private File pdfFile;

    public TextView deb_total, cre_total;

    // TODO: Rename and change types of parameters
    private String spPosition;

    public static ReportsFragment newInstance(String message) {
        ReportsFragment fragment = new ReportsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SPINNER_ID, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            spPosition = getArguments().getString(ARG_SPINNER_ID);
        }
    }

    public ReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.recycler);
        date_picker = view.findViewById(R.id.date_picker);
        sp_acgroup = view.findViewById(R.id.sp_acgroup);
        sp_acgroup.setOnItemSelectedListener(this);
        itemupdated = view.findViewById(R.id.itemupdated);
        itemnewadded = view.findViewById(R.id.itemnewadded);


        deb_total = view.findViewById(R.id.deb_total);
        cre_total = view.findViewById(R.id.cre_total);

        add = view.findViewById(R.id.add);

        r_acname = view.findViewById(R.id.r_acname);
        r_debitbal = view.findViewById(R.id.r_debitbal);
        r_creditbal = view.findViewById(R.id.r_creditbal);

        r_acname.setOnClickListener(this);
        r_debitbal.setOnClickListener(this);
        r_creditbal.setOnClickListener(this);


        searchView = view.findViewById(R.id.report_search);
        spinner = view.findViewById(R.id.report_spinner);

        databaseHelper = new DatabaseHelper(getContext());
        prefrence = new Prefrence(getContext());

        // Spinner Drop down elements
        List<String> spinner_list = new ArrayList<String>();
        spinner_list.add("Select");
        spinner_list.add("Ac Name");
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        add.setOnClickListener(this);

        Date date = new Date();
        date_picker.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));

        List<Account2Group> list = databaseHelper.getAccount2Group("SELECT * FROM Account2Group");
        if (list == null || list.isEmpty()) {
            getAccountGroups();
            list = list = databaseHelper.getAccount2Group("SELECT * FROM Account2Group");
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getContext(), list, "");
            sp_acgroup.setAdapter(spinnerAdapter);
        } else {
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getContext(), list, "");
            sp_acgroup.setAdapter(spinnerAdapter);

        }

        sp_acgroup.setSelection(Integer.valueOf(spPosition));

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");

            }
        });

//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getReports();
//                filter = 0;
//                searchView.setQuery("", false);
//                searchView.clearFocus();
//            }
//        });

        getReports();


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

        Log.e(GenericConstants.MYEdiMenttion, "Code Edited");
        date_picker.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(ReportsFragment.this.getContext(), ViewDBAllData.class));
                return true;
            }
        });
       // add.setOnLongClickListener(longClickListenerForaddbtn);
        return view;
    }

    public void getReports() {


//        String query = "SELECT        GroupTotal.AccountID, SUM(GroupTotal.Debit) AS Debit, SUM(GroupTotal.Credit) AS Credit, SUM(GroupTotal.Debit) - SUM(GroupTotal.Credit) AS Bal, CASE WHEN (SUM(Debit) - SUM(Credit)) > 0 THEN (SUM(Debit) - SUM(Credit))\n" +
//                "                         ELSE 0 END AS DebitBal, CASE WHEN (SUM(Debit) - SUM(Credit)) < 0 THEN (SUM(Debit) - SUM(Credit)) ELSE 0 END AS CreditBal, Account3Name_2.AcName, Account3Name_2.AcGroupID\n" +
//                "FROM            (SELECT        CashBook.DebitAccount AS AccountID, CashBook.Amount AS Debit, 0 AS Credit\n" +
//                "                          FROM            CashBook INNER JOIN\n" +
//                "                                                    Account3Name ON CashBook.DebitAccount = Account3Name.AcNameID\n" +
//                "                          WHERE        (CashBook.ClientID = " + prefrence.getClientIDSession() + ") AND (CashBook.CBDate <= '" + date_picker.getText().toString() + "') AND (Account3Name.AcGroupID = " + ((Account2Group) sp_acgroup.getSelectedItem()).getAcGroupID() + ")\n" +
//                "                          UNION ALL\n" +
//                "                          SELECT        CashBook_1.CreditAccount AS AccountID, 0 AS Debit, CashBook_1.Amount AS Credit\n" +
//                "                          FROM            CashBook AS CashBook_1 INNER JOIN\n" +
//                "                                                   Account3Name AS Account3Name_1 ON CashBook_1.CreditAccount = Account3Name_1.AcNameID\n" +
//                "                          WHERE        (CashBook_1.ClientID = " + prefrence.getClientIDSession() + ") AND (CashBook_1.CBDate <= '" + date_picker.getText().toString() + "') AND (Account3Name_1.AcGroupID = " + ((Account2Group) sp_acgroup.getSelectedItem()).getAcGroupID() + ")) AS GroupTotal INNER JOIN\n" +
//                "                         Account3Name AS Account3Name_2 ON GroupTotal.AccountID = Account3Name_2.AcNameID\n" +
//                "GROUP BY GroupTotal.AccountID, Account3Name_2.AcName, Account3Name_2.AcGroupID" +
//                orderby;
        countForNewAdded=countForUpdated=0;
        String groupID = ((Account2Group) sp_acgroup.getSelectedItem()).getAcGroupID();
        String date = date_picker.getText().toString();
//        String query = "SELECT        Account3Name.AcNameID AS AccountID, AccountBalanct.Debit, AccountBalanct.Credit, AccountBalanct.Bal, AccountBalanct.DebitBal, AccountBalanct.CreditBal, \n" +
//                "                         Account3Name.AcName,Account3Name.ID,Account3Name.UpdatedDate,Account3Name.AcGroupID\n" +
//                "FROM            Account3Name LEFT OUTER JOIN\n" +
//                "                             (SELECT        AccountID, SUM(Debit) AS Debit, SUM(Credit) AS Credit, SUM(Debit) - SUM(Credit) AS Bal, CASE WHEN (SUM(Debit) - SUM(Credit)) > 0 THEN (SUM(Debit)\n" +
//                "                                                          - SUM(Credit)) ELSE 0 END AS DebitBal, CASE WHEN (SUM(Debit) - SUM(Credit)) < 0 THEN (SUM(Debit) - SUM(Credit)) ELSE 0 END AS CreditBal\n" +
//                "                               FROM            (SELECT        DebitAccount AS AccountID, Amount AS Debit, 0 AS Credit\n" +
//                "                                                         FROM            CashBook\n" +
//                "                                                         WHERE        (ClientID = " + prefrence.getClientIDSession() + ") AND (CBDate <= '" + date + "')\n" +
//                "                                                         UNION ALL\n" +
//                "                                                         SELECT        CreditAccount AS AccountID, 0 AS Debit, Amount AS Credit\n" +
//                "                                                         FROM            CashBook AS CashBook_1\n" +
//                "                                                         WHERE        (ClientID = " + prefrence.getClientIDSession() + ") AND (CBDate <= '" + date + "')) AS GroupTotal\n" +
//                "                               GROUP BY AccountID) AS AccountBalanct ON Account3Name.AcNameID = AccountBalanct.AccountID\n" +
//                "WHERE        (Account3Name.AcGroupID = " + groupID + ")";
//

        String query1="Select\n" +
                "    Account3Name.AcNameID As AccountID,\n" +
                "    AccountBalanct.Debit,\n" +
                "    AccountBalanct.Credit,\n" +
                "    AccountBalanct.Bal,\n" +
                "    AccountBalanct.DebitBal,\n" +
                "    AccountBalanct.CreditBal,\n" +
                "    Account3Name.AcName,\n" +
                "    Account3Name.ID,\n" +
                "    Account3Name.UpdatedDate,\n" +
                "    Account3Name.AcGroupID,\n" +
                "    Account3Name.ClientID\n" +
                "From\n" +
                "    Account3Name Left Join\n" +
                "    (Select\n" +
                "         GroupTotal.AccountID,\n" +
                "         Sum(GroupTotal.Debit) As Debit,\n" +
                "         Sum(GroupTotal.Credit) As Credit,\n" +
                "         Sum(GroupTotal.Debit) - Sum(GroupTotal.Credit) As Bal,\n" +
                "         Case\n" +
                "             When (Sum(GroupTotal.Debit) - Sum(GroupTotal.Credit)) > 0\n" +
                "             Then (Sum(GroupTotal.Debit) - Sum(GroupTotal.Credit))\n" +
                "             Else 0\n" +
                "         End As DebitBal,\n" +
                "         Case\n" +
                "             When (Sum(GroupTotal.Debit) - Sum(GroupTotal.Credit)) < 0\n" +
                "             Then (Sum(GroupTotal.Debit) - Sum(GroupTotal.Credit))\n" +
                "             Else 0\n" +
                "         End As CreditBal,\n" +
                "         GroupTotal.ClientID\n" +
                "     From\n" +
                "         (Select\n" +
                "              CashBook.DebitAccount As AccountID,\n" +
                "              CashBook.Amount As Debit,\n" +
                "              0 As Credit,\n" +
                "              CashBook.ClientID\n" +
                "          From\n" +
                "              CashBook\n" +
                "          Where\n" +
                "              CashBook.ClientID = '"+prefrence.getClientIDSession()+"' And\n" +
                "              CashBook.CBDate <= '"+date+"'\n" +
                "          Union All\n" +
                "          Select\n" +
                "              CashBook_1.CreditAccount As AccountID,\n" +
                "              0 As Debit,\n" +
                "              CashBook_1.Amount As Credit,\n" +
                "              CashBook_1.ClientID\n" +
                "          From\n" +
                "              CashBook As CashBook_1\n" +
                "          Where\n" +
                "              CashBook_1.ClientID = '"+prefrence.getClientIDSession()+"' And\n" +
                "              CashBook_1.CBDate <= '"+date+"') As GroupTotal\n" +
                "     Group By\n" +
                "         GroupTotal.AccountID,\n" +
                "         GroupTotal.ClientID) As AccountBalanct On Account3Name.AcNameID = AccountBalanct.AccountID\n" +
                "            And AccountBalanct.ClientID = Account3Name.ClientID\n" +
                "Where\n" +
                "    Account3Name.AcGroupID = '"+groupID+"' And\n" +
                "    Account3Name.ClientID = '"+prefrence.getClientIDSession()+"'";

        reportsList = databaseHelper.getReports(query1);


        Log.e("REPORT-QUERY", query1);

        int gCredit = 0, gDebit = 0;
        Log.e("key", "No of item in the list " + reportsList.size());
        for (Reports r : reportsList) {
//            gDebit = gDebit + Integer.valueOf(r.getDebitBal());
//            gCredit = gCredit + Integer.valueOf(r.getCreditBal());
            if (r.getUpdatedDate().equals(GenericConstants.NullFieldStandardText)) {
                if (r.getAccountID().equals("0")) {
                    ///////////////////New Insertion
                    countForNewAdded++;

                } else {
                    //////////////////////Updated Data
                    countForUpdated++;
                }
            }
            Log.e("kk", "D:" + r.getDebitBal() + ":");
            Log.e("kk", "C:" + r.getCreditBal() + ":");
            if (r.getDebitBal() == null || r.getDebitBal().isEmpty()) {
                gDebit = gDebit + 0;
            } else
                gDebit = gDebit + Integer.parseInt(r.getDebitBal());
            if (r.getCreditBal() == null || r.getCreditBal().isEmpty()) {
                gCredit = gCredit + 0;
            } else
                gCredit = gCredit + Integer.parseInt(r.getCreditBal());
        }
        cre_total.setText(String.valueOf(gCredit));
        deb_total.setText(String.valueOf(gDebit));

        adapter = new ReportsAdapter(getContext(), reportsList, ReportsAdapter.Cash_Book);
        adapter.callBackMethod = this;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        if(countForUpdated>0 || countForNewAdded>0){
            itemupdated.setText(""+countForUpdated);
            itemnewadded.setVisibility(View.VISIBLE);
            itemnewadded.setVisibility(View.VISIBLE);
            itemnewadded.setText(""+countForNewAdded);
        }

        // }catch (Exception e){e.printStackTrace();}

    }


    public void getAccountGroups() {
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Loading");
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetAccountGroup.php";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RES", response);
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Account2Group");
                            String success = jsonObj.getString("success");
                            Log.e("Success", success);
                            if (success.equals("1")) {
//                                Account2Group.deleteAll(Account2Group.class);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery", jsonObject.toString());
                                    String AcGroupID = jsonObject.getString("AcGroupID");
                                    String AcTypeID = jsonObject.getString("AcTypeID");
                                    String AcGruopName = jsonObject.getString("AcGruopName");

                                    databaseHelper.createAccount2Group(new Account2Group(AcGroupID, AcTypeID, AcGruopName));

                                }

                                mProgress.dismiss();
//                                getCashBook();

                            } else {
                                String message = jsonObj.getString("message");
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mProgress.dismiss();
                Log.e("Error", error.toString());
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
//                Intent intent = new Intent(getContext(), ChaartOfAccAddActivity.class);
//                intent.putExtra("AcNameID", "0");
//                intent.putExtra("GroupName", ((Account2Group) sp_acgroup.getSelectedItem()).getAcGruopName());
//                intent.putExtra("GroupID", ((Account2Group) sp_acgroup.getSelectedItem()).getAcGroupID());
//                intent.putExtra("Type", "Add");
//                startActivity(intent);

                showMyDialogForForm();
                break;
            case R.id.r_acname:
                orderBy = "AcName";
                if (status == 0) {
                    status = 1;
                    orderby = " ORDER BY " + orderBy + " ASC";
                } else {
                    status = 0;
                    orderby = " ORDER BY " + orderBy + " DESC";
                }
                getReports();
                break;
            case R.id.r_debitbal:
                orderBy = "DebitBal";
                orderBy(orderBy);
                break;
            case R.id.r_creditbal:
                orderBy = "CreditBal";
                orderBy(orderBy);
                break;
        }
    }

    private void filter(String text) {
        filterdList = new ArrayList<>();

        //looping through existing elements
        if (!text.isEmpty()) {
            for (Reports s : reportsList) {
                switch (filter) {
                    case 1:
                        if (s.getAcName().toLowerCase().contains(text.toLowerCase())) {
                            //adding the element to filtered list
                            filterdList.add(s);
                        }
                        break;
                    case 2:
                        if (s.getDebitBal().toLowerCase().contains(text.toLowerCase())) {
                            //adding the element to filtered list
                            filterdList.add(s);
                        }
                        break;
                    case 3:
                        if (s.getCreditBal().toLowerCase().contains(text.toLowerCase())) {
                            //adding the element to filtered list
                            filterdList.add(s);
                        }
                        break;
                }
            }
        } else {
            filterdList = reportsList;
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.filterList(filterdList);

    }

    public void orderBy(String order_by) {
        if (status == 0) {
            status = 1;
            orderby = " ORDER BY " + order_by + " DESC";
        } else {
            status = 0;
            orderby = " ORDER BY " + order_by + " ASC";
        }
        getReports();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cb_menu, menu);
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        } else if (item.getItemId() == R.id.action_print) {
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
        } else if (item.getItemId() == R.id.action_refresh) {
            if (isConnected()) {
                refereshTables(getContext());
            } else {
                Toast.makeText(getContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //Check Internet Connection
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }


    public void createPdf() throws IOException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }
        String pdfname = "Report1.pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, output);
        writer.createXmpMetadata();
        writer.setTagged();
        writer.setPageEvent(new Footer());
        document.open();
        document.addLanguage("en-us");

        PdfDictionary parameters = new PdfDictionary();
        Log.e("PDFDocument", "Created2");
        parameters.put(PdfName.MODDATE, new PdfDate());

        Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
        Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
        Chunk chunk = new Chunk("Client Name", chapterFont);
        Paragraph name = new Paragraph("Address", paragraphFont);
        name.setIndentationLeft(0);
        Paragraph contact = new Paragraph("Contact", paragraphFont);
        contact.setIndentationLeft(0);

        PdfPTable title = new PdfPTable(new float[]{3, 3, 3});
        title.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        title.getDefaultCell().setFixedHeight(30);
        title.setTotalWidth(PageSize.A4.getWidth());
        title.setWidthPercentage(100);
        title.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.setSpacingBefore(5);
        title.addCell(footerCell("", PdfPCell.ALIGN_CENTER));
        PdfPCell cell = new PdfPCell(new Phrase(((MenuClickActivity) getActivity()).getSupportActionBar().getTitle().toString(), chapterFont));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.addCell(cell);
        title.addCell(footerCell("", PdfPCell.ALIGN_CENTER));
        title.addCell(footerCell("", PdfPCell.ALIGN_CENTER));
        title.addCell(footerCell("", PdfPCell.ALIGN_CENTER));

        PdfPCell pCell = pCell = new PdfPCell(new Phrase("Date" + ": " + date_picker.getText().toString()));
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
        table.addCell("Ac Name");
        table.addCell("Debit Bal");
        table.addCell("Credit Bal");
       // table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.PINK);
        }
        int dTotal = 0, cTotal = 0;
        if (filter > 0) {
            for (Reports c : filterdList) {
                table.addCell(getCell(c.getAcName(), PdfPCell.ALIGN_LEFT));
                table.addCell(getCell(c.getDebitBal(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(c.getCreditBal(), PdfPCell.ALIGN_RIGHT));
                ///////////////////////////////My Aditeion
                if (c.getDebitBal() == null || c.getDebitBal().isEmpty()) {
                    dTotal = dTotal + 0;
                } else
                    dTotal = dTotal + Integer.parseInt(c.getDebitBal());
                if (c.getCreditBal() == null || c.getCreditBal().isEmpty()) {
                    cTotal = cTotal + 0;
                } else
                    cTotal = cTotal + Integer.parseInt(c.getCreditBal());
                ////////////////////////////////////
//                dTotal = dTotal + Integer.valueOf(c.getDebitBal());
//                cTotal = cTotal + Integer.valueOf(c.getCreditBal());
            }
        } else {
            for (Reports c : reportsList) {
                table.addCell(getCell(c.getAcName(), PdfPCell.ALIGN_LEFT));
                table.addCell(getCell(c.getDebitBal(), PdfPCell.ALIGN_RIGHT));
                table.addCell(getCell(c.getCreditBal(), PdfPCell.ALIGN_RIGHT));
                ///////////////////////////////My Aditeion
                if (c.getDebitBal() == null || c.getDebitBal().isEmpty()) {
                    dTotal = dTotal + 0;
                } else
                    dTotal = dTotal + Integer.parseInt(c.getDebitBal());
                if (c.getCreditBal() == null || c.getCreditBal().isEmpty()) {
                    cTotal = cTotal + 0;
                } else
                    cTotal = cTotal + Integer.parseInt(c.getCreditBal());
                ////////////////////////////////////
//                dTotal = dTotal + Integer.valueOf(c.getDebitBal());
//                cTotal = cTotal + Integer.valueOf(c.getCreditBal());
            }
        }

        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD);
        PdfPCell total = new PdfPCell(new Phrase("Total", totalFont));
        total.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        total.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(total);
        table.addCell(getCell(String.valueOf(dTotal), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(cTotal), PdfPCell.ALIGN_RIGHT));

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
        Log.e("PDFDocument", "Created");
    }

    public PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(alignment);
        cell.setFixedHeight(40);
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

    public void customPDFView() {
       try{
           PackageManager packageManager = getContext().getPackageManager();

           Intent testIntent = new Intent(Intent.ACTION_VIEW);
           testIntent.setType("application/pdf");
           List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
           if (list.size() > 0) {
               Intent intent = new Intent();
               intent.setAction(Intent.ACTION_VIEW);

               Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider",pdfFile.getAbsoluteFile());


               // Uri uri = Uri.fromFile(pdfFile);
               intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
               intent.setDataAndType(uri, "application/pdf");
               getContext().startActivity(intent);

           } else {
               Toast.makeText(getContext(), "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
           }
       }catch (Exception e){
           MNotificationClass.ShowToastTem(getContext(),"Error While Openin File Check File in "+pdfFile.getAbsolutePath());

           e.printStackTrace();

       }
    }

    @Override
    public void getPoupCallBackInfo(String AcNameID, String dbID, String menuItemName) {
        MNotificationClass.ShowToastTem(getContext(), "AcName:" + AcNameID + " dbID:" + dbID + " item :" + menuItemName);
        String groupName = ((Account2Group) sp_acgroup.getSelectedItem()).getAcGruopName();
        String groupID = ((Account2Group) sp_acgroup.getSelectedItem()).getAcGroupID();
        if (groupName.equals("Employee") || groupID.equals("2") && menuItemName.equals(DialogForEmployee.DIALOG_EDIT_TEXT_TITLE)) {
            DialogForEmployee emp = new DialogForEmployee();

            Bundle bb = new Bundle();
            bb.putString("head", groupName);
            bb.putString("groupName", groupName);
            bb.putString("groupID", groupID);
            bb.putString("type", menuItemName);
            bb.putString("dbpkid", dbID);
            bb.putString("acnameid", AcNameID);
            emp.setArguments(bb);
            emp.show(getFragmentManager(), "Show");
        } else if (groupName.equals("Client") || groupName.equals("Suppliers") ||
                groupID.equals("5") || groupID.equals("6")) {
            DialogForCliSup emp = new DialogForCliSup();

            Bundle bb = new Bundle();
            bb.putString("head", groupName);
            bb.putString("groupName", groupName);
            bb.putString("groupID", groupID);
            bb.putString("type", menuItemName);
            bb.putString("dbpkid", dbID);
            bb.putString("acnameid", AcNameID);
            emp.setArguments(bb);
            emp.show(getFragmentManager(), "Show");

        } else if (groupName.equals("Cash And Bank") || groupName.equals("General Expense") || groupName.equals("Capital")
                || groupName.equals("Fixed Assets") || groupName.equals("Incom") || groupID.equals("1") || groupID.equals("3")
                || groupID.equals("8") || groupID.equals("4") || groupID.equals("7")) {
            DialogForAccountingTypeItem emp = new DialogForAccountingTypeItem();


            Bundle bb = new Bundle();

            bb.putString("head", groupName);
            bb.putString("groupName", groupName);
            bb.putString("groupID", groupID);
            bb.putString("type", menuItemName);
            bb.putString("dbpkid", dbID);
            bb.putString("acnameid", AcNameID);
            emp.setArguments(bb);
            FragmentManager manager = getFragmentManager();
            emp.show(manager, "Show");


        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        getReports();
        filter = 0;
        searchView.setQuery("", false);
        searchView.clearFocus();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
            Log.e("PAGE NUMBER", String.valueOf(writer.getPageNumber()));
            table.addCell(footerCell(String.format("Page %d ", writer.getPageNumber() - 1), PdfPCell.ALIGN_LEFT));
            table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
            table.addCell(footerCell("www.easysoft.com.pk", PdfPCell.ALIGN_LEFT));
            table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));

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
    String cashID, incomeID, expenseID;

    public void refereshTables(Context context) {

        cashID = databaseHelper.getID("SELECT AcNameID FROM Account3Name WHERE ClientID = " + prefrence.getClientIDSession() + " and AcName = 'Cash'");
        incomeID = databaseHelper.getID("SELECT AcNameID FROM Account3Name WHERE ClientID = " + prefrence.getClientIDSession() + " and AcName = 'Booking Income'");
        expenseID = databaseHelper.getID("SELECT AcNameID FROM Account3Name WHERE ClientID = " + prefrence.getClientIDSession() + " and AcName = 'Booking Expense'");

        databaseHelper = new DatabaseHelper(context);
        mProgress = new ProgressDialog(context);
        mProgress.setMessage("Loading...");
        mProgress.setCancelable(false);
        mProgress.show();

        Account3NameApis account3NameApis=new Account3NameApis(getContext(),
                mProgress,databaseHelper,prefrence);
        account3NameApis.trigerAllMethodInSequecne();
        account3NameApis.FinalCallBAckListner=new Account3NameApis.Acunt3namlistner() {
            @Override
            public void FinishCallBackmethod(String success, String funType) {
                Log.e("Account3Namefunc","Flag4 Suces:"+success+" funType:"+funType);
                MNotificationClass.ShowToastTem(getContext(),"All Method Finished");
            }
        };
       // updateAccount3Name();
       // addAccount3Name();
    }

    ////////////////////Account3Name all 4 operations
    ///////////////////////////////////////////Methos Calling Sequences
    ////////////////Get Edited Record From Server
//    public void updateAccount3Name() {
//        Log.e("method", "Flag 1 MathodName::updateAccount3Name");
//
//        mProgress.show();
//        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
//            mProgress.setMessage("Loading... getUpdatedData updateAccount3Name Method:updateAccount3Name CLI " + prefrence.getClientIDSession());
//
//        String tag_json_obj = "json_obj_req";
//        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshAccount3Name.php";
//
//        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        mProgress.dismiss();
//                        JSONObject jsonObj = null;
//
//                        try {
//                            jsonObj = new JSONObject(response);
//                            String success = jsonObj.getString("success");
//                            Log.e("Account3Name2", jsonObj.toString());
//                            if (success.equals("1")) {
//                                JSONArray jsonArray = jsonObj.getJSONArray("Account3Name");
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                    Log.e("Account3Name", jsonObject.toString());
//                                    String AcNameID = jsonObject.getString("AcNameID");
//                                    String AcName = jsonObject.getString("AcName");
//                                    String AcGroupID = jsonObject.getString("AcGroupID");
//                                    String AcAddress = jsonObject.getString("AcAddress");
//                                    String AcMobileNo = jsonObject.getString("AcMobileNo");
//                                    String AcContactNo = jsonObject.getString("AcContactNo");
//                                    String AcEmailAddress = jsonObject.getString("AcEmailAddress");
//                                    String AcDebitBal = jsonObject.getString("AcDebitBal");
//                                    String AcCreditBal = jsonObject.getString("AcCreditBal");
//                                    String AcPassward = jsonObject.getString("AcPassward");
//                                    String ClientID = jsonObject.getString("ClientID");
//                                    String ClientUserID = jsonObject.getString("ClientUserID");
//                                    String SysCode = jsonObject.getString("SysCode");
//                                    String NetCode = jsonObject.getString("NetCode");
//                                    String ed = jsonObject.getString("UpdatedDate");
//                                    JSONObject jbb = new JSONObject(ed);
//                                    String UpdatedDate = jbb.getString("date");
//                                    String SerialNo = jsonObject.getString("SerialNo");
//                                    String UserRights = jsonObject.getString("UserRights");
//                                    String SecurityRights = jsonObject.getString("SecurityRights");
//                                    String Salary = jsonObject.getString("Salary");
//                                    String SessionDate = jsonObject.getString("SessionDate");
//
//                                    String query = "UPDATE Account3Name SET AcNameID = '" + AcNameID + "', AcName = '" + AcName + "', AcGroupID = '" + AcGroupID + "', AcAddress = '" + AcAddress + "', AcMobileNo = '" + AcMobileNo
//                                            + "', AcContactNo = '" + AcContactNo + "', AcEmailAddress = '" + AcEmailAddress + "', AcDebitBal = '" + AcDebitBal + "', AcCreditBal = '" + AcCreditBal + "', AcPassward = '" + AcPassward
//                                            + "', ClientID = '" + ClientID + "', ClientUserID = '" + ClientUserID + "', SysCode = '" + SysCode + "', NetCode = '" + NetCode + "', UpdatedDate = '" + UpdatedDate + "', SerialNo = '" + SerialNo
//                                            + "', UserRights = '" + UserRights + "', SecurityRights = '" + SecurityRights + "', Salary ='" + Salary + "' WHERE AcNameID = " + AcNameID;
//                                    databaseHelper.updateAccount3Name(query);
//                                    updatedDate = SessionDate;
//
//                                }
//                                Log.e("key", "All Fields Up To dated");
//                                getReports();
//                            } else {
//                                String message = jsonObj.getString("message");
//                                MNotificationClass.ShowToastTem(getContext(), message);
//                            }
//
//                            Log.e("Sarem", "CashBook1");
//                            //Previously Added Function
//                            // getCashBook1();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            GenericConstants.ShowDebugModeDialog(getContext(),
//                                    "Error", e.getMessage());
//                        }
//
//                        ////Calling Next Method
//                        getAccount3Name();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                ////Calling Next Method
//                getAccount3Name();
//                mProgress.dismiss();
//                GenericConstants.ShowDebugModeDialog(getContext(),
//                        "Error", error.getMessage());
//                Log.e("Error", error.toString());
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//
//
//                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(AcNameID AS Int)) FROM Account3Name");
//
//                //  String date = databaseHelper.getClientUpdatedDate(prefrence.getClientIDSession());
//                String date = databaseHelper.getAccount3NameMaxUpdatedDate(prefrence.getClientIDSession());
//
//                ///////////////////must send date in single quotes
//                ///////////////////////////trim last 3 zero of the date other wise server not accept it
//                date = date.trim();
//                date = date.substring(0, date.length() - 3);
//                params.put("ClientID", prefrence.getClientIDSession());
//                params.put("MaxID", String.valueOf(maxID));
//                params.put("SessionDate", "'" + date + "'");
//                Log.e("key", "Look For Updata Server CLIID:" + prefrence.getClientUserIDSession() + " MaxID:" + maxID + " Serssion Date " + date);
//                return params;
//            }
//        };
//        int socketTimeout = 10000;//10 seconds
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        jsonObjectRequest.setRetryPolicy(policy);
//        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
//        ///////CAlling Next Method
//
//    }
//
//    /////////////////////Record That Are New on The server
//    public void getAccount3Name() {
//        Log.e("method", "Flag 2 MathodName::getAccount3Name");
//
//        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
//            mProgress.setMessage("Loading... getNew InsertDataFromServer Method:getAccount3Name CLI " + prefrence.getClientIDSession());
//        mProgress.show();
//        String tag_json_obj = "json_obj_req";
//        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshAccount3Name.php";
//
//        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        mProgress.dismiss();
//                        JSONObject jsonObj = null;
//
//                        try {
//                            jsonObj = new JSONObject(response);
//                            String success = jsonObj.getString("success");
//                            Log.e("Account3Name1", jsonObj.toString());
//                            if (success.equals("1")) {
//                                JSONArray jsonArray = jsonObj.getJSONArray("Account3Name");
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                    Log.e("Account3Name", jsonObject.toString());
//                                    String AcNameID = jsonObject.getString("AcNameID");
//                                    String AcName = jsonObject.getString("AcName");
//                                    String AcGroupID = jsonObject.getString("AcGroupID");
//                                    String AcAddress = jsonObject.getString("AcAddress");
//                                    String AcMobileNo = jsonObject.getString("AcMobileNo");
//                                    String AcContactNo = jsonObject.getString("AcContactNo");
//                                    String AcEmailAddress = jsonObject.getString("AcEmailAddress");
//                                    String AcDebitBal = jsonObject.getString("AcDebitBal");
//                                    String AcCreditBal = jsonObject.getString("AcCreditBal");
//                                    String AcPassward = jsonObject.getString("AcPassward");
//                                    String ClientID = jsonObject.getString("ClientID");
//                                    String ClientUserID = jsonObject.getString("ClientUserID");
//                                    String SysCode = jsonObject.getString("SysCode");
//                                    String NetCode = jsonObject.getString("NetCode");
//                                    String ed = jsonObject.getString("UpdatedDate");
//                                    JSONObject jbb = new JSONObject(ed);
//                                    String UpdatedDate = jbb.getString("date");
//                                    String SerialNo = jsonObject.getString("SerialNo");
//                                    String UserRights = jsonObject.getString("UserRights");
//                                    String SecurityRights = jsonObject.getString("SecurityRights");
//                                    String Salary = jsonObject.getString("Salary");
//                                    String SessionDate = jsonObject.getString("SessionDate");
//
//                                    databaseHelper.createAccount3Name(new Account3Name(AcNameID, AcName, AcGroupID, AcAddress, AcMobileNo, AcContactNo, AcEmailAddress, AcDebitBal, AcCreditBal, AcPassward, ClientID, ClientUserID, SysCode, NetCode, UpdatedDate, SerialNo, UserRights, SecurityRights, Salary));
//                                    updatedDate = SessionDate;
/////////////////////////////////////////Calling This method to update the list of Account3Name data
//                                    getReports();
////                                    if (i == jsonArray.length() - 1) {
////                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Account3Name");
////                                        for (TableSession s : se){
////                                            s.setMaxID(AcNameID);
////                                            s.setInsertDate(SessionDate);
////                                            s.save();
////                                        }
////
////                                    }
//
//                                }
//                                if (GenericConstants.IS_DEBUG_MODE_ENABLED)
//                                    MNotificationClass.ShowToast(getContext(), "No of new inserted record " + jsonArray.length());
//
//                            } else {
//                                String message = jsonObj.getString("message");
//                                MNotificationClass.ShowToastTem(getContext(), message);
//                            }
//
//                        } catch (JSONException e) {
//                            GenericConstants.ShowDebugModeDialog(getContext(),
//                                    "Error", e.getMessage());
//                            e.printStackTrace();
//                        }
//                        //////////Calling Next Method
//                        updateAccount3Name1();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //////////Calling Next Method
//                updateAccount3Name1();
//                mProgress.dismiss();
//                Log.e("Error", error.toString());
//                GenericConstants.ShowDebugModeDialog(getContext(),
//                        "Error", error.getMessage());
////                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("ClientID", prefrence.getClientIDSession());
//                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(AcNameID AS Int)) FROM Account3Name");
//                params.put("MaxID", String.valueOf(maxID));
//                Log.e("key", "MaxID" + maxID);
//                return params;
//            }
//        };
//        int socketTimeout = 10000;//10 seconds
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        jsonObjectRequest.setRetryPolicy(policy);
//        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
//
//    }
//
//    ////////////////////Record That are Edited on Sqlite
//    public void updateAccount3Name1() {
//        Log.e("method", "Flag 3 MathodName::updateAccount3Name1");
//        mProgress.show();
//        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
//            mProgress.setMessage("Loading... updateAccount3Name1 UpDateRecordFromSqliteToCloud Method:updateAccount3Name1 CLI " + prefrence.getClientIDSession());
//
//        String query = "SELECT * FROM Account3Name  WHERE AcNameID > 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
//        final List<Account3Name> addBooking = databaseHelper.getAccount3Name(query);
//        Log.e("BookingID UP", String.valueOf(addBooking.size()));
//
//        for (final Account3Name c : addBooking) {
//            Log.e("BookingID UP", "DAta" + c.toString());
//            String tag_json_obj = "json_obj_req";
//            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/UpdateCharofAcc.php";
//
//            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            mProgress.dismiss();
//                            Log.e("Account3Name4", response);
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                String success = jsonObject.getString("success");
//                                Log.e("Success CB", success);
//                                if (success.equals("1")) {
//                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
//                                    String message = jsonObject.getString("message");
//                                    databaseHelper.updateCashBook("UPDATE Account3Name SET UpdatedDate = '" + UpdatedDate + "' WHERE ID = " + c.getId());
//                                    updatedDate = UpdatedDate;
//                                    getReports();
////                                    List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Account3Name");
////                                    for (TableSession s : se){
////                                        s.setUpdateDate(UpdatedDate);
////                                        s.save();
////                                    }
//                                    databaseHelper.updateClient("UPDATE Client SET UpdatedDate = '" + updatedDate + "' WHERE ClientID = " + prefrence.getClientIDSession());
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    //////////////////////////////////Calling Next Methos
//                    addAccount3Name();
//                    mProgress.dismiss();
//                    Log.e("Error", error.toString());
////                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//
//                    Map<String, String> params = new HashMap<String, String>();
//
//                    params.put("AcNameID", c.getAcNameID());
//                    params.put("AcName", c.getAcName());
//                    params.put("AcAddress", c.getAcAddress());
//                    params.put("AcContactNo", c.getAcContactNo());
//                    params.put("AcEmailAddress", c.getAcEmailAddress());
//                    params.put("Salary", c.getSalary());
//                    params.put("AcMobileNo", c.getAcMobileNo());
//                    params.put("AcPassward", c.getAcPassward());
//                    params.put("SecurityRights", c.getSecurityRights());
//                    params.put("ClientID", prefrence.getClientIDSession());
//                    params.put("AcGroupID", c.getAcGroupID());
//
//                    return params;
//                }
//            };
//            int socketTimeout = 30000;//30 seconds - change to what you want
//            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//            jsonObjectRequest.setRetryPolicy(policy);
//            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
//        }
//        //////////////////////////////////Calling Next Methos
//        addAccount3Name();
//        getReports();
//        if (addBooking.size() == 0) {
//            databaseHelper.updateClient("UPDATE Client SET UpdatedDate = '" + updatedDate + "' WHERE ClientID = " + prefrence.getClientIDSession());
//        }
//
//    }
//
//    ////////////////Record That are added in sqlite and not Uploaded on server
//    public void addAccount3Name() {
//        Log.e("method", "Flag 4 MathodName::addAccount3Name");
//
//        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
//            mProgress.setMessage("Loading... addFromSqliteToCloud Method:addAccount3Name CLI " + prefrence.getClientIDSession());
//
//        String query = "SELECT * FROM Account3Name WHERE  AcNameID < 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
//        final List<Account3Name> addBooking = databaseHelper.getAccount3Name(query);
//        Log.e("BookingID UP", String.valueOf(addBooking.size()));
//
//        for (final Account3Name c : addBooking) {
//            mProgress.show();
//            String tag_json_obj = "json_obj_req";
//            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/BilalApi/AddCharofAcc.php";
//
//            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            mProgress.dismiss();
//                            Log.e("Account3Name", response);
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                String success = jsonObject.getString("success");
//                                Log.e("Success CB", success);
//                                if (success.equals("1")) {
//                                    String id = jsonObject.getString("AcNameID");
//                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
//                                    String message = jsonObject.getString("message");
//                                    databaseHelper.updateCashBook("UPDATE Account3Name SET AcNameID = '" + id + "', UpdatedDate = '" + UpdatedDate + "' WHERE ID = " + c.getId());
//                                    updatedDate = UpdatedDate;
//
//                                   String iddst= refdb.Account3NameTableFun.UpdateAcNameIDInCashBook(databaseHelper,
//                                            c.getClientID(),
//                                            c.getAcNameID(),
//                                            id);
////                                    String iddst= refdb.Account3NameTableFun.UpdateAcNameIDInCashBook(
////                                            databaseHelper,
////                                            "115",
////                                            "-2525",
////                                            "2525");
//                                    Log.e("oberve",c.toString()+"");
//                                   Log.e("oberve",iddst+"");
//                                    if (GenericConstants.IS_DEBUG_MODE_ENABLED)
//                                        Toast.makeText(getContext(), id + " Recourd Updated", Toast.LENGTH_SHORT).show();
//                                    getReports();
////                                    List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Account3Name");
////                                    for (TableSession s : se){
////                                        s.setMaxID(id);
////                                        s.setInsertDate(UpdatedDate);
////                                        s.save();
////                                    }
//                                } else {
//                                    databaseHelper.deleteAccount3NameEntry("DELETE FROM Account3Name WHERE ID = " + c.getId());
//                                    String message = jsonObject.getString("message");
//                                    if (GenericConstants.IS_DEBUG_MODE_ENABLED)
//                                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                MNotificationClass.ShowToastTem(getContext(), e.getMessage() + " -" + response);
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    mProgress.dismiss();
//                    GenericConstants.ShowDebugModeDialog(getContext(),
//                            "Error", error.getMessage());
////                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//
//                    Map<String, String> params = new HashMap<String, String>();
//                    ///Parameter Sequences
////                    isset($_POST['AcName']) && --
////                    isset($_POST['AcAddress']) ---
////                            && isset($_POST['AcContactNo']) && ---
////                            isset($_POST['AcEmailAddress']) ---
////                            && isset($_POST['Salary'])---
////                            && isset($_POST['AcMobileNo']) && ---
////                            isset($_POST['AcPassward']) ---
////                            && isset($_POST['SecurityRights']) && ----
////                            isset($_POST['ClientID']) -----
////                            && isset($_POST['AcGroupID']) && ---
////                            isset($_POST['SerialNo'])  ----
////                            &&  isset($_POST['AcDebitBal']) && ---
////                            isset($_POST[' AcCreditBal']) ----
////                            && isset($_POST['ClientUserID']) && ---
////                            isset($_POST['SysCode']) ---
////                            && isset($_POST['NetCode']) &&----
////                            isset($_POST['UpdatedDate']) ----
////                            && isset($_POST['UserRights']) && ---
////                            isset($_POST['AccountPhoto']---
//                    Log.e("key", "Set Pram ::" + c.toString());
//                    params.put("AcName", c.getAcName());
//                    params.put("AcGroupID", c.getAcGroupID());
//                    params.put("AcAddress", c.getAcAddress());
//                    params.put("AcContactNo", c.getAcContactNo());
//                    params.put("AcEmailAddress", c.getAcEmailAddress());
//                    params.put("Salary", c.getSalary());
//                    params.put("AcMobileNo", c.getAcMobileNo());
//                    params.put("AcPassward", c.getAcPassward());
//                    params.put("SecurityRights", c.getSecurityRights());
//                    params.put("ClientID", prefrence.getClientIDSession());
//                    /////////////////////////////////
//                    params.put("UpdatedDate", c.getUpdatedDate());
//                    //params.put("AcNameID", c.getAcNameID());
//                    params.put("SerialNo", c.getSerialNo());
//                    params.put("AcDebitBal", c.getAcDebitBal());
//                    params.put("AcCreditBal", c.getAcCreditBal());
//                    params.put("ClientUserID", c.getClientUserID());
//                    params.put("SysCode", c.getSysCode());
//                    params.put("NetCode", c.getNetCode());
//                    params.put("UserRights", c.getUserRights());
//                    params.put("AccountPhoto", "Not Set Yet ");
//
//                    return params;
//                }
//            };
//
//            int socketTimeout = 30000;//30 seconds - change to what you want
//            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//            jsonObjectRequest.setRetryPolicy(policy);
//            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
//        }
//        mProgress.dismiss();
//        MNotificationClass.ShowToast(getContext(), "All Done");
//        if (addBooking.size() < 0)
//            GenericConstants.ShowDebugModeDialog(getContext(), "Error", "Updated Data Not Found ");
//    }
//////////////////////////////////////////////////////////END Account3Name all 4 operations

    public void getCashBook1() {

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshCashBook.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            String success = jsonObj.getString("success");
                            Log.e("CashBook1", jsonObj.toString());
                            if (success.equals("1")) {
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

                                    databaseHelper.createCashBook(new CashBook(CashBookID, CBDate1, DebitAccount, CreditAccount, CBRemark, Amount, ClientID, ClientUserID, NetCode, SysCode, UpdatedDate, TableID, SerialNo, TableName));
                                    updatedDate = SessionDate;
//                                    if (i == jsonArray.length() - 1) {
//                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","CashBook");
//                                        for (TableSession s : se){
//                                            s.setMaxID(CashBookID);
//                                            s.setInsertDate(SessionDate);
//                                            s.save();
//                                        }
//                                    }

                                }

                            } else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            Log.e("Sarem", "CashBook2");
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
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ClientID", prefrence.getClientIDSession());
                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(CashBookID AS Int)) FROM CashBook");
                Log.e("MAXIDCASH", String.valueOf(maxID));
                params.put("MaxID", String.valueOf(maxID));

                Log.e("MAXID", String.valueOf(maxID));

                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void updateCashBook() {

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshCashBook.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            Log.e("CashBook2", response);
                            String success = jsonObj.getString("success");
                            if (success.equals("1")) {
                                JSONArray jsonArray = jsonObj.getJSONArray("CashBook");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Account3Name", jsonObject.toString());
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

                                    String query = "UPDATE CashBook SET CBDate = '" + CBDate1 + "', DebitAccount = '" + DebitAccount + "', CreditAccount = '" + CreditAccount + "', CBRemarks = '" + CBRemark + "', Amount = '" + Amount + "', ClientID = '" + ClientID + "', ClientUserID = '" + ClientUserID + "', NetCode = '" + NetCode + "', SysCode = '" + SysCode + "', UpdatedDate = '" + UpdatedDate + "', TableID = '" + TableID +
                                            "' WHERE CashBookID = " + CashBookID;
                                    databaseHelper.updateCashBook(query);
                                    updatedDate = SessionDate;


                                }

                            } else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            Log.e("Sarem", "CashBook3");
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
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ClientID", prefrence.getClientIDSession());
                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(CashBookID AS Int)) FROM CashBook");
                Log.e("MAXIDCASH", String.valueOf(maxID));
                params.put("MaxID", String.valueOf(maxID));
                String date = databaseHelper.getClientUpdatedDate(prefrence.getClientIDSession());
                params.put("SessionDate", date);

                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getBookings1() {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshBooking.php";


        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Booking1", response);
                        String text = "", BookingID = "", ClientName = "", ClientMobile = "", ClientAddress = "", ClientNic = "", EventName = "", BookingDate = "", EventDate = "",
                                ArrangePersons = "", ChargesTotal = "", Description = "", ClientID = "", ClientUserID = "", NetCode = "", SysCode = "", UpdatedDate = "";
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
                                    Log.e("TEST", EventDate);
                                    ArrangePersons = jsonObject.getString("ArrangePersons");
                                    ChargesTotal = jsonObject.getString("ChargesTotal");
                                    Description = jsonObject.getString("Description");
                                    ClientID = jsonObject.getString("ClientID");
                                    ClientUserID = jsonObject.getString("ClientUserID");
                                    NetCode = jsonObject.getString("NetCode");
                                    SysCode = jsonObject.getString("SysCode");
                                    Log.e("TEST", "TEST");
                                    String up = jsonObject.getString("UpdatedDate");
                                    JSONObject jbb = new JSONObject(up);
                                    UpdatedDate = jbb.getString("date");
                                    String SessionDate = jsonObject.getString("SessionDate");
                                    String Shift = jsonObject.getString("Shift");
                                    String SerialNo = jsonObject.getString("SerialNo");

                                    databaseHelper.createBooking(new Bookings(BookingID, ClientName, ClientMobile, ClientAddress, ClientNic, EventName, BookingDate, EventDate, ArrangePersons, ChargesTotal, Description, ClientID, ClientUserID, NetCode, SysCode, UpdatedDate, Shift, SerialNo));
                                    updatedDate = SessionDate;
                                    getReports();
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
                            } else {
//                                pDialog.dismiss();
                            }
                            Log.e("Sarem", "CashBook4");
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ClientID", prefrence.getClientIDSession());
                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(BookingID AS Int)) FROM Booking");
                params.put("MaxID", String.valueOf(maxID));

                return params;
            }
        };

        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void updateBookings() {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshBooking.php";


        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String text = "", BookingID = "", ClientName = "", ClientMobile = "", ClientAddress = "", ClientNic = "", EventName = "", BookingDate = "", EventDate = "",
                                ArrangePersons = "", ChargesTotal = "", Description = "", ClientID = "", ClientUserID = "", NetCode = "", SysCode = "", UpdatedDate = "";
                        try {
                            JSONObject json = new JSONObject(response);
                            String success = json.getString("success");
                            Log.e("Booking2", json.toString());

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
                                    Log.e("TEST", EventDate);
                                    ArrangePersons = jsonObject.getString("ArrangePersons");
                                    ChargesTotal = jsonObject.getString("ChargesTotal");
                                    Description = jsonObject.getString("Description");
                                    ClientID = jsonObject.getString("ClientID");
                                    ClientUserID = jsonObject.getString("ClientUserID");
                                    NetCode = jsonObject.getString("NetCode");
                                    SysCode = jsonObject.getString("SysCode");
                                    Log.e("TEST", "TEST");
                                    String up = jsonObject.getString("UpdatedDate");
                                    JSONObject jbb = new JSONObject(up);
                                    UpdatedDate = jbb.getString("date");
                                    String SessionDate = jsonObject.getString("SessionDate");

                                    String query = "UPDATE Booking SET BookingID = '" + BookingID + "', ClientName = '" + ClientName + "', ClientMobile = '" + ClientMobile + "', ClientAddress = '" + ClientAddress + "', ClientNic = '" + ClientNic
                                            + "', EventName = '" + EventName + "', BookingDate = '" + BookingDate + "', EventDate = '" + EventDate + "', ArrangePersons ='" + ArrangePersons + "', ChargesTotal = '" + ChargesTotal + "', Description = '" + Description
                                            + "', ClientID = '" + ClientID + "', ClientUserID = '" + ClientUserID + "', NetCode = '" + NetCode + "', SysCode = '" + SysCode + "', UpdatedDate = '" + UpdatedDate + "' WHERE BookingID = " + BookingID;
                                    databaseHelper.updateBooking(query);
                                    updatedDate = SessionDate;
                                    getReports();
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
                            } else {
//                                mProgress.dismiss();
                            }
                            Log.e("Sarem", "CashBook5");
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ClientID", prefrence.getClientIDSession());
                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(BookingID AS Int)) FROM Booking");
                params.put("MaxID", String.valueOf(maxID));
                String date = databaseHelper.getClientUpdatedDate(prefrence.getClientIDSession());
                params.put("SessionDate", date);

                return params;
            }
        };

        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void addCashBook() {
        String query = "SELECT * FROM CashBook WHERE CashBookID = 0 AND UpdatedDate = 0";
        List<CashBook> addCashBook = databaseHelper.getCashBook(query);

        for (final CashBook c : addCashBook) {
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/AddCashBook.php";

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("CashBook3", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equals("1")) {
                                    String id = jsonObject.getString("CBID");
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE CashBook SET CashBookID = '" + id + "', UpdatedDate = '" + UpdatedDate + "' WHERE ID = " + c.getcId());
                                    updatedDate = UpdatedDate;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
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
        }
        Log.e("Sarem", "CashBook5");
        addCashBook2();
    }

    public void addCashBook2() {
        String query = "SELECT * FROM CashBook WHERE UpdatedDate = 0";
        List<CashBook> addCashBook = databaseHelper.getCashBook(query);
        Log.e("CASHBOOK UP", String.valueOf(addCashBook.size()));

        for (final CashBook c : addCashBook) {
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/UpdateCashBook.php";

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("CashBook4", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("Success CB", success);
                                if (success.equals("1")) {
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE CashBook SET UpdatedDate = '" + UpdatedDate + "' WHERE ID = " + c.getcId());

                                    updatedDate = UpdatedDate;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgress.dismiss();
                    Log.e("Error", error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
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
        Log.e("Sarem", "CashBook6");
        addBooking();
        //For Refresh Recycler
//        mProgress.dismiss();
    }

    public void addBooking() {
        String query = "SELECT * FROM Booking WHERE BookingID = 'o' AND UpdatedDate = 0";
        final List<Bookings> addBooking = databaseHelper.getBookings(query);
        Log.e("BookingID UP", String.valueOf(addBooking.size()));

        for (final Bookings c : addBooking) {
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/AddEvent.php";

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Booking3", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("Success Boo", success);
                                if (success.equals("1")) {
                                    String id = jsonObject.getString("BookingID");
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE Booking SET BookingID = '" + id + "', UpdatedDate = '" + UpdatedDate + "' WHERE ID = " + c.getId());
                                    updatedDate = UpdatedDate;
                                    getReports();
//                                    List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Bookings");
//                                    for (TableSession s : se){
//                                        s.setMaxID(id);
//                                        s.setInsertDate(UpdatedDate);
//                                        s.save();
//                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e("Sarem", "CashBook7");
//                            addAccount3Name();
                            mProgress.dismiss();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgress.dismiss();
                    Log.e("Error", error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
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

    public void updateBooking() {
        String query = "SELECT * FROM Booking WHERE UpdatedDate = 0 AND BookingID != 'o'";
        final List<Bookings> addBooking = databaseHelper.getBookings(query);
        Log.e("BookingID UP", String.valueOf(addBooking.size()));

        for (final Bookings c : addBooking) {
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/UpdateEvent.php";

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Booking4", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("Success CB", success);
                                if (success.equals("1")) {
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE Booking SET UpdatedDate = '" + UpdatedDate + "' WHERE ID = " + c.getId());
                                    updatedDate = UpdatedDate;
                                    getReports();
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
                    Log.e("Error", error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
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
       // addAccount3Name();
    }


//    View.OnLongClickListener longClickListenerForaddbtn = new View.OnLongClickListener() {
//        @Override
//        public boolean onLongClick(View v) {
//            showMyDialogForForm();
//            return true;
//        }
//    };

    public void showMyDialogForForm() {
        MNotificationClass.ShowToastTem(getContext(), "ClickLong");

        String groupName = ((Account2Group) sp_acgroup.getSelectedItem()).getAcGruopName();
        String groupID = ((Account2Group) sp_acgroup.getSelectedItem()).getAcGroupID();
        if (groupName.equals("Client") || groupName.equals("Suppliers") || groupID.equals("5") || groupID.equals("6")) {
            ////Shwo Dialog For Client-Supplier
//            name_layout.setVisibility(View.VISIBLE);
//            address_layout.setVisibility(View.VISIBLE);
//            mobile_layout.setVisibility(View.VISIBLE);
//            email_layout.setVisibility(View.VISIBLE);
//            salary_layout.setVisibility(View.GONE);
//            login_mobile_layout.setVisibility(View.VISIBLE);
//            password_layout.setVisibility(View.VISIBLE);
            DialogForCliSup cliSup = new DialogForCliSup();
            Bundle bb = new Bundle();
            bb.putString("head", groupName);
            bb.putString("groupID", groupID);
            bb.putString("groupName", groupName);
            bb.putString("type", "Add");
            cliSup.setArguments(bb);
            cliSup.show(getFragmentManager(), "Show");
        } else if (groupName.equals("Cash And Bank") || groupName.equals("General Expense") || groupName.equals("Capital")
                || groupName.equals("Fixed Assets") || groupName.equals("Incom") || groupID.equals("1") || groupID.equals("3")
                || groupID.equals("8") || groupID.equals("4") || groupID.equals("7")) {
            DialogForAccountingTypeItem item = new DialogForAccountingTypeItem();

            Bundle bb = new Bundle();
            bb.putString("head", groupName);
            bb.putString("groupName", groupName);
            bb.putString("groupID", groupID);
            bb.putString("type", "Add");
            item.setArguments(bb);
            item.show(getFragmentManager(), "Show");
//            isSingleTextView=true;
//            name_layout.setVisibility(View.VISIBLE);
//            address_layout.setVisibility(View.GONE);
//            mobile_layout.setVisibility(View.GONE);
//            email_layout.setVisibility(View.GONE);
//            salary_layout.setVisibility(View.GONE);
//            loginInfo.setVisibility(View.GONE);
//            login_mobile_layout.setVisibility(View.GONE);
//            password_layout.setVisibility(View.GONE);
//            spinner.setVisibility(View.GONE);
//            spinner.setSelection(0);
//            spPosition = 0;
        } else if (groupName.equals("Employee") || groupID.equals("2")) {
            DialogForEmployee emp = new DialogForEmployee();
            Bundle bb = new Bundle();
            bb.putString("head", groupName);
            bb.putString("groupName", groupName);
            bb.putString("groupID", groupID);
            bb.putString("type", "Add");
            emp.setArguments(bb);
            emp.show(getFragmentManager(), "Show");
//            name_layout.setVisibility(View.VISIBLE);
//            address_layout.setVisibility(View.VISIBLE);
//            mobile_layout.setVisibility(View.VISIBLE);
//            email_layout.setVisibility(View.VISIBLE);
//            salary_layout.setVisibility(View.VISIBLE);
//            login_mobile_layout.setVisibility(View.VISIBLE);
//            password_layout.setVisibility(View.VISIBLE);
        }
    }

}
