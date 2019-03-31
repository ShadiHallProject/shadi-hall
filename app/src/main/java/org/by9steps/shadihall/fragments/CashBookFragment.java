package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
import android.content.Intent;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import org.by9steps.shadihall.activities.CashCollectionActivity;
import org.by9steps.shadihall.adapters.CashBookAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.CashEntry;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class CashBookFragment extends Fragment {

    ProgressDialog mProgress;
    RecyclerView recyclerView;
    List<CashEntry> mList;

    DatabaseHelper databaseHelper;
    List<CashBook> cashBooksList;

    int m = 0, amount, gAmount;

    public CashBookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cash_book, container, false);

        recyclerView = view.findViewById(R.id.recycler);

        databaseHelper = new DatabaseHelper(getContext());


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(getContext(), CashCollectionActivity.class);
                intent.putExtra("BookingID","0");
                intent.putExtra("Spinner","View");
                startActivity(intent);
            }
        });

        mList = new ArrayList<>();

        String query = "";

        List<User> list = User.listAll(User.class);
        for (User u : list){
            query = "SELECT        CashBook.CashBookID, CashBook.CBDate, CashBook.DebitAccount, CashBook.CreditAccount, CashBook.CBRemarks, CashBook.Amount, CashBook.ClientID, CashBook.ClientUserID, CashBook.BookingID, \n" +
                    "                         Account3Name.AcName AS DebitAccountName, Account3Name_1.AcName AS CreditAccountName, Account3Name_2.AcName AS UserName, CashBook.UpdatedDate\n" +
                    "FROM            CashBook INNER JOIN\n" +
                    "                         Account3Name ON CashBook.DebitAccount = Account3Name.AcNameID INNER JOIN\n" +
                    "                         Account3Name AS Account3Name_1 ON CashBook.CreditAccount = Account3Name_1.AcNameID INNER JOIN\n" +
                    "                         Account3Name AS Account3Name_2 ON CashBook.ClientUserID = Account3Name_2.AcNameID\n" +
                    "WHERE        (CashBook.ClientID = "+u.getClientID()+")";
            cashBooksList = databaseHelper.getCashBookEntry(query);
        }

        for (CashBook c : cashBooksList){
            String[] separated = c.getCBDate().split("-");

            if (m == 0) {
                mList.add(CashEntry.createSection(separated[1]+"/"+separated[2]));
                mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),"ss",c.getAmount(),c.getClientID(),c.getClientUserID(),c.getBookingID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), "s"));
                m = Integer.valueOf(separated[1]);

                amount = Integer.valueOf(c.getAmount()) + amount;
                gAmount = Integer.valueOf(c.getAmount()) + gAmount;
            }else if (m == Integer.valueOf(separated[1])){
                amount = Integer.valueOf(c.getAmount()) + amount;
                gAmount = Integer.valueOf(c.getAmount()) + gAmount;
                mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),"ss",c.getAmount(),c.getClientID(),c.getClientUserID(),c.getBookingID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), "s"));
            }else {
                mList.add(CashEntry.createTotal(String.valueOf(amount)));
                amount = 0;
                amount = Integer.valueOf(c.getAmount()) + amount;
                gAmount = Integer.valueOf(c.getAmount()) + gAmount;

                mList.add(CashEntry.createSection(separated[1]+"/"+separated[2]));
                mList.add(CashEntry.createRow(c.getCashBookID(),c.getCBDate(),c.getDebitAccount(),c.getCreditAccount(),"ss",c.getAmount(),c.getClientID(),c.getClientUserID(),c.getBookingID(),c.getDebitAccountName(),c.getCreditAccountName(),c.getUserName(), "s"));
                m = Integer.valueOf(separated[1]);
            }
        }

        mList.add(CashEntry.createTotal(String.valueOf(amount)));
        mList.add(CashEntry.createSection("Grand Total"));
        mList.add(CashEntry.createTotal(String.valueOf(gAmount)));
        CashBookAdapter adapter = new CashBookAdapter(getContext(),mList);
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
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetCashbookEntry.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RES",response);
                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("CashBook");
                            String success = jsonObj.getString("success");
                            Log.e("Success",success);
                            if (success.equals("1")){
                                mList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String CashBookID = jsonObject.getString("CashBookID");
                                    String CB_Date = jsonObject.getString("CBDate");
                                    JSONObject jbb = new JSONObject(CB_Date);
                                    String CBDate = jbb.getString("date");
                                    String DebitAccount = jsonObject.getString("DebitAccount");
                                    String CreditAccount = jsonObject.getString("CreditAccount");
//                                    String CBRemarks = jsonObject.getString("CBRemarks");
                                    String Amount = jsonObject.getString("Amount");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String ClientUserID = jsonObject.getString("ClientUserID");
                                    String BookingID = jsonObject.getString("BookingID");
                                    String DebitAccountName = jsonObject.getString("DebitAccountName");
                                    String CreditAccountName = jsonObject.getString("CreditAccountName");
                                    String UserName = jsonObject.getString("UserName");
//                                    String Updated_Date = jsonObject.getString("UpdatedDate");
//                                    JSONObject jbbb = new JSONObject(Updated_Date);
//                                    String UpdatedDate = jbbb.getString("date");

                                    DateFormat old = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    String pattern="dd-MM-yyyy";
                                    DateFormat df = new SimpleDateFormat(pattern);
                                    Date date = old.parse(CBDate);
                                    String CBDate1 = df.format(date);
                                    String[] separated = CBDate1.split("-");

                                    if (m == 0) {
                                        mList.add(CashEntry.createSection(separated[1]+"/"+separated[2]));
                                        mList.add(CashEntry.createRow(CashBookID,CBDate1,DebitAccount,CreditAccount,"ss",Amount,ClientID,ClientUserID,BookingID,DebitAccountName,CreditAccountName,UserName, "s"));
                                        m = Integer.valueOf(separated[1]);

                                        amount = Integer.valueOf(Amount) + amount;
                                        gAmount = Integer.valueOf(Amount) + gAmount;
                                    }else if (m == Integer.valueOf(separated[1])){
                                        amount = Integer.valueOf(Amount) + amount;
                                        gAmount = Integer.valueOf(Amount) + gAmount;
                                        mList.add(CashEntry.createRow(CashBookID,CBDate1,DebitAccount,CreditAccount,"ss",Amount,ClientID,ClientUserID,BookingID,DebitAccountName,CreditAccountName,UserName, "s"));
                                    }else {
                                        mList.add(CashEntry.createTotal(String.valueOf(amount)));
                                        amount = 0;
                                        amount = Integer.valueOf(Amount) + amount;
                                        gAmount = Integer.valueOf(Amount) + gAmount;

                                        mList.add(CashEntry.createSection(separated[1]+"/"+separated[2]));
                                        mList.add(CashEntry.createRow(CashBookID,CBDate1,DebitAccount,CreditAccount,"ss",Amount,ClientID,ClientUserID,BookingID,DebitAccountName,CreditAccountName,UserName, "s"));
                                        m = Integer.valueOf(separated[1]);
                                    }
                                }
                                mList.add(CashEntry.createTotal(String.valueOf(amount)));
                                mList.add(CashEntry.createSection("Grand Total"));
                                mList.add(CashEntry.createTotal(String.valueOf(gAmount)));
                                CashBookAdapter adapter = new CashBookAdapter(getContext(),mList);
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

