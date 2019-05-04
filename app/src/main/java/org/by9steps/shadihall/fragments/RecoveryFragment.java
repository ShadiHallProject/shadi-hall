package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
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

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.RecoveryAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.Recovery;
import org.by9steps.shadihall.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    String orderBy = "EventName";
    int status = 0;
    String orderby = " ORDER BY " + orderBy + " DESC";

    TextView r_eventname, r_clientname, r_eventdate, r_totalcharges, r_received, r_expensed, r_balance, r_profit;

    RecyclerView recyclerView;

    DatabaseHelper databaseHelper;
    List<Recovery> recoveries;
    List<Recovery> filterdList;

    RecoveryAdapter adapter;
    //    EditText search;
    SearchView searchView;
    Spinner spinner;
    int filter;

    public RecoveryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recovery, container, false);
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

    public void getRecoveryData(){
        String query = "";
        List<User> list = User.listAll(User.class);
        for (User u : list) {
            query = "SELECT        derivedtbl_1.ClientID, Booking.BookingID, SUM(derivedtbl_1.Received) AS Recieved, SUM(derivedtbl_1.Expense) AS Expensed, Booking.ChargesTotal, IFNULL(Booking.ChargesTotal, 0) - IFNULL(SUM(derivedtbl_1.Received), 0)\n" +
                    "                          AS Balance, IFNULL(SUM(derivedtbl_1.Received), 0) - IFNULL(SUM(derivedtbl_1.Expense), 0) AS Profit, Booking.EventName, Booking.EventDate, Booking.ClientName\n" +
                    "FROM            (SELECT        CashBook.ClientID, CashBook.BookingID, SUM(CashBook.Amount) AS Received, 0 AS Expense\n" +
                    "                          FROM            CashBook INNER JOIN Account3Name ON CashBook.CreditAccount = Account3Name.AcNameID\n" +
                    "                          WHERE        (Account3Name.AcName = 'Booking Income')\n" +
                    "                          GROUP BY CashBook.BookingID, CashBook.ClientID\n" +
                    "                          UNION ALL\n" +
                    "                          SELECT        CashBook_1.ClientID, CashBook_1.BookingID, 0 AS Received, SUM(CashBook_1.Amount) AS Expense\n" +
                    "                          FROM            CashBook AS CashBook_1 INNER JOIN Account3Name AS Account3Name_1 ON CashBook_1.DebitAccount = Account3Name_1.AcNameID\n" +
                    "                          WHERE        (Account3Name_1.AcName = 'Booking Expense')\n" +
                    "                          GROUP BY CashBook_1.BookingID, CashBook_1.ClientID) AS derivedtbl_1 INNER JOIN\n" +
                    "                          Booking ON derivedtbl_1.BookingID = Booking.BookingID GROUP BY derivedtbl_1.ClientID, Booking.BookingID, Booking.ChargesTotal, Booking.EventName, Booking.EventDate,Booking.ClientName HAVING (derivedtbl_1.ClientID =" + u.getClientID() + ")"+ orderby;
        }

        recoveries = databaseHelper.getRecoveries(query);

        mList = new ArrayList<>();

        for (Recovery r : recoveries) {
//            String pattern="yyyy-MM-dd";
//            DateFormat df = new SimpleDateFormat(pattern);
//            Date date = df.parse(r.getEventDate());
//            String eventDate = df.format(date);
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
            } else {
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
                List<User> list = User.listAll(User.class);
                for (User u : list) {
                    params.put("ClientID", u.getClientID());
                }
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
        switch (view.getId()){
            case R.id.r_eventname:
                orderBy = "EventName";
                if (status == 0) {
                    status = 1;
                    orderby = " ORDER BY " + orderBy + " ASC";
                } else {
                    status = 0;
                    orderby = " ORDER BY " + orderBy + " DESC";
                }
                getRecoveryData();
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

    public void orderBy(String order_by){
        if (status == 0) {
            status = 1;
            orderby = " ORDER BY " + order_by + " DESC";
        } else {
            status = 0;
            orderby = " ORDER BY " + order_by + " ASC";
        }
        getRecoveryData();
    }
}
