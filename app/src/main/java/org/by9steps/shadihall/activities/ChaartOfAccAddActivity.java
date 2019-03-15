package org.by9steps.shadihall.activities;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.by9steps.shadihall.R;

public class ChaartOfAccAddActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    TextInputLayout name_layout;
    TextInputEditText name;
    TextInputLayout address_layout;
    TextInputEditText address;
    TextInputLayout mobile_layout;
    TextInputEditText mobile;
    TextInputLayout email_layout;
    TextInputEditText email;
    TextInputLayout salary_layout;
    TextInputEditText salary;
    TextInputLayout login_mobile_layout;
    TextInputEditText login_mobile;
    TextInputLayout password_layout;
    TextInputEditText password;
    Spinner spinner;
    Button add;

    int spPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chaart_of_acc_add);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add");
        }

        name_layout = findViewById(R.id.name_layout);
        name = findViewById(R.id.name);
        address_layout = findViewById(R.id.address_layout);
        address = findViewById(R.id.address);
        mobile_layout = findViewById(R.id.mobile_layout);
        mobile = findViewById(R.id.mobile);
        email_layout = findViewById(R.id.email_layout);
        email = findViewById(R.id.email);
        salary_layout = findViewById(R.id.salary_layout);
        salary = findViewById(R.id.salary);
        login_mobile_layout = findViewById(R.id.login_number_layout);
        login_mobile = findViewById(R.id.login_number);
        password_layout = findViewById(R.id.password_layout);
        password = findViewById(R.id.password);
        spinner = findViewById(R.id.spinner);
        add = findViewById(R.id.add);

        add.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);

        String[] sp_items = {"Not Allowed To Login","Admin","Custom Rights"};
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sp_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spPosition = position;
        Log.e("Position",String.valueOf(spPosition));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
