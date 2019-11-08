package org.by9steps.shadihall.callingapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.helper.ApiRefStrings;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.Item2Group;
import org.by9steps.shadihall.model.item3name.Item3Name_;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item2GroupApis {
    public interface listentodatafinish {
        public void method(String name);
    }

    private Context context;
    private ProgressDialog mProgress;
    private DatabaseHelper databaseHelper;
    ////////////////////////////////Counter For Sending Data To control Async Task
    int total = 0, count = 0;

    public Item2GroupApis(Context context, ProgressDialog mProgress, DatabaseHelper databaseHelper) {
        this.context = context;
        this.mProgress = mProgress;
        this.databaseHelper = databaseHelper;
    }

    ////////////////////////////////////////////
    public void AddNewDatafromSqliteToServer(final String clientID, final listentodatafinish mlistner) {
        total = count = 0;
        final String TAG = "AddDataToItem2Group";
        mProgress.setMessage("Please wait...getItem2Group");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.AddDataToItem2GorupLoc;
        ///////////////////Getting data from Ddatabaes
        String qq = "Select * from Item2Group where ClientID=" + clientID + " AND Item2GroupID<0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
        List<Item2Group> list = databaseHelper.getItem2GroupData(qq);
        total = list.size();
        for (final Item2Group itemtem : list) {
            Log.e(TAG, "Loop counter item2group:" + count);
            StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            a_count++;
//                        mProgress.dismiss();
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = new JSONObject(response);
                                Log.e(TAG, "res " + response);
                                String success = jsonObj.getString("success");
                                if (success.equals("1")) {

                                    String item2GroupID = jsonObj.getString("Item2GroupID");
                                    String dateup = jsonObj.getString("UpdatedDate");
                                    String oldid = itemtem.getItem2GroupID();
                                    String cliid = itemtem.getClientID();

                                    itemtem.setItem2GroupID(item2GroupID);
                                    itemtem.setUpdatedDate(dateup);
                                    Log.e(TAG, "Item2Group" + itemtem.toString());
                                    long idd = refdb.Table2Group.UpdateItem2Group(databaseHelper, itemtem, 1);
                                    //////////////////////////////For Item2GroupID update in Item3Name
                                    Item3Name_ item3Name = new Item3Name_();
                                    item3Name.setItem2GroupID(Integer.parseInt(item2GroupID));
                                    item3Name.setClientID(Integer.parseInt(cliid));
                                    Log.e(TAG, "ItemTEm" + item3Name.toString());
                                    long iidd = refdb.Table3Name.UpdatedItem3GroupID(databaseHelper, oldid, item3Name);
                                    Log.e(TAG, "Item2Group UPdatedID " + idd);
                                    Log.e(TAG, "Item3Name UPdatedID " + iidd);
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
                            if (count >= total) {
                                MNotificationClass.ShowToast(context, "Updated All data Good To go ");
                                mProgress.dismiss();
                                mlistner.method("1");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    count++;
                    if (count >= total) {
                        MNotificationClass.ShowToast(context, "Updated All data Good To go ");
                        mProgress.dismiss();
                        mlistner.method("1");
                    }
//                mProgress.dismiss();
                    Log.e(TAG, error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    //  Item1TypeID,Item2GroupName,ClientID,ClientUserID,NetCode, SysCode)
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Item1TypeID", itemtem.getItem1TypeID());
                    params.put("Item2GroupName", itemtem.getItem2GroupName());
                    params.put("ClientID", itemtem.getClientID());
                    params.put("ClientUserID", itemtem.getClientUserID());
                    params.put("NetCode", itemtem.getNetCode());
                    params.put("SysCode", itemtem.getSysCode());


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
            MNotificationClass.ShowToast(context, "Updated All data Good To go0 ");
            mProgress.dismiss();
            mlistner.method("1");
        }
    }

    //////////////Sending Edited Data To server
    public void AddEditedDataFromSqliteToItem2GroupServer(final String clientID, final listentodatafinish mlistner) {
        total = count = 0;
        final String TAG = "EditedDataToserv";
        mProgress.setMessage("Please wait...getItem2Group");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.SendUpdatedDataToServer;
        ///////////////////Getting data from Ddatabaes
        String qq = "Select * from Item2Group where ClientID=" + clientID + " AND Item2GroupID > 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
        List<Item2Group> list = databaseHelper.getItem2GroupData(qq);
        total = list.size();
        for (final Item2Group itemtem : list) {
            StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            count++;
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
                                    itemtem.setUpdatedDate(dateup);
                                    long idd = refdb.Table2Group.UpdateItem2Group(databaseHelper, itemtem, 3);
                                    Log.e(TAG, "Item2Group UPdatedID " + idd);

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
                            if (count >= total) {
                                MNotificationClass.ShowToast(context, "Updated All data Good To go ");
                                mProgress.dismiss();
                                mlistner.method("1");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (count >= total) {
                        mProgress.dismiss();
                        mlistner.method("1");
                    }
                    Log.e(TAG, error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    //  Item1TypeID,Item2GroupName,ClientID,ClientUserID,NetCode, SysCode)
                    Log.e(TAG, itemtem.toString());
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Item1TypeID", itemtem.getItem1TypeID());
                    params.put("Item2GroupName", itemtem.getItem2GroupName());
                    params.put("Item2GroupID", itemtem.getItem2GroupID());
                    params.put("ClientID", itemtem.getClientID());
                    params.put("ClientUserID", itemtem.getClientUserID());
                    params.put("NetCode", itemtem.getNetCode());
                    params.put("SysCode", itemtem.getSysCode());


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
            MNotificationClass.ShowToast(context, "Updated All data Good To go ");
            mProgress.dismiss();
            mlistner.method("1");
        }
    }

    /////////////////////////New Data From Server
    public void getNewInsertedDataFromServerItem2Group(final String clientID, final listentodatafinish mlistner) {
        total = 1;
        count = 0;
        final String TAG = "itm2grpnewrinserted";
        mProgress.setMessage("Please wait...getItem2Group");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetNewInsertedAndUpdatedDataData;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);

                            Log.e(TAG, response);
                            String success = jsonObj.getString("success");
                            if (success.equals("1")) {

                                JSONArray jsonArray = jsonObj.getJSONArray("Item2Group");
                                Log.e(TAG, "return From SErver Length object " + jsonArray.length());
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


                            } else {

                                String message = jsonObj.getString("message");
                                Log.e(TAG, message);

//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            GenericConstants.ShowDebugModeDialog(context,
                                    "Error", e.getMessage());
                            Log.e("Item2Group", e.getMessage());
                            e.printStackTrace();
                        }
                        count++;
                        if (count >= total) {
                            mlistner.method("1-res");

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                count++;
                if (count >= total) {
                    mlistner.method("-1-errer");

                }
//                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String maxid = refdb.Table2Group.getMaxDAtaFromitem2Group(databaseHelper, clientID, 1);

                Map<String, String> params = new HashMap<String, String>();
                Log.e(TAG, "Request Server Item2Group " + clientID + " maxID " + maxid);
                params.put("ClientID", clientID);
                params.put("MaxID", maxid);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
//
//            if (count >= total) {
//                mlistner.method("1-endmethod");
//
//            }


    }

    //////////////////Edited Data From Server
    public void getEditedDataFromServerItem2Group(final String clientID, final listentodatafinish mlistner) {
        total = 1;
        count = 0;
        final String TAG = "itm2grpedited";
        mProgress.setMessage("Please wait...getEditedDataFromServerItem2Group");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetNewInsertedAndUpdatedDataData;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);

                            String success = jsonObj.getString("success");
                            if (success.equals("1")) {
                                JSONArray jsonArray = jsonObj.getJSONArray("Item2Group");
                                Log.e(TAG, response);
                                Log.e(TAG, "return From SErver Length object " + jsonArray.length());
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
                                    long idd = refdb.Table2Group.UpdateItem2Group(databaseHelper, item2Group, 2);
                                    Log.e(TAG, "Item2Group UpdatedID= " + idd);
                                }


                            } else {

                                String message = jsonObj.getString("message");
                                Log.e(TAG, message);

//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            GenericConstants.ShowDebugModeDialog(context,
                                    "Except", e.getMessage());
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        count++;
                        if (count >= total) {
                            mlistner.method("1");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (count >= total) {
                    mlistner.method("-1");
                }
//                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String maxid = refdb.Table2Group.getMaxDAtaFromitem2Group(databaseHelper, clientID, 1);
                String updated = refdb.Table2Group.getMaxDAtaFromitem2Group(databaseHelper, clientID, 2);

                if (updated != null)
                    updated = updated.substring(0, updated.length());
                else{
                    updated="0";
                }
                Map<String, String> params = new HashMap<String, String>();
                Log.e(TAG, "Request Server Item2Group " + clientID + " maxID " + maxid + "update:" + updated);
                params.put("ClientID", clientID);
                params.put("MaxID", maxid);

                params.put("SessionDate", updated);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        if (count >= total) {
            mlistner.method("1");
        }
    }

    ////////////////////////////////////////triger All method
    public void trigerAllMethodinSequence(final String clientid, final listentodatafinish finallistner) {
//         getEditedDataFromServerItem2Group(clientid, new listentodatafinish() {
//             @Override
//             public void method(String name) {
//                 Log.e("flag1","Method::"+name);
//                 AddEditedDataFromSqliteToItem2GroupServer(clientid, new listentodatafinish() {
//                     @Override
//                     public void method(String name) {
//                         Log.e("flag2","Method::"+name);
//                         getNewInsertedDataFromServerItem2Group(clientid, new listentodatafinish() {
//                             @Override
//                             public void method(String name) {
//                                 Log.e("flag3","Method::"+name);
//                                 AddNewDatafromSqliteToServer(clientid, new listentodatafinish() {
//                                     @Override
//                                     public void method(String name) {
//                                         Log.e("flag4","Method::"+name);
//                                         finallistner.method("All Done");
//                                     }
//                                 });
//                             }
//                         });
//                     }
//                 });
//             }
//         });
        getEditedDataFromServerItem2Group(clientid, new Item2GroupApis.listentodatafinish() {
            @Override
            public void method(String name) {
                Log.e("status", "Flag2");
                AddEditedDataFromSqliteToItem2GroupServer(clientid, new Item2GroupApis.listentodatafinish() {
                    @Override
                    public void method(String name) {
                        Log.e("status", "Flag3");
                        getNewInsertedDataFromServerItem2Group(clientid, new Item2GroupApis.listentodatafinish() {
                            @Override
                            public void method(String name) {
                                Log.e("infint", "" + name);
                                Log.e("status", "Flag4");
                                AddNewDatafromSqliteToServer(clientid, new Item2GroupApis.listentodatafinish() {
                                    @Override
                                    public void method(String name) {
                                        //  Log.e("status", "Flag5");
                                        finallistner.method("All Done");
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
