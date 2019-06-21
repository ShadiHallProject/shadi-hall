package org.by9steps.shadihall;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.by9steps.shadihall.activities.CashCollectionActivity;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.TableSession;
import org.by9steps.shadihall.model.UpdateDate;
import org.by9steps.shadihall.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static AppController mInstance;
    public static String profileType = "Edit";
    public static String addCB = "View";
    public static String date = "";
    public static String internet = "No";
    public static String fDate1 = "";
    public static String fDate2 = "";

    public static String imageUrl = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/ClientImages";

    DatabaseHelper databaseHelper;
    ProgressDialog mProgress;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    
    public static String stringDateFormate(String sFormate, String dFormate, String date){
        String mDate = null;
        SimpleDateFormat sf = new SimpleDateFormat(sFormate);
        try {
            Date dd = sf.parse(date);
            SimpleDateFormat ss = new SimpleDateFormat(dFormate);
            mDate = ss.format(dd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mDate;
    }

     public void refereshTables(Context context){
        databaseHelper = new DatabaseHelper(context);
        mProgress = new ProgressDialog(context);
        mProgress.setMessage("Loading...");
        mProgress.setCancelable(false);
        mProgress.show();
        getAccount3Name();

    }

    public void getAccount3Name(){
        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshAccount3Name.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            String success = jsonObj.getString("success");
                            Log.e("Sarem",jsonObj.toString());
                            if (success.equals("1")){
                                JSONArray jsonArray = jsonObj.getJSONArray("Account3Name");
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
                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Account3Name");
                                        for (TableSession s : se){
                                            s.setMaxID(AcNameID);
                                            s.setUpdateDate(SessionDate);
                                            s.save();
                                        }

                                    }

                                }

                            }else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }Log.e("SSSSS","SAREMS1");
                            updateAccount3Name();
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
                List<User> list = User.listAll(User.class);
                for (User u : list) {
                    params.put("ClientID", u.getClientID());
                    Log.e("SAREM",u.getClientID());
                }
                List<TableSession> tableSessions = TableSession.find(TableSession.class,"table_Name = ?","Account3Name");
                for (TableSession t : tableSessions){
                    params.put("MaxID",t.getMaxID());
                    Log.e("SAREM",t.getMaxID());
                }
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void updateAccount3Name(){
        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshAccount3Name.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            String success = jsonObj.getString("success");
                            if (success.equals("1")){
                                JSONArray jsonArray = jsonObj.getJSONArray("Account3Name");
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

                                    String query = "UPDATE Account3Name SET AcNameID = '"+AcNameID+"', AcName = '"+AcName+"', AcGroupID = '"+AcGroupID+"', AcAddress = '"+AcAddress+"', AcMobileNo = '"+AcMobileNo
                                            + "', AcContactNo = '"+AcContactNo+"', AcEmailAddress = '"+AcEmailAddress+"', AcDebitBal = '"+AcDebitBal+"', AcCreditBal = '"+AcCreditBal+"', AcPassward = '"+AcPassward
                                            + "', ClientID = '"+ClientID+"', ClientUserID = '"+ClientUserID+"', SysCode = '"+SysCode+"', NetCode = '"+NetCode+"', UpdatedDate = '"+UpdatedDate+"', SerialNo = '"+SerialNo
                                            +"', UserRights = '"+UserRights+"', SecurityRights = '"+SecurityRights+"', Salary '"+Salary+"' WHERE AcNameID = "+AcNameID;
                                    databaseHelper.updateAccount3Name(query);

                                    if (i == jsonArray.length() - 1) {
                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Account3Name");
                                        for (TableSession s : se){
                                            s.setMaxID(AcNameID);
                                            s.setUpdateDate(SessionDate);
                                            s.save();
                                        }
                                    }

                                }

                            }else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            getCashBook();
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
                List<User> list = User.listAll(User.class);
                for (User u : list) {
                    params.put("ClientID", u.getClientID());
                }
                List<TableSession> tableSessions = TableSession.find(TableSession.class,"table_Name = ?","Account3Name");
                for (TableSession t : tableSessions){
                    params.put("MaxID",t.getMaxID());
                    params.put("SessionDate",t.getUpdateDate());
                    Log.e("SAREM",t.getUpdateDate());
                }
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
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshCashBook.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            String success = jsonObj.getString("success");
                            if (success.equals("1")){
                                JSONArray jsonArray = jsonObj.getJSONArray("CashBook");
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
                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","CashBook");
                                        for (TableSession s : se){
                                            s.setMaxID(CashBookID);
                                            s.setUpdateDate(SessionDate);
                                            s.save();
                                        }
                                    }

                                }

                            }else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            updateCashBook();
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
                List<User> list = User.listAll(User.class);
                for (User u : list) {
                    params.put("ClientID", u.getClientID());
                }
                List<TableSession> tableSessions = TableSession.find(TableSession.class,"table_Name = ?","CashBook");
                for (TableSession t : tableSessions){
                    params.put("MaxID",t.getMaxID());
                }
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void updateCashBook(){

        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshCashBook.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            String success = jsonObj.getString("success");
                            if (success.equals("1")){
                                JSONArray jsonArray = jsonObj.getJSONArray("CashBook");
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

                                    String query = "UPDATE CashBook SET CBDate = '"+CBDate1+"', DebitAccount = '"+DebitAccount+"', CreditAccount = '"+CreditAccount+"', CBRemarks '"+CBRemark+"', Amount = '"+Amount+"', ClientID = '"+ClientID+"', ClientUserID = '"+ClientUserID+"', NetCode = '"+NetCode+"', SysCode = '"+SysCode+"', UpdatedDate = '"+UpdatedDate+"', BookingID = '"+BookingID+
                                            "' WHERE CashBookID = "+CashBookID;
                                    databaseHelper.updateCashBook(query);

                                    if (i == jsonArray.length() - 1) {
                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","CashBook");
                                        for (TableSession s : se){
                                            s.setMaxID(CashBookID);
                                            s.setUpdateDate(SessionDate);
                                            s.save();
                                        }
                                    }


                                }

                            }else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            getBookings();
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
                List<User> list = User.listAll(User.class);
                for (User u : list) {
                    params.put("ClientID", u.getClientID());
                }
                List<TableSession> tableSessions = TableSession.find(TableSession.class,"table_Name = ?","CashBook");
                for (TableSession t : tableSessions){
                    params.put("MaxID",t.getMaxID());
                    params.put("SessionDate",t.getUpdateDate());
                }
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getBookings(){
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshBooking.php";


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

                                    databaseHelper.createBooking(new Bookings(BookingID,ClientName,ClientMobile,ClientAddress,ClientNic,EventName,BookingDate,EventDate,ArrangePersons,ChargesTotal,Description,ClientID,ClientUserID,NetCode,SysCode,UpdatedDate));

                                    if (i == jsonArray.length() - 1) {
                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Bookings");
                                        for (TableSession s : se){
                                            s.setMaxID(BookingID);
                                            s.setUpdateDate(SessionDate);
                                            s.save();
                                        }
                                    }
                                }

