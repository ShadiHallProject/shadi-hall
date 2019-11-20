package org.by9steps.shadihall.callingapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.by9steps.shadihall.AppController;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.by9steps.shadihall.helper.ApiRefStrings.UrlForUploadClientRefrenceImagesImagesOnCloud;

public class ImageUploadingApi {

    public interface ImageUploadingImage {
        void FinishCallBackmethod(String success, String funType);
    }

    public static void uploadImageToCloud(final Context context, final String cliid, String img, ImageView imageView, Uri uri, final ImageUploadingImage callback) {


//            Bitmap bitmap = new BitmapDrawable(RegisterActivity.this.getResources(), f.getAbsolutePath()).getBitmap();
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//            final String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
//
//

        final String imgname = "image" + img;
        Log.e("uritrack",uri.toString());
//        imageView.setDrawingCacheEnabled(true);
//        imageView.buildDrawingCache();
//        Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//        final String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        File f = new File(uri.toString());
        //  Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
        Bitmap bitmap = new BitmapDrawable(context.getResources(), f.getAbsolutePath()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        final String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),
                Base64.DEFAULT);

        String tag_json_obj = "json_obj_req";
        String url = UrlForUploadClientRefrenceImagesImagesOnCloud;


        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    JSONObject jsonObj = null;

                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("IMAGESREsponse" + imgname, response);
                            jsonObj = new JSONObject(response);
                            String success = jsonObj.getString("success");

                            if (success.equals("1")) {
                                callback.FinishCallBackmethod(success, "success");
//                                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                                        editor.putString(resume, "1");
//                                        editor.apply();
//                                        Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
//                                        finish();


                            } else {
                                String message = jsonObj.getString("message");
                                callback.FinishCallBackmethod(success, message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Error", error.toString());
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Log.e("ImageDetalisForaUp","---"+cliid+"--"+imgname);
                Map<String, String> params = new HashMap<String, String>();
                params.put("DisplayImage", encodedImage);
                params.put("Name", imgname + ".png");
                params.put("ClientID", cliid);
                return params;
            }
        };
        int socketTimeout = 30000;//30 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

}
