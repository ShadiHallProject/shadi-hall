package org.by9steps.shadihall.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.chartofaccountdialog.VehicleBookingDialog;
import org.by9steps.shadihall.helper.ApiRefStrings;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.Vehicle2Name;
import org.by9steps.shadihall.model.Vehicle2NameData;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleBookingFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "VehicleBookingFragment";

    Prefrence prefrence;
    DatabaseHelper helper;
    ProgressDialog mProgress;
    int counter = 0, totalitem = 0;



    ImageView btnAdd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.vehicle_booking_fragment,container,false);
        btnAdd=view.findViewById(R.id.btnAddItem);btnAdd.setOnClickListener(this);

        prefrence=new Prefrence(getContext());
        helper=new DatabaseHelper(getContext());
        fillGridView();

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.btnAddItem){
            Bundle bundle=new Bundle();
            bundle.putString("type","New");
            VehicleBookingDialog dialog=new VehicleBookingDialog();
            dialog.setArguments(bundle);
            dialog.show(getFragmentManager(),"TAG");
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.cb_menu, menu);
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        } else if (item.getItemId() == R.id.action_print) {
            mProgress = new ProgressDialog(getContext());
            mProgress.setMessage("Loading....");
            mProgress.show();

            updateVehicle2NameOnSever();

            //getVehicle2NameUpdatedEditedDataFromServer();

        } else if (item.getItemId() == R.id.action_refresh) {

            sendtoserver();

        }
        return super.onOptionsItemSelected(item);
    }




    //sending data to server

    private void sendtoserver() {
        mProgress = new ProgressDialog(getContext());
        mProgress.setMessage("Loading....");
        mProgress.show();

        MNotificationClass.ShowToastTem(getContext(),prefrence.getClientIDSession());

        sendVehicle2NameDataToServer();
    }

//sending local sqlite data to server
    public void sendVehicle2NameDataToServer(){


        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
           mProgress.setMessage("Loading...  Method:sendVehicle2NameDataToServer CID" + prefrence.getClientIDSession());

        String query = "SELECT * FROM Vehicle2Name WHERE  VehicleID <= 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";


        final List<Vehicle2Name> vehicle2NameList=helper.getVehicle2Name(query);

        MNotificationClass.ShowToastTem(getContext(),""+vehicle2NameList.size());



        totalitem=vehicle2NameList.size();
        Log.e(TAG, "sendVehicle2NameDataToServer: "+totalitem+" list "+vehicle2NameList );
        for(final Vehicle2Name data:vehicle2NameList){
            Log.d(TAG, "sendVehicle2NameDataToServer: inside  for loop");
            mProgress.show();
            String tag_json_obj = "json_obj_req";
            String url= ApiRefStrings.SendVehicle2NameDataToServer;
            StringRequest jsonObjectRequest=new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: ");
                            counter++;
                            mProgress.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {

                                    String id = jsonObject.getString("VehicleID");
                                    String updated_date = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");

                                    int staid = helper.updateVehicle2Name(data.getID(), id, updated_date, null);
                                    if(staid>0){
                                        //updata in to second table
                                    }
                                    if (GenericConstants.IS_DEBUG_MODE_ENABLED) {
                                        Toast.makeText(getContext(), staid + " Recourd Updated", Toast.LENGTH_SHORT).show();
                                        Log.e("Vehicle2Name", "pkID:" + data.getID() + " Status:" + staid);
                                    }
                                    //update gridview

                                }else {
                                    String message = jsonObject.getString("message");
                                    if (GenericConstants.IS_DEBUG_MODE_ENABLED)
                                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                }




                            }
                            catch (JSONException e){
                                MNotificationClass.ShowToastTem(getContext(), e.getMessage() + " -" + response);
                                e.printStackTrace();

                            }
                            if (counter >= totalitem){
                                sendVehicle2NameDataToServer();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    mProgress.dismiss();
                    GenericConstants.ShowDebugModeDialog(getContext(),
                            "Error on Vehicle1Group ", error.getMessage());

                }
            })
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("VehicleID",data.getClientID());
                    params.put("VehicleGroupID",data.getVehicleGroupID());
                    params.put("VehicleName",data.getVehicleName());
                    params.put("Brand",data.getBrand());
                    params.put("Model",data.getModel());
                    params.put("Colour",data.getColour());
                    params.put("RegistrationNo",data.getRegistrationNo());
                    params.put("Account3ID",data.getAccount3ID());
                    params.put("Status",data.getStatus());
                    params.put("Lng",data.getLng());
                    params.put("Lat",data.getLat());
                    params.put("ContactNo",data.getContactNo());
                    params.put("SerialNo",data.getSerialNo());
                    params.put("UpdatedDate",data.getUpdatedDate());
                    params.put("ClientID",data.getClientID());
                    params.put("ClientUserID",data.getClientUserID());
                    params.put("NetCode",data.getNetCode());
                    params.put("SysCode",data.getSysCode());
                    return params;
                }
            };

            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);



        }

        mProgress.dismiss();
        MNotificationClass.ShowToast(getContext(), "All Done");
        if (vehicle2NameList.size() < 0)
            MNotificationClass.ShowToastTem(getContext(),"No inserted data in sqlite");

    }

