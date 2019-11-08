package org.by9steps.shadihall.callingapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.helper.ApiRefStrings;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.Account3Name;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.by9steps.shadihall.helper.ApiRefStrings.Account3NameGetEDitedRecordFromServer;
import static org.by9steps.shadihall.helper.ApiRefStrings.Account3NameGetNewRecordFromServer;
import static org.by9steps.shadihall.helper.ApiRefStrings.Account3NameSendSqliteEditedRecord;
import static org.by9steps.shadihall.helper.ApiRefStrings.Account3NameSendSqliteNewAddedRecord;

public class Account3NameApis {
    public Account3NameApis(Context context, ProgressDialog mProgress, DatabaseHelper databaseHelper, Prefrence prefrence) {
        this.context = context;
        this.mProgress = mProgress;
        this.databaseHelper = databaseHelper;
        this.prefrence = prefrence;
    }

    public interface Acunt3namlistner {
        public void FinishCallBackmethod(String success, String funType);
    }

    public Acunt3namlistner FinalCallBAckListner;
    private Context context;
    private ProgressDialog mProgress;
    private DatabaseHelper databaseHelper;
    private Prefrence prefrence;
    public String TAG="account3namefun";
    //////////////////////Counting item to avoid aSync Task
//    count++;
//                        if(count>=total){
//        listener.FinishCallBackmethod("1","1updateAccount3Name");
//    }
    int total, count;

    ////////////////////Account3Name all 4 operations
    ///////////////////////////////////////////Methos Calling Sequences
    ////////////////Get Edited Record From Server
    public void updateAccount3Name(final Acunt3namlistner listener) {
        total = 1;
        count = 0;
        Log.e(TAG, "Flag 1 MathodName::updateAccount3Name");

        mProgress.show();
        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
            mProgress.setMessage("Loading... getUpdatedData updateAccount3Name Method:updateAccount3Name CLI " + prefrence.getClientIDSession());

        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.Account3NameGetEDitedRecordFromServer;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            String success = jsonObj.getString("success");
                            Log.e("Account3Name2", jsonObj.toString());
                            if (success.equals("1")) {
                                JSONArray jsonArray = jsonObj.getJSONArray("Account3Name");
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

                                    String query = "UPDATE Account3Name SET AcNameID = '" + AcNameID + "', AcName = '" + AcName + "', AcGroupID = '" + AcGroupID + "', AcAddress = '" + AcAddress + "', AcMobileNo = '" + AcMobileNo
                                            + "', AcContactNo = '" + AcContactNo + "', AcEmailAddress = '" + AcEmailAddress + "', AcDebitBal = '" + AcDebitBal + "', AcCreditBal = '" + AcCreditBal + "', AcPassward = '" + AcPassward
                                            + "', ClientID = '" + ClientID + "', ClientUserID = '" + ClientUserID + "', SysCode = '" + SysCode + "', NetCode = '" + NetCode + "', UpdatedDate = '" + UpdatedDate + "', SerialNo = '" + SerialNo
                                            + "', UserRights = '" + UserRights + "', SecurityRights = '" + SecurityRights + "', Salary ='" + Salary + "' WHERE AcNameID = " + AcNameID;
                                    databaseHelper.updateAccount3Name(query);
                                    // updatedDate = SessionDate;

                                }
                                Log.e(TAG, "All Fields Up To dated");
                                //getReports();
                            } else {
                                String message = jsonObj.getString("message");
                                MNotificationClass.ShowToastTem(context, message);
                            }

