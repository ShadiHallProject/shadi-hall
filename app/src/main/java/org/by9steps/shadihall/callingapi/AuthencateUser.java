package org.by9steps.shadihall.callingapi;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.model.Client;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static org.by9steps.shadihall.helper.ApiRefStrings.LoginAttempApiRef;

public class AuthencateUser {

    public interface AuthListnerForClient {
        void listenerForLoginAuth(String succescode, String message, @Nullable String userright, @Nullable Client client);
    }


    public static void CheckClientActivationAndPassword(final Context cc, final String clientID, final String clientUserID, final String password, final AuthListnerForClient mlistner) {
        String tag_json_obj = "json_obj_req";
        String u = LoginAttempApiRef;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("ClientLoginRES", response);
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = new JSONObject(response);
                            String success = jsonObj.getString("success");
                            String message = jsonObj.getString("message");
                            if (success.equals("1")) {
                                JSONArray jsonArray = jsonObj.getJSONArray("UserInfo");
                                Log.e("ClientLoginUserInfo", jsonArray.toString());
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String userright = jsonObject.getString("UserRights");
                                    String cId = jsonObject.getString("ClientID");
                                    String ClientParentID = jsonObject.getString("ClientParentID");
                                    String EntryType = jsonObject.getString("EntryType");
                                    String LoginMobileNo = jsonObject.getString("LoginMobileNo");
                                    String CompanyName = jsonObject.getString("CompanyName");
                                    String CompanyAddress = jsonObject.getString("CompanyAddress");
                                    String CompanyNumber = jsonObject.getString("CompanyNumber");
                                    String NameOfPerson = jsonObject.getString("NameOfPerson");
                                    String Email = jsonObject.getString("Email");
                                    String WebSite = jsonObject.getString("WebSite");
                                    String Password = "Null";
                                    String ActiveClient = jsonObject.getString("ActiveClient");
                                    String Country = jsonObject.getString("Country");
                                    String City = jsonObject.getString("City");
                                    String SubCity = jsonObject.getString("SubCity");
                                    String CapacityOfPersons = jsonObject.getString("CapacityOfPersons");
                                    String ClientUserID = jsonObject.getString("ClientUserID");
                                    String SysCode = jsonObject.getString("SysCode");
                                    String NetCode = jsonObject.getString("NetCode");
                                    String up = jsonObject.getString("UpdatedDate");
                                    JSONObject jb = new JSONObject(up);
                                    String UpdatedDate = jb.getString("date");
                                    String Lat = jsonObject.getString("Lat");
                                    String Lng = jsonObject.getString("Lng");
                                    String ProjectID = jsonObject.getString("ProjectID");
                                    Client refclient = new Client(cId, ClientParentID, EntryType, LoginMobileNo, CompanyName,
                                            CompanyAddress, CompanyNumber, NameOfPerson, Email, WebSite, Password, ActiveClient,
                                            Country, City, SubCity, CapacityOfPersons, ClientUserID, SysCode, NetCode, UpdatedDate,
                                            Lat, Lng, ProjectID);
                                    mlistner.listenerForLoginAuth(success, message,
                                            userright, refclient);
                                }
                            } else {
                                //mProgress.dismiss();
                                mlistner.listenerForLoginAuth(success, message,
                                        null, null);
                                Toast.makeText(cc, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mlistner.listenerForLoginAuth("0", "Exception:"+e.getMessage(),
                                    null, null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // mProgress.dismiss();
                mlistner.listenerForLoginAuth("0",error.toString(),null,null);
                Log.e("Error", error.toString());
                Toast.makeText(cc, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("ClientLogin", "ClientID:" + clientID + " ClientUserID:"
                        + clientUserID + " Password:" + password);
                params.put("ClientID", clientID);
                params.put("ClientUserID", clientUserID);
                params.put("Password", password);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }
}
