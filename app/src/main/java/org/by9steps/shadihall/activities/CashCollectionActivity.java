package org.by9steps.shadihall.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.fragments.SelectDateFragment;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.InputValidation;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.ChartOfAcc;
import org.by9steps.shadihall.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class CashCollectionActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout date_layout;
    public static TextView date;
    TextInputLayout description_layout;
    TextInputEditText description;
    TextInputLayout amount_layout;
    TextInputEditText amount;
    TextInputLayout debit_account_layout, credit_account_layout;
    TextView debit_account, credit_account, tv_cr, tv_db;
    Button add;

    InputValidation inputValidation;

    String bookingID, spinnerType;
    ProgressDialog pDialog;

    ArrayList<String> listDebit = new ArrayList<>();
    ArrayList<String> listCredit = new ArrayList<>();
    ArrayList<String> mDebit = new ArrayList<>();
    ArrayList<String> mCredit = new ArrayList<>();
    SpinnerDialog debitDialog, creditDialog;

    DatabaseHelper databaseHelper;
    List<ChartOfAcc> chartOfAccList;
    String query = "", currentDate, dDebit = null, cCredit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_collection);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Cash Collection");
        }

        Intent intent = getIntent();
        if (intent != null) {
            bookingID = intent.getStringExtra("BookingID");
            spinnerType = intent.getStringExtra("Spinner");
        }

        inputValidation = new InputValidation(this);
        databaseHelper = new DatabaseHelper(this);
        if (isConnected()){
            addCashBook();
        }

        Date da = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = sf.format(da);

        List<User> list = User.listAll(User.class);
        for (User u : list){
            query = "SELECT        Account1Type.AcTypeID, Account1Type.AcTypeName, Account2Group.AcGroupID, Account2Group.AcGruopName, derivedtbl_1.AccountID, Account3Name.AcName, SUM(derivedtbl_1.Debit) AS Debit, SUM(derivedtbl_1.Credit) \n" +
                    "                         AS Credit, derivedtbl_1.ClientID, SUM(derivedtbl_1.Debit) - SUM(derivedtbl_1.Credit) AS Bal, CASE WHEN (SUM(Debit) - SUM(Credit)) > 0 THEN (SUM(Debit) - SUM(Credit)) ELSE 0 END AS DebitBL, CASE WHEN (SUM(Debit) \n" +
                    "                         - SUM(Credit)) < 0 THEN (SUM(Debit) - SUM(Credit)) ELSE 0 END AS CreditBL, MAX(derivedtbl_1.CBDate) AS MaxDate\n" +
                    "FROM            (SELECT        CreditAccount AS AccountID, 0 AS Debit, Amount AS Credit, ClientID, CBDate\n" +
                    "                          FROM            CashBook AS CashBook\n" +
                    "                          WHERE        (ClientID = "+u.getClientID()+") AND (CBDate <= '"+currentDate+"')\n" +
                    "                          UNION ALL\n" +
                    "                          SELECT        DebitAccount AS AccountID, Amount AS Debit, 0 AS Credit, ClientID, CBDate\n" +
                    "                          FROM            CashBook AS CashBook_1\n" +
                    "                          WHERE        (ClientID = "+u.getClientID()+") AND (CBDate <= '"+currentDate+"')) AS derivedtbl_1 INNER JOIN\n" +
                    "                         Account3Name ON derivedtbl_1.AccountID = Account3Name.AcNameID INNER JOIN\n" +
                    "                         Account2Group ON Account3Name.AcGroupID = Account2Group.AcGroupID INNER JOIN\n" +
                    "                         Account1Type ON Account2Group.AcTypeID = Account1Type.AcTypeID\n" +
                    "GROUP BY derivedtbl_1.ClientID, derivedtbl_1.AccountID, Account3Name.AcName, Account2Group.AcGroupID, Account2Group.AcGruopName, Account1Type.AcTypeName, Account1Type.AcTypeID";
        }

        List<ChartOfAcc> li = databaseHelper.getChartofAccount(query);
        for (ChartOfAcc a : li){
            listCredit.add(a.getAcName());
            mCredit.add(a.getAccountID());
            listDebit.add(a.getAcName());
            mDebit.add(a.getAccountID());
        }

        date_layout = findViewById(R.id.date_layout);
        date = findViewById(R.id.date);
        description_layout = findViewById(R.id.description_layout);
        description = findViewById(R.id.description);
        amount_layout = findViewById(R.id.enter_amount_layout);
        amount = findViewById(R.id.enter_amount);
        debit_account = findViewById(R.id.debit_account);
        debit_account_layout = findViewById(R.id.debit_account_layout);
        credit_account = findViewById(R.id.credit_account);
        credit_account_layout = findViewById(R.id.credit_account_layout);
        tv_cr = findViewById(R.id.tv_cr);
        tv_db = findViewById(R.id.tv_db);
        add = findViewById(R.id.add);

        Date date1 = new Date();
        SimpleDateFormat curFormater = new SimpleDateFormat("dd-M-yyyy");
        String d = curFormater.format(date1);
        date.setText(d);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.date = "CashBook";
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getSupportFragmentManager(), "DatePicker");
            }
        });


        if (!spinnerType.equals("Hide")){
//            tv_cr.setVisibility(View.GONE);
//            tv_db.setVisibility(View.GONE);
            debit_account.setOnClickListener(this);
            credit_account.setOnClickListener(this);
            debit_account.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop,0);
            credit_account.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop,0);
        }else {
//            credit_account_layout.setVisibility(View.GONE);
//            debit_account_layout.setVisibility(View.GONE);
//            List<User> list = User.listAll(User.class);
//            for (User u : list) {
                credit_account.setText("Income");
                debit_account.setText("Cash");
//            }
        }

        add.setOnClickListener(this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.debit_account:
                Log.e("CLICK","CLICK");
                debitDialog = new SpinnerDialog(CashCollectionActivity.this,listDebit,"Select Debit");
                debitDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String s, int i) {
                        Toast.makeText(CashCollectionActivity.this,"Item Selected",Toast.LENGTH_LONG).show();
                        dDebit = mDebit.get(i);
                        debit_account.setText(s);
                    }
                });
                debitDialog.showSpinerDialog();
                break;
            case R.id.credit_account:
                Log.e("CLICK","CLICK");
                creditDialog = new SpinnerDialog(CashCollectionActivity.this,listCredit,"Select Credit");
                creditDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String s, int i) {
                        Toast.makeText(CashCollectionActivity.this,"Item Selected",Toast.LENGTH_LONG).show();
                        credit_account.setText(s);
                        cCredit = mCredit.get(i);
                    }
                });
                creditDialog.showSpinerDialog();
                break;
            case R.id.add:

                if (cCredit == null){
                    Toast.makeText(CashCollectionActivity.this,"Select Credit",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dDebit == null){
                    Toast.makeText(CashCollectionActivity.this,"Select Debit",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(amount, amount_layout, getString(R.string.error_message_amount))) {
                    return;
                }else{

                    final List<User> list = User.listAll(User.class);

                    if (isConnected()){
                        String tag_json_obj = "json_obj_req";
                        String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/AddCashBook.php";

                        pDialog = new ProgressDialog(CashCollectionActivity.this);
                        pDialog.setMessage("Loading...");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        pDialog.dismiss();
                                        Log.e("Response",response);
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String success = jsonObject.getString("success");
                                            if (success.equals("1")){
                                                String message = jsonObject.getString("message");
                                                Toast.makeText(CashCollectionActivity.this, message, Toast.LENGTH_SHORT).show();
//                                            finish();
                                                Date d = new Date();
                                                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                                                String da = sf.format(d);
                                                for (User u : list) {
                                                    databaseHelper.createCashBook(new CashBook("0", date.getText().toString(), dDebit, cCredit, description.getText().toString(), amount.getText().toString(), u.getClientID(),u.getClientUserID(),"0","0",da,bookingID));
                                                }
                                                clearCashe();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.dismiss();
                                Log.e("Error",error.toString());
                                Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() {

                                Map<String, String> params = new HashMap<String, String>();
                                List<User> list = User.listAll(User.class);
                                for (User u : list) {
                                    params.put("CBDate", date.getText().toString());
                                    params.put("DebitAccount", dDebit);
                                    params.put("CreditAccount", cCredit);
                                    params.put("CBRemarks", description.getText().toString());
                                    params.put("Amount", amount.getText().toString());
                                    params.put("ClientID", u.getClientID());
                                    params.put("ClientUserID", u.getClientUserID());
                                    params.put("NetCode", "0");
                                    params.put("SysCode", "0");
                                    params.put("BookingID", bookingID);
                                }
                                return params;
                            }
                        };
                        int socketTimeout = 30000;//30 seconds - change to what you want
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        jsonObjectRequest.setRetryPolicy(policy);
                        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
                    }else {
                        for (User u : list) {
                            databaseHelper.createCashBook(new CashBook("0", currentDate, dDebit, cCredit, description.getText().toString(), amount.getText().toString(), u.getClientID(),u.getClientUserID(),"0","0",currentDate,bookingID));
                        }
                        clearCashe();
                    }
                }
                break;
        }
    }

    public void addCashBook(){
        String query = "SELECT * FROM CashBook WHERE CashBookID = 0";
        List<CashBook> addCashBook;
        addCashBook = databaseHelper.getCashBook(query);

        for (final CashBook c : addCashBook){
            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/AddCashBook.php";

            pDialog = new ProgressDialog(CashCollectionActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pDialog.dismiss();
                            Log.e("Response",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                if (success.equals("1")){
                                    String message = jsonObject.getString("message");
//                                    Toast.makeText(CashCollectionActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    Log.e("Error",error.toString());
//                    Toast.makeText(CashCollectionActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    List<User> list = User.listAll(User.class);
                    for (User u : list) {
                        params.put("CBDate", c.getCBDate());
                        params.put("DebitAccount", c.getDebitAccount());
                        params.put("CreditAccount", c.getCreditAccount());
                        params.put("CBRemarks", c.getCBRemarks());
                        params.put("Amount", c.getAmount());
                        params.put("ClientID", u.getClientID());
                        params.put("ClientUserID", u.getClientUserID());
                        params.put("NetCode", "0");
                        params.put("SysCode", "0");
                        params.put("BookingID", c.getBookingID());
                    }
                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
    }

    public void clearCashe(){
        cCredit = null;
        dDebit = null;
        description.setText("");
        amount.setText("");
        debit_account.setText("Select Debit");
        credit_account.setText("Select Credit");
    }

    //Check Internet Connection
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }


}
