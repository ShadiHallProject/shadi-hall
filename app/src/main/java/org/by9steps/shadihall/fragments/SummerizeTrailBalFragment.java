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
import org.by9steps.shadihall.adapters.SummerizeTrialBalanceAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.Summerize;
import org.by9steps.shadihall.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummerizeTrailBalFragment extends Fragment {

    ProgressDialog mProgress;
    String currentDate;

    DatabaseHelper databaseHelper;
    List<Summerize> summerizeList;

    RecyclerView recyclerView;
    List<Summerize> mList = new ArrayList<>();
    String m = "First";
    int debBal, creBal;
    int gDebBal, gCreBal;


    public SummerizeTrailBalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summerize_trail_bal, container, false);

        recyclerView = view.findViewById(R.id.recycler);
        databaseHelper = new DatabaseHelper(getContext());

        Date date = new Date();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = curFormater.format(date);

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
                    "GROUP BY derivedtbl_1.ClientID, Account2Group.AcGroupID, Account2Group.AcGruopName, Account1Type.AcTypeName, Account1Type.AcTypeID";
            summerizeList = databaseHelper.getSummerizeTB(query);
        }

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
        SummerizeTrialBalanceAdapter adapter = new SummerizeTrialBalanceAdapter(getContext(),mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

//        getSummerize();
        return view;
    }


    public void getSummerize(){
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Loading");
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/SummerizeTrailBalance.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Summerize");
                            Log.e("SSSSS",jsonArray.toString());
                            String success = jsonObj.getString("success");
                            if (success.equals("1")){
//                                Account3Name.deleteAll(Account3Name.class);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery",jsonObject.toString());
                                    String AcTypeID = jsonObject.getString("AcTypeID");
                                    String AcTypeName = jsonObject.getString("AcTypeName");
                                    String AcGroupID = jsonObject.getString("AcGroupID");
                                    String AcGruopName = jsonObject.getString("AcGruopName");
                                    String Debit = jsonObject.getString("Debit");
                                    String Credit = jsonObject.getString("Credit");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String Bal = jsonObject.getString("Bal");
                                    String DebitBL = jsonObject.getString("DebitBL");
                                    String CreditBL = jsonObject.getString("CreditBL");

                                    if (m.equals("First")) {
                                        mList.add(Summerize.createSection(AcTypeName));
                                        mList.add(Summerize.createRow(AcTypeID,AcTypeName,AcGroupID,AcGruopName,Debit,Credit,ClientID,Bal,DebitBL,CreditBL));
                                        m = AcTypeName;

                                        debBal = Integer.valueOf(DebitBL) + debBal;
                                        creBal = Integer.valueOf(CreditBL) + creBal;
                                        gDebBal = Integer.valueOf(DebitBL) + gDebBal;
                                        gCreBal = Integer.valueOf(CreditBL) + gCreBal;

                                    }else if (m.equals(AcTypeName)){
                                        debBal = Integer.valueOf(DebitBL) + debBal;
                                        creBal = Integer.valueOf(CreditBL) + creBal;
                                        gDebBal = Integer.valueOf(DebitBL) + gDebBal;
                                        gCreBal = Integer.valueOf(CreditBL) + gCreBal;
                                        mList.add(Summerize.createRow(AcTypeID,AcTypeName,AcGroupID,AcGruopName,Debit,Credit,ClientID,Bal,DebitBL,CreditBL));
                                    }else {
                                        mList.add(Summerize.createTotal(String.valueOf(debBal),String.valueOf(creBal)));
                                        debBal = 0; creBal = 0;
                                        debBal = Integer.valueOf(DebitBL) + debBal;
                                        creBal = Integer.valueOf(CreditBL) + creBal;
                                        gDebBal = Integer.valueOf(DebitBL) + gDebBal;
                                        gCreBal = Integer.valueOf(CreditBL) + gCreBal;
                                        mList.add(Summerize.createSection(AcTypeName));
                                        mList.add(Summerize.createRow(AcTypeID,AcTypeName,AcGroupID,AcGruopName,Debit,Credit,ClientID,Bal,DebitBL,CreditBL));
                                        m = AcTypeName;
                                    }
                                }
                                mList.add(Summerize.createTotal(String.valueOf(debBal),String.valueOf(creBal)));
                                mList.add(Summerize.createSection("Grand Total"));
                                mList.add(Summerize.createTotal(String.valueOf(gDebBal),String.valueOf(gCreBal)));
                                SummerizeTrialBalanceAdapter adapter = new SummerizeTrialBalanceAdapter(getContext(),mList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(adapter);

                                mProgress.dismiss();

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
                    params.put("CBDate", currentDate);
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
