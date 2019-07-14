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
Log.e("IDDD",projectID);
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

        Log.e("CREATVIEW","OK");

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
//            case R.id.login_btn:

//                oNumber = owner_numb.getText().toString();
//                uNumber = user_number.getText().toString();
//                mPassword = password.getText().toString();
//
//                if (!inputValidation.isInputEditTextFilled(owner_numb, owner_layout, getString(R.string.enter_owner_number))) {
//                    return;
//                }
//                if (!inputValidation.isInputEditTextFilled(user_number, user_layout, getString(R.string.enter_user_number))) {
//                    return;
//                }
//                if (!inputValidation.isInputEditTextFilled(password, password_layout, getString(R.string.enter_password))) {
//                    return;
//                }
//                else {
//                    mProgress = new ProgressDialog(getContext());
//                    mProgress.setTitle("Checking credentials");
//                    mProgress.setMessage("Please wait...");
//                    mProgress.setCanceledOnTouchOutside(false);
//                    mProgress.show();
//
//                    CBUpdate.deleteAll(CBUpdate.class);
//
//                    String tag_json_obj = "json_obj_req";
//                    String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/Login.php";
//
//                    StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//                                    Log.e("RES",response);
//                                    JSONObject jsonObj = null;
//
//                                    try {
//                                        jsonObj= new JSONObject(response);
//                                        JSONArray jsonArray = jsonObj.getJSONArray("UserInfo");
//                                        String success = jsonObj.getString("success");
//                                        String message = jsonObj.getString("message");
//                                        if (success.equals("1")){
//
//                                            User.deleteAll(User.class);
//                                            for (int i = 0; i < jsonArray.length(); i++) {
//                                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                                cId = jsonObject.getString("ClientID");
//                                                String cashID = jsonObject.getString("CashID");
//                                                String bookingIncomeID = jsonObject.getString("BookingIncomeID");
//                                                String bookingExpenseID = jsonObject.getString("BookingExpenseID");
//                                                String acNameID = jsonObject.getString("AcNameID");
//                                                String clientUserID = jsonObject.getString("ClientUserID");
//                                                String acName = jsonObject.getString("AcName");
//                                                String acAddress = jsonObject.getString("AcAddress");
//                                                String acContactNo = jsonObject.getString("AcContactNo");
//
//                                                User user = new User(cId,cashID,bookingIncomeID,bookingExpenseID,acNameID,clientUserID,acName);
//                                                user.save();
//                                            }
//
//                                            databaseHelper = new DatabaseHelper(getContext());
//
//                                            if (isConnected()) {
//                                                getAccount3Name();
//                                            }else {
//                                                Toast.makeText(getContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
//                                            }
//
//                                        }else {
//                                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            mProgress.dismiss();
//                            Log.e("Error",error.toString());
//                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
//                        }
//                    }){
//                        @Override
//                        protected Map<String, String> getParams() {
//                            Map<String, String> params = new HashMap<String, String>();
//                            Log.e("NUMBER",oNumber);
//                            params.put("logMobNum", uNumber);
//                            params.put("AcMobNum", oNumber);
//                            params.put("password", mPassword);
//                            return params;
//                        }
//                    };
//                    int socketTimeout = 10000;//10 seconds - change to what you want
//                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//                    jsonObjectRequest.setRetryPolicy(policy);
//                    AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
//                }

