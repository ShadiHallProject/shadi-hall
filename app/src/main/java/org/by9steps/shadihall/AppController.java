package org.by9steps.shadihall;

import android.app.Application;
import android.app.ProgressDialog;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.by9steps.shadihall.helper.ApiRefStrings;
import org.by9steps.shadihall.helper.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public static String imageUrl = ApiRefStrings.ServerAddress + "PhpApi/ClientImages/";

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

    public static String stringDateFormate(String sFormate, String dFormate, String date) {
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


    public static boolean compareDate() throws ParseException {
        String valid_until = "5/12/2019";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date validtill = sdf.parse(valid_until);
        Date c = Calendar.getInstance().getTime();
///////////////////////////////////Second DAte
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        String untilformated = df.format(validtill);
        Log.e("formdate", formattedDate + " until:" + untilformated + " compare:" + c.after(validtill));
       // return c.after(validtill);
        return true;
    }

    public String getTodayDate() {
        Date c = Calendar.getInstance().getTime();
///////////////////////////////////Second DAte
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        return formattedDate;
    }
}
