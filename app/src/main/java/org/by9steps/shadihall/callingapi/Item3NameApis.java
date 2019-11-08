package org.by9steps.shadihall.callingapi;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
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
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.Item2Group;
import org.by9steps.shadihall.model.item3name.Item3Name;
import org.by9steps.shadihall.model.item3name.Item3Name_;
import org.by9steps.shadihall.model.item3name.UpdatedDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item3NameApis {
    public interface item3namefunListener {
        public void method(String success,String funType);
    }
    private Context context;
    private ProgressDialog mProgress;
    private DatabaseHelper databaseHelper;
    //////////////////////Counting item to avoid aSync Task
    int total, count;

    public Item3NameApis(Context context, ProgressDialog mProgress, DatabaseHelper databaseHelper) {
        this.context = context;
        this.mProgress = mProgress;
        this.databaseHelper = databaseHelper;
    }


    public void SendNewDatafromSqliteToServer(final String clientID,final item3namefunListener listener) {
        total = count = 0;
        final String TAG = "AddDataToItem3Name";
        mProgress.setMessage("Please wait...Item3Name");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.SendNewDataItem3NameToCloud;
        ///////////////////Getting data from Ddatabaes
        String qq = "Select * from Item3Name where ClientID=" + clientID + " AND Item3NameID<0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
        final List<Item3Name_> list = databaseHelper.getItem3NameData(qq);
        total = list.size();
        for (final Item3Name_ itemtem : list) {
            StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "res " + response);
                            count++;
//                        mProgress.dismiss();
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = new JSONObject(response);

                                String success = jsonObj.getString("success");
                                if (success.equals("1")) {

                                    String item3NameIDd = jsonObj.getString("Item3NameID");
                                    String dateup = jsonObj.getString("UpdatedDate");
                                    String olditem3nameid= String.valueOf(itemtem.getItem3NameID());
                                    itemtem.setItem3NameID(Integer.parseInt(item3NameIDd));
                                    UpdatedDate updatedDate = new UpdatedDate();
                                    updatedDate.setDate(dateup);
                                    itemtem.setUpdatedDate(updatedDate);
                                    long idd = refdb.Table3Name.UpdatedItem3NameDateID(databaseHelper, null, itemtem);
                                    //////////////Resolving Dependency Item3NameID to SalePur2
                                    long iid=refdb.SalePur2.updateItem3NameidInSalePur2(databaseHelper,itemtem.getClientID()+"",
                                            olditem3nameid,item3NameIDd);
                                    Log.e(TAG, "Item3Naem UPdatedID " + idd+" salepur2upID:"+iid);
                                    //Log.e(TAG, "Item3Name UPdatedID " + iidd);
                                    //   getProjectMenu();

                                } else {
                                    // getProjectMenu();
                                    String message = jsonObj.getString("message");
                                    Log.e(TAG, message);
                                    MNotificationClass.ShowToast(context, message);
//                                    if (count >= total) {
//                                        listener.method(success,"SendNewDatafromSqliteToServer");
//                                        MNotificationClass.ShowToast(context, "Updated All Item3Name Good To go ");
//                                        mProgress.dismiss();
//                                    }
                                }
                                if (count >= total) {
                                    listener.method(success,"SendNewDatafromSqliteToServer");
                                   // MNotificationClass.ShowToast(context, "Updated All Item3Name Good To go ");
                                    mProgress.dismiss();
                                }
                            } catch (JSONException e) {
                               MNotificationClass.ShowToast(context,
                                        "Error:"+ e.getMessage());
                                if (count >= total) {
                                    listener.method("0","SendNewDatafromSqliteToServer");
                                   // MNotificationClass.ShowToast(context, "Updated All Item3Name Good To go ");
                                    mProgress.dismiss();
                                }
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    count++;
//                mProgress.dismiss();
                    Log.e(TAG, error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
////////(Item2GroupID,ItemName,ClientID,ClientUserID,NetCode,SysCode,SalePrice,ItemCode,Stock
                    Map<String, String> params = new HashMap<String, String>();
                    Log.e(TAG, "paramToSendCloud" + itemtem.toString());
                    //Item2GroupID,Item1TypeID,Item2GroupName,ClientID,ClientUserID,NetCode, SysCode
                    params.put("Item2GroupID", String.valueOf(itemtem.getItem2GroupID()));
                    params.put("ItemName", itemtem.getItemName());
                    params.put("ClientID", String.valueOf(itemtem.getClientID()));
                    params.put("ClientUserID", String.valueOf(itemtem.getClientUserID()));
                    params.put("NetCode", itemtem.getNetCode());
                    params.put("SysCode", itemtem.getSysCode());
                    params.put("SalePrice", itemtem.getSalePrice());
                    params.put("ItemCode", itemtem.getItemCode());
                    if (itemtem.getStock() == null || itemtem.getStock().isEmpty())
                        params.put("Stock", "0");
                    else
                        params.put("Stock", itemtem.getStock());


                    return params;
                }
            };
            int socketTimeout = 10000;//10 seconds
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

        }
        Log.e(TAG, "ItemSizeFromSqlite" + list.size());

        if (count >= total) {
            listener.method("0","SendNewInsertedDatafromSqlite");
            MNotificationClass.ShowToast(context, "Updated All data Good To go ");
            mProgress.dismiss();
        }
    }

    public void SendEditedDataFromSqliteToItem3NameServer(final String clientID, final item3namefunListener listener) {
        total = count = 0;
        final String TAG = "EditedDta2servItm3nam";
        mProgress.setMessage("Please wait...getItem3Name");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.SendUpdatedDataToServerItem3Name;
        ///////////////////Getting data from Ddatabaes
        String qq = "Select * from Item3Name where ClientID=" + clientID + " AND Item3NameID > 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
        final List<Item3Name_> list = databaseHelper.getItem3NameData(qq);
        total = list.size();
        for (final Item3Name_ item3Name : list) {
            StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

//                        mProgress.dismiss();
                            Log.e(TAG, "res::" + response);
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = new JSONObject(response);
                                String success = jsonObj.getString("success");
                                String roweffected = jsonObj.getString("roweffected");
                                if (success.equals("1") && !roweffected.equals("0")) {
//                                    {"success":1,"roweffected"
//                                    :1,"UpdatedDate":
//                                    "2019-10-16 17:51:53","message":
//                                    "Update Successfully"}
                                    String dateup = jsonObj.getString("UpdatedDate");
                                    Log.e(TAG, "Updated Date =" + dateup);
                                    UpdatedDate datetemup = new UpdatedDate();
                                    datetemup.setDate(dateup);
                                    item3Name.setUpdatedDate(datetemup);
                                    long idd = refdb.Table3Name.UpdatedItem3NameDateID(databaseHelper, "null", item3Name);
                                    Log.e(TAG, "item3Name UPdatedID " + idd);

                                    //   getProjectMenu();

                                } else {
                                    // getProjectMenu();
                                    String message = jsonObj.getString("message");
                                    Log.e(TAG, message);
                                    MNotificationClass.ShowToast(context, message);
                                }
                            } catch (JSONException e) {
                                GenericConstants.ShowDebugModeDialog(context,
                                        "Error", e.getMessage());
                                Log.e(TAG, e.getMessage());
                                e.printStackTrace();
                            }
                            count++;
                            if (count >= total) {
                                MNotificationClass.ShowToast(context, "Updated All data Good To go ");
                                mProgress.dismiss();
                                listener.method("1","2");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    count++;
                    if (count >= total) {
                        listener.method("1","2");
                    }
//                mProgress.dismiss();
                    Log.e(TAG, error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    //  Item1TypeID,Item2GroupName,ClientID,ClientUserID,NetCode, SysCode)
                    Log.e(TAG, item3Name.toString());
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Item2GroupID", String.valueOf(item3Name.getItem2GroupID()));
                    params.put("ItemName", item3Name.getItemName());
                    params.put("ClientID", String.valueOf(item3Name.getClientID()));
                    params.put("ClientUserID", String.valueOf(item3Name.getClientUserID()));
                    params.put("NetCode", item3Name.getNetCode());
                    params.put("SysCode", item3Name.getSysCode());
                    params.put("SalePrice", item3Name.getSalePrice());
                    params.put("ItemCode", item3Name.getItemCode());
                    if (item3Name.getStock() == null || item3Name.getStock().isEmpty())
                        params.put("Stock", "0");
                    else
                        params.put("Stock", item3Name.getStock());
                    params.put("Item3NameID", String.valueOf(item3Name.getItem3NameID()));


                    return params;
                }
            };
            int socketTimeout = 10000;//10 seconds
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

        }
        Log.e(TAG, "ItemSizeFromSqlite" + list.size());

        if (count >= total) {
            listener.method("1","2");
        }
    }

    public void getNewInsertedDataFromServerItem3Name(final String clientID, final item3namefunListener listener ) {
       total=1;count=0;
        final String TAG = "itm3namnewrinserted";
        mProgress.setMessage("Please wait...getItemItem3nam");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetNewInsertedAndUpdatedDataItem3Name;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            // JSONArray jsonArray = jsonObj.getJSONArray("Item2Group");
                            Log.e(TAG, response);
                            //String success = jsonObj.getString("success");
                            if (response != null) {
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                Item3Name item3 = gson.fromJson(response, Item3Name.class);
                                if (item3.getSuccess() != 0) {

                                    for (Item3Name_ name : item3.getItem3Name()) {
                                        Log.e(TAG, " " + name.getClientID() + " --" + name.getUpdatedDate().getDate());
                                        long idd = refdb.Table3Name.AddItem3Name(databaseHelper, name);
                                        Log.e(TAG, "inser id" + idd);
                                    }
                                    Log.e(TAG, "onResponse: " + item3.getItem3Name().toString());


                                } else {
                                    MNotificationClass.ShowToast(context, "No Data Found in Item3Name");
                                }
                            }

                            Log.e(TAG, "onResponse: " + response);

                        } catch (JSONException e) {
                            GenericConstants.ShowDebugModeDialog(context,
                                    "Error", e.getMessage());
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        count++;
                        if(count>=total){
                            listener.method("1","3");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                count++;
                if(count>=total){
                    listener.method("1","3");
                }
//                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                int maxid = refdb.Table3Name.getmaxItem3NameID(databaseHelper, clientID);
                if (maxid < 0) {
                    maxid = maxid * -1;
                }
                maxid--;
                Map<String, String> params = new HashMap<String, String>();
                Log.e(TAG, "Request Server item3Name " + clientID + " maxID " + maxid);
                params.put("ClientID", clientID);
                params.put("MaxID", "" + maxid);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        if(count>=total){
            listener.method("1","3");
        }
    }

    public void getNewEditedDataFromServerItem3Name(final String clientID, final item3namefunListener listener) {
        total=1;count=0;
        final String TAG = "itm3namnewEdited";
        mProgress.setMessage("Please wait...getItemItem3nam");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetNewInsertedAndUpdatedDataItem3Name;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            // JSONArray jsonArray = jsonObj.getJSONArray("Item2Group");
                            Log.e(TAG, response);
                            //String success = jsonObj.getString("success");
                            if (response != null) {
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                Item3Name item3 = gson.fromJson(response, Item3Name.class);
                                if (item3.getSuccess() != 0) {

                                    for (Item3Name_ name : item3.getItem3Name()) {
                                        Log.e(TAG, " " + name.getClientID() + " --" + name.getUpdatedDate().getDate());
                                        long idd = refdb.Table3Name.UpdateTheWholeObject(databaseHelper, null, name);
                                        Log.e(TAG, "inser id" + idd);
                                    }
                                    Log.e(TAG, "onResponse: " + item3.getItem3Name().toString());


                                } else {
                                    MNotificationClass.ShowToast(context, "No Data Found in Item3Name");
                                }
                            }

                            Log.e(TAG, "onResponse: " + response);

                        } catch (JSONException e) {
                            GenericConstants.ShowDebugModeDialog(context,
                                    "Error", e.getMessage());
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        count++;
                        if(count>=total){
                            listener.method("1","1");
                            //callme
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                count++;
                if(count>=total){
                    listener.method("0","1");
                }
//                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                int maxid = refdb.Table3Name.getmaxItem3NameID(databaseHelper, clientID);
                if (maxid < 0) {
                    maxid = maxid * -1;
                }
                maxid--;
                String maxdate = refdb.Table3Name.getmaxUpdateItem3Name(databaseHelper, clientID);
                Map<String, String> params = new HashMap<String, String>();
                Log.e(TAG, "Request Server item3Name " + clientID + " maxID " + maxid + "SessinDate:" + maxdate);
                params.put("ClientID", clientID);
                params.put("MaxID", "" + maxid);
                params.put("SessionDate", "" + maxdate);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        if(count>=total){
            listener.method("1","1");
        }
    }

    public void trigerAllMethod(final String clientID, final item3namefunListener finallistner){
        getNewEditedDataFromServerItem3Name(clientID, new item3namefunListener() {
            @Override
            public void method(String success, String funType) {
                Log.e("flag1item3",funType);
                SendEditedDataFromSqliteToItem3NameServer(clientID, new item3namefunListener() {
                    @Override
                    public void method(String success, String funType) {
                        Log.e("flag2item3",funType);
                        getNewInsertedDataFromServerItem3Name(clientID, new item3namefunListener() {
                            @Override
                            public void method(String success, String funType) {
                                Log.e("flag3item3",funType);
                                SendNewDatafromSqliteToServer(clientID, new item3namefunListener() {
                                    @Override
                                    public void method(String success, String funType) {
                                        Log.e("flag4item3",funType);
                                        finallistner.method(success,funType);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        })
        ;
    }
}
