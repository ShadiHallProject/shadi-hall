package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import org.by9steps.shadihall.adapters.ProfitLossDateAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.BalSheet;
import org.by9steps.shadihall.model.ProfitLoss;
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
public class DateProfitLossFragment extends Fragment implements View.OnClickListener {

    ProgressDialog mProgress;
    ProfitLossDateAdapter adapter;

    String orderBy = "CBDate";
    int status = 0;
    String orderby = " ORDER BY " + orderBy + " DESC";
    TextView dpl_date, dpl_profit, dpl_expense, dpl_income;

    RecyclerView recyclerView;
    List<ProfitLoss> mList = new ArrayList<>();
    int m = 0, income, expense, profit;
    int gIncome, gExpense, gProfit;

    DatabaseHelper databaseHelper;
    List<ProfitLoss> profitLossList;

    List<ProfitLoss> filterdList;

//    EditText search;
    Spinner spinner;
    SearchView searchView;
    int filter;

    public DateProfitLossFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_date_profit_loss, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        searchView = view.findViewById(R.id.dpl_search);
        spinner = view.findViewById(R.id.dpl_spinner);

        dpl_date = view.findViewById(R.id.dpl_date);
        dpl_income = view.findViewById(R.id.dpl_income);
        dpl_expense = view.findViewById(R.id.dpl_expense);
        dpl_profit = view.findViewById(R.id.dpl_profit);

        dpl_date.setOnClickListener(this);
        dpl_income.setOnClickListener(this);
        dpl_expense.setOnClickListener(this);
        dpl_profit.setOnClickListener(this);

        databaseHelper = new DatabaseHelper(getContext());

        getProfitloss();

//        getProfitLoss();

        // Spinner Drop down elements
        List<String> spinner_list = new ArrayList<String>();
        spinner_list.add("Select");
        spinner_list.add("Date");
        spinner_list.add("Income");
        spinner_list.add("Expense");
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
                        searchView.setQuery("",false);
                        searchView.clearFocus();
                        break;
                    case 1:
                        filter = 1;
                        searchView.setQuery("",false);
                        searchView.clearFocus();
                        break;
                    case 2:
                        filter = 2;
                        searchView.setQuery("",false);
                        searchView.clearFocus();
                        break;
                    case 3:
                        filter = 3;
                        searchView.setQuery("",false);
                        searchView.clearFocus();
                        break;
                    case 4:
                        filter = 4;
                        searchView.setQuery("",false);
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

