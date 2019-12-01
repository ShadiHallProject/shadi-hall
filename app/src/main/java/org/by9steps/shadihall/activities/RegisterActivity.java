package org.by9steps.shadihall.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fxn.pix.Pix;
import com.orm.SugarContext;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.GalleryAdapter;
import org.by9steps.shadihall.adapters.ProjectsListAdapter;
import org.by9steps.shadihall.callingapi.ImageUploadingApi;
import org.by9steps.shadihall.chartofaccountdialog.ProjectMenuDialog;
import org.by9steps.shadihall.helper.ApiRefStrings;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.GenericConstants;
import org.by9steps.shadihall.helper.InputValidation;
import org.by9steps.shadihall.helper.MNotificationClass;
import org.by9steps.shadihall.helper.Prefrence;
import org.by9steps.shadihall.model.Projects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    final int REQUEST_MAP_FOR_CITY = 22;
    int IMAGE_Code = 0;
    LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    ///////////////////////////Counter For Breaking Asycn Task
    int j = 0;
    int count, total;
    private int RequestCode = 102;
    private RecyclerView recyclerView;
    GalleryAdapter galleryAdapter;
    TextView projectstatus;
    //////////////////////////Current SelectedImage From Image group
    // private ImageView currentSelectImage;
    private int posi;
    ArrayList<String> imageList;
    InputValidation inputValidation;

    TextInputLayout c_name_layout;
    TextInputEditText c_name;
    TextInputLayout c_address_layout;
    TextInputEditText c_address;
    TextInputLayout name_of_person_layout;
    TextInputEditText name_of_person;
    TextInputLayout password_layout;
    TextInputEditText password;
    TextInputLayout c_number_layout;
    TextInputEditText c_number;
    TextInputLayout login_number_layout;
    TextView login_number, selectImages, projectnamed;
    //    TextInputLayout country_layout;
//    TextView country;
    TextInputLayout financial_year_layout;
    Button financial_year;
    TextInputLayout description_layout;
    TextInputEditText description;
    //    TextInputLayout persons_layout;
//    TextInputEditText persons;
    TextInputLayout website_layout;
    TextInputEditText website;
    TextInputLayout email_layout;
    TextInputEditText email;
    CircleImageView image, projimage;
    Button register;
    ImageView contact_list, iclocationlogo, img1, img2, img3, img4, img5;
    ///////////////////////Hash map for images and ites uri
    HashMap<Integer, Uri> imageshashmap;
    /////////////TAG if img set tag=set else tag=null
    String TAGSET = "set", TAGNULL = "null";
//    Spinner sp_country;
//    Spinner sp_city;
//    Spinner sp_sub_city;

    ProgressDialog pDialog;
    private final int GALLERY = 100;
    private final int CAMERA = 101;
    private final int RefImage_CAMERA = 105;
    // private final int RefImage_GALLERY = 106;
    String encodedImage = "";
    String ph, type, latitude, longitude;
    String country = "", city = "", subCity = "";
    int choosenYear = 2019;

    //shared prefrences
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String phone = "phoneKey";
    public static final String resume = "resume";
    Prefrence prefrence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SugarContext.init(this);
        img1 = findViewById(R.id.img1);
        img1.setOnClickListener(this);
        img2 = findViewById(R.id.img2);
        img2.setOnClickListener(this);
        img3 = findViewById(R.id.img3);
        img3.setOnClickListener(this);
        img4 = findViewById(R.id.img4);
        img4.setOnClickListener(this);
        img5 = findViewById(R.id.img5);
        img5.setOnClickListener(this);
        imageshashmap = new HashMap<>();
        /////////////////////////////Setting initial tag to nnull
        img1.setTag(TAGNULL);
        img2.setTag(TAGNULL);
        img3.setTag(TAGNULL);
        img4.setTag(TAGNULL);
        img5.setTag(TAGNULL);
        iclocationlogo = findViewById(R.id.locationimage);
        projectnamed = findViewById(R.id.projectname);
        recyclerView = findViewById(R.id.gv);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        selectImages = findViewById(R.id.selectImages);
        projectstatus = findViewById(R.id.projectstatus);
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
//        persons = findViewById(R.id.persons);
//        persons_layout = findViewById(R.id.persons_layout);
        projimage = findViewById(R.id.projectimage);
        website = findViewById(R.id.website);
        website_layout = findViewById(R.id.website_layout);
        email = findViewById(R.id.email);
        email_layout = findViewById(R.id.email_layout);
        image = findViewById(R.id.image);
        register = findViewById(R.id.register);
        contact_list = findViewById(R.id.contactList);
        financial_year_layout = findViewById(R.id.financial_year_layout);
        financial_year = findViewById(R.id.financial_year);
        description_layout = findViewById(R.id.description_layout);
        description = findViewById(R.id.description);
