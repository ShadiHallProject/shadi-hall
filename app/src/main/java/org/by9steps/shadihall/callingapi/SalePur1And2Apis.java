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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.helper.ApiRefStrings;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.item3name.UpdatedDate;
import org.by9steps.shadihall.model.salepur1data.SalePur1Data;
import org.by9steps.shadihall.model.salepur1data.Salepur1;
import org.by9steps.shadihall.model.salepur2data.SalePur2;
import org.by9steps.shadihall.model.salepur2data.SalePur2Data;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalePur1And2Apis {
    public SalePur1And2Apis(Context context, ProgressDialog mProgress, DatabaseHelper databaseHelper, Prefrence prefrence) {
        this.context = context;
        this.mProgress = mProgress;
        this.databaseHelper = databaseHelper;
        this.prefrence = prefrence;
    }

    public interface SalePur1funListener {
        public void FinishCallBackmethod(String success, String funType);
    }

    public SalePur1funListener finallistnerForAlldone;
    private Context context;
    private ProgressDialog mProgress;
    private DatabaseHelper databaseHelper;
    private Prefrence prefrence;
    //////////////////////Counting item to avoid aSync Task
//    count++;
//                        if(count>=total){
//        salePur1funListener.FinishCallBackmethod("1","1");
//    }
    int total, count;

    private void getSalePur1UpdatedEditedDataFromServer(final SalePur1funListener salePur1funListener) {
        total = 1;
        count = 0;
        Log.e("status", "Flag1");
        final String TAG = "getSalePur1UpdatedEditedDataFromServer";
        mProgress.setMessage("Please wait...getSalePur1DAta");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetSalePur1EditedDataFromServer;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null) {
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                Log.e("getSalePur1EditedData", response);
                                SalePur1Data salePur1Data = gson.fromJson(response, SalePur1Data.class);
                                Log.e("kkk", salePur1Data.getSuccess() + " " + salePur1Data.getSalepur1().size());
                                if (salePur1Data.getSuccess() != 0) {
                                    for (Salepur1 salepur1 : salePur1Data.getSalepur1()) {
                                        Log.e("getSalePur1DataList", " " + salepur1.toString());
                                        long idd = refdb.SlePur1.updateSalepur1fromServer(databaseHelper, salepur1);
                                        if (idd <= 0)
                                            refdb.SlePur1.AddItemSalePur1(databaseHelper, salepur1);
                                        Log.e("getSalePur1EditedData", "inser id::" + idd);
                                    }
                                    Log.e("getSalePur1EditedData", "onResponse: " + salePur1Data.getSalepur1());


                                } else {
                                    MNotificationClass.ShowToast(context, "No Data Found in SalePur1Table");
                                }
                            }

                            Log.e("getSalePur1EditedData", "onResponse: " + response);

                            mProgress.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        count++;
                        if (count >= total) {
                            salePur1funListener.FinishCallBackmethod("1", "1salepur1");
                        }
                        //  getSalePur2UpdatedEditedDataFromServer();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //getSalePur2UpdatedEditedDataFromServer();
                count++;
                if (count >= total) {
                    salePur1funListener.FinishCallBackmethod("1", "1salepur1");
                }
                GenericConstants.ShowDebugModeDialog(context, "Error SalePur2", error.getMessage());
                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String date = databaseHelper.getSalePur1MaxUpdatedDate(prefrence.getClientIDSession());
                if(date!=null){
                    date = date.trim();
                    date = date.substring(0, date.length() - 3);
                }else{
                 date="0";
                }

                Map<String, String> params = new HashMap<String, String>();
                Log.e("getSalePur1EditedData", "Max UpdateSalePur1 " + date);

                params.put("ClientID", prefrence.getClientIDSession());
                params.put("SessionDate", date);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

        if (count >= total) {
            salePur1funListener.FinishCallBackmethod("1", "1salepur1");
        }
    }

    private void getSalePur2UpdatedEditedDataFromServer(final SalePur1funListener salePur1funListener) {
        total = 1;
        count = 0;
        Log.e("status", "Flag2");

        final String TAG = "getSalePur2UpdatedEditedDataFromServer";
        mProgress.setMessage("Please wait...getSalePur2DAta");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetSalePur2EditedDataFromServer;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null) {
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                SalePur2Data salePur2Data = gson.fromJson(response, SalePur2Data.class);
                                if (salePur2Data.getSuccess() != 0) {
                                    for (SalePur2 salePur2 : salePur2Data.getSalePur2()) {
                                        Log.e("getSalePur2Data", " " + salePur2.getClientID() + " --" + salePur2.getUpdatedDate().getDate());
                                        long idd = refdb.SalePur2.updateSalepur2fromServer(databaseHelper, salePur2);
                                        if (idd <= 0)
                                            refdb.SalePur2.AddItemSalePur2(databaseHelper, salePur2);
                                        Log.e("getSalePur2Data", "inser id" + idd);
                                    }


                                } else {
                                    MNotificationClass.ShowToast(context, "No Data Found in SalePur2Table");
                                }
                            }

                            Log.e("getSalePur2Data", "onResponse: " + response);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mProgress.dismiss();
                        count++;
                        if (count >= total) {
                            salePur1funListener.FinishCallBackmethod("1", "1SalePur2");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                count++;
                if (count >= total) {
                    salePur1funListener.FinishCallBackmethod("1", "1SalePur2");
                }
                GenericConstants.ShowDebugModeDialog(context, "Error SalePur2", error.getMessage());
                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String date = databaseHelper.getSalePur2MaxUpdatedDate(prefrence.getClientIDSession());
                if (date != null){
                    date = date.trim();
                    date = date.substring(0, date.length() - 3);
                }
                else{
                    date="0";
                }
                Map<String, String> params = new HashMap<String, String>();
                Log.e("getSalePur2Data", "Max UpdateSalePur1 " + date);

                params.put("ClientID", prefrence.getClientIDSession());
                params.put("SessionDate", date);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

        if (count >= total) {
            salePur1funListener.FinishCallBackmethod("1", "1SalePur2");
        }
    }

    ///////////////////////////////////////////////////////////////////////////Second Group
