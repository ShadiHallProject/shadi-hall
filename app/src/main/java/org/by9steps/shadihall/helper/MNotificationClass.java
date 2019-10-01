package org.by9steps.shadihall.helper;

import android.content.Context;
import android.widget.Toast;

public class MNotificationClass {
    public static void ShowToastTem(Context cc, String click){
        Toast.makeText(cc, click, Toast.LENGTH_SHORT).show();
    }
    public static void ShowToast(Context cc, String click){
        Toast.makeText(cc, click, Toast.LENGTH_SHORT).show();
    }
}
