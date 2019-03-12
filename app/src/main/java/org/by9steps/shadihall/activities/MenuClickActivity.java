package org.by9steps.shadihall.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.fragments.BookCalendarFragment;
import org.by9steps.shadihall.fragments.BookingFormFragment;
import org.by9steps.shadihall.fragments.CashBookFragment;
import org.by9steps.shadihall.fragments.ChartOfAccFragment;
import org.by9steps.shadihall.fragments.ListFragment;
import org.by9steps.shadihall.fragments.RecoveryFragment;
import org.by9steps.shadihall.fragments.ReportsFragment;
import org.by9steps.shadihall.fragments.TrailBalanceFragment;

public class MenuClickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_click);

        String message = "",position = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            message = extras.getString("message");
            position = extras.getString("position");
        }

        if (message.equals("Cash Book") && position.equals("0")){
            Log.e("Position",position);
            Log.e("Message",message);
        }

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(message);
        }

        if (message.equals("Booking") && position.equals("0")) {
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
        }else if (message.equals("Cash Book") && position.equals("0")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, new ListFragment())
                    .commit();
        }else if (message.equals("ChartOfAcc")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, new ChartOfAccFragment())
                    .commit();
        }
        //Reports
        else if (message.equals("Cash Book")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, new CashBookFragment())
                    .commit();
        }else if (message.equals("Booking")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, new ListFragment())
                    .commit();
        }else if (message.equals("Cash and Bank")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, ReportsFragment.newInstance("0"))
                    .commit();
        }else if (message.equals("Employee")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, ReportsFragment.newInstance("1"))
                    .commit();
        }else if (message.equals("Expense")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, ReportsFragment.newInstance("2"))
                    .commit();
        }else if (message.equals("Fixed Asset")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, ReportsFragment.newInstance("3"))
                    .commit();
        }else if (message.equals("Supplier")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, ReportsFragment.newInstance("5"))
                    .commit();
        }else if (message.equals("Client")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, ReportsFragment.newInstance("4"))
                    .commit();
        }else if (message.equals("Revenue")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, ReportsFragment.newInstance("6"))
                    .commit();
        }else if (message.equals("Capital")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, ReportsFragment.newInstance("7"))
                    .commit();
        }else if (message.equals("Website")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, new ListFragment())
                    .commit();
        }else if (message.equals("Trail Balance")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, new TrailBalanceFragment())
                    .commit();
        }else if (message.equals("Profit/Loss")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, new ListFragment())
                    .commit();
        }else if (message.equals("Bal Sheet")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.menu_container, new ListFragment())
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