                            Log.e(TAG, "CashBook1");
                            //Previously Added Function
                            // getCashBook1();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            GenericConstants.ShowDebugModeDialog(context,
                                    "Error", e.getMessage());
                        }

                        ////Calling Next Method
                        // getAccount3Name();
                        count++;
                        if (count >= total) {
                            listener.FinishCallBackmethod("1", "1updateAccount3Name");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                count++;
                if (count >= total) {
                    listener.FinishCallBackmethod("1", "1updateAccount3Name");
                }
                mProgress.dismiss();
                GenericConstants.ShowDebugModeDialog(context,
                        "Error", error.getMessage());
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(AcNameID AS Int)) FROM Account3Name");

                //  String date = databaseHelper.getClientUpdatedDate(prefrence.getClientIDSession());
                String date = databaseHelper.getAccount3NameMaxUpdatedDate(prefrence.getClientIDSession());

                ///////////////////must send date in single quotes
                ///////////////////////////trim last 3 zero of the date other wise server not accept it
                date = date.trim();
                date = date.substring(0, date.length() - 3);
                params.put("ClientID", prefrence.getClientIDSession());
                params.put("MaxID", String.valueOf(maxID));
                params.put("SessionDate", "'" + date + "'");
                Log.e("key", "Look For Updata Server CLIID:" + prefrence.getClientUserIDSession() + " MaxID:" + maxID + " Serssion Date " + date);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

        if (count >= total) {
            listener.FinishCallBackmethod("1", "1updateAccount3Name");
        }

    }

    /////////////////////Record That Are New on The server
    public void getAccount3Name(final Acunt3namlistner listener) {
        total = 1;
        count = 0;
        Log.e(TAG, "Flag 2 MathodName::getAccount3Name");

        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
            mProgress.setMessage("Loading... getNew InsertDataFromServer Method:getAccount3Name CLI " + prefrence.getClientIDSession());
        mProgress.show();
        String tag_json_obj = "json_obj_req";
        String u = Account3NameGetNewRecordFromServer;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            String success = jsonObj.getString("success");
                            Log.e(TAG, jsonObj.toString());
                            if (success.equals("1")) {
                                JSONArray jsonArray = jsonObj.getJSONArray("Account3Name");
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
//                                    updatedDate = SessionDate;
/////////////////////////////////////////Calling This method to update the list of Account3Name data
//                                    getReports();
//                                    if (i == jsonArray.length() - 1) {
//                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Account3Name");
//                                        for (TableSession s : se){
//                                            s.setMaxID(AcNameID);
//                                            s.setInsertDate(SessionDate);
//                                            s.save();
//                                        }
//
//                                    }

                                }
                                if (GenericConstants.IS_DEBUG_MODE_ENABLED)
                                    MNotificationClass.ShowToast(context, "No of new inserted record " + jsonArray.length());

                            } else {
                                String message = jsonObj.getString("message");
                                MNotificationClass.ShowToastTem(context, message);
                            }

                        } catch (JSONException e) {
                            GenericConstants.ShowDebugModeDialog(context,
                                    "Error", e.getMessage());
                            e.printStackTrace();
                        }
                        //////////Calling Next Method
                        //updateAccount3Name1();
                        count++;
                        if (count >= total) {
                            listener.FinishCallBackmethod("1", "2getAccount3Name");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //////////Calling Next Method
                count++;
                if (count >= total) {
                    listener.FinishCallBackmethod("0", "2getAccount3Name");
                }
                mProgress.dismiss();
                Log.e(TAG, error.toString());
                GenericConstants.ShowDebugModeDialog(context,
                        "Error", error.getMessage());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ClientID", prefrence.getClientIDSession());
                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(AcNameID AS Int)) FROM Account3Name");
                params.put("MaxID", String.valueOf(maxID));
                Log.e("key", "MaxID" + maxID);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        if (count >= total) {
            listener.FinishCallBackmethod("1", "2getAccount3Name");
        }
    }

    ////////////////////Record That are Edited on Sqlite
    public void updateAccount3Name1(final Acunt3namlistner listener) {
        total = count = 0;
        Log.e(TAG, "Flag 3 MathodName::updateAccount3Name1");
        mProgress.show();
        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
            mProgress.setMessage("Loading... updateAccount3Name1 UpDateRecordFromSqliteToCloud Method:updateAccount3Name1 CLI " + prefrence.getClientIDSession());

        String query = "SELECT * FROM Account3Name  WHERE AcNameID > 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
        final List<Account3Name> addBooking = databaseHelper.getAccount3Name(query);
        Log.e(TAG, String.valueOf(addBooking.size()));
        total = addBooking.size();
        for (final Account3Name c : addBooking) {
            Log.e(TAG, "DAta" + c.toString());
            String tag_json_obj = "json_obj_req";
            String url = Account3NameSendSqliteEditedRecord;

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mProgress.dismiss();
                            Log.e(TAG, response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e(TAG, success);
                                if (success.equals("1")) {
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE Account3Name SET UpdatedDate = '" + UpdatedDate + "' WHERE ID = " + c.getId());
                                    //updatedDate = UpdatedDate;
//                                    getReports();
//                                    List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Account3Name");
//                                    for (TableSession s : se){
//                                        s.setUpdateDate(UpdatedDate);
//                                        s.save();
//                                    }
                                    databaseHelper.updateClient("UPDATE Client SET UpdatedDate = '" + UpdatedDate + "' WHERE ClientID = " + prefrence.getClientIDSession());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            count++;
                            if (count >= total) {
                                listener.FinishCallBackmethod("1", "3updateAccount3Name1");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //////////////////////////////////Calling Next Methos
                    count++;
                    if (count >= total) {
                        listener.FinishCallBackmethod("1", "3updateAccount3Name1");
                    }
                    mProgress.dismiss();
                    Log.e(TAG, error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();

                    params.put("AcNameID", c.getAcNameID());
                    params.put("AcName", c.getAcName());
                    params.put("AcAddress", c.getAcAddress());
                    params.put("AcContactNo", c.getAcContactNo());
                    params.put("AcEmailAddress", c.getAcEmailAddress());
                    params.put("Salary", c.getSalary());
                    params.put("AcMobileNo", c.getAcMobileNo());
                    params.put("AcPassward", c.getAcPassward());
                    params.put("SecurityRights", c.getSecurityRights());
                    params.put("ClientID", prefrence.getClientIDSession());
                    params.put("AcGroupID", c.getAcGroupID());

                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);


        }
        if (count >= total) {
            listener.FinishCallBackmethod("1", "3updateAccount3Name1");
        }
        //////////////////////////////////Calling Next Methos
//        addAccount3Name();
//        getReports();

//        if (addBooking.size() == 0) {
//            databaseHelper.updateClient("UPDATE Client SET UpdatedDate = '" + updatedDate + "' WHERE ClientID = " + prefrence.getClientIDSession());
//        }


    }

    ////////////////Record That are added in sqlite and not Uploaded on server
    public void addAccount3Name(final Acunt3namlistner listener) {
        total = count = 0;
        Log.e(TAG, "Flag 4 MathodName::addAccount3Name");

        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
            mProgress.setMessage("Loading... addFromSqliteToCloud Method:addAccount3Name CLI " + prefrence.getClientIDSession());

        String query = "SELECT * FROM Account3Name WHERE  AcNameID < 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
        final List<Account3Name> addBooking = databaseHelper.getAccount3Name(query);
        Log.e("BookingID UP", String.valueOf(addBooking.size()));

        for (final Account3Name c : addBooking) {
            mProgress.show();
            String tag_json_obj = "json_obj_req";
            String url = Account3NameSendSqliteNewAddedRecord;

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mProgress.dismiss();
                            Log.e(TAG, response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e(TAG, success);
                                if (success.equals("1")) {
                                    String id = jsonObject.getString("AcNameID");
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE Account3Name SET AcNameID = '" + id + "', UpdatedDate = '" + UpdatedDate + "' WHERE ID = " + c.getId());
                                    // updatedDate = UpdatedDate;

                                    String iddst = refdb.Account3NameTableFun.UpdateAcNameIDInCashBook(databaseHelper,
                                            c.getClientID(),
                                            c.getAcNameID(),
                                            id);
//                                    String iddst= refdb.Account3NameTableFun.UpdateAcNameIDInCashBook(
//                                            databaseHelper,
//                                            "115",
//                                            "-2525",
//                                            "2525");
                                    Log.e("oberve", c.toString() + "");
                                    Log.e("oberve", iddst + "");
                                    if (GenericConstants.IS_DEBUG_MODE_ENABLED)
                                        Toast.makeText(context, id + " Recourd Updated", Toast.LENGTH_SHORT).show();
                                    // getReports();
//                                    List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Account3Name");
//                                    for (TableSession s : se){
//                                        s.setMaxID(id);
//                                        s.setInsertDate(UpdatedDate);
//                                        s.save();
//                                    }
                                } else {
                                    databaseHelper.deleteAccount3NameEntry("DELETE FROM Account3Name WHERE ID = " + c.getId());
                                    String message = jsonObject.getString("message");
                                    if (GenericConstants.IS_DEBUG_MODE_ENABLED)
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                MNotificationClass.ShowToastTem(context, e.getMessage() + " -" + response);
                                e.printStackTrace();
                            }
                            count++;
                            if (count >= total) {
                                listener.FinishCallBackmethod("1", "4addAccount3Name");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    count++;
                    if (count >= total) {
                        listener.FinishCallBackmethod("0", "4addAccount3Name");
                    }
                    mProgress.dismiss();
                    GenericConstants.ShowDebugModeDialog(context,
                            "Error", error.getMessage());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    ///Parameter Sequences
//                    isset($_POST['AcName']) && --
//                    isset($_POST['AcAddress']) ---
//                            && isset($_POST['AcContactNo']) && ---
//                            isset($_POST['AcEmailAddress']) ---
//                            && isset($_POST['Salary'])---
//                            && isset($_POST['AcMobileNo']) && ---
//                            isset($_POST['AcPassward']) ---
//                            && isset($_POST['SecurityRights']) && ----
//                            isset($_POST['ClientID']) -----
//                            && isset($_POST['AcGroupID']) && ---
//                            isset($_POST['SerialNo'])  ----
//                            &&  isset($_POST['AcDebitBal']) && ---
//                            isset($_POST[' AcCreditBal']) ----
//                            && isset($_POST['ClientUserID']) && ---
//                            isset($_POST['SysCode']) ---
//                            && isset($_POST['NetCode']) &&----
//                            isset($_POST['UpdatedDate']) ----
//                            && isset($_POST['UserRights']) && ---
//                            isset($_POST['AccountPhoto']---
                    Log.e("key", "Set Pram ::" + c.toString());
                    params.put("AcName", c.getAcName());
                    params.put("AcGroupID", c.getAcGroupID());
                    params.put("AcAddress", c.getAcAddress());
                    params.put("AcContactNo", c.getAcContactNo());
                    params.put("AcEmailAddress", c.getAcEmailAddress());
                    params.put("Salary", c.getSalary());
                    params.put("AcMobileNo", c.getAcMobileNo());
                    params.put("AcPassward", c.getAcPassward());
                    params.put("SecurityRights", c.getSecurityRights());
                    params.put("ClientID", prefrence.getClientIDSession());
                    /////////////////////////////////
                    params.put("UpdatedDate", c.getUpdatedDate());
                    //params.put("AcNameID", c.getAcNameID());
                    params.put("SerialNo", c.getSerialNo());
                    params.put("AcDebitBal", c.getAcDebitBal());
                    params.put("AcCreditBal", c.getAcCreditBal());
                    params.put("ClientUserID", c.getClientUserID());
                    params.put("SysCode", c.getSysCode());
                    params.put("NetCode", c.getNetCode());
                    params.put("UserRights", c.getUserRights());
                    params.put("AccountPhoto", "Not Set Yet ");

                    return params;
                }
            };

            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
        mProgress.dismiss();
        MNotificationClass.ShowToast(context, "All Done");
        if (count >= total) {
            listener.FinishCallBackmethod("0", "4addAccount3Name");
        }
        if (addBooking.size() < 0)
            MNotificationClass.ShowToast(context, "Info" + "Updated Data Not Found ");
    }

    public void trigerAllMethodInSequecne() {

        updateAccount3Name(new Acunt3namlistner() {
            @Override
            public void FinishCallBackmethod(String success, String funType) {
                Log.e("Account3Namefunc", "Flag1 Suces:" + success + " funType:" + funType);
                getAccount3Name(secon2methodhandler);
            }
        });

    }

    Acunt3namlistner secon2methodhandler = new Acunt3namlistner() {
        @Override
        public void FinishCallBackmethod(String success, String funType) {
            Log.e("Account3Namefunc", "Flag2 Suces:" + success + " funType:" + funType);
            updateAccount3Name1(new Acunt3namlistner() {
                @Override
                public void FinishCallBackmethod(String success, String funType) {
                    Log.e("Account3Namefunc", "Flag3 Suces:" + success + " funType:" + funType);
                    if (FinalCallBAckListner != null)
                        addAccount3Name(FinalCallBAckListner);
                }
            });
        }
    };
}
