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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import org.by9steps.shadihall.adapters.BalSheetDateAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.BalSheet;
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
public class MonthBalSheetFragment extends Fragment implements View.OnClickListener {

    ProgressDialog mProgress;
    BalSheetDateAdapter adapter;

    String orderBy = "CBDate";
    TextView mbs_capital, mbs_month, mbs_profitloss, mbs_liabilities, mbs_cpl, mbs_assets;

    RecyclerView recyclerView;
    List<BalSheet> mList = new ArrayList<>();
    int m = 0, capital, profitLoss, liabilities, cpl, assets;
    int gCapital, gProfitLoss, gLiabilities, gCpl, gAssets;

    DatabaseHelper databaseHelper;
    List<BalSheet> balSheetList;

    List<BalSheet> filterdList;

//    EditText search;
    Spinner spinner;
    SearchView searchView;
    int filter;

    public MonthBalSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_month_bal_sheet, container, false);

        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.recycler);
        searchView = view.findViewById(R.id.mbs_search);
        spinner = view.findViewById(R.id.mbs_spinner);

        databaseHelper = new DatabaseHelper(getContext());

        mbs_month = view.findViewById(R.id.mbs_month);
        mbs_capital = view.findViewById(R.id.mbs_capital);
        mbs_profitloss = view.findViewById(R.id.mbs_profitloss);
        mbs_liabilities = view.findViewById(R.id.mbs_liabilities);
        mbs_cpl = view.findViewById(R.id.mbs_cpl);
        mbs_assets = view.findViewById(R.id.mbs_assets);

        mbs_month.setOnClickListener(this);
        mbs_capital.setOnClickListener(this);
        mbs_profitloss.setOnClickListener(this);
        mbs_liabilities.setOnClickListener(this);
        mbs_cpl.setOnClickListener(this);
        mbs_assets.setOnClickListener(this);

