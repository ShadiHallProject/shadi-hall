package org.by9steps.shadihall.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.orm.SugarContext;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.chartofaccountdialog.ProjectMenuDialog;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.ViewDBAllData;
import org.by9steps.shadihall.helper.refdb;
import org.by9steps.shadihall.model.ProjectMenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;


public class SplashActivity extends AppCompatActivity {

    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    String rationale = "Please provide location permission so that you can ...";
    Permissions.Options options = new Permissions.Options()
            .setRationaleDialogTitle("Info")
            .setSettingsDialogTitle("Warning");

    //shared prefrences
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String login = "loginKey";

    ImageView splash_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SugarContext.init(this);
///

        String iddst= refdb.Account3NameTableFun.UpdateAcNameIDInCashBook(
                                            new DatabaseHelper(this),
                                            "115",
                                            "-2527",
                                            "999999");
        Log.e("observeme",iddst);
        Fabric.with(this, new Crashlytics());
        try {
            AppController.compareDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //shared prefrences
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        splash_logo = findViewById(R.id.splash_icon);
        final Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.blink);

        Permissions.check(this, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                splash_logo.startAnimation(animation);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String s = "No";

                        if(sharedPreferences.contains(login)){
                            s = sharedPreferences.getString(login,"");
                        }

                        if (s.equals("Yes")) {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }else{
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }
//                        try {
//                            File file=getDatabasePath("EasySoftDataFile.db");
//                            Log.e("ddaaptSource",file.toString()+" is exist:"+file.exists());
//
//                           //File targetloc= getDir(Environment.getExternalStorageDirectory().getAbsolutePath());
//                          //  File targetloc= getFilesDir();
//                            File docsFolder = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES) + "");
//                            if (!docsFolder.exists()) {
//                                docsFolder.mkdir();
//                            }
//                            File ff=new File(docsFolder,"EasySoftDataFile");
//                            //if(!ff.exists()){
//                               // ff.mkdir();
//                                 boolean creted=ff.mkdir();
//                            Log.e("ddaaptDesti","File Created Status:"+creted);
//                         //   }
//                            Log.e("ddaaptDesti",ff.toString());
//                            movefile(file,ff);
//
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }

                      //  startActivity(new Intent(SplashActivity.this, ViewDBAllData.class));

                    }
                },3000);
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                finish();
            }
        });
//        Crashlytics.getInstance().crash();
    }

    private void movefile(File file, File targetloc) throws Exception {
        InputStream in = new FileInputStream(file);
        OutputStream out = new FileOutputStream(targetloc);

        // Copy the bits from instream to outstream
        byte[] buf = new byte[1024];
        int len;

        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        in.close();
        out.close();
    }

}
