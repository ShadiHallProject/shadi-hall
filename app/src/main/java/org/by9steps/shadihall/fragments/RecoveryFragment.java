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

    RecyclerView recyclerView;


    public RecoveryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recovery, container, false);
        recyclerView = view.findViewById(R.id.recycler);

        mList = new ArrayList<>();

        getRecoveries();

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

                                    mList.add(new Recovery(ClientID,BookingID,Recieved,Expensed,ChargesTotal,Balance,Profit,EventName,eventDate,ClientName));

                                }

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
