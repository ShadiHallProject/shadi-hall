package org.by9steps.shadihall.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import java.util.concurrent.TimeUnit;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

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

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(phone, ph);
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                    intent.putExtra("PHONE",ph);
                    startActivity(intent);

                }
                break;
        }
    }

}
