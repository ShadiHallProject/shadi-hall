package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import org.by9steps.shadihall.activities.TrailBalanceActivity;
import org.by9steps.shadihall.adapters.ReportsAdapter;
import org.by9steps.shadihall.model.Account3Name;
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
public class TrailBalanceFragment extends Fragment implements View.OnClickListener {

    ProgressDialog mProgress;
    RecyclerView recyclerView;
    String currentDate;

    CardView trailBalance, plStatement, balanceSheet;


    public TrailBalanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trail_balance, container, false);


        trailBalance = view.findViewById(R.id.trail_balance);
        plStatement = view.findViewById(R.id.pl_statement);
        balanceSheet = view.findViewById(R.id.balance_sheet);

        trailBalance.setOnClickListener(this);
        plStatement.setOnClickListener(this);
        balanceSheet.setOnClickListener(this);

//        recyclerView = view.findViewById(R.id.recycler);

        Date date = new Date();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = curFormater.format(date);

//        getCashBook();

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.trail_balance:
                intent = new Intent(getContext(),TrailBalanceActivity.class);
                intent.putExtra("message","1");
                startActivity(intent);
                break;
            case R.id.pl_statement:
                intent = new Intent(getContext(),TrailBalanceActivity.class);
                intent.putExtra("message","2");
                startActivity(intent);
                break;
            case R.id.balance_sheet:
                intent = new Intent(getContext(),TrailBalanceActivity.class);
                intent.putExtra("message","3");
                startActivity(intent);
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getCashBook(){
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Loading");
        mProgress.setMessage("Please wait...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetAccountDetails.php";

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
//                                Account3Name.deleteAll(Account3Name.class);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery",jsonObject.toString());
                                    String AcTypeID = jsonObject.getString("AcTypeID");
                                    String AcTypeName = jsonObject.getString("AcTypeName");
                                    String AcGroupID = jsonObject.getString("AcGroupID");
                                    String AcGruopName = jsonObject.getString("AcGruopName");
                                    String AccountID = jsonObject.getString("AccountID");
                                    String AcName = jsonObject.getString("AcName");
                                    String Debit = jsonObject.getString("Debit");
                                    String Credit = jsonObject.getString("Credit");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String Bal = jsonObject.getString("Bal");
                                    String DebitBL = jsonObject.getString("DebitBL");
                                    String CreditBL = jsonObject.getString("CreditBL");
                                    String ed = jsonObject.getString("MaxDate");
                                    JSONObject jbb = new JSONObject(ed);
                                    String MaxDate = jbb.getString("date");

                                    Log.e("MAxDAte",MaxDate);
//                                    Account3Name account3Name = new Account3Name(AcTypeID,AcTypeName,AcGroupID,AcGruopName,AccountID,AcName,Debit,Credit,ClientID,MaxDate,Bal,DebitBL,CreditBL);
//                                    account3Name.save();


                                }

//                                List<Account3Name> mList = CashBook.listAll(Account3Name.class);
//                                ReportsAdapter adapter = new ReportsAdapter(getContext(),mList,ReportsAdapter.Trail_Balance,"");
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
