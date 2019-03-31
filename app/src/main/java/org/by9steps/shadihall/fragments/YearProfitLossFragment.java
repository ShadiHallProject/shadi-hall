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
import org.by9steps.shadihall.adapters.ProfitLossDateAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.ProfitLoss;
import org.by9steps.shadihall.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class YearProfitLossFragment extends Fragment {

    ProgressDialog mProgress;

    RecyclerView recyclerView;
    List<ProfitLoss> mList = new ArrayList<>();
    int m = 0, income, expense, profit;
    int gIncome, gExpense, gProfit;

    DatabaseHelper databaseHelper;
    List<ProfitLoss> profitLossList;

    public YearProfitLossFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_year_profit_loss, container, false);

        recyclerView = view.findViewById(R.id.recycler);

        databaseHelper = new DatabaseHelper(getContext());

//        List<User> list = User.listAll(User.class);
//        for (User u : list){
//            String quuery = "SELECT        ClientID, strftime('%m', 'CBDate') + ' ' + strftime('%Y', 'CBDate') AS Month1, SUM(Income) AS Income, SUM(Expense) AS Expense, IFNULL(SUM(Income), 0) - IFNULL(SUM(Expense), 0) AS Profit, CAST(YEAR(CBDate)\n" +
//                    "                         AS Varchar(4)) + ' ' + CAST(MONTH(CBDate) AS Varchar(2)) AS Sorting\n" +
//                    "FROM            (SELECT        CashBook.ClientID, Account1Type.AcTypeName, CashBook.CBDate, 0 AS Income, SUM(CashBook.Amount) AS Expense\n" +
//                    "                          FROM            CashBook INNER JOIN\n" +
//                    "                                                    Account3Name ON CashBook.DebitAccount = Account3Name.AcNameID INNER JOIN\n" +
//                    "                                                    Account2Group ON Account3Name.AcGroupID = Account2Group.AcGroupID INNER JOIN\n" +
//                    "                                                    Account1Type ON Account2Group.AcTypeID = Account1Type.AcTypeID\n" +
//                    "                          GROUP BY Account1Type.AcTypeName, CashBook.CBDate, CashBook.ClientID\n" +
//                    "                          HAVING         (Account1Type.AcTypeName = 'Expense')\n" +
//                    "                          UNION ALL\n" +
//                    "                          SELECT        CashBook_1.ClientID, Account1Type_1.AcTypeName, CashBook_1.CBDate, SUM(CashBook_1.Amount) AS Income, 0 AS Expense\n" +
//                    "                          FROM            Account2Group AS Account2Group_1 INNER JOIN\n" +
//                    "                                                   Account3Name AS Account3Name_1 ON Account2Group_1.AcGroupID = Account3Name_1.AcGroupID INNER JOIN\n" +
//                    "                                                   Account1Type AS Account1Type_1 ON Account2Group_1.AcTypeID = Account1Type_1.AcTypeID INNER JOIN\n" +
//                    "                                                   CashBook AS CashBook_1 ON Account3Name_1.AcNameID = CashBook_1.CreditAccount\n" +
//                    "                          GROUP BY Account1Type_1.AcTypeName, CashBook_1.CBDate, CashBook_1.ClientID\n" +
//                    "                          HAVING        (Account1Type_1.AcTypeName = 'Revenue')) AS derivedtbl_1\n" +
//                    "GROUP BY ClientID, CAST(YEAR(CBDate) AS Varchar(4)) + ' ' + CAST(MONTH(CBDate) AS Varchar(2)), DATENAME(MM, CBDate) + ' ' + DATENAME(yyyy, CBDate)\n" +
//                    "HAVING        (ClientID = "+u.getClientID()+")";
//            profitLossList = databaseHelper.getProfitLoss(quuery);
//        }
//
//        for (ProfitLoss p : profitLossList){
//            String[] separated = p.getCBDate().split(" ");
//
//            if (m == 0) {
//                mList.add(ProfitLoss.createSection(separated[1]));
//                mList.add(ProfitLoss.createRow(p.getClientID(),p.getCBDate(),p.getIncome(),p.getExpense(),p.getProfit()));
//                m = Integer.valueOf(separated[1]);
//
//                income = Integer.valueOf(p.getIncome()) + income;
//                expense = Integer.valueOf(p.getExpense()) + expense;
//                profit = Integer.valueOf(p.getProfit()) + profit;
//                gIncome = Integer.valueOf(p.getIncome()) + gIncome;
//                gExpense = Integer.valueOf(p.getExpense()) + gExpense;
//                gProfit = Integer.valueOf(p.getProfit()) + gProfit;
//            }else if (m == Integer.valueOf(separated[1])){
//                income = Integer.valueOf(p.getIncome()) + income;
//                expense = Integer.valueOf(p.getExpense()) + expense;
//                profit = Integer.valueOf(p.getProfit()) + profit;
//                gIncome = Integer.valueOf(p.getIncome()) + gIncome;
//                gExpense = Integer.valueOf(p.getExpense()) + gExpense;
//                gProfit = Integer.valueOf(p.getProfit()) + gProfit;
//                mList.add(ProfitLoss.createRow(p.getClientID(),p.getCBDate(),p.getIncome(),p.getExpense(),p.getProfit()));
//            }else {
//                mList.add(ProfitLoss.createTotal(String.valueOf(income),String.valueOf(expense),String.valueOf(profit)));
//                income = 0; expense = 0; profit = 0;
//                income = Integer.valueOf(p.getIncome()) + income;
//                expense = Integer.valueOf(p.getExpense()) + expense;
//                profit = Integer.valueOf(p.getProfit()) + profit;
//                gIncome = Integer.valueOf(p.getIncome()) + gIncome;
//                gExpense = Integer.valueOf(p.getExpense()) + gExpense;
//                gProfit = Integer.valueOf(p.getProfit()) + gProfit;
//                mList.add(ProfitLoss.createSection(separated[1]));
//                mList.add(ProfitLoss.createRow(p.getClientID(),p.getCBDate(),p.getIncome(),p.getExpense(),p.getProfit()));
//                m = Integer.valueOf(separated[1]);
//            }
//        }
//
//        mList.add(ProfitLoss.createTotal(String.valueOf(income),String.valueOf(expense),String.valueOf(profit)));
//        mList.add(ProfitLoss.createSection("Grand Total"));
//        mList.add(ProfitLoss.createTotal(String.valueOf(gIncome),String.valueOf(gExpense),String.valueOf(gProfit)));
//        ProfitLossDateAdapter adapter = new ProfitLossDateAdapter(getContext(),mList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);

        getProfitLoss();

        return view;
    }

    public void getProfitLoss(){
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Loading");
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/ProfitLoss.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("ProfitLoss");
                            Log.e("SSSSS",jsonArray.toString());
                            String success = jsonObj.getString("success");
                            if (success.equals("1")){
//                                Account3Name.deleteAll(Account3Name.class);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery",jsonObject.toString());
                                    String ClientID = jsonObject.getString("ClientID");
                                    String CBDate = jsonObject.getString("Month1");
                                    String Income = jsonObject.getString("Income");
                                    String Expense = jsonObject.getString("Expense");
                                    String Profit = jsonObject.getString("Profit");
                                    String Sorting = jsonObject.getString("Sorting");

//                                    String pattern="yyyy-MM-dd";
//                                    DateFormat df = new SimpleDateFormat(pattern);
//                                    Date date = df.parse(CBDate);
//                                    DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
//                                    String cbDate = dff.format(date);
                                    String[] separated = CBDate.split(" ");

                                    if (m == 0) {
                                        mList.add(ProfitLoss.createSection(separated[1]));
                                        mList.add(ProfitLoss.createRow(ClientID,CBDate,Income,Expense,Profit));
                                        m = Integer.valueOf(separated[1]);

                                        income = Integer.valueOf(Income) + income;
                                        expense = Integer.valueOf(Expense) + expense;
                                        profit = Integer.valueOf(Profit) + profit;
                                        gIncome = Integer.valueOf(Income) + gIncome;
                                        gExpense = Integer.valueOf(Expense) + gExpense;
                                        gProfit = Integer.valueOf(Profit) + gProfit;
                                    }else if (m == Integer.valueOf(separated[1])){
                                        income = Integer.valueOf(Income) + income;
                                        expense = Integer.valueOf(Expense) + expense;
                                        profit = Integer.valueOf(Profit) + profit;
                                        gIncome = Integer.valueOf(Income) + gIncome;
                                        gExpense = Integer.valueOf(Expense) + gExpense;
                                        gProfit = Integer.valueOf(Profit) + gProfit;
                                        mList.add(ProfitLoss.createRow(ClientID,CBDate,Income,Expense,Profit));
                                    }else {
                                        mList.add(ProfitLoss.createTotal(String.valueOf(income),String.valueOf(expense),String.valueOf(profit)));
                                        income = 0; expense = 0; profit = 0;
                                        income = Integer.valueOf(Income) + income;
                                        expense = Integer.valueOf(Expense) + expense;
                                        profit = Integer.valueOf(Profit) + profit;
                                        gIncome = Integer.valueOf(Income) + gIncome;
                                        gExpense = Integer.valueOf(Expense) + gExpense;
                                        gProfit = Integer.valueOf(Profit) + gProfit;
                                        mList.add(ProfitLoss.createSection(separated[1]));
                                        mList.add(ProfitLoss.createRow(ClientID,CBDate,Income,Expense,Profit));
                                        m = Integer.valueOf(separated[1]);
                                    }
                                }

                                mList.add(ProfitLoss.createTotal(String.valueOf(income),String.valueOf(expense),String.valueOf(profit)));
                                mList.add(ProfitLoss.createSection("Grand Total"));
                                mList.add(ProfitLoss.createTotal(String.valueOf(gIncome),String.valueOf(gExpense),String.valueOf(gProfit)));
                                ProfitLossDateAdapter adapter = new ProfitLossDateAdapter(getContext(),mList);
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
                    params.put("Year", "year");
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
