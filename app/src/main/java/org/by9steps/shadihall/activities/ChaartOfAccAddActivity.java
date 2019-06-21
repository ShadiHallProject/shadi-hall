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
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.ChartOfAcc;
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
    TextView loginInfo;
    Spinner spinner;
    Button add;

    InputValidation inputValidation;
    DatabaseHelper databaseHelper;
    ProgressDialog pDialog;

    int spPosition = 0;
    String groupID, groupName, type, acNameID, id;
    List<Account3Name> chartOfAcc;

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
            groupName = intent.getStringExtra("GroupName");
            type = intent.getStringExtra("Type");
            acNameID = intent.getStringExtra("AcNameID");
        }

        Log.e("CHART",groupID + "  "+ groupName);

        inputValidation = new InputValidation(this);
        databaseHelper = new DatabaseHelper(this);

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
        loginInfo = findViewById(R.id.login_info);
        login_mobile_layout = findViewById(R.id.login_mobile_layout);
        login_mobile = findViewById(R.id.login_mobile);
        password_layout = findViewById(R.id.password_layout);
        password = findViewById(R.id.password);
        spinner = findViewById(R.id.spinner);
        add = findViewById(R.id.add);


        if (groupName.equals("Client") || groupName.equals("Suppliers") || groupID.equals("5") || groupID.equals("6")){
            name_layout.setVisibility(View.VISIBLE);
            address_layout.setVisibility(View.VISIBLE);
            mobile_layout.setVisibility(View.VISIBLE);
            email_layout.setVisibility(View.VISIBLE);
            salary_layout.setVisibility(View.GONE);
            login_mobile_layout.setVisibility(View.VISIBLE);
            password_layout.setVisibility(View.VISIBLE);
        }else if (groupName.equals("Cash And Bank") || groupName.equals("General Expense") || groupName.equals("Capital")
                || groupName.equals("Fixed Assets") || groupName.equals("Incom") || groupID.equals("1") || groupID.equals("3")
                || groupID.equals("8") || groupID.equals("4") || groupID.equals("7")){

            name_layout.setVisibility(View.VISIBLE);
            address_layout.setVisibility(View.GONE);
            mobile_layout.setVisibility(View.GONE);
            email_layout.setVisibility(View.GONE);
            salary_layout.setVisibility(View.GONE);
            loginInfo.setVisibility(View.GONE);
            login_mobile_layout.setVisibility(View.GONE);
            password_layout.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            spinner.setSelection(0);
            spPosition = 0;
        }else if (groupName.equals("Employee") || groupID.equals("2")){
            name_layout.setVisibility(View.VISIBLE);
            address_layout.setVisibility(View.VISIBLE);
            mobile_layout.setVisibility(View.VISIBLE);
            email_layout.setVisibility(View.VISIBLE);
            salary_layout.setVisibility(View.VISIBLE);
            login_mobile_layout.setVisibility(View.VISIBLE);
            password_layout.setVisibility(View.VISIBLE);
        }

        add.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);

        String[] sp_items = {"Not Allowed To Login","Admin","Custom Rights"};
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sp_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (type.equals("Edit")){
            String query = "SELECT * FROM Account3Name WHERE AcNameID = "+acNameID;
            chartOfAcc = databaseHelper.getAccount3Name(query);
            for (Account3Name c : chartOfAcc){
                id = c.getId();
                name.setText(c.getAcName());
                address.setText(c.getAcAddress());
                mobile.setText(c.getAcContactNo());
                email.setText(c.getAcEmailAddress());
                salary.setText(c.getSalary());
                login_mobile.setText(c.getAcMobileNo());
                password.setText(c.getAcPassward());
                Log.e("CCCCCC",c.getSecurityRights());
                if (c.getSecurityRights().equals("0")){
                    spinner.setSelection(0);
                }else if (c.getSecurityRights().equals("1")){
                    spinner.setSelection(1);
                }else if (c.getSecurityRights().equals("2")){
                    spinner.setSelection(2);
                }
                add.setText(getString(R.string.update));
            }
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

                    List<User> list = User.listAll(User.class);
                    String query;
                    String query1;
                        for (User u : list) {


                            if (type.equals("Add")) {

                                query = "SELECT AcName FROM Account3Name WHERE ClientID = "+u.getClientID() + " AND AcName = '"+name.getText().toString()+"'";
                                query1 = "SELECT AcMobileNo FROM Account3Name WHERE ClientID = "+u.getClientID()+" AND AcMobileNo = '"+login_mobile.getText().toString()+"'";

                                if (databaseHelper.findAccount3Name(query)){
                                    Toast.makeText(ChaartOfAccAddActivity.this, "Name Already Register", Toast.LENGTH_SHORT).show();
                                }else if (databaseHelper.findAccount3Name(query1)){
                                    Toast.makeText(ChaartOfAccAddActivity.this, "Login Number Already Register", Toast.LENGTH_SHORT).show();
                                }else {
                                    databaseHelper.createAccount3Name(new Account3Name("0", name.getText().toString(), groupID, address.getText().toString(), login_mobile.getText().toString(),
                                            mobile.getText().toString(), email.getText().toString(), "", "", password.getText().toString(), u.getClientID(), u.getClientUserID(), "0", "0", "0", "",
                                            "", String.valueOf(spPosition), salary.getText().toString()));
                                    finish();
                                }

                            }else if (type.equals("Edit")){

                                query = "SELECT AcName FROM Account3Name WHERE ClientID = "+u.getClientID() + " AND AcName = '"+name.getText().toString() +"' AND ID != "+id;
                                query1 = "SELECT AcMobileNo FROM Account3Name WHERE ClientID = "+u.getClientID()+" AND AcMobileNo = '"+login_mobile.getText().toString()+"' AND ID != "+id;

                                if (databaseHelper.findAccount3Name(query)){
                                    Toast.makeText(ChaartOfAccAddActivity.this, "Name Already Register", Toast.LENGTH_SHORT).show();
                                }else if (databaseHelper.findAccount3Name(query1)){
                                    Toast.makeText(ChaartOfAccAddActivity.this, "Login Number Already Register", Toast.LENGTH_SHORT).show();
                                }else {

                                    if (groupName.equals("Client") || groupName.equals("Suppliers") || groupID.equals("5") || groupID.equals("6")){

                                        query = "Update Account3Name SET AcName = '"+name.getText().toString()+"', AcAddress = '"+address.getText().toString()+"', AcMobileNo = '"+login_mobile.getText().toString()
                                                +"', AcContactNo ='"+mobile.getText().toString()+"', AcEmailAddress = '"+email.getText().toString()+"', AcPassward = '"+password.getText().toString()
                                                +"', SecurityRights = '"+spPosition+"', UpdatedDate = '0' WHERE AcNameID = "+ acNameID;
                                        databaseHelper.updateAccount3Name(query);
                                        finish();

                                    }else if (groupName.equals("Cash And Bank") || groupName.equals("General Expense") || groupName.equals("Capital")
                                            || groupName.equals("Fixed Assets") || groupName.equals("Incom") || groupID.equals("1") || groupID.equals("3")
                                            || groupID.equals("8") || groupID.equals("4") || groupID.equals("7")){

                                        query = "Update Account3Name SET AcName = '"+name.getText().toString()+"', UpdatedDate = '0'";
                                        databaseHelper.updateAccount3Name(query);
                                        finish();

                                    }else if (groupName.equals("Employee") || groupID.equals("2")){
                                        query = "Update Account3Name SET AcName = '"+name.getText().toString()+"', AcAddress = '"+address.getText().toString()+"', AcMobileNo = '"+login_mobile.getText().toString()
                                                +"', AcContactNo ='"+mobile.getText().toString()+"', AcEmailAddress = '"+email.getText().toString()+"', AcPassward = '"+password.getText().toString()
                                                +"', SecurityRights = '"+spPosition+"', Salary = '"+salary.getText().toString()+"', UpdatedDate = '0' WHERE AcNameID = "+ acNameID;
                                        databaseHelper.updateAccount3Name(query);
                                        finish();
                                    }

                                }
                            }
                        }


//                    String tag_json_obj = "json_obj_req";
//                    String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/AddCharofAcc.php";
//
//                    pDialog = new ProgressDialog(ChaartOfAccAddActivity.this);
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
//                                            Toast.makeText(ChaartOfAccAddActivity.this, message, Toast.LENGTH_SHORT).show();
//                                            finish();
//                                        }else {
//                                            String message = jsonObject.getString("message");
//                                            Toast.makeText(ChaartOfAccAddActivity.this, message, Toast.LENGTH_SHORT).show();
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
//                            Toast.makeText(ChaartOfAccAddActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }){
//                        @Override
//                        protected Map<String, String> getParams() {
//
//                            Map<String, String> params = new HashMap<String, String>();
//                            List<User> list = User.listAll(User.class);
//                            for (User u : list) {
//                                params.put("AcName", name.getText().toString());
//                                params.put("AcAddress", address.getText().toString());
//                                params.put("AcContactNo", mobile.getText().toString());
//                                params.put("AcEmailAddress", email.getText().toString());
//                                params.put("Salary", salary.getText().toString());
//                                params.put("AcMobileNo", login_mobile.getText().toString());
//                                params.put("AcPassward", password.getText().toString());
//                                params.put("SecurityRights", String.valueOf(spPosition));
//                                params.put("ClientID", u.getClientID());
//                                params.put("AcGroupID", groupID);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spPosition = position;
        Log.e("Position",String.valueOf(spPosition));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
