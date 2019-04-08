package org.by9steps.shadihall.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;


import org.by9steps.shadihall.AppController;
import org.by9steps.shadihall.R;
import org.by9steps.shadihall.activities.MainActivity;
import org.by9steps.shadihall.activities.RegisterActivity;
import org.by9steps.shadihall.helper.DatabaseHelper;
import org.by9steps.shadihall.model.Bookings;
import org.by9steps.shadihall.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Map;

import static com.squareup.timessquare.CalendarPickerView.SelectionMode.SINGLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookCalendarFragment extends Fragment {

    DatabaseHelper databaseHelper;
    CalendarPickerView calendar;
    List<Date> dateList = new ArrayList<>();
    List<Bookings> bookingList;

    Date today;
    Calendar LastYear, nextYear;
    String pattern="yyyy-MM-dd";
    ProgressDialog pDialog;
    int day, month, year;

    public BookCalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_calendar, container, false);
        setHasOptionsMenu(true);

        calendar = view.findViewById(R.id.calendar);
        databaseHelper = new DatabaseHelper(getContext());

        String query = "";
        List<User> list = User.listAll(User.class);
        for (User u: list) {
            query = "SELECT * FROM Booking WHERE ClientID =" + u.getClientID();
        }
        bookingList = databaseHelper.getBookings(query);

        LastYear = Calendar.getInstance();
        LastYear.add(Calendar.YEAR, -1);
        nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        today = new Date();

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

        calendar.setCustomDayView(new DefaultDayViewAdapter());
        calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());

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
                if (found) {
                    day = calselected.get(Calendar.DAY_OF_MONTH);
                    month = calselected.get(Calendar.MONTH)+1;
                    year = calselected.get(Calendar.YEAR);
                    String selectedDate = day + "-" + month + "-" + year;
//                    Toast.makeText(getContext(), "Click is\n" + selectedDate, Toast.LENGTH_LONG).show();

                    DateFormat df = new SimpleDateFormat(pattern);
                    selectedDate= df.format(calendar.getSelectedDate());
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.menu_container, BookingDetailFragment.newInstance(selectedDate))
                            .addToBackStack(null)
                            .commit();

                    Toast.makeText(getContext(), selectedDate+"\nEvent Already Booked \n" + calselected.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
                } else {
                    //new Activity
                    day = calselected.get(Calendar.DAY_OF_MONTH);
                    month = calselected.get(Calendar.MONTH)+1;
                    year = calselected.get(Calendar.YEAR);
                    String selectedDate = day + "-" + month + "-" + year;
//                    Toast.makeText(getContext(), "Click is\n" + selectedDate, Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.menu_container, BookingFormFragment.newInstance(selectedDate))
                            .addToBackStack(null)
                            .commit();
//                    Intent intent = new Intent(BookCalender.this, BookingForm.class);
//                    intent.putExtra("message", selectedDate);
//                    startActivity(intent);
                }
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });

        FetchFromDb();

        return  view;
    }


    public void FetchFromDb() {
        dateList = new ArrayList<>();

//        List<Bookings> list = Bookings.listAll(Bookings.class);

        for (Bookings book : bookingList) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date date = null;
            try {
                date = sdf.parse(book.getEventDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if (between(cal.getTime(), today, nextYear.getTime())) {
                dateList.add(cal.getTime());
            }
        }
        // mDatabase.close();
        calendar.highlightDates(dateList);

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

    public void getBookings(){
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String url = "http://69.167.137.121/plesk-site-preview/sky.com.pk/shadiHall/GetBookings.php";

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Searching...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String text = "", BookingID = "", ClientName = "", ClientMobile = "", ClientAddress = "", ClientNic = "", EventName = "", BookingDate = "", EventDate = "",
                                ChargesTotal = "",Description = "", ClientID ="", ClientUserID = "";
                        try {
                            JSONObject json = new JSONObject(response);
                            String success = json.getString("success");
                            Log.e("Response",success);

                            if (success.equals("1")) {
                                JSONArray jsonArray = json.getJSONArray("Bookings");
                                Log.e("SSSS", jsonArray.toString());
//                                Bookings.deleteAll(Bookings.class);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    BookingID = jsonObject.getString("BookingID");
                                    ClientName = jsonObject.getString("ClientName");
                                    ClientMobile = jsonObject.getString("ClientMobile");
                                    ClientAddress = jsonObject.getString("ClientAddress");
                                    ClientNic = jsonObject.getString("ClientNic");
                                    EventName = jsonObject.getString("EventName");
                                    BookingDate = jsonObject.getString("BookingDate");
                                    String ed = jsonObject.getString("EventDate");
                                    JSONObject jb = new JSONObject(ed);
                                    EventDate = jb.getString("date");
                                    Log.e("EVENTDATE",EventDate);
                                    ChargesTotal = jsonObject.getString("ChargesTotal");
                                    Description = jsonObject.getString("Description");
                                    ClientID = jsonObject.getString("ClientID");
                                    ClientUserID = jsonObject.getString("ClientUserID");

                                    Log.e("SSSS",ClientName + ClientID);

//                                    Bookings bookings = new Bookings(BookingID,ClientName,ClientMobile,ClientAddress,ClientNic,EventName,BookingDate,EventDate,ChargesTotal,Description,ClientID,ClientUserID);
//                                    bookings.save();
                                }

                                FetchFromDb();
                                pDialog.dismiss();
                            }else {
                                pDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
                pDialog.dismiss();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                List<User> list = User.listAll(User.class);
                for (User u: list) {
                    params.put("ClientID", u.getClientID());
                }
                return params;
            }
        };

        int socketTimeout = 10000;//10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            getActivity().onBackPressed();
        }
        return true;
    }

}
