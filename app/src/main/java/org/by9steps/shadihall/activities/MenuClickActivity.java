package org.by9steps.shadihall.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.chartofaccountdialog.CustomDialogOnDismisListener;
import org.by9steps.shadihall.fragments.BalSheetFragment;
import org.by9steps.shadihall.fragments.BookCalendarFragment;
import org.by9steps.shadihall.fragments.CashBookFragment;
import org.by9steps.shadihall.fragments.ChartOfAccFragment;
import org.by9steps.shadihall.fragments.DateBalSheetFragment;
import org.by9steps.shadihall.fragments.DateProfitLossFragment;
import org.by9steps.shadihall.fragments.ListFragment;
import org.by9steps.shadihall.fragments.MonthBalSheetFragment;
import org.by9steps.shadihall.fragments.MonthTrialBalance;
import org.by9steps.shadihall.fragments.ProfitLossFragment;
import org.by9steps.shadihall.fragments.RecoveryFragment;
import org.by9steps.shadihall.fragments.ReportsFragment;
import org.by9steps.shadihall.fragments.SalePur1Fragment;
import org.by9steps.shadihall.fragments.SummerizeTrailBalFragment;
import org.by9steps.shadihall.fragments.TrailBalanceFragment;
import org.by9steps.shadihall.fragments.YearProfitLossFragment;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.helper.Prefrence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuClickActivity extends AppCompatActivity implements CustomDialogOnDismisListener {

    String currentDate;
    DatabaseHelper databaseHelper;
    //////////////////////listener for Change
    ReportsFragment reportsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(this.getClass().getName(),"ClientUserID"+new Prefrence(this).getClientUserIDSession());
        //Log.e(this.getClass().getName(),"ClientUserIDMY"+new Prefrence(this).getMYClientUserIDSession());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_click);

        databaseHelper = new DatabaseHelper(this);

        Date date = new Date();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = curFormater.format(date);

        String title = "", message = "",ValuePass = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title = extras.getString("title");
            message = extras.getString("message");
            ValuePass = extras.getString("position");
        }

//        if (AppController.internet.equals("Yes")){
//            getAccount3Name();
//        }


        if (savedInstanceState == null) {

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(title);
            }

            if (message.equals("SalPur1")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new SalePur1Fragment())
                        .commit();
            } else if (message.equals("Booking")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new BookCalendarFragment())
                        .commit();
            } else if (message.equals("Recovery")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new RecoveryFragment())
                        .commit();
            } else if (message.equals("ChartOfAcc")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new ChartOfAccFragment())
                        .commit();
            }
            //Reports
            else if (message.equals("CashBook")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new CashBookFragment())
                        .commit();
            } else if (message.equals("AccountEntry")) {
                reportsFragment=ReportsFragment.newInstance(ValuePass);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, reportsFragment)
                        .commit();
            } else if (message.equals("TreeView")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new ChartOfAccFragment())
                        .commit();
            } else if (message.equals("CompletTrailBalance")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new ChartOfAccFragment())
                        .commit();
            } else if (message.equals("MonthWiseTrailBalance")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new SummerizeTrailBalFragment())
                        .commit();
            } else if (message.equals("YearlyTrailBalance")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new MonthTrialBalance())
                        .commit();
            } else if (message.equals("P/LStatmentBooking")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new RecoveryFragment())
                        .commit();
            } else if (message.equals("P/LStatmentDateWise")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new DateProfitLossFragment())
                        .commit();
            } else if (message.equals("P/LStatmentyearly")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new YearProfitLossFragment())
                        .commit();
            } else if (message.equals("BalanceSheetDate")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new DateBalSheetFragment())
                        .commit();
            } else if (message.equals("BalanceSheetMonth")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new MonthBalSheetFragment())
                        .commit();
            }

        }
    }


    @Override
    public void onDismissListener(String key) {
        reportsFragment.getReports();
        reportsFragment.filter = 0;
        reportsFragment.searchView.setQuery("", false);
        reportsFragment.searchView.clearFocus();
    }
}