//                break;
            case R.id.create_account:
                if (isGpsEnabled()) {
                    Intent intent = new Intent(getContext(), MapsActivity.class);
                    startActivity(intent);
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

    public void getAccount3Name(){

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/test.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Account3Name");
                            String success = jsonObj.getString("success");
                            if (success.equals("1")){
                                databaseHelper.deleteAccount3Name();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Account3Name",jsonObject.toString());
                                    String AcNameID = jsonObject.getString("AcNameID");
                                    String AcName = jsonObject.getString("AcName");
                                    String AcGroupID = jsonObject.getString("AcGroupID");
                                    String AcAddress = jsonObject.getString("AcAddress");
                                    String AcMobileNo = jsonObject.getString("AcMobileNo");
                                    String AcContactNo = jsonObject.getString("AcContactNo");
                                    String AcEmailAddress = jsonObject.getString("AcEmailAddress");
                                    String AcDebitBal = jsonObject.getString("AcDebitBal");
                                    String AcCreditBal = jsonObject.getString("AcCreditBal");
                                    String AcPassward = jsonObject.getString("AcPassward");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String ClientUserID = jsonObject.getString("ClientUserID");
                                    String SysCode = jsonObject.getString("SysCode");
                                    String NetCode = jsonObject.getString("NetCode");
                                    String ed = jsonObject.getString("UpdatedDate");
                                    JSONObject jbb = new JSONObject(ed);
                                    String UpdatedDate = jbb.getString("date");
                                    String SerialNo = jsonObject.getString("SerialNo");
                                    String UserRights = jsonObject.getString("UserRights");
                                    String SecurityRights = jsonObject.getString("SecurityRights");
                                    String Salary = jsonObject.getString("Salary");
                                    String SessionDate = jsonObject.getString("SessionDate");

                                    databaseHelper.createAccount3Name(new Account3Name(AcNameID,AcName,AcGroupID,AcAddress,AcMobileNo,AcContactNo,AcEmailAddress,AcDebitBal,AcCreditBal,AcPassward,ClientID,ClientUserID,SysCode,NetCode,UpdatedDate,SerialNo,UserRights,SecurityRights,Salary));

                                    if (i == jsonArray.length() - 1) {
                                        TableSession.deleteAll(TableSession.class);
                                        TableSession session = new TableSession("Account3Name", AcNameID, SessionDate, SessionDate);
                                        session.save();
                                    }
                                }
                                getCashBook();

                            }else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mProgress.dismiss();
                Log.e("Error",error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", cId);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getCashBook(){

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/Test2.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("CashBook");
                            String success = jsonObj.getString("success");
                            if (success.equals("1")){
                                databaseHelper.deleteCashBook();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Account3Name",jsonObject.toString());
                                    String CashBookID = jsonObject.getString("CashBookID");
                                    String cb = jsonObject.getString("CBDate");
                                    JSONObject jbb = new JSONObject(cb);
                                    String CBDate = jbb.getString("date");
                                    SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = ss.parse(CBDate);
                                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                                    String CBDate1 = sf.format(date);
                                    String DebitAccount = jsonObject.getString("DebitAccount");
                                    String CreditAccount = jsonObject.getString("CreditAccount");
                                    String CBRemark = jsonObject.getString("CBRemarks");
                                    String Amount = jsonObject.getString("Amount");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String ClientUserID = jsonObject.getString("ClientUserID");
                                    String NetCode = jsonObject.getString("NetCode");
                                    String SysCode = jsonObject.getString("SysCode");
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
//                                    JSONObject jb = new JSONObject(ed);
//                                    String UpdatedDate = jb.getString("date");
                                    String BookingID = jsonObject.getString("BookingID");
                                    String SessionDate = jsonObject.getString("SessionDate");

                                    databaseHelper.createCashBook(new CashBook(CashBookID,CBDate1,DebitAccount,CreditAccount,CBRemark,Amount,ClientID,ClientUserID,NetCode,SysCode,UpdatedDate,BookingID));

                                    if (i == jsonArray.length() - 1) {
                                        TableSession session = new TableSession("CashBook", CashBookID, SessionDate, SessionDate);
                                        session.save();
                                    }

                                    Date da = new Date();
                                    SimpleDateFormat sff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String d = sff.format(da);
                                    if (i == jsonArray.length()-1) {

                                        UpdateDate.deleteAll(UpdateDate.class);
                                        UpdateDate updateDate = new UpdateDate(d, CashBookID);
                                        updateDate.save();
                                    }
                                }
//                                mProgress.dismiss();

                            }else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            getAccountGroups();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mProgress.dismiss();
                Log.e("Error",error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", cId);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getAccountGroups(){

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
                                databaseHelper.deleteAccount2Group();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery",jsonObject.toString());
                                    String AcGroupID = jsonObject.getString("AcGroupID");
                                    String AcTypeID = jsonObject.getString("AcTypeID");
                                    String AcGruopName = jsonObject.getString("AcGruopName");

                                    databaseHelper.createAccount2Group(new Account2Group(AcGroupID,AcTypeID,AcGruopName));
                                }

                                getBookings();
//
//                                mProgress.dismiss();
//                                getCashBook();

                            }else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mProgress.dismiss();
                Log.e("Error",error.toString());
