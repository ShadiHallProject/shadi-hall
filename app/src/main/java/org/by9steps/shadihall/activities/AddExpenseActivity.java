package org.by9steps.shadihall.activities;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.InputValidation;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout date_layout;
    TextView date;
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
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Expense Collection");
        }

        Intent intent = getIntent();
        if (intent != null) {
            bookingID = intent.getStringExtra("BookingID");
            spinnerType = intent.getStringExtra("Spinner");
        }

        inputValidation = new InputValidation(this);
        databaseHelper = new DatabaseHelper(this);

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
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        String d = curFormater.format(date1);
        date.setText(d);

        if (!spinnerType.equals("Hide")){
            tv_cr.setVisibility(View.GONE);
            tv_db.setVisibility(View.GONE);
            debit_account.setOnClickListener(this);
            credit_account.setOnClickListener(this);
            debit_account.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop,0);
            credit_account.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_drop,0);
        }else {
//            credit_account_layout.setVisibility(View.GONE);
//            debit_account_layout.setVisibility(View.GONE);
            List<User> list = User.listAll(User.class);
//            for (User u : list) {
            credit_account.setText("Expense");
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
            case R.id.add:

                if (!inputValidation.isInputEditTextFilled(amount, amount_layout, getString(R.string.error_message_amount))) {
                    return;
                }else{

                    final List<User> list = User.listAll(User.class);

                    for (User u : list) {
                        databaseHelper.createCashBook(new CashBook("0", date.getText().toString(), u.getBookingExpenseID(), u.getCashID(), description.getText().toString(), amount.getText().toString(), u.getClientID(),u.getClientUserID(),"0","0","0",bookingID));
                    }

//                    String tag_json_obj = "json_obj_req";
//                    String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/AddCashBook.php";
//
//                    pDialog = new ProgressDialog(AddExpenseActivity.this);
//                    pDialog.setMessage("Loading...");
//                    pDialog.setCancelable(false);
//                    pDialog.show();
//                    StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//                                    pDialog.dismiss();
//                                    Log.e("Response",response);
//                                    try {
//                                        JSONObject jsonObject = new JSONObject(response);
//                                        String success = jsonObject.getString("success");
//                                        if (success.equals("1")){
//                                            String message = jsonObject.getString("message");
//                                            Toast.makeText(AddExpenseActivity.this, message, Toast.LENGTH_SHORT).show();
//                                            finish();
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            pDialog.dismiss();
//                            Log.e("Error",error.toString());
//                            Toast.makeText(AddExpenseActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }){
//                        @Override
//                        protected Map<String, String> getParams() {
//
//                            Map<String, String> params = new HashMap<String, String>();
//                            List<User> list = User.listAll(User.class);
//                            for (User u : list) {
//                                params.put("CBDate", date.getText().toString());
//                                params.put("DebitAccount", u.getBookingExpenseID());
//                                params.put("CreditAccount", u.getCashID());
//                                params.put("CBRemarks", description.getText().toString());
//                                params.put("Amount", amount.getText().toString());
//                                params.put("ClientID", u.getClientID());
//                                params.put("ClientUserID", u.getClientUserID());
//                                params.put("NetCode", "0");
//                                params.put("SysCode", "0");
//                                params.put("BookingID", bookingID);
//                            }
//                            return params;
//                        }
//                    };
//                    int socketTimeout = 30000;//30 seconds - change to what you want
//                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//                    jsonObjectRequest.setRetryPolicy(policy);
//                    AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

                }
                break;
        }

    }
}
