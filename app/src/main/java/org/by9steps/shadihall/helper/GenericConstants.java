package org.by9steps.shadihall.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class GenericConstants {
    public static String MYEdittion = "MyEdition";
    ///////////////////////Check For Debug mode
    public static boolean IS_DEBUG_MODE_ENABLED = true;

    /////////////MYBugFixFlag
    public static String LOG_KEY_FOR_BUG_FIX = "MY Fix";
    public static String MYByPassForOTP = "My Fix";
    //////////////Null Field Standard Text For Server
    public static String NullFieldStandardText = "Null";

    public static String ByPASSForGetBooking = "ByPass";
    public static String MYEdiMenttion = "myEdition";
    public static String AmbigousStateTEmpForOnlyThiss="Ambigous ";


    public static void ShowDebugModeDialog(Context cc, String title, String mes) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cc);
        builder.setTitle(title).setMessage(mes);
        builder.create().show();
    }

    public static void ShowToastTem(Context cc, String click) {
        if (IS_DEBUG_MODE_ENABLED)
            Toast.makeText(cc, click, Toast.LENGTH_SHORT).show();
    }

    //Check Internet Connection
    public static boolean isConnected(Context mCtx) {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) mCtx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}
