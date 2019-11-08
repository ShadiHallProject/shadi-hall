package org.by9steps.shadihall.callingapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

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
import org.by9steps.shadihall.model.Bookings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingApis implements ApiGenericSequence {

    public BookingApis(Context context, ProgressDialog mProgress, DatabaseHelper databaseHelper, Prefrence prefrence) {
        this.context = context;
        this.mProgress = mProgress;
        this.databaseHelper = databaseHelper;
        this.prefrence = prefrence;
    }

    public interface BookingApisListener {
        public void FinishBookingCallBackMethod(String success, String funType);
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

    @Override
    public void SendNewInsertedDataToCloudDB(Object callbackmethod) {
        count=total=0;
        final BookingApisListener listener = (BookingApisListener) callbackmethod;
        TAG = "bookingsndnewdata";
        String query = "SELECT * FROM Booking WHERE BookingID < 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
        final List<Bookings> addBooking = databaseHelper.getBookings(query);
        Log.e(TAG, String.valueOf(addBooking.size()));
total=addBooking.size();
        for (final Bookings c : addBooking) {
            String tag_json_obj = "json_obj_req";
            String url = ApiRefStrings.BOOKING_ADD_NEW_DATA_TO_SERVER;

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
                                    String id = jsonObject.getString("BookingID");
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE Booking SET BookingID = '" + id + "', UpdatedDate = '" + UpdatedDate + "' WHERE ID = " + c.getId());
                                    // updatedDate = UpdatedDate;
//                                    List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Bookings");
//                                    for (TableSession s : se){
//                                        s.setMaxID(id);
//                                        s.setInsertDate(UpdatedDate);
//                                        s.save();
//                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.e(TAG, "CashBook7");
//                            addAccount3Name();
                            //mProgress.dismiss();
                            count++;
                            if(count>=total){
                                listener.FinishBookingCallBackMethod("0","newinsertion");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    count++;
                    if(count>=total){
                        listener.FinishBookingCallBackMethod("1","newinsertion");
                    }
                    //mProgress.dismiss();
                    Log.e("Error", error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
//                    ClientName
//                            ClientMobile
//                    ClientAddress
//                            ClientNic
//                    EventName
//                            BookingDate
//                    EventDate
//                            ArrangePersons
//                    ChargesTotal
//                            Description
//                    ClientID
//                            ClientUserID
//                    NetCode
//                            SysCode
//                    Shift
//                            SerialNo
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
                    params.put("ClientID", prefrence.getClientIDSession());
                    params.put("ClientUserID", prefrence.getClientUserIDSession());
                    params.put("NetCode", "0");
                    params.put("SysCode", "0");
//                    params.put("DebitAccount", cashID);
//                    params.put("CreditAccount", incomeID);
                    // params.put("Amount", c.getAmount());
                    params.put("Shift", c.getShift());
                    params.put("SerialNo", c.getSerialNo());

                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }

        if(count>=total){
            listener.FinishBookingCallBackMethod("1","newinsertion");
        }

    }

    @Override
    public void SendUpdatedDataFromSqliteToCloud(Object callbackmethod) {
        TAG="sendupdatedta";
        final BookingApisListener listener = (BookingApisListener) callbackmethod;
        count=total=0;
        String query = "SELECT * FROM Booking WHERE UpdatedDate = '"+GenericConstants.NullFieldStandardText+"' AND BookingID >0";
        final List<Bookings> addBooking = databaseHelper.getBookings(query);
        Log.e(TAG, "Size::"+String.valueOf(addBooking.size()));
         total=addBooking.size();
        for (final Bookings c : addBooking){
            String tag_json_obj = "json_obj_req";
            String url = ApiRefStrings.BOOKING_ADD_UPDATED_DATA_TO_SERVER;

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG,response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e(TAG,success);
                                if (success.equals("1")){
                                    String UpdatedDate = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    databaseHelper.updateCashBook("UPDATE Booking SET UpdatedDate = '"+UpdatedDate+"' WHERE ID = "+c.getId());
//

//                                    updatedDate = UpdatedDate;
//                                    FetchFromDb();
//                                    List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Bookings");
//                                    for (TableSession s : se){
//                                        s.setUpdateDate(UpdatedDate);
//                                        s.save();
//                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            count++;
                            if(count>=total){
                                listener.FinishBookingCallBackMethod("1","2UpdatedDataToCloud");
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    count++;
                    if(count>=total){
                        listener.FinishBookingCallBackMethod("1","2UpdatedDataToCloud");
                    }
                   // mProgress.dismiss();
                    Log.e(TAG,error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();

                    params.put("BookingID", c.getBookingID());
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
                    params.put("ClientID", prefrence.getClientIDSession());
                    params.put("ClientUserID", prefrence.getClientUserIDSession());
                    params.put("NetCode", "0");
                    params.put("SysCode", "0");
                    params.put("Shift", c.getShift());

                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
        if(count>=total){
            listener.FinishBookingCallBackMethod("1","2UpdatedDataToCloud");
        }
    }

    @Override
    public void getNewInsertedDataFromServer(Object callbackmethod) {
        count=total=0;
        total=1;
        TAG="getnewinsertedFromse";
        final BookingApisListener listener = (BookingApisListener) callbackmethod;

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String url = ApiRefStrings.BOOKING_GET_UPDATED_EDITED_FROM_SERVER;


        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG,response);
                        String text = "", BookingID = "", ClientName = "", ClientMobile = "", ClientAddress = "", ClientNic = "", EventName = "", BookingDate = "", EventDate = "",
                                ArrangePersons ="", ChargesTotal = "",Description = "", ClientID ="", ClientUserID = "", NetCode = "",SysCode = "", UpdatedDate = "";
                        try {
                            JSONObject json = new JSONObject(response);
                            String success = json.getString("success");

                            if (success.equals("1")) {
                                JSONArray jsonArray = json.getJSONArray("Bookings");
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
                                    Log.e(TAG+"TEST",EventDate);
                                    ArrangePersons = jsonObject.getString("ArrangePersons");
                                    ChargesTotal = jsonObject.getString("ChargesTotal");
                                    Description = jsonObject.getString("Description");
                                    ClientID = jsonObject.getString("ClientID");
                                    ClientUserID = jsonObject.getString("ClientUserID");
                                    NetCode = jsonObject.getString("NetCode");
                                    SysCode = jsonObject.getString("SysCode");
                                    Log.e(TAG+"TEST","TEST");
                                    String up = jsonObject.getString("UpdatedDate");
                                    JSONObject jbb = new JSONObject(up);
                                    UpdatedDate = jbb.getString("date");
                                    String SessionDate = jsonObject.getString("SessionDate");
                                    String Shift = jsonObject.getString("Shift");
                                    String SerialNo = jsonObject.getString("SerialNo");

                                    databaseHelper.createBooking(new Bookings(BookingID,ClientName,ClientMobile,ClientAddress,ClientNic,EventName,BookingDate,EventDate,ArrangePersons,ChargesTotal,Description,ClientID,ClientUserID,NetCode,SysCode,UpdatedDate,Shift, SerialNo));
                                   // updatedDate = SessionDate;
//                                    if (i == jsonArray.length() - 1) {
//                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Bookings");
//                                        for (TableSession s : se){
//                                            s.setMaxID(BookingID);
//                                            s.setInsertDate(SessionDate);
//                                            s.save();
//                                        }
//                                    }
                                   /// FetchFromDb();
                                }

//                                FetchFromDb();
//                                pDialog.dismiss();
                            }else {
//                                pDialog.dismiss();
                            }Log.e(TAG,"CashBook4");
                            //updateBookings();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        count++;
                        if(count>=total){
                            listener.FinishBookingCallBackMethod("1","3GetNewInsertedDataFromCloud");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                count++;
                if(count>=total){
                    listener.FinishBookingCallBackMethod("1","3GetNewInsertedDataFromCloud");
                }
                Log.e("Error", error.toString());
//                pDialog.dismiss();
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ClientID", prefrence.getClientIDSession());
                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(BookingID AS Int)) FROM Booking");
                params.put("MaxID",String.valueOf(maxID));
                Log.e(TAG," SEndTosERsver:MAXID:"+maxID+" ClientID:"+prefrence.getClientIDSession());
                return params;
            }
        };

        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

        if(count>=total){
            listener.FinishBookingCallBackMethod("1","3GetNewInsertedDataFromCloud");
        }
    }

    @Override
    public void getUpdatedDataFromServer(Object callbackmethod) {
        total=1;count=0;
        TAG="getupdatedadta";
        final BookingApisListener listener = (BookingApisListener) callbackmethod;
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String url  = ApiRefStrings.BOOKING_GET_UPDATED_EDITED_FROM_SERVER;

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String text = "", BookingID = "", ClientName = "", ClientMobile = "", ClientAddress = "", ClientNic = "", EventName = "", BookingDate = "", EventDate = "",
                                ArrangePersons ="", ChargesTotal = "",Description = "", ClientID ="", ClientUserID = "", NetCode = "",SysCode = "", UpdatedDate = "";
                        try {
                            JSONObject json = new JSONObject(response);
                            String success = json.getString("success");
                            Log.e(TAG,json.toString());

                            if (success.equals("1")) {
                                JSONArray jsonArray = json.getJSONArray("Bookings");
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
                                            +"', EventName = '"+EventName+"', BookingDate = '"+BookingDate+"', EventDate = '"+EventDate+"', ArrangePersons ='"+ArrangePersons+"', ChargesTotal = '"+ChargesTotal+"', Description = '"+Description
                                            +"', ClientID = '"+ClientID+"', ClientUserID = '"+ClientUserID+"', NetCode = '"+NetCode+"', SysCode = '"+SysCode+"', UpdatedDate = '"+UpdatedDate+"' WHERE BookingID = "+ BookingID;
                                    databaseHelper.updateCashBook(query);
//                                    updatedDate = SessionDate;
//                                    FetchFromDb();
//                                    if (i == jsonArray.length() - 1) {
//                                        List<TableSession> se = TableSession.find(TableSession.class,"table_Name = ?","Bookings");
//                                        for (TableSession s : se){
////                                            s.setMaxID(BookingID);
//                                            s.setUpdateDate(SessionDate);
//                                            s.save();
//                                        }
//                                    }
                                }


//                                FetchFromDb();
//                                mProgress.dismiss();
                            }else {
//                                mProgress.dismiss();
                            }Log.e(TAG,"CashBook5");
                           // addCashBook();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        count++;
                        if(count>=total){
                            listener.FinishBookingCallBackMethod("1","4GetNewUpdatedDataFromServer");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                count++;
                if(count>=total){
                    listener.FinishBookingCallBackMethod("0","4GetNewUpdatedDataFromServer");
                }
                Log.e(TAG, error.toString());
//                pDialog.dismiss();
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("ClientID", prefrence.getClientIDSession());
                int maxID = databaseHelper.getMaxValue("SELECT max(CAST(BookingID AS Int)) FROM Booking");
                params.put("MaxID",String.valueOf(maxID));
                String date = refdb.BookingTable.GetMaximumUpdatedDateFromBooking(databaseHelper,prefrence.getClientIDSession());
                if(date!=null){
                    int index=date.lastIndexOf('.');
                    if(index>0)
                        date=date.substring(0,index+4);
                }else{
                    date="0";
                }


                params.put("SessionDate",date);
                Log.e(TAG," SEndTosERsver:MAXID:("+maxID+") ClientID:("+prefrence.getClientIDSession()+")SessionDate"+date);

                return params;
            }
        };

        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

        if(count>=total){
            listener.FinishBookingCallBackMethod("1","4GetNewUpdatedDataFromServer");
        }
    }

    @Override
    public void trigerAllMethodInRow(Object finalCallbackmethod) {
        final BookingApisListener listener = (BookingApisListener) finalCallbackmethod;
        final BookingApis refob=new BookingApis(context,mProgress,databaseHelper,prefrence);
        refob.getUpdatedDataFromServer(new BookingApis.BookingApisListener(){
            @Override
            public void FinishBookingCallBackMethod(String success, String funType) {

                Log.e("flag","Done1--"+funType);
                refob.SendUpdatedDataFromSqliteToCloud(new BookingApis.BookingApisListener(){
                    @Override
                    public void FinishBookingCallBackMethod(String success, String funType) {
                        Log.e("flag","Done2--"+funType);
                        refob.getNewInsertedDataFromServer(new BookingApis.BookingApisListener(){
                            @Override
                            public void FinishBookingCallBackMethod(String success, String funType) {
                                Log.e("flag","Done3--"+funType);
                                refob.SendNewInsertedDataToCloudDB(new BookingApis.BookingApisListener(){
                                    @Override
                                    public void FinishBookingCallBackMethod(String success, String funType) {
                                        Log.e("flag","Done4--"+funType);
                                      listener.FinishBookingCallBackMethod("1","All Done");
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
