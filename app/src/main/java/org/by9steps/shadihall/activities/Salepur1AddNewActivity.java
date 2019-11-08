package org.by9steps.shadihall.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
import org.by9steps.shadihall.adapters.SalePur1adapterPage2;
import org.by9steps.shadihall.callingapi.SalePur1And2Apis;
import org.by9steps.shadihall.chartofaccountdialog.DialogForAddNewSaleEntry;
import org.by9steps.shadihall.fragments.SelectDateFragment;
import org.by9steps.shadihall.helper.ApiRefStrings;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.JoinQueryDaliyEntryPage1;
import org.by9steps.shadihall.model.ModelForSalePur1page2;
import org.by9steps.shadihall.model.item3name.UpdatedDate;
import org.by9steps.shadihall.model.salepur1data.SPDate;
import org.by9steps.shadihall.model.salepur1data.SalePur1Data;
import org.by9steps.shadihall.model.salepur1data.Salepur1;
import org.by9steps.shadihall.model.salepur2data.SalePur2;
import org.by9steps.shadihall.model.salepur2data.SalePur2Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.LoginException;

public class Salepur1AddNewActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, SalePur1adapterPage2.popUpMenuItemClickListener {

    public static final String ENABLE_EDITING_MES = "Enable Editing";
    //////////////for Ad new Invoice
    //ImageView addimageview;
    ////////////////Fro Horizontal Top List Click Listener
    LinearLayout layoutprint, layoutback, layoutnext, layoutadd, layoutedit, layoutupdate, layoutsearch;
    RecyclerView recyclerView;
    //////////////For Save and Cancel the invoice editing
    Button savebtn, cancelbtn;
    SalePur1adapterPage2 salePur1adapterPage2;
    DatabaseHelper helper;
    Spinner spinner;
    public static Button datePicker;
    String EntryType, ClientID, ClientUserID;
    EditText remarks, nameofperson, daysleft;
    TextView serialno;
    List<ModelForSalePur1page2> spinnerData;
    List<ModelForSalePur1page2> griddata;
    int spinneritemindex = -1;
    Prefrence prefrence;
    ///////////////////isEdit Type Or New Dialog
    //boolean isedit = false;
    /////////////Progresss Dialog
    ProgressDialog mProgress;
    ///SalePur1ID fro current Sale Transaction
    int maxid;
    ////////////Variable for Updating Salepur1 Data
    int counter = 0, totalitem = 0;
    //////pkid for geting ID from Sqqlite while editing
    String pkid;
    ////////////////for lock sale item editng
    public static boolean locksaleitemedit = false;
    ///////////////////////Check For Editing A Sale
    public static boolean isSaleCreating = false;
    /////////////////////////FAB for add new item
    FloatingActionButton additemfab;
    //////////////////////ArrayList For Track data back and forward
    List<JoinQueryDaliyEntryPage1> arrayListForTrack = null;
    String salePur1IdSeq[];
    int currentslectediteminList = -1;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_salepur1_add_new);
        additemfab = findViewById(R.id.fabaddnewitem);
        additemfab.setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerviewsalepur2);
        serialno = findViewById(R.id.receiyptno);
        datePicker = findViewById(R.id.date_picker);
        datePicker.setOnClickListener(this);
        //  addimageview = findViewById(R.id.addimageview);
        // addimageview.bringToFront();
        savebtn = findViewById(R.id.savebtn);
        savebtn.setOnClickListener(this);
        savebtn.bringToFront();
        cancelbtn = findViewById(R.id.cancelbtn);
        cancelbtn.setOnClickListener(this);
        cancelbtn.bringToFront();
        /////////////////Assigning Layout IDs fro horizontal views
        layoutprint = findViewById(R.id.headimage1);
        layoutprint.setOnClickListener(this);
        layoutback = findViewById(R.id.headimage2);
        layoutback.setOnClickListener(this);
        layoutnext = findViewById(R.id.headimage3);
        layoutnext.setOnClickListener(this);
        layoutadd = findViewById(R.id.headimage4);
        layoutadd.setOnClickListener(this);
        layoutedit = findViewById(R.id.headimage5);
        layoutedit.setOnClickListener(this);
        layoutupdate = findViewById(R.id.headimage6);
        layoutupdate.setOnClickListener(this);
        layoutsearch = findViewById(R.id.headimage7);
        layoutsearch.setOnClickListener(this);
///////////////////////////////////////////////////////////
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        spinner = findViewById(R.id.accountspinner);
        spinner.setOnItemSelectedListener(this);
        helper = new DatabaseHelper(this);
        //////////////Setting Date on Button

        remarks = findViewById(R.id.name);
        nameofperson = findViewById(R.id.formname_of_person1);
        daysleft = findViewById(R.id.formdayleft);
        prefrence = new Prefrence(this);
        ClientUserID = prefrence.getClientUserIDSession();
        EntryType = getIntent().getStringExtra("EntryType");
        // isedit = getIntent().getBooleanExtra("edit", false);
        String salepur1id = getIntent().getStringExtra("salepur1id");
