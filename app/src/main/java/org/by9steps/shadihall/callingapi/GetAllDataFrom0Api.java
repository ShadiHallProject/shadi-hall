package org.by9steps.shadihall.callingapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.helper.ApiRefStrings;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.Account1Type;
import org.by9steps.shadihall.model.Account2Group;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.Item1Type;
import org.by9steps.shadihall.model.Item2Group;
import org.by9steps.shadihall.model.ProjectMenu;
import org.by9steps.shadihall.model.item3name.Item3Name;
import org.by9steps.shadihall.model.item3name.Item3Name_;
import org.by9steps.shadihall.model.salepur1data.SalePur1Data;
import org.by9steps.shadihall.model.salepur1data.Salepur1;
import org.by9steps.shadihall.model.salepur2data.SalePur2;
import org.by9steps.shadihall.model.salepur2data.SalePur2Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.by9steps.shadihall.helper.ApiRefStrings.ACCOUNT1TYPE_GET_ALL_DATA;
import static org.by9steps.shadihall.helper.ApiRefStrings.Account3NameGetDataFromServer;
import static org.by9steps.shadihall.helper.ApiRefStrings.BOOKING_GET_ALL_DATA_FROM_SERVER;

public class GetAllDataFrom0Api {

    private Context mCtx;
    private ProgressDialog mProgress;
    private DatabaseHelper databaseHelper;
    Prefrence prefrence;
    public StringBuilder finalreportallmethod=new StringBuilder();

    ////////////////////////Interface For lisening data
    public interface EachApiListner {
        void apifinished(String code, String message, String methodname);
    }

    public GetAllDataFrom0Api(Context mCtx, ProgressDialog mProgress, DatabaseHelper databaseHelper, Prefrence prefrence) {
        this.mCtx = mCtx;
        this.mProgress = mProgress;
        this.databaseHelper = databaseHelper;
        this.prefrence = prefrence;
    }

//    private void login(final String mClientID, final String mClientUserID, final String mPassword) {
//        ////////mProgress.setMessage("Please wait...login");
//        if (sharedPreferences.contains(phone)) {
//            ph = sharedPreferences.getString(phone, "");
//        }
//        mProgress = new ProgressDialog(mCtx);
//        mProgress.setTitle("Checking credentials");
//        ////////mProgress.setMessage("Please wait...");
//        mProgress.setCanceledOnTouchOutside(false);
//        mProgress.show();
//
////        CBUpdate.deleteAll(CBUpdate.class);
//
//        String tag_json_obj = "json_obj_req";
//        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/Login.php";
//
//        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("ClientLoginRES", response);
//                        JSONObject jsonObj = null;
//
//                        try {
//                            jsonObj = new JSONObject(response);
//                            String success = jsonObj.getString("success");
//                            String message = jsonObj.getString("message");
//                            if (success.equals("1")) {
//                                JSONArray jsonArray = jsonObj.getJSONArray("UserInfo");
//                                Log.e("ClientLoginUserInfo", jsonArray.toString());
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                    String cashID = jsonObject.getString("CashID");
//                                    String bookingIncomeID = jsonObject.getString("BookingIncomeID");
//                                    String bookingExpenseID = jsonObject.getString("BookingExpenseID");
//                                    String acNameID = "0";
//                                    String acName = "ss";
//                                    cId = jsonObject.getString("ClientID");
//                                    String ClientParentID = jsonObject.getString("ClientParentID");
//                                    String EntryType = jsonObject.getString("EntryType");
//                                    String LoginMobileNo = jsonObject.getString("LoginMobileNo");
//                                    String CompanyName = jsonObject.getString("CompanyName");
//                                    String CompanyAddress = jsonObject.getString("CompanyAddress");
//                                    String CompanyNumber = jsonObject.getString("CompanyNumber");
//                                    String NameOfPerson = jsonObject.getString("NameOfPerson");
//                                    String Email = jsonObject.getString("Email");
//                                    String WebSite = jsonObject.getString("WebSite");
//                                    String Password = jsonObject.getString("Password");
//                                    String ActiveClient = jsonObject.getString("ActiveClient");
//                                    String Country = jsonObject.getString("Country");
//                                    String City = jsonObject.getString("City");
//                                    String SubCity = jsonObject.getString("SubCity");
//                                    String CapacityOfPersons = jsonObject.getString("CapacityOfPersons");
//                                    String ClientUserID = jsonObject.getString("ClientUserID");
//                                    String SysCode = jsonObject.getString("SysCode");
//                                    String NetCode = jsonObject.getString("NetCode");
//                                    String up = jsonObject.getString("UpdatedDate");
//                                    JSONObject jb = new JSONObject(up);
//                                    String UpdatedDate = jb.getString("date");
//                                    String Lat = jsonObject.getString("Lat");
//                                    String Lng = jsonObject.getString("Lng");
//                                    String ProjectID = jsonObject.getString("ProjectID");
//
//                                    databaseHelper.createClient(new Client(cId, ClientParentID, EntryType, LoginMobileNo, CompanyName,
//                                            CompanyAddress, CompanyNumber, NameOfPerson, Email, WebSite, Password, ActiveClient,
//                                            Country, City, SubCity, CapacityOfPersons, ClientUserID, SysCode, NetCode, UpdatedDate,
//                                            Lat, Lng, ProjectID));
//
////                                    User user = new User(cId,cashID,bookingIncomeID,bookingExpenseID,acNameID,ClientUserID,acName);
////                                    user.save();
//                                }
//
//                                if (isConnected()) {
//                                    getCashBook();
//                                } else {
//                                    Toast.makeText(mCtx, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
//                                }
//
//                            } else {
//                                mProgress.dismiss();
//                                Toast.makeText(mCtx, message, Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mProgress.dismiss();
//                Log.e("Error", error.toString());
//                Toast.makeText(mCtx, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                Log.e("ClientLogin","ClientID:"+mClientID+" ClientUserID:"
//                        +mClientUserID+" Password:"+mPassword);
//                params.put("ClientID", mClientID);
//                params.put("ClientUserID", mClientUserID);
//                params.put("Password", mPassword);
//                return params;
//            }
//        };
//        int socketTimeout = 10000;//10 seconds - change to what you want
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        jsonObjectRequest.setRetryPolicy(policy);
//        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
//    }

