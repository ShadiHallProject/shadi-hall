package org.by9steps.shadihall.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.adapters.GalleryAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectImagesActivity extends AppCompatActivity {

    private int RequestCode = 100;

    private GridView gvGallery;
    TextView selectImages;
    Button register;
    ProgressDialog pDialog;

    ArrayList<String> imageList;
    int j = 0;
    String clientID;

    //shared prefrences
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String resume = "resume";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_images);

        gvGallery = findViewById(R.id.gv);
        selectImages = findViewById(R.id.selectImages);
        register = findViewById(R.id.register);

        //shared prefrences
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        Intent intent = getIntent();
        if (intent != null){
            clientID = intent.getStringExtra("ClientID");
        }

        selectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pix.start(SelectImagesActivity.this,                    //Activity or Fragment Instance
                        RequestCode,                //Request code for activity results
                        5);    //Number of images to restict selection count
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImages();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == RequestCode) {
            imageList = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

//            GalleryAdapter galleryAdapter = new GalleryAdapter(SelectImagesActivity.this, imageList);
//            gvGallery.setAdapter(galleryAdapter);
            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                    .getLayoutParams();
            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Pix.start(SelectImagesActivity.this, RequestCode, 5);
            } else {
                Toast.makeText(SelectImagesActivity.this, "Approve permissions to open ImagePicker", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploadImages(){
        for (int i = 0; i < imageList.size(); i++) {
            j++;

            File f = new File(imageList.get(i));
            Bitmap bitmap = new BitmapDrawable(SelectImagesActivity.this.getResources(), f.getAbsolutePath()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            final String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            String tag_json_obj = "json_obj_req";
            String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/UploadImages.php";

            pDialog = new ProgressDialog(SelectImagesActivity.this);
            pDialog.setMessage("Searching...");
            pDialog.setCancelable(false);
            pDialog.show();
            StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        JSONObject jsonObj = null;
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.e("IMAGES",response);
                                jsonObj= new JSONObject(response);
                                String success = jsonObj.getString("success");

                                if (success.equals("1")){
                                    if (j == imageList.size()){
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(resume, "1");
                                        editor.apply();
                                        Toast.makeText(SelectImagesActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                                        finish();
                                        pDialog.dismiss();
                                    }
                                }else {
                                    String message = jsonObj.getString("message");
                                    Toast.makeText(SelectImagesActivity.this, message, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SelectImagesActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("DisplayImage",encodedImage);
                    params.put("Name","image"+String.valueOf(j)+".png");
                    params.put("ClientID",clientID);
                    return params;
                }
            };
            int socketTimeout = 30000;//30 seconds
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
        }
    }

}
