package org.by9steps.shadihall.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.fragments.DateBalSheetFragment;
import org.by9steps.shadihall.fragments.DateProfitLossFragment;
import org.by9steps.shadihall.fragments.ListFragment;
import org.by9steps.shadihall.fragments.MonthBalSheetFragment;
import org.by9steps.shadihall.fragments.RecoveryFragment;
import org.by9steps.shadihall.fragments.YearProfitLossFragment;

public class ProfitLossActivity extends AppCompatActivity {

    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit_loss);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Profit and Loss");
        }

        Intent intent = getIntent();
        if (intent != null) {
            message = intent.getStringExtra("message");
        }

        if (message.equals("1")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RecoveryFragment())
                    .commit();
        }else if (message.equals("2")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DateProfitLossFragment())
                    .commit();
        }else if (message.equals("3")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new YearProfitLossFragment())
                    .commit();
        }
    }
}
