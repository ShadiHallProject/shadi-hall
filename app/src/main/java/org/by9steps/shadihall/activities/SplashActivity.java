package org.by9steps.shadihall.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.orm.SugarContext;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.Account1Type;
import org.by9steps.shadihall.model.Account2Group;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SplashActivity extends AppCompatActivity {

    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    String rationale = "Please provide location permission so that you can ...";
    Permissions.Options options = new Permissions.Options()
            .setRationaleDialogTitle("Info")
            .setSettingsDialogTitle("Warning");

    //shared prefrences
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String login = "loginKey";

    ImageView splash_logo;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SugarContext.init(this);

        databaseHelper = new DatabaseHelper(this);

        //shared prefrences
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        splash_logo = findViewById(R.id.splash_icon);
        final Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.blink);

        Permissions.check(this, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                splash_logo.startAnimation(animation);
                if (isConnected()) {
                    getAccount3Name();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String s = "No";

                        if(sharedPreferences.contains(login)){
                            s = sharedPreferences.getString(login,"");
                        }
                        if (s.equals("Yes")) {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }else{
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }

                    }
                },6600);
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                finish();
            }
        });
    }

    public void getAccount3Name(){
//        mProgress = new ProgressDialog(MainActivity.this);
//        mProgress.setTitle("Loading");
//        mProgress.setMessage("Please wait...");
//        mProgress.setCanceledOnTouchOutside(false);
//        mProgress.show();

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

                                    databaseHelper.createAccount3Name(new Account3Name(AcNameID,AcName,AcGroupID,AcAddress,AcMobileNo,AcContactNo,AcEmailAddress,AcDebitBal,AcCreditBal,AcPassward,ClientID,ClientUserID,SysCode,NetCode,UpdatedDate,SerialNo,UserRights,SecurityRights,Salary));
                                }
                                getCashBook();

                            }else {
                                String message = jsonObj.getString("message");
                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getCashBook(){
//        mProgress = new ProgressDialog(getContext());
//        mProgress.setTitle("Loading");
//        mProgress.setMessage("Please wait...");
//        mProgress.setCanceledOnTouchOutside(false);
//        mProgress.show();

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
                                    String CBRemarks = jsonObject.getString("CBRemarks");
                                    String Amount = jsonObject.getString("Amount");
                                    String ClientID = jsonObject.getString("ClientID");
                                    String ClientUserID = jsonObject.getString("ClientUserID");
                                    String NetCode = jsonObject.getString("NetCode");
                                    String SysCode = jsonObject.getString("SysCode");
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
//                                    JSONObject jb = new JSONObject(ed);
//                                    String UpdatedDate = jb.getString("date");
                                    String BookingID = jsonObject.getString("BookingID");

                                    databaseHelper.createCashBook(new CashBook(CashBookID,CBDate1,DebitAccount,CreditAccount,CBRemarks,Amount,ClientID,ClientUserID,NetCode,SysCode,UpdatedDate,BookingID));
                                }
                                getAccountGroups();
//                                mProgress.dismiss();

                            }else {
                                String message = jsonObj.getString("message");
                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
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
//                mProgress.dismiss();
                Log.e("Error",error.toString());
                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getAccountGroups(){
//        mProgress = new ProgressDialog(getContext());
//        mProgress.setTitle("Loading");
//        mProgress.setMessage("Please wait...");
//        mProgress.setCanceledOnTouchOutside(false);
//        mProgress.show();

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

//        pDialog = new ProgressDialog(getContext());
//        pDialog.setMessage("Searching...");
//        pDialog.setCancelable(false);
//        pDialog.show();

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
                                    Log.e("EVENTDATE",EventDate);
                                    ArrangePersons = jsonObject.getString("ArrangePersons");
                                    ChargesTotal = jsonObject.getString("ChargesTotal");
                                    Description = jsonObject.getString("Description");
                                    ClientID = jsonObject.getString("ClientID");
                                    ClientUserID = jsonObject.getString("ClientUserID");
                                    NetCode = jsonObject.getString("NetCode");
                                    SysCode = jsonObject.getString("SysCode");
                                    String up = jsonObject.getString("UpdatedDate");
                                    JSONObject jbb = new JSONObject(up);
                                    UpdatedDate = jbb.getString("date");

                                    Log.e("SSSS",ClientName + ClientID);

                                    databaseHelper.createBooking(new Bookings(BookingID,ClientName,ClientMobile,ClientAddress,ClientNic,EventName,BookingDate,EventDate,ArrangePersons,ChargesTotal,Description,ClientID,ClientUserID,NetCode,SysCode,UpdatedDate));
                                }
                                getAccountTypes();

//                                FetchFromDb();
//                                pDialog.dismiss();
                            }else {
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
                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getAccountTypes(){
//        mProgress.setTitle("Loading");
//        mProgress.setMessage("Please wait...");
//        mProgress.setCanceledOnTouchOutside(false);
//        mProgress.show();

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
                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.toString());
                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    //Check Internet Connection
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}
