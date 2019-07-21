package org.by9steps.shadihall.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import org.by9steps.shadihall.activities.LoginActivity;
import org.by9steps.shadihall.activities.MapsActivity;
import org.by9steps.shadihall.adapters.ShadiHallListAdapter;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.InputValidation;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.Account1Type;
import org.by9steps.shadihall.model.Account2Group;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.ActiveClients;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.ShadiHallList;
import org.by9steps.shadihall.model.TableSession;
import org.by9steps.shadihall.model.UpdateDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    TextInputEditText owner_numb, password;
    TextView user_number;
    TextInputLayout owner_layout, user_layout, password_layout;
    Button login, create_account, logOut;
    RecyclerView recyclerView;

    private ProgressDialog mProgress;
    private InputValidation inputValidation;
    List<ActiveClients> mList;
    Prefrence prefrence;

    String oNumber, uNumber, mPassword, ph;
    String cId;

    //shared prefrences
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String log_in = "loginKey";
    public static final String phone = "phoneKey";
    public static final String resume = "resume";

    DatabaseHelper databaseHelper;
    ShadiHallListAdapter adapter;

    //GPS Enable
    LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
//        owner_layout = view.findViewById(R.id.owner_layout);
//        owner_numb = view.findViewById(R.id.owner_mob);
        user_layout = view.findViewById(R.id.user_layout);
        user_number = view.findViewById(R.id.user_mob);
        password_layout = view.findViewById(R.id.password_layout);
        password = view.findViewById(R.id.password);
        login = view.findViewById(R.id.login_btn);
        create_account = view.findViewById(R.id.create_account);
        logOut = view.findViewById(R.id.log_out);
        recyclerView = view.findViewById(R.id.recycler);

        inputValidation = new InputValidation(getContext());
        databaseHelper = new DatabaseHelper(getContext());
        prefrence = new Prefrence(getContext());
        mList = new ArrayList<>();

//        login.setOnClickListener(this);
        create_account.setOnClickListener(this);
        logOut.setOnClickListener(this);

        //shared prefrences
        sharedPreferences = getActivity().getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(resume, "0");
        editor.apply();

        if(sharedPreferences.contains(phone)){
            ph = sharedPreferences.getString(phone,"");
            Log.e("PHONE",ph);
            user_number.setText(ph);
        }

        lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);

        if (isConnected()) {
            getShadiHallList();
        }else {
            String query = "SELECT * FROM ActiveAccounts";
            mList = databaseHelper.getActiveAccounts(query);
            adapter = new ShadiHallListAdapter(getContext(),mList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new ShadiHallListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String clientID, String clientUserID, String projectID, String userRights) {
                    prefrence.setClientIDSession(clientID);
                    prefrence.setClientUserIDSession(clientUserID);
                    prefrence.setProjectIDSession(projectID);
                    prefrence.setUserRighhtsSession(userRights);

                    FragmentManager manager = getFragmentManager();
                    assert manager != null;
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.mContainer, new MenuItemsFragment());
                    transaction.commit();
                }
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(sharedPreferences.contains(resume)){
            String res = sharedPreferences.getString(resume,"");
            assert res != null;
            if (res.equals("1")){
                getShadiHallList();
            }
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(resume, "0");
        editor.apply();
    }

    private void getShadiHallList(){
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Please wait...");
        mProgress.setMessage("Getting Your Number Details");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetShadiHallList.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RES",response);
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            String success = jsonObj.getString("success");
                            String message = jsonObj.getString("message");
                            if (success.equals("1")){
                                JSONArray jsonArray = jsonObj.getJSONArray("ShadiHall");
                                databaseHelper.deleteActiveAccounts();
                                mList.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String AcMobileNo = jsonObject.getString("AcMobileNo");
                                    String UserRights = jsonObject.getString("UserRights");
                                    String AcName = jsonObject.getString("AcName");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String CompanyName = jsonObject.getString("CompanyName");
                                    String CompanyAddress = jsonObject.getString("CompanyAddress");
                                    String ProjectID = jsonObject.getString("ProjectID");
                                    String ProjectName = jsonObject.getString("ProjectName");
                                    String ClientUserID = jsonObject.getString("ClientUserID");
                                    databaseHelper.createActiveAccounts(new ActiveClients(AcMobileNo, UserRights,
                                            AcName,
                                            ClientID,
                                            CompanyName,
                                            CompanyAddress,
                                            ProjectID,
                                            ProjectName,
                                            ClientUserID));
//                                    mList.add(new ShadiHallList(AcMobileNo,UserRights,AcName,ClientID,CompanyName,CompanyAddress,ProjectID,ProjectName));
                                }
                                String query = "SELECT * FROM ActiveAccounts";
                                mList = databaseHelper.getActiveAccounts(query);
                                adapter = new ShadiHallListAdapter(getContext(),mList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setAdapter(adapter);
                                adapter.setOnItemClickListener(new ShadiHallListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(String clientID, String clientUserID, String projectID, String userRights) {

                                        prefrence.setClientIDSession(clientID);
                                        prefrence.setClientUserIDSession(clientUserID);
                                        prefrence.setProjectIDSession(projectID);
                                        prefrence.setUserRighhtsSession(userRights);

                                        FragmentManager manager = getFragmentManager();
                                        assert manager != null;
                                        FragmentTransaction transaction = manager.beginTransaction();
                                        transaction.replace(R.id.mContainer, new MenuItemsFragment());
                                        transaction.commit();
                                    }
                                });
                                mProgress.dismiss();


                            }else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
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
                params.put("LoginMobileNo", user_number.getText().toString());
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_account:
                if (prefrence.getProjectIDSession().equals("0")){
                    Toast.makeText(getContext(), "Please Select Project First", Toast.LENGTH_SHORT).show();
                }else {
                    if (isGpsEnabled()) {
                        Intent intent = new Intent(getContext(), MapsActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.log_out:
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
                break;
        }
    }

    private Boolean isGpsEnabled(){
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage(R.string.gps_network_not_enabled);
            alertDialogBuilder.setPositiveButton(R.string.open_location_settings,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });

            alertDialogBuilder.setNegativeButton(R.string.Cancel,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return gps_enabled;
    }

    //Check Internet Connection
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}
