package org.by9steps.shadihall.activities;

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
import org.by9steps.shadihall.fragments.BalSheetFragment;
import org.by9steps.shadihall.fragments.BookCalendarFragment;
import org.by9steps.shadihall.fragments.CashBookFragment;
import org.by9steps.shadihall.fragments.ChartOfAccFragment;
import org.by9steps.shadihall.fragments.ListFragment;
import org.by9steps.shadihall.fragments.ProfitLossFragment;
import org.by9steps.shadihall.fragments.RecoveryFragment;
import org.by9steps.shadihall.fragments.ReportsFragment;
import org.by9steps.shadihall.fragments.TrailBalanceFragment;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.Account1Type;
import org.by9steps.shadihall.model.Account2Group;
import org.by9steps.shadihall.model.Account3Name;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.CashBook;
import org.by9steps.shadihall.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuClickActivity extends AppCompatActivity {

    String currentDate;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_click);

        databaseHelper = new DatabaseHelper(this);

        Date date = new Date();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = curFormater.format(date);

        String message = "",position = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            message = extras.getString("message");
            position = extras.getString("position");
        }

//        if (AppController.internet.equals("Yes")){
//            getAccount3Name();
//        }


        if (savedInstanceState == null) {
            Log.e("MESSAGE",message);
            if (message.equals("Cash Book") && position.equals("0")) {
                Log.e("Position", position);
                Log.e("Message", message);
            }

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(message);
            }

            if (message.equals("Booking") && position.equals("0")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new BookCalendarFragment())
                        .commit();
            } else if (message.equals("Recovery")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new RecoveryFragment())
                        .commit();
            } else if (message.equals("Web Editing")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new ListFragment())
                        .commit();
            } else if (message.equals("Cash Book") && position.equals("0")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new ListFragment())
                        .commit();
            } else if (message.equals("ChartOfAcc")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new ChartOfAccFragment())
                        .commit();
            }
            //Reports
            else if (message.equals("Cash Book")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new CashBookFragment())
                        .commit();
            } else if (message.equals("Booking")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new ListFragment())
                        .commit();
            } else if (message.equals("Incom")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, ReportsFragment.newInstance("6"))
                        .commit();
            }else if (message.equals("Cash And Bank")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, ReportsFragment.newInstance("0"))
                        .commit();
            } else if (message.equals("Employee")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, ReportsFragment.newInstance("1"))
                        .commit();
            } else if (message.equals("General Expense")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, ReportsFragment.newInstance("2"))
                        .commit();
            } else if (message.equals("Fixed Asset")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, ReportsFragment.newInstance("3"))
                        .commit();
            } else if (message.equals("Supplier")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, ReportsFragment.newInstance("5"))
                        .commit();
            } else if (message.equals("Client")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, ReportsFragment.newInstance("4"))
                        .commit();
            } else if (message.equals("Revenue")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, ReportsFragment.newInstance("6"))
                        .commit();
            } else if (message.equals("Capital")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, ReportsFragment.newInstance("7"))
                        .commit();
            } else if (message.equals("Website")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new ListFragment())
                        .commit();
            } else if (message.equals("Trail Balance")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new TrailBalanceFragment())
                        .commit();
            } else if (message.equals("Profit/Loss")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new ProfitLossFragment())
                        .commit();
            } else if (message.equals("Bal Sheet")) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.menu_container, new BalSheetFragment())
                        .commit();
            }

        }
    }


}