//                                FetchFromDb();
//                                pDialog.dismiss();
                            }else {
//                                pDialog.dismiss();
                            }
                            updateBookings();
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
                List<User> list = User.listAll(User.class);
                for (User u : list) {
                    params.put("ClientID", u.getClientID());
                }
                List<TableSession> tableSessions = TableSession.find(TableSession.class,"table_Name = ?","CashBook");
                for (TableSession t : tableSessions){
                    params.put("MaxID",t.getMaxID());
                }
                return params;
            }
        };

        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void updateBookings(){
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/RefreshBooking.php";


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

                                    String query = "UPDATE Booking SET BookingID = '"+BookingID+"', ClientName = '"+ClientName+"', ClientMobile = '"+ClientMobile+"', ClientAddress = '"+ClientAddress+"', ClientNic = '"+ClientNic
                                            +"', EventName = '"+EventName+"', BookingDate = '"+BookingDate+"', EventDate = '"+EventDate+"', ArrangePersons '"+ArrangePersons+"', ChargesTotal = '"+ChargesTotal+"', Description = '"+Description
                                            +"', ClientID = '"+ClientID+"', ClientUserID '"+ClientUserID+"', NetCode = '"+NetCode+"', SysCode = '"+SysCode+"', UpdatedDate = '"+UpdatedDate+"' WHERE BookingID = "+ BookingID;
                                    databaseHelper.updateBooking(query);

                                    if (i == jsonArray.length() - 1) {
                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Bookings");
                                        for (TableSession s : se){
                                            s.setMaxID(BookingID);
                                            s.setUpdateDate(SessionDate);
                                            s.save();
                                        }
                                    }
                                }