/////////////////////////////////////////////////////
        Date date = new Date();
        datePicker.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));


        setItemToListRecycler();
        if (salepur1id != null) {
            try {
                ////////////For Edit Invoice
                maxid = Integer.parseInt(salepur1id);
                setDataOnDialogForEdit();
                serialno.setText("Invoice " + maxid);
                pkid = getIntent().getStringExtra("pkid");
                //addimageview.setImageDrawable(getResources().getDrawable(R.drawable.edit));
            } catch (Exception e) {
                e.printStackTrace();
            }
            SetDataOnGridView();

            isSaleCreating = false;

        } else {
            ///////////////////////Creating New Sale
            isSaleCreating = true;
            generateInvoiceID();
            additemfab.setVisibility(View.VISIBLE);
            //ShowRowForAddItem();

        }

        GetDataFroBackAndForwardTracking(1, salepur1id);
        locksaleitemedit = false;

    }


    private void setDataOnDialogForEdit() {
        spinner.setFocusable(false);
        spinner.setFocusableInTouchMode(false);
        // spinner.setEnabled(false);
        String qq = "Select * from SalePur1 where SalePur1ID=" + maxid;
        List<Salepur1> datalist = refdb.SlePur1.GetSalePur1Data(helper, qq);
        Log.e("output", "" + datalist.size());
        for (Salepur1 temd : datalist) {
            nameofperson.setText(temd.getNameOfPerson());
            nameofperson.setFocusable(false);
            remarks.setText(temd.getRemarks());
            remarks.setFocusable(false);
            daysleft.setText("" + temd.getPayAfterDay());
            // addimageview.setVisibility(View.INVISIBLE);
            daysleft.setFocusable(false);
            savebtn.setVisibility(View.INVISIBLE);
            cancelbtn.setVisibility(View.INVISIBLE);
            /////////////////set spinner data

            if (spinnerData != null) {
                for (int i = 0; i < spinnerData.size(); i++) {
                    boolean spinr = spinnerData.get(i).Columns[0].equals(temd.getAcNameID() + "");
                    Log.e("spinr", spinr + " Spinner Data:" + spinnerData.get(i).Columns[0] +
                            ": acName ID::" + temd.getAcNameID() + ":");
                    if (spinr) {
                        spinner.setSelection(i, true);
                    }
                }
            }


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
        //////////////list allitem expected ::SalePur1ID,ItemSerial,Item3Name,ItemDesc,Qty,Price,Total,EntryType,ClientID,ID
        String realquery = " SELECT        SalePur2.SalePur1ID, SalePur2.ItemSerial, Item3Name.ItemName || ' ' || Item2GroupName || ' ' ||ItemDescription AS ItemDesc, SalePur2.Qty, SalePur2.Price, SalePur2.Total, SalePur2.EntryType, SalePur2.ClientID,SalePur2.ID\n" +
                "FROM            SalePur2 INNER JOIN\n" +
                "                         Item3Name ON SalePur2.Item3NameID = Item3Name.Item3NameID AND SalePur2.ClientID = Item3Name.ClientID INNER JOIN\n" +
                "                         Item2Group ON Item3Name.Item2GroupID = Item2Group.Item2GroupID AND Item3Name.ClientID = Item2Group.ClientID\n" +
                "WHERE        (SalePur2.ClientID = " + ClientID + ") AND (SalePur2.SalePur1ID = " + maxid + ") AND (SalePur2.EntryType = '" + EntryType + "')";

        griddata = helper.GetDataFroJoinQuerySalerpurpage2(realquery);
        int grandtotal = 0, columncount = 0, qtycount = 0;
        //List<ModelForSalePur1page2> temformatlist=griddata;
        columncount = griddata.size();
        for (ModelForSalePur1page2 temobj : griddata) {
            //temformatlist.add(temobj);
            try {
                qtycount += Float.parseFloat(temobj.Columns[3]);
                grandtotal += Float.parseFloat(temobj.Columns[5]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (griddata.size() >= 1) {
            griddata.get(griddata.size() - 1).setLastRowfun = true;
            griddata.get(griddata.size() - 1).ColumnCount = columncount + "";
            griddata.get(griddata.size() - 1).totalqty = qtycount + "";
            griddata.get(griddata.size() - 1).GrandTotal = grandtotal + "";
        }

        salePur1adapterPage2 = new SalePur1adapterPage2(griddata, this);
        salePur1adapterPage2.setListenerpopup(this);
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

    ///////////////////Show row for add item in list
//    public void ShowRowForAddItem(){
//
//        String Columns[]={null,"0","0","0","0","0","0","0"};
//        griddata=new ArrayList<>();
//        ModelForSalePur1page2 temobj=new ModelForSalePur1page2(Columns);
//        temobj.setVisiblitlOfTopRow=false;
//        griddata.add(temobj);
//        Log.e("ssss","ShwoRowCAlls::"+griddata.size());
//        salePur1adapterPage2 = new SalePur1adapterPage2(griddata, this);
//        salePur1adapterPage2.setListenerpopup(this);
//       // salePur1adapterPage2.salepur1id=null;
//        recyclerView.setAdapter(salePur1adapterPage2);
//    }


    //////////////////Whend Refresh Click
    private void refereshTables() {
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loading....");
        mProgress.show();

        //getSalePur1UpdatedEditedDataFromServer();
        SalePur1And2Apis salePur1And2Apis=new SalePur1And2Apis(this,
                mProgress,
                helper,prefrence);
        salePur1And2Apis.finallistnerForAlldone=new SalePur1And2Apis.SalePur1funListener() {
            @Override
            public void FinishCallBackmethod(String success, String funType) {
                MNotificationClass.ShowToastTem(Salepur1AddNewActivity.this,
                        "Final Method Calls finished");
            }
        };
        salePur1And2Apis.trigerAllSalpur1and2Method();
    }

//    /////////////////GEt Data from server that is edited or new Inserted
//    private void getSalePur1UpdatedEditedDataFromServer() {
//
//        Log.e("status", "Flag1");
//        final String TAG = "getSalePur1UpdatedEditedDataFromServer";
//        mProgress.setMessage("Please wait...getSalePur1DAta");
//        String tag_json_obj = "json_obj_req";
//        String u = ApiRefStrings.GetSalePur1EditedDataFromServer;
//
//        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            if (response != null) {
//                                GsonBuilder gsonBuilder = new GsonBuilder();
//                                Gson gson = gsonBuilder.create();
//                                Log.e("getSalePur1EditedData", response);
//                                SalePur1Data salePur1Data = gson.fromJson(response, SalePur1Data.class);
//                                Log.e("kkk", salePur1Data.getSuccess() + " " + salePur1Data.getSalepur1().size());
//                                if (salePur1Data.getSuccess() != 0) {
//                                    for (Salepur1 salepur1 : salePur1Data.getSalepur1()) {
//                                        Log.e("getSalePur1DataList", " " + salepur1.toString());
//                                        long idd = refdb.SlePur1.updateSalepur1fromServer(helper, salepur1);
//                                        if (idd <= 0)
//                                            refdb.SlePur1.AddItemSalePur1(helper, salepur1);
//                                        Log.e("getSalePur1EditedData", "inser id::" + idd);
//                                    }
//                                    Log.e("getSalePur1EditedData", "onResponse: " + salePur1Data.getSalepur1());
//
//
//                                } else {
//                                    MNotificationClass.ShowToast(Salepur1AddNewActivity.this, "No Data Found in SalePur1Table");
//                                }
//                            }
//
//                            Log.e("getSalePur1EditedData", "onResponse: " + response);
//
//                            mProgress.dismiss();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        getSalePur2UpdatedEditedDataFromServer();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                getSalePur2UpdatedEditedDataFromServer();
//                GenericConstants.ShowDebugModeDialog(Salepur1AddNewActivity.this, "Error SalePur2", error.getMessage());
//                mProgress.dismiss();
//                Log.e("Error", error.toString());
////                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                String date = helper.getSalePur1MaxUpdatedDate(prefrence.getClientIDSession());
//                date = date.trim();
//                date = date.substring(0, date.length() - 3);
//                Map<String, String> params = new HashMap<String, String>();
//                Log.e("getSalePur1EditedData", "Max UpdateSalePur1 " + date);
//
//                params.put("ClientID", prefrence.getClientIDSession());
//                params.put("SessionDate", date);
//                return params;
//            }
//        };
//        int socketTimeout = 10000;//10 seconds
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        jsonObjectRequest.setRetryPolicy(policy);
//        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
//    }
//
//    private void getSalePur2UpdatedEditedDataFromServer() {
//        Log.e("status", "Flag2");
//
//        final String TAG = "getSalePur2UpdatedEditedDataFromServer";
//        mProgress.setMessage("Please wait...getSalePur2DAta");
//        String tag_json_obj = "json_obj_req";
//        String u = ApiRefStrings.GetSalePur2EditedDataFromServer;
//
//        StringRequest jsonObjectRequest = new StringRequest(com.android.volley.Request.Method.POST, u,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            if (response != null) {
//                                GsonBuilder gsonBuilder = new GsonBuilder();
//                                Gson gson = gsonBuilder.create();
//                                SalePur2Data salePur2Data = gson.fromJson(response, SalePur2Data.class);
//                                if (salePur2Data.getSuccess() != 0) {
//                                    for (SalePur2 salePur2 : salePur2Data.getSalePur2()) {
//                                        Log.e("getSalePur2Data", " " + salePur2.getClientID() + " --" + salePur2.getUpdatedDate().getDate());
//                                        long idd = refdb.SalePur2.updateSalepur2fromServer(helper, salePur2);
//                                        if (idd <= 0)
//                                            refdb.SalePur2.AddItemSalePur2(helper, salePur2);
//                                        Log.e("getSalePur2Data", "inser id" + idd);
//                                    }
//
//
//                                } else {
//                                    MNotificationClass.ShowToast(Salepur1AddNewActivity.this, "No Data Found in SalePur2Table");
//                                }
//                            }
//
//                            Log.e("getSalePur2Data", "onResponse: " + response);
//
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        mProgress.dismiss();
//                        updateSalePur1();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                updateSalePur1();
//                GenericConstants.ShowDebugModeDialog(Salepur1AddNewActivity.this, "Error SalePur2", error.getMessage());
//                mProgress.dismiss();
//                Log.e("Error", error.toString());
////                Toast.makeText(SplashActivity.this, error.toString(), Toast.LENGTH_LONG).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                String date = helper.getSalePur2MaxUpdatedDate(prefrence.getClientIDSession());
//                date = date.trim();
//                date = date.substring(0, date.length() - 3);
//                Map<String, String> params = new HashMap<String, String>();
//                Log.e("getSalePur2Data", "Max UpdateSalePur1 " + date);
//
//                params.put("ClientID", prefrence.getClientIDSession());
//                params.put("SessionDate", date);
//                return params;
//            }
//        };
//        int socketTimeout = 10000;//10 seconds
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        jsonObjectRequest.setRetryPolicy(policy);
//        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
//    }
//
//    /////////////Send Edited Record From Sqlite To server
//    public void updateSalePur1() {
//        Log.e("status", "Flag3");
//        totalitem = counter = 0;
//        Log.e("updateSalePur1", "Flag 3 MathodName::updateSalePur1");
//        mProgress.show();
//        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
//            mProgress.setMessage("Loading... updateSalePur1 UpDateRecordFromSqliteToCloud Method:updateSalePur1 CLI " + prefrence.getClientIDSession());
//
//        String query = "SELECT * FROM SalePur1 WHERE ClientID = " + prefrence.getClientIDSession() + " AND SalePur1ID > 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
//        final List<Salepur1> salepur1s = refdb.SlePur1.GetSalePur1Data(helper, query);
//        Log.e("updateSalePur1", String.valueOf(salepur1s.size()));
//        totalitem = salepur1s.size();
//        for (final Salepur1 c : salepur1s) {
//            Log.e("updateSalePur1", "DAta" + c.toString());
//            String tag_json_obj = "json_obj_req";
//            String url = ApiRefStrings.UpdateDataSalePur1OnServer;
//
//            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            counter++;
//                            mProgress.dismiss();
//                            Log.e("updateSalePur1", response);
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                String success = jsonObject.getString("success");
//                                Log.e("updateSalePur1", success);
//                                if (success.equals("1")) {
//                                    int effectrow = jsonObject.getInt("roweffected");
//                                    if (effectrow != 0) {
//                                        String updatec = jsonObject.getString("UpdatedDate");
//                                        String message = jsonObject.getString("message");
//
//                                        long idd = helper.UpdateSalePur1Data(c.getID(), null, updatec, null);
//                                        if (idd != -1)
//                                            MNotificationClass.ShowToast(Salepur1AddNewActivity.this, "Updated Data");
//                                        else
//                                            MNotificationClass.ShowToast(Salepur1AddNewActivity.this, "Sqlite Not Updated");
//                                    } else {
//                                        MNotificationClass.ShowToast(Salepur1AddNewActivity.this, "Not Updated Record " + c.getID());
//                                    }
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            //  updateSalePur2();
//                        }
//
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    //////////////////////////////////Calling Next Methos
//                    updateSalePur2();
//                    mProgress.dismiss();
//                    Log.e("updateSalePur1", error.toString());
////                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//
//                    Map<String, String> params = new HashMap<String, String>();
//
//
////                    Remarks
////                            ClientID
////                    ClientUserID
////                            NetCode
////                    SysCode
////                            NameOfPerson
////                    PayAfterDay
//                    params.put("SalePur1ID", c.getSalePur1ID() + "");
//                    params.put("EntryType", c.getEntryType() + "");
//                    params.put("SPDate", c.getSPDate().getDate() + "");
//                    params.put("AcNameID", c.getAcNameID() + "");
//                    params.put("Remarks", c.getRemarks());
//                    params.put("ClientID", c.getClientID() + "");
//                    params.put("ClientUserID", c.getClientUserID() + "");
//                    params.put("NetCode", c.getNetCode());
//                    params.put("SysCode", c.getSysCode());
//                    params.put("NameOfPerson", c.getNameOfPerson());
//                    params.put("PayAfterDay", c.getPayAfterDay() + "");
//                    return params;
//                }
//            };
//            int socketTimeout = 30000;//30 seconds - change to what you want
//            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//            jsonObjectRequest.setRetryPolicy(policy);
//            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
//        }
//////////////////////////////////////As we know update not harm the server it may call synchronously
//        ///
//        if (salepur1s.size() <= 0 || counter >= totalitem) {
//            updateSalePur2();
//        }
//
//
//    }
//
//    public void updateSalePur2() {
//        counter = totalitem = 0;
//        Log.e("status", "Flag4");
//
//        Log.e("method", "Flag 3 MathodName::updateSalePur2");
//        mProgress.show();
//        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
//            mProgress.setMessage("Loading... updateSalePur2 UpDateRecordFromSqliteToCloud Method:updateSalePur2 CLI " + prefrence.getClientIDSession());
//
//        String query = "SELECT * FROM SalePur2 WHERE UpdatedDate = '" + GenericConstants.NullFieldStandardText + "' AND ItemSerial >0 ";
//        final List<SalePur2> salePur2s = refdb.SalePur2.GetSalePurItemList(helper, query);
//        Log.e("updateSalePur2", String.valueOf(salePur2s.size()));
//        totalitem = salePur2s.size();
//        for (final SalePur2 c : salePur2s) {
//            Log.e("updateSalePur2", "DAta" + c.toString());
//            String tag_json_obj = "json_obj_req";
//            String url = ApiRefStrings.UpdateDataSalePur2OnServer;
//
//            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            counter++;
//                            mProgress.dismiss();
//                            Log.e("updateSalePur2", response);
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                String success = jsonObject.getString("success");
//                                Log.e("updateSalePur2", success);
//                                if (success.equals("1")) {
//                                    int effectrow = jsonObject.getInt("roweffected");
//                                    if (effectrow != 0) {
//                                        String UpdatedDate = jsonObject.getString("UpdatedDate");
//                                        String message = jsonObject.getString("message");
//                                        UpdatedDate update = new UpdatedDate();
//                                        update.setDate(UpdatedDate);
//                                        c.setUpdatedDate(update);
//                                        long idd = helper.UpdateDataForSalePur2(c.getID() + "", c, true);
//                                        if (idd != -1)
//                                            MNotificationClass.ShowToast(Salepur1AddNewActivity.this, "Updated Data");
//                                        else
//                                            MNotificationClass.ShowToast(Salepur1AddNewActivity.this, "Sqlite Not Updated");
//                                    } else {
//                                        MNotificationClass.ShowToast(Salepur1AddNewActivity.this, "Not Updated Record " + c.getID());
//
//                                    }
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            // sendSalePur1DataToServer();
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    //////////////////////////////////Calling Next Methos
//                    sendSalePur1DataToServer();
//                    mProgress.dismiss();
//                    Log.e("Error", error.toString());
////                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//
//                    Map<String, String> params = new HashMap<String, String>();
//
//                    params.put("SalePur1ID", c.getSalePur1ID() + "");
//                    params.put("ItemSerial", c.getItemSerial() + "");
//                    params.put("EntryType", c.getEntryType());
//                    params.put("Item3NameID", c.getItem3NameID() + "");
//                    params.put("ItemDescription", c.getItemDescription());
//                    params.put("QtyAdd", c.getQty());
//                    params.put("QtyLess", c.getQtyLess());
//                    params.put("Price", c.getPrice());
//                    params.put("Location", c.getLocation());
//                    params.put("ClientID", c.getClientID() + "");
//                    params.put("ClientUserID", c.getClientUserID() + "");
//                    params.put("NetCode", c.getNetCode());
//                    params.put("SysCode", c.getSysCode());
//                    return params;
//                }
//            };
//            int socketTimeout = 30000;//30 seconds - change to what you want
//            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//            jsonObjectRequest.setRetryPolicy(policy);
//            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
//        }
//
//
//        /////////////As update DAta to cloud not have problem servver deny the request it can
//        ///call asynchoronausly
//        if (salePur2s.size() <= 0) {
//            mProgress.dismiss();
//
//        }
//        sendSalePur1DataToServer();
//
//    }
//
//    ///////////////////////////////Send New Data from sqlite To server
//    private void sendSalePur1DataToServer() {
//        Log.e("status", "Flag5");
//
//        Log.e("sendSalePur1Data", "Flag 1 MathodName::sendSalePur1DataToServer");
//
//        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
//            mProgress.setMessage("Loading...  Method:sendSalePur1DataToServer CLI " + prefrence.getClientIDSession());
//
//        String query = "SELECT * FROM SalePur1 WHERE  SalePur1ID < 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
//        final List<Salepur1> salepur1List = helper.getSalePur1Data(query);
//        Log.e("sendSalePur1Data", "Size" + String.valueOf(salepur1List.size()));
//        totalitem = salepur1List.size();
//        for (final Salepur1 data : salepur1List) {
//            mProgress.show();
//            String tag_json_obj = "json_obj_req";
//            String url = ApiRefStrings.SendSalePur1DataToCloud;
//
//            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            counter++;
//                            mProgress.dismiss();
//                            Log.e("sendSalePur1Data", response);
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                String success = jsonObject.getString("success");
//                                Log.e("sendSalePur1Data", success);
//                                if (success.equals("1")) {
//                                    String id = jsonObject.getString("SalePur1Id");
//                                    String updated_date = jsonObject.getString("UpdatedDate");
//                                    String message = jsonObject.getString("message");
//                                    int staid = helper.UpdateSalePur1Data(data.getID(), id, updated_date, null);
//                                    int upidcashbk=-1;
//                                    if (staid > 0) {
//                                        helper.UpdateSalePur1IdInSalePur2Table(data.getSalePur1ID(),
//                                                data.getEntryType(),
//                                                data.getClientID(),
//                                                id);
//                                         upidcashbk=refdb.SlePur1.updateSalePur1IdinCashbook(helper,
//                                                data.getClientID()+"",
//                                                data.getEntryType()+"",
//                                                data.getSalePur1ID()+"",
//                                                id+"");
//                                    }
//                                    if (GenericConstants.IS_DEBUG_MODE_ENABLED) {
//                                        Toast.makeText(Salepur1AddNewActivity.this, staid + " Recourd Updated", Toast.LENGTH_SHORT).show();
//                                        Log.e("sendSalePur1Data", "pkID:" + data.getID() + " Status:" + staid+" cash bok status "+upidcashbk);
//                                    }
//                                    getUpdateGridStatus();
//
//                                } else {
//                                    //databaseHelper.deleteAccount3NameEntry("DELETE FROM Account3Name WHERE ID = " + c.getId());
//                                    String message = jsonObject.getString("message");
//                                    if (GenericConstants.IS_DEBUG_MODE_ENABLED)
//                                        Toast.makeText(Salepur1AddNewActivity.this, message, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                MNotificationClass.ShowToastTem(Salepur1AddNewActivity.this, e.getMessage() + " -" + response);
//                                e.printStackTrace();
//                            }
//                            if (counter >= totalitem) {
//                                ///////////////CAlling Next Method
//                                sendSalePur2DataToServer();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    mProgress.dismiss();
//                    GenericConstants.ShowDebugModeDialog(Salepur1AddNewActivity.this,
//                            "Error", error.getMessage());
////                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//
//                    Map<String, String> params = new HashMap<String, String>();
//
//                    Log.e("sendSalePur1Data", "Set Pram ::" + data.toString());
//                    params.put("EntryType", data.getEntryType());
//                    params.put("SPDate", data.getSPDate().getDate());
//                    params.put("AcNameID", String.valueOf(data.getAcNameID()));
//                    params.put("Remarks", data.getRemarks());
//                    params.put("ClientID", String.valueOf(data.getClientID()));
//                    params.put("ClientUserID", String.valueOf(data.getClientUserID()));
//                    params.put("NetCode", data.getNetCode());
//                    params.put("SysCode", data.getSysCode());
//                    params.put("NameOfPerson", data.getNameOfPerson());
//                    params.put("PayAfterDay", String.valueOf(data.getPayAfterDay()));
//
//                    return params;
//                }
//            };
//
//            int socketTimeout = 30000;//30 seconds - change to what you want
//            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//            jsonObjectRequest.setRetryPolicy(policy);
//            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
//        }
//        if (salepur1List.size() <= 0 || counter >= totalitem)
//            sendSalePur2DataToServer();
//
//        mProgress.dismiss();
//        MNotificationClass.ShowToast(Salepur1AddNewActivity.this, "All Done");
//        if (salepur1List.size() < 0)
//            MNotificationClass.ShowToastTem(this, "No inserted data in sqlite");
//
//
//    }
//
//    private void sendSalePur2DataToServer() {
//        Log.e("status", "Flag6");
//        counter = totalitem = 0;
//
//        Log.e("sendSalePur2Data", "Flag 1 MathodName::sendSalePur2DataToServer");
//
//        if (GenericConstants.IS_DEBUG_MODE_ENABLED)
//            mProgress.setMessage("Loading...  Method:sendSalePur2DataToServer CLI " + prefrence.getClientIDSession());
//
//        String query = "SELECT * FROM SalePur2 WHERE  ItemSerial < 0 AND UpdatedDate = '" + GenericConstants.NullFieldStandardText + "'";
//        final List<SalePur2> salePur2List = helper.getSalePur2Data(query);
//        Log.e("sendSalePur2Data", "New Item Inserted Size:" + String.valueOf(salePur2List.size()));
//        totalitem = salePur2List.size();
//        for (final SalePur2 salePur2 : salePur2List) {
//            counter++;
//            mProgress.show();
//            String tag_json_obj = "json_obj_req";
//            Log.e("sendSalePur2Data", "++SendItem:" + salePur2.toString());
//            String url = ApiRefStrings.SendSalePur2DataToCloud;
//
//            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            mProgress.dismiss();
//                            Log.e("sendSalePur2Data", "++Response:" + response);
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                String success = jsonObject.getString("success");
//                                Log.e("sendSalePur2Data", success);
//                                if (success.equals("1")) {
//                                    String id = jsonObject.getString("ItemSerial");
//                                    String updated_date = jsonObject.getString("UpdatedDate");
//                                    String message = jsonObject.getString("message");
//                                    int staid = helper.UpdateSalePur2Data(salePur2.getID(), id, updated_date);
//
//                                    if (GenericConstants.IS_DEBUG_MODE_ENABLED) {
//                                        Toast.makeText(Salepur1AddNewActivity.this, staid + " Recourd Updated", Toast.LENGTH_SHORT).show();
//                                        Log.e("sendSalePur2Data", "pkID:" + salePur2.getID() + " Status:" + staid);
//                                    }
//                                    // getUpdateGridStatus();
//
//                                } else {
//                                    //databaseHelper.deleteAccount3NameEntry("DELETE FROM Account3Name WHERE ID = " + c.getId());
//                                    String message = jsonObject.getString("message");
//                                    if (GenericConstants.IS_DEBUG_MODE_ENABLED)
//                                        Toast.makeText(Salepur1AddNewActivity.this, message, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                MNotificationClass.ShowToastTem(Salepur1AddNewActivity.this, e.getMessage() + " -" + response);
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    mProgress.dismiss();
//                    GenericConstants.ShowDebugModeDialog(Salepur1AddNewActivity.this,
//                            "Error", error.getMessage());
////                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//
//                    Map<String, String> params = new HashMap<String, String>();
//
//                    Log.e("sendSalePur2Data", "Set Pram ::" + salePur2.toString());
//                    /////////////////////////////
//                    params.put("SalePur1ID", String.valueOf(salePur2.getSalePur1ID()));
//                    params.put("EntryType", salePur2.getEntryType());
//                    params.put("ItemDescription", salePur2.getItemDescription());
//                    params.put("QtyAdd", salePur2.getQtyAdd());
//                    params.put("QtyLess", salePur2.getQtyLess());
//                    params.put("Price", salePur2.getPrice());
//                    params.put("Location", salePur2.getLocation());
//                    params.put("ClientID", String.valueOf(salePur2.getClientID()));
//                    params.put("ClientUserID", String.valueOf(salePur2.getClientUserID()));
//                    params.put("NetCode", salePur2.getNetCode());
//                    params.put("SysCode", salePur2.getSysCode());
//                    params.put("Item3NameID", String.valueOf(salePur2.getItem3NameID()));
//
//                    return params;
//                }
//            };
//
//            int socketTimeout = 30000;//30 seconds - change to what you want
//            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//            jsonObjectRequest.setRetryPolicy(policy);
//            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
//        }
//        mProgress.dismiss();
//        MNotificationClass.ShowToast(Salepur1AddNewActivity.this, "All Done");
//        if (salePur2List.size() < 0 || counter >= totalitem)
//            GenericConstants.ShowDebugModeDialog(Salepur1AddNewActivity.this, "Error", "Updated Data Not Found ");
//
//
//    }

    //////////this function Call when any update From server will publish here
    private void getUpdateGridStatus() {
    }

    ///////////////////////add Btn Click Listnerner
    @SuppressLint("RestrictedApi")
    public void AddNewDialog() {

        //generateInvoiceID();
        ///////Temp Try To Do
//        else if (pkid != null) {
//            MNotificationClass.ShowToastTem(this, "Update");
//            updateDataForSalePur1inSqlite();
//        }

        if (pkid != null) {
            ///////////////////CAll mehtod for Existing Sale item entry
            DialogForAddNewSaleEntry saleEntry = new DialogForAddNewSaleEntry();
            Bundle bundle = new Bundle();
            bundle.putString("ClientID", ClientID);
            bundle.putInt("SalePur1ID", maxid);
            bundle.putString("EntryType", EntryType);
            saleEntry.setArguments(bundle);
            saleEntry.show(getSupportFragmentManager(), "TAG");
        } else if (pkid == null || pkid.isEmpty()) {
            ///////////////////CAll mehtod for new Sale item entry
            additemfab.setVisibility(View.INVISIBLE);
            generateInvoiceID();
            // maxid = -maxid;
            String cashbookid = SaveDataToSalePur1(maxid);
            ////////////////////////put lock on all fields
            nameofperson.setFocusable(false);
            remarks.setFocusable(false);
            daysleft.setFocusable(false);
            locksaleitemedit = false;
            isSaleCreating = false;
            ///////////////////////////////////////
            SetDataOnGridView();
            // addimageview.setVisibility(View.INVISIBLE);
            DialogForAddNewSaleEntry saleEntry = new DialogForAddNewSaleEntry();
            Bundle bundle = new Bundle();
            bundle.putString("ClientID", ClientID);
            bundle.putInt("SalePur1ID", maxid);
            bundle.putString("EntryType", EntryType);
            bundle.putString("cashbookrefid", cashbookid);
            saleEntry.setArguments(bundle);
            saleEntry.show(getSupportFragmentManager(), "TAG");
        }

    }

    ////////////////////////////////Sending Update Data To SalePur1TAble Sqlite
    private void updateDataForSalePur1inSqlite() {
        ModelForSalePur1page2 temdata = spinnerData.get(spinneritemindex);
        Salepur1 salepur1 = new Salepur1();
        salepur1.setID(Integer.parseInt(pkid));
        //salepur1.setSalePur1ID((maxid));
        // salepur1.setEntryType(EntryType);
        ////////////////////Setting Today Date
        SPDate spDate = new SPDate();
        spDate.setDate(datePicker.getText().toString());
        Log.e("tem","actual:"+datePicker.getText().toString()+" Seted:"+spDate.getDate());
        salepur1.setSPDate(spDate);

        /////////////////////////////////////////
        salepur1.setAcNameID(Integer.parseInt(temdata.Columns[0]));
        salepur1.setRemarks(remarks.getText().toString());
//        salepur1.setClientID(Integer.parseInt(ClientID));
//        salepur1.setClientUserID(Integer.parseInt(ClientUserID));
        salepur1.setNetCode(GenericConstants.NullFieldStandardText);
        salepur1.setSysCode(GenericConstants.NullFieldStandardText);
        ////////////////////////////Set Updated Date
        UpdatedDate updatedDate = new UpdatedDate();
        updatedDate.setDate(GenericConstants.NullFieldStandardText);
        salepur1.setUpdatedDate(updatedDate);
        /////////////////
        salepur1.setNameOfPerson(nameofperson.getText().toString());
        String daysl = daysleft.getText().toString();
        if (daysl.isEmpty())
            daysl = "0";
        salepur1.setPayAfterDay(Integer.parseInt(daysl));
        Log.e("editdata", salepur1.toString());
        int statusid = helper.UpdateSalePur1Data(0, null, null, salepur1);
        if (statusid == -1)
            MNotificationClass.ShowToast(this, "Data Not Updated");
        else {
            List<Salepur1> llist = refdb.SlePur1.GetSalePur1Data(helper,
                    "Select * from SalePur1 where ID=" + salepur1.getID());
            if (llist.size() > 0) {

                String ii = updateThisSalePur1EntryToCAshBook(llist.get(0));

                MNotificationClass.ShowToast(this, "Data Updated cashbkid=" + ii);
            } else {
                MNotificationClass.ShowToast(this, "Data Updated ");
            }
        }
    }

    ///////////////////Generating Invoice ID from Sqlite
    private void generateInvoiceID() {
        maxid = refdb.SlePur1.GetMaximumID(helper, ClientID, EntryType);
        //maxid++;

        serialno.setText("Invoice " + maxid);
    }

    private String SaveDataToSalePur1(int maxid) {


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
        salepur1.setNameOfPerson(nameofperson.getText().toString());
        String daysl = daysleft.getText().toString();
        if (daysl.isEmpty())
            daysl = "0";
        salepur1.setPayAfterDay(Integer.parseInt(daysl));


        long iid = refdb.SlePur1.AddItemSalePur1(helper, salepur1);
        if (iid != -1) {
            pkid = String.valueOf(iid);
            // setFieldsEmpty();
            MNotificationClass.ShowToast(this, "Receipt Created " + iid);
            return addThisSalePur1EntryToCAshBook(salepur1);
        } else {
            MNotificationClass.ShowToast(this, "Receipt Not Created ");
        }
        return "";

    }

    private String addThisSalePur1EntryToCAshBook(Salepur1 salepur1tem) {
        Prefrence pp = new Prefrence(this);
        ModelForSalePur1page2 spdata = spinnerData.get(spinneritemindex);
        int maxcashbookid = refdb.CashBookTableRef.getmaxCashBookID(helper, "" + salepur1tem.getClientID());
        CashBook cashBook = new CashBook();
        if (salepur1tem.getEntryType().equals(DialogForAddNewSaleEntry.Entrytypes[0])) {
            cashBook.setDebitAccount(spdata.Columns[0]);
            cashBook.setCreditAccount("8");
        } else if (salepur1tem.getEntryType().equals(DialogForAddNewSaleEntry.Entrytypes[1])) {
            cashBook.setDebitAccount("9");
            cashBook.setCreditAccount(spdata.Columns[0]);
        } else if (salepur1tem.getEntryType().equals(DialogForAddNewSaleEntry.Entrytypes[2])) {
            cashBook.setDebitAccount("11");
            cashBook.setCreditAccount(spdata.Columns[0]);
        } else if (salepur1tem.getEntryType().equals(DialogForAddNewSaleEntry.Entrytypes[3])) {
            cashBook.setDebitAccount(spdata.Columns[0]);
            cashBook.setCreditAccount("12");
        }
        cashBook.setCashBookID(maxcashbookid + "");
        cashBook.setCBRemarks(salepur1tem.getRemarks() + "-" + salepur1tem.getNameOfPerson());
        cashBook.setAmount("0");
        cashBook.setTableName("SalePur1_" + salepur1tem.getEntryType());
        cashBook.setTableID("" + salepur1tem.getSalePur1ID());
        cashBook.setClientID(pp.getClientIDSession());
        cashBook.setClientUserID(pp.getClientUserIDSession());
        cashBook.setUpdatedDate(GenericConstants.NullFieldStandardText);
        cashBook.setSysCode("0");
        cashBook.setNetCode("0");
        cashBook.setCBDate(salepur1tem.getSPDate().getDate());
        Log.e("cashbok123",cashBook.toString());
        String id = helper.createCashBook(cashBook)+"";
        MNotificationClass.ShowToast(this, "Save Value Cash Book " + id);
        Log.e("cashbook", "Save Value Cash Book " + id);
        return id;


    }

    public String updateThisSalePur1EntryToCAshBook(Salepur1 salepur1tem) {
        Prefrence pp = new Prefrence(this);
        ModelForSalePur1page2 spdata = spinnerData.get(spinneritemindex);
        // int maxcashbookid = refdb.CashBookTableRef.getmaxCashBookID(helper, "" + salepur1tem.getClientID());
        CashBook cashBook = new CashBook();
        if (salepur1tem.getEntryType().equals("Sale")) {
            cashBook.setDebitAccount(spdata.Columns[0]);
            cashBook.setCreditAccount("8");
        } else if (salepur1tem.getEntryType().equals("Purchase")) {
            cashBook.setDebitAccount("9");
            cashBook.setCreditAccount(spdata.Columns[0]);
        } else if (salepur1tem.getEntryType().equals("Sale Return")) {
            cashBook.setDebitAccount("11");
            cashBook.setCreditAccount(spdata.Columns[0]);
        } else if (salepur1tem.getEntryType().equals("Purchase Return")) {
            cashBook.setDebitAccount(spdata.Columns[0]);
            cashBook.setCreditAccount("12");
        }
        // cashBook.setCashBookID(maxcashbookid + "");
        cashBook.setCBRemarks(salepur1tem.getRemarks() + "-" + salepur1tem.getNameOfPerson());
        ///////////////////Getting GrandTotal

        cashBook.setTableName("SalePur1_" + salepur1tem.getEntryType());
        cashBook.setTableID("" + salepur1tem.getSalePur1ID());
        cashBook.setClientID(salepur1tem.getClientID() + "");
        //  cashBook.setClientUserID(salepur1tem.getClientUserID()+"");
        cashBook.setUpdatedDate(GenericConstants.NullFieldStandardText);
        cashBook.setSysCode("0");
        cashBook.setNetCode("0");
        cashBook.setCBDate(salepur1tem.getSPDate().getDate());
        String grndt = refdb.CashBookTableRef.calcGrandTotal(helper, cashBook.getTableID(), cashBook.getClientID(), salepur1tem.getEntryType());
        cashBook.setAmount(grndt);
        long id = refdb.CashBookTableRef.updateCashBookwholeObject(helper, cashBook);
        MNotificationClass.ShowToast(this, "Save Value Cash Book " + id);
        Log.e("cashbook", "Save Value Cash Book " + id);
        return id + "";


    }

    private void setFieldsEmpty() {
        daysleft.setText("");
        nameofperson.setText("");
        remarks.setText("");
        if (griddata != null)
            griddata.clear();
        if (salePur1adapterPage2 != null)
            salePur1adapterPage2.notifyDataSetChanged();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_picker:

                if (locksaleitemedit) {
                    DialogFragment newFragment = new SelectDateFragment();
                    newFragment.show(getSupportFragmentManager(), "DatePicker");
                } else MNotificationClass.ShowToast(this, ENABLE_EDITING_MES);
                break;
            case R.id.headimage1:
                MNotificationClass.ShowToastTem(this, "1");
                break;
            case R.id.headimage2:
                //MNotificationClass.ShowToastTem(this, "2");
                moveToBackitemInList();
                break;
            case R.id.headimage3:
                //MNotificationClass.ShowToastTem(this, "3");
                moveToForwardItemInList();
                break;
            case R.id.headimage4:
                isSaleCreating = true;
                //MNotificationClass.ShowToastTem(this, "4");
                additemfab.setVisibility(View.VISIBLE);
                savebtn.setVisibility(View.INVISIBLE);
                cancelbtn.setVisibility(View.INVISIBLE);
                setFieldsEmpty();
                generateInvoiceID();
                nameofperson.setFocusable(true);
                nameofperson.setFocusableInTouchMode(true);
                remarks.setFocusable(true);
                remarks.setFocusableInTouchMode(true);
                daysleft.setFocusable(true);
                daysleft.setFocusableInTouchMode(true);
                //isedit = false;
                pkid = null;
                //ShowRowForAddItem();
                break;
            case R.id.headimage5:
                if (!isSaleCreating) {
                    MNotificationClass.ShowToastTem(this, "5");
                    nameofperson.setFocusable(true);
                    nameofperson.setFocusableInTouchMode(true);
                    nameofperson.setEnabled(true);
                    spinner.setEnabled(true);
                    spinner.setFocusable(true);
                    remarks.setFocusable(true);
                    remarks.setFocusableInTouchMode(true);
                    remarks.setEnabled(true);
                    daysleft.setFocusable(true);
                    daysleft.setFocusableInTouchMode(true);
                    daysleft.setEnabled(true);
                    additemfab.setVisibility(View.INVISIBLE);
                    locksaleitemedit = true;

                    savebtn.setVisibility(View.VISIBLE);
                    savebtn.bringToFront();
                    cancelbtn.setVisibility(View.VISIBLE);
                    cancelbtn.bringToFront();
                } else {
                    MNotificationClass.ShowToast(this, "Not Edit Able");
                }


                break;
            case R.id.headimage6:
                if (GenericConstants.isConnected(this)) {
                    refereshTables();
                } else {
                    MNotificationClass.ShowToast(this, "Please Check Your Internet Connection");
                }
                break;
            case R.id.headimage7:
                MNotificationClass.ShowToastTem(this, "7");
                break;
            case R.id.savebtn:
                MNotificationClass.ShowToastTem(this, "Save");
                //////////Calling the save btn
                if (pkid != null)
                    updateDataForSalePur1inSqlite();
                additemfab.setVisibility(View.INVISIBLE);
                savebtn.setVisibility(View.INVISIBLE);
                cancelbtn.setVisibility(View.INVISIBLE);
                nameofperson.setFocusable(false);
                remarks.setFocusable(false);
                daysleft.setFocusable(false);
                locksaleitemedit = false;
                break;
            case R.id.cancelbtn:
                MNotificationClass.ShowToastTem(this, "Cancel");
                savebtn.setVisibility(View.INVISIBLE);
                cancelbtn.setVisibility(View.INVISIBLE);
                additemfab.setVisibility(View.INVISIBLE);
                nameofperson.setFocusable(false);
                remarks.setFocusable(false);
                daysleft.setFocusable(false);
                locksaleitemedit = false;
                setDataOnDialogForEdit();
                break;
            case R.id.fabaddnewitem:
                ////////////////////////Add new Items
                AddNewDialog();

                break;
        }
    }

    @SuppressLint("RestrictedApi")
    private void moveToForwardItemInList() {
        isSaleCreating = false;
        locksaleitemedit = false;
        additemfab.setVisibility(View.INVISIBLE);
        boolean flagdata = false;
        if (arrayListForTrack != null && currentslectediteminList == -1) {
            currentslectediteminList = arrayListForTrack.size() - 1;
            flagdata = true;
        } else {
            if (arrayListForTrack != null && currentslectediteminList > 0) {
                currentslectediteminList--;
                flagdata = true;
            } else {
                flagdata = false;
                MNotificationClass.ShowToast(this, "No more Item ");
            }
        }


        if (arrayListForTrack != null && arrayListForTrack.size() > 0 && flagdata) {
            maxid = Integer.parseInt(arrayListForTrack.get(currentslectediteminList).getSalePur1ID());
            ClientID = arrayListForTrack.get(currentslectediteminList).getClientID();
            EntryType = arrayListForTrack.get(currentslectediteminList).getEntryType();
            setDataOnDialogForEdit();
            serialno.setText("Invoice " + maxid);
            pkid = arrayListForTrack.get(currentslectediteminList).PkID;
            SetDataOnGridView();
        } else {
            MNotificationClass.ShowToast(this, "No items");
        }
    }

    @SuppressLint("RestrictedApi")
    private void moveToBackitemInList() {

        ////////////
        isSaleCreating = false;
        locksaleitemedit = false;
        additemfab.setVisibility(View.INVISIBLE);
        boolean flagdata = false;
        if (currentslectediteminList == -1) {
            currentslectediteminList = 1;
            flagdata = true;
        } else {
            if (arrayListForTrack != null && currentslectediteminList < arrayListForTrack.size() - 1) {
                currentslectediteminList++;
                flagdata = true;
            } else {
                flagdata = false;
                MNotificationClass.ShowToast(this, "No more Item ");
            }
        }


        if (arrayListForTrack != null && arrayListForTrack.size() > 0 && flagdata) {
            maxid = Integer.parseInt(arrayListForTrack.get(currentslectediteminList).getSalePur1ID());
            ClientID = arrayListForTrack.get(currentslectediteminList).getClientID();
            EntryType = arrayListForTrack.get(currentslectediteminList).getEntryType();
            setDataOnDialogForEdit();
            serialno.setText("Invoice " + maxid);
            pkid = arrayListForTrack.get(currentslectediteminList).PkID;
            SetDataOnGridView();
        } else {
            MNotificationClass.ShowToast(this, "No items");
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
        if (pkid != null)
            SetDataOnGridView();
        try {
            if (AppController.compareDate()) {
                Prefrence pp = new Prefrence(this);
                int cliid=Integer.parseInt(pp.getClientIDSession());
                cliid++;
                pp.setClientIDSession(cliid+"");
                pp.setMYClientUserIDSession(pp.getClientUserIDSession()+ "1");
                maxid = 1;

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pkid != null)
            SetDataOnGridView();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pkid != null)
            SetDataOnGridView();

    }


    ////////////////Listen Menu Item Click Listner
    @Override
    public void popUpMyClickListener(MenuItem item, String ID, ModelForSalePur1page2 model) {
        if (locksaleitemedit && item.getTitle().equals("Edit")) {
            String Entrytypes[] = DialogForAddNewSaleEntry.Entrytypes;
            MNotificationClass.ShowToastTem(this, "Item:" + item.getTitle() + " ID:" + ID + " model:" +
                    model.Columns[8]+" SalePur1ID:" +
                    model.Columns[0]);

            DialogForAddNewSaleEntry dialogForAdd = new DialogForAddNewSaleEntry();
            Bundle bundle = new Bundle();
//        SalePur1ID 0
//        ItemSerial 1
//        ItemDesc    2
//        Qty         3
//        Price      4
//        Total      5
//        EntryType   6
//        ClientID    7
//          ID       8
            bundle.putString("ClientID", model.Columns[7]);
            bundle.putInt("SalePur1ID", Integer.parseInt(model.Columns[0]));
            bundle.putString("EntryType", model.Columns[6]);
            bundle.putString("ID", model.Columns[8]);
            dialogForAdd.setArguments(bundle);
            dialogForAdd.show(getSupportFragmentManager(), "TAg");
        } else if (item.getTitle().equals("Edit"))
            MNotificationClass.ShowToast(this, "Enable Editing First");

    }

    /////////////////////////////////Get Data For Back And Forward Query
    public void GetDataFroBackAndForwardTracking(int sortbyfiels, String curSalePur1ID) {
        String query = "  SELECT         SalePur1.SalePur1ID, SalePur1.EntryType, SalePur1.SPDate, Account3Name.AcName, SalePur1.Remarks, SUM(SalePur2.Total) AS BillAmt, SalePur1.ClientID, SalePur1.NameOfPerson, SalePur1.PayAfterDay,SalePur1.ID\n" +
                "        FROM            SalePur1 INNER JOIN\n" +
                "        Account3Name ON SalePur1.AcNameID = Account3Name.AcNameID INNER JOIN\n" +
                "        SalePur2 ON SalePur1.SalePur1ID = SalePur2.SalePur1ID\n" +
                "        GROUP BY SalePur1.SalePur1ID, SalePur1.EntryType, SalePur1.SPDate, Account3Name.AcName, SalePur1.ClientID, SalePur1.NameOfPerson, SalePur1.PayAfterDay, SalePur1.Remarks\n" +
                "        HAVING        (SalePur1.EntryType = '" + EntryType + "') AND (SalePur1.ClientID = " + prefrence.getClientIDSession() + ")\n" +
                "        ORDER BY " + sortbyfiels + " DESC  ";

        arrayListForTrack = helper.GetDataFroJoinQuery(query);

        salePur1IdSeq = new String[arrayListForTrack.size()];
        for (int i = 0; i < arrayListForTrack.size(); i++) {
            Log.e("sequence", i + "-(" + arrayListForTrack.get(i).getSalePur1ID() + ")");
            salePur1IdSeq[i] = arrayListForTrack.get(i).getSalePur1ID();
            arrayListForTrack.get(i).getSalePur1ID();
            if (salePur1IdSeq[i].equals(curSalePur1ID))
                currentslectediteminList = i;
        }
        //    circularQue(arrayListForTrack,null);
    }
    //////////////////Circular Que for back forward Treacking
//    public void circularQue(List<JoinQueryDaliyEntryPage1> arrayListForTrack,String curSalepurid){
//
//    }

}
