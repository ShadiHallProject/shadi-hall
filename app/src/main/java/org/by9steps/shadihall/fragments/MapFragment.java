package org.by9steps.shadihall.fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.NearestListAdapter;
import org.by9steps.shadihall.helper.ApiRefStrings;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.Client;
import org.by9steps.shadihall.model.Projects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Comparator;
import java.util.Collections;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_LAT = "lat";
    private static final String ARG_LNG = "lng";
    private static final String ARG_TYPE = "type";
    private static final String ARG_TITLE = "title";

    private GoogleMap mMap;
    SearchView searchView;
    MarkerOptions marker;
    ProgressDialog mProgress;
    RecyclerView recyclerView;
    LinearLayout layout;
    NearestListAdapter adapter;
    DatabaseHelper databaseHelper;

    LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;

    List<Client> mList;
    List<Projects> projectsList;

    private Float ZOOM = 12f;

    String lat = null, lng = null, type = "no", cTitle;


    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(String Lat, String Lng, String type, String cTitle){
        MapFragment frag = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LAT, Lat);
        args.putString(ARG_LNG, Lng);
        args.putString(ARG_TYPE, type);
        args.putString(ARG_TITLE, cTitle);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            lat = getArguments().getString(ARG_LAT);
            lng = getArguments().getString(ARG_LNG);
            type = getArguments().getString(ARG_TYPE);
            cTitle = getArguments().getString(ARG_TITLE);
            Log.e("TYPEEE",type);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        layout = view.findViewById(R.id.recycler_layout);

        databaseHelper = new DatabaseHelper(getContext());

        if (type.equals("hide")){
            layout.setVisibility(View.GONE);
        }

        lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);

        if (isGpsEnabled()){
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        mList = new ArrayList<>();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Toast.makeText(getContext(), "Map Ready", Toast.LENGTH_SHORT).show();

        getDeviceLocation();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                String title = "Selected Location";
                mMap.clear();
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    Address obj ;
                    if (addresses != null) {
                        obj = addresses.get(0);
//                        title = obj.getSubLocality()+obj.getFeatureName();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.e("LAT+LNG", lat+lng);
                if (!type.equals("hide")){
                    moveCamera(latLng, ZOOM, title);
                    Log.e("CROWN",String.valueOf(latLng.latitude)+"   "+String.valueOf(latLng.longitude));
                    getShadiHallList(String.valueOf(latLng.latitude),String.valueOf(latLng.longitude));
                }


            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
//                if (!marker.getTitle().equals("Selected Location")) {
//                    String[] strings = marker.getId().split("m");
//
//                    FragmentManager fm = getActivity().getSupportFragmentManager();
//                    DialoogFragment editNameDialogFragment = DialoogFragment.newInstance(mList.get(Integer.valueOf(strings[1])).getClientID(),
//                            mList.get(Integer.valueOf(strings[1])).getCompanyName(),
//                            mList.get(Integer.valueOf(strings[1])).getCountry(),
//                            mList.get(Integer.valueOf(strings[1])).getCity(),
//                            mList.get(Integer.valueOf(strings[1])).getSubCity(),
//                            mList.get(Integer.valueOf(strings[1])).getWebSite(),
//                            "easysoft.com.pk",
//                            "800");
//                    editNameDialogFragment.show(fm, "fragment_edit_name");
//                }
                return false;
            }
        });
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
//                            moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), ZOOM, "My Location");
                            Log.e("My Location", location.getLatitude()+"   "+ location.getLongitude());
                            if (!type.equals("hide")) {
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), ZOOM));
                                getShadiHallList(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                            }else{
                                moveCamera(new LatLng(Double.valueOf(lat), Double.valueOf(lng)), 16, cTitle);
                            }
                        }
                    }
                });

    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        createMarker(latLng.latitude, latLng.longitude, title,"",1);
//        marker = new MarkerOptions().position(latLng).title(title);
//
//        mMap.addMarker(marker);

    }

    protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {

        return mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet));
//                .icon(BitmapDescriptorFactory.fromResource(iconResID)));
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
//                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 100);
                        }
                    });

            alertDialogBuilder.setNegativeButton(R.string.Cancel,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getContext(), "Location Must Be Enabled", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
        return gps_enabled;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            if (isGpsEnabled()){
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            }
        }
    }

    private void getShadiHallList(final String lat, final String lng){
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Please wait...");
        mProgress.setMessage("Getting Details");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        String tag_json_obj = "json_obj_req";

        //String u = "";
        String u= ApiRefStrings.GetShadiHallDetail;
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
                                mList.clear();
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
                                    createMarker(Double.valueOf(Lat), Double.valueOf(Lng), CompanyName,Country+" "+City,1);

                                }
//                                Collections.sort(mList, new DistanceComparator());
                                adapter = new NearestListAdapter(getContext(),mList,"1");
                                adapter.setOnItemClickListener(new NearestListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(String clientID, String name, String country, String city, String subCity, String website, String email, String persons, String Lat, String Lng, String number) {
                                        FragmentManager fm = getActivity().getSupportFragmentManager();
                                        DialoogFragment dialoogFragment = DialoogFragment.newInstance(clientID,
                                                name,
                                                country,
                                                city,
                                                subCity,
                                                website,
                                                email,
                                                persons,
                                                Lat,
                                                Lng,
                                                number);
                                        dialoogFragment.show(fm, "fragment_edit_name");
                                        dialoogFragment.setOnDialogueClickListener(new DialoogFragment.DialogueClickListener() {
                                            @Override
                                            public void updateMapLocation(String lat, String lng) {
                                                Log.e("DIALOGUE", lat+"    "+lng);
                                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(lat), Double.valueOf(lng)), 16));
                                            }

                                            @Override
                                            public void repllaceFragment(String id) {

                                            }
                                        });
                                    }
                                    @Override
                                    public void replaceFragment(String Lat, String Lng, String title){

                                    }

                                });
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerView.setAdapter(adapter);

                            }else {
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//                                mProgress.dismiss();
                            }
                            getProject();
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
        int socketTimeout = 20000;//10 seconds - change to what you want
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

    public void getProject(){
        String tag_json_obj = "json_obj_req";
        String u= ApiRefStrings.GetProject;

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("GetProject",response);
//                        mProgress.dismiss();
                        JSONObject jsonObj = null;

                        try {
                            jsonObj= new JSONObject(response);
                            String success = jsonObj.getString("success");
                            Log.e("Success",success);
                            if (success.equals("1")){
                                String query = "SELECT * FROM Project";
                                projectsList = databaseHelper.getProjects(query);
                                if (projectsList.size() > 0) {
                                    databaseHelper.deleteProject();
                                }
                                JSONArray jsonArray = jsonObj.getJSONArray("Project");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("Project",jsonObject.toString());
                                    String ProjectID = jsonObject.getString("ProjectID");
                                    String ProjectName = jsonObject.getString("ProjectName");
//                                    String ProjectDescription = jsonObject.getString("ProjectDescription");
                                    String ProjectType = jsonObject.getString("ProjectType");

                                    databaseHelper.createProjects(new Projects(ProjectID,ProjectName,ProjectType));
                                }

                                mProgress.dismiss();
                            }else {
                                mProgress.dismiss();
                                String message = jsonObj.getString("message");
//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        int socketTimeout = 20000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }


}
