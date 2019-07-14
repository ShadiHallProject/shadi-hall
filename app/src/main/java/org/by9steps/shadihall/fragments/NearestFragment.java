package org.by9steps.shadihall.fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.NearestListAdapter;
import org.by9steps.shadihall.model.Client;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearestFragment extends Fragment {

    RecyclerView recyclerView;
    NearestListAdapter adapter;
    ProgressDialog mProgress;

    LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;

    List<Client> mList;

    public NearestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nearest, container, false);

        lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        recyclerView = view.findViewById(R.id.recycler);

        mList = new ArrayList<>();

        if (isGpsEnabled()){
            getDeviceLocation();
        }

        return view;
    }

    public void getDeviceLocation() {

        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.e("My Location", location.getLatitude()+"   "+ location.getLongitude());
                            getShadiHallList(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                        }
                    }
                });

    }

    private void getShadiHallList(final String lat, final String lng){
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Please wait...");
        mProgress.setMessage("Getting Your Number Details");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        final String tag_json_obj = "json_obj_req";
        String u = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetShadiHallDetail.php";

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Location List",response);
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            String success = jsonObj.getString("success");
                            String message = jsonObj.getString("message");
                            if (success.equals("1")){
                                JSONArray jsonArray = jsonObj.getJSONArray("ShadiHall");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("ShadiHall",jsonObject.toString());
                                    String ClientID = jsonObject.getString("ClientID");
                                    String Lat = jsonObject.getString("Lat");
                                    String Lng = jsonObject.getString("Lng");
                                    String CompanyName = jsonObject.getString("CompanyName");
                                    String CompanyAddress = jsonObject.getString("CompanyAddress");
                                    String CompanyNumber = jsonObject.getString("CompanyNumber");
                                    String WebSite = jsonObject.getString("WebSite");
                                    String Country = jsonObject.getString("Country");
                                    String City = jsonObject.getString("City");
                                    String SubCity = jsonObject.getString("SubCity");
                                    String ProjectID = jsonObject.getString("ProjectID");
                                    String Email = jsonObject.getString("Email");
                                    String CapacityOfPersons = jsonObject.getString("CapacityOfPersons");
                                    float Distance = 0;
                                    if (Lat != null && Lng != null) {
                                        Log.e("LAT AND LNG",Lat+"   "+Lng);
                                        Distance = calcDistance(lat, lng, Lat, Lng);
                                    }
                                    Log.e("DISTANCE",String.valueOf(Distance));

                                    mList.add(new Client(ClientID,Lat,Lng, String.valueOf(Distance),CompanyName,CompanyAddress,CompanyNumber,WebSite,Country,City,SubCity,ProjectID, Email, CapacityOfPersons));

                                }
//                                Collections.sort(mList, new DistanceComparator());
                                adapter = new NearestListAdapter(getContext(),mList,"0");
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(adapter);

                                adapter.setOnItemClickListener(new NearestListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(String clientID, String name, String country, String city, String subCity, String website, String email, String persons, String Lat, String Lng, String CompanyNumber) {

                                    }

                                    @Override
                                    public void replaceFragment(String Lat, String Lng, String title) {
                                        getChildFragmentManager().beginTransaction()
                                                .add(R.id.map_container, MapFragment.newInstance(Lat,Lng,"hide",title))
                                                .addToBackStack(null)
                                                .commit();
                                    }
                                });

                                mProgress.dismiss();

                            }else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
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
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Lat", lat);
                params.put("Lng", lng);
                params.put("ProjectID", "2");
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    private float calcDistance(String lat1, String long1, String lat2, String long2){
        Location currentLoc = new Location("point A");

        currentLoc.setLatitude(Double.parseDouble(lat1));
        currentLoc.setLongitude(Double.parseDouble(long1));

        Location shadiHallLoc = new Location("point B");

        shadiHallLoc.setLatitude(Double.parseDouble(lat2));
        shadiHallLoc.setLongitude(Double.parseDouble(long2));

        float distance = shadiHallLoc.distanceTo(currentLoc);
        return distance/1000;
    }

    public class DistanceComparator implements Comparator<Client>
    {
        public int compare(Client left, Client right) {
            return left.getDistance().compareTo(right.getDistance());
        }
    }


    private Boolean isGpsEnabled(){
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setMessage(R.string.gps_network_not_enabled);
            alertDialogBuilder.setPositiveButton(R.string.open_location_settings,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });

            alertDialogBuilder.setNegativeButton(R.string.Cancel,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getShadiHallList(String.valueOf(00.00000), String.valueOf(00.00000));
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return gps_enabled;
    }

}
