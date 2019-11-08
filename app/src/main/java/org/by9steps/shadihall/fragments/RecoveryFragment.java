package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import org.by9steps.shadihall.adapters.RecoveryAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.CashEntry;
import org.by9steps.shadihall.model.Recovery;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
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
public class RecoveryFragment extends Fragment implements View.OnClickListener {

    ProgressDialog mProgress;
    List<Recovery> mList;
    int m = 0, recieved, expense, chargesTotal, balance, profit;
    int gRecieved, gExpense, gChargesTotal, gBalance, gProfit;

    String orderBy = "EventDate";
    int status = 0;
    String orderby = " ORDER BY " + orderBy + " DESC";

    TextView r_eventname, r_clientname, r_eventdate, r_totalcharges, r_received, r_expensed, r_balance, r_profit;

    RecyclerView recyclerView;

    DatabaseHelper databaseHelper;
    List<Recovery> recoveries;
    List<Recovery> filterdList;

    Prefrence prefrence;

    RecoveryAdapter adapter;
    //    EditText search;
    SearchView searchView;
    Spinner spinner;
    int filter;

    //Print
    private static final String TAG = "PdfCreatorActivity";
    private File pdfFile;
    String d = "0";

    public RecoveryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recovery, container, false);

        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.recycler);
//        search = view.findViewById(R.id.recovery_search);
        spinner = view.findViewById(R.id.recovery_spinner);
        searchView = view.findViewById(R.id.recovery_search);


        r_eventname = view.findViewById(R.id.r_eventname);
        r_clientname = view.findViewById(R.id.r_clientname);
        r_eventdate = view.findViewById(R.id.r_eventdate);
        r_totalcharges = view.findViewById(R.id.r_totalcharges);
        r_received = view.findViewById(R.id.r_received);
        r_expensed = view.findViewById(R.id.r_expensed);
        r_balance = view.findViewById(R.id.r_balance);
        r_profit = view.findViewById(R.id.r_profit);

        r_eventname.setOnClickListener(this);
        r_clientname.setOnClickListener(this);
        r_eventdate.setOnClickListener(this);
        r_totalcharges.setOnClickListener(this);
        r_received.setOnClickListener(this);
        r_expensed.setOnClickListener(this);
        r_balance.setOnClickListener(this);
        r_profit.setOnClickListener(this);

        databaseHelper = new DatabaseHelper(getContext());
        prefrence = new Prefrence(getContext());

        getRecoveryData();