//                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getBookings(){
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetBookings.php";


        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String text = "", BookingID = "", ClientName = "", ClientMobile = "", ClientAddress = "", ClientNic = "", EventName = "", BookingDate = "", EventDate = "",
                                ArrangePersons ="", ChargesTotal = "",Description = "", ClientID ="", ClientUserID = "", NetCode = "",SysCode = "", UpdatedDate = "";
                        try {
                            JSONObject json = new JSONObject(response);
                            String success = json.getString("success");
                            Log.e("Response",success);

                            if (success.equals("1")) {
                                JSONArray jsonArray = json.getJSONArray("Bookings");
                                Log.e("SSSS", jsonArray.toString());
                                databaseHelper.deleteBooking();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    BookingID = jsonObject.getString("BookingID");
                                    ClientName = jsonObject.getString("ClientName");
                                    ClientMobile = jsonObject.getString("ClientMobile");
                                    ClientAddress = jsonObject.getString("ClientAddress");
                                    ClientNic = jsonObject.getString("ClientNic");
                                    EventName = jsonObject.getString("EventName");
                                    String bd = jsonObject.getString("BookingDate");
                                    JSONObject jbbb = new JSONObject(bd);
                                    BookingDate = jbbb.getString("date");
                                    String ed = jsonObject.getString("EventDate");
                                    JSONObject jb = new JSONObject(ed);
                                    EventDate = jb.getString("date");
                                    Log.e("TEST",EventDate);
                                    ArrangePersons = jsonObject.getString("ArrangePersons");
                                    ChargesTotal = jsonObject.getString("ChargesTotal");
                                    Description = jsonObject.getString("Description");
                                    ClientID = jsonObject.getString("ClientID");
                                    ClientUserID = jsonObject.getString("ClientUserID");
                                    NetCode = jsonObject.getString("NetCode");
                                    SysCode = jsonObject.getString("SysCode");
                                    Log.e("TEST","TEST");
                                    String up = jsonObject.getString("UpdatedDate");
                                    JSONObject jbb = new JSONObject(up);
                                    UpdatedDate = jbb.getString("date");
                                    String SessionDate = jsonObject.getString("SessionDate");
                                    String Shift = jsonObject.getString("Shift");

                                    Log.e("TEST","SSSS");

                                    databaseHelper.createBooking(new Bookings(BookingID,ClientName,ClientMobile,ClientAddress,ClientNic,EventName,BookingDate,EventDate,ArrangePersons,ChargesTotal,Description,ClientID,ClientUserID,NetCode,SysCode,UpdatedDate,Shift));

                                    if (i == jsonArray.length() - 1) {
                                        TableSession session = new TableSession("Bookings", BookingID, SessionDate, SessionDate);
                                        session.save();
                                    }
                                }

//                                FetchFromDb();
//                                pDialog.dismiss();
                            }else {
//                                pDialog.dismiss();
                            }
                            getAccountTypes();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
