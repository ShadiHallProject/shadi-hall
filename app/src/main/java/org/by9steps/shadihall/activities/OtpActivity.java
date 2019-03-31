package org.by9steps.shadihall.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.orm.SugarContext;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.InputValidation;

import java.util.ArrayList;
import java.util.Random;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private String mVerificationId = "";
    public boolean mVerificationInProgress = false;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;


    TextInputLayout otpLayout;
    TextInputEditText otpcode;
    Button otpBtn, resendOtp;
    ProgressDialog progressDialog;

    String otp, savecode, ph, phonenum;
    private InputValidation inputValidation;

    //shared prefrences
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String code = "otpKey";
    public static final String phone = "phoneKey";
    public static final String login = "loginKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        SugarContext.init(this);

        inputValidation = new InputValidation(this);
        progressDialog = new ProgressDialog(this);
        initFireBaseCallbacks();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Otp");
        }

        //shared prefrences
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        otpcode = findViewById(R.id.otpcode);
        otpLayout = findViewById(R.id.otp_layout);
        otpBtn = findViewById(R.id.otp_btn);
        resendOtp = findViewById(R.id.resend_otp);
        otpBtn.setOnClickListener(this);
        resendOtp.setOnClickListener(this);

        if (sharedPreferences.contains(phone))
            ph = sharedPreferences.getString(phone, "");
        phonenum = getIntent().getStringExtra("PHONE");
        sendVerificationCode(phonenum);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.otp_btn:
                otp = otpcode.getText().toString();

                if (!inputValidation.isInputEditTextFilled(otpcode, otpLayout, getString(R.string.error_message_otp))) {
                    return;
                } else {

                    verifyVerificationCode(otp);

                }

                break;
            case R.id.resend_otp:
                if (sharedPreferences.contains(phone))
                    ph = sharedPreferences.getString(phone, "");
                sendVerificationCode(ph);

                break;
        }
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

    void initFireBaseCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Toast.makeText(OtpActivity.this, "Verification Complete", Toast.LENGTH_SHORT).show();
                //Getting the code sent by SMS
                String code = credential.getSmsCode();

                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {
                    otpcode.setText(code);
                    //verifying the code
                    verifyVerificationCode(code);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(OtpActivity.this, "Verification Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Toast.makeText(OtpActivity.this, "Code Sent", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                mVerificationId = verificationId;
                mResendToken = token;


            }
        };
    }


    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+92"+mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private void verifyVerificationCode(String code) {
        progressDialog.setTitle("Otp verification");
        progressDialog.setMessage("Please wait while we check your credentials");
        progressDialog.show();
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(OtpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            Intent intent = new Intent(OtpActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            progressDialog.dismiss();

                        } else {

                            //verification unsuccessful.. display an error message
                            progressDialog.dismiss();
                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }
}
