package org.by9steps.shadihall.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;

import org.by9steps.shadihall.R;
import org.by9steps.shadihall.helper.ThemeProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.squareup.timessquare.CalendarPickerView.SelectionMode.SINGLE;


public class DetailCalendarActivity extends AppCompatActivity {

    List<Date> dateList = new ArrayList<>();
    List<Date> BookingList = new ArrayList<>();
    CalendarPickerView calendar;
    Date today;
    Calendar LastYear, nextYear;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeProvider.setThemeOfApp(this);
        setContentView(R.layout.activity_detail_calendar);
        String message = "";
        //   initializeCalendar();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            message = extras.getString("message");
        }

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(message);
        }

        LastYear = Calendar.getInstance();
        LastYear.add(Calendar.YEAR, -1);
        nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        today = new Date();
        getEventDates();
//https://github.com/square/android-times-square/blob/master/sample/src/main/java/com/squareup/timessquare/sample/SampleTimesSquareActivity.java
//https://github.com/wangjiegulu/privatedoctorlibs/blob/master/projects/android-times-square-idle_wu-lib/src/com/squareup/timessquare/CalendarPickerView.java
        calendar = (CalendarPickerView) findViewById(R.id.calendar);
//        calendar.init(LastYear.getTime(), nextYear.getTime()).withSelectedDate(today);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.YEAR, 2019);
        today = c.getTime();
        c.set(Calendar.DAY_OF_MONTH, 31);
        c.set(Calendar.MONTH, 11);
        c.set(Calendar.YEAR, 2020);
        nextYear.setTime(c.getTime());
        calendar.init(today, nextYear.getTime())
                .withSelectedDate(new Date()).inMode(SINGLE);


        //        c.setTime(new Date());
        calendar.setCustomDayView(new DefaultDayViewAdapter());
        calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());
//        dateList.add(today);
//        dateList.add(c.getTime());
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                //     String selecteddate= DateFormat.getInstance().format(DateFormat.FULL).format(String.valueOf(date));
                Boolean found = false;
                Calendar calselected = Calendar.getInstance();
                calselected.setTime(date);
                for (int i = 0; i < dateList.size(); i++) {
                    if (dateList.get(i).equals(calselected.getTime())) {
                        found = true;
                    }
                }
                if (date.before(new Date())) {
                    Toast.makeText(getApplicationContext(), "Not Allowed To Order On This Date", Toast.LENGTH_LONG).show();
                } else if (found) {
                    Toast.makeText(getApplicationContext(), "Event Already Booked \n" + calselected.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_LONG).show();
                } else {
                    String selectedDate = calselected.get(Calendar.DAY_OF_MONTH) + "-" + calselected.get(Calendar.MONTH) + "-" + calselected.get(Calendar.YEAR);
                    Toast.makeText(DetailCalendarActivity.this, "Click is\n" + selectedDate, Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(DetailCalendarActivity.this, RequestForm.class);
//                    intent.putExtra("message", selectedDate);
//                    startActivity(intent);
                }
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

    }

    private void getEventDates() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://shadihall.easysoft.com.pk/phpapi/BookingRequestCalender.php";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //     mTextView.setText("Response is: "+ response.substring(0,500));
                        String text = "";
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray json2 = json.getJSONArray("Booking");
                            for (int k = 0; k < json2.length(); k++) {
                                text = json2.get(k).toString();
                                JSONObject obj = new JSONObject(text);
                                Calendar cal = Calendar.getInstance();
                                text = obj.getString("EventDate");
                                obj = new JSONObject(text);
                                text = obj.getString("date");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = sdf.parse(text);
                                cal.setTime(date);
                                int day = cal.get(Calendar.DAY_OF_MONTH);
                                if (day == 25) {
                                    Toast.makeText(DetailCalendarActivity.this, "Day is\n" + day, Toast.LENGTH_LONG).show();
                                }
                                if (between(cal.getTime(), today, nextYear.getTime()) == true) {
                                    dateList.add(cal.getTime());
                                }
                            }
                            //                            getDisabledDays().add(cal);
//                            calendarView.setDisabledDays(getDisabledDays());
                            calendar.highlightDates(dateList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailCalendarActivity.this, "Server Error\nIssue Will Be Resolved Soon\nPlease Retry After Few Minutes", Toast.LENGTH_LONG).show();
                            finish();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailCalendarActivity.this, "Server Error\nIssue Will Be Resolved Soon\nPlease Retry After Few Minutes", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        Toast.makeText(DetailCalendarActivity.this, "Response is\n" + text, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    mTextView.setText("That didn't work!");
                Toast.makeText(DetailCalendarActivity.this, "Server Error\nIssue Will Be Resolved Soon\nPlease Retry After Few Minutes", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        queue.add(stringRequest);
    }

    public static boolean between(Date date, Date dateStart, Date dateEnd) {
        if (date != null && dateStart != null && dateEnd != null) {
            if (date.after(dateStart) && date.before(dateEnd)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

}