//                                FetchFromDb();
//                                mProgress.dismiss();
                            }else {
//                                mProgress.dismiss();
                            }
                            addCashBook();
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
                List<User> list = User.listAll(User.class);
                for (User u : list) {
                    params.put("ClientID", u.getClientID());
                }
                List<TableSession> tableSessions = TableSession.find(TableSession.class,"table_Name = ?","CashBook");
                for (TableSession t : tableSessions){
                    params.put("MaxID",t.getMaxID());
                    params.put("SessionDate",t.getUpdateDate());
                }
                return params;
            }
        };

        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void addCashBook(){
        String query = "SELECT * FROM CashBook WHERE CashBookID = 0 AND UpdatedDate = 0";
        List<CashBook> addCashBook = databaseHelper.getCashBook(query);

        for (final CashBook c : addCashBook){
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/AddCashBook.php";

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("SuccessSS",success);
                                if (success.equals("1")){
                                    String id = jsonObject.getString("CBID");
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE CashBook SET CashBookID = '"+id+"', UpdatedDate = '"+UpdatedDate+"' WHERE ID = "+c.getcId());
//                                    Toast.makeText(CashCollectionActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error",error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    List<User> list = User.listAll(User.class);
                    for (User u : list) {
                        params.put("CBDate", c.getCBDate());
                        params.put("DebitAccount", c.getDebitAccount());
                        params.put("CreditAccount", c.getCreditAccount());
                        params.put("CBRemarks", c.getCBRemarks());
                        params.put("Amount", c.getAmount());
                        params.put("ClientID", u.getClientID());
                        params.put("ClientUserID", u.getClientUserID());
                        params.put("NetCode", "0");
                        params.put("SysCode", "0");
                        params.put("BookingID", c.getBookingID());
                    }
                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
        addCashBook2();
    }
    public void addCashBook2(){
        String query = "SELECT * FROM CashBook WHERE UpdatedDate = 0";
        List<CashBook> addCashBook = databaseHelper.getCashBook(query);

        for (final CashBook c : addCashBook){
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/UpdateCashBook.php";

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("Success",success);
                                if (success.equals("1")){
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE CashBook SET UpdatedDate = '"+UpdatedDate+"' WHERE ID = "+c.getcId());
//                                    Toast.makeText(CashCollectionActivity.this, message, Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    List<User> list = User.listAll(User.class);
                    for (User u : list) {
                        params.put("CBDate", c.getCBDate());
                        params.put("DebitAccount", c.getDebitAccount());
                        params.put("CreditAccount", c.getCreditAccount());
                        params.put("CBRemarks", c.getCBRemarks());
                        params.put("Amount", c.getAmount());
                        params.put("ClientID", u.getClientID());
                        params.put("ClientUserID", u.getClientUserID());
                        params.put("NetCode", "0");
                        params.put("SysCode", "0");
                        params.put("BookingID", c.getBookingID());
                    }
                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
        addBooking();
    }

    public void addBooking(){
        String query = "SELECT * FROM Booking WHERE BookingID = 0 AND UpdatedDate = 0";
        final List<Bookings> addBooking = databaseHelper.getBookings(query);
        Log.e("BookingID UP", String.valueOf(addBooking.size()));

        for (final Bookings c : addBooking){
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/AddEvent.php";

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response CB",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("Success CB",success);
                                if (success.equals("1")){
                                    String id = jsonObject.getString("BookingID");
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE Booking SET BookingID = '"+ id +"', UpdatedDate = '"+UpdatedDate+"' WHERE ID = "+c.getId());
//                                    Toast.makeText(CashCollectionActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            getCashBook();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgress.dismiss();
                    Log.e("Error",error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    List<User> list = User.listAll(User.class);
                    for (User u : list) {
                        params.put("ClientName", c.getClientName());
                        params.put("ClientMobile", c.getClientMobile());
                        params.put("ClientAddress", c.getClientAddress());
                        params.put("ClientNic", c.getClientNic());
                        params.put("EventName", c.getEventName());
                        params.put("BookingDate", c.getBookingDate());
                        params.put("EventDate", c.getEventDate());
                        params.put("ArrangePersons", c.getArrangePersons());
                        params.put("ChargesTotal", c.getChargesTotal());
                        params.put("Description", c.getDescription());
                        params.put("ClientID", u.getClientID());
                        params.put("ClientUserID", u.getClientUserID());
                        params.put("NetCode", "0");
                        params.put("SysCode", "0");
                        params.put("DebitAccount", u.getCashID());
                        params.put("CreditAccount", u.getBookingIncomeID());
                        params.put("Amount", c.getAmount());
                    }
                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
//        mProgress.dismiss();
//        updateBooking();
    }

    public void updateBooking(){
        String query = "SELECT * FROM Booking WHERE UpdatedDate = 0";
        final List<Bookings> addBooking = databaseHelper.getBookings(query);
        Log.e("BookingID UP", String.valueOf(addBooking.size()));

        for (final Bookings c : addBooking){
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/AddEvent.php";

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response CB",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("Success CB",success);
                                if (success.equals("1")){
                                    String id = jsonObject.getString("BookingID");
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE Booking SET UpdatedDate = '"+UpdatedDate+"' WHERE ID = "+c.getId());
//                                    Toast.makeText(CashCollectionActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            getCashBook();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgress.dismiss();
                    Log.e("Error",error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    List<User> list = User.listAll(User.class);
                    for (User u : list) {
                        params.put("ClientName", c.getClientName());
                        params.put("ClientMobile", c.getClientMobile());
                        params.put("ClientAddress", c.getClientAddress());
                        params.put("ClientNic", c.getClientNic());
                        params.put("EventName", c.getEventName());
                        params.put("BookingDate", c.getBookingDate());
                        params.put("EventDate", c.getEventDate());
                        params.put("ArrangePersons", c.getArrangePersons());
                        params.put("ChargesTotal", c.getChargesTotal());
                        params.put("Description", c.getDescription());
                        params.put("ClientID", u.getClientID());
                        params.put("ClientUserID", u.getClientUserID());
                        params.put("NetCode", "0");
                        params.put("SysCode", "0");
                        params.put("DebitAccount", u.getCashID());
                        params.put("CreditAccount", u.getBookingIncomeID());
                        params.put("Amount", c.getAmount());
                    }
                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
        //For Refresh Recycler
//        mProgress.dismiss();
    }

    public void addAccount3Name(){
        String query = "SELECT * FROM Account3Name WHERE AcNameID = 0 AND UpdatedDate = 0";
        final List<Account3Name> addBooking = databaseHelper.getAccount3Name(query);
        Log.e("BookingID UP", String.valueOf(addBooking.size()));

        for (final Account3Name c : addBooking){
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/AddCharofAcc.php";

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response CB",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("Success CB",success);
                                if (success.equals("1")){
                                    String id = jsonObject.getString("AcNameID");
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE Account3Name SET AcNameID = '"+ id +"', UpdatedDate = '"+UpdatedDate+"' WHERE ID = "+c.getId());

                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Account3Name");
                                        for (TableSession s : se){
                                            s.setMaxID(id);
                                            s.setUpdateDate(UpdatedDate);
                                            s.save();
                                        }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            getCashBook();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgress.dismiss();
                    Log.e("Error",error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    List<User> list = User.listAll(User.class);
                    for (User u : list) {
                        params.put("AcName", c.getAcName());
                        params.put("AcAddress", c.getAcAddress());
                        params.put("AcContactNo", c.getAcContactNo());
                        params.put("AcEmailAddress", c.getAcEmailAddress());
                        params.put("Salary", c.getSalary());
                        params.put("AcMobileNo", c.getAcMobileNo());
                        params.put("AcPassward", c.getAcPassward());
                        params.put("SecurityRights", c.getSecurityRights());
                        params.put("ClientID", u.getClientID());
                        params.put("AcGroupID", c.getAcGroupID());
                    }
                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
        mProgress.dismiss();
    }
}
