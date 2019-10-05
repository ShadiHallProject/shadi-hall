package org.by9steps.shadihall.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import org.by9steps.shadihall.adapters.SalePur1adapterPage2;
import org.by9steps.shadihall.chartofaccountdialog.DialogForAddNewSaleEntry;
import org.by9steps.shadihall.fragments.SelectDateFragment;
import org.by9steps.shadihall.helper.ApiRefStrings;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.ModelForSalePur1page2;
import org.by9steps.shadihall.model.item3name.UpdatedDate;
import org.by9steps.shadihall.model.salepur1data.SPDate;
import org.by9steps.shadihall.model.salepur1data.Salepur1;
import org.by9steps.shadihall.model.salepur2data.SalePur2;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Salepur1AddNewActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    SalePur1adapterPage2 salePur1adapterPage2;
    DatabaseHelper helper;
    Spinner spinner;
    public static Button datePicker;
    String EntryType, ClientID, ClientUserID;
    EditText remarks;
    List<ModelForSalePur1page2> spinnerData;
    List<ModelForSalePur1page2> griddata;
    int spinneritemindex = -1;
    Prefrence prefrence;
    /////////////Progresss Dialog
    ProgressDialog mProgress;
    ///SalePur1ID fro current Sale Transaction
    int maxid;
    ////////////Variable for Updating Salepur1 Data
    int counter = 0, totalitem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salepur1_add_new);
        recyclerView = findViewById(R.id.recyclerviewsalepur2);
        datePicker = findViewById(R.id.date_picker);
        datePicker.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        spinner = findViewById(R.id.accountspinner);
        spinner.setOnItemSelectedListener(this);
        helper = new DatabaseHelper(this);
        //////////////Setting Date on Button
        remarks = findViewById(R.id.name);
        ClientUserID = new Prefrence(this).getClientUserIDSession();
        EntryType = getIntent().getStringExtra("EntryType");
        String salepur1id = getIntent().getStringExtra("salepur1id");

        Date date = new Date();
        datePicker.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
        prefrence = new Prefrence(this);

        setItemToListRecycler();
        if (salepur1id != null) {
            try {
                maxid= Integer.parseInt(salepur1id);
            }catch (Exception e){
                e.printStackTrace();
            }
            SetDataOnGridView();
        }
    }

    private void setItemToListRecycler() {
        ClientID = new Prefrence(this).getClientIDSession();


        ///REsponse Expected :AcNameID AcName AcGruopName Balance
        String spinnerquery = "SELECT        Account3Name.AcNameID, Account3Name.AcName, Account2Group.AcGruopName, IFNULL(Account3Name.AcDebitBal, 0) - IFNULL(Account3Name.AcCreditBal, 0) AS Balance\n" +
                "FROM            Account3Name INNER JOIN\n" +
                "                         Account2Group ON Account3Name.AcGroupID = Account2Group.AcGroupID\n" +
                "WHERE        (Account3Name.ClientID = " + ClientID + ") AND (Account3Name.AcNameID != 0)\n" +
                "ORDER BY Account3Name.AcName            ";

        spinnerData = helper.GetDataFroJoinQuerySalerpurpage2(spinnerquery);
        // Creating adapter for spinner
        List<String> spinner_list = new ArrayList<>();
        for (ModelForSalePur1page2 data : spinnerData) {
            spinner_list.add(data.Columns[0].trim() + "_" + data.Columns[1]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinner_list);
        spinner.setAdapter(dataAdapter);


    }

    ////////////////////////////Setting Data on The Grid For Current Transaction
    public void SetDataOnGridView() {
        ClientID = new Prefrence(this).getClientIDSession();

        //////1-2-3-4-5
////////itemserial itemdesc quant price total
        String realquery = " SELECT        SalePur2.SalePur1ID, SalePur2.ItemSerial, Item3Name.ItemName || ' ' || Item2GroupName || ' ' ||ItemDescription AS ItemDesc, SalePur2.Qty, SalePur2.Price, SalePur2.Total, SalePur2.EntryType, SalePur2.ClientID\n" +
                "FROM            SalePur2 INNER JOIN\n" +
                "                         Item3Name ON SalePur2.Item3NameID = Item3Name.Item3NameID AND SalePur2.ClientID = Item3Name.ClientID INNER JOIN\n" +
                "                         Item2Group ON Item3Name.Item2GroupID = Item2Group.Item2GroupID AND Item3Name.ClientID = Item2Group.ClientID\n" +
                "WHERE        (SalePur2.ClientID = " + ClientID + ") AND (SalePur2.SalePur1ID = " + maxid + ") AND (SalePur2.EntryType = '" + EntryType + "')";
        griddata = helper.GetDataFroJoinQuerySalerpurpage2(realquery);

        salePur1adapterPage2 = new SalePur1adapterPage2(griddata, this);
        recyclerView.setAdapter(salePur1adapterPage2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.cb_menu, menu);
        MenuItem settings = menu.findItem(R.id.action_settings);
        settings.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_print) {
//            try {
//                createPdf();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (DocumentException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            return true;
        } else if (item.getItemId() == R.id.action_refresh) {
            if (GenericConstants.isConnected(this)) {
                refereshTables();
            } else {
                MNotificationClass.ShowToast(this, "Please Check Your Internet Connection");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void refereshTables() {
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loading....");
        mProgress.show();
        sendSalePur1DataToServer();
    }

    private void sendSalePur1DataToServer() {
        Log.e("sendSalePur1Data", "Flag 1 MathodName::sendSalePur1DataToServer");

        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
            mProgress.setMessage("Loading...  Method:sendSalePur1DataToServer CLI " + prefrence.getClientIDSession());

        String query = "SELECT * FROM SalePur1 WHERE  SalePur1ID < 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
        final List<Salepur1> salepur1List = helper.getSalePur1Data(query);
        Log.e("sendSalePur1Data", "Size" + String.valueOf(salepur1List.size()));
        totalitem = salepur1List.size();
        for (final Salepur1 data : salepur1List) {
            mProgress.show();
            String tag_json_obj = "json_obj_req";
            String url = ApiRefStrings.SendSalePur1DataToCloud;

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            counter++;
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
                                    int staid = helper.UpdateSalePur1Data(data.getID(), id, updated_date);
                                    if (staid > 0) {
                                        helper.UpdateSalePur1IdInSalePur2Table(data.getSalePur1ID(), id);
                                    }
                                    if (GenericConstants.IS_DEBUG_MODE_ENABLED) {
                                        Toast.makeText(Salepur1AddNewActivity.this, staid + " Recourd Updated", Toast.LENGTH_SHORT).show();
                                        Log.e("sendSalePur1Data", "pkID:" + data.getID() + " Status:" + staid);
                                    }
                                    getUpdateGridStatus();

                                } else {
                                    //databaseHelper.deleteAccount3NameEntry("DELETE FROM Account3Name WHERE ID = " + c.getId());
                                    String message = jsonObject.getString("message");
                                    if (GenericConstants.IS_DEBUG_MODE_ENABLED)
                                        Toast.makeText(Salepur1AddNewActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                MNotificationClass.ShowToastTem(Salepur1AddNewActivity.this, e.getMessage() + " -" + response);
                                e.printStackTrace();
                            }
                            if (counter >= totalitem) {
                                ///////////////CAlling Next Method
                                sendSalePur2DataToServer();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgress.dismiss();
                    GenericConstants.ShowDebugModeDialog(Salepur1AddNewActivity.this,
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
        mProgress.dismiss();
        MNotificationClass.ShowToast(Salepur1AddNewActivity.this, "All Done");
        if (salepur1List.size() < 0)
            GenericConstants.ShowDebugModeDialog(Salepur1AddNewActivity.this, "Error", "Updated Data Not Found ");


    }

    private void sendSalePur2DataToServer() {
        Log.e("sendSalePur2Data", "Flag 1 MathodName::sendSalePur2DataToServer");

        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
            mProgress.setMessage("Loading...  Method:sendSalePur1DataToServer CLI " + prefrence.getClientIDSession());

        String query = "SELECT * FROM SalePur2 WHERE  ItemSerial < 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
        final List<SalePur2> salePur2List = helper.getSalePur2Data(query);
        Log.e("sendSalePur2Data", "Size" + String.valueOf(salePur2List.size()));

        for (final SalePur2 salePur2 : salePur2List) {
            mProgress.show();
            String tag_json_obj = "json_obj_req";
            String url = ApiRefStrings.SendSalePur2DataToCloud;

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mProgress.dismiss();
                            Log.e("sendSalePur2Data", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                Log.e("sendSalePur2Data", success);
                                if (success.equals("1")) {
                                    String id = jsonObject.getString("ItemSerial");
                                    String updated_date = jsonObject.getString("UpdatedDate");
                                    String message = jsonObject.getString("message");
                                    int staid = helper.UpdateSalePur2Data(salePur2.getID(), id, updated_date);

                                    if (GenericConstants.IS_DEBUG_MODE_ENABLED) {
                                        Toast.makeText(Salepur1AddNewActivity.this, staid + " Recourd Updated", Toast.LENGTH_SHORT).show();
                                        Log.e("sendSalePur2Data", "pkID:" + salePur2.getID() + " Status:" + staid);
                                    }
                                    getUpdateGridStatus();

                                } else {
                                    //databaseHelper.deleteAccount3NameEntry("DELETE FROM Account3Name WHERE ID = " + c.getId());
                                    String message = jsonObject.getString("message");
                                    if (GenericConstants.IS_DEBUG_MODE_ENABLED)
                                        Toast.makeText(Salepur1AddNewActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                MNotificationClass.ShowToastTem(Salepur1AddNewActivity.this, e.getMessage() + " -" + response);
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgress.dismiss();
                    GenericConstants.ShowDebugModeDialog(Salepur1AddNewActivity.this,
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
        MNotificationClass.ShowToast(Salepur1AddNewActivity.this, "All Done");
        if (salePur2List.size() < 0)
            GenericConstants.ShowDebugModeDialog(Salepur1AddNewActivity.this, "Error", "Updated Data Not Found ");


    }

    private void getUpdateGridStatus() {
    }

    ///////////////////////add Btn Click Listnerner
    public void AddNewDialog(View view) {
        maxid = refdb.SlePur1.GetMaximumID(helper);
        maxid++;
        maxid = -maxid;
        SaveDataToSalePur1(maxid);
        SetDataOnGridView();
        DialogForAddNewSaleEntry saleEntry = new DialogForAddNewSaleEntry();
        Bundle bundle = new Bundle();
        bundle.putString("ClientID", ClientID);
        bundle.putInt("SalePur1ID", maxid);
        bundle.putString("EntryType", EntryType);
        saleEntry.setArguments(bundle);
        saleEntry.show(getSupportFragmentManager(), "TAG");
    }

    private void SaveDataToSalePur1(int maxid) {


        ModelForSalePur1page2 temdata = spinnerData.get(spinneritemindex);
        Salepur1 salepur1 = new Salepur1();
        salepur1.setSalePur1ID((maxid));
        salepur1.setEntryType(EntryType);
        ////////////////////Setting Today Date
        SPDate spDate = new SPDate();
        spDate.setDate(datePicker.getText().toString());
        salepur1.setSPDate(spDate);
        /////////////////////////////////////////
        salepur1.setAcNameID(Integer.parseInt(temdata.Columns[0]));
        salepur1.setRemarks(remarks.getText().toString());
        salepur1.setClientID(Integer.parseInt(ClientID));
        salepur1.setClientUserID(Integer.parseInt(ClientUserID));
        salepur1.setNetCode(GenericConstants.NullFieldStandardText);
        salepur1.setSysCode(GenericConstants.NullFieldStandardText);
        ////////////////////////////Set Updated Date
        UpdatedDate updatedDate = new UpdatedDate();
        updatedDate.setDate(GenericConstants.NullFieldStandardText);
        salepur1.setUpdatedDate(updatedDate);
        /////////////////
        salepur1.setNameOfPerson(GenericConstants.NullFieldStandardText);
        salepur1.setPayAfterDay(0);


        long iid = refdb.SlePur1.AddItemSalePur1(helper, salepur1);
        if (iid != -1) {
            MNotificationClass.ShowToast(this, "Receipt Created " + iid);
        } else {
            MNotificationClass.ShowToast(this, "Receipt Not Created ");
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_picker:

                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getSupportFragmentManager(), "DatePicker");
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinneritemindex = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    ////////////////Refreshing Grid By Life Cycle

    @Override
    protected void onStart() {
        super.onStart();
        SetDataOnGridView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SetDataOnGridView();

    }

    @Override
    protected void onStop() {
        super.onStop();
        SetDataOnGridView();

    }


}
