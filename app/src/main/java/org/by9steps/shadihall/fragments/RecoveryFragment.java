package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecoveryFragment extends Fragment {

    ProgressDialog mProgress;
    List<Recovery> mList;
    int m = 0, recieved, expense, chargesTotal, balance, profit;
    int gRecieved, gExpense, gChargesTotal, gBalance, gProfit;

    RecyclerView recyclerView;

    DatabaseHelper databaseHelper;
    List<Recovery> recoveries;


    public RecoveryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recovery, container, false);
        recyclerView = view.findViewById(R.id.recycler);

        databaseHelper = new DatabaseHelper(getContext());

        String query = "";
        List<User> list = User.listAll(User.class);
        for (User u: list) {
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
                    "                          Booking ON derivedtbl_1.BookingID = Booking.BookingID GROUP BY derivedtbl_1.ClientID, Booking.BookingID, Booking.ChargesTotal, Booking.EventName, Booking.EventDate,Booking.ClientName HAVING (derivedtbl_1.ClientID ="+ u.getClientID()+")";
        }

        recoveries = databaseHelper.getRecoveries(query);

        mList = new ArrayList<>();

        for (Recovery r : recoveries){
//            String pattern="yyyy-MM-dd";
//            DateFormat df = new SimpleDateFormat(pattern);
//            Date date = df.parse(r.getEventDate());
//            String eventDate = df.format(date);
            String[] separated = r.getEventDate().split("-");

            if (m == 0) {
                mList.add(Recovery.createSection(separated[1]+"/"+separated[0]));
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
            }else if (m == Integer.valueOf(separated[1])){
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
            }else {
                mList.add(Recovery.createTotal(String.valueOf(recieved),String.valueOf(expense),String.valueOf(chargesTotal),String.valueOf(balance),String.valueOf(profit)));
                recieved = 0; expense = 0; chargesTotal = 0; balance = 0; profit = 0;
                mList.add(Recovery.createSection(separated[1]+"/"+separated[0]));
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

        mList.add(Recovery.createTotal(String.valueOf(recieved),String.valueOf(expense),String.valueOf(chargesTotal),String.valueOf(balance),String.valueOf(profit)));
        mList.add(Recovery.createSection("Grand Total"));
        mList.add(Recovery.createTotal(String.valueOf(gRecieved),String.valueOf(gExpense),String.valueOf(gChargesTotal),String.valueOf(gBalance),String.valueOf(gProfit)));
        AppController.addCB = "View";
        RecoveryAdapter adapter = new RecoveryAdapter(getContext(),mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

//        getRecoveries();

        return view;
    }

    public void getRecoveries(){
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
                        Log.e("RES",response);
                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Recovery");
                            String success = jsonObj.getString("success");
                            Log.e("Success",success);
                            if (success.equals("1")){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery",jsonObject.toString());
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

                                    String pattern="yyyy-MM-dd";
                                    DateFormat df = new SimpleDateFormat(pattern);
                                    Date date = df.parse(EventDate);
                                    String eventDate = df.format(date);
                                    String[] separated = eventDate.split("-");

                                    if (m == 0) {
                                        mList.add(Recovery.createSection(separated[1]+"/"+separated[0]));
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
                                    }else if (m == Integer.valueOf(separated[1])){
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
                                    }else {
                                        mList.add(Recovery.createTotal(String.valueOf(recieved),String.valueOf(expense),String.valueOf(chargesTotal),String.valueOf(balance),String.valueOf(profit)));
                                        recieved = 0; expense = 0; chargesTotal = 0; balance = 0; profit = 0;
                                        mList.add(Recovery.createSection(separated[1]+"/"+separated[0]));
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
                                mList.add(Recovery.createTotal(String.valueOf(recieved),String.valueOf(expense),String.valueOf(chargesTotal),String.valueOf(balance),String.valueOf(profit)));
                                mList.add(Recovery.createSection("Grand Total"));
                                mList.add(Recovery.createTotal(String.valueOf(gRecieved),String.valueOf(gExpense),String.valueOf(gChargesTotal),String.valueOf(gBalance),String.valueOf(gProfit)));
                                AppController.addCB = "View";
                                RecoveryAdapter adapter = new RecoveryAdapter(getContext(),mList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(adapter);

                            }else {
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
                Log.e("Error",error.toString());
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                List<User> list = User.listAll(User.class);
                for (User u: list) {
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

}
