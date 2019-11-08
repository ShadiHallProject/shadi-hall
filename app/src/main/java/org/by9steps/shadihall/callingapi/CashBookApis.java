package org.by9steps.shadihall.callingapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.helper.ApiRefStrings;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.CashBook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.by9steps.shadihall.helper.ApiRefStrings.CASH_BOOK_ADD_NEW_DATA_TO_SERVER;
import static org.by9steps.shadihall.helper.ApiRefStrings.CASH_BOOK_ADD_UPDATED_DATA_TO_SERVER;
import static org.by9steps.shadihall.helper.ApiRefStrings.CASH_BOOK_GET_UPDATED_EDITED_FROM_SERVER;

public class CashBookApis {
    public CashBookApis(Context context, ProgressDialog mProgress,
                        DatabaseHelper databaseHelper, Prefrence prefrence) {

        this.context = context;
        this.mProgress = mProgress;
        this.databaseHelper = databaseHelper;
        this.prefrence = prefrence;
    }

    public interface CashBookApiListener {
        public void FinishCashBookCallBackMethod(String success, String funType);
    }

    private Context context;
    private ProgressDialog mProgress;
    private DatabaseHelper databaseHelper;
    private Prefrence prefrence;
    public String TAG = "cashbook";
    //////////////////////Counting item to avoid aSync Task
//    count++;
//                        if(count>=total){
//        listener.FinishCallBackmethod("1","1updateAccount3Name");
//    }
    int total, count;


    public void GetAllDataFromCashBook(final String clientid, final CashBookApiListener listener) {
        TAG = "getcashbookdata";
        total = 1;
        count = 0;
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.CASH_BOOK_GET_ALL_DATA_FROM_SERVER;
        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            String success = jsonObj.getString("success");
                            Log.e(TAG, jsonObj.toString());
                            if (success.equals("1")) {
                               // deleteCashBookbyid(databaseHelper,clientid);
                                refdb.CashBookTableRef.delteAllbyClientID(databaseHelper,clientid);

                                JSONArray jsonArray = jsonObj.getJSONArray("CashBook");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String CashBookID = jsonObject.getString("CashBookID");
                                    String cb = jsonObject.getString("CBDate");
                                    JSONObject jb = new JSONObject(cb);
                                    String cb1 = jb.getString("date");
                                    SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = ss.parse(cb1);
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
                                    String stringobj = jsonObject.getString("UpdatedDate");
                                    JSONObject jbb = new JSONObject(stringobj);
                                    String UpdatedDate = jbb.getString("date");
                                    String TableID = jsonObject.getString("TableID");
                                    String SerialNo = jsonObject.getString("SerialNo");
                                    String TableName = jsonObject.getString("TableName");
                                    CashBook cashBookref = new CashBook();
                                    cashBookref.setCashBookID(CashBookID);
                                    cashBookref.setCBDate(CBDate1);
                                    cashBookref.setDebitAccount(DebitAccount);
                                    cashBookref.setCreditAccount(CreditAccount);
                                    cashBookref.setCBRemarks(CBRemark);
                                    cashBookref.setAmount(Amount);
                                    cashBookref.setClientID(ClientID);
                                    cashBookref.setClientUserID(ClientUserID);
                                    cashBookref.setNetCode(NetCode);
                                    cashBookref.setSysCode(SysCode);
                                    cashBookref.setUpdatedDate(UpdatedDate);
                                    cashBookref.setTableID(TableID);
                                    cashBookref.setSerialNo(SerialNo);
                                    cashBookref.setTableName(TableName);
                                    Log.e("objcount", cashBookref.toString());
                                    databaseHelper.createCashBook(cashBookref);

                                }

                            } else {
                                String message = jsonObj.getString("message");
                                MNotificationClass.ShowToast(context, "Some Thing Went Wrong");
                            }

                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                        count++;

