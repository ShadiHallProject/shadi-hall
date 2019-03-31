package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.ChaartOfAccAddActivity;
import org.by9steps.shadihall.adapters.ReportsAdapter;
import org.by9steps.shadihall.adapters.SpinnerAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.Account2Group;
import org.by9steps.shadihall.model.Reports;
import org.by9steps.shadihall.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_SPINNER_ID = "spinner_position";

    ProgressDialog mProgress;
    RecyclerView recyclerView;
    String currentDate;
    int day,month,year;
    static Button date_picker;
    Spinner sp_acgroup;
    ImageView refresh, add;

    DatabaseHelper databaseHelper;
    List<Reports> reportsList;

    public static TextView deb_total, cre_total;

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

        recyclerView = view.findViewById(R.id.recycler);
        date_picker = view.findViewById(R.id.date_picker);
        sp_acgroup = view.findViewById(R.id.sp_acgroup);
        deb_total = view.findViewById(R.id.deb_total);
        cre_total = view.findViewById(R.id.cre_total);
        refresh = view.findViewById(R.id.refresh);
        add = view.findViewById(R.id.add);

        databaseHelper = new DatabaseHelper(getContext());

        add.setOnClickListener(this);
//        FloatingActionButton fab = view.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Click action
//                startActivity(new Intent(getContext(), ChaartOfAccAddActivity.class));
//            }
//        });

        Date date = new Date();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
//        currentDate = curFormater.format(date);
//        Log.e("DATEEE", currentDate);
        date_picker.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));

        List<Account2Group> list = databaseHelper.getAccount2Group("SELECT * FROM Account2Group");
        if (list == null || list.isEmpty()){
            getAccountGroups();
            list = list = databaseHelper.getAccount2Group("SELECT * FROM Account2Group");
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getContext(),list,"");
            sp_acgroup.setAdapter(spinnerAdapter);
        }else {
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(getContext(),list,"");
            sp_acgroup.setAdapter(spinnerAdapter);
//            getCashBook();

        }

        sp_acgroup.setSelection(Integer.valueOf(spPosition));

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getCashBook();
                getReports();
            }
        });

        getReports();

        return view;
    }

    public void getReports(){


        List<User> list = User.listAll(User.class);

        for (User u : list){
            String query = "SELECT        GroupTotal.AccountID, SUM(GroupTotal.Debit) AS Debit, SUM(GroupTotal.Credit) AS Credit, SUM(GroupTotal.Debit) - SUM(GroupTotal.Credit) AS Bal, CASE WHEN (SUM(Debit) - SUM(Credit)) > 0 THEN (SUM(Debit) - SUM(Credit))\n" +
                    "                         ELSE 0 END AS DebitBal, CASE WHEN (SUM(Debit) - SUM(Credit)) < 0 THEN (SUM(Debit) - SUM(Credit)) ELSE 0 END AS CreditBal, Account3Name_2.AcName, Account3Name_2.AcGroupID\n" +
                    "FROM            (SELECT        CashBook.DebitAccount AS AccountID, CashBook.Amount AS Debit, 0 AS Credit\n" +
                    "                          FROM            CashBook INNER JOIN\n" +
                    "                                                    Account3Name ON CashBook.DebitAccount = Account3Name.AcNameID\n" +
                    "                          WHERE        (CashBook.ClientID = "+u.getClientID()+") AND (CashBook.CBDate <= '"+date_picker.getText().toString()+"') AND (Account3Name.AcGroupID = "+((Account2Group)sp_acgroup.getSelectedItem()).getAcGroupID()+")\n" +
                    "                          UNION ALL\n" +
                    "                          SELECT        CashBook_1.CreditAccount AS AccountID, 0 AS Debit, CashBook_1.Amount AS Credit\n" +
                    "                          FROM            CashBook AS CashBook_1 INNER JOIN\n" +
                    "                                                   Account3Name AS Account3Name_1 ON CashBook_1.CreditAccount = Account3Name_1.AcNameID\n" +
                    "                          WHERE        (CashBook_1.ClientID = "+u.getClientID()+") AND (CashBook_1.CBDate <= '"+date_picker.getText().toString()+"') AND (Account3Name_1.AcGroupID = "+((Account2Group)sp_acgroup.getSelectedItem()).getAcGroupID()+")) AS GroupTotal INNER JOIN\n" +
                    "                         Account3Name AS Account3Name_2 ON GroupTotal.AccountID = Account3Name_2.AcNameID\n" +
                    "GROUP BY GroupTotal.AccountID, Account3Name_2.AcName, Account3Name_2.AcGroupID";
            reportsList = databaseHelper.getReports(query);
        }

        ReportsAdapter adapter = new ReportsAdapter(getContext(),reportsList,ReportsAdapter.Cash_Book);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

    }

    public void getCashBook(){
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Loading");
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetCashBook.php";

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
//                                CashBook.deleteAll(CashBook.class);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery",jsonObject.toString());
                                    String AccountID = jsonObject.getString("AccountID");
                                    String Debit = jsonObject.getString("Debit");
                                    String Credit = jsonObject.getString("Credit");
                                    String Bal = jsonObject.getString("Bal");
                                    String DebitBal = jsonObject.getString("DebitBal");
                                    String CreditBal = jsonObject.getString("CreditBal");
                                    String AcName = jsonObject.getString("AcName");
                                    String AcGroupID = jsonObject.getString("AcGroupID");

//                                    CashBook cashBook = new CashBook(AccountID,Debit,Credit,Bal,DebitBal,CreditBal,AcName,AcGroupID);
//                                    cashBook.save();

                                }

//                                List<CashBook> mList = CashBook.listAll(CashBook.class);
//                                ReportsAdapter adapter = new ReportsAdapter(getContext(),mList,ReportsAdapter.Cash_Book);
//                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                                recyclerView.setAdapter(adapter);

                            }else {
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
                    params.put("AcGroupID", ((Account2Group)sp_acgroup.getSelectedItem()).getAcGroupID());
                    params.put("CBDate", date_picker.getText().toString());
                }
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getAccountGroups(){
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
                        Log.e("RES",response);
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Account2Group");
                            String success = jsonObj.getString("success");
                            Log.e("Success",success);
                            if (success.equals("1")){
//                                Account2Group.deleteAll(Account2Group.class);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery",jsonObject.toString());
                                    String AcGroupID = jsonObject.getString("AcGroupID");
                                    String AcTypeID = jsonObject.getString("AcTypeID");
                                    String AcGruopName = jsonObject.getString("AcGruopName");

                                    databaseHelper.createAccount2Group(new Account2Group(AcGroupID,AcTypeID,AcGruopName));

                                }

                                mProgress.dismiss();
//                                getCashBook();

                            }else {
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
                Log.e("Error",error.toString());
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
        switch (v.getId()){
            case R.id.add:
                startActivity(new Intent(getContext(),ChaartOfAccAddActivity.class));
                break;
        }
    }
}