//        List<User> list = User.listAll(User.class);
//        for (User u : list){
//            String query = "SELECT        DATENAME(MM, CBDate) + N' ' + DATENAME(yyyy, CBDate) AS Month, SUM(Capital) AS Capital, SUM(Expense) + SUM(Revenue) AS ProfitLoss, SUM(Liabilities) AS Liabilities, SUM(Expense) + SUM(Revenue) + SUM(Capital)\n" +
//                    "                         + SUM(Liabilities) AS [C + P + L], SUM(Assets) AS Assets, ClientID, CAST(YEAR(CBDate) AS Varchar(4)) + N' ' + CAST(MONTH(CBDate) AS Varchar(2)) AS Sorting\n" +
//                    "FROM            (SELECT        derivedtbl_1.ClientID, derivedtbl_1.CBDate, Account1Type.AcTypeName, CAST(CASE WHEN AcTypeName = 'Expense' THEN (SUM(derivedtbl_1.Debit) - SUM(derivedtbl_1.Credit)) ELSE 0 END AS INT) AS Expense,\n" +
//                    "                                                    CAST(CASE WHEN AcTypeName = 'Revenue' THEN (SUM(derivedtbl_1.Debit) - SUM(derivedtbl_1.Credit)) ELSE 0 END AS INT) AS Revenue, CAST(CASE WHEN AcTypeName = 'Capital' THEN (SUM(derivedtbl_1.Debit)\n" +
//                    "                                                    - SUM(derivedtbl_1.Credit)) ELSE 0 END AS INT) AS Capital, CAST(CASE WHEN AcTypeName = 'Assets And Liability' THEN (CASE WHEN (SUM(Debit) - SUM(Credit)) > 0 THEN (SUM(Debit) - SUM(Credit)) ELSE 0 END)\n" +
//                    "                                                    ELSE 0 END AS INT) AS Assets, CAST(CASE WHEN AcTypeName = 'Assets And Liability' THEN (CASE WHEN (SUM(Debit) - SUM(Credit)) < 0 THEN (SUM(Debit) - SUM(Credit)) ELSE 0 END) ELSE 0 END AS INT)\n" +
//                    "                                                    AS Liabilities\n" +
//                    "                          FROM            (SELECT        CreditAccount AS AccountID, 0 AS Debit, Amount AS Credit, ClientID, CBDate\n" +
//                    "                                                    FROM            CashBook AS CashBook\n" +
//                    "                                                    WHERE        (ClientID = "+u.getClientID()+")\n" +
//                    "                                                    UNION ALL\n" +
//                    "                                                    SELECT        DebitAccount AS AccountID, Amount AS Debit, 0 AS Credit, ClientID, CBDate\n" +
//                    "                                                    FROM            CashBook AS CashBook_1\n" +
//                    "                                                    WHERE        (ClientID = "+u.getClientID()+")) AS derivedtbl_1 INNER JOIN\n" +
//                    "                                                    Account3Name ON derivedtbl_1.AccountID = Account3Name.AcNameID INNER JOIN\n" +
//                    "                                                    Account2Group ON Account3Name.AcGroupID = Account2Group.AcGroupID INNER JOIN\n" +
//                    "                                                    Account1Type ON Account2Group.AcTypeID = Account1Type.AcTypeID\n" +
//                    "                          GROUP BY derivedtbl_1.ClientID, Account1Type.AcTypeName, derivedtbl_1.CBDate) AS derivedtbl_2\n" +
//                    "GROUP BY ClientID, DATENAME(MM, CBDate) + N' ' + DATENAME(yyyy, CBDate), CAST(YEAR(CBDate) AS Varchar(4)) + N' ' + CAST(MONTH(CBDate) AS Varchar(2))";
//            balSheetList = databaseHelper.getBalSheet(query);
//        }
//
//        for (BalSheet b : balSheetList){
//            String[] separated = b.getCBDate().split(" ");
//
//            if (m == 0) {
//                mList.add(BalSheet.createSection(separated[1]));
//                mList.add(BalSheet.createRow(b.getCBDate(),b.getCapital(),b.getProfitLoss(),b.getLiabilities(),b.getC_P_L(),b.getAssets(),b.getClientID()));
//                m = Integer.valueOf(separated[1]);
//
//                capital = Integer.valueOf(b.getCapital()) + capital;
//                profitLoss = Integer.valueOf(b.getProfitLoss()) + profitLoss;
//                liabilities = Integer.valueOf(b.getLiabilities()) + liabilities;
//                cpl = Integer.valueOf(b.getC_P_L()) + cpl;
//                assets = Integer.valueOf(b.getAssets()) + assets;
//                gCapital = Integer.valueOf(b.getCapital()) + gCapital;
//                gProfitLoss = Integer.valueOf(b.getProfitLoss()) + gProfitLoss;
//                gLiabilities = Integer.valueOf(b.getLiabilities()) + gLiabilities;
//                gCpl = Integer.valueOf(b.getC_P_L()) + gCpl;
//                gAssets = Integer.valueOf(b.getAssets()) + gAssets;
//            }else if (m == Integer.valueOf(separated[1])){
//                capital = Integer.valueOf(b.getCapital()) + capital;
//                profitLoss = Integer.valueOf(b.getProfitLoss()) + profitLoss;
//                liabilities = Integer.valueOf(b.getLiabilities()) + liabilities;
//                cpl = Integer.valueOf(b.getC_P_L()) + cpl;
//                assets = Integer.valueOf(b.getAssets()) + assets;
//                gCapital = Integer.valueOf(b.getCapital()) + gCapital;
//                gProfitLoss = Integer.valueOf(b.getProfitLoss()) + gProfitLoss;
//                gLiabilities = Integer.valueOf(b.getLiabilities()) + gLiabilities;
//                gCpl = Integer.valueOf(b.getC_P_L()) + gCpl;
//                gAssets = Integer.valueOf(b.getAssets()) + gAssets;
//                mList.add(BalSheet.createRow(b.getCBDate(),b.getCapital(),b.getProfitLoss(),b.getLiabilities(),b.getC_P_L(),b.getAssets(),b.getClientID()));
//            }else {
//                mList.add(BalSheet.createTotal(String.valueOf(capital),String.valueOf(profitLoss),String.valueOf(liabilities),String.valueOf(cpl),String.valueOf(assets)));
//                capital = 0; profitLoss = 0; liabilities = 0; cpl = 0; assets = 0;
//                capital = Integer.valueOf(b.getCapital()) + capital;
//                profitLoss = Integer.valueOf(b.getProfitLoss()) + profitLoss;
//                liabilities = Integer.valueOf(b.getLiabilities()) + liabilities;
//                cpl = Integer.valueOf(b.getC_P_L()) + cpl;
//                assets = Integer.valueOf(b.getAssets()) + assets;
//                gCapital = Integer.valueOf(b.getCapital()) + gCapital;
//                gProfitLoss = Integer.valueOf(b.getProfitLoss()) + gProfitLoss;
//                gLiabilities = Integer.valueOf(b.getLiabilities()) + gLiabilities;
//                gCpl = Integer.valueOf(b.getC_P_L()) + gCpl;
//                gAssets = Integer.valueOf(b.getAssets()) + gAssets;
//                mList.add(BalSheet.createSection(separated[1]));
//                mList.add(BalSheet.createRow(b.getCBDate(),b.getCapital(),b.getProfitLoss(),b.getLiabilities(),b.getC_P_L(),b.getAssets(),b.getClientID()));
//                m = Integer.valueOf(separated[1]);
//            }
//        }
//
//        mList.add(BalSheet.createTotal(String.valueOf(capital),String.valueOf(profitLoss),String.valueOf(liabilities),String.valueOf(cpl),String.valueOf(assets)));
//        mList.add(BalSheet.createSection("Grand Total"));
//        mList.add(BalSheet.createTotal(String.valueOf(gCapital),String.valueOf(gProfitLoss),String.valueOf(gLiabilities),String.valueOf(gCpl),String.valueOf(gAssets)));
//        BalSheetDateAdapter adapter = new BalSheetDateAdapter(getContext(),mList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
        getBalSheet();

        // Spinner Drop down elements
        List<String> spinner_list = new ArrayList<String>();
        spinner_list.add("Select");
        spinner_list.add("Month");
        spinner_list.add("Capital");
        spinner_list.add("ProfitLoss");
        spinner_list.add("Liabilities");
        spinner_list.add("C+P+L");
        spinner_list.add("Assets");

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
                        break;
                    case 1:
                        filter = 1;
                        break;
                    case 2:
                        filter = 2;
                        break;
                    case 3:
                        filter = 3;
                        break;
                    case 4:
                        filter = 4;
                        break;
                    case 5:
                        filter = 5;
                        break;
                    case 6:
                        filter = 6;
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

    public void getBalSheet(){
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Loading");
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/BalSheet.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("BalSheet");
                            Log.e("SSSSS",jsonArray.toString());
                            String success = jsonObj.getString("success");
                            if (success.equals("1")){
//                                Account3Name.deleteAll(Account3Name.class);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery",jsonObject.toString());
                                    String CBDate = jsonObject.getString("Month");
                                    String Capital = jsonObject.getString("Capital");
                                    String ProfitLoss = jsonObject.getString("ProfitLoss");
                                    String Liabilities = jsonObject.getString("Liabilities");
                                    String C_P_L = jsonObject.getString("C + P + L");
                                    String Assets = jsonObject.getString("Assets");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String Sorting = jsonObject.getString("Sorting");

                                    String[] separated = CBDate.split(" ");

                                    if (m == 0) {
                                        mList.add(BalSheet.createSection(separated[1]));
                                        mList.add(BalSheet.createRow(CBDate,Capital,ProfitLoss,Liabilities,C_P_L,Assets,ClientID));
                                        m = Integer.valueOf(separated[1]);

                                        capital = Integer.valueOf(Capital) + capital;
                                        profitLoss = Integer.valueOf(ProfitLoss) + profitLoss;
                                        liabilities = Integer.valueOf(Liabilities) + liabilities;
                                        cpl = Integer.valueOf(C_P_L) + cpl;
                                        assets = Integer.valueOf(Assets) + assets;
                                        gCapital = Integer.valueOf(Capital) + gCapital;
                                        gProfitLoss = Integer.valueOf(ProfitLoss) + gProfitLoss;
                                        gLiabilities = Integer.valueOf(Liabilities) + gLiabilities;
                                        gCpl = Integer.valueOf(C_P_L) + gCpl;
                                        gAssets = Integer.valueOf(Assets) + gAssets;
                                    }else if (m == Integer.valueOf(separated[1])){
                                        capital = Integer.valueOf(Capital) + capital;
                                        profitLoss = Integer.valueOf(ProfitLoss) + profitLoss;
                                        liabilities = Integer.valueOf(Liabilities) + liabilities;
                                        cpl = Integer.valueOf(C_P_L) + cpl;
                                        assets = Integer.valueOf(Assets) + assets;
                                        gCapital = Integer.valueOf(Capital) + gCapital;
                                        gProfitLoss = Integer.valueOf(ProfitLoss) + gProfitLoss;
                                        gLiabilities = Integer.valueOf(Liabilities) + gLiabilities;
                                        gCpl = Integer.valueOf(C_P_L) + gCpl;
                                        gAssets = Integer.valueOf(Assets) + gAssets;
                                        mList.add(BalSheet.createRow(CBDate,Capital,ProfitLoss,Liabilities,C_P_L,Assets,ClientID));
                                    }else {
                                        mList.add(BalSheet.createTotal(String.valueOf(capital),String.valueOf(profitLoss),String.valueOf(liabilities),String.valueOf(cpl),String.valueOf(assets)));
                                        capital = 0; profitLoss = 0; liabilities = 0; cpl = 0; assets = 0;
                                        capital = Integer.valueOf(Capital) + capital;
                                        profitLoss = Integer.valueOf(ProfitLoss) + profitLoss;
                                        liabilities = Integer.valueOf(Liabilities) + liabilities;
                                        cpl = Integer.valueOf(C_P_L) + cpl;
                                        assets = Integer.valueOf(Assets) + assets;
                                        gCapital = Integer.valueOf(Capital) + gCapital;
                                        gProfitLoss = Integer.valueOf(ProfitLoss) + gProfitLoss;
                                        gLiabilities = Integer.valueOf(Liabilities) + gLiabilities;
                                        gCpl = Integer.valueOf(C_P_L) + gCpl;
                                        gAssets = Integer.valueOf(Assets) + gAssets;
                                        mList.add(BalSheet.createSection(separated[1]));
                                        mList.add(BalSheet.createRow(CBDate,Capital,ProfitLoss,Liabilities,C_P_L,Assets,ClientID));
                                        m = Integer.valueOf(separated[1]);
                                    }
                                }
                                mList.add(BalSheet.createTotal(String.valueOf(capital),String.valueOf(profitLoss),String.valueOf(liabilities),String.valueOf(cpl),String.valueOf(assets)));
                                mList.add(BalSheet.createSection("Grand Total"));
                                mList.add(BalSheet.createTotal(String.valueOf(gCapital),String.valueOf(gProfitLoss),String.valueOf(gLiabilities),String.valueOf(gCpl),String.valueOf(gAssets)));
                                adapter = new BalSheetDateAdapter(getContext(),mList);
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
                    params.put("Month", "month");
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
            for (BalSheet s : mList) {
                if (s.isRow() == 1)
                    switch (filter) {
                        case 1:
                            if (s.getCBDate().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 2:
                            if (s.getCapital().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 3:
                            if (s.getProfitLoss().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 4:
                            if (s.getLiabilities().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 5:
                            if (s.getC_P_L().toLowerCase().contains(text.toLowerCase())) {
                                //adding the element to filtered list
                                filterdList.add(s);
                            }
                            break;
                        case 6:
                            if (s.getAssets().toLowerCase().contains(text.toLowerCase())) {
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
            case R.id.mbs_month:
                orderBy = "CBDate";
//                getCashBook();
                break;
            case R.id.mbs_capital:
                orderBy = "Capital";
//                getCashBook();
                break;
            case R.id.mbs_profitloss:
                orderBy = "ProfitLoss";
//                getCashBook();
                break;
            case R.id.mbs_liabilities:
                orderBy = "Liabilities";
//                getCashBook();
                break;
            case R.id.mbs_cpl:
                orderBy = "C_P_L";
//                getCashBook();
                break;
            case R.id.mbs_assets:
                orderBy = "Assets";
//                getCashBook();
                break;
        }
    }

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
//            try {
//                createPdf();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (DocumentException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
