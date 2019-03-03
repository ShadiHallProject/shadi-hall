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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.orm.SugarContext;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.InputValidation;

import java.util.ArrayList;
import java.util.Random;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout otpLayout;
    TextInputEditText otpcode;
    Button otpBtn, resendOtp;
    ProgressDialog pDialog;

    String otp, savecode, ph;
    private InputValidation inputValidation;

    //shared prefrences
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String code = "otpKey";
    public static final String phone = "phoneKey";
    public static final String login = "loginKey";

    //sms
    ArrayList<PendingIntent> sentPI = new ArrayList<PendingIntent>();
    ArrayList<PendingIntent> deliveredPI = new ArrayList<PendingIntent>();
    BroadcastReceiver smsSentReceiver, smsDeliverReceiver;
    PendingIntent s, d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        SugarContext.init(this);

        inputValidation = new InputValidation(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Otp");
        }

        //sms
        s = PendingIntent.getBroadcast(this, 0, new Intent("SENT"), 0);
        d = PendingIntent.getBroadcast(this, 0, new Intent("DELEIVERED"), 0);

        //shared prefrences
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        otpcode = findViewById(R.id.otpcode);
        otpLayout = findViewById(R.id.otp_layout);
        otpBtn = findViewById(R.id.otp_btn);
        resendOtp = findViewById(R.id.resend_otp);
        otpBtn.setOnClickListener(this);
        resendOtp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.otp_btn:
                otp = otpcode.getText().toString();

                if (!inputValidation.isInputEditTextFilled(otpcode, otpLayout, getString(R.string.error_message_otp))) {
                    return;
                } else {
                    if (sharedPreferences.contains(code))
                        savecode = sharedPreferences.getString(code, "");

                    if (otp.equals(savecode)) {
                            if (sharedPreferences.contains(code))
                                ph = sharedPreferences.getString(phone, "");
                            startActivity(new Intent(OtpActivity.this, MainActivity.class));
                            finish();
                    } else {
                        Toast.makeText(this, "Otp does not match! Please enter correct otp.", Toast.LENGTH_SHORT).show();
                        resendOtp.setVisibility(View.VISIBLE);
                    }
                }

                break;
            case R.id.resend_otp:
                try {
                    if (sharedPreferences.contains(code))
                        ph = sharedPreferences.getString(phone, "");
                    final int min = 2111;
                    final int max = 9999;
                    final int random = new Random().nextInt((max - min) + 1) + min;

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(code, String.valueOf(random));
                    editor.apply();


                    SmsManager smsManager = SmsManager.getDefault();
                    String messageText = "Your verification code is:" + random;
                    ArrayList<String> mSMSMessage = smsManager.divideMessage(messageText);
                    for (int i = 0; i < mSMSMessage.size(); i++) {
                        sentPI.add(i, s);
                        deliveredPI.add(i, d);
                    }
                    smsManager.sendMultipartTextMessage(ph, null, mSMSMessage, sentPI, deliveredPI);
                } catch (Exception e) {
                    e.printStackTrace();
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
                        break;

                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure!", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