    public void getCashBook(String clientID, final EachApiListner listner) {
        ////////mProgress.setMessage("Please wait...getCashBook");
        CashBookApis apis = new CashBookApis(mCtx, mProgress, databaseHelper, prefrence);
        apis.GetAllDataFromCashBook(clientID, new CashBookApis.CashBookApiListener() {
            @Override
            public void FinishCashBookCallBackMethod(String success, String funType) {
                Log.e("cashbook", "Data Inserted FunType:" + funType);
                listner.apifinished(success, funType, "getCashBook");
            }
        });

    }

    public void getAccount3Name(final String clientID, final EachApiListner listner) {
        ////////mProgress.setMessage("Please wait...getAccount3Name");
        String tag_json_obj = "json_obj_req";
        String u = Account3NameGetDataFromServer;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        Log.e("Account3Name ", response);
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Account3Name");
                            String success = jsonObj.getString("success");
                            String message = "not defined";
                            if (success.equals("1")) {
                                ////////databaseHelper.delete
                                deletedata("Account3Name", clientID);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Account3Name", jsonObject.toString());
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

                                    databaseHelper.createAccount3Name(new Account3Name(AcNameID, AcName, AcGroupID, AcAddress, AcMobileNo, AcContactNo, AcEmailAddress, AcDebitBal, AcCreditBal, AcPassward, ClientID, ClientUserID, SysCode, NetCode, UpdatedDate, SerialNo, UserRights, SecurityRights, Salary));

//                                    if (i == jsonArray.length() - 1) {
//                                        TableSession.deleteAll(TableSession.class);
//                                        TableSession session = new TableSession("Account3Name", AcNameID, SessionDate, SessionDate);
//                                        session.save();
//                                    }
                                }

                            } else {
                                String message2 = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            listner.apifinished(success, message, "getAccount3Name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listner.apifinished("0", e.getMessage(), "getAccount3Name");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", clientID);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getAccountGroups(final EachApiListner listner) {
        //////////mProgress.setMessage("Please wait...getAccountGroups");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.ACCOUNT2GROUP_GET_ALL_DATA;

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("RES", response);
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Account2Group");
                            String success = jsonObj.getString("success");
                            // String message = jsonObj.getString("message");
                            Log.e("Success", success);
                            if (success.equals("1")) {
                                //////databaseHelper.delete
                                deletedata("Account2Group", null);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery", jsonObject.toString());
                                    String AcGroupID = jsonObject.getString("AcGroupID");
                                    String AcTypeID = jsonObject.getString("AcTypeID");
                                    String AcGruopName = jsonObject.getString("AcGruopName");

                                    databaseHelper.createAccount2Group(new Account2Group(AcGroupID, AcTypeID, AcGruopName));
                                }

                            } else {
                                String message2 = jsonObj.getString("message");
//                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            }
                            listner.apifinished(success, "novalue", "getAccountGroups");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listner.apifinished("0", e.getMessage(), "getAccountGroups");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getAccountTypes(final EachApiListner listner) {
        //////////mProgress.setMessage("Please wait...getAccountTypes");
        String tag_json_obj = "json_obj_req";
        String u = ACCOUNT1TYPE_GET_ALL_DATA;

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Account1Type", response);
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            // String message = jsonObj.getString("message");
                            jsonObj = new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Account1Type");
                            String success = jsonObj.getString("success");
                            Log.e("Success", success);
                            if (success.equals("1")) {
                                //////databaseHelper.delete
                                deletedata("Account1Type", null);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Recovery", jsonObject.toString());
                                    String AcTypeID = jsonObject.getString("AcTypeID");
                                    String AcTypeName = jsonObject.getString("AcTypeName");

                                    databaseHelper.createAccount1Type(new Account1Type(AcTypeID, AcTypeName));
                                }
                                //getItem1Type();
                            } else {
                                String message2 = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            listner.apifinished(success, "novalue", "getAccountTypes");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listner.apifinished("0", e.getMessage(), "getAccountTypes");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getItem1Type(final EachApiListner listner) {
        //////////mProgress.setMessage("Please wait...getItem1Type");

        Log.e("GetItem1Type", "OK");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetItem1TypeLoc;
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("GetItem1Type", response);
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                           // String message = jsonObj.getString("message");

                            jsonObj = new JSONObject(response);
                            String success = jsonObj.getString("success");
                            Log.e("Success", success);
                            if (success.equals("1")) {
                                //////databaseHelper.delete
                                deletedata("Item1Type", null);
                                JSONArray jsonArray = jsonObj.getJSONArray("Item1Type");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("GetItem1Type", jsonObject.toString());
                                    Item1Type item1Type = new Item1Type();
                                    item1Type.setItem1TypeID(jsonObject.getString("Item1TypeID"));
                                    item1Type.setItemType(jsonObject.getString("ItemType"));
                                    long idd = refdb.TableItem1.AddItem1Type(databaseHelper, item1Type);
                                    Log.e("GetItem1Type", "On Add ITem " + idd);
                                }
                                //getItem2Group();

                            } else {
                                String message2 = jsonObj.getString("message");
                                GenericConstants.ShowDebugModeDialog(mCtx, "Error item1type",
                                        message2);
                            }

                            listner.apifinished(success, "novalue", "getItem1Type");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listner.apifinished("0", e.getMessage(), "getItem1Type");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getItem2Group(final String clientID, final EachApiListner listner) {
        final String TAG = "getItem2Group";
        ////////mProgress.setMessage("Please wait...getItem2Group");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetItem2GorupLoc;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            //String message = jsonObj.getString("message");
                            JSONArray jsonArray = jsonObj.getJSONArray("Item2Group");
                            Log.e(TAG, response);
                            String success = jsonObj.getString("success");
                            if (success.equals("1")) {
                                //////databaseHelper.delete
                                deletedata("Item2Group", clientID);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e(TAG, jsonObject.toString());
                                    Item2Group item2Group = new Item2Group();
                                    //item2Group.setClientID(jsonObject.getString("ID"));
                                    item2Group.setItem2GroupID(jsonObject.getString("Item2GroupID"));
                                    item2Group.setItem1TypeID(jsonObject.getString("Item1TypeID"));
                                    item2Group.setItem2GroupName(jsonObject.getString("Item2GroupName"));
                                    item2Group.setClientUserID(jsonObject.getString("ClientUserID"));
                                    item2Group.setClientID(jsonObject.getString("ClientID"));
                                    item2Group.setSysCode(jsonObject.getString("SysCode"));
                                    item2Group.setNetCode(jsonObject.getString("NetCode"));
                                    JSONObject dateup = jsonObject.getJSONObject("UpdatedDate");
                                    item2Group.setUpdatedDate(dateup.getString("date"));
                                    long idd = refdb.Table2Group.AddItem2Group(databaseHelper, item2Group);
                                    Log.e(TAG, "Item2Group InsertID " + idd);
                                }
                                //getPrfojectMenu();

                            } else {
                                //getPrfojectMenu();
                                String message2 = jsonObj.getString("message");
                                Log.e(TAG, message2);

//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            listner.apifinished(success, "novalue", "getItem2Group");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listner.apifinished("0", e.getMessage(), "getItem2Group");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
//                mProgress.dismiss();
                GenericConstants.ShowDebugModeDialog(mCtx,
                        "Error Item2Group", e.getMessage());
                Log.e("Item2Group", e.getMessage());
                e.printStackTrace();
                Log.e("Error", e.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", clientID);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        // getItem3Name();
    }

    public void getItem3Name(final String clientID, final EachApiListner listner) {
        final String TAG = "getitem3Name";
        ////////mProgress.setMessage("Please wait...getItem3Name");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetItem3NameLoc;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            Item3Name item3 = gson.fromJson(response, Item3Name.class);
                            if (item3.getSuccess() != 0) {
                                //////databaseHelper.delete
                                deletedata("Item3Name", clientID);
                                for (Item3Name_ name : item3.getItem3Name()) {
                                    Log.e(TAG, " " + name.getClientID() + " --" + name.getUpdatedDate().getDate());
                                    long idd = refdb.Table3Name.AddItem3Name(databaseHelper, name);
                                    Log.e(TAG, "inser id" + idd);
                                }
                                Log.e(TAG, "onResponse: " + item3.getItem3Name().toString());


                            } else {
                                MNotificationClass.ShowToast(mCtx, "No Data Found in Item3Name");
                            }
                            listner.apifinished(item3.getSuccess() + "", "item3name", "getItem3Name");
                        }

                        Log.e(TAG, "onResponse: " + response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GenericConstants.ShowDebugModeDialog(mCtx, "Error item3nam", error.getMessage());
//                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", clientID);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        //getSalePur1Data();
    }

    public void getSalePur1Data(final String clientID, final EachApiListner listner) {
        final String TAG = "getSalePur1Data";
        ////////mProgress.setMessage("Please wait...getSalePur1DAta");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetSalePur1Data;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            SalePur1Data salePur1Data = gson.fromJson(response, SalePur1Data.class);
                            if (salePur1Data.getSuccess() != 0) {
                                //////databaseHelper.delete
                                deletedata("SalePur1", clientID);
                                for (Salepur1 salepur1 : salePur1Data.getSalepur1()) {
                                    Log.e("getSalePur1Data", " " + salepur1.getClientID() + " --" + salepur1.getUpdatedDate().getDate());
                                    long idd = refdb.SlePur1.AddItemSalePur1(databaseHelper, salepur1);
                                    Log.e("getSalePur1Data", "inser id" + idd);
                                }
                                Log.e("getSalePur1Data", "onResponse: " + salePur1Data.getSalepur1());


                            } else {
                                MNotificationClass.ShowToast(mCtx, "No Data Found in SalePur1Table");
                            }
                            listner.apifinished(salePur1Data.getSuccess() + ""
                                    , "Gsono salepur1", "getSalePur1Data");
                        }

                        Log.e("getSalePur1Data", "onResponse: " + response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GenericConstants.ShowDebugModeDialog(mCtx, "Error salepur1", error.getMessage());
//                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", clientID);
                //params.put("EntryType","Sales");
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        //getSalePur2Data();
    }

    public void getSalePur2Data(final String clientID, final EachApiListner listner) {
        final String TAG = "getSalePur2Data";
        ////////mProgress.setMessage("Please wait...getSalePur2Data");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetSalePur2Data;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            SalePur2Data salePur2Data = gson.fromJson(response, SalePur2Data.class);
                            if (salePur2Data.getSuccess() != 0) {
                                //////databaseHelper.delete
                                deletedata("SalePur2", clientID);
                                for (SalePur2 salePur2 : salePur2Data.getSalePur2()) {
                                    Log.e("getSalePur2Data", " " + salePur2.getClientID() + " --" + salePur2.getUpdatedDate().getDate());
                                    long idd = refdb.SalePur2.AddItemSalePur2(databaseHelper, salePur2);
                                    Log.e("getSalePur2Data", "inser id" + idd);
                                }
                                Log.e("getSalePur2Data", "onResponse: " + salePur2Data.getSalePur2());


                            } else {
                                MNotificationClass.ShowToast(mCtx, "No Data Found in SalePur1Table");
                            }
                            listner.apifinished(salePur2Data.getSuccess() + "",
                                    salePur2Data.getMessage(), "SalePur2Data");
                        }

                        Log.e("getSalePur2Data", "onResponse: " + response);

                        // getProjectMenu();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GenericConstants.ShowDebugModeDialog(mCtx, "Error SalePur2", error.getMessage());
//                mProgress.dismiss();
                //  getProjectMenu();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", clientID);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }

    public void getProjectMenu(final EachApiListner listner) {
        ////////mProgress.setMessage("Please wait...getProjectMenu");

        Log.e("GetProjectMenu", "OK");
        String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetProjectMenu.php";

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("GetProjectMenu", response);
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {

                            jsonObj = new JSONObject(response);
                            //String message = jsonObj.getString("message");

                            String success = jsonObj.getString("success");
                            Log.e("ProjectMenu", success);
                            if (success.equals("1")) {
                                JSONArray jsonArray = jsonObj.getJSONArray("ProjectMenu");
                                //////databaseHelper.delete
                                deletedata("ProjectMenu", null);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("ProjectMenu", jsonObject.toString());
                                    String MenuID = jsonObject.getString("MenuID");
                                    String ProjectID = jsonObject.getString("ProjectID");
                                    String MenuGroup = jsonObject.getString("MenuGroup");
                                    String MenuName = jsonObject.getString("MenuName");
                                    String PageOpen = jsonObject.getString("PageOpen");
                                    String ValuePass = jsonObject.getString("ValuePass");
                                    String ImageName = jsonObject.getString("ImageName");
                                    String GroupSortBy = jsonObject.getString("GroupSortBy");
                                    String SortBy = jsonObject.getString("SortBy");

                                    databaseHelper.createProjectMenu(new ProjectMenu(MenuID, ProjectID, MenuGroup, MenuName, PageOpen, ValuePass, ImageName, GroupSortBy, SortBy));
                                }


                            } else {
                                String message2 = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            listner.apifinished(success, "novalue", "ProjectMenu");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listner.apifinished("0", e.getMessage(), "ProjectMenu");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getBookings(final String clientID, final EachApiListner listner) {

        String tag_json_obj = "json_obj_req";
        String url = BOOKING_GET_ALL_DATA_FROM_SERVER;


        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String text = "", BookingID = "", ClientName = "", ClientMobile = "", ClientAddress = "", ClientNic = "", EventName = "", BookingDate = "", EventDate = "",
                                ArrangePersons = "", ChargesTotal = "", Description = "", ClientID = "", ClientUserID = "", NetCode = "", SysCode = "", UpdatedDate = "";
                        try {
                            JSONObject json = new JSONObject(response);
                            String success = json.getString("success");
                          //  String message = json.getString("message");
                            Log.e("Response", success);

                            if (success.equals("1")) {
                                JSONArray jsonArray = json.getJSONArray("Bookings");
                                Log.e("SSSS", jsonArray.toString());
                                //////databaseHelper.delete
                                deletedata("Booking", clientID);
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
                                    Log.e("TEST", EventDate);
                                    ArrangePersons = jsonObject.getString("ArrangePersons");
                                    ChargesTotal = jsonObject.getString("ChargesTotal");
                                    Description = jsonObject.getString("Description");
                                    ClientID = jsonObject.getString("ClientID");
                                    ClientUserID = jsonObject.getString("ClientUserID");
                                    NetCode = jsonObject.getString("NetCode");
                                    SysCode = jsonObject.getString("SysCode");
                                    Log.e("TEST", "TEST");
                                    String up = jsonObject.getString("UpdatedDate");
                                    JSONObject jbb = new JSONObject(up);
                                    UpdatedDate = jbb.getString("date");
                                    String SessionDate = jsonObject.getString("SessionDate");
                                    String Shift = jsonObject.getString("Shift");
                                    String SerialNo = jsonObject.getString("SerialNo");

                                    Log.e("TEST", "SSSS");
                                    Bookings servbooking = new Bookings(BookingID, ClientName, ClientMobile, ClientAddress, ClientNic, EventName, BookingDate, EventDate, ArrangePersons, ChargesTotal, Description, ClientID, ClientUserID, NetCode, SysCode, UpdatedDate, Shift, SerialNo);
                                    Log.e("serve", servbooking.toString());
                                    databaseHelper.createBooking(servbooking);

//                                    if (i == jsonArray.length() - 1) {
//                                        TableSession session = new TableSession("Bookings", BookingID, SessionDate, SessionDate);
//                                        session.save();
//                                    }
                                }
                                // getAccount4UserRights();

//                                FetchFromDb();
//                                pDialog.dismiss();
                            } else {
                                Log.e(GenericConstants.ByPASSForGetBooking, "ByPASSing fun");
                                // getAccount4UserRights();
//                                pDialog.dismiss();
                            }
                            listner.apifinished(success, "novalue", "Booking");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listner.apifinished("0", e.getMessage(), "Booking");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
//                pDialog.dismiss();
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", clientID);
                return params;
            }
        };

        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getAccount4UserRights(final EachApiListner listner) {
        ////////mProgress.setMessage("Please wait...getAccount4UserRights");

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
                           // String message = json.getString("message");
                            Log.e("Success", success);

                            if (success.equals("1")) {
                                JSONArray jsonArray = json.getJSONArray("Account4UserRights");
                                Log.e("Account4UserRights", jsonArray.toString());
//                                ////////databaseHelper.delete
                                deletedata("Account4UserRights", null);
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

//                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    editor.putString(log_in, "Yes");
//                                    editor.apply();

                                    ///////////////////////////////////////////////
//                                    Log.e(GenericConstants.MYEdittion, "Editing");
//                                    Log.e(this.getClass().getName(), "Client ID::" + clientID);
//                                    Log.e(this.getClass().getName(), "ClientUserID::::" + clientUserID);
//                                    Log.e(this.getClass().getName(), "ProjectIDSerssion::" + projectID);
//                                    Log.e(this.getClass().getName(), "UserRightSession::" + userRights);
//                                    new Prefrence(mCtx).setMYClientUserIDSession(clientUserID);


                                    mProgress.dismiss();

                                }
                            } else {
//                                SharedPreferences.Editor editor = mCtx.getSharedPreferences().edit();
//                                editor.putString(log_in, "Yes");
//                                editor.apply();
///////////////////////////////////////////////
//                                Log.e(GenericConstants.MYEdittion, "Editing");
//                                Log.e(this.getClass().getName()+"sam", "Client ID::" + clientID);
//                                Log.e(this.getClass().getName()+"sam", "ClientUserID::" + clientUserID);
//                                Log.e(this.getClass().getName()+"sam", "ProjectIDSerssion::" + projectID);
//                                Log.e(this.getClass().getName()+"sam", "UserRightSession::" + userRights);
//                                new Prefrence(mCtx).setMYClientUserIDSession(clientUserID);
                                ////////////////////////////////

                                mProgress.dismiss();
                            }
                            listner.apifinished(success, "novalue", "Account4Right");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listner.apifinished("0", e.getMessage(), "Account4Right");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
//                pDialog.dismiss();
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
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
            ConnectivityManager cm = (ConnectivityManager) mCtx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }


    private void deletedata(String tablename, String clientid) {

        int iddelet = -1;
        if (clientid == null) {

            iddelet = databaseHelper.getWritableDatabase()
                    .delete(tablename, null, null);
        } else {

            iddelet = databaseHelper.getWritableDatabase()
                    .delete(tablename, "ClientID=" + clientid, null);

        }
        Log.e("deletestatus", iddelet + " ");
    }


    public void trigerall0apimehtd(final String clientID,final EachApiListner finallistner){
        final StringBuilder builder=new StringBuilder();
        finalreportallmethod=builder;
        getCashBook(clientID, new EachApiListner() {
            @Override
            public void apifinished(String code, String message, String methodname) {
                builder.append("\n flag1"+"("+code+") mes:"+methodname);
                Log.e("flag1","("+code+") mes:"+methodname);
                getAccount3Name(clientID, new EachApiListner() {
                    @Override
                    public void apifinished(String code, String message, String methodname) {
                        builder.append("\n flag2"+"("+code+") mes:"+methodname);
                        Log.e("flag2","("+code+") mes:"+methodname);
                        getAccountGroups(new EachApiListner() {
                            @Override
                            public void apifinished(String code, String message, String methodname) {
                                builder.append("\n flag3"+"("+code+") mes:"+methodname);
                                Log.e("flag3","("+code+") mes:"+methodname);
                                getAccountTypes(new EachApiListner() {
                                    @Override
                                    public void apifinished(String code, String message, String methodname) {
                                        builder.append("\n flag4"+"("+code+") mes:"+methodname);
                                        Log.e("flag4","("+code+") mes:"+methodname);
                                        getItem1Type(new EachApiListner() {
                                            @Override
                                            public void apifinished(String code, String message, String methodname) {
                                                builder.append("\n flag5"+"("+code+") mes:"+methodname);
                                                Log.e("flag5","("+code+") mes:"+methodname);
                                                getItem2Group(clientID, new EachApiListner() {
                                                    @Override
                                                    public void apifinished(String code, String message, String methodname) {
                                                        builder.append("\n flag6"+"("+code+") mes:"+methodname);
                                                        Log.e("flag6","("+code+") mes:"+methodname);
                                                        getItem3Name(clientID, new EachApiListner() {
                                                            @Override
                                                            public void apifinished(String code, String message, String methodname) {
                                                                builder.append("\n flag7"+"("+code+") mes:"+methodname);
                                                                Log.e("flag7","("+code+") mes:"+methodname);
                                                                getSalePur1Data(clientID, new EachApiListner() {
                                                                    @Override
                                                                    public void apifinished(String code, String message, String methodname) {
                                                                        builder.append("\n flag8"+"("+code+") mes:"+methodname);
                                                                        Log.e("flag8","("+code+") mes:"+methodname);
                                                                        getSalePur2Data(clientID, new EachApiListner() {
                                                                            @Override
                                                                            public void apifinished(String code, String message, String methodname) {
                                                                                builder.append("\n flag9"+"("+code+") mes:"+methodname);
                                                                                Log.e("flag9","("+code+") mes:"+methodname);
                                                                                getProjectMenu(new EachApiListner() {
                                                                                    @Override
                                                                                    public void apifinished(String code, String message, String methodname) {
                                                                                        builder.append("\n flag10"+"("+code+") mes:"+methodname);
                                                                                        Log.e("flag10","("+code+") mes:"+methodname);
                                                                                    getBookings(clientID, new EachApiListner() {
                                                                                        @Override
                                                                                        public void apifinished(String code, String message, String methodname) {
                                                                                            builder.append("\n flag11"+"("+code+") mes:"+methodname);
                                                                                            Log.e("flag11","("+code+") mes:"+methodname);
                                                                                            getAccount4UserRights(new EachApiListner() {
                                                                                                @Override
                                                                                                public void apifinished(String code, String message, String methodname) {
                                                                                                    builder.append("\n flag12"+"("+code+") mes:"+methodname);
                                                                                                    Log.e("flag12","("+code+") mes:"+methodname);
                                                                                                    finalreportallmethod=builder;
                                                                                                    finallistner.apifinished("1","Done All","Final Triger");
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    });
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }

}