//        sub_city_layout = findViewById(R.id.sub_city_name_layout);
//        sub_city = findViewById(R.id.sub_city_name);
//        sp_country = findViewById(R.id.sp_country);
//        sp_city = findViewById(R.id.sp_city);
//        sp_sub_city = findViewById(R.id.sp_sub_city);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        inputValidation = new InputValidation(this);

        //shared prefrences
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        prefrence = new Prefrence(this);
////////////////////////////////////////////For
        selectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pix.start(RegisterActivity.this,                    //Activity or Fragment Instance
                        RequestCode,                //Request code for activity results
                        5);    //Number of images to restict selection count
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("TYPE");
            if (type.equals("Register")) {
//                latitude = intent.getStringExtra("Latitude");
//                longitude = intent.getStringExtra("Longitude");
                String url = ApiRefStrings.ServerAddress + "ProjectImages/ProjectsLogo/" + prefrence.getProjectIDSession() + ".png";
                Log.e("URLOfProj", url);
                Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.default_avatar)
                        .into(projimage);
                String query = "SELECT * FROM Project where ProjectID=" + prefrence.getProjectIDSession();
                List<Projects> projectsList = new DatabaseHelper(this).getProjects(query);
                String projname = "";
                if (projectsList != null) {
                    projname = projectsList.get(0).getProjectName();
                    projectnamed.setText(projname);
                }
                projectstatus.setText("Click To Set Location");
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
//                try {
////                    addresses = geocoder.getFromLocation(Double.valueOf(latitude), Double.valueOf(longitude), 1);
////                    city = addresses.get(0).getSubLocality();
////                    country = addresses.get(0).getCountryName();
////                    subCity = addresses.get(0).getLocality();
//
////                    country.setText("Country:  "+countryName);
////                    city.setText("City:  "+cityName);
////                    sub_city.setText("Sub City:  "+subCity);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            } else if (type.equals("Edit")) {
                MNotificationClass.ShowToastTem(this, "Edit Mode");
                String url = ApiRefStrings.ServerAddress + "ProjectImages/ProjectsLogo/" + prefrence.getProjectIDSession() + ".png";
                Log.e("URLOfProj", url);
                Picasso.get()
                        .load(url)
                        .placeholder(R.drawable.default_avatar)
                        .into(projimage);
                String query = "SELECT * FROM Project where ProjectID=" + prefrence.getProjectIDSession();
                List<Projects> projectsList = new DatabaseHelper(this).getProjects(query);
                String projname = "";
                if (projectsList != null) {
                    projname = projectsList.get(0).getProjectName();
                    projectnamed.setText(projname);
                }
            }
        }


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (type.equals(AppController.profileType)) {
                getSupportActionBar().setTitle("Edit");
            } else {
                getSupportActionBar().setTitle("Register");
            }

        }

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        choosenYear = Integer.valueOf(df.format(date));
        financial_year.setText(String.valueOf(choosenYear));

        register.setOnClickListener(this);
        image.setOnClickListener(this);
        contact_list.setOnClickListener(this);
        financial_year.setOnClickListener(this);

        // Spinner click listener
//        sp_country.setOnItemSelectedListener(this);
//        sp_city.setOnItemSelectedListener(this);
//        sp_sub_city.setOnItemSelectedListener(this);

//        getAreaName();

        if (sharedPreferences.contains(phone)) {
            ph = sharedPreferences.getString(phone, "");
            login_number.setText(ph);
        }

        if (type.equals(AppController.profileType)) {
            password_layout.setVisibility(View.GONE);
            getUserProfile();
            register.setText(R.string.update);
        }
///////////////////////////////////////////////Requesting For map To city name
        projectstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGpsEnabled()) {
                    Intent i = new Intent(RegisterActivity.this, MapsActivity.class);
                    startActivityForResult(i, REQUEST_MAP_FOR_CITY);
                }
            }
        });
        iclocationlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGpsEnabled()) {
                    Intent i = new Intent(RegisterActivity.this, MapsActivity.class);
                    startActivityForResult(i, REQUEST_MAP_FOR_CITY);
                }
            }
        });
        projimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProjectMenuDialog dialog = new ProjectMenuDialog();
                boolean isconnected = GenericConstants.isConnected(RegisterActivity.this);
                if (isconnected && !type.equals(AppController.profileType)) {

                    dialog.listenerproj = new ProjectsListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(String id, String name) {

                              prefrence.setProjectIDSession(id);
                              prefrence.setUserRighhtsSession("0");
                              prefrence.setClientUserIDSession("0");
                              prefrence.setMYClientUserIDSession("0");
                              prefrence.setClientIDSession("0");
                              dialog.dismiss();
                              String url = ApiRefStrings.ServerAddress + "/ProjectImages/ProjectsLogo/" + prefrence.getProjectIDSession() + ".png";
                              Log.e("URL", url);
                              Picasso.get()
                                      .load(url)
                                      .placeholder(R.drawable.default_avatar)
                                      .into(projimage);
                              String query = "SELECT * FROM Project where ProjectID=" + prefrence.getProjectIDSession();
                              List<Projects> projectsList = new DatabaseHelper(RegisterActivity.this).getProjects(query);
                              String projname = "";
                              if (projectsList != null) {
                                  projname = projectsList.get(0).getProjectName();
                                  projectnamed.setText(projname);
                              }
//                            if (isGpsEnabled()) {
//                                Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
//                                intent.putExtra("TYPE", "Register");
//
//                                startActivity(intent);
////                                Intent i=new Intent(getContext(), MapsActivity.class);
////                                i.putExtra("ClientID","98");
////                                startActivity(i);
//                            }
                              MNotificationClass.ShowToastTem(RegisterActivity.this,
                                      name);

                            // prefrence.setProjectIDSession(id);
                        }
                    };
                    dialog.show(getSupportFragmentManager(), "Show");

                    // Toast.makeText(getContext(), "Please Select Project First", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        // On selecting a spinner item
