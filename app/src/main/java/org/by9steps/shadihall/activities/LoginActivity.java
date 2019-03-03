package org.by9steps.shadihall.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.InputValidation;

import java.util.ArrayList;
import java.util.Random;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginBtn;
    TextInputLayout phoneLayout;
    TextInputEditText phoneNum;

    private InputValidation inputValidation;

    String ph;

    //sms
    ArrayList<PendingIntent> sentPI = new ArrayList<PendingIntent>();
    ArrayList<PendingIntent> deliveredPI = new ArrayList<PendingIntent>();
    BroadcastReceiver smsSentReceiver, smsDeliverReceiver;
    PendingIntent s, d;

    //shared prefrences
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String code = "otpKey";
    public static final String phone = "phoneKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputValidation = new InputValidation(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("Login");
        }

        //sms
        s = PendingIntent.getBroadcast(this, 0, new Intent("SENT"), 0);
        d = PendingIntent.getBroadcast(this, 0, new Intent("DELEIVERED"), 0);

        //shared prefrences
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        phoneNum = findViewById(R.id.login_number);
        phoneLayout = findViewById(R.id.phone_layout);
        loginBtn = findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                ph = phoneNum.getText().toString();

                if (!inputValidation.isInputEditTextFilled(phoneNum, phoneLayout, getString(R.string.error_message_phone))) {
                    return;
                } else {
                    try {

                        final int min = 2111;
                        final int max = 9999;
                        final int random = new Random().nextInt((max - min) + 1) + min;

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(code, String.valueOf(random));
                        editor.putString(phone, ph);
                        editor.apply();

                        SmsManager smsManager = SmsManager.getDefault();
                        String messageText = "Your verification code is:" + random;
                        Log.e("OTP CODE",String.valueOf(random));
                        ArrayList<String> mSMSMessage = smsManager.divideMessage(messageText);
                        for (int i = 0; i < mSMSMessage.size(); i++) {
                            sentPI.add(i, s);
                            deliveredPI.add(i, d);
                        }
                        smsManager.sendMultipartTextMessage(phoneNum.getText().toString(), null, mSMSMessage, sentPI, deliveredPI);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        smsSentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, OtpActivity.class));
                        finish();
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, OtpActivity.class));
                        finish();
                        break;

                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU!", Toast.LENGTH_SHORT).show();
                        break;

                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off!", Toast.LENGTH_SHORT).show();
                        break;

                }

            }
        };

        smsDeliverReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered!", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        registerReceiver(smsSentReceiver, new IntentFilter("SENT"));
        registerReceiver(smsDeliverReceiver, new IntentFilter("DELEIVERED"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliverReceiver);
    }
}
