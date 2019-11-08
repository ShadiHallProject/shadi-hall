package org.by9steps.shadihall.fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.CustomeAdapterForSpinner;
import org.by9steps.shadihall.chartofaccountdialog.customGroupDialog1;
import org.by9steps.shadihall.chartofaccountdialog.customGroupDialog2;
import org.by9steps.shadihall.genericgrid.MediatorClass;
import org.by9steps.shadihall.helper.ApiRefStrings;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.Account5customGroup1;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountCustomGroup extends Fragment implements View.OnClickListener {

    String [] arr;
    int [] arrIds;
    List<Account5customGroup1> list;

    DatabaseHelper databaseHelper;
    Prefrence prefrence;
    Spinner spinner;
    Button imageView,btnAddMember;
    RecyclerView recyclerView;

    ProgressDialog mProgress;
    int counter = 0, totalitem = 0;
    int itemselectindex;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_account_custom_group,container,false);

        itemselectindex=0;
        databaseHelper=new DatabaseHelper(getContext());
        prefrence=new Prefrence(getContext());
        spinner=view.findViewById(R.id.spGroupName);
        imageView=view.findViewById(R.id.addAccountCustomGroup); imageView.setOnClickListener(this);
        btnAddMember=view.findViewById(R.id.btnAddMemberInGroup);btnAddMember.setOnClickListener(this);
        recyclerView=view.findViewById(R.id.recyclerviewmgrid);



        String query="select * from Account5customGroup1 where ClientID='"+prefrence.getClientIDSession()+"'";

        list=databaseHelper.getAccount5customGroup1(query);
        arr=new String[list.size()];
        arrIds=new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i]=list.get(i).getCustomGroupName();
            arrIds[i]=list.get(i).getID();
        }

        CustomeAdapterForSpinner adapter=new CustomeAdapterForSpinner(getContext(),arr,arrIds);
        //ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,arr);
        //spinner.setAdapter(arrayAdapter);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemselectindex=position;

//                String query="select * from Account5customGroup2 where ClientID='"+prefrence.getClientIDSession()+"' AND CustomGroup1ID = '"+list.get(itemselectindex).getCustomGroup1ID()+"'";
//                Cursor cc=databaseHelper.getReadableDatabase().rawQuery(query,null);
//                Log.e("aaaaaa",""+cc.getCount());
//                final MediatorClass mediatorClass=new MediatorClass(cc,recyclerView);
//                //mediatorClass.setSortingAllowed(true);
//                mediatorClass.ShowGrid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fillgridView();

        return view;
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
            //do something
        } else if (item.getItemId() == R.id.action_refresh) {
            //do something
            sendtoserver();
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View v) {

        if(v.getId()== R.id.addAccountCustomGroup)
        {
            Bundle bundle=new Bundle();
            bundle.putString("mode","new");
            customGroupDialog1 dialog1=new customGroupDialog1();
            dialog1.setArguments(bundle);
            dialog1.show(getFragmentManager(),"TAG");

        }
        if(v.getId()== R.id.btnAddMemberInGroup){
            MNotificationClass.ShowToastTem(getContext(),list.get(itemselectindex).getCustomGroup1ID());
            Bundle bundle=new Bundle();
            bundle.putString("CustomGroup1ID",list.get(itemselectindex).getCustomGroup1ID());
            customGroupDialog2 dialog2=new customGroupDialog2();
            dialog2.setArguments(bundle);
            dialog2.show(getFragmentManager(),"TAG");

        }

    }

    public void fillgridView(){

       // String query="select * from Account5customGroup2 where ClientID='"+prefrence.getClientIDSession()+"' AND CustomGroup1ID = '"+list.get(itemselectindex).getCustomGroup1ID()+"'";

        String query1="select * from Account3CustomGroup_SelectAc where ClientID = '"+prefrence.getClientIDSession()+"'";
        Cursor cc=databaseHelper.getReadableDatabase().rawQuery(query1,null);
        Log.e("aaaaaa",""+cc.getCount());
        final MediatorClass mediatorClass=new MediatorClass(cc,recyclerView);
        mediatorClass.setSortingAllowed(true);
        mediatorClass.ShowGrid();

    }


    //sending data to server

    private void sendtoserver() {
        mProgress = new ProgressDialog(getContext());
        mProgress.setMessage("Loading....");
        mProgress.show();

        MNotificationClass.ShowToastTem(getContext(),prefrence.getClientIDSession());

        sendAccount5customGroup1DataToServer();
    }

    //sending local sqlite data to server
    public void sendAccount5customGroup1DataToServer(){


        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
            mProgress.setMessage("Loading...  Method:sendAccount5customGroup1DataToServer CID" + prefrence.getClientIDSession());

        String query = "SELECT * FROM Account5customGroup1 WHERE  CustomGroup1ID <= 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";



        final List<Account5customGroup1> account5customGroup1=databaseHelper.getAccount5customGroup1(query);

        MNotificationClass.ShowToastTem(getContext(),""+account5customGroup1.size());



        totalitem=account5customGroup1.size();
        Log.e("data", "account5customGroup1: "+totalitem+" list "+account5customGroup1 );
        for(final Account5customGroup1 data:account5customGroup1){
            Log.d("TAG", "account5customGroup1: inside  for loop");
            mProgress.show();
            String tag_json_obj = "json_obj_req";
            String url= ApiRefStrings.SendAccount5customGroup1DataToServer;
            StringRequest jsonObjectRequest=new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("TAG", "onResponse: ");
                            counter++;
                            mProgress.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {

                                    String id = jsonObject.getString("CustomGroup1ID");
                                    String updated_date = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");

                                    int staid = databaseHelper.updateAccount5customGroup1(data.getID(), id, updated_date, null);


                                    if(staid>0){
                                        //updata in to second table
                                    }
                                    if (GenericConstants.IS_DEBUG_MODE_ENABLED) {
                                        Toast.makeText(getContext(), staid + " Recourd Updated", Toast.LENGTH_SHORT).show();
                                        Log.e("Account5customGroup1", "pkID:" + data.getID() + " Status:" + staid);
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
                                sendAccount5customGroup1DataToServer();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    mProgress.dismiss();
                    GenericConstants.ShowDebugModeDialog(getContext(),
                            "Error on Account5customGroup1 ", error.getMessage());

                }
            })
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("CustomGroup1ID",data.getCustomGroup1ID());
                    params.put("CustomGroupName",data.getCustomGroupName());
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
        if (account5customGroup1.size() < 0)
            MNotificationClass.ShowToastTem(getContext(),"No inserted data in sqlite");

    }



}