                        if (count >= total) {
                            listener.FinishCashBookCallBackMethod("1", "1GetAllData");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                count++;

                if (count >= total) {
                    listener.FinishCashBookCallBackMethod("0", "1GetAllData");
                }
//                mProgress.dismiss();
                Log.e(TAG, error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e(TAG, " Clietnd id " + clientid);
                params.put("ClientID", clientid);

                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }

    public void SendNewInsertedDataToCloudDB(final CashBookApiListener listener) {
        TAG = "sendnewdata";
        total = 0;
        count = 0;
        mProgress.setMessage("Loading...SendNewInsertedDataCAshBook");
        String query = "SELECT * FROM CashBook WHERE CashBookID < 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
        List<CashBook> addCashBook = databaseHelper.getCashBook(query);
        total = addCashBook.size();
        Log.e(TAG, "Szie of new inserted data sqltie " + total);
        for (final CashBook c : addCashBook) {
            String tag_json_obj = "json_obj_req";
            String url = CASH_BOOK_ADD_NEW_DATA_TO_SERVER;

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equals("1")) {

                                    String id = jsonObject.getString("CashBookID");
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE CashBook SET CashBookID = '" + id + "', UpdatedDate = '" + UpdatedDate + "' WHERE ID = " + c.getcId());

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            count++;

                            if (count >= total) {
                                listener.FinishCashBookCallBackMethod("1", "1newdatatoserver");
                            }
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    count++;
                    if (count >= total) {
                        listener.FinishCashBookCallBackMethod("0", "1newdatatoserver");
                    }
                    Log.e(TAG, error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    Log.e("sendnewdata", c.toString());
                    params.put("CBDate", c.getCBDate());
                    params.put("DebitAccount", c.getDebitAccount());
                    params.put("CreditAccount", c.getCreditAccount());
                    params.put("CBRemarks", c.getCBRemarks());
                    params.put("Amount", c.getAmount());
                    params.put("ClientID", prefrence.getClientIDSession());
                    params.put("ClientUserID", prefrence.getClientUserIDSession());
                    params.put("NetCode", "0");
                    params.put("SysCode", "0");
                    params.put("TableID", c.getTableID());
                    params.put("SerialNo", c.getSerialNo());
                    params.put("TableName", c.getTableName());

                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }

        if (count >= total) {
            listener.FinishCashBookCallBackMethod("1", "2InsertedNewDataToSqlite");
        }
    }

    public void SendUpdatedDataFromSqliteToCloud(final CashBookApiListener listener) {
        TAG = "sendupdatedat";
        total = count = 0;
        String query = "SELECT * FROM CashBook WHERE  CashBookID>0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
        List<CashBook> addCashBook = databaseHelper.getCashBook(query);
        Log.e(TAG, String.valueOf(addCashBook.size()));
        total = addCashBook.size();
        for (final CashBook c : addCashBook) {
            String tag_json_obj = "json_obj_req";
            String url = CASH_BOOK_ADD_UPDATED_DATA_TO_SERVER;

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e(TAG, success);
                                if (success.equals("1")) {
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE CashBook SET UpdatedDate = '" + UpdatedDate + "' WHERE ID = " + c.getcId());

                                    // updatedDate = UpdatedDate;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            count++;
                            if (count >= total) {
                                listener.FinishCashBookCallBackMethod("1", "SendUpdatedDataFromSqliteToCloud");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    count++;
                    if (count >= total) {
                        listener.FinishCashBookCallBackMethod("1", "SendUpdatedDataFromSqliteToCloud");
                    }
                    // mProgress.dismiss();
                    Log.e(TAG, error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("CashBookID", c.getCashBookID());
                    params.put("CBDate", c.getCBDate());
                    params.put("DebitAccount", c.getDebitAccount());
                    params.put("CreditAccount", c.getCreditAccount());
                    params.put("CBRemarks", c.getCBRemarks());
                    params.put("Amount", c.getAmount());
                    params.put("ClientID", prefrence.getClientIDSession());
                    params.put("ClientUserID", prefrence.getClientUserIDSession());
                    params.put("NetCode", "0");
                    params.put("SysCode", "0");
                    params.put("TableID", c.getTableID());
                    params.put("SerialNo", c.getSerialNo());

                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
        Log.e("Sarem", "CashBook6");
        if (count >= total) {
            listener.FinishCashBookCallBackMethod("1", "SendUpdatedDataFromSqliteToCloud");
        }
    }

    public void getNewInsertedDataFromServer(final CashBookApiListener listener) {
        TAG = "newinsertedfrmser";
        total = 1;
        count = 0;
        String tag_json_obj = "json_obj_req";
        String u = CASH_BOOK_GET_UPDATED_EDITED_FROM_SERVER;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            String success = jsonObj.getString("success");
                            Log.e(TAG, jsonObj.toString());

                            if (success.equals("1")) {
                                JSONArray jsonArray = jsonObj.getJSONArray("CashBook");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Log.e(TAG, "Object (" + i + ") " + jsonArray.getJSONObject(i));
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
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
                                    String TableID = jsonObject.getString("TableID");
                                    String SessionDate = jsonObject.getString("SessionDate");
                                    String SerialNo = jsonObject.getString("SerialNo");
                                    String TableName = jsonObject.getString("TableName");

                                    long ii = databaseHelper.createCashBook(new CashBook(CashBookID, CBDate1, DebitAccount, CreditAccount, CBRemark, Amount, ClientID, ClientUserID, NetCode, SysCode, UpdatedDate, TableID, SerialNo, TableName));
                                    Log.e(TAG, "InsertedID:" + ii);
                                    //updatedDate = SessionDate;
//                                    if (i == jsonArray.length() - 1) {
//                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","CashBook");
//                                        for (TableSession s : se){
//                                            s.setMaxID(CashBookID);
//                                            s.setInsertDate(SessionDate);
//                                            s.save();
//                                        }
//                                    }

                                }

                            } else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            Log.e(TAG, "CashBook2");
                            //updateCashBook();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        count++;

                        if (count >= total) {
                            listener.FinishCashBookCallBackMethod("1", "getNewInsertedDataFromServer");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                count++;

                if (count >= total) {
                    listener.FinishCashBookCallBackMethod("0", "getNewInsertedDataFromServer");
                }
//                mProgress.dismiss();
                Log.e(TAG, error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ClientID", prefrence.getClientIDSession());
                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(CashBookID AS Int)) FROM CashBook");
                Log.e("MAXIDCASH", String.valueOf(maxID));
                params.put("MaxID", String.valueOf(maxID));

                Log.e("MAXID", String.valueOf(maxID));

                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

        if (count >= total) {
            listener.FinishCashBookCallBackMethod("1", "getNewInsertedDataFromServer");
        }
    }

    public void getUpdatedDataFromServer(final CashBookApiListener listener) {
        TAG = "getupdatafrom";
        total = 1;
        count = 0;
        String tag_json_obj = "json_obj_req";
        String u = CASH_BOOK_GET_UPDATED_EDITED_FROM_SERVER;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;
                        Log.e(TAG, response);
                        try {
                            jsonObj = new JSONObject(response);
                            Log.e(TAG, response);
                            String success = jsonObj.getString("success");
                            if (success.equals("1")) {
                                JSONArray jsonArray = jsonObj.getJSONArray("CashBook");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e(TAG, jsonObject.toString());
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
                                   // String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String stringobj = jsonObject.getString("UpdatedDate");
                                    JSONObject jbb1 = new JSONObject(stringobj);
                                    String UpdatedDate = jbb1.getString("date");
//                                    JSONObject jb = new JSONObject(ed);
//                                    String UpdatedDate = jb.getString("date");
                                    String TableID = jsonObject.getString("TableID");
                                    String SessionDate = jsonObject.getString("SessionDate");

                                    String query = "UPDATE CashBook SET CBDate = '" + CBDate1 + "', DebitAccount = '" + DebitAccount + "', CreditAccount = '" + CreditAccount + "', CBRemarks = '" + CBRemark + "', Amount = '" + Amount + "', ClientID = '" + ClientID + "', ClientUserID = '" + ClientUserID + "', NetCode = '" + NetCode + "', SysCode = '" + SysCode + "', UpdatedDate = '" + UpdatedDate + "', TableID = '" + TableID +
                                            "' WHERE CashBookID = " + CashBookID;
                                    databaseHelper.updateCashBook(query);
                                    // updatedDate = SessionDate;


                                }

                            } else {
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            Log.e(TAG, "CashBook3");
                            // getBookings1();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        count++;
                        if (count >= total) {
                            listener.FinishCashBookCallBackMethod("1", "getUpdatedDataFromServer");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                count++;
                if (count >= total) {
                    listener.FinishCashBookCallBackMethod("1", "getUpdatedDataFromServer");
                }
//                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String maxuptime = "";

                String qq2 = "Select max(UpdatedDate) as UpdatedDate from CashBook";
                Cursor cc1 = databaseHelper.getReadableDatabase()
                        .rawQuery(qq2, null);
                if (cc1.moveToFirst()) {
                    maxuptime = cc1.getString(0);
                }

                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(CashBookID AS Int)) FROM CashBook");
                Log.e("MAXIDCASH", String.valueOf(maxID));

                String date = databaseHelper.getClientUpdatedDate(prefrence.getClientIDSession());
                int index=maxuptime.lastIndexOf('.');
                maxuptime=maxuptime.substring(0,index+4);
                Log.e(TAG, "VAluesSENDed:(" + maxuptime + ")(" + maxID + ")" + prefrence.getClientIDSession());
                params.put("ClientID", prefrence.getClientIDSession());
                params.put("MaxID", String.valueOf(maxID));
                params.put("SessionDate", maxuptime);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

        if (count >= total) {
            listener.FinishCashBookCallBackMethod("1", "getUpdatedDataFromServer");
        }
    }


    public void trigerAllMethodInRow(final CashBookApiListener FinalMethodTriger) {
        getUpdatedDataFromServer(new CashBookApiListener() {
            @Override
            public void FinishCashBookCallBackMethod(String success, String funType) {
                Log.e("flagcashbook1", "Succes:" + success + " FunType:" + funType);
                SendUpdatedDataFromSqliteToCloud(new CashBookApiListener() {
                    @Override
                    public void FinishCashBookCallBackMethod(String success, String funType) {
                        Log.e("flagcashbook2", "Succes:" + success + " FunType:" + funType);
                        getNewInsertedDataFromServer(new CashBookApiListener() {
                            @Override
                            public void FinishCashBookCallBackMethod(String success, String funType) {
                                Log.e("flagcashbook3", "Succes:" + success + " FunType:" + funType);
                                SendNewInsertedDataToCloudDB(new CashBookApiListener() {
                                    @Override
                                    public void FinishCashBookCallBackMethod(String success, String funType) {
                                        FinalMethodTriger.FinishCashBookCallBackMethod(success, funType);
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