//Send Edited Record From Sqlite To server
    public void updateVehicle2NameOnSever() {
        Log.e("status", "Flag3");
        totalitem = counter = 0;
        Log.e("updateVehicle2Name", "Flag 3 MathodName::updateVehicle2Name");
        mProgress.show();
        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
            mProgress.setMessage("Loading... updateVehicle2Name UpDateRecordFromSqliteToCloud Method:updateVehicle2Name CLI " + prefrence.getClientIDSession());

        String query = "SELECT * FROM Vehicle2Name WHERE ClientID = " + prefrence.getClientIDSession() + " AND VehicleID >= 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
        //final List<Salepur1> salepur1s = refdb.SlePur1.GetSalePur1Data(helper, query);

        final List<Vehicle2Name> vehicle2NameList =helper.getVehicle2Name(query);

        Log.e("updateVehicle2Name", String.valueOf(vehicle2NameList.size()));

        totalitem = vehicle2NameList.size();
        for (final Vehicle2Name date : vehicle2NameList) {
            Log.e("updateSalePur1", "DAta" + date.toString());
            String tag_json_obj = "json_obj_req";
            String url = ApiRefStrings.UpdateDataVehicle2NameOnServer;

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            counter++;
                            mProgress.dismiss();
                            Log.e("updateVehicle2Name", response);
                            try {
                                Log.e("try ", "Inside try" );
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("updateVehicle2Name", success);
                                if (success.equals("1")) {
                                    Log.e("try  if ", "success 1" );
                                    int effectrow = jsonObject.getInt("roweffected");
                                    if (effectrow != 0) {
                                        String updatec = jsonObject.getString("UpdatedDate");
                                        String message = jsonObject.getString("message");

                                        long idd=helper.updateVehicle2Name(date.getID(),null,updatec,null);

                                        if (idd != -1)
                                            MNotificationClass.ShowToast(getContext(), "Sqlite Updated Data");
                                        else
                                            MNotificationClass.ShowToast(getContext(), "Sqlite Not Updated");
                                    } else {
                                        MNotificationClass.ShowToast(getContext(), "Not Updated Record " + date.getID());
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //  updateSalePur2();
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgress.dismiss();
                    Log.e("updateVehicle2Name", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();

                    params.put("VehicleID",date.getVehicleID());
                    params.put("VehicleGroupID",date.getVehicleGroupID());
                    params.put("VehicleName",date.getVehicleName());
                    params.put("Brand",date.getBrand());
                    params.put("Model",date.getModel());
                    params.put("Colour",date.getColour());
                    params.put("RegistrationNo",date.getRegistrationNo());
                    params.put("Account3ID",date.getAccount3ID());
                    params.put("Status",date.getStatus());
                    params.put("Lng",date.getLng());
                    params.put("Lat",date.getLat());
                    params.put("ContactNo",date.getContactNo());
                    params.put("SerialNo",date.getSerialNo());
                    params.put("UpdatedDate",date.getUpdatedDate());
                    params.put("ClientID",date.getClientID());
                    params.put("ClientUserID",date.getClientUserID());
                    params.put("NetCode",date.getNetCode());
                    params.put("SysCode",date.getSysCode());
                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }

        if (vehicle2NameList.size() <= 0 || counter >= totalitem) {
            //send method to call
        }


    }

//GEt Data from server that is edited or new Inserted
    private void getVehicle2NameUpdatedEditedDataFromServer() {

        Log.e("status", "inside function");
        final String TAG = "getVehicle2NameUpdatedEditedDataFromServer";
        mProgress.setMessage("Please wait...Vehicle2Name");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetVehicle2NameEditedDataFromServer;

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("try  ", "inside try");
                            if (response != null) {
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                Log.e("Vehicle2Name ", response);
                                Vehicle2NameData vehicle2NameData=gson.fromJson(response,Vehicle2NameData.class);
//                                Log.e("TAG", vehicle2NameData.getSuccess() + " " + vehicle2NameData.getVehicle2Names().size());
                                MNotificationClass.ShowToastTem(getContext(),"" +vehicle2NameData.getSuccess());
                                if (vehicle2NameData.getSuccess() != 0) {
                                    Log.e("if  ", "inside if");
                                    for (Vehicle2Name vehicle2Name : vehicle2NameData.getVehicle2Names()) {
                                        Log.e("getVehicle2NameDataList", " " + vehicle2Name.toString());
                                        long idd = helper.updateVehicle2NameTablefromserver(vehicle2Name);
                                        Log.e("iddddddd  ", ""+idd);
                                        if (idd <= 0)
                                            helper.createVehicle2Name(vehicle2Name);
                                        Log.e("getVehicle2Name", "inser id::" + idd);
                                    }
                                    Log.e("getVehicle2NameData", "onResponse: " + vehicle2NameData.getVehicle2Names());


                                } else {
                                    MNotificationClass.ShowToast(getContext(), "No Data Found in Vehicle2Name");
                                }
                            }

                            Log.e("getVehicle2NameData", "onResponse: " + response);

                            mProgress.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                       // getSalePur2UpdatedEditedDataFromServer();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //GenericConstants.ShowDebugModeDialog(getContext(), "Error Vehicle2Name", error.getMessage());
                MNotificationClass.ShowToast(getContext(),error.getMessage());
                mProgress.dismiss();
                Log.e("Error", error.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                String date = helper.getVehicle2NameMaxUpdatedDate(prefrence.getClientIDSession());
                date = date.trim();
                date = date.substring(0, date.length() - 3);
                Map<String, String> params = new HashMap<String, String>();
                Log.e("getVehicle2NameData", "Max Vehicle2Name " + date);
                Log.e("ClinetID",prefrence.getClientIDSession());

                params.put("ClientID", prefrence.getClientIDSession());
                params.put("SessionDate", date);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }



    public void fillGridView(){
        AddVehicleGridView addVehicleGridView=new AddVehicleGridView();
        getChildFragmentManager().beginTransaction()
                .add(R.id.GVcontainer,addVehicleGridView)
                .addToBackStack(null)
                .commit();
    }

}
