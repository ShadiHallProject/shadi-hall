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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import org.by9steps.shadihall.helper.InputValidation;
import org.by9steps.shadihall.model.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChaartOfAccAddActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    TextInputLayout name_layout;
    TextInputEditText name;
    TextInputLayout address_layout;
    TextInputEditText address;
    TextInputLayout mobile_layout;
    TextInputEditText mobile;
    TextInputLayout email_layout;
    TextInputEditText email;
    TextInputLayout salary_layout;
    TextInputEditText salary;
    TextInputLayout login_mobile_layout;
    TextInputEditText login_mobile;
    TextInputLayout password_layout;
    TextInputEditText password;
    Spinner spinner;
    Button add;

    InputValidation inputValidation;
    ProgressDialog pDialog;

    int spPosition = 0;
    String groupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chaart_of_acc_add);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add");
        }

        Intent intent = getIntent();
        if (intent != null) {
            groupID = intent.getStringExtra("GroupID");
        }

        inputValidation = new InputValidation(this);

        name_layout = findViewById(R.id.name_layout);
        name = findViewById(R.id.name);
        address_layout = findViewById(R.id.address_layout);
        address = findViewById(R.id.address);
        mobile_layout = findViewById(R.id.mobile_layout);
        mobile = findViewById(R.id.mobile);
        email_layout = findViewById(R.id.email_layout);
        email = findViewById(R.id.email);
        salary_layout = findViewById(R.id.salary_layout);
        salary = findViewById(R.id.salary);
        login_mobile_layout = findViewById(R.id.login_mobile_layout);
        login_mobile = findViewById(R.id.login_mobile);
        password_layout = findViewById(R.id.password_layout);
        password = findViewById(R.id.password);
        spinner = findViewById(R.id.spinner);
        add = findViewById(R.id.add);

        add.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);

        String[] sp_items = {"Not Allowed To Login","Admin","Custom Rights"};
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sp_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
                if (!inputValidation.isInputEditTextFilled(name, name_layout, getString(R.string.error_message_c_name))) {
                    return;
                }if (!inputValidation.isInputEditTextFilled(address, address_layout, getString(R.string.error_message_c_address))) {
                return;
                }if (!inputValidation.isInputEditTextFilled(mobile, mobile_layout, getString(R.string.error_message_c_number))) {
                return;
                }if (!inputValidation.isInputEditTextFilled(email, email_layout, getString(R.string.error_message_email))) {
                return;
                }if (!inputValidation.isInputEditTextFilled(salary, salary_layout, getString(R.string.error_message_salary))) {
                return;
                }if (!inputValidation.isInputEditTextFilled(login_mobile, login_mobile_layout, getString(R.string.error_message_login_number))) {
                return;
                }if (!inputValidation.isInputEditTextFilled(password, password_layout, getString(R.string.error_message_password))) {
                return;
                }else{

                    String tag_json_obj = "json_obj_req";
                    String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/AddCharofAcc.php";

                    pDialog = new ProgressDialog(ChaartOfAccAddActivity.this);
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
                                            Toast.makeText(ChaartOfAccAddActivity.this, message, Toast.LENGTH_SHORT).show();
                                            finish();
                                        }else {
                                            String message = jsonObject.getString("message");
                                            Toast.makeText(ChaartOfAccAddActivity.this, message, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ChaartOfAccAddActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() {

                            Map<String, String> params = new HashMap<String, String>();
                            List<User> list = User.listAll(User.class);
                            for (User u : list) {
                                params.put("AcName", name.getText().toString());
                                params.put("AcAddress", address.getText().toString());
                                params.put("AcContactNo", mobile.getText().toString());
                                params.put("AcEmailAddress", email.getText().toString());
                                params.put("Salary", salary.getText().toString());
                                params.put("AcMobileNo", login_mobile.getText().toString());
                                params.put("AcPassward", password.getText().toString());
                                params.put("SecurityRights", String.valueOf(spPosition));
                                params.put("ClientID", u.getClientID());
                                params.put("AcGroupID", groupID);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spPosition = position;
        Log.e("Position",String.valueOf(spPosition));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
