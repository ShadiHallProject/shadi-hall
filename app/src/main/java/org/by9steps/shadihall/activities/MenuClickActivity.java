package org.by9steps.shadihall.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.fragments.BookCalendarFragment;
import org.by9steps.shadihall.fragments.ChartOfAccFragment;
import org.by9steps.shadihall.fragments.ListFragment;
import org.by9steps.shadihall.fragments.RecoveryFragment;

public class MenuClickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_click);

        String message = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            message = extras.getString("message");
        }

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(message);
        }

        if (message.equals("Booking")) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, new BookCalendarFragment())
                    .commit();
        }else if (message.equals("Recovery")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, new RecoveryFragment())
                    .commit();
        }else if (message.equals("Web Editing")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, new ListFragment())
                    .commit();
        }else if (message.equals("Cash Book")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, new ListFragment())
                    .commit();
        }else if (message.equals("ChartOfAcc")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, new ChartOfAccFragment())
                    .commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }
}