//        getRecoveries();

        // Spinner Drop down elements
        List<String> spinner_list = new ArrayList<String>();
        spinner_list.add("Select");
        spinner_list.add("Event Date");
        spinner_list.add("Event Name");
        spinner_list.add("Client Name");
        spinner_list.add("Total Charges");
        spinner_list.add("Received");
        spinner_list.add("Expensed");
        spinner_list.add("Balance");
        spinner_list.add("Profit");

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
                    case 5:
                        filter = 5;
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        break;
                    case 6:
                        filter = 6;
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        break;
                    case 7:
                        filter = 7;
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        break;
                    case 8:
                        filter = 8;
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
    public void onResume() {
        super.onResume();
        getRecoveryData();

    }

    public void getRecoveryData() {

        recieved = 0;
        expense = 0;
        chargesTotal = 0;
        balance = 0;
        profit = 0;
        gRecieved = 0;
        gExpense = 0;
        gChargesTotal = 0;
        gBalance = 0;
        gProfit = 0;

        recoveries = databaseHelper.getRecoveries("select * from ShadiHallBookingProfit");

        mList = new ArrayList<>();

        for (Recovery r : recoveries) {
            String pattern = "yyyy-MM-dd";
            DateFormat df = new SimpleDateFormat(pattern);
            Date date = null;
            try {
                date = df.parse(r.getEventDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String eventDate = df.format(date);
            String[] separated = r.getEventDate().split("-");

            if (m == 0) {
                mList.add(Recovery.createSection(separated[1] + "/" + separated[0]));
                mList.add(Recovery.createRow(r.getClientID(), r.getBookingID(), r.getRecieved(), r.getExpensed(), r.getChargesTotal(), r.getBalance(), r.getProfit(), r.getEventName(), r.getEventDate(), r.getClientName()));
                m = Integer.valueOf(separated[1]);

                recieved = Integer.valueOf(r.getRecieved()) + recieved;
                expense = Integer.valueOf(r.getExpensed()) + expense;
                chargesTotal = Integer.valueOf(r.getChargesTotal()) + chargesTotal;
                balance = Integer.valueOf(r.getBalance()) + balance;
                profit = Integer.valueOf(r.getProfit()) + profit;
                gRecieved = Integer.valueOf(r.getRecieved()) + gRecieved;
                gExpense = Integer.valueOf(r.getExpensed()) + gExpense;
                gChargesTotal = Integer.valueOf(r.getChargesTotal()) + gChargesTotal;
                gBalance = Integer.valueOf(r.getBalance()) + gBalance;
                gProfit = Integer.valueOf(r.getProfit()) + gProfit;
            } else if (m == Integer.valueOf(separated[1])) {
                recieved = Integer.valueOf(r.getRecieved()) + recieved;
                expense = Integer.valueOf(r.getExpensed()) + expense;
                chargesTotal = Integer.valueOf(r.getChargesTotal()) + chargesTotal;
                balance = Integer.valueOf(r.getBalance()) + balance;
                profit = Integer.valueOf(r.getProfit()) + profit;
                gRecieved = Integer.valueOf(r.getRecieved()) + gRecieved;
                gExpense = Integer.valueOf(r.getExpensed()) + gExpense;
                gChargesTotal = Integer.valueOf(r.getChargesTotal()) + gChargesTotal;
                gBalance = Integer.valueOf(r.getBalance()) + gBalance;
                gProfit = Integer.valueOf(r.getProfit()) + gProfit;
                mList.add(Recovery.createRow(r.getClientID(), r.getBookingID(), r.getRecieved(), r.getExpensed(), r.getChargesTotal(), r.getBalance(), r.getProfit(), r.getEventName(), r.getEventDate(), r.getClientName()));
            } else if (m != Integer.valueOf(separated[1]) && m != 0) {
                mList.add(Recovery.createTotal(String.valueOf(recieved), String.valueOf(expense), String.valueOf(chargesTotal), String.valueOf(balance), String.valueOf(profit)));
                recieved = 0;
                expense = 0;
                chargesTotal = 0;
                balance = 0;
                profit = 0;
                mList.add(Recovery.createSection(separated[1] + "/" + separated[0]));
                mList.add(Recovery.createRow(r.getClientID(), r.getBookingID(), r.getRecieved(), r.getExpensed(), r.getChargesTotal(), r.getBalance(), r.getProfit(), r.getEventName(), r.getEventDate(), r.getClientName()));
                recieved = Integer.valueOf(r.getRecieved()) + recieved;
                expense = Integer.valueOf(r.getExpensed()) + expense;
                chargesTotal = Integer.valueOf(r.getChargesTotal()) + chargesTotal;
                balance = Integer.valueOf(r.getBalance()) + balance;
                profit = Integer.valueOf(r.getProfit()) + profit;
                gRecieved = Integer.valueOf(r.getRecieved()) + gRecieved;
                gExpense = Integer.valueOf(r.getExpensed()) + gExpense;
                gChargesTotal = Integer.valueOf(r.getChargesTotal()) + gChargesTotal;
                gBalance = Integer.valueOf(r.getBalance()) + gBalance;
                gProfit = Integer.valueOf(r.getProfit()) + gProfit;
                m = Integer.valueOf(separated[1]);
            }
        }



        mList.add(Recovery.createTotal(String.valueOf(recieved), String.valueOf(expense), String.valueOf(chargesTotal), String.valueOf(balance), String.valueOf(profit)));
        mList.add(Recovery.createSection("Grand Total"));
        mList.add(Recovery.createTotal(String.valueOf(gRecieved), String.valueOf(gExpense), String.valueOf(gChargesTotal), String.valueOf(gBalance), String.valueOf(gProfit)));
        AppController.addCB = "View";
        adapter = new RecoveryAdapter(getContext(), mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        m = 0;
    }

    public void getRecoveries() {
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Loading");
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetRecoveries.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RES", response);
                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Recovery");
                            String success = jsonObj.getString("success");
                            Log.e("Success", success);
                            if (success.equals("1")) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery", jsonObject.toString());
                                    String ClientID = jsonObject.getString("ClientID");
                                    String BookingID = jsonObject.getString("BookingID");
                                    String Recieved = jsonObject.getString("Recieved");
                                    String Expensed = jsonObject.getString("Expensed");
                                    String ChargesTotal = jsonObject.getString("ChargesTotal");
                                    String Balance = jsonObject.getString("Balance");
                                    String Profit = jsonObject.getString("Profit");
                                    String EventName = jsonObject.getString("EventName");
                                    String ed = jsonObject.getString("EventDate");
                                    JSONObject jbb = new JSONObject(ed);
                                    String EventDate = jbb.getString("date");
                                    String ClientName = jsonObject.getString("ClientName");

                                    String pattern = "yyyy-MM-dd";
                                    DateFormat df = new SimpleDateFormat(pattern);
                                    Date date = df.parse(EventDate);
                                    String eventDate = df.format(date);
                                    String[] separated = eventDate.split("-");

                                    if (m == 0) {
                                        mList.add(Recovery.createSection(separated[1] + "/" + separated[0]));
                                        mList.add(Recovery.createRow(ClientID, BookingID, Recieved, Expensed, ChargesTotal, Balance, Profit, EventName, eventDate, ClientName));
                                        m = Integer.valueOf(separated[1]);

                                        recieved = Integer.valueOf(Recieved) + recieved;
                                        expense = Integer.valueOf(Expensed) + expense;
                                        chargesTotal = Integer.valueOf(ChargesTotal) + chargesTotal;
                                        balance = Integer.valueOf(Balance) + balance;
                                        profit = Integer.valueOf(Profit) + profit;
                                        gRecieved = Integer.valueOf(Recieved) + gRecieved;
                                        gExpense = Integer.valueOf(Expensed) + gExpense;
                                        gChargesTotal = Integer.valueOf(ChargesTotal) + gChargesTotal;
                                        gBalance = Integer.valueOf(Balance) + gBalance;
                                        gProfit = Integer.valueOf(Profit) + gProfit;
                                    } else if (m == Integer.valueOf(separated[1])) {
                                        recieved = Integer.valueOf(Recieved) + recieved;
                                        expense = Integer.valueOf(Expensed) + expense;
                                        chargesTotal = Integer.valueOf(ChargesTotal) + chargesTotal;
                                        balance = Integer.valueOf(Balance) + balance;
                                        profit = Integer.valueOf(Profit) + profit;
                                        gRecieved = Integer.valueOf(Recieved) + gRecieved;
                                        gExpense = Integer.valueOf(Expensed) + gExpense;
                                        gChargesTotal = Integer.valueOf(ChargesTotal) + gChargesTotal;
                                        gBalance = Integer.valueOf(Balance) + gBalance;
                                        gProfit = Integer.valueOf(Profit) + gProfit;
                                        mList.add(Recovery.createRow(ClientID, BookingID, Recieved, Expensed, ChargesTotal, Balance, Profit, EventName, eventDate, ClientName));
                                    } else {
                                        mList.add(Recovery.createTotal(String.valueOf(recieved), String.valueOf(expense), String.valueOf(chargesTotal), String.valueOf(balance), String.valueOf(profit)));
                                        recieved = 0;
                                        expense = 0;
                                        chargesTotal = 0;
                                        balance = 0;
                                        profit = 0;
                                        mList.add(Recovery.createSection(separated[1] + "/" + separated[0]));
                                        mList.add(Recovery.createRow(ClientID, BookingID, Recieved, Expensed, ChargesTotal, Balance, Profit, EventName, eventDate, ClientName));
                                        recieved = Integer.valueOf(Recieved) + recieved;
                                        expense = Integer.valueOf(Expensed) + expense;
                                        chargesTotal = Integer.valueOf(ChargesTotal) + chargesTotal;
                                        balance = Integer.valueOf(Balance) + balance;
                                        profit = Integer.valueOf(Profit) + profit;
                                        gRecieved = Integer.valueOf(Recieved) + gRecieved;
                                        gExpense = Integer.valueOf(Expensed) + gExpense;
                                        gChargesTotal = Integer.valueOf(ChargesTotal) + gChargesTotal;
                                        gBalance = Integer.valueOf(Balance) + gBalance;
                                        gProfit = Integer.valueOf(Profit) + gProfit;
                                        m = Integer.valueOf(separated[1]);
                                    }
                                }
                                mList.add(Recovery.createTotal(String.valueOf(recieved), String.valueOf(expense), String.valueOf(chargesTotal), String.valueOf(balance), String.valueOf(profit)));
                                mList.add(Recovery.createSection("Grand Total"));
                                mList.add(Recovery.createTotal(String.valueOf(gRecieved), String.valueOf(gExpense), String.valueOf(gChargesTotal), String.valueOf(gBalance), String.valueOf(gProfit)));
                                AppController.addCB = "View";
                                RecoveryAdapter adapter = new RecoveryAdapter(getContext(), mList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(adapter);

                            } else {
                                String message = jsonObj.getString("message");
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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
                mProgress.dismiss();
                Log.e("Error", error.toString());
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ClientID", prefrence.getClientIDSession());

                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    private void filter(String text) {
        filterdList = new ArrayList<>();

        //looping through existing elements
        if (!text.isEmpty()) {
            for (Recovery s : mList) {
                if (s.isRow() == 1) {
                    switch (filter) {
                        case 1:
                            if (s.getEventDate().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 2:
                            if (s.getEventName().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 3:
                            if (s.getClientName().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 4:
                            if (s.getChargesTotal().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 5:
                            if (s.getRecieved().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 6:
                            if (s.getExpensed().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 7:
                            if (s.getBalance().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 8:
                            if (s.getProfit().toLowerCase().contains(text.toLowerCase())) {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.r_eventname:
                orderBy = "EventName";
                orderBy(orderBy);
                break;
            case R.id.r_clientname:
                orderBy = "ClientName";
                orderBy(orderBy);
                break;
            case R.id.r_eventdate:
                orderBy = "EventDate";
                orderBy(orderBy);
                break;
            case R.id.r_totalcharges:
                orderBy = "ChargesTotal";
                orderBy(orderBy);
                break;
            case R.id.r_received:
                orderBy = "Recieved";
                orderBy(orderBy);
                break;
            case R.id.r_expensed:
                orderBy = "Expensed";
                orderBy(orderBy);
                break;
            case R.id.r_balance:
                orderBy = "Balance";
                orderBy(orderBy);
                break;
            case R.id.r_profit:
                orderBy = "Profit";
                orderBy(orderBy);
                break;
        }
    }

    public void orderBy(String order_by) {
        if (status == 0) {
            status = 1;
            orderby = " ORDER BY " + order_by + " DESC";
        } else {
            status = 0;
            orderby = " ORDER BY " + order_by + " ASC";
        }
        getRecoveryData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cb_menu, menu);
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
        MenuItem refresh = menu.findItem(R.id.action_refresh);
        refresh.setVisible(false);
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
                Toast.makeText(getContext(), "Internet Connected", Toast.LENGTH_SHORT).show();
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
            Log.i(TAG, "Created a new directory for PDF");
        }

        String pdfname = "ProfitLoss.pdf";
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
        PdfPCell cell = new PdfPCell(new Phrase("Profit/Loss By Event", chapterFont));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.addCell(cell);
        title.addCell(footerCell("", PdfPCell.ALIGN_CENTER));

        title.addCell(footerCell("", PdfPCell.ALIGN_CENTER));
        title.addCell(footerCell("", PdfPCell.ALIGN_CENTER));
        PdfPCell pCell = new PdfPCell(new Phrase(spinner.getSelectedItem() + ": " + searchView.getQuery()));
        pCell.setBorder(PdfPCell.NO_BORDER);
        pCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        pCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.addCell(pCell);


        PdfPTable table = new PdfPTable(new float[]{3, 3, 3, 3, 3, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(40);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.setSpacingBefore(20);
        table.addCell("Event Date");
        table.addCell("Event Name");
        table.addCell("Client Name");
        table.addCell("Total Charges");
        table.addCell("Received");
        table.addCell("Expensed");
        table.addCell("Balance");
        table.addCell("Profit");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.PINK);
        }

        recieved = 0;
        expense = 0;
        chargesTotal = 0;
        balance = 0;
        profit = 0;
        gRecieved = 0;
        gExpense = 0;
        gChargesTotal = 0;
        gBalance = 0;
        gProfit = 0;

        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA, 13, Font.BOLD);
        PdfPCell total = new PdfPCell(new Phrase("Total", totalFont));
        total.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        total.setVerticalAlignment(Element.ALIGN_MIDDLE);
        total.setFixedHeight(35);
        d = "0";
        if (filter > 0) {
            for (Recovery c : filterdList) {
                String[] separated = null;
                if (!c.getEventDate().equals(""))
                    separated = c.getEventDate().split("-");

                if (d.equals("0")) {
                    d = separated[1];

                    PdfPCell section = new PdfPCell(new Phrase(separated[1] + "-" + separated[0], totalFont));
                    section.setBorder(PdfPCell.NO_BORDER);
                    section.setFixedHeight(30);
                    section.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(section);
                    table.addCell(footerCell("", PdfPCell.ALIGN_RIGHT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));

                    table.addCell(getCell(AppController.stringDateFormate("yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd", c.getEventDate()), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getEventName(), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getClientName(), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getChargesTotal(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getRecieved(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getExpensed(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getBalance(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getProfit(), PdfPCell.ALIGN_RIGHT));

                    recieved = Integer.valueOf(c.getRecieved()) + recieved;
                    expense = Integer.valueOf(c.getExpensed()) + expense;
                    chargesTotal = Integer.valueOf(c.getChargesTotal()) + chargesTotal;
                    balance = Integer.valueOf(c.getBalance()) + balance;
                    profit = Integer.valueOf(c.getProfit()) + profit;
                    gRecieved = Integer.valueOf(c.getRecieved()) + gRecieved;
                    gExpense = Integer.valueOf(c.getExpensed()) + gExpense;
                    gChargesTotal = Integer.valueOf(c.getChargesTotal()) + gChargesTotal;
                    gBalance = Integer.valueOf(c.getBalance()) + gBalance;
                    gProfit = Integer.valueOf(c.getProfit()) + gProfit;
                } else if (d.equals(separated[1])) {
                    table.addCell(getCell(AppController.stringDateFormate("yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd", c.getEventDate()), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getEventName(), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getClientName(), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getChargesTotal(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getRecieved(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getExpensed(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getBalance(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getProfit(), PdfPCell.ALIGN_RIGHT));

                    recieved = Integer.valueOf(c.getRecieved()) + recieved;
                    expense = Integer.valueOf(c.getExpensed()) + expense;
                    chargesTotal = Integer.valueOf(c.getChargesTotal()) + chargesTotal;
                    balance = Integer.valueOf(c.getBalance()) + balance;
                    profit = Integer.valueOf(c.getProfit()) + profit;
                    gRecieved = Integer.valueOf(c.getRecieved()) + gRecieved;
                    gExpense = Integer.valueOf(c.getExpensed()) + gExpense;
                    gChargesTotal = Integer.valueOf(c.getChargesTotal()) + gChargesTotal;
                    gBalance = Integer.valueOf(c.getBalance()) + gBalance;
                    gProfit = Integer.valueOf(c.getProfit()) + gProfit;
                } else if (!d.equals("0") && !d.equals(separated[1])) {

                    d = separated[1];

                    table.addCell(getCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(total);
                    table.addCell(getCell(String.valueOf(chargesTotal), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(String.valueOf(recieved), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(String.valueOf(expense), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(String.valueOf(balance), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(String.valueOf(profit), PdfPCell.ALIGN_RIGHT));

                    PdfPCell section = new PdfPCell(new Phrase(separated[1] + "-" + separated[0], totalFont));
                    section.setBorder(PdfPCell.NO_BORDER);
                    section.setFixedHeight(30);
                    section.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(section);
                    table.addCell(footerCell("", PdfPCell.ALIGN_RIGHT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));

                    recieved = 0;
                    expense = 0;
                    chargesTotal = 0;
                    balance = 0;
                    profit = 0;
                    table.addCell(getCell(AppController.stringDateFormate("yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd", c.getEventDate()), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getEventName(), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getClientName(), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getChargesTotal(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getRecieved(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getExpensed(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getBalance(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getProfit(), PdfPCell.ALIGN_RIGHT));

                    recieved = Integer.valueOf(c.getRecieved()) + recieved;
                    expense = Integer.valueOf(c.getExpensed()) + expense;
                    chargesTotal = Integer.valueOf(c.getChargesTotal()) + chargesTotal;
                    balance = Integer.valueOf(c.getBalance()) + balance;
                    profit = Integer.valueOf(c.getProfit()) + profit;
                    gRecieved = Integer.valueOf(c.getRecieved()) + gRecieved;
                    gExpense = Integer.valueOf(c.getExpensed()) + gExpense;
                    gChargesTotal = Integer.valueOf(c.getChargesTotal()) + gChargesTotal;
                    gBalance = Integer.valueOf(c.getBalance()) + gBalance;
                    gProfit = Integer.valueOf(c.getProfit()) + gProfit;
                }
            }
        } else {
            for (Recovery c : recoveries) {
                String[] separated = c.getEventDate().split("-");
                if (d.equals("0")) {
                    d = separated[1];

                    PdfPCell section = new PdfPCell(new Phrase(separated[1] + "-" + separated[0], totalFont));
                    section.setBorder(PdfPCell.NO_BORDER);
                    section.setFixedHeight(30);
                    section.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(section);
                    table.addCell(footerCell("", PdfPCell.ALIGN_RIGHT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));

                    table.addCell(getCell(AppController.stringDateFormate("yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd", c.getEventDate()), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getEventName(), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getClientName(), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getChargesTotal(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getRecieved(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getExpensed(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getBalance(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getProfit(), PdfPCell.ALIGN_RIGHT));

                    recieved = Integer.valueOf(c.getRecieved()) + recieved;
                    expense = Integer.valueOf(c.getExpensed()) + expense;
                    chargesTotal = Integer.valueOf(c.getChargesTotal()) + chargesTotal;
                    balance = Integer.valueOf(c.getBalance()) + balance;
                    profit = Integer.valueOf(c.getProfit()) + profit;
                    gRecieved = Integer.valueOf(c.getRecieved()) + gRecieved;
                    gExpense = Integer.valueOf(c.getExpensed()) + gExpense;
                    gChargesTotal = Integer.valueOf(c.getChargesTotal()) + gChargesTotal;
                    gBalance = Integer.valueOf(c.getBalance()) + gBalance;
                    gProfit = Integer.valueOf(c.getProfit()) + gProfit;
                } else if (d.equals(separated[1])) {
                    table.addCell(getCell(AppController.stringDateFormate("yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd", c.getEventDate()), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getEventName(), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getClientName(), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getChargesTotal(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getRecieved(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getExpensed(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getBalance(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getProfit(), PdfPCell.ALIGN_RIGHT));

                    recieved = Integer.valueOf(c.getRecieved()) + recieved;
                    expense = Integer.valueOf(c.getExpensed()) + expense;
                    chargesTotal = Integer.valueOf(c.getChargesTotal()) + chargesTotal;
                    balance = Integer.valueOf(c.getBalance()) + balance;
                    profit = Integer.valueOf(c.getProfit()) + profit;
                    gRecieved = Integer.valueOf(c.getRecieved()) + gRecieved;
                    gExpense = Integer.valueOf(c.getExpensed()) + gExpense;
                    gChargesTotal = Integer.valueOf(c.getChargesTotal()) + gChargesTotal;
                    gBalance = Integer.valueOf(c.getBalance()) + gBalance;
                    gProfit = Integer.valueOf(c.getProfit()) + gProfit;
                } else if (!d.equals("0") && !d.equals(separated[1])) {

                    d = separated[1];

                    table.addCell(getCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(total);
                    table.addCell(getCell(String.valueOf(chargesTotal), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(String.valueOf(recieved), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(String.valueOf(expense), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(String.valueOf(balance), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(String.valueOf(profit), PdfPCell.ALIGN_RIGHT));

                    PdfPCell section = new PdfPCell(new Phrase(separated[1] + "-" + separated[0], totalFont));
                    section.setBorder(PdfPCell.NO_BORDER);
                    section.setFixedHeight(30);
                    section.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    section.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(section);
                    table.addCell(footerCell("", PdfPCell.ALIGN_RIGHT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));
                    table.addCell(footerCell("", PdfPCell.ALIGN_LEFT));

                    recieved = 0;
                    expense = 0;
                    chargesTotal = 0;
                    balance = 0;
                    profit = 0;
                    table.addCell(getCell(AppController.stringDateFormate("yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd", c.getEventDate()), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getEventName(), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getClientName(), PdfPCell.ALIGN_LEFT));
                    table.addCell(getCell(c.getChargesTotal(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getRecieved(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getExpensed(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getBalance(), PdfPCell.ALIGN_RIGHT));
                    table.addCell(getCell(c.getProfit(), PdfPCell.ALIGN_RIGHT));

                    recieved = Integer.valueOf(c.getRecieved()) + recieved;
                    expense = Integer.valueOf(c.getExpensed()) + expense;
                    chargesTotal = Integer.valueOf(c.getChargesTotal()) + chargesTotal;
                    balance = Integer.valueOf(c.getBalance()) + balance;
                    profit = Integer.valueOf(c.getProfit()) + profit;
                    gRecieved = Integer.valueOf(c.getRecieved()) + gRecieved;
                    gExpense = Integer.valueOf(c.getExpensed()) + gExpense;
                    gChargesTotal = Integer.valueOf(c.getChargesTotal()) + gChargesTotal;
                    gBalance = Integer.valueOf(c.getBalance()) + gBalance;
                    gProfit = Integer.valueOf(c.getProfit()) + gProfit;
                }
            }
        }

        table.addCell(getCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(getCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(total);
        table.addCell(getCell(String.valueOf(chargesTotal), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(recieved), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(expense), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(balance), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(profit), PdfPCell.ALIGN_RIGHT));

        table.addCell(getCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(getCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(getCell("Grand Total", PdfPCell.ALIGN_CENTER));
        table.addCell(getCell(String.valueOf(gChargesTotal), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gRecieved), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gExpense), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gBalance), PdfPCell.ALIGN_RIGHT));
        table.addCell(getCell(String.valueOf(gProfit), PdfPCell.ALIGN_RIGHT));

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
        Log.e("PDFDocument", "Created");
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

    public void customPDFView() {
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
}