    public void getProfitloss(){

        List<User> list = User.listAll(User.class);
        for (User u : list){
            String query = "SELECT        ClientID, CBDate, SUM(Income) AS Income, SUM(Expense) AS Expense, IFNULL(SUM(Income), 0) - IFNULL(SUM(Expense), 0) AS Profit\n" +
                    "FROM            (SELECT        CashBook.ClientID, Account1Type.AcTypeName, CashBook.CBDate, 0 AS Income, SUM(CashBook.Amount) AS Expense\n" +
                    "                          FROM            CashBook INNER JOIN\n" +
                    "                                                    Account3Name ON CashBook.DebitAccount = Account3Name.AcNameID INNER JOIN\n" +
                    "                                                    Account2Group ON Account3Name.AcGroupID = Account2Group.AcGroupID INNER JOIN\n" +
                    "                                                    Account1Type ON Account2Group.AcTypeID = Account1Type.AcTypeID\n" +
                    "                          GROUP BY Account1Type.AcTypeName, CashBook.CBDate, CashBook.ClientID\n" +
                    "                          HAVING         (Account1Type.AcTypeName = 'Expense')\n" +
                    "                          UNION ALL\n" +
                    "                          SELECT        CashBook_1.ClientID, Account1Type_1.AcTypeName, CashBook_1.CBDate, SUM(CashBook_1.Amount) AS Income, 0 AS Expense\n" +
                    "                          FROM            Account2Group AS Account2Group_1 INNER JOIN\n" +
                    "                                                   Account3Name AS Account3Name_1 ON Account2Group_1.AcGroupID = Account3Name_1.AcGroupID INNER JOIN\n" +
                    "                                                   Account1Type AS Account1Type_1 ON Account2Group_1.AcTypeID = Account1Type_1.AcTypeID INNER JOIN\n" +
                    "                                                   CashBook AS CashBook_1 ON Account3Name_1.AcNameID = CashBook_1.CreditAccount\n" +
                    "                          GROUP BY Account1Type_1.AcTypeName, CashBook_1.CBDate, CashBook_1.ClientID\n" +
                    "                          HAVING        (Account1Type_1.AcTypeName = 'Revenue')) AS derivedtbl_1\n" +
                    "GROUP BY ClientID, CBDate\n" +
                    "HAVING        (ClientID = "+u.getClientID()+")"+ orderby;

            Log.e("PROFIT-LOSS QUERY",query);
            profitLossList = databaseHelper.getProfitLoss(query);
        }

        for (ProfitLoss p : profitLossList){
            String[] separated = p.getCBDate().split("-");

            if (m == 0) {
                mList.add(ProfitLoss.createSection(separated[1]+"/"+separated[2]));
                mList.add(ProfitLoss.createRow(p.getClientID(),p.getCBDate(),p.getIncome(),p.getExpense(),p.getProfit()));
                m = Integer.valueOf(separated[1]);

                income = Integer.valueOf(p.getIncome()) + income;
                expense = Integer.valueOf(p.getExpense()) + expense;
                profit = Integer.valueOf(p.getProfit()) + profit;
                gIncome = Integer.valueOf(p.getIncome()) + gIncome;
                gExpense = Integer.valueOf(p.getExpense()) + gExpense;
                gProfit = Integer.valueOf(p.getProfit()) + gProfit;
            }else if (m == Integer.valueOf(separated[1])){
                income = Integer.valueOf(p.getIncome()) + income;
                expense = Integer.valueOf(p.getExpense()) + expense;
                profit = Integer.valueOf(p.getProfit()) + profit;
                gIncome = Integer.valueOf(p.getIncome()) + gIncome;
                gExpense = Integer.valueOf(p.getExpense()) + gExpense;
                gProfit = Integer.valueOf(p.getProfit()) + gProfit;
                mList.add(ProfitLoss.createRow(p.getClientID(),p.getCBDate(),p.getIncome(),p.getExpense(),p.getProfit()));
            }else {
                mList.add(ProfitLoss.createTotal(String.valueOf(income),String.valueOf(expense),String.valueOf(profit)));
                income = 0; expense = 0; profit = 0;
                income = Integer.valueOf(p.getIncome()) + income;
                expense = Integer.valueOf(p.getExpense()) + expense;
                profit = Integer.valueOf(p.getProfit()) + profit;
                gIncome = Integer.valueOf(p.getIncome()) + gIncome;
                gExpense = Integer.valueOf(p.getExpense()) + gExpense;
                gProfit = Integer.valueOf(p.getProfit()) + gProfit;
                mList.add(ProfitLoss.createSection(separated[1]+"/"+separated[2]));
                mList.add(ProfitLoss.createRow(p.getClientID(),p.getCBDate(),p.getIncome(),p.getExpense(),p.getProfit()));
                m = Integer.valueOf(separated[1]);
            }
        }
        mList.add(ProfitLoss.createTotal(String.valueOf(income),String.valueOf(expense),String.valueOf(profit)));
        mList.add(ProfitLoss.createSection("Grand Total"));
        mList.add(ProfitLoss.createTotal(String.valueOf(gIncome),String.valueOf(gExpense),String.valueOf(gProfit)));
        adapter = new ProfitLossDateAdapter(getContext(),mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
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
                                    String CB_Date = jsonObject.getString("CBDate");
                                    JSONObject jbb = new JSONObject(CB_Date);
                                    String CBDate = jbb.getString("date");
                                    String Income = jsonObject.getString("Income");
                                    String Expense = jsonObject.getString("Expense");
                                    String Profit = jsonObject.getString("Profit");

                                    String pattern="yyyy-MM-dd";
                                    DateFormat df = new SimpleDateFormat(pattern);
                                    Date date = df.parse(CBDate);
                                    DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
                                    String cbDate = dff.format(date);
                                    String[] separated = cbDate.split("-");

                                    if (m == 0) {
                                        mList.add(ProfitLoss.createSection(separated[1]+"/"+separated[2]));
                                        mList.add(ProfitLoss.createRow(ClientID,cbDate,Income,Expense,Profit));
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
                                        mList.add(ProfitLoss.createRow(ClientID,cbDate,Income,Expense,Profit));
                                    }else {
                                        mList.add(ProfitLoss.createTotal(String.valueOf(income),String.valueOf(expense),String.valueOf(profit)));
                                        income = 0; expense = 0; profit = 0;
                                        income = Integer.valueOf(Income) + income;
                                        expense = Integer.valueOf(Expense) + expense;
                                        profit = Integer.valueOf(Profit) + profit;
                                        gIncome = Integer.valueOf(Income) + gIncome;
                                        gExpense = Integer.valueOf(Expense) + gExpense;
                                        gProfit = Integer.valueOf(Profit) + gProfit;
                                        mList.add(ProfitLoss.createSection(separated[1]+"/"+separated[2]));
                                        mList.add(ProfitLoss.createRow(ClientID,cbDate,Income,Expense,Profit));
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
                    params.put("Date", "1");
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
            for (ProfitLoss s : mList) {
                if (s.isRow()==1)
                switch (filter) {
                    case 1:
                        if (s.getCBDate().toLowerCase().contains(text.toLowerCase())) {
                            //adding the element to filtered list
                            filterdList.add(s);
                        }
                        break;
                    case 2:
                        if (s.getIncome().toLowerCase().contains(text.toLowerCase())) {
                            //adding the element to filtered list
                            filterdList.add(s);
                        }
                        break;
                    case 3:
                        if (s.getExpense().toLowerCase().contains(text.toLowerCase())) {
                            //adding the element to filtered list
                            filterdList.add(s);
                        }
                        break;
                    case 4:
                        if (s.getProfit().toLowerCase().contains(text.toLowerCase())) {
                            //adding the element to filtered list
                            filterdList.add(s);
                        }
                        break;
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
            case R.id.dpl_date:
                orderBy = "CBDate";
                orderBy(orderBy);
                break;
            case R.id.dpl_income:
                orderBy = "Income";
                orderBy(orderBy);
                break;
            case R.id.dpl_expense:
                orderBy = "Expense";
                orderBy(orderBy);
                break;
            case R.id.dpl_profit:
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
        getProfitloss();
    }
}
