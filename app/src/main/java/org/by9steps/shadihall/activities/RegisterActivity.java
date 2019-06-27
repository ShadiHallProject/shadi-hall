package org.by9steps.shadihall.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.orm.SugarContext;
import com.orm.util.NamingHelper;
import com.squareup.picasso.Picasso;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.SpinnerAdapter;
import org.by9steps.shadihall.helper.InputValidation;
import org.by9steps.shadihall.model.AreaName;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    InputValidation inputValidation;

    TextInputLayout c_name_layout;
    TextInputEditText c_name;
    TextInputLayout c_address_layout;
    TextInputEditText c_address;
    TextInputLayout name_of_person_layout;
    TextInputEditText name_of_person;
//    TextInputLayout country_layout;
//    TextInputEditText country;
    TextInputLayout password_layout;
    TextInputEditText password;
    TextInputLayout c_number_layout;
    TextInputEditText c_number;
    TextInputLayout login_number_layout;
    TextView login_number;
//    TextInputLayout city_layout;
//    TextInputEditText city;
//    TextInputLayout sub_city_layout;
//    TextInputEditText sub_city;
    TextInputLayout persons_layout;
    TextInputEditText persons;
    TextInputLayout website_layout;
    TextInputEditText website;
    TextInputLayout email_layout;
    TextInputEditText email;
    CircleImageView image;
    Button register;
    ImageView contact_list;
    Spinner sp_country;
    Spinner sp_city;
    Spinner sp_sub_city;

    ProgressDialog pDialog;
    private final int GALLERY = 100;
    private final int CAMERA = 101;
    String encodedImage = "";
    String ph,type, latitude, longitude;

    //shared prefrences
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String phone = "phoneKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SugarContext.init(this);

        inputValidation = new InputValidation(this);

        //shared prefrences
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("TYPE");
            if (type.equals("Register")){
                latitude = intent.getStringExtra("Latitude");
                longitude = intent.getStringExtra("Longitude");
            }
        }


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (type.equals(AppController.profileType)){
                getSupportActionBar().setTitle("Edit");
            }else {
                getSupportActionBar().setTitle("Register");
            }

        }

        c_name = findViewById(R.id.c_name);
        c_name_layout = findViewById(R.id.c_name_layout);
        c_address = findViewById(R.id.c_address);
        c_address_layout = findViewById(R.id.c_address_layout);
        name_of_person = findViewById(R.id.name_of_person);
        name_of_person_layout = findViewById(R.id.name_of_person_layout);
        password = findViewById(R.id.password);
        password_layout = findViewById(R.id.password_layout);
        c_number = findViewById(R.id.c_number);
        c_number_layout = findViewById(R.id.c_number_layout);
        login_number = findViewById(R.id.login_number);
        login_number_layout = findViewById(R.id.login_number_layout);
        persons = findViewById(R.id.persons);
        persons_layout = findViewById(R.id.persons_layout);
        website = findViewById(R.id.website);
        website_layout = findViewById(R.id.website_layout);
        email = findViewById(R.id.email);
        email_layout = findViewById(R.id.email_layout);
        image = findViewById(R.id.image);
        register = findViewById(R.id.register);
        contact_list = findViewById(R.id.contactList);
        sp_country = findViewById(R.id.sp_country);
        sp_city = findViewById(R.id.sp_city);
        sp_sub_city = findViewById(R.id.sp_sub_city);

        register.setOnClickListener(this);
        image.setOnClickListener(this);
        contact_list.setOnClickListener(this);

        // Spinner click listener
        sp_country.setOnItemSelectedListener(this);
        sp_city.setOnItemSelectedListener(this);
        sp_sub_city.setOnItemSelectedListener(this);

        getAreaName();

        if(sharedPreferences.contains(phone)){
            ph = sharedPreferences.getString(phone,"");
            login_number.setText(ph);
        }

        if (type.equals(AppController.profileType)){
            password_layout.setVisibility(View.GONE);
            getUserProfile();
            register.setText(R.string.update);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item

        switch (parent.getId()){
            case R.id.sp_country:
                AreaName areaName = (AreaName) parent.getItemAtPosition(position);
                Log.e("SSS",areaName.getAreaID());
                List<AreaName> da = AreaName.find(AreaName.class, NamingHelper.toSQLNameDefault("AreaInID ")+" = ?", areaName.getAreaID());
                SpinnerAdapter adapter = new SpinnerAdapter(RegisterActivity.this,da);
                sp_city.setAdapter(adapter);
                break;
            case R.id.sp_city:
                AreaName areaNam = (AreaName) parent.getItemAtPosition(position);
                List<AreaName> da1 = AreaName.find(AreaName.class, NamingHelper.toSQLNameDefault("AreaInID ")+" = ?", areaNam.getAreaID());
                SpinnerAdapter adapte = new SpinnerAdapter(RegisterActivity.this,da1);
                sp_sub_city.setAdapter(adapte);

                break;
            case R.id.sp_sub_city:

                break;
        }

        String i = parent.getItemAtPosition(position).toString();

    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                AreaName cu = (AreaName) sp_country.getSelectedItem();
                final String country = cu.getAreaName();
                AreaName cu1 = (AreaName) sp_city.getSelectedItem();
                final String city = cu1.getAreaName();
                AreaName cu2 = (AreaName) sp_sub_city.getSelectedItem();
                final String subCity = cu2.getAreaName();

                if (!inputValidation.isInputEditTextFilled(c_name, c_name_layout, getString(R.string.error_message_c_name))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(c_address, c_address_layout, getString(R.string.error_message_c_address))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(name_of_person, name_of_person_layout, getString(R.string.error_message_name_of_person))) {
                    return;
                }

//                if (!inputValidation.isInputEditTextFilled(country, country_layout, getString(R.string.error_message_country))) {
//                    return;
//                }
                if (!inputValidation.isInputEditTextFilled(password, password_layout, getString(R.string.error_message_password))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(c_number, c_number_layout, getString(R.string.error_message_c_number))) {
                    return;
                }
//                if (!inputValidation.isInputEditTextFilled(login_number, login_number_layout, getString(R.string.error_message_login_number))) {
//                    return;
//                }
//                if (!inputValidation.isInputEditTextFilled(city, city_layout, getString(R.string.error_message_city))) {
//                    return;
//                }
//                if (!inputValidation.isInputEditTextFilled(sub_city, sub_city_layout, getString(R.string.error_message_sub_city))) {
//                    return;
//                }
                if (!inputValidation.isInputEditTextFilled(persons, persons_layout, getString(R.string.error_message_persons))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(website, website_layout, getString(R.string.error_message_website))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(email, email_layout, getString(R.string.error_message_email))) {
                    return;
                }
                if (encodedImage.isEmpty() || encodedImage.equals("")){
                    Toast.makeText(RegisterActivity.this,"Select Company Logo",Toast.LENGTH_LONG).show();
                }
                else {

                    if (type.equals(AppController.profileType)){

                    }else{
                        String tag_json_obj = "json_obj_req";
                        String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/Register.php";

                        pDialog = new ProgressDialog(RegisterActivity.this);
                        pDialog.setMessage("Searching...");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    JSONObject jsonObj = null;
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            jsonObj= new JSONObject(response);
                                            String success = jsonObj.getString("success");

                                            if (success.equals("1")){
                                                pDialog.dismiss();
                                                Log.e("Response",response);
                                                Toast.makeText(RegisterActivity.this, "User Register", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }else if (success.equals("0")){
                                                pDialog.dismiss();
                                                String message = jsonObj.getString("message");
                                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() {

                                Map<String, String> params = new HashMap<String, String>();
                                params.put("NetCode", "0");
                                params.put("SysCode", "0");
                                params.put("CompanyName", c_name.getText().toString());
                                params.put("CompanyAddress", c_address.getText().toString());
                                params.put("CompanyNumber", c_number.getText().toString());
                                params.put("NameOfPerson", name_of_person.getText().toString());
                                params.put("LoginMobileNo", login_number.getText().toString());
                                params.put("Email", email.getText().toString());
                                params.put("Country", country);
                                params.put("Password", password.getText().toString());
                                params.put("City", city);
                                params.put("SubCity", subCity);
                                params.put("Website", website.getText().toString());
                                params.put("CapacityOfPersons", persons.getText().toString());
                                params.put("CompanyLogo", encodedImage);
                                params.put("DisplayImage", encodedImage);
                                params.put("Lat", latitude);
                                params.put("Lng", longitude);
                                return params;
                            }
                        };
                        int socketTimeout = 30000;//30 seconds
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        jsonObjectRequest.setRetryPolicy(policy);
                        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
                    }
                }
                break;
            case R.id.image:
                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Add Photo!");

                builder.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (options[item].equals("Take Photo")) {

                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, CAMERA);

                        }else if (options[item].equals("Choose from Gallery")){

                            Intent in = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(in, GALLERY);

                        }
                        else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.contactList:
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            Uri contactData = data.getData();
            Cursor c =  getContentResolver().query(contactData, null, null, null, null);
            if (c.moveToFirst()) {

                String phoneNumber="",emailAddress="";
                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                //http://stackoverflow.com/questions/866769/how-to-call-android-contacts-list   our upvoted answer

                String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if ( hasPhone.equalsIgnoreCase("1"))
                    hasPhone = "true";
                else
                    hasPhone = "false" ;

                if (Boolean.parseBoolean(hasPhone))
                {
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                    while (phones.moveToNext())
                    {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phones.close();
                }

                // Find Email Addresses
                Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);
                while (emails.moveToNext())
                {
                    emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                }
                emails.close();

                c_number.setText(phoneNumber);
            }
            c.close();
        }else if (resultCode == Activity.RESULT_OK && requestCode == GALLERY){
            if (data != null) {
                getImage(data, requestCode);
            }
        }else if (resultCode == Activity.RESULT_OK && requestCode == CAMERA){
            if (data != null){
                getImage(data, requestCode);
            }
        }
    }

    //Get images from gallery and camera
    public void getImage(Intent data, int code){
        Uri contentURI;
        Bundle bundle;
        Bitmap bit = null;
        if (code == GALLERY){
            contentURI = data.getData();
            try {
                bit = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (code == CAMERA){
            bundle = data.getExtras();
            bit = (Bitmap) bundle.get("data");
        }
        if (bit != null) {
            image.setImageBitmap(bit);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    public void getAreaName(){

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetAreaName.php";

        if (isConnected()) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String success = response.getString("success");

                                if (success.equals("1")) {
                                    JSONArray jsonArray = response.getJSONArray("AreaName");
                                    Log.e("SSSS",jsonArray.toString());
                                    AreaName.deleteAll(AreaName.class);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String areaID = jsonObject.getString("AreaID");
                                        String areaInID = jsonObject.getString("AreaInID");
                                        String areaName = jsonObject.getString("AreaName");

                                        AreaName area = new AreaName(areaID, areaInID, areaName);
                                        area.save();
                                    }

                                    List<AreaName> da = AreaName.find(AreaName.class, NamingHelper.toSQLNameDefault("AreaInID ")+" = ?", "0");
                                    SpinnerAdapter adapter = new SpinnerAdapter(RegisterActivity.this,da);
                                    sp_country.setAdapter(adapter);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error", error.toString());
//                    Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            });

            int socketTimeout = 10000;//10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }else {
            Toast.makeText(RegisterActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
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

    public void getUserProfile(){
        // Tag used to cancel the request
        final String tag_json_obj = "json_obj_req";
        String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetUserProfile.php";

        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("Searching...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Profile",response);
                        try {
                            JSONObject json = new JSONObject(response);
                            String success = json.getString("success");
                            Log.e("Profile",success);

                            if (success.equals("1")) {
                                JSONArray jsonArray = json.getJSONArray("UserProfile");
                                Log.e("Profile",jsonArray.toString());
                                AreaName.deleteAll(AreaName.class);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String cId = jsonObject.getString("ClientID");
                                    String cName = jsonObject.getString("CompanyName");
                                    String cAddress = jsonObject.getString("CompanyAddress");
                                    String cNumber = jsonObject.getString("CompanyNumber");
                                    String pName = jsonObject.getString("NameOfPerson");
                                    String cMail = jsonObject.getString("Email");
                                    String cCountry = jsonObject.getString("Country");
                                    String cCity = jsonObject.getString("City");
                                    String cSubCity = jsonObject.getString("SubCity");
                                    String cWebsite = jsonObject.getString("WebSite");
                                    String cPersons = jsonObject.getString("CapacityOfPersons");

                                    Picasso.get()
                                            .load(AppController.imageUrl+cId+"/logo.JPG")
                                            .placeholder(R.drawable.default_avatar)
                                            .into(image);

                                    c_name.setText(cName);
                                    c_address.setText(cAddress);
                                    c_number.setText(cNumber);
                                    name_of_person.setText(pName);
                                    email.setText(cMail);
                                    website.setText(cWebsite);
                                    persons.setText(cPersons);

                                    pDialog.dismiss();
                                }
                            }else {
                                pDialog.dismiss();
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
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("Phone",ph);
                params.put("LoginMobileNo", ph);
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }
}
