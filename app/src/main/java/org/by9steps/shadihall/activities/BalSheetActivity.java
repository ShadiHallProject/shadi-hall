package org.by9steps.shadihall.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.fragments.DateBalSheetFragment;
import org.by9steps.shadihall.fragments.ListFragment;
import org.by9steps.shadihall.fragments.MonthBalSheetFragment;
import org.by9steps.shadihall.fragments.SummerizeTrailBalFragment;
import org.by9steps.shadihall.helper.ThemeProvider;

public class BalSheetActivity extends AppCompatActivity {

    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeProvider.setThemeOfApp(this);
        setContentView(R.layout.activity_bal_sheet);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Balance Sheet");
        }

        Intent intent = getIntent();
        if (intent != null) {
            message = intent.getStringExtra("message");
        }

        if (message.equals("1")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DateBalSheetFragment())
                    .commit();
        }else if (message.equals("2")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MonthBalSheetFragment())
                    .commit();
        }
    }

}
