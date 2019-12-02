package org.by9steps.shadihall.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MenuClickActivity;
import org.by9steps.shadihall.adapters.SectionRecyclerViewAdapter;
import org.by9steps.shadihall.helper.ApiRefStrings;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.PrefrenceResturentSeekBar;
import org.by9steps.shadihall.helper.ViewDBAllData;
import org.by9steps.shadihall.model.Restaurant2Table;
import org.by9steps.shadihall.model.ResturentSectionModel1;
import org.by9steps.shadihall.model.joinQueryForResturent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentResturent extends Fragment {


    DatabaseHelper databaseHelper;
    Prefrence prefrence;
    PrefrenceResturentSeekBar myPreference;
    private SectionRecyclerViewAdapter adapter1;


    private static int NUMBER_OF_COLS = 3;
    private RecyclerView recyclerView;
    private TextView orderConform;
    public  int order = 0;
    public static final String TABLE_BOOKED="Booked";
    public static final String TABLE_Running="Running";


    ArrayList<ResturentSectionModel1> sectionModelArrayListJoinQuery;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.activity_resturent,null);

        // filldata();
        prefrence = new Prefrence(getContext());
        databaseHelper = new DatabaseHelper(getContext());
        myPreference=new PrefrenceResturentSeekBar(getContext());

        Log.d("id", "onCreateView: "+prefrence.getClientIDSession());




        setUpRecyclerView(view);
        populateRecyclerView();



        //temp data view
        orderConform.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(getContext(), ViewDBAllData.class));
                return true;
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.sb_menu, menu);


        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView1 = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView1.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String text = s;
                adapter1.filter(text);
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            getActivity().onBackPressed();
        if(item.getItemId()== R.id.action_seekbar){
            MenuClickActivity activity=(MenuClickActivity)getContext();
            activity.tableLayoutSetting();

        }
        return super.onOptionsItemSelected(item);


    }


    //setup recycler view
    private void setUpRecyclerView(View view) {
        orderConform=(TextView)view.findViewById(R.id.TVOrderToConform);
        recyclerView = (RecyclerView)view. findViewById(R.id.sectioned_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    //populate recycler view
    private void populateRecyclerView() {

        order=0;
        String value=myPreference.getTABLE_GRID_VIEW_COL();
        NUMBER_OF_COLS=Integer.parseInt(value);
        String query="select * from Restaurant1Potion where ClientID = '"+prefrence.getClientIDSession()+"'";
        List<String> PorationList=databaseHelper.getPortationName(query);
     //   MNotificationClass.ShowToastTem(getContext(),"size portaion name list size"+PorationList.size());

        String query1="Select\n" +
                "    Restaurant2Table.TableName,\n" +
                "    Restaurant1Potion.PotionName,\n" +
                "    Restaurant2Table.TableStatus,\n" +
                "    Restaurant2Table.ClientID,\n" +
                "    Query1.BillAmount,\n" +
                "    Restaurant2Table.SalPur1ID,\n" +
                "    Restaurant2Table.TableID\n" +
                "From\n" +
                "    Restaurant2Table Left Join\n" +
                "    Restaurant1Potion On Restaurant1Potion.PotionID = Restaurant2Table.PotionID\n" +
                "            And Restaurant1Potion.ClientID = Restaurant2Table.ClientID Left Join\n" +
                "    (Select\n" +
                "         SalePur1.ClientID,\n" +
                "         SalePur1.SalePur1ID,\n" +
                "         SalePur1.BillAmount,\n" +
                "         SalePur1.BillStatus\n" +
                "     From\n" +
                "         SalePur1\n" +
                "     Where\n" +
                "         SalePur1.BillStatus = 'Running') As Query1 On Query1.SalePur1ID = Restaurant2Table.SalPur1ID\n" +
                "            And Query1.ClientID = Restaurant2Table.ClientID\n" +
                "Where\n" +
                "    Restaurant2Table.ClientID = '"+prefrence.getClientIDSession()+"'";

        List<joinQueryForResturent> TotalItemList;
        TotalItemList=databaseHelper.GetDataFromjoinQueryForResturent(query1);

        for (int i = 0; i < TotalItemList.size(); i++) {
            if( TotalItemList.get(i).getTableStatus()!=null && TotalItemList.get(i).getTableStatus().equals(TABLE_Running))
                order=order+1;
        }
        orderConform.setText(order+" Order in Queue ");

        sectionModelArrayListJoinQuery = new ArrayList<>();
        for (int i = 0; i < PorationList.size(); i++) {

            ArrayList<joinQueryForResturent> itemArrayListjoinQuery = new ArrayList<>();
            for (int j = 0; j < TotalItemList.size(); j++) {
                if(PorationList.get(i).equals(TotalItemList.get(j).getPotionName())) {
                    itemArrayListjoinQuery.add(TotalItemList.get(j));
                }
            }
            sectionModelArrayListJoinQuery.add(new ResturentSectionModel1(PorationList.get(i),itemArrayListjoinQuery));
        }
        adapter1  = new SectionRecyclerViewAdapter(getContext(), NUMBER_OF_COLS, sectionModelArrayListJoinQuery);
        recyclerView.setAdapter(adapter1);


    }

    //Apis calling for data getting
    private void filldataRestaurant2Table(){

        prefrence = new Prefrence(getContext());
        databaseHelper = new DatabaseHelper(getContext());
        get0ApiDataRestaurant2Table();
    }

    public void get0ApiDataRestaurant2Table() {
        final String TAG = "Restaurant2Table";
        ///mProgress.setMessage("Please wait...createRestaurant2Table");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetRestaurant2TableData;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // mProgress.dismiss();
                        JSONObject jsonObj = null;
                        Log.e("responce  ", response);

                        Log.d("aaaaaaaaaa", "onResponse: before try");
                        try {

                            jsonObj = new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Restaurant2Table");
                            Log.e(TAG, response);
                            String success = jsonObj.getString("success");
                            Log.d("successssssss", "onResponse: "+success);
                            if (success.equals("1")) {
                                databaseHelper.deleteRestaurant2Table();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Log.d("foooooooooooor", "onResponse: "+success);
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e(TAG, jsonObject.toString());
                                    Restaurant2Table restaurant2Table=new Restaurant2Table();
                                    restaurant2Table.setTableID(jsonObject.getString("TableID"));
                                    restaurant2Table.setPotionID(jsonObject.getString("PotionID"));
                                    restaurant2Table.setTableDescription(jsonObject.getString("TableDescription"));
                                    restaurant2Table.setTableStatus(jsonObject.getString("TableStatus"));
                                    restaurant2Table.setClientUserID(jsonObject.getString("ClientUserID"));
                                    restaurant2Table.setClientID(jsonObject.getString("ClientID"));
                                    restaurant2Table.setSysCode(jsonObject.getString("SysCode"));
                                    restaurant2Table.setNetCode(jsonObject.getString("NetCode"));
                                    restaurant2Table.setUpdatedDate(jsonObject.getString("UpdatedDate"));

                                    Log.d("befoe create", "createRestaurant2Table: fun");
                                    long iddd=databaseHelper.createRestaurant2Table(restaurant2Table);
                                    Log.e(TAG, "createRestaurant2Table InsertID " + iddd);
                                    MNotificationClass.ShowToastTem(getContext(),"createRestaurant2Table id "+iddd);
                                }
                                //getPrfojectMenu();

                            } else {
                                //getPrfojectMenu();
                                String message = jsonObj.getString("message");
                                Log.e(TAG, message);

//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            MNotificationClass.ShowToastTem(getContext(),
                                    "Restaurant2Table");
                            Log.e("Rest2Table + catch ", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
//                mProgress.dismiss();
                GenericConstants.ShowDebugModeDialog(getContext(),
                        "Error Restaurant2Table", e.getMessage());
                Log.e("Rest2Table onError", e.getMessage());
                e.printStackTrace();
                Log.e("Error", e.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", prefrence.getClientIDSession());
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }

    private void fillRestaurant1Potion(){
        get0ApiDataRestaurant1Potion();
    }
    public void get0ApiDataRestaurant1Potion(){

        final String TAG = "Restaurant1Potion";
        ///mProgress.setMessage("Please wait...createRestaurant2Table");
        String tag_json_obj = "json_obj_req";
        String u = ApiRefStrings.GetRestaurant1PotionData;

        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // mProgress.dismiss();
                        JSONObject jsonObj = null;
                        Log.e("responce  ", response);

                        Log.d("aaaaaaaaaa", "onResponse: before try");
                        try {

                            jsonObj = new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("Restaurant1Potion");
                            Log.e(TAG, response);
                            String success = jsonObj.getString("success");
                            Log.d("successssssss", "onResponse: "+success);
                            if (success.equals("1")) {
                                databaseHelper.deleteRestaurant2Table();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Log.d("foooooooooooor", "onResponse: "+success);
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e(TAG, jsonObject.toString());
                                    Restaurant2Table restaurant2Table=new Restaurant2Table();
                                    restaurant2Table.setTableID(jsonObject.getString("TableID"));
                                    restaurant2Table.setPotionID(jsonObject.getString("PotionID"));
                                    restaurant2Table.setTableDescription(jsonObject.getString("TableDescription"));
                                    restaurant2Table.setTableStatus(jsonObject.getString("TableStatus"));
                                    restaurant2Table.setClientUserID(jsonObject.getString("ClientUserID"));
                                    restaurant2Table.setClientID(jsonObject.getString("ClientID"));
                                    restaurant2Table.setSysCode(jsonObject.getString("SysCode"));
                                    restaurant2Table.setNetCode(jsonObject.getString("NetCode"));
                                    restaurant2Table.setUpdatedDate(jsonObject.getString("UpdatedDate"));

                                    Log.d("befoe create", "createRestaurant2Table: fun");
                                    long iddd=databaseHelper.createRestaurant2Table(restaurant2Table);
                                    Log.e(TAG, "createRestaurant2Table InsertID " + iddd);
                                    MNotificationClass.ShowToastTem(getContext(),"createRestaurant2Table id "+iddd);
                                }
                                //getPrfojectMenu();

                            } else {
                                //getPrfojectMenu();
                                String message = jsonObj.getString("message");
                                Log.e(TAG, message);

//                                Toast.makeText(SplashActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            MNotificationClass.ShowToastTem(getContext(),
                                    "Restaurant2Table");
                            Log.e("Rest2Table + catch ", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
//                mProgress.dismiss();
                GenericConstants.ShowDebugModeDialog(getContext(),
                        "Error Restaurant2Table", e.getMessage());
                Log.e("Rest2Table onError", e.getMessage());
                e.printStackTrace();
                Log.e("Error", e.toString());
//                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ClientID", prefrence.getClientIDSession());
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }



    //seekbar update column
    public void updateTableColumn(){
        populateRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        populateRecyclerView();
    }
}