//
//        switch (parent.getId()){
//            case R.id.sp_country:
//                AreaName areaName = (AreaName) parent.getItemAtPosition(position);
//                Log.e("SSS",areaName.getAreaID());
//                List<AreaName> da = AreaName.find(AreaName.class, NamingHelper.toSQLNameDefault("AreaInID ")+" = ?", areaName.getAreaID());
//                SpinnerAdapter adapter = new SpinnerAdapter(RegisterActivity.this,da);
//                sp_city.setAdapter(adapter);
//                break;
//            case R.id.sp_city:
//                AreaName areaNam = (AreaName) parent.getItemAtPosition(position);
//                List<AreaName> da1 = AreaName.find(AreaName.class, NamingHelper.toSQLNameDefault("AreaInID ")+" = ?", areaNam.getAreaID());
//                SpinnerAdapter adapte = new SpinnerAdapter(RegisterActivity.this,da1);
//                sp_sub_city.setAdapter(adapte);
//
//                break;
//            case R.id.sp_sub_city:
//
//                break;
//        }
//
//        String i = parent.getItemAtPosition(position).toString();
//
//    }
//    @Override
//    public void onNothingSelected(AdapterView<?> arg0) {
//        // TODO Auto-generated method stub
//    }

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
//                final String contry = country.getText().toString();
//                final String cty = city.getText().toString();
//                final String subCty = sub_city.getText().toString();
                Log.e("asdfasdf", "FirstPass");
                if (city.equals("") || country.equals("") || subCity.equals("")) {

                    // uploadImages("115");
//                    pDialog = new ProgressDialog(RegisterActivity.this);
//                    pDialog.setTitle("New Image Upload");
//                    pDialog.setMessage("Uploading image");
//                    pDialog.setCancelable(false);
//                    pDialog.show();
//                    ImageUploadingApi.uploadImageToCloud(this,
//                            "1051212", "1", img5,
//                            Uri.parse(imageList.get(0)), new ImageUploadingApi.ImageUploadingImage() {
//                                @Override
//                                public void FinishCallBackmethod(String success, String funType) {
//                                    Log.e("imageurisss", success + "---" + funType);
//                                    //img5.setTag("up");
////                                    pDialog.setMessage("Sending Images To Cloud...5");
//                                    pDialog.dismiss();
//                                    Toast.makeText(RegisterActivity.this, "All Images Done",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            });
                    MNotificationClass.ShowToast(this, "Select City First");
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(c_name, c_name_layout, getString(R.string.error_message_c_name))) {
                    return;
                }
//                if (!inputValidation.isInputEditTextFilled(c_address, c_address_layout, getString(R.string.error_message_c_address))) {
//                    return;
//                }
                if (!inputValidation.isInputEditTextFilled(name_of_person, name_of_person_layout, getString(R.string.error_message_name_of_person))) {
                    return;
                }

                if (!type.equals(AppController.profileType)) {
                    if (!inputValidation.isInputEditTextFilled(password, password_layout, getString(R.string.error_message_password))) {
                        return;
                    }
                }

//                if (!inputValidation.isInputEditTextFilled(c_number, c_number_layout, getString(R.string.error_message_c_number))) {
//                    return;
//                }
                ////////////////////////////////////
