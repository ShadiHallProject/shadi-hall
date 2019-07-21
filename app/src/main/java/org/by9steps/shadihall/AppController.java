package org.by9steps.shadihall;

import android.app.Application;
import android.app.ProgressDialog;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.by9steps.shadihall.helper.DatabaseHelper;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static AppController mInstance;
    public static String profileType = "Edit";
    public static String addCB = "View";
    public static String date = "";
    public static String internet = "No";
    public static String fDate1 = "";
    public static String fDate2 = "";

    public static String imageUrl = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/ClientImages/";

    DatabaseHelper databaseHelper;
    ProgressDialog mProgress;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    
    public static String stringDateFormate(String sFormate, String dFormate, String date){
        String mDate = null;
        SimpleDateFormat sf = new SimpleDateFormat(sFormate);
        try {
            Date dd = sf.parse(date);
            SimpleDateFormat ss = new SimpleDateFormat(dFormate);
            mDate = ss.format(dd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mDate;
    }

}
