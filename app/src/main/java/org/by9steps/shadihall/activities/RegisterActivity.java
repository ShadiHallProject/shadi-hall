package org.by9steps.shadihall.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    InputValidation inputValidation;

    TextInputLayout c_name_layout;
    TextInputEditText c_name;
    TextInputLayout c_address_layout;
    TextInputEditText c_address;
    TextInputLayout name_of_person_layout;
    TextInputEditText name_of_person;
    TextInputLayout country_layout;
    TextInputEditText country;
    TextInputLayout password_layout;
    TextInputEditText password;
    TextInputLayout c_number_layout;
    TextInputEditText c_number;
    TextInputLayout login_number_layout;
    TextInputEditText login_number;
    TextInputLayout city_layout;
    TextInputEditText city;
    TextInputLayout sub_city_layout;
    TextInputEditText sub_city;
    TextInputLayout persons_layout;
    TextInputEditText persons;
    TextInputLayout website_layout;
    TextInputEditText website;
    TextInputLayout email_layout;
    TextInputEditText email;
    CircleImageView image;
    Button register;
    ImageView contact_list;

    ProgressDialog pDialog;
    private final int GALLERY = 100;
    private final int CAMERA = 101;
    String encodedImage = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Register");
        }

        inputValidation = new InputValidation(this);

        c_name = findViewById(R.id.c_name);
        c_name_layout = findViewById(R.id.c_name_layout);
        c_address = findViewById(R.id.c_address);
        c_address_layout = findViewById(R.id.c_address_layout);
        name_of_person = findViewById(R.id.name_of_person);
        name_of_person_layout = findViewById(R.id.name_of_person_layout);
        country = findViewById(R.id.country);
        country_layout = findViewById(R.id.country_layout);
        password = findViewById(R.id.password);
        password_layout = findViewById(R.id.password_layout);
        c_number = findViewById(R.id.c_number);
        c_number_layout = findViewById(R.id.c_number_layout);
        login_number = findViewById(R.id.login_number);
        login_number_layout = findViewById(R.id.login_number_layout);
        city = findViewById(R.id.city);
        city_layout = findViewById(R.id.city_layout);
        sub_city = findViewById(R.id.sub_city);
        sub_city_layout = findViewById(R.id.sub_city_layout);
        persons = findViewById(R.id.persons);
        persons_layout = findViewById(R.id.persons_layout);
        website = findViewById(R.id.website);
        website_layout = findViewById(R.id.website_layout);
        email = findViewById(R.id.email);
        email_layout = findViewById(R.id.email_layout);
        image = findViewById(R.id.image);
        register = findViewById(R.id.register);
        contact_list = findViewById(R.id.contactList);

        register.setOnClickListener(this);
        image.setOnClickListener(this);
        contact_list.setOnClickListener(this);


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
                if (!inputValidation.isInputEditTextFilled(c_name, c_name_layout, getString(R.string.error_message_c_name))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(c_address, c_address_layout, getString(R.string.error_message_c_address))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(name_of_person, name_of_person_layout, getString(R.string.error_message_name_of_person))) {
                    return;
                }

                if (!inputValidation.isInputEditTextFilled(country, country_layout, getString(R.string.error_message_country))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(password, password_layout, getString(R.string.error_message_password))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(c_number, c_number_layout, getString(R.string.error_message_c_number))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(login_number, login_number_layout, getString(R.string.error_message_login_number))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(city, city_layout, getString(R.string.error_message_city))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(sub_city, sub_city_layout, getString(R.string.error_message_sub_city))) {
                    return;
                }
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

                    String tag_json_obj = "json_obj_req";
                    String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/Register.php";

                    pDialog = new ProgressDialog(RegisterActivity.this);
                    pDialog.setMessage("Searching...");
                    pDialog.setCancelable(false);
                    pDialog.show();
                        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        pDialog.dismiss();
                                        Log.e("Response",response);
                                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                                        finish();
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
                                params.put("Country", country.getText().toString());
                                params.put("Password", password.getText().toString());
                                params.put("City", city.getText().toString());
                                params.put("SubCity", sub_city.getText().toString());
                                params.put("Website", website.getText().toString());
                                params.put("CapacityOfPersons", persons.getText().toString());
                                params.put("CompanyLogo", encodedImage);
                                params.put("DisplayImage", encodedImage);
                                return params;
                            }
                        };
                        int socketTimeout = 30000;//30 seconds - change to what you want
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        jsonObjectRequest.setRetryPolicy(policy);
                        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
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
}