//                if (!inputValidation.isInputEditTextFilled(login_number, login_number_layout, getString(R.string.error_message_login_number))) {
//                    return;
//                }
//                if (!inputValidation.isInputEditTextFilled(city, city_layout, getString(R.string.error_message_city))) {
//                    return;
//                }
//                if (!inputValidation.isInputEditTextFilled(sub_city, sub_city_layout, getString(R.string.error_message_sub_city))) {
//                    return;
//                }
//                if (!inputValidation.isInputEditTextFilled(persons, persons_layout, getString(R.string.error_message_persons))) {
//                    return;
//                }
//                if (!inputValidation.isInputEditTextFilled(website, website_layout, getString(R.string.error_message_website))) {
//                    return;
//                }
//                if (!inputValidation.isInputEditTextFilled(email, email_layout, getString(R.string.error_message_email))) {
//                    return;
//                }
//                if (!inputValidation.isInputEditTextFilled(description, description_layout, getString(R.string.error_message_description))) {
//                    return;
//                }
                if (encodedImage.isEmpty() || encodedImage.equals("") ) {
                    Toast.makeText(RegisterActivity.this, "Select Company Logo", Toast.LENGTH_LONG).show();
                    if (type.equals(AppController.profileType))
                        SendUpdatedDataToServer();
                } else {

                    if (type.equals(AppController.profileType)) {
                        SendUpdatedDataToServer();
                    } else {
                        String tag_json_obj = "json_obj_req";
                        String url = ApiRefStrings.RegisterAccountApiRef;

                        pDialog = new ProgressDialog(RegisterActivity.this);
                        pDialog.setTitle("New Account");
                        pDialog.setMessage("Creating Account...");
                        pDialog.setCancelable(false);
                        pDialog.show();

                        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    JSONObject jsonObj = null;

                                    @Override
                                    public void onResponse(String response) {
                                        Log.e("Register", response);
                                        try {
                                            jsonObj = new JSONObject(response);
                                            String success = jsonObj.getString("success");

                                            if (success.equals("1")) {
                                                // pDialog.dismiss();
                                                Log.e("Response", response);
                                                String id = jsonObj.getString("ClientID");
//                                                Toast.makeText(RegisterActivity.this, "User Register", Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(RegisterActivity.this, SelectImagesActivity.class);
//                                                intent.putExtra("ClientID",id);
//                                                startActivity(intent);
//                                                finish();
                                                uploadImages(id);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString(resume, "1");
                                                editor.apply();
                                            } else if (success.equals("0")) {
                                                pDialog.dismiss();
                                                String message = jsonObj.getString("message");
                                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                            } else {
                                                pDialog.dismiss();
                                                Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.dismiss();
                                Log.e("Error", error.toString());
                                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() {

                                Map<String, String> params = new HashMap<String, String>();
                                params.put("NetCode", "0");
                                params.put("SysCode", "0");
                                params.put("CompanyName", checkNullField(c_name.getText().toString()));
                                params.put("CompanyAddress", checkNullField(c_address.getText().toString()));
                                params.put("CompanyNumber", checkNullField(c_number.getText().toString()));
                                params.put("NameOfPerson", checkNullField(name_of_person.getText().toString()));
                                params.put("LoginMobileNo", checkNullField(login_number.getText().toString()));
                                params.put("Email", checkNullField(email.getText().toString()));
                                params.put("Country", checkNullField(country));
                                params.put("Password", checkNullField(password.getText().toString()));
                                params.put("City", checkNullField(city));
                                params.put("SubCity", checkNullField(subCity));
                                params.put("Website", checkNullField(website.getText().toString()));
                                params.put("CapacityOfPersons", "0");
                                params.put("CompanyLogo", encodedImage);
                                params.put("DisplayImage", encodedImage);
                                params.put("Lat", latitude);
                                params.put("Lng", longitude);
                                params.put("ProjectID", prefrence.getProjectIDSession());
                                params.put("BusinessDescriptions", checkNullField(description.getText().toString()));
                                params.put("FinancialYear", financial_year.getText().toString());
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
                final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

                builder.setTitle("Add Photo!");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (options[item].equals("Take Photo")) {

                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, CAMERA);

                        } else if (options[item].equals("Choose from Gallery")) {

                            Intent in = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(in, GALLERY);

                        } else if (options[item].equals("Cancel")) {
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
            case R.id.financial_year:
                MonthPickerDialog.Builder builde = new MonthPickerDialog.Builder(RegisterActivity.this, new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        financial_year.setText(Integer.toString(selectedYear));
                        choosenYear = selectedYear;
                    }
                }, choosenYear, 0);

                builde.showYearOnly()
                        .setYearRange(1990, 2050)
                        .build()
                        .show();
                break;
            case R.id.img1:
                IMAGE_Code = 1;
                requestImageFromAndroidPhone();
                break;
            case R.id.img2:
                IMAGE_Code = 2;
                requestImageFromAndroidPhone();
                break;
            case R.id.img3:
                IMAGE_Code = 3;
                requestImageFromAndroidPhone();
                break;
            case R.id.img4:
                IMAGE_Code = 4;
                requestImageFromAndroidPhone();
                break;
            case R.id.img5:
                IMAGE_Code = 5;
                requestImageFromAndroidPhone();
                break;


        }
    }

    private String checkNullField(String toString) {
        if (toString.isEmpty())
            return GenericConstants.NullFieldStandardText;
        else
            return toString;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            Uri contactData = data.getData();
            Cursor c = getContentResolver().query(contactData, null, null, null, null);
            if (c.moveToFirst()) {

                String phoneNumber = "", emailAddress = "";
                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                //http://stackoverflow.com/questions/866769/how-to-call-android-contacts-list   our upvoted answer

                String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (hasPhone.equalsIgnoreCase("1"))
                    hasPhone = "true";
                else
                    hasPhone = "false";

                if (Boolean.parseBoolean(hasPhone)) {
                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                    while (phones.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    phones.close();
                }

                // Find Email Addresses
                Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null);
                while (emails.moveToNext()) {
                    emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                }
                emails.close();

                c_number.setText(phoneNumber);
            }
            c.close();
        } else if (resultCode == Activity.RESULT_OK && requestCode == GALLERY) {
            if (data != null) {
                getImage(data, requestCode);
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == CAMERA) {
            if (data != null) {
                getImage(data, requestCode);
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == RequestCode) {
            imageList = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

            galleryAdapter = new GalleryAdapter(RegisterActivity.this, imageList);
            galleryAdapter.listeneachclicklistner = new GalleryAdapter.listeneachclicklistner() {
                @Override
                public void ImageClicked(int pos, String obj, ImageView imageView) {
                    SelectImageFrom(pos, obj, imageView, galleryAdapter, imageList);
                }

                @Override
                public void onImageDeleteClick(int pos, String obj, ImageView imageView) {
                    Log.e("deletepos", "->" + pos + " tsize:" + imageList.size());
                    imageList.remove(pos);
                    for (int i = 0; i < imageList.size(); i++)
                        Log.e("deleteposobjectsim", imageList.get(i));
                    galleryAdapter.notifyDataSetChanged();
                }
            };
            recyclerView.setAdapter(galleryAdapter);


        } else if (resultCode == Activity.RESULT_OK && requestCode == RefImage_CAMERA) {
            imageList.remove(posi);
            imageList.add(posi, data.getStringArrayListExtra(Pix.IMAGE_RESULTS).get(0));
            galleryAdapter.notifyItemChanged(posi);
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_MAP_FOR_CITY) {
            if (data.getExtras() != null) {

                country = data.getStringExtra("country");
                city = data.getStringExtra("city");
                subCity = data.getStringExtra("subcity");
                latitude = data.getStringExtra("Lat");
                longitude = data.getStringExtra("Long");
                Log.e("resultbymap", country + " " + city + " " + subCity);
                projectstatus.setText(country + " " + city + " " + subCity);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Log.e("image1uri", resultUri.toString());
                if (IMAGE_Code == 1) {
                    imageshashmap.put(1, resultUri);
                    img1.setImageURI(resultUri);
                    img1.setTag(TAGSET);

                } else if (IMAGE_Code == 2) {
                    imageshashmap.put(2, resultUri);
                    img2.setImageURI(resultUri);
                    img2.setTag(TAGSET);
                    // requestImageFromAndroidPhone();
                } else if (IMAGE_Code == 3) {
                    imageshashmap.put(3, resultUri);
                    img3.setImageURI(resultUri);
                    img3.setTag(TAGSET);
                    //requestImageFromAndroidPhone();
                } else if (IMAGE_Code == 4) {
                    imageshashmap.put(4, resultUri);
                    img4.setImageURI(resultUri);
                    img4.setTag(TAGSET);
                    //requestImageFromAndroidPhone();
                } else if (IMAGE_Code == 5) {
                    imageshashmap.put(5, resultUri);
                    img5.setImageURI(resultUri);
                    img5.setTag(TAGSET);
                    //requestImageFromAndroidPhone();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void requestImageFromAndroidPhone() {
        int rationx = 1800;
        int rationy = 1900;

        CropImage.activity()
                .setAllowFlipping(false)
                //.setRequestedSize(500, 500, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                .setMinCropResultSize(rationx, rationy)
                .setMaxCropResultSize(rationx, rationy)
                .setAllowCounterRotation(false)
                .setAllowRotation(false)
                .setAutoZoomEnabled(true)
//                        .setAspectRatio(4,3)
                .setGuidelines(CropImageView.Guidelines.OFF)
                .start(RegisterActivity.this);
    }

    //Get images from gallery and camera
    public void getImage(Intent data, int code) {
        Uri contentURI;
        Bundle bundle;
        Bitmap bit = null;
        if (code == GALLERY) {
            contentURI = data.getData();
            try {
                bit = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (code == CAMERA) {
            bundle = data.getExtras();
            bit = (Bitmap) bundle.get("data");
        }
        if (bit != null) {
            image.setImageBitmap(bit);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
//        Log.e("ENCODEDIMAGE",encodedImage);
//        byte [] encodeByte=Base64.decode(encodedImage,Base64.DEFAULT);
//        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//        image.setImageBitmap(bitmap);
    }

    //Check Internet Connection
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public void getUserProfile() {
        // Tag used to cancel the request
        final String tag_json_obj = "json_obj_req";
        String url = ApiRefStrings.ServerAddress + "PhpApi/GetUserProfile.php";

        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("Searching...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Profile", response);
                        //  pDialog.dismiss();
                        try {
                            JSONObject json = new JSONObject(response);
                            String success = json.getString("success");
                            Log.e("Profileasdf", success);

                            if (success.equals("1")) {
                                JSONArray jsonArray = json.getJSONArray("UserProfile");
                                Log.e("ProfileToSTring", "AraryLen " + jsonArray.length());

                                Log.e("ProfileToSTring", jsonArray.toString());
                                // AreaName.deleteAll(AreaName.class);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Log.e("ProfilObjtss", jsonObject.toString());

                                    //  String cId = jsonObject.getString("ClientID");
                                    String cName = jsonObject.getString("CompanyName");
                                    String cAddress = jsonObject.getString("CompanyAddress");
                                    String cNumber = jsonObject.getString("CompanyNumber");
                                    String pName = jsonObject.getString("NameOfPerson");
                                    String cMail = jsonObject.getString("Email");
                                    country = jsonObject.getString("Country");
                                    city = jsonObject.getString("City");
                                    subCity = jsonObject.getString("SubCity");
                                    String cWebsite = jsonObject.getString("Website");
                                    latitude = jsonObject.getString("Lat");
                                    longitude = jsonObject.getString("Lng");
                                    description.setText(jsonObject.getString("BusinessDescriptions"));
                                    projectstatus.setText(country + " " + city + " " + subCity);
                                    // String cPersons = jsonObject.getString("CapacityOfPersons");

//                                    Picasso.get()
//                                            .load(AppController.imageUrl + cId + "/logo.png")
//                                            .placeholder(R.drawable.default_avatar)
//                                            .into(image);
                                    setImagesToImageViewFromSErver();
                                    c_name.setText(cName);
                                    c_address.setText(cAddress);
                                    c_number.setText(cNumber);
                                    name_of_person.setText(pName);
                                    email.setText(cMail);
                                    website.setText(cWebsite);
                                    //persons.setText(cPersons);

                                    pDialog.dismiss();
                                }
                            } else {
                                pDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("except", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.e("Error", error.toString());
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("PhoneNoOfClient", ph + " ClientDI:" + prefrence.getClientIDSession());
                params.put("ClientID", prefrence.getClientIDSession() + "");
                return params;
            }
        };
        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void setImagesToImageViewFromSErver() {
        String uriForLogo=AppController.imageUrl + prefrence.getClientIDSession() + "/logo.png";
        Log.e("asdfasddfasdf",uriForLogo);

        Picasso.get().load(AppController.imageUrl + prefrence.getClientIDSession() + "/logo.png")
                .placeholder(R.drawable.default_avatar).networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(image);
       // encodedImage="Null";
       // createEncodeIamgeStramTosendServer();

        try {
//            img1.setTag(TAGSET);
//            img2.setTag(TAGSET);
//            img3.setTag(TAGSET);
//            img4.setTag(TAGSET);
//            img5.setTag(TAGSET);
            /////////////////////////Loading Slider Images
            Picasso.get().load(AppController.imageUrl + prefrence.getClientIDSession() + "/SliderImages/image1.png")
                    .placeholder(R.drawable.default_avatar)
                    .into(img1, new Callback() {
                        @Override
                        public void onSuccess() {
                            img1.setTag(TAGNULL);
                        }

                        @Override
                        public void onError(Exception e) {
                            img1.setTag(TAGNULL);
                            // img5.setVisibility(IN);
                        }
                    });
            /////////////////////////Loading Slider Images
            Picasso.get().load(AppController.imageUrl + prefrence.getClientIDSession() + "/SliderImages/image2.png")
                    .placeholder(R.drawable.default_avatar)
                    .into(img2, new Callback() {
                        @Override
                        public void onSuccess() {
                            img2.setTag(TAGNULL);
                        }

                        @Override
                        public void onError(Exception e) {
                            img2.setTag(TAGNULL);
                            // img5.setVisibility(IN);
                        }
                    });
            /////////////////////////Loading Slider Images
            Picasso.get().load(AppController.imageUrl + prefrence.getClientIDSession() + "/SliderImages/image3.png")
                    .placeholder(R.drawable.default_avatar)
                    .into(img3, new Callback() {
                        @Override
                        public void onSuccess() {
                            img3.setTag(TAGNULL);
                        }

                        @Override
                        public void onError(Exception e) {
                            img3.setTag(TAGNULL);
                            // img5.setVisibility(IN);
                        }
                    });
            /////////////////////////Loading Slider Images
            Picasso.get().load(AppController.imageUrl + prefrence.getClientIDSession() + "/SliderImages/image4.png")
                    .placeholder(R.drawable.default_avatar)
                    .into(img4, new Callback() {
                        @Override
                        public void onSuccess() {
                            img4.setTag(TAGNULL);
                        }

                        @Override
                        public void onError(Exception e) {
                            img4.setTag(TAGNULL);
                            // img5.setVisibility(IN);
                        }
                    });

            /////////////////////////Loading Slider Images
            Picasso.get().load(AppController.imageUrl + prefrence.getClientIDSession() + "/SliderImages/image5.png")
                    .placeholder(R.drawable.default_avatar)
                    .into(img5, new Callback() {
                        @Override
                        public void onSuccess() {
                            img5.setTag(TAGNULL);
                        }

                        @Override
                        public void onError(Exception e) {
                            img5.setTag(TAGNULL);
                            // img5.setVisibility(IN);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    ///////////////////////////////////////////////Click Listener For Each Image
    public void SelectImageFrom(int pos, String src, ImageView imageView, GalleryAdapter adapter, ArrayList<String> imageList) {
        //currentSelectImage=imageView;
        posi = pos;
        final CharSequence[] options = {"Take Photo", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

        builder.setTitle("Reselect Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    Pix.start(RegisterActivity.this,                    //Activity or Fragment Instance
                            RefImage_CAMERA,                //Request code for activity results
                            1);


                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private Boolean isGpsEnabled() {
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(R.string.gps_network_not_enabled);
            alertDialogBuilder.setPositiveButton(R.string.open_location_settings,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });

            alertDialogBuilder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return gps_enabled;
    }

    public void SendUpdatedDataToServer() {
        Log.e("asdfasdf", "ClickedUpdateDAta");
        String tag_json_obj = "json_obj_req";
        String url = ApiRefStrings.UpdateClientDataINClientTAble;

        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setTitle("Updating Account");
        pDialog.setMessage("Updating Account...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    JSONObject jsonObj = null;

                    @Override
                    public void onResponse(String response) {
                        Log.e("Register", response);
                        try {
                            jsonObj = new JSONObject(response);
                            String success = jsonObj.getString("success");

                            if (success.equals("1")) {
                                // pDialog.dismiss();
                                Log.e("Response", response);
                                String id = jsonObj.getString("ClientID");
//                                                Toast.makeText(RegisterActivity.this, "User Register", Toast.LENGTH_SHORT).show();
//                                                Intent intent = new Intent(RegisterActivity.this, SelectImagesActivity.class);
//                                                intent.putExtra("ClientID",id);
//                                                startActivity(intent);
//                                                finish();
                                pDialog.show();
                                 uploadImages(id);
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                editor.putString(resume, "1");
//                                editor.apply();
                               // pDialog.dismiss();
                            } else if (success.equals("0")) {
                                pDialog.dismiss();
                                String message = jsonObj.getString("message");
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                pDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.e("Error", error.toString());
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                Log.e("asdfasdfCie", prefrence.getClientIDSession() + "");
                Log.e("asdfasdfCie", encodedImage+ "()()()");
                params.put("CompanyName", checkNullField(c_name.getText().toString()));
                params.put("CompanyAddress", checkNullField(c_address.getText().toString()));
                params.put("CompanyNumber", checkNullField(c_number.getText().toString()));
                params.put("NameOfPerson", checkNullField(name_of_person.getText().toString()));
                // params.put("LoginMobileNo", checkNullField(login_number.getText().toString()));
                params.put("Email", checkNullField(email.getText().toString()));
                params.put("Country", checkNullField(country));
                params.put("ClientID", prefrence.getClientIDSession());
                //  params.put("Password", checkNullField(password.getText().toString()));
                params.put("City", checkNullField(city));
                params.put("SubCity", checkNullField(subCity));
                params.put("Website", checkNullField(website.getText().toString()));
                params.put("CapacityOfPersons", "0");
                // params.put("CompaDisplayImagenyLogo", encodedImage);
              //  params.put("", encodedImage);
               // createEncodeIamgeStramTosendServer();
                if (encodedImage.isEmpty() || encodedImage.equals("") ){
                   // params.put("CompanyLogo", "Null");
                } else {

                    params.put("CompanyLogo", encodedImage);
                }
                params.put("Lat", latitude);
                params.put("Lng", longitude);
                params.put("ProjectID", prefrence.getProjectIDSession());
                params.put("BusinessDescriptions", checkNullField(description.getText().toString()));
                params.put("FinancialYear", financial_year.getText().toString());
                return params;
            }
        };
        int socketTimeout = 30000;//30 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    private void createEncodeIamgeStramTosendServer() {
        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache(true);
        Bitmap bit = image.getDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    ///////////////////////////////////////////////All Fun Related To Image Uploading
    private void uploadImages(final String clientID) {

        pDialog.setMessage("Sending Images To Cloud...0");
        Log.e("flageimage1", "inimg1");
        // IMAGE_Code++;
        if (img1.getTag().equals(TAGSET)) {
            ImageUploadingApi.uploadImageToCloud(this,
                    clientID, "1", img1, imageshashmap.get(1), new ImageUploadingApi.ImageUploadingImage() {
                        @Override
                        public void FinishCallBackmethod(String success, String funType) {
                            Log.e("upimagesst1", success + "--" + funType);
                            pDialog.setMessage("Sending Images To Cloud...1");
                            img1.setTag("up");
                            SendImage2ToColud(clientID);
                        }
                    });
        } else SendImage2ToColud(clientID);
//        count = total = 0;
//        int size = 0;
//        if (imageList != null && imageList.size() > 0) {
//            size = imageList.size();
//            pDialog = new ProgressDialog(RegisterActivity.this);
//            pDialog.setMessage("Searching...");
//            pDialog.setCancelable(false);
//            pDialog.show();
//        } else {
//            MNotificationClass.ShowToast(this, "No Image ");
//        }
//        total = size;
//        for (int i = 0; i < size; i++) {
//            j++;
//            File f = new File(imageList.get(i));
////            imageView.setDrawingCacheEnabled(true);
////            imageView.buildDrawingCache();
////            Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
//            Bitmap bitmap = new BitmapDrawable(RegisterActivity.this.getResources(), f.getAbsolutePath()).getBitmap();
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//            final String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
//
//            String tag_json_obj = "json_obj_req";
//            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/UploadImages.php";
//
//
//            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
//                    new Response.Listener<String>() {
//                        JSONObject jsonObj = null;
//
//                        @Override
//                        public void onResponse(String response) {
//                            count++;
//                            try {
//                                Log.e("IMAGES", response);
//                                jsonObj = new JSONObject(response);
//                                String success = jsonObj.getString("success");
//                                pDialog.setMessage("Uploading Image..." + count);
//                                if (success.equals("1")) {
//                                    if (count >= total) {
//                                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                                        editor.putString(resume, "1");
//                                        editor.apply();
//                                        Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
//                                        finish();
//                                        pDialog.dismiss();
//                                    }
//                                } else {
//                                    String message = jsonObj.getString("message");
//                                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
//                                    pDialog.dismiss();
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    pDialog.dismiss();
//                    Log.e("Error", error.toString());
//                    Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("DisplayImage", encodedImage);
//                    params.put("Name", "image" + String.valueOf(j) + ".png");
//                    params.put("ClientID", clientID);
//                    return params;
//                }
//            };
//            int socketTimeout = 30000;//30 seconds
//            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//            jsonObjectRequest.setRetryPolicy(policy);
//            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
//        }
    }

    private void SendImage2ToColud(final String clientID) {
        Log.e("flageimage2", "inimg2");
        if (img2.getTag().equals(TAGSET)) {
            ImageUploadingApi.uploadImageToCloud(this,
                    clientID, "2", img2, imageshashmap.get(2), new ImageUploadingApi.ImageUploadingImage() {
                        @Override
                        public void FinishCallBackmethod(String success, String funType) {
                            Log.e("upimagesst2", success + "--" + funType);
                            pDialog.setMessage("Sending Images To Cloud...2");
                            img2.setTag("up");
                            SendImage3ToColud(clientID);

                        }
                    });
        } else SendImage3ToColud(clientID);
    }

    private void SendImage3ToColud(final String clientID) {
        Log.e("flageimage3", "inimg3");
        if (img3.getTag().equals(TAGSET)) {
            ImageUploadingApi.uploadImageToCloud(this,
                    clientID, "3", img3, imageshashmap.get(3), new ImageUploadingApi.ImageUploadingImage() {
                        @Override
                        public void FinishCallBackmethod(String success, String funType) {
                            Log.e("upimagesst3", success + "--" + funType);
                            pDialog.setMessage("Sending Images To Cloud...3");
                            img3.setTag("up");
                            SendImage4ToColud(clientID);
                        }
                    });
        } else SendImage4ToColud(clientID);
    }

    private void SendImage4ToColud(final String clientID) {
        Log.e("flageimage4", "inimg4");
        if (img4.getTag().equals(TAGSET)) {
            ImageUploadingApi.uploadImageToCloud(this,
                    clientID, "4", img4, imageshashmap.get(4), new ImageUploadingApi.ImageUploadingImage() {
                        @Override
                        public void FinishCallBackmethod(String success, String funType) {
                            Log.e("upimagesst4", success + "--" + funType);
                            pDialog.setMessage("Sending Images To Cloud...4");
                            img4.setTag("up");
                            SendImage5ToColud(clientID);
                        }
                    });
        } else SendImage5ToColud(clientID);
    }

    private void SendImage5ToColud(final String clientID) {
        Log.e("flageimage5", "inimg5");
        if (img5.getTag().equals(TAGSET)) {
            ImageUploadingApi.uploadImageToCloud(this,
                    clientID, "5", img5, imageshashmap.get(5), new ImageUploadingApi.ImageUploadingImage() {
                        @Override
                        public void FinishCallBackmethod(String success, String funType) {
                            img5.setTag("up");
                            pDialog.setMessage("Sending Images To Cloud...5");
                            pDialog.dismiss();
                            finish();
                            Toast.makeText(RegisterActivity.this, "All Images Done", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            pDialog.dismiss();
            finish();
        }
    }
}
