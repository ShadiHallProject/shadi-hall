package org.by9steps.shadihall.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.InputValidation;

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
