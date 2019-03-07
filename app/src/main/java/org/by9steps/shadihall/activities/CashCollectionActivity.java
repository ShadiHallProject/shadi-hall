package org.by9steps.shadihall.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
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
import org.by9steps.shadihall.helper.InputValidation;
import org.by9steps.shadihall.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class CashCollectionActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout date_layout;
    TextView date;
    TextInputLayout description_layout;
    TextInputEditText description;
    TextInputLayout amount_layout;
    TextInputEditText amount;
    TextInputLayout debit_account_layout, credit_account_layout;
    TextView debit_account, credit_account;
    Button add;

    InputValidation inputValidation;

    String bookingID, spinnerType;
    ProgressDialog pDialog;

    ArrayList<String> mList = new ArrayList<>();
    SpinnerDialog debitDialog, creditDialog;

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
        add = findViewById(R.id.add);

        Date date1 = new Date();
        SimpleDateFormat curFormater = new SimpleDateFormat("dd-M-yyyy");
        String d = curFormater.format(date1);
        date.setText(d);


        if (!spinnerType.equals("Hide")){
            debit_account.setOnClickListener(this);
            credit_account.setOnClickListener(this);
            debit_account.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop,0);
            credit_account.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop,0);
        }else {
            credit_account_layout.setVisibility(View.GONE);
            debit_account_layout.setVisibility(View.GONE);
        }

        add.setOnClickListener(this);

        for (int i = 0; i<5; i++){
            mList.add("Item"+" "+i);
        }


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
                debitDialog = new SpinnerDialog(CashCollectionActivity.this,mList,"Debit Account");
                debitDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String s, int i) {
                        Toast.makeText(CashCollectionActivity.this,"Item Selected",Toast.LENGTH_LONG).show();
                        debit_account.setText(s);
                    }
                });
                debitDialog.showSpinerDialog();
                break;
            case R.id.credit_account:
                Log.e("CLICK","CLICK");
                creditDialog = new SpinnerDialog(CashCollectionActivity.this,mList,"Credit Account");
                creditDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String s, int i) {
                        Toast.makeText(CashCollectionActivity.this,"Item Selected",Toast.LENGTH_LONG).show();
                        credit_account.setText(s);
                    }
                });
                creditDialog.showSpinerDialog();
                break;
            case R.id.add:

                if (!inputValidation.isInputEditTextFilled(amount, amount_layout, getString(R.string.error_message_amount))) {
                    return;
                }else{

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
                                            finish();
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
                                params.put("DebitAccount", u.getCashID());
                                params.put("CreditAccount", u.getBookingIncomeID());
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

                }
                break;
        }

    }


}
