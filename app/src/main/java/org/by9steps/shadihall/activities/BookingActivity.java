package org.by9steps.shadihall.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.fragments.BookingDetailFragment;
import org.by9steps.shadihall.fragments.BookingFormFragment;
import org.by9steps.shadihall.helper.ThemeProvider;

public class BookingActivity extends AppCompatActivity {

    String bookingID, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeProvider.setThemeOfApp(this);
        setContentView(R.layout.activity_booking);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Booking Detail");
        }

        Intent intent = getIntent();
        if (intent != null){
            bookingID = intent.getStringExtra("BookingID");
            type = intent.getStringExtra("Type");
        }

        if (type.equals("Detail")){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, BookingDetailFragment.newInstance(bookingID))
                    .commit();
        }else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, BookingFormFragment.newInstance("2019-01-01".toString(), bookingID,"No"))
                    .commit();
        }

    }

}