//                pDialog.dismiss();
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", cId);
                return params;
            }
        };

        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getAccountTypes(){

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetAccountType.php";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RES",response);
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Account1Type");
                            String success = jsonObj.getString("success");
                            Log.e("Success",success);
                            if (success.equals("1")){
                                databaseHelper.deleteAccount1Type();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery",jsonObject.toString());
                                    String AcTypeID = jsonObject.getString("AcTypeID");
                                    String AcTypeName = jsonObject.getString("AcTypeName");

                                    databaseHelper.createAccount1Type(new Account1Type(AcTypeID,AcTypeName));
                                }

                            }else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            getItem1Type();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getItem1Type(){
        Log.e("GetItem1Type","OK");
        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetItem1Type.php";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("GetItem1Type",response);
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            String success = jsonObj.getString("success");
                            Log.e("Success",success);
                            if (success.equals("1")){
                                JSONArray jsonArray = jsonObj.getJSONArray("Item1Type");
                                databaseHelper.deleteAccount1Type();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("GetItem1Type",jsonObject.toString());
                                    String Item1TypeID = jsonObject.getString("Item1TypeID");
                                    String ItemType = jsonObject.getString("ItemType");

//                                    databaseHelper.createAccount1Type(new Account1Type(AcTypeID,AcTypeName));
                                }
                                getProject();
                            }else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getProject(){
        Log.e("GetProject","Ok");
        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetProject.php";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("GetProject",response);
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            String success = jsonObj.getString("success");
                            Log.e("Success",success);
                            if (success.equals("1")){
                                JSONArray jsonArray = jsonObj.getJSONArray("Project");
                                databaseHelper.deleteAccount1Type();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Project",jsonObject.toString());
                                    String ProjectID = jsonObject.getString("ProjectID");
                                    String ProjectName = jsonObject.getString("ProjectName");
//                                    String ProjectDescription = jsonObject.getString("ProjectDescription");
                                    String ProjectType = jsonObject.getString("ProjectType");

//                                    databaseHelper.createAccount1Type(new Account1Type(AcTypeID,AcTypeName));
                                }

                            }else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            getProjectMenu();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getProjectMenu(){
        Log.e("GetProjectMenu","OK");
        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetProjectMenu.php";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("GetProjectMenu",response);
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            String success = jsonObj.getString("success");
                            Log.e("ProjectMenu",success);
                            if (success.equals("1")){
                                JSONArray jsonArray = jsonObj.getJSONArray("ProjectMenu");
                                databaseHelper.deleteAccount1Type();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("ProjectMenu",jsonObject.toString());
                                    String MenuID = jsonObject.getString("MenuID");
                                    String ProjectID = jsonObject.getString("ProjectID");
                                    String MenuGroup = jsonObject.getString("MenuGroup");
                                    String MenuName = jsonObject.getString("MenuName");
                                    String PageOpen = jsonObject.getString("PageOpen");
                                    String ValuePass = jsonObject.getString("ValuePass");
                                    String ImageName = jsonObject.getString("ImageName");
                                    String SortBy = jsonObject.getString("SortBy");

//                                    databaseHelper.createAccount1Type(new Account1Type(AcTypeID,AcTypeName));
                                }

                            }else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            getAccount4UserRights();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getAccount4UserRights(){
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetAccount4UserRights.php";


        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject json = new JSONObject(response);
                            String success = json.getString("success");
                            Log.e("Success",success);

                            if (success.equals("1")) {
                                JSONArray jsonArray = json.getJSONArray("Account4UserRights");
                                Log.e("Account4UserRights", jsonArray.toString());
//                                databaseHelper.deleteBooking();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String AcRightsID = jsonObject.getString("AcRightsID");
                                    String Account3ID = jsonObject.getString("Account3ID");
                                    String MenuName = jsonObject.getString("MenuName");
                                    String PageOpen = jsonObject.getString("PageOpen");
                                    String ValuePass = jsonObject.getString("ValuePass");
                                    String ImageName = jsonObject.getString("ImageName");
                                    String Inserting = jsonObject.getString("Inserting");
                                    String Edting = jsonObject.getString("Edting");
                                    String Deleting = jsonObject.getString("Deleting");
                                    String Reporting = jsonObject.getString("Reporting");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String ClientUserID = jsonObject.getString("ClientUserID");
                                    String NetCode = jsonObject.getString("NetCode");
                                    String SysCode = jsonObject.getString("SysCode");
                                    String UpdateDate = jsonObject.getString("UpdateDate");
                                    String SortBy = jsonObject.getString("SortBy");

                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(log_in, "Yes");
                                    editor.apply();

                                    FragmentManager manager = getFragmentManager();
                                    assert manager != null;
                                    FragmentTransaction transaction = manager.beginTransaction();
                                    transaction.replace(R.id.mContainer, new MenuItemsFragment());
                                    transaction.commit();
                                    mProgress.dismiss();

                                }
                            }else {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(log_in, "Yes");
                                editor.apply();

                                FragmentManager manager = getFragmentManager();
                                assert manager != null;
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.mContainer, new MenuItemsFragment());
                                transaction.commit();
                                mProgress.dismiss();
//                                pDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
//                pDialog.dismiss();
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Account3ID", "1");
                return params;
            }
        };

        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
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