/////////////Send Edited Record From Sqlite To server
    public void updateSalePur1(final SalePur1funListener salePur1funListener) {
        Log.e("status", "Flag3");
        total = count = 0;
        Log.e("updateSalePur1", "Flag 3 MathodName::updateSalePur1");
        mProgress.show();
        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
            mProgress.setMessage("Loading... updateSalePur1 UpDateRecordFromSqliteToCloud Method:updateSalePur1 CLI " + prefrence.getClientIDSession());

        String query = "SELECT * FROM SalePur1 WHERE ClientID = " + prefrence.getClientIDSession() + " AND SalePur1ID > 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
        final List<Salepur1> salepur1s = refdb.SlePur1.GetSalePur1Data(databaseHelper, query);
        Log.e("updateSalePur1", String.valueOf(salepur1s.size()));
        total = salepur1s.size();
        for (final Salepur1 c : salepur1s) {
            Log.e("updateSalePur1", "DAta" + c.toString());
            String tag_json_obj = "json_obj_req";
            String url = ApiRefStrings.UpdateDataSalePur1OnServer;

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            mProgress.dismiss();
                            Log.e("updateSalePur1", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("updateSalePur1", success);
                                if (success.equals("1")) {
                                    int effectrow = jsonObject.getInt("roweffected");
                                    if (effectrow != 0) {
                                        String updatec = jsonObject.getString("UpdatedDate");
                                        String message = jsonObject.getString("message");

                                        long idd = databaseHelper.UpdateSalePur1Data(c.getID(), null, updatec, null);
                                        if (idd != -1)
                                            MNotificationClass.ShowToast(context, "Updated Data");
                                        else
                                            MNotificationClass.ShowToast(context, "Sqlite Not Updated");
                                    } else {
                                        MNotificationClass.ShowToast(context, "Not Updated Record " + c.getID());
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            count++;
                            if (count >= total) {
                                salePur1funListener.FinishCallBackmethod("1", "1updateSalePur1");
                            }
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //////////////////////////////////Calling Next Methos
                    count++;
                    if (count >= total) {
                        salePur1funListener.FinishCallBackmethod("0", "1updateSalePur1");
                    }
                    mProgress.dismiss();
                    Log.e("updateSalePur1", error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();


//                    Remarks
//                            ClientID
//                    ClientUserID
//                            NetCode
//                    SysCode
//                            NameOfPerson
//                    PayAfterDay
                    params.put("SalePur1ID", c.getSalePur1ID() + "");
                    params.put("EntryType", c.getEntryType() + "");
                    params.put("SPDate", c.getSPDate().getDate() + "");
                    params.put("AcNameID", c.getAcNameID() + "");
                    params.put("Remarks", c.getRemarks());
                    params.put("ClientID", c.getClientID() + "");
                    params.put("ClientUserID", c.getClientUserID() + "");
                    params.put("NetCode", c.getNetCode());
                    params.put("SysCode", c.getSysCode());
                    params.put("NameOfPerson", c.getNameOfPerson());
                    params.put("PayAfterDay", c.getPayAfterDay() + "");
                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
////////////////////////////////////As we know update not harm the server it may call synchronously
        ///

        if (count >= total) {
            salePur1funListener.FinishCallBackmethod("1", "1updateSalePur1");
        }


    }

    public void updateSalePur2(final SalePur1funListener salePur1funListener) {
        count = total = 0;
        Log.e("status", "Flag4");

        Log.e("method", "Flag 3 MathodName::updateSalePur2");
        mProgress.show();
        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
            mProgress.setMessage("Loading... updateSalePur2 UpDateRecordFromSqliteToCloud Method:updateSalePur2 CLI " + prefrence.getClientIDSession());

        String query = "SELECT * FROM SalePur2 WHERE UpdatedDate = '" + GenericConstants.NullFieldStandardText + "' AND ItemSerial >0 ";
        final List<SalePur2> salePur2s = refdb.SalePur2.GetSalePurItemList(databaseHelper, query);
        Log.e("updateSalePur2", String.valueOf(salePur2s.size()));
        total = salePur2s.size();
        for (final SalePur2 c : salePur2s) {
            Log.e("updateSalePur2", "DAta" + c.toString());
            String tag_json_obj = "json_obj_req";
            String url = ApiRefStrings.UpdateDataSalePur2OnServer;

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            mProgress.dismiss();
                            Log.e("updateSalePur2", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("updateSalePur2", success);
                                if (success.equals("1")) {
                                    int effectrow = jsonObject.getInt("roweffected");
                                    if (effectrow != 0) {
                                        String UpdatedDate = jsonObject.getString("UpdatedDate");
                                        String message = jsonObject.getString("message");
                                        org.by9steps.shadihall.model.item3name.UpdatedDate update = new UpdatedDate();
                                        update.setDate(UpdatedDate);
                                        c.setUpdatedDate(update);
                                        long idd = databaseHelper.UpdateDataForSalePur2(c.getID() + "", c, true);
                                        if (idd != -1)
                                            MNotificationClass.ShowToast(context, "Updated Data");
                                        else
                                            MNotificationClass.ShowToast(context, "Sqlite Not Updated");
                                    } else {
                                        MNotificationClass.ShowToast(context, "Not Updated Record " + c.getID());

                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            count++;
                            if (count >= total) {
                                salePur1funListener.FinishCallBackmethod("1", "2updateSalePur2");
                            }
                            // sendSalePur1DataToServer();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //////////////////////////////////Calling Next Methos
                    //sendSalePur1DataToServer();
                    count++;
                    if (count >= total) {
                        salePur1funListener.FinishCallBackmethod("0", "2updateSalePur2");
                    }
                    mProgress.dismiss();
                    Log.e("Error", error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();

                    params.put("SalePur1ID", c.getSalePur1ID() + "");
                    params.put("ItemSerial", c.getItemSerial() + "");
                    params.put("EntryType", c.getEntryType());
                    params.put("Item3NameID", c.getItem3NameID() + "");
                    params.put("ItemDescription", c.getItemDescription());
                    params.put("QtyAdd", c.getQty());
                    params.put("QtyLess", c.getQtyLess());
                    params.put("Price", c.getPrice());
                    params.put("Location", c.getLocation());
                    params.put("ClientID", c.getClientID() + "");
                    params.put("ClientUserID", c.getClientUserID() + "");
                    params.put("NetCode", c.getNetCode());
                    params.put("SysCode", c.getSysCode());
                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }


        /////////////As update DAta to cloud not have problem servver deny the request it can
        ///call asynchoronausly

        if (count >= total) {
            salePur1funListener.FinishCallBackmethod("1", "2updateSalePur2");
        }
        //  sendSalePur1DataToServer();

    }

    ///////////////////////////////Send New Data from sqlite To server
    private void sendSalePur1DataToServer(final SalePur1funListener salePur1funListener) {
        total = count = 0;
        Log.e("status", "Flag5");

        Log.e("sendSalePur1Data", "Flag 1 MathodName::sendSalePur1DataToServer");

        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
            mProgress.setMessage("Loading...  Method:sendSalePur1DataToServer CLI " + prefrence.getClientIDSession());

        String query = "SELECT * FROM SalePur1 WHERE  SalePur1ID < 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
        final List<Salepur1> salepur1List = databaseHelper.getSalePur1Data(query);
        Log.e("sendSalePur1Data", "Size" + String.valueOf(salepur1List.size()));
        total = salepur1List.size();
        for (final Salepur1 data : salepur1List) {
            mProgress.show();
            String tag_json_obj = "json_obj_req";
            String url = ApiRefStrings.SendSalePur1DataToCloud;

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            mProgress.dismiss();
                            Log.e("sendSalePur1Data", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("sendSalePur1Data", success);
                                if (success.equals("1")) {
                                    String id = jsonObject.getString("SalePur1Id");
                                    String updated_date = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    int staid = databaseHelper.UpdateSalePur1Data(data.getID(), id, updated_date, null);
                                    int upidcashbk = -1;
                                    if (staid > 0) {
                                        databaseHelper.UpdateSalePur1IdInSalePur2Table(data.getSalePur1ID(),
                                                data.getEntryType(),
                                                data.getClientID(),
                                                id);
                                        upidcashbk = refdb.SlePur1.updateSalePur1IdinCashbook(databaseHelper,
                                                data.getClientID() + "",
                                                data.getEntryType() + "",
                                                data.getSalePur1ID() + "",
                                                id + "");
                                    }
                                    if (GenericConstants.IS_DEBUG_MODE_ENABLED) {
                                        Toast.makeText(context, staid + " Recourd Updated", Toast.LENGTH_SHORT).show();
                                        Log.e("sendSalePur1Data", "pkID:" + data.getID() + " Status:" + staid + " cash bok status " + upidcashbk);
                                    }
                                    // getUpdateGridStatus();

                                } else {
                                    //databaseHelper.deleteAccount3NameEntry("DELETE FROM Account3Name WHERE ID = " + c.getId());
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
                                salePur1funListener.FinishCallBackmethod("1", "3sendSalePur1DataToServer");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgress.dismiss();
                    count++;
                    if (count >= total) {
                        salePur1funListener.FinishCallBackmethod("1", "3sendSalePur1DataToServer");
                    }
                    GenericConstants.ShowDebugModeDialog(context,
                            "Error", error.getMessage());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();

                    Log.e("sendSalePur1Data", "Set Pram ::" + data.toString());
                    params.put("EntryType", data.getEntryType());
                    params.put("SPDate", data.getSPDate().getDate());
                    params.put("AcNameID", String.valueOf(data.getAcNameID()));
                    params.put("Remarks", data.getRemarks());
                    params.put("ClientID", String.valueOf(data.getClientID()));
                    params.put("ClientUserID", String.valueOf(data.getClientUserID()));
                    params.put("NetCode", data.getNetCode());
                    params.put("SysCode", data.getSysCode());
                    params.put("NameOfPerson", data.getNameOfPerson());
                    params.put("PayAfterDay", String.valueOf(data.getPayAfterDay()));

                    return params;
                }
            };

            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }

        if (count >= total) {
            salePur1funListener.FinishCallBackmethod("1", "3sendSalePur1DataToServer");
        }

        mProgress.dismiss();
        //  MNotificationClass.ShowToast(context, "All Done");
        if (salepur1List.size() < 0)
            MNotificationClass.ShowToastTem(context, "No inserted data in sqlite");


    }

    private void sendSalePur2DataToServer(final SalePur1funListener salePur1funListener) {
        Log.e("status", "Flag6");
        count = total = 0;

        Log.e("sendSalePur2Data", "Flag 1 MathodName::sendSalePur2DataToServer");

        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
            mProgress.setMessage("Loading...  Method:sendSalePur2DataToServer CLI " + prefrence.getClientIDSession());

        String query = "SELECT * FROM SalePur2 WHERE  ItemSerial < 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
        final List<SalePur2> salePur2List = databaseHelper.getSalePur2Data(query);
        Log.e("sendSalePur2Data", "New Item Inserted Size:" + String.valueOf(salePur2List.size()));
        total = salePur2List.size();
        for (final SalePur2 salePur2 : salePur2List) {

            mProgress.show();
            String tag_json_obj = "json_obj_req";
            Log.e("sendSalePur2Data", "++SendItem:" + salePur2.toString());
            String url = ApiRefStrings.SendSalePur2DataToCloud;

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mProgress.dismiss();
                            Log.e("sendSalePur2Data", "++Response:" + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("sendSalePur2Data", success);
                                if (success.equals("1")) {
                                    String id = jsonObject.getString("ItemSerial");
                                    String updated_date = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    int staid = databaseHelper.UpdateSalePur2Data(salePur2.getID(), id, updated_date);

                                    if (GenericConstants.IS_DEBUG_MODE_ENABLED) {
                                        Toast.makeText(context, staid + " Recourd Updated", Toast.LENGTH_SHORT).show();
                                        Log.e("sendSalePur2Data", "pkID:" + salePur2.getID() + " Status:" + staid);
                                    }
                                    // getUpdateGridStatus();

                                } else {
                                    //databaseHelper.deleteAccount3NameEntry("DELETE FROM Account3Name WHERE ID = " + c.getId());
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
                                salePur1funListener.FinishCallBackmethod("1", "3sendSalePur2DataToServer");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgress.dismiss();
                    count++;
                    if (count >= total) {
                        salePur1funListener.FinishCallBackmethod("1", "3sendSalePur2DataToServer");
                    }
                    GenericConstants.ShowDebugModeDialog(context,
                            "Error", error.getMessage());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();

                    Log.e("sendSalePur2Data", "Set Pram ::" + salePur2.toString());
                    /////////////////////////////
                    params.put("SalePur1ID", String.valueOf(salePur2.getSalePur1ID()));
                    params.put("EntryType", salePur2.getEntryType());
                    params.put("ItemDescription", salePur2.getItemDescription());
                    params.put("QtyAdd", salePur2.getQtyAdd());
                    params.put("QtyLess", salePur2.getQtyLess());
                    params.put("Price", salePur2.getPrice());
                    params.put("Location", salePur2.getLocation());
                    params.put("ClientID", String.valueOf(salePur2.getClientID()));
                    params.put("ClientUserID", String.valueOf(salePur2.getClientUserID()));
                    params.put("NetCode", salePur2.getNetCode());
                    params.put("SysCode", salePur2.getSysCode());
                    params.put("Item3NameID", String.valueOf(salePur2.getItem3NameID()));

                    return params;
                }
            };

            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
        mProgress.dismiss();
        //MNotificationClass.ShowToast(context, "All Done");
        if (count >= total) {
            salePur1funListener.FinishCallBackmethod("1", "3sendSalePur2DataToServer");
        }

    }

    public void trigerAllSalpur1and2Method() {
        getSalePur1UpdatedEditedDataFromServer(new SalePur1funListener() {
            @Override
            public void FinishCallBackmethod(String success, String funType) {
                Log.e("funcallingsatus", "Flag1 Suces:" + success + " funType:" + funType);
                getSalePur2UpdatedEditedDataFromServer(callingSecond2MethodForUpdate);
            }
        });
    }

    SalePur1funListener callingSecond2MethodForUpdate = new SalePur1funListener() {
        @Override
        public void FinishCallBackmethod(String success, String funType) {
            Log.e("funcallingsatus", "Flag2 Suces:" + success + " funType:" + funType);
            updateSalePur1(new SalePur1funListener() {
                @Override
                public void FinishCallBackmethod(String success, String funType) {
                    Log.e("funcallingsatus", "Flag3 Suces:" + success + " funType:" + funType);
                    updateSalePur2(callingmethdtoSendOnServer);
                }
            });
        }
    };
    SalePur1funListener callingmethdtoSendOnServer = new SalePur1funListener() {
        @Override
        public void FinishCallBackmethod(String success, String funType) {
            Log.e("funcallingsatus", "Flag4 Suces:" + success + " funType:" + funType);
            sendSalePur1DataToServer(new SalePur1funListener() {
                @Override
                public void FinishCallBackmethod(String success, String funType) {
                    Log.e("funcallingsatus", "Flag5 Suces:" + success + " funType:" + funType);
                    sendSalePur2DataToServer(new SalePur1funListener() {
                        @Override
                        public void FinishCallBackmethod(String success, String funType) {
                            Log.e("funcallingsatus", "Flag6 Suces:" + success + " funType:" + funType);
                            if (finallistnerForAlldone != null) {
                                finallistnerForAlldone.FinishCallBackmethod(success,
                                        funType);
                            }
                        }
                    });
                }
            });
        }
    };
}
